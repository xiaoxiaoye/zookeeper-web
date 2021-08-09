package com.remmy.backend.common;

import lombok.Getter;

/**
 * @author yejiaxin
 */

@Getter
public class RespBean {
    private final Integer status;
    private final String msg;
    private final Object obj;

    public RespBean(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public static RespBean markSuccess(String msg) {
        return new RespBean(200, msg, null);
    }

    public static RespBean markSuccess(String msg, Object data) {
        return new RespBean(200, msg, data);
    }

    public static RespBean markError(String msg) {
        return new RespBean(500, msg, null);
    }
}
