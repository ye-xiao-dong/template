package com.yd.springbootdemo.utils.exceptions;

/**
 * IO运行时异常，常用于对IOException的包装
 *
 * @author： 叶小东
 * @date： 2020/3/10 19:46
 */
public class IORuntimeException extends RuntimeException {

    private static final long serialVersionUID = 8247610319171014183L;

    public IORuntimeException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

}
