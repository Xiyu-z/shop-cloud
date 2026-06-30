package com.shop.user.factory;


import com.shop.core.domain.entity.SysUser;
import com.shop.core.result.R;
import com.shop.user.user.ApiUserService;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.math.BigDecimal;

/**
 * @Author: dada
 * @Date: 2024/7/11 21:19
 * @Version: 1.0
 * @Description:
 */
public class ApiUserFallbackFactory implements FallbackFactory<ApiUserService> {
    @Override
    public ApiUserService create(Throwable cause) {
        return new ApiUserService() {
            @Override
            public R<SysUser> getUserInfo(String identifier) {
                return null;
            }

            @Override
            public R<?> registerUserInfo(SysUser sysUser) {
                return null;
            }

            @Override
            public R<SysUser> getUserById(Long id) {
                return null;
            }

            @Override
            public R<?> updateIntegral(SysUser sysUser) {
                return null;
            }

        };
    }
}