package com.nieyue.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nieyue.util.ResultUtil;
import com.nieyue.util.StateResult;

/**
 * 统一异常处理
 * @author 聂跃
 * @date 2017年4月12日
 */
@RestControllerAdvice
public class MyExceptionAdvice {
	
	@ExceptionHandler(value=Exception.class)
	@ResponseBody
	public StateResult jsonErrorHandler( Exception e) throws Exception {
	       return ResultUtil.getFail();
	    }
	
}
