package com.shop.goods.cart;

import com.shop.core.domain.entity.SysCart;
import com.shop.core.result.R;
import com.shop.goods.factory.ApiCartFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Author: dada
 * @Date: 2024/7/13 17:37
 * @Version: 1.0
 * @Description:
 */
@FeignClient(contextId = "apiCartService",value = "shop-goods", name = "shop-goods", fallbackFactory = ApiCartFallbackFactory.class)
public interface ApiCartService {

    @DeleteMapping("/cart/{id}")
    R<?> delById(@PathVariable("id") Long id);

    @GetMapping("/cart/getById/{id}")
    R<SysCart> getById(@PathVariable("id") String id);

}
