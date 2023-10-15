package com.easypan.exception;

import com.easypan.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;


/**
 * 异常处理器
 *
 * @author 阿沐 babamu@126.com
 */
@Slf4j
@RestControllerAdvice
public class FastExceptionHandler {

	protected static final String STATUS_SUCCESS = "success";

	protected static final String STATUS_ERROR = "error";

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(FastException.class)
	public Result<String> handleRenException(FastException ex){

		return Result.error(ex.getCode(), ex.getMsg());
	}

	/**
	 * SpringMVC参数绑定，Validator校验不正确
	 */
	@ExceptionHandler(BindException.class)
	public Result<String> bindException(BindException ex) {
		FieldError fieldError = ex.getFieldError();
		assert fieldError != null;
		return Result.error(fieldError.getDefaultMessage());
	}

	@ExceptionHandler(Exception.class)
	public Result<String> handleException(Exception e){
		log.error(e.getMessage(), e);
		Result<String> ajaxResponse = new Result<>();
		//404
		if (e instanceof NoHandlerFoundException) {
			ajaxResponse.setCode(ErrorCode.CODE_404.getCode());
			ajaxResponse.setMsg(ErrorCode.CODE_404.getMsg());
			ajaxResponse.setData(STATUS_ERROR);
		} else if (e instanceof FastException) {
			//业务错误
			FastException biz = (FastException) e;
			ajaxResponse.setCode(biz.getCode() == null ? ErrorCode.CODE_600.getCode() : biz.getCode());
			ajaxResponse.setMsg(biz.getMessage());
			ajaxResponse.setData(STATUS_ERROR);
		} else if (e instanceof BindException|| e instanceof MethodArgumentTypeMismatchException) {
			//参数类型错误
			ajaxResponse.setCode(ErrorCode.CODE_600.getCode());
			ajaxResponse.setMsg(ErrorCode.CODE_600.getMsg());
			ajaxResponse.setData(STATUS_ERROR);
		} else if (e instanceof DuplicateKeyException) {
			//主键冲突
			ajaxResponse.setCode(ErrorCode.CODE_601.getCode());
			ajaxResponse.setMsg(ErrorCode.CODE_601.getMsg());
			ajaxResponse.setData(STATUS_ERROR);
		} else {
			ajaxResponse.setCode(ErrorCode.CODE_500.getCode());
			ajaxResponse.setMsg(ErrorCode.CODE_500.getMsg());
			ajaxResponse.setData(STATUS_ERROR);
		}
		return ajaxResponse;
	}
}