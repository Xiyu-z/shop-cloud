package com.shop.goods.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.core.domain.entity.SysCart;
import com.shop.core.domain.entity.SysGoods;
import com.shop.core.domain.vo.AttrVo;
import com.shop.core.result.R;
import com.shop.core.utils.PageUtils;
import com.shop.goods.service.SysCartService;
import com.shop.goods.service.SysGoodsService;
import com.shop.security.annotation.Log;
import com.shop.security.utils.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private SysCartService cartService;
    @Autowired
    private SysGoodsService goodsService;

    @GetMapping("/list")
    @Log(describe = "查询购物车列表")
    public R<?> list(SysCart item) {
        PageUtils.start();
        List<SysCart> list = cartService.lambdaQuery()
                .like(!ObjectUtils.isEmpty(item.getName()), SysCart::getName, item.getName())
                .list();
        return R.table(list);
    }

    @GetMapping("/getList")
    @Log(describe = "查询自己购物车列表")
    public R<?> getList() {
        List<SysCart> list = cartService.lambdaQuery()
                .eq(SysCart::getCreateBy, SecurityUtils.getUserId())
                .list();
        return R.ok(list);
    }

    @PostMapping
    @Log(describe = "新增购物车")
    @Transactional(rollbackFor = Exception.class)
    public R<?> add(@RequestBody SysCart item) {
        Integer pid = item.getPid();
        String nameToFind = item.getAttr();
        SysGoods goods = goodsService.getById(pid);
        if (ObjectUtils.isEmpty(goods)) {
            return R.failed("商品不存在");
        }
        String attrStr = goods.getAttr();
        List<AttrVo> attrVos = JSON.parseArray(attrStr, AttrVo.class);
        AttrVo result = attrVos.stream()
                .filter(attr -> nameToFind.equals(attr.getName()))
                .findFirst()
                .orElse(null);
        if (ObjectUtils.isEmpty(result)) {
            return R.failed("商品属性不存在");
        }
        //判断商品数量
        if (result.getNum() <= 0) {
            return R.failed("该商品属性已售罄");
        }
        //属性值
        String addrName = result.getName();
        //查看当前登录人是否已经加入过购物车
        SysCart cart = cartService.lambdaQuery()
                .eq(SysCart::getCreateBy, SecurityUtils.getUserId())
                .eq(SysCart::getPid, pid)
                .eq(SysCart::getAttr, addrName)
                .one();
        if (!ObjectUtils.isEmpty(cart)) {
            String sumPriceStr = cart.getSum();
            BigDecimal sumPrice = new BigDecimal(sumPriceStr);
            //如果已经存在，则数量+1
            Integer num = cart.getNum();
            cart.setNum(num + 1);
            //总价计算
            BigDecimal sum = sumPrice.multiply(new BigDecimal(num + 1));
            cart.setSum(sum.toString());
            cartService.updateById(cart);
            return R.ok();
        }
        BeanUtils.copyProperties(goods, item);
        BigDecimal price = result.getPrice();
        item.setSum(price.toString());
        item.setNum(1);
        item.setPrice(price.toString());
        item.setAttr(addrName);
        item.setId(null);
        return R.ok(cartService.save(item));
    }


    @PutMapping
    @Log(describe = "修改购物车")
    public R<?> edit(@RequestBody SysCart item) {
        return R.ok(cartService.updateById(item));
    }

    @DeleteMapping("/{id}")
    @Log(describe = "删除购物车")
    public R<?> delete(@PathVariable("id") Long id) {
        return R.ok(cartService.removeById(id));
    }

    @DeleteMapping("/delAll")
    @Log(describe = "清空购物车")
    public R<?> delAll() {
        LambdaQueryWrapper<SysCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCart::getCreateBy, SecurityUtils.getUserId());
        cartService.remove(wrapper);
        return R.ok();
    }

    @GetMapping("getById/{id}")
    @Log(describe = "根据id获取购物车信息")
    public R<SysCart> getById(@PathVariable("id") String id) {
        SysCart cart = cartService.lambdaQuery()
                .eq(SysCart::getId, id)
                .eq(SysCart::getCreateBy, SecurityUtils.getUserId()).one();
        return R.ok(cart);
    }

    @GetMapping("/getMyCart")
    @Log(describe = "获取我的购物车")
    public R<?> getMyCart() {
        List<SysCart> list = cartService.lambdaQuery()
                .eq(SysCart::getCreateBy, SecurityUtils.getUserId())
                .list();
        for (SysCart sysCart : list) {
            SysGoods goods = goodsService.getById(sysCart.getPid());
            if (!ObjectUtils.isEmpty(goods)) {
                String attrStr = goods.getAttr();
                List<AttrVo> attrVos = JSON.parseArray(attrStr, AttrVo.class);
                AttrVo result = attrVos.stream()
                        .filter(attr -> sysCart.getAttr().equals(attr.getName()))
                        .findFirst()
                        .orElse(null);
                sysCart.setAttr(JSON.toJSONString(result));
            }
        }
        return R.ok(list);
    }

    @PutMapping("editNum")
    @Log(describe = "修改购物车数量")
    public R<?> editNum(@RequestBody SysCart item) {
        // 查询购物车记录
        SysCart cart = cartService.lambdaQuery()
                .eq(SysCart::getCreateBy, SecurityUtils.getUserId())
                .eq(SysCart::getId, item.getId())
                .one();

        if (cart == null) {
            return R.failed("购物车商品不存在");
        }

        // 更新购物车数量
        cart.setNum(item.getNum());

        // 计算总价
        BigDecimal unitPrice = new BigDecimal(cart.getPrice());
        BigDecimal sum = unitPrice.multiply(new BigDecimal(item.getNum()));
        cart.setSum(sum.setScale(2, RoundingMode.HALF_UP).toString()); // 设置总价精度为两位小数

        // 更新购物车记录
        boolean updated = cartService.updateById(cart);
        if (updated) {
            return R.ok();
        } else {
            return R.failed("更新购物车数量失败");
        }
    }


    @GetMapping("delByIds")
    R<?> delByIds(List<String> ids) {
        return R.ok(cartService.removeByIds(ids));
    }
}
