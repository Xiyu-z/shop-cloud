package com.shop.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.core.domain.entity.SysUser;
import com.shop.security.utils.SecurityUtils;
import com.shop.user.service.SysUserService;
import com.shop.user.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
* @author dada
* @description 针对表【sys_user】的数据库操作Service实现
* @createDate 2024-07-11 15:12:39
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public int editPwd(SysUser sysUser) {
        String encodePassword = SecurityUtils.encryptPassword(sysUser.getPassword());
        sysUser.setPassword(encodePassword);
        LambdaUpdateWrapper<SysUser> update = Wrappers.lambdaUpdate();
        update.set(SysUser::getPassword,encodePassword);
        update.eq(SysUser::getUserId, SecurityUtils.getUserId());
        return sysUserMapper.update(null,update);
    }
}




