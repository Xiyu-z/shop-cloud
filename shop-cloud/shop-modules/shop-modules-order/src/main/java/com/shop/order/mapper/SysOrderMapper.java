package com.shop.order.mapper;

import com.shop.core.domain.entity.SysOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.core.domain.vo.EchartsVo;

import java.util.List;

/**
* @author dada
* @description 针对表【sys_order】的数据库操作Mapper
* @createDate 2024-07-12 15:43:41
* @Entity com.shop.core.domain.entity.SysOrder
*/
public interface SysOrderMapper extends BaseMapper<SysOrder> {

    List<EchartsVo> productStats(Long userId);
}




