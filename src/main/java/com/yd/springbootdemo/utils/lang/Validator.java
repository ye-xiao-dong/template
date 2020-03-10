package com.yd.springbootdemo.utils.lang;

import com.yd.springbootdemo.utils.date.DateUtil;
import com.yd.springbootdemo.utils.exceptions.ValidateException;
import com.yd.springbootdemo.utils.text.ReUtil;
import com.yd.springbootdemo.utils.text.StrUtil;
import org.springframework.util.Assert;

import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字段验证器
 *
 * @author： 叶小东
 * @date： 2019/12/23 15:28
 */
public class Validator {

    private Validator() {
    }

    /** 英文字母 、数字和下划线 */
    public final static Pattern GENERAL = PatternPool.GENERAL;
    /** 数字 */
    public final static Pattern NUMBERS = PatternPool.NUMBERS;
    /** 分组 */
    public final static Pattern GROUP_VAR = PatternPool.GROUP_VAR;
    /** IP v4 */
    public final static Pattern IPV4 = PatternPool.IPV4;
    /** IP v6 */
    public final static Pattern IPV6 = PatternPool.IPV6;
    /** 货币 */
    public final static Pattern MONEY = PatternPool.MONEY;
    /** 邮件 */
    public final static Pattern EMAIL = PatternPool.EMAIL;
    /** 移动电话 */
    public final static Pattern MOBILE = PatternPool.MOBILE;
    /** 身份证号码 */
    public final static Pattern CITIZEN_ID = PatternPool.CITIZEN_ID;
    /** 邮编 */
    public final static Pattern ZIP_CODE = PatternPool.ZIP_CODE;
    /** 生日 */
    public final static Pattern BIRTHDAY = PatternPool.BIRTHDAY;
    /** URL */
    public final static Pattern URL = PatternPool.URL;
    /** Http URL */
    public final static Pattern URL_HTTP = PatternPool.URL_HTTP;
    /** 中文字、英文字母、数字和下划线 */
    public final static Pattern GENERAL_WITH_CHINESE = PatternPool.GENERAL_WITH_CHINESE;
    /** UUID */
    public final static Pattern UUID = PatternPool.UUID;
    /** 不带横线的UUID */
    public final static Pattern UUID_SIMPLE = PatternPool.UUID_SIMPLE;
    /** 中国车牌号码 */
    public final static Pattern PLATE_NUMBER = PatternPool.PLATE_NUMBER;

    /**
     * 验证是否为空
     * 对于String类型判定是否为empty(null 或 "")
     *
     * @param value 值
     * @return 是否为空
     * @return 是否为空
     */
    public static boolean isEmpty(Object value) {
        return (null == value || (value instanceof String && StrUtil.isEmpty((String) value)));
    }

    /**
     * 验证是否为非空
     * 对于String类型判定是否为empty(null 或 "")
     *
     * @param value 值
     * @return 是否为空
     * @return 是否为空
     */
    public static boolean isNotEmpty(Object value) {
        return false == isEmpty(value);
    }

    /**
     * 通过正则表达式验证
     *
     * @param regex 正则
     * @param value 值
     * @return 是否匹配正则
     */
    public static boolean isMactchRegex(String regex, CharSequence value) {
        return ReUtil.isMatch(regex, value);
    }

    /**
     * 通过正则表达式验证
     * 不符合正则抛出{@link ValidateException} 异常
     *
     * @param <T> 字符串类型
     * @param regex 正则
     * @param value 值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateMatchRegex(String regex, T value, String errorMsg) throws ValidateException {
        if (!isMactchRegex(regex, value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 通过正则表达式验证
     *
     * @param pattern 正则模式
     * @param value 值
     * @return 是否匹配正则
     */
    public static boolean isMactchRegex(Pattern pattern, CharSequence value) {
        return ReUtil.isMatch(pattern, value);
    }

    /**
     * 验证是否为英文字母 、数字和下划线
     *
     * @param value 值
     * @return 是否为英文字母 、数字和下划线
     */
    public static boolean isGeneral(CharSequence value) {
        return isMactchRegex(GENERAL, value);
    }

    /**
     * 验证是否为给定长度范围的英文字母 、数字和下划线
     *
     * @param value 值
     * @param min 最小长度，负数自动识别为0
     * @param max 最大长度，0或负数表示不限制最大长度
     * @return 是否为给定长度范围的英文字母 、数字和下划线
     */
    public static boolean isGeneral(CharSequence value, int min, int max) {
        String reg = "^\\w{" + min + "," + max + "}$";
        if (min < 0) {
            min = 0;
        }
        if (max <= 0) {
            reg = "^\\w{" + min + ",}$";
        }
        return isMactchRegex(reg, value);
    }

