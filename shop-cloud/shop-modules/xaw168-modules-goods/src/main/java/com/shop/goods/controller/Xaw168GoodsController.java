package com.shop.goods.controller;

import com.shop.core.result.R;
import com.shop.goods.entity.Xaw168Goods;
import com.shop.goods.service.Xaw168GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品 Controller - xaw168 库存扣减接口
 */
@RestController
@RequestMapping("/xaw168/goods")
public class Xaw168GoodsController {

    @Autowired
    private Xaw168GoodsService goodsService;

    /**
     * 商品数量减少接口
     * 根据商品ID扣减库存
     */
    @GetMapping("/reduceStock/{goodsId}/{num}/{orderNo}")
    public R<?> reduceStock(@PathVariable Integer goodsId,
                            @PathVariable Integer num,
                            @PathVariable String orderNo) {
        // 查询商品
        Xaw168Goods goods = goodsService.getById(goodsId);
        if (goods == null) {
            return R.failed("商品不存在");
        }
        // 校验库存
        Integer currentStock = goods.getStock();
        if (currentStock == null || currentStock < num) {
            return R.failed("库存不足，剩余：" + currentStock);
        }
        // 扣减库存
        goods.setStock(currentStock - num);
        goodsService.updateById(goods);
        return R.ok();
    }
}
