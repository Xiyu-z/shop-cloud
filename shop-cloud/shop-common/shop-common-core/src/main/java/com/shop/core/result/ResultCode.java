package com.shop.core.result;

import com.shop.core.constant.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author:dada
 * @Date: 2024/7/10 21:57
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
public enum ResultCode implements CustomizeResultCode, Serializable {


    SUCCESS(HttpStatus.SUCCESS, "操作成功"),
    SYSTEM_EXECUTION_ERROR(HttpStatus.ERROR, "系统内部错误"),
    ;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    private Integer code;

    private String msg;

    @Override
    public String toString() {
        return "{" +
                "\"code\":\"" + code + '\"' +
                ", \"msg\":\"" + msg + '\"' +
                '}';
    }
}
