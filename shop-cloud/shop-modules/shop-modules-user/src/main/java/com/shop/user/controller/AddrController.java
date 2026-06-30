package com.shop.user.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shop.core.domain.entity.SysAddr;
import com.shop.core.domain.entity.SysAddr;
import com.shop.core.result.R;
import com.shop.core.utils.PageUtils;
import com.shop.security.annotation.Log;
import com.shop.security.utils.SecurityUtils;
import com.shop.user.service.SysAddrService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addr")
public class AddrController {

    @Autowired
    private SysAddrService addrService;
    @GetMapping("/list")
    @Log(describe = "查询我的地址列表")
    public R<?> list(SysAddr item) {
        PageUtils.start();
        List<SysAddr> list = addrService.lambdaQuery()
                .like(!ObjectUtils.isEmpty(item.getNickname()),SysAddr::getNickname,item.getNickname())
                .eq(SysAddr::getStatus,0)
                .eq(SysAddr::getCreateBy, SecurityUtils.getUserId())
                .list();
        return R.table(list);
    }
    @GetMapping("/getAll")
    @Log(describe = "查询我的所有的地址列表")
    public R<?> getAll() {
        List<SysAddr> list = addrService.lambdaQuery()
                .eq(SysAddr::getStatus,0)
                .eq(SysAddr::getCreateBy, SecurityUtils.getUserId())
                .list();
        return R.ok(list);
    }
    @PostMapping
    @Log(describe = "新增地址")
    public R<?> add(@RequestBody SysAddr item) {
        return R.ok(addrService.save(item));
    }
    @PutMapping
    @Log(describe = "修改地址")
    public R<?> edit(@RequestBody SysAddr item) {
        return R.ok(addrService.updateById(item));
    }

    @DeleteMapping("/{id}")
    @Log(describe = "删除地址")
    public R<?> delete(@PathVariable("id") Long id) {
        LambdaUpdateWrapper<SysAddr> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysAddr::getId,id);
        wrapper.eq(SysAddr::getCreateBy, SecurityUtils.getUserId());
        wrapper.set(SysAddr::getStatus,"1");
        addrService.update(wrapper);
        return R.ok();
    }

    @GetMapping("getById/{id}")
    @Log(describe = "根据id获取地址信息")
    public R<?> getById(@PathVariable String id) {
        return R.ok(addrService.getById(id));
    }
}