    /**
     * 验证是否为给定长度范围的英文字母 、数字和下划线
     *
     * @param <T> 字符串类型
     * @param value 值
     * @param min 最小长度，负数自动识别为0
     * @param max 最大长度，0或负数表示不限制最大长度
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateGeneral(T value, int min, int max, String errorMsg) throws ValidateException {
        if (false == isGeneral(value, min, max)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为给定最小长度的英文字母 、数字和下划线
     *
     * @param value 值
     * @param min 最小长度，负数自动识别为0
     * @return 是否为给定最小长度的英文字母 、数字和下划线
     */
    public static boolean isGeneral(CharSequence value, int min) {
        return isGeneral(value, min, 0);
    }

    /**
     * 验证是否为给定最小长度的英文字母 、数字和下划线
     *
     * @param <T> 字符串类型
     * @param value 值
     * @param min 最小长度，负数自动识别为0
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateGeneral(T value, int min, String errorMsg) throws ValidateException {
        return validateGeneral(value, min, 0, errorMsg);
    }

    /**
     * 判断字符串是否全部为字母组成，包括大写和小写字母和汉字
     *
     * @param value 值
     * @return 是否全部为字母组成，包括大写和小写字母和汉字
     */
    public static boolean isLetter(CharSequence value) {
        return StrUtil.isAllCharMatch(value,Character::isLetter);
    }

    /**
     * 验证是否全部为字母组成，包括大写和小写字母和汉字
     *
     * @param <T> 字符串类型
     * @param value 表单值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateLetter(T value, String errorMsg) throws ValidateException {
        if (false == isLetter(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 判断字符串是否全部为大写字母
     *
     * @param value 值
     * @return 是否全部为大写字母
     */
    public static boolean isUpperCase(CharSequence value) {
        return StrUtil.isAllCharMatch(value, Character::isUpperCase);
    }

    /**
     * 验证字符串是否全部为大写字母
     *
     * @param <T> 字符串类型
     * @param value 表单值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateUpperCase(T value, String errorMsg) throws ValidateException {
        if (false == isUpperCase(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 判断字符串是否全部为小写字母
     *
     * @param value 值
     * @return 是否全部为小写字母
     */
    public static boolean isLowerCase(CharSequence value) {
        return StrUtil.isAllCharMatch(value, Character::isLowerCase);
    }

