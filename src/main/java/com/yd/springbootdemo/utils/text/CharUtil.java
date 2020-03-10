package com.yd.springbootdemo.utils.text;

/**
 * 字符工具类
 * 部分工具来自于Apache Commons系列
 *
 * @author： 叶小东
 * @date： 2019/12/23 19:35
 */
public class CharUtil {
    public static final char BACKSLASH = '\\';
    public static final char DELIM_START = '{';

    /**
     * 是否空白符
     * 空白符包括空格、制表符、全角空格和不间断空格
     *
     * @param c 字符
     * @return 是否空白符
     */
    public static boolean isBlankChar(char c) {
        return isBlankChar((int) c);
    }

    /**
     * 是否空白符
     * 空白符包括空格、制表符、全角空格和不间断空格
     *
     * @param c 字符
     * @return 是否空白符
     */
    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == '\ufeff' || c == '\u202a';
    }

}


