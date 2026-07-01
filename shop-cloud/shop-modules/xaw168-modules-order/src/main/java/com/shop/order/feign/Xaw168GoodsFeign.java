package com.shop.order.feign;

import com.shop.core.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign 客户端 - 调用 xaw168-goods 服务
 */
@FeignClient(contextId = "xaw168GoodsFeign", value = "xaw168-goods", name = "xaw168-goods")
public interface Xaw168GoodsFeign {

    @GetMapping("/xaw168/goods/reduceStock/{goodsId}/{num}/{orderNo}")
    R<?> reduceStock(@PathVariable("goodsId") Integer goodsId,
                     @PathVariable("num") Integer num,
                     @PathVariable("orderNo") String orderNo);
}
