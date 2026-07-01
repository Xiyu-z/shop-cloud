package com.shop.goods.controller;

import com.shop.core.domain.entity.SysLogs;
import com.shop.core.result.R;
import com.shop.core.utils.PageUtils;
import com.shop.goods.service.SysLogsService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogsController {
    @Autowired
    private SysLogsService logsService;
    @GetMapping("/list")
    public R<?> list(SysLogs item) {
        PageUtils.start();
        List<SysLogs> list = logsService.lambdaQuery()
                .like(!ObjectUtils.isEmpty(item.getMethod()),SysLogs::getMethod,item.getMethod())
                .list();
        return R.table(list);
    }
    @PostMapping
    public R<?> add(@RequestBody SysLogs item) {
        return R.ok(logsService.save(item));
    }
    @PutMapping
    public R<?> edit(@RequestBody SysLogs item) {
        return R.ok(logsService.updateById(item));
    }

    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        return R.ok(logsService.removeById(id));
    }

    @GetMapping("getById/{id}")
    public R<?> getById(@PathVariable String id) {
        return R.ok(logsService.getById(id));
    }
}
