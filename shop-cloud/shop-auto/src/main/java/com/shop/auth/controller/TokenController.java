package com.shop.auth.controller;

import com.shop.auth.service.SysLoginService;
import com.shop.core.domain.model.LoginUser;
import com.shop.core.result.R;
import com.shop.core.utils.JwtUtils;
import com.shop.core.utils.StringUtils;
import com.shop.security.service.TokenService;
import com.shop.security.utils.AuthUtils;
import com.shop.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: dada
 * @Date: 2024/7/11 0:22
 * @Version: 1.0
 * @Description:
 */
@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;
    /**
     * 用户登录
     *
     * @param form
     * @return
     */
    @PostMapping("login")
    public R<?> login(@RequestBody LoginUser form) {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(form.getUsername(), form.getPassword());
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo));
    }

    /**
     * 用户退出
     *
     * @param request
     * @return
     */
    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            // 删除用户在Redis中得缓存记录
            AuthUtils.logoutByToken(token);
        }
        return R.ok();
    }

    @PostMapping("register")
    public R<?> register(@RequestBody LoginUser user) {
        // 用户注册
        sysLoginService.register(user);
        return R.ok();
    }
}
