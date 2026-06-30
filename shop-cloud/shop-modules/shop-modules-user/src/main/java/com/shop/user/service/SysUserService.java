package com.shop.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.core.domain.entity.SysUser;

/**
* @author dada
* @description 针对表【sys_user】的数据库操作Service
* @createDate 2024-07-11 15:12:39
*/
public interface SysUserService extends IService<SysUser> {

    int editPwd(SysUser sysUser);
}
