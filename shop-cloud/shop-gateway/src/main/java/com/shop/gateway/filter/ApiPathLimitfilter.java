package com.shop.gateway.filter;

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

/**
 * 按接口请求路径URI令牌桶限流全局过滤器
 * key：请求接口路径uri，value：对应接口的令牌桶
 */
@Component
public class ApiPathLimitfilter implements GlobalFilter, Ordered {

    public static final String WARNING_MSG = "接口请求过于频繁，请稍后再试";

    // key=接口URI路径，value=该接口独立令牌桶
    public static final Map<String, Bucket> API_BUCKET_CACHE = new ConcurrentHashMap<>();

    // 令牌桶最大容量（最大并发允许请求数）
    public static final long capacity = 2;
    // 令牌补充间隔
    public static final long refillSecond = 2;
    // 每次补充令牌数量
    public static final long refillTokenNum = 1;

    private static final Logger log = LoggerFactory.getLogger(ApiPathLimitfilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 获取当前请求接口路径
        String apiUri = request.getPath().value();
        log.info("当前访问接口路径:{}", apiUri);

        // 根据接口路径获取对应令牌桶
        Bucket bucket = API_BUCKET_CACHE.get(apiUri);
        if (bucket == null) {
            // 该接口首次访问，新建令牌桶存入缓存
            bucket = createNewBucket();
            API_BUCKET_CACHE.put(apiUri, bucket);
        }

        log.info("接口:{} ,当前可用令牌数:{} ", apiUri, bucket.getAvailableTokens());

        // 尝试消费1个令牌
        if (bucket.tryConsume(1)) {
            //获取令牌，放行请求
            return chain.filter(exchange);
        } else {
            //无令牌，返回限流提示
            String message = WARNING_MSG;
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            response.getHeaders().add("Content-Type", "text/plain;charset=utf-8");
            return response.writeWith(Mono.just(buffer));
        }
    }

    /**
     * 创建统一规则令牌桶
     */
    private Bucket createNewBucket() {
        Duration refillDuration = Duration.ofSeconds(refillSecond);
        Refill refill = Refill.of(refillTokenNum, refillDuration);
        Bandwidth bandwidth = Bandwidth.classic(capacity, refill);
        return Bucket4j.builder().addLimit(bandwidth).build();
    }

    /**
     * 过滤器执行优先级，数字越小越先执行
     */
    @Override
    public int getOrder() {
        return -100;
    }
}