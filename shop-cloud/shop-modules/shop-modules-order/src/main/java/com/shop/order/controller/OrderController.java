package com.shop.order.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shop.core.domain.entity.SysCart;
import com.shop.core.domain.entity.SysOrder;
import com.shop.core.domain.entity.SysUser;
import com.shop.core.domain.vo.EchartsVo;
import com.shop.core.result.R;
import com.shop.core.utils.PageUtils;
import com.shop.goods.cart.ApiCartService;
import com.shop.goods.goods.ApiGoodsService;
import com.shop.order.service.SysOrderService;
import com.shop.security.annotation.Log;
import com.shop.security.utils.SecurityUtils;
import com.shop.user.user.ApiUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private SysOrderService orderService;
    @Autowired
    private ApiCartService apiCartService;
    @Autowired
    private ApiGoodsService apiGoodsService;
    @Autowired
    private ApiUserService apiUserService;

    /**
     * 购买环保商品兑换积分
     */
    BigDecimal percentage = new BigDecimal("0.01"); // 1% 对应的小数形式


    @GetMapping("/list")
    @Log(describe = "查询订单列表")
    public R<?> list(SysOrder item) {
        PageUtils.start();
        List<SysOrder> list = orderService.lambdaQuery()
                .like(!ObjectUtils.isEmpty(item.getName()), SysOrder::getName, item.getName())
                .list();
        for (SysOrder sysOrder : list) {
            Long createBy = sysOrder.getCreateBy();
            R<SysUser> user = apiUserService.getUserById(createBy);
            SysUser data = user.getData();
            String username = data.getUsername();
            if (!ObjectUtils.isEmpty(username)) {
                sysOrder.setUsername(username);
            }

        }
        return R.table(list);
    }
    @GetMapping("/getMyOrder")
    @Log(describe="查询我的订单列表")
    public R<?> getMyOrder(SysOrder item) {
        PageUtils.start();
        List<SysOrder> list = orderService.lambdaQuery()
                .like(!ObjectUtils.isEmpty(item.getName()), SysOrder::getName, item.getName())
                .eq(SysOrder::getCreateBy, SecurityUtils.getUserId())
                .orderByDesc(SysOrder::getCreateTime)
                .list();
        return R.table(list);
    }

    @PostMapping
    @Log(describe = "新增订单")
    @Transactional(rollbackFor = Exception.class)
    public R<?> add(@RequestBody List<SysOrder> items, @RequestParam Boolean useIntegrals, @RequestParam String point) {
        Long userId = SecurityUtils.getUserId();
        //获取用户能搞使用的积分
        R<SysUser> user = apiUserService.getUserById(userId);
        SysUser data = user.getData();
        BigDecimal discount = data.getIntegral();

        //勾选使用积分，先清空积分
        if (useIntegrals) {
            SysUser sysUser = new SysUser();
            sysUser.setUserId(userId);
            BigDecimal bigDecimal = new BigDecimal(point);
            BigDecimal multiply = discount.subtract(bigDecimal);
            BigDecimal scaledResult = multiply.setScale(2, RoundingMode.HALF_UP);
            sysUser.setIntegral(scaledResult);
            apiUserService.updateIntegral(sysUser);
        }
        for (SysOrder item : items) {
            Integer cid = item.getCid();
            R<SysCart> service = apiCartService.getById(cid.toString());
            SysCart cart = service.getData();
            if (!ObjectUtils.isEmpty(cart)) {
                Integer pid = cart.getPid();
                //减库存
                R<?> editAddrNun = apiGoodsService.editAddrNun(pid.toString(), cart.getAttr(), cart.getNum());
                if (editAddrNun.getCode() == 200) {
                    //删除购物车
                    apiCartService.delById(cid.longValue());
                    //下单
                    BeanUtils.copyProperties(cart, item);
                    item.setId(null);
                    String number = "DD-" + UUID.randomUUID().toString().replaceAll("-", "");
                    item.setNumbers(number);
                    item.setStatus("0");
                    BigDecimal roundedResult = new BigDecimal("0");
                    //增加积分
                    if (!ObjectUtils.isEmpty(item.getType())) {
                        String sum = item.getSum();
                        BigDecimal bigDecimal = new BigDecimal(sum);
                        BigDecimal result = bigDecimal.multiply(percentage).setScale(2, RoundingMode.HALF_UP);;
                        R<SysUser> users = apiUserService.getUserById(userId);
                        SysUser datas = users.getData();
                        BigDecimal integral1 = datas.getIntegral();
                        BigDecimal addResult = result.add(integral1);
                        roundedResult = addResult.setScale(2, RoundingMode.HALF_UP);
                        SysUser sysUser = new SysUser();
                        sysUser.setUserId(userId);
                        sysUser.setIntegral(roundedResult);
                        item.setIntegral(result.toString());
                        apiUserService.updateIntegral(sysUser);
                    }else {
                        item.setIntegral("0");
                    }
                    //添加订单
                    orderService.save(item);
                }
            }
        }
        return R.ok();
    }

    @PutMapping
    @Log(describe = "修改订单")
    public R<?> edit(@RequestBody SysOrder item) {
        return R.ok(orderService.updateById(item));
    }

    @PutMapping("editStatus")
    @Log(describe = "修改订单状态")
    public R<?> editStatus(@RequestBody SysOrder item) {
        LambdaUpdateWrapper<SysOrder> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysOrder::getStatus, item.getStatus());
        wrapper.eq(SysOrder::getId, item.getId());
        orderService.update(wrapper);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    @Log(describe = "删除订单")
    public R<?> delete(@PathVariable("id") Long id) {
        return R.ok(orderService.removeById(id));
    }

    @GetMapping("getById/{id}")
    @Log(describe = "根据id获取订单信息")
    public R<?> getById(@PathVariable String id) {
        return R.ok(orderService.getById(id));
    }



    @GetMapping("/productStats")
    @Log(describe = "环保商品统计")
    public R<?> productStats() {
        List<EchartsVo> echartsVos = orderService.productStats();
        return R.ok(echartsVos);
    }
}
