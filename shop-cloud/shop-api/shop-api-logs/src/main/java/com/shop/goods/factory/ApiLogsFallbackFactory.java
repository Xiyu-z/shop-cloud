package com.shop.goods.factory;


import com.shop.goods.logs.ApiLogsService;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @Author: dada
 * @Date: 2024/7/11 21:19
 * @Version: 1.0
 * @Description:
 */
public class ApiLogsFallbackFactory implements FallbackFactory<ApiLogsService> {

    @Override
    public ApiLogsService create(Throwable cause) {
        return null;
    }
}