package com.yd.springbootdemo.utils.exceptions;

import com.yd.springbootdemo.utils.text.StrUtil;

/**
 * 异常工具类
 *
 * @author： 叶小东
 * @date： 2020/3/10 19:22
 */
public class ExceptionUtil {

    /**
     * 获得完整消息，包括异常名，消息格式为：{SimpleClassName}: {ThrowableMessage}
     *
     * @param e 异常
     * @return 完整消息
     */
    public static String getMessage(Throwable e) {
        if (null == e) {
            return "null";
        }
        return StrUtil.format("{}: {}", e.getClass().getSimpleName(), e.getMessage());
    }
}
