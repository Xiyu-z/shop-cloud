package com.shop.order.controller;

import com.shop.core.result.R;
import com.shop.order.entity.Xaw168Order;
import com.shop.order.feign.Xaw168GoodsFeign;
import com.shop.order.service.Xaw168OrderService;
import com.shop.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * 订单 Controller - xaw168 下单接口
 */
@RestController
@RequestMapping("/xaw168/order")
public class Xaw168OrderController {

    @Autowired
    private Xaw168OrderService orderService;

    @Autowired
    private Xaw168GoodsFeign xaw168GoodsFeign;

    /**
     * 下单接口
     * 创建订单记录，并调用 goods 模块扣减库存
     */
    @PostMapping
    public R<?> placeOrder(@RequestBody Xaw168Order item) {
        // 自动生成订单编号
        String orderNo = "DD-" + UUID.randomUUID().toString().replaceAll("-", "");
        item.setOrderNo(orderNo);
        // 状态默认为已下单
        item.setStatus("0");
        // 自动填充创建人
        item.setCreateBy(SecurityUtils.getUserId());
        // 保存订单
        orderService.save(item);

        // 调用 goods 模块扣减库存
        R<?> reduceResult = xaw168GoodsFeign.reduceStock(item.getGoodsId(), item.getNum(), orderNo);
        if (!R.isSuccess(reduceResult)) {
            return R.failed("下单成功，但库存扣减失败：" + reduceResult.getMsg());
        }

        return R.ok();
    }
}
