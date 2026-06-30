package com.shop.order.service;

import com.shop.core.domain.entity.SysOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.core.domain.vo.EchartsVo;

import java.util.List;

/**
* @author dada
* @description 针对表【sys_order】的数据库操作Service
* @createDate 2024-07-12 15:43:41
*/
public interface SysOrderService extends IService<SysOrder> {

    List<EchartsVo> productStats();
}
