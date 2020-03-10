package com.yd.springbootdemo.utils.exceptions;

import com.yd.springbootdemo.utils.text.StrUtil;

/**
 * 工具类异常
 *
 * @author： 叶小东
 * @date： 2020/3/10 19:15
 */
public class UtilException extends RuntimeException{

    private static final long serialVersionUID = 8247610319171014183L;

    public UtilException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public UtilException(String message) {
        super(message);
    }

    public UtilException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public UtilException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}

