package com.shop.gateway.predicates;



import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class CustomPathRoutePredicateFactory extends
        AbstractRoutePredicateFactory<CustomPathRoutePredicateFactory.Config> {

    public static final PathMatcher PATH_MATCHER =  new AntPathMatcher();



    public CustomPathRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() { //用于初始化参数名称
        return Arrays.asList("pattern");
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            //请求对象
            ServerHttpRequest request = exchange.getRequest();
            //请求路径
            String path = request.getURI().getPath();
            if(PATH_MATCHER.match(config.pattern, path)){
                return true;
            }
            return false;
        };
    }

    public static class Config{ //静态内部类

        private String pattern; //用改变量接收配置文件的配置规则

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

    }

}
