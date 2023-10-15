package com.easypan.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 *
 * @author 阿沐 babamu@126.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FastException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    private Integer code;
	private String msg;

	public FastException(String msg) {
		super(msg);
		this.code = ErrorCode.CODE_500.getCode();
		this.msg = msg;
	}

	public FastException(ErrorCode errorCode) {
		super(errorCode.getMsg());
		this.code = errorCode.getCode();
		this.msg = errorCode.getMsg();
	}

	public FastException(String msg, Throwable e) {
		super(msg, e);
		this.code = ErrorCode.CODE_500.getCode();
		this.msg = msg;
	}


}