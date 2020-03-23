package com.yd.springbootdemo.utils.io;


import com.yd.springbootdemo.utils.exceptions.ExceptionUtil;
import com.yd.springbootdemo.utils.text.StrUtil;

/**
 * IO运行时异常，常用于对IOException的包装
 * 
 *
 */
public class IORuntimeException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public IORuntimeException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public IORuntimeException(String message) {
		super(message);
	}

	public IORuntimeException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public IORuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public IORuntimeException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
