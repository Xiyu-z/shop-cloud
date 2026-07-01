package com.shop.goods.logs;
import com.shop.core.domain.entity.SysLogs;
import com.shop.core.result.R;
import com.shop.goods.factory.ApiLogsFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: dada
 * @Date: 2024/7/12 23:01
 * @Version: 1.0
 * @Description:
 */
@FeignClient(contextId = "apiLogsService",value = "shop-logs", name = "shop-logs", fallbackFactory = ApiLogsFallbackFactory.class)
public interface ApiLogsService {

    @PostMapping("/logs")
    R<?> add(@RequestBody SysLogs item);
}
