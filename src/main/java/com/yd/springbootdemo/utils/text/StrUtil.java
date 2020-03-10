package com.yd.springbootdemo.utils.text;

import com.yd.springbootdemo.utils.collection.ArrayUtil;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.function.Predicate;

/**
 * @author： 叶小东
 * @date： 2019/12/23 19:59
 */
public class StrUtil {

    public static final char C_BACKSLASH = CharUtil.BACKSLASH;
    public static final char C_DELIM_START = CharUtil.DELIM_START;
    public static final String EMPTY = "";
    public static final String EMPTY_JSON = "{}";

    /**
     * 字符串是否为空，空的定义如下:
     * 1、为null
     * 2、为""
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 字符串是否为空白 空白的定义如下：
     * 1、为null
     * 2、为不可见字符（如空格）
     * 3、""
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public static boolean isBlank(CharSequence str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            if (false == CharUtil.isBlankChar(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否包含空字符串
     *
     * @param strs 字符串列表
     * @return 是否包含空字符串
     */
    public static boolean hasBlank(CharSequence... strs) {
        if (ArrayUtil.isEmpty(strs)) {
            return true;
        }
        for (CharSequence str : strs) {
            if (isBlank(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将对象转为字符串
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组
     * 2、对象数组会调用Arrays.toString方法
     *
     * @param obj 对象
     * @return 字符串
     */
    public static String utf8Str(Object obj) {
        return str(obj, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 将对象转为字符串
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组
     * 2、对象数组会调用Arrays.toString方法
     *
     * @param obj 对象
     * @param charset 字符集
     * @return 字符串
     */
    public static String str(Object obj, Charset charset) {
        if (null == obj) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        else if (obj instanceof byte[]) {
            return str((byte[]) obj, charset);
        }
        else if (obj instanceof Byte[]) {
            return str((Byte[]) obj, charset);
        }
        else if (obj instanceof ByteBuffer) {
            return str((ByteBuffer) obj, charset);
        }
        else if (ArrayUtil.isArray(obj)) {
            return ArrayUtil.toString(obj);
        }
        return obj.toString();
    }

    /**
     * 解码字节码
     *
     * @param data 字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public static String str(byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }
        if (null == charset) {
            return new String(data);
        }
        return new String(data, charset);
    }

    /**
     * 解码字节码
     *
     * @param data 字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public static String str(Byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }

        byte[] bytes = new byte[data.length];
        Byte dataByte;
        for (int i = 0; i < data.length; i++) {
            dataByte = data[i];
            bytes[i] = (null == dataByte) ? -1 : dataByte.byteValue();
        }
        return str(bytes, charset);
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data 数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String str(ByteBuffer data, Charset charset) {
        if (null == charset) {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }

    /**
     * {@link CharSequence} 转为字符串，null安全
     *
     * @param cs {@link CharSequence}
     * @return 字符串
     */
    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }

    /**
     * 格式化文本, {} 表示占位符
     * 此方法只是简单将占位符 {} 按照顺序替换为参数
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可
     * 例：
     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b
     * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is \{} for a
     * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params 参数值
     * @return 格式化后的文本
     */
    public static String format(CharSequence template, Object... params) {
        if (null == template) {
            return null;
        }
        if (ArrayUtil.isEmpty(params) || isBlank(template)) {
            return template.toString();
        }
        return StrFormatter.format(template.toString(), params);
    }

    /**
     * 比较两个字符串是否相等。
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @param ignoreCase 是否忽略大小写
     * @return 如果两个字符串相同，或者都是null，则返回true
     */
    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (null == str1) {
            // 只有两个都为null才判断相等
            return str2 == null;
        }
        if (null == str2) {
            // 字符串2空，字符串1非空，直接false
            return false;
        }
        if (ignoreCase) {
            return str1.toString().equalsIgnoreCase(str2.toString());
        }
        else {
            return str1.equals(str2);
        }
    }

    /**
     * 字符串的每一个字符是否都与定义的匹配器匹配
     *
     * @param value 字符串
     * @param matcher 匹配器
     * @return 是否全部匹配
     */
    public static boolean isAllCharMatch(CharSequence value, Predicate<Character> matcher) {
        if (StrUtil.isBlank(value)) {
            return false;
        }
        int len = value.length();
        boolean isAllMatch = true;
        for (int i = 0; i < len; i++) {
            isAllMatch &= matcher.test(value.charAt(i));
        }
        return isAllMatch;
    }

    /**
     * 改进JDK subString
     * index从0开始计算，最后一个字符为-1
     * 如果from和to位置一样，返回 ""
     * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length
     * 如果经过修正的index中from大于to，则互换from和to example:
     * abcdefgh 2 3 =》 c
     * abcdefgh 2 -3 =》 cde
     *
     * @param str String
     * @param fromIndex 开始的index（包括）
     * @param toIndex 结束的index（不包括）
     * @return 字串
     */
    public static String sub(CharSequence str, int fromIndex, int toIndex) {
        if (isEmpty(str)) {
            return str(str);
        }
        int len = str.length();
        if (fromIndex < 0) {
            fromIndex = len + fromIndex;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        }
        else if (fromIndex > len) {
            fromIndex = len;
        }
        if (toIndex < 0) {
            toIndex = len + toIndex;
            if (toIndex < 0) {
                toIndex = len;
            }
        }
        else if (toIndex > len) {
            toIndex = len;
        }
        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }
        if (fromIndex == toIndex) {
            return EMPTY;
        }
        return str.toString().substring(fromIndex, toIndex);
    }

}