    /**
     * 验证字符串是否全部为小写字母
     *
     * @param <T> 字符串类型
     * @param value 表单值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateLowerCase(T value, String errorMsg) throws ValidateException {
        if (false == isLowerCase(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证该字符串是否是数字
     * 是否为数字，支持包括：
     *
     * 1、10进制
     * 2、16进制数字（0x开头）
     * 3、科学计数法形式（1234E3）
     * 4、类型标识形式（123D）
     * 5、正负数标识形式（+123、-234）
     *
     * @param str 字符串值
     * @return 是否为数字
     */
    public static boolean isNumber(String str) {
        if (StrUtil.isBlank(str)) {
            return false;
        }
        char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
        if (sz > start + 1) {
            if (chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
                int i = start + 2;
                if (i == sz) {
                    return false; // str == "0x"
                }
                // checking hex (it can't be anything else)
                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                }
                return true;
            }
        }
        sz--;
        // don't want to loop to the last char, check it afterwords
        // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            }
            else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                hasDecPoint = true;
            }
            else if (chars[i] == 'e' || chars[i] == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return false;
                }
                if (false == foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            }
            else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false;
                // we need a digit after the E
            }
            else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                // no type qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                // can't have an E at the last byte
                return false;
            }
            if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                // single trailing decimal point after non-exponent is ok
                return foundDigit;
            }
            if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l' || chars[i] == 'L') {
                // not allowing L with an exponent
                return foundDigit && !hasExp;
            }
            // last character is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
        return false == allowSigns && foundDigit;
    }

    /**
     * 验证是否为数字
     *
     * @param value 表单值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static String validateNumber(String value, String errorMsg) throws ValidateException {
        if (false == isNumber(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证该字符串是否是字母（包括大写和小写字母）
     *
     * @param value 字符串内容
     * @return 是否是字母（包括大写和小写字母）
     */
    public static boolean isWord(CharSequence value) {
        return isMactchRegex(PatternPool.WORD, value);
    }

    /**
     * 验证是否为字母（包括大写和小写字母）
     *
     * @param <T> 字符串类型
     * @param value 表单值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateWord(T value, String errorMsg) throws ValidateException {
        if (false == isWord(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为货币
     *
     * @param value 值
     * @return 是否为货币
     */
    public static boolean isMoney(CharSequence value) {
        return isMactchRegex(MONEY, value);
    }

    /**
     * 验证是否为货币
     *
     * @param <T> 字符串类型
     * @param value 值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateMoney(T value, String errorMsg) throws ValidateException {
        if (false == isMoney(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为邮政编码（中国）
     *
     * @param value 值
     * @return 是否为邮政编码（中国）
     */
    public static boolean isZipCode(CharSequence value) {
        return isMactchRegex(ZIP_CODE, value);
    }

    /**
     * 验证是否为邮政编码（中国）
     *
     * @param <T> 字符串类型
     * @param value 表单值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateZipCode(T value, String errorMsg) throws ValidateException {
        if (false == isZipCode(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为可用邮箱地址
     *
     * @param value 值
     * @return 否为可用邮箱地址
     */
    public static boolean isEmail(CharSequence value) {
        return isMactchRegex(EMAIL, value);
    }

    /**
     * 验证是否为可用邮箱地址
     *
     * @param <T> 字符串类型
     * @param value 值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateEmail(T value, String errorMsg) throws ValidateException {
        if (false == isEmail(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为手机号码（中国）
     *
     * @param value 值
     * @return 是否为手机号码（中国）
     */
    public static boolean isMobile(CharSequence value) {
        return isMactchRegex(MOBILE, value);
    }

    /**
     * 验证是否为手机号码（中国）
     *
     * @param <T> 字符串类型
     * @param value 值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateMobile(T value, String errorMsg) throws ValidateException {
        if (false == isMobile(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为身份证号码（18位中国）
     * 出生日期只支持到到2999年
     *
     * @param value 值
     * @return 是否为身份证号码（18位中国）
     */
    public static boolean isCitizenId(CharSequence value) {
        return isMactchRegex(CITIZEN_ID, value);
    }

    /**
     * 验证是否为身份证号码（18位中国）
     * 出生日期只支持到到2999年
     *
     * @param <T> 字符串类型
     * @param value 值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateCitizenIdNumber(T value, String errorMsg) throws ValidateException {
        if (false == isCitizenId(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为生日
     *
     * @param year 年，从1900年开始计算
     * @param month 月，从1开始计数
     * @param day 日，从1开始计数
     * @return 是否为生日
     */
    public static boolean isBirthday(int year, int month, int day) {
        // 验证年
        int thisYear = DateUtil.thisYear();
        if (year < 1900 || year > thisYear) {
            return false;
        }
        // 验证月
        if (month < 1 || month > 12) {
            return false;
        }
        // 验证日
        if (day < 1 || day > 31) {
            return false;
        }
        if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
            return false;
        }
        if (month == 2) {
            if (day > 29 || (day == 29 && false == DateUtil.isLeapYear(year))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证是否为生日
     * 只支持以下几种格式：
     * yyyyMMdd
     * yyyy-MM-dd
     * yyyy/MM/dd
     * yyyy.MM.dd
     * yyyy年MM月dd日
     *
     * @param value 值
     * @return 是否为生日
     */
    public static boolean isBirthday(CharSequence value) {
        if (isMactchRegex(BIRTHDAY, value)) {
            Matcher matcher = BIRTHDAY.matcher(value);
            if (matcher.find()) {
                int year = Integer.parseInt(matcher.group(1));
                int month = Integer.parseInt(matcher.group(3));
                int day = Integer.parseInt(matcher.group(5));
                return isBirthday(year, month, day);
            }
        }
        return false;
    }

    /**
     * 验证验证是否为生日
     *
     * @param <T> 字符串类型
     * @param value 值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateBirthday(T value, String errorMsg) throws ValidateException {
        if (false == isBirthday(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为IPV4地址
     *
     * @param value 值
     * @return 是否为IPV4地址
     */
    public static boolean isIpv4(CharSequence value) {
        return isMactchRegex(IPV4, value);
    }

    /**
     * 验证是否为IPV4地址
     *
     * @param <T> 字符串类型
     * @param value 值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateIpv4(T value, String errorMsg) throws ValidateException {
        if (false == isIpv4(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为IPV6地址
     *
     * @param value 值
     * @return 是否为IPV6地址
     */
    public static boolean isIpv6(CharSequence value) {
        return isMactchRegex(IPV6, value);
    }

    /**
     * 验证是否为IPV6地址
     *
     * @param <T> 字符串类型
     * @param value 值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateIpv6(T value, String errorMsg) throws ValidateException {
        if (false == isIpv6(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为MAC地址
     *
     * @param value 值
     * @return 是否为MAC地址
     */
    public static boolean isMac(CharSequence value) {
        return isMactchRegex(PatternPool.MAC_ADDRESS, value);
    }

    /**
     * 验证是否为MAC地址
     *
     * @param <T> 字符串类型
     * @param value 值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateMac(T value, String errorMsg) throws ValidateException {
        if (false == isMac(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为中国车牌号
     *
     * @param value 值
     * @return 是否为中国车牌号
     */
    public static boolean isPlateNumber(CharSequence value) {
        return isMactchRegex(PLATE_NUMBER, value);
    }

    /**
     * 验证是否为中国车牌号
     *
     * @param <T> 字符串类型
     * @param value 值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validatePlateNumber(T value, String errorMsg) throws ValidateException {
        if (false == isPlateNumber(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为URL
     *
     * @param value 值
     * @return 是否为URL
     */
    public static boolean isUrl(CharSequence value) {
        try {
            new java.net.URL(StrUtil.str(value));
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

    /**
     * 验证是否为URL
     *
     * @param <T> 字符串类型
     * @param value 值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateUrl(T value, String errorMsg) throws ValidateException {
        if (false == isUrl(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为汉字
     *
     * @param value 值
     * @return 是否为汉字
     */
    public static boolean isChinese(CharSequence value) {
        return isMactchRegex("^" + ReUtil.RE_CHINESE + "+$", value);
    }

    /**
     * 验证是否为汉字
     *
     * @param <T> 字符串类型
     * @param value 表单值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateChinese(T value, String errorMsg) throws ValidateException {
        if (false == isChinese(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为中文字、英文字母、数字和下划线
     *
     * @param value 值
     * @return 是否为中文字、英文字母、数字和下划线
     */
    public static boolean isGeneralWithChinese(CharSequence value) {
        return isMactchRegex(GENERAL_WITH_CHINESE, value);
    }

    /**
     * 验证是否为中文字、英文字母、数字和下划线
     *
     * @param <T> 字符串类型
     * @param value 值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateGeneralWithChinese(T value, String errorMsg) throws ValidateException {
        if (false == isGeneralWithChinese(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为UUID
     * 包括带横线标准格式和不带横线的简单模式
     *
     * @param value 值
     * @return 是否为UUID
     */
    public static boolean isUUID(CharSequence value) {
        return isMactchRegex(UUID, value) || isMactchRegex(UUID_SIMPLE, value);
    }

    /**
     * 验证是否为UUID
     * 包括带横线标准格式和不带横线的简单模式
     *
     * @param <T> 字符串类型
     * @param value 值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateUUID(T value, String errorMsg) throws ValidateException {
        if (false == isUUID(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 验证是否为Hex（16进制）字符串
     *
     * @param value 值
     * @return 是否为Hex（16进制）字符串
     */
    public static boolean isHex(CharSequence value) {
        return isMactchRegex(PatternPool.HEX, value);
    }

    /**
     * 验证是否为Hex（16进制）字符串
     *
     * @param <T> 字符串类型
     * @param value 值
     * @param errorMsg 验证错误的信息
     * @return 验证后的值
     * @throws ValidateException 验证异常
     */
    public static <T extends CharSequence> T validateHex(T value, String errorMsg) throws ValidateException {
        if (false == isHex(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    /**
     * 检查给定的数字是否在指定范围内
     *
     * @param value 值
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 是否满足
     */
    public static boolean isBetween(Number value, Number min, Number max) {
        Assert.notNull(value);
        Assert.notNull(min);
        Assert.notNull(max);
        final double doubleValue = value.doubleValue();
        return (doubleValue >= min.doubleValue()) && (doubleValue <= max.doubleValue());
    }

    /**
     * 检查给定的数字是否在指定范围内
     *
     * @param value 值
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @param errorMsg 验证错误的信息
     * @throws ValidateException 验证异常
     */
    public static void validateBetween(Number value, Number min, Number max, String errorMsg) throws ValidateException {
        if (false == isBetween(value, min, max)) {
            throw new ValidateException(errorMsg);
        }
    }
}


