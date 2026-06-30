package com.shop.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shop.core.domain.entity.SysType;
import com.shop.core.result.R;
import com.shop.core.utils.PageUtils;
import com.shop.goods.service.SysTypeService;
import com.shop.security.annotation.Log;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/type")
public class TypeController {
    @Autowired
    private SysTypeService typeService;
    @GetMapping("/list")
    @Log(describe = "查询类型列表")
    public R<?> list(SysType item) {
        PageUtils.start();
        List<SysType> list = typeService.lambdaQuery()
                .like(!ObjectUtils.isEmpty(item.getName()),SysType::getName,item.getName())
                .eq(SysType::getStatus,0)
                .list();
        return R.table(list);
    }
    @PostMapping
    @Log(describe = "新增类型")
    public R<?> add(@RequestBody SysType item) {
        return R.ok(typeService.save(item));
    }
    @PutMapping
    @Log(describe = "修改类型")
    public R<?> edit(@RequestBody SysType item) {
        return R.ok(typeService.updateById(item));
    }

    @DeleteMapping("/{id}")
    @Log(describe = "删除类型")
    public R<?> delete(@PathVariable("id") Long id) {
        LambdaUpdateWrapper<SysType> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysType::getId,id);
        wrapper.set(SysType::getStatus,1);
        typeService.update(wrapper);
        return R.ok();
    }

    @GetMapping("getById/{id}")
    @Log(describe = "根据id获取类型信息")
    public R<?> getById(@PathVariable String id) {
        return R.ok(typeService.getById(id));
    }

    @GetMapping("getAll")
    @Log(describe = "获取所有类型")
    public R<?> getByModule() {
        return R.ok(typeService.list());
    }


}
