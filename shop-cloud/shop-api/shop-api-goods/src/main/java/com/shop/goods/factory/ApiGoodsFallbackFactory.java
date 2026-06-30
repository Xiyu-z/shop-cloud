package com.shop.goods.factory;

import com.shop.goods.goods.ApiGoodsService;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @Author: dada
 * @Date: 2024/7/13 17:50
 * @Version: 1.0
 * @Description:
 */
public class ApiGoodsFallbackFactory implements FallbackFactory<ApiGoodsService> {
    @Override
    public ApiGoodsService create(Throwable cause) {
        return null;
    }
}
