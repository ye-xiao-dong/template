package com.yd.springbootdemo.utils.lang;

/**
 * 描述：HTTP接口的结果处理工具类
 *
 */

public class ResultUtil {
    public static ResultVO successResult(Object o) {
        return new ResultVO(0, "success", o);
    }

    public static ResultVO errorResult(Integer code, String message, Object o) {
        return new ResultVO(code, message, o);
    }

    /**
     * 将驼峰转为下划线分割的小写字符串
     * 这是为了兼容前端传入的驼峰参数
     * 注意数字一定是要用下划线分割的
     *
     * @return
     */
    public static String camelcaseToUnderline(String camelcase) {
        char[] chars = camelcase.toCharArray();
        String underLineStr = "";
        for (int i = 0; i < chars.length; i++) {
            int ascCode = Integer.valueOf(chars[i]);
            // 大写字母
            if (ascCode >= 65 && ascCode <= 90) {
                underLineStr += ("_" + chars[i]).toLowerCase();
                // 数字(这里没考虑大于10的数字，只考虑0-9)
            } else if (ascCode >= 48 && ascCode <= 59) {
                underLineStr += "_" + chars[i];
            } else {
                underLineStr += chars[i];
            }
        }
        return underLineStr;
    }
}
