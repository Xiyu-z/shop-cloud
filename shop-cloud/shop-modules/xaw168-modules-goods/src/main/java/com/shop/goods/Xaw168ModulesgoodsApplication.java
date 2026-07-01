package com.shop.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.shop.*.mapper")
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.shop"})
@ComponentScan(basePackages = {"com.shop"})
public class Xaw168ModulesgoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(Xaw168ModulesgoodsApplication.class, args);
    }

}
