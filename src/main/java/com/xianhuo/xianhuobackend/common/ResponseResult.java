package com.xianhuo.xianhuobackend.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseResult<T> implements Serializable {
    // 返回状态码
    private Integer code;

    // 返回体
    private T data;

    // 返回信息
    private String message;

    public static <T> ResponseResult<T> ok() {
        return returnResult(REnum.SUCCESS.getCode(), null, REnum.SUCCESS.getDesc());
    }

    public static <T> ResponseResult<T> ok(T data) {
        return returnResult(REnum.SUCCESS.getCode(), data, null);
    }

    public static <T> ResponseResult<T> ok(T data, String msg) {
        return returnResult(REnum.SUCCESS.getCode(), data, msg);
    }

    public static <T> ResponseResult<T> fail() {
        return returnResult(REnum.FAIL.getCode(), null, REnum.FAIL.getDesc());
    }

    public static <T> ResponseResult<T> fail(T data) {
        return returnResult(REnum.FAIL.getCode(), data, null);
    }

    public static <T> ResponseResult<T> fail(T data, String msg) {
        return returnResult(REnum.FAIL.getCode(), data, msg);
    }

    // 统一返回信息
    public static <T> ResponseResult<T> returnResult(Integer code, T data, String msg) {
        final ResponseResult<T> result = new ResponseResult<>();
        result.setCode(code);
        result.setData(data);
        result.setMessage(msg);
        return result;
    }
}
