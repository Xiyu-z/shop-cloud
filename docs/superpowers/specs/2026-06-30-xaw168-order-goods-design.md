# xaw168 下单与库存扣减模块设计

**日期**: 2026-06-30
**状态**: 设计已确认

---

## 1. 概述

在 shop-cloud-vue 项目中，基于两个新模块 `xaw168-modules-order` 和 `xaw168-modules-logs`，新增独立的商品表 `xaw168_goods` 和订单表 `xaw168_order`，并提供下单和库存扣减两个解耦的 API。

**核心原则**: 新表、新实体、新接口全部独立，不与原有 `sys_goods`/`sys_order` 等共享数据或代码。

---

## 2. 数据库表

### 2.1 xaw168_goods（商品表）

```sql
CREATE TABLE `xaw168_goods` (
  `id`          INT(11)      NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name`        VARCHAR(255) DEFAULT NULL COMMENT '商品名称',
  `price`       VARCHAR(50)  DEFAULT NULL COMMENT '单价',
  `stock`       INT(11)      DEFAULT 0 COMMENT '库存数量',
  `img`         VARCHAR(500) DEFAULT NULL COMMENT '封面图',
  `details`     TEXT         DEFAULT NULL COMMENT '商品详情',
  `status`      VARCHAR(10)  DEFAULT '1' COMMENT '状态（0下架/1上架）',
  `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME     DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';
```

### 2.2 xaw168_order（订单表）

```sql
CREATE TABLE `xaw168_order` (
  `id`          INT(11)      NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `goods_id`    INT(11)      DEFAULT NULL COMMENT '商品ID',
  `goods_name`  VARCHAR(255) DEFAULT NULL COMMENT '商品名称',
  `price`       VARCHAR(50)  DEFAULT NULL COMMENT '单价',
  `num`         INT(11)      DEFAULT NULL COMMENT '购买数量',
  `total_price` VARCHAR(50)  DEFAULT NULL COMMENT '总价',
  `order_no`    VARCHAR(100) DEFAULT NULL COMMENT '订单编号',
  `status`      VARCHAR(10)  DEFAULT '0' COMMENT '状态（0已下单/1已发货/2已完成）',
  `create_by`   BIGINT(20)   DEFAULT NULL COMMENT '下单用户ID',
  `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME     DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
```

---

## 3. 模块结构

### 3.1 xaw168-modules-order（下单模块，端口 9206）

```
xaw168-modules-order/src/main/java/com/shop/order/
├── Xaw168ModulesOrderApplication.java
├── entity/
│   └── Xaw168Order.java
├── mapper/
│   └── Xaw168OrderMapper.java
├── service/
│   ├── Xaw168OrderService.java
│   └── impl/
│       └── Xaw168OrderServiceImpl.java
└── controller/
    └── Xaw168OrderController.java
```

### 3.2 xaw168-modules-logs（库存扣减模块，端口 9207）

```
xaw168-modules-logs/src/main/java/com/shop/logs/
├── Xaw168ModulesLogsApplication.java   (修正包名)
├── entity/
│   └── Xaw168Goods.java
├── mapper/
│   └── Xaw168GoodsMapper.java
├── service/
│   ├── Xaw168GoodsService.java
│   └── impl/
│       └── Xaw168GoodsServiceImpl.java
└── controller/
    └── Xaw168GoodsController.java
```

---

## 4. API 设计

### 4.1 下单接口 `POST /xaw168/order`

请求体:
```json
{
  "goodsId": 1,
  "goodsName": "商品名称",
  "price": "99.00",
  "num": 2,
  "totalPrice": "198.00"
}
```

逻辑:
- 自动生成订单编号 `DD-{UUID(去横线)}`
- 状态默认 `"0"`（已下单）
- 自动填充 `createBy`（通过 MyBatis-Plus MetaObjectHandler）
- 仅创建订单，不操作库存

### 4.2 库存扣减接口 `PUT /xaw168/goods/reduceStock`

请求参数:
| 参数 | 类型 | 说明 |
|------|------|------|
| goodsId | Integer | 商品ID |
| num | Integer | 扣减数量 |
| orderNo | String | 关联订单编号 |

逻辑:
1. 根据 goodsId 查询商品
2. 校验 stock >= num，否则返回"库存不足"
3. 执行 `stock = stock - num` 更新
4. 返回成功

错误场景:
- 商品不存在 → `R.failed("商品不存在")`
- 库存不足 → `R.failed("库存不足")`

---

## 5. 技术决策

| 决策项 | 选择 | 原因 |
|--------|------|------|
| 实体位置 | 各模块内部 entity 包 | 与原有 `shop-common-core` 解耦 |
| 下单+扣减 | 两个独立接口 | 用户指定解耦方式 |
| ORM | MyBatis-Plus (已集成) | 沿用项目技术栈 |
| 认证 | 通过 SecurityUtils | 沿用项目现有安全机制 |
| 响应格式 | R<T> | 沿用项目统一响应 |

---

## 6. 待实现清单

- [ ] 创建 `xaw168_goods` 和 `xaw168_order` 数据库表
- [ ] xaw168-modules-order: 新建 entity、mapper、service、controller
- [ ] xaw168-modules-logs: 修正 Application 包名，新建 entity、mapper、service、controller
- [ ] 创建 bootstrap.yml 配置文件
- [ ] 注册 Nacos 服务发现
