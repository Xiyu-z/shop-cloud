package com.shop.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.order.entity.Xaw168Order;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

/**
 * 订单 Mapper - 对应 xaw168_order 表
 */
@Mapper
public interface Xaw168OrderMapper extends BaseMapper<Xaw168Order> {

}
