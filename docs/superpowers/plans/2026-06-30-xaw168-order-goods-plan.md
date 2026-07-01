# xaw168 下单与库存扣减模块 — 实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development or superpowers:executing-plans to implement this plan task-by-task.

**Goal:** 在 xaw168-modules-order 和 xaw168-modules-logs 两个模块中，新增独立的商品表/订单表，并实现下单和库存扣减两个解耦的 REST API。

**Architecture:** 沿用项目现有分层模式（Controller → Service → ServiceImpl → Mapper），实体类放在各模块内部而非共享 common 包，真正做到与原内容无关。

**Tech Stack:** Spring Boot 2.7.3 + MyBatis-Plus 3.5.1 + MySQL + Nacos

---

## Task 1: SQL DDL — 创建 xaw168_goods 和 xaw168_order 表

**Files:**
- Create: `shop-cloud/shop-modules/xaw168-modules-order/src/main/resources/sql/init.sql`

## Task 2: xaw168-modules-order — 实体类 Xaw168Order

**Files:**
- Create: `shop-cloud/shop-modules/xaw168-modules-order/src/main/java/com/shop/order/entity/Xaw168Order.java`

## Task 3: xaw168-modules-order — Mapper

**Files:**
- Create: `shop-cloud/shop-modules/xaw168-modules-order/src/main/java/com/shop/order/mapper/Xaw168OrderMapper.java`

## Task 4: xaw168-modules-order — Service

**Files:**
- Create: `shop-cloud/shop-modules/xaw168-modules-order/src/main/java/com/shop/order/service/Xaw168OrderService.java`
- Create: `shop-cloud/shop-modules/xaw168-modules-order/src/main/java/com/shop/order/service/impl/Xaw168OrderServiceImpl.java`

## Task 5: xaw168-modules-order — Controller + bootstrap.yml

**Files:**
- Create: `shop-cloud/shop-modules/xaw168-modules-order/src/main/java/com/shop/order/controller/Xaw168OrderController.java`
- Create: `shop-cloud/shop-modules/xaw168-modules-order/src/main/resources/bootstrap.yml`

## Task 6: xaw168-modules-logs — 修正 Application 包名

**Files:**
- Delete: `shop-cloud/shop-modules/xaw168-modules-logs/src/main/java/com/shop/order/Xaw168ModulesOrderApplication.java`
- Create: `shop-cloud/shop-modules/xaw168-modules-logs/src/main/java/com/shop/logs/Xaw168ModulesLogsApplication.java`

## Task 7: xaw168-modules-logs — 实体类 Xaw168Goods

**Files:**
- Create: `shop-cloud/shop-modules/xaw168-modules-logs/src/main/java/com/shop/logs/entity/Xaw168Goods.java`

## Task 8: xaw168-modules-logs — Mapper

**Files:**
- Create: `shop-cloud/shop-modules/xaw168-modules-logs/src/main/java/com/shop/logs/mapper/Xaw168GoodsMapper.java`

## Task 9: xaw168-modules-logs — Service

**Files:**
- Create: `shop-cloud/shop-modules/xaw168-modules-logs/src/main/java/com/shop/logs/service/Xaw168GoodsService.java`
- Create: `shop-cloud/shop-modules/xaw168-modules-logs/src/main/java/com/shop/logs/service/impl/Xaw168GoodsServiceImpl.java`

## Task 10: xaw168-modules-logs — Controller + bootstrap.yml

**Files:**
- Create: `shop-cloud/shop-modules/xaw168-modules-logs/src/main/java/com/shop/logs/controller/Xaw168GoodsController.java`
- Create: `shop-cloud/shop-modules/xaw168-modules-logs/src/main/resources/bootstrap.yml`

## Task 11: 验证编译

Run: `mvn compile` 确认无编译错误
