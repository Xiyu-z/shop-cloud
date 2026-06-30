package com.shop.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.core.domain.entity.SysOrder;
import com.shop.core.domain.vo.EchartsVo;
import com.shop.order.service.SysOrderService;
import com.shop.order.mapper.SysOrderMapper;
import com.shop.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author dada
* @description 针对表【sys_order】的数据库操作Service实现
* @createDate 2024-07-12 15:43:41
*/
@Service
public class SysOrderServiceImpl extends ServiceImpl<SysOrderMapper, SysOrder>
    implements SysOrderService{

    @Autowired
    private SysOrderMapper orderMapper;
    @Override
    public List<EchartsVo> productStats() {
        return orderMapper.productStats(SecurityUtils.getUserId());
    }
}




