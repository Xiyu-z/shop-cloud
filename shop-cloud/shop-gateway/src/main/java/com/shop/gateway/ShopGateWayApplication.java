package com.shop.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Author: dada
 * @Date: 2024/7/10 22:29
 * @Version: 1.0
 * @Description:
 * 没有在nacos中配置数据地址需要加  --》 (exclude = {DataSourceAutoConfiguration.class })
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ShopGateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopGateWayApplication.class, args);
    }
}
