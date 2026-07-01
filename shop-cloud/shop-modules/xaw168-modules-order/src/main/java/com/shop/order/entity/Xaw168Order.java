package com.shop.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单实体 - 对应 xaw168_order 表
 */
@TableName(value = "xaw168_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Xaw168Order implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 商品ID
     */
    private Integer goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 单价
     */
    private String price;

    /**
     * 购买数量
     */
    private Integer num;

    /**
     * 总价
     */
    private String totalPrice;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 状态（0已下单/1已发货/2已完成）
     */
    private String status;

    /**
     * 下单用户ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
