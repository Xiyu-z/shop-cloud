-- xaw168 商品表
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

-- xaw168 订单表
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
