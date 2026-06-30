package com.shop.goods.factory;

import com.shop.goods.cart.ApiCartService;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @Author: dada
 * @Date: 2024/7/13 17:38
 * @Version: 1.0
 * @Description:
 */
public class ApiCartFallbackFactory implements FallbackFactory<ApiCartService> {
    @Override
    public ApiCartService create(Throwable cause) {
        return null;
    }
}
