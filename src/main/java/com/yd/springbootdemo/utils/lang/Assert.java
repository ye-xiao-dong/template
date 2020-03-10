package com.yd.springbootdemo.utils.lang;


import com.yd.springbootdemo.utils.text.StrUtil;

/**
 * 断言
 * 断言某些对象或值是否符合规定，否则抛出异常。经常用于做变量检查
 *
 * @author： 叶小东
 * @date： 2020/3/10 19:48
 */
public class Assert {

    /**
     * 断言对象是否不为{@code null} ，如果为{@code null} 抛出{@link IllegalArgumentException} 异常 Assert that an object is not {@code null} .
     *
     * @param <T> 被检查对象泛型类型
     * @param object 被检查对象
     * @param errorMsgTemplate 错误消息模板，变量使用{}表示
     * @param params 参数
     * @return 被检查后的对象
     * @throws IllegalArgumentException if the object is {@code null}
     */
    public static <T> T notNull(T object, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
        }
        return object;
    }

    /**
     * 断言对象是否不为{@code null} ，如果为{@code null} 抛出{@link IllegalArgumentException} 异常
     *
     * @param <T> 被检查对象类型
     * @param object 被检查对象
     * @return 非空对象
     * @throws IllegalArgumentException if the object is {@code null}
     */
    public static <T> T notNull(T object) throws IllegalArgumentException {
        return notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }
}
