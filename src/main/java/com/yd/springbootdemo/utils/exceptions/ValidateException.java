package com.yd.springbootdemo.utils.exceptions;

/**
 * 验证异常
 * @author： 叶小东
 * @date： 2020/3/10 20:19
 */
public class ValidateException extends RuntimeException{

    private static final long serialVersionUID = 6057602589533840889L;

    public ValidateException(String msg) {
        super(msg);
    }

}
