package com.shop.gateway.filter;

import com.shop.gateway.IpUtil.IpUtil;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import org.springframework.core.io.buffer.DataBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//令牌桶算法限流
@Component
public class IpLimitfilter implements GlobalFilter, Ordered {

    public static final String WARNING_MSG = "请求过于频繁，请稍后再试";

    //key存ip地址，value存限流令牌桶
    public static final Map<String, Bucket> LOCAL_CACHE = new ConcurrentHashMap<>();

    // 令牌桶初始容量
    public static final long capacity = 2;

    // 补充桶的时间间隔，即2秒补充一次
    public static final long seconds = 2;

    // 每次补充token的个数
    public static final long refillTokens = 1;
    private static final Logger log = LoggerFactory.getLogger(IpLimitfilter.class);



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取客户端请求对象
        ServerHttpRequest request = exchange.getRequest();
        //获取客户端响应对象
        ServerHttpResponse response = exchange.getResponse();
        //获取客户端ip地址
        String ip = IpUtil.getIpAddr(request);
        log.info("客户端的访问IP为:{}", ip);

        Bucket bucket = LOCAL_CACHE.get(ip);
        if (bucket == null) {  //第一次访问
            //创建令牌桶，并将ip和令牌桶放到Map
            bucket = createNewBucket();
            //将ip和令牌放到map
            LOCAL_CACHE.put(ip, bucket);
        }else { //后续访问
            LOCAL_CACHE.get(ip);
        }

        log.info("IP:{} ,令牌桶可用的Token数量:{} ", ip, bucket.getAvailableTokens());

        if (bucket.tryConsume(1)) {
            //放行
            return chain.filter(exchange);
        } else {
            String message = "IP请求过于频繁，请稍后再试";
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);

            DataBuffer buffer = response.bufferFactory().wrap(bytes);

            //指定编码
            response.getHeaders().add("Content-Type", "text/plain;charset=utf-8");
            return response.writeWith(Mono.just(buffer));
        }
    }

    private Bucket createNewBucket() {
        Duration refillDuration = Duration.ofSeconds(seconds);
        Refill refill = Refill.of(refillTokens, refillDuration);
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}



