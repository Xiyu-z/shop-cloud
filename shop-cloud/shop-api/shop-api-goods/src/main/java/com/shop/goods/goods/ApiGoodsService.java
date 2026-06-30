package com.shop.goods.goods;
import com.shop.core.result.R;
import com.shop.goods.factory.ApiGoodsFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: dada
 * @Date: 2024/7/13 17:49
 * @Version: 1.0
 * @Description:
 */
@FeignClient(contextId = "apiGoodsService",value = "shop-goods", name = "shop-goods", fallbackFactory = ApiGoodsFallbackFactory.class)
public interface ApiGoodsService {

    @GetMapping("/goods/editAddrNun/{id}/{name}/{num}")
    R<?> editAddrNun(@PathVariable("id") String id,@PathVariable("name") String name,@PathVariable("num") Integer num);
}
