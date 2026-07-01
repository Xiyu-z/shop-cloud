package com.shop.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品实体 - 对应 xaw168_goods 表
 */
@TableName(value = "xaw168_goods")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Xaw168Goods implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 单价
     */
    private String price;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 封面图
     */
    private String img;

    /**
     * 商品详情
     */
    private String details;

    /**
     * 状态（0下架/1上架）
     */
    private String status;

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
