package com.easypan.utils;

import lombok.Data;
import com.easypan.exception.ErrorCode;

/**
 * 响应数据
 *
 * @author 阿沐 babamu@126.com
 */
@Data
public class Result<T> {
    // 编码 200表示成功，其他值表示失败
    private int code = 200;
    // 消息内容
    private String msg = "success";
    // 响应数据
    private T data;

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error() {
        return error(ErrorCode.CODE_500);
    }

    public static <T> Result<T> error(String msg) {
        return error(ErrorCode.CODE_500.getCode(), msg);
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> Result<T> error(int code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
