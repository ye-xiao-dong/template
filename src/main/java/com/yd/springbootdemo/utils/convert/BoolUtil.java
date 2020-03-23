package com.yd.springbootdemo.utils.convert;


import com.yd.springbootdemo.utils.collection.ArrayUtil;
import com.yd.springbootdemo.utils.convert.Convert;
import com.yd.springbootdemo.utils.text.StrUtil;

/**
 * Boolean类型相关工具类
 * 
 *
 * @since 4.1.16
 */
public class BoolUtil {

	/** 表示为真的字符串 */
	private static final String[] TRUE_ARRAY = { "true", "yes", "y", "t", "ok", "1", "on", "是", "对", "真", };

	/**
	 * 取相反值
	 * 
	 * @param bool Boolean值
	 * @return 相反的Boolean值
	 */
	public static Boolean negate(Boolean bool) {
		if (bool == null) {
			return null;
		}
		return bool.booleanValue() ? Boolean.FALSE : Boolean.TRUE;
	}

	/**
	 * 检查 {@code Boolean} 值是否为 {@code true}
	 *
	 * <pre>
	 *   BoolUtil.isTrue(Boolean.TRUE)  = true
	 *   BoolUtil.isTrue(Boolean.FALSE) = false
	 *   BoolUtil.isTrue(null)          = false
	 * </pre>
	 *
	 * @param bool 被检查的Boolean值
	 * @return 当值为true且非null时返回{@code true}
	 */
	public static boolean isTrue(Boolean bool) {
		return Boolean.TRUE.equals(bool);
	}

	/**
	 * 检查 {@code Boolean} 值是否为 {@code false}
	 *
	 * <pre>
	 *   BoolUtil.isFalse(Boolean.TRUE)  = false
	 *   BoolUtil.isFalse(Boolean.FALSE) = true
	 *   BoolUtil.isFalse(null)          = false
	 * </pre>
	 *
	 * @param bool 被检查的Boolean值
	 * @return 当值为false且非null时返回{@code true}
	 */
	public static boolean isFalse(Boolean bool) {
		return Boolean.FALSE.equals(bool);
	}

	/**
	 * 取相反值
	 * 
	 * @param bool Boolean值
	 * @return 相反的Boolean值
	 */
	public static boolean negate(boolean bool) {
		return bool ? false : true;
	}

	/**
	 * 转换字符串为boolean值
	 * 
	 * @param valueStr 字符串
	 * @return boolean值
	 */
	public static boolean toBoolean(String valueStr) {
		if (StrUtil.isNotBlank(valueStr)) {
			valueStr = valueStr.trim().toLowerCase();
			if (ArrayUtil.contains(TRUE_ARRAY, valueStr)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * boolean值转为int
	 * 
	 * @param value Boolean值
	 * @return int值
	 */
	public static int toInt(boolean value) {
		return value ? 1 : 0;
	}

	/**
	 * boolean值转为Integer
	 * 
	 * @param value Boolean值
	 * @return Integer值
	 */
	public static Integer toInteger(boolean value) {
		return Integer.valueOf(toInt(value));
	}

	/**
	 * boolean值转为char
	 * 
	 * @param value Boolean值
	 * @return char值
	 */
	public static char toChar(boolean value) {
		return (char) toInt(value);
	}

	/**
	 * boolean值转为Character
	 * 
	 * @param value Boolean值
	 * @return Character值
	 */
	public static Character toCharacter(boolean value) {
		return Character.valueOf(toChar(value));
	}

	/**
	 * boolean值转为byte
	 * 
	 * @param value Boolean值
	 * @return byte值
	 */
	public static byte toByte(boolean value) {
		return (byte) toInt(value);
	}

	/**
	 * boolean值转为Byte
	 * 
	 * @param value Boolean值
	 * @return Byte值
	 */
	public static Byte toByteObj(boolean value) {
		return Byte.valueOf(toByte(value));
	}

	/**
	 * boolean值转为long
	 * 
	 * @param value Boolean值
	 * @return long值
	 */
	public static long toLong(boolean value) {
		return (long) toInt(value);
	}

	/**
	 * boolean值转为Long
	 * 
	 * @param value Boolean值
	 * @return Long值
	 */
	public static Long toLongObj(boolean value) {
		return Long.valueOf(toLong(value));
	}

	/**
	 * boolean值转为short
	 * 
	 * @param value Boolean值
	 * @return short值
	 */
	public static short toShort(boolean value) {
		return (short) toInt(value);
	}

	/**
	 * boolean值转为Short
	 * 
	 * @param value Boolean值
	 * @return Short值
	 */
	public static Short toShortObj(boolean value) {
		return Short.valueOf(toShort(value));
	}

	/**
	 * boolean值转为float
	 * 
	 * @param value Boolean值
	 * @return float值
	 */
	public static float toFloat(boolean value) {
		return (float) toInt(value);
	}

	/**
	 * boolean值转为Float
	 * 
	 * @param value Boolean值
	 * @return float值
	 */
	public static Float toFloatObj(boolean value) {
		return Float.valueOf(toFloat(value));
	}

	/**
	 * boolean值转为double
	 * 
	 * @param value Boolean值
	 * @return double值
	 */
	public static double toDouble(boolean value) {
		return (double) toInt(value);
	}

	/**
	 * boolean值转为double
	 * 
	 * @param value Boolean值
	 * @return double值
	 */
	public static Double toDoubleObj(boolean value) {
		return Double.valueOf(toDouble(value));
	}

	/**
	 * 将boolean转换为字符串
	 *
	 * <pre>
	 *   BoolUtil.toString(true, "true", "false")   = "true"
	 *   BoolUtil.toString(false, "true", "false")  = "false"
	 * </pre>
	 *
	 * @param bool Boolean值
	 * @param trueString 当值为 {@code true}时返回此字符串, 可能为 {@code null}
	 * @param falseString 当值为 {@code false}时返回此字符串, 可能为 {@code null}
	 * @return 结果值
	 */
	public static String toString(boolean bool, String trueString, String falseString) {
		return bool ? trueString : falseString;
	}

	/**
	 * 对Boolean数组取与
	 *
	 * <pre>
	 *   BoolUtil.and(true, true)         = true
	 *   BoolUtil.and(false, false)       = false
	 *   BoolUtil.and(true, false)        = false
	 *   BoolUtil.and(true, true, false)  = false
	 *   BoolUtil.and(true, true, true)   = true
	 * </pre>
	 *
	 * @param array {@code Boolean}数组
	 * @return 取与为真返回{@code true}
	 */
	public static boolean and(boolean... array) {
		if (ArrayUtil.isEmpty(array)) {
			throw new IllegalArgumentException("The Array must not be empty !");
		}
		for (final boolean element : array) {
			if (false == element) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 对Boolean数组取与
	 *
	 * <pre>
	 *   BoolUtil.and(Boolean.TRUE, Boolean.TRUE)                 = Boolean.TRUE
	 *   BoolUtil.and(Boolean.FALSE, Boolean.FALSE)               = Boolean.FALSE
	 *   BoolUtil.and(Boolean.TRUE, Boolean.FALSE)                = Boolean.FALSE
	 *   BoolUtil.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)   = Boolean.TRUE
	 *   BoolUtil.and(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE) = Boolean.FALSE
	 *   BoolUtil.and(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)  = Boolean.FALSE
	 * </pre>
	 *
	 * @param array {@code Boolean}数组
	 * @return 取与为真返回{@code true}
	 */
	public static Boolean and(final Boolean... array) {
		if (ArrayUtil.isEmpty(array)) {
			throw new IllegalArgumentException("The Array must not be empty !");
		}
		final boolean[] primitive = Convert.convert(boolean[].class, array);
		return Boolean.valueOf(and(primitive));
	}

	/**
	 * 对Boolean数组取或
	 *
	 * <pre>
	 *   BoolUtil.or(true, true)          = true
	 *   BoolUtil.or(false, false)        = false
	 *   BoolUtil.or(true, false)         = true
	 *   BoolUtil.or(true, true, false)   = true
	 *   BoolUtil.or(true, true, true)    = true
	 *   BoolUtil.or(false, false, false) = false
	 * </pre>
	 *
	 * @param array {@code Boolean}数组
	 * @return 取或为真返回{@code true}
	 */
	public static boolean or(boolean... array) {
		if (ArrayUtil.isEmpty(array)) {
			throw new IllegalArgumentException("The Array must not be empty !");
		}
		for (final boolean element : array) {
			if (element) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 对Boolean数组取或
	 *
	 * <pre>
	 *   BoolUtil.or(Boolean.TRUE, Boolean.TRUE)                  = Boolean.TRUE
	 *   BoolUtil.or(Boolean.FALSE, Boolean.FALSE)                = Boolean.FALSE
	 *   BoolUtil.or(Boolean.TRUE, Boolean.FALSE)                 = Boolean.TRUE
	 *   BoolUtil.or(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)    = Boolean.TRUE
	 *   BoolUtil.or(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE)  = Boolean.TRUE
	 *   BoolUtil.or(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)   = Boolean.TRUE
	 *   BoolUtil.or(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE) = Boolean.FALSE
	 * </pre>
	 *
	 * @param array {@code Boolean}数组
	 * @return 取或为真返回{@code true}
	 */
	public static Boolean or(Boolean... array) {
		if (ArrayUtil.isEmpty(array)) {
			throw new IllegalArgumentException("The Array must not be empty !");
		}
		final boolean[] primitive = Convert.convert(boolean[].class, array);
		return Boolean.valueOf(or(primitive));
	}

	/**
	 * 对Boolean数组取异或
	 *
	 * <pre>
	 *   BoolUtil.xor(true, true)   = false
	 *   BoolUtil.xor(false, false) = false
	 *   BoolUtil.xor(true, false)  = true
	 *   BoolUtil.xor(true, true)   = false
	 *   BoolUtil.xor(false, false) = false
	 *   BoolUtil.xor(true, false)  = true
	 * </pre>
	 *
	 * @param array {@code boolean}数组
	 * @return 如果异或计算为true返回 {@code true}
	 */
	public static boolean xor(boolean... array) {
		if (ArrayUtil.isEmpty(array)) {
			throw new IllegalArgumentException("The Array must not be empty");
		}

		boolean result = false;
		for (final boolean element : array) {
			result ^= element;
		}

		return result;
	}

	/**
	 * 对Boolean数组取异或
	 *
	 * <pre>
	 *   BoolUtil.xor(new Boolean[] { Boolean.TRUE, Boolean.TRUE })   = Boolean.FALSE
	 *   BoolUtil.xor(new Boolean[] { Boolean.FALSE, Boolean.FALSE }) = Boolean.FALSE
	 *   BoolUtil.xor(new Boolean[] { Boolean.TRUE, Boolean.FALSE })  = Boolean.TRUE
	 * </pre>
	 *
	 * @param array {@code Boolean} 数组
	 * @return 异或为真取{@code true}
	 */
	public static Boolean xor(Boolean... array) {
		if (ArrayUtil.isEmpty(array)) {
			throw new IllegalArgumentException("The Array must not be empty !");
		}
		final boolean[] primitive = Convert.convert(boolean[].class, array);
		return Boolean.valueOf(xor(primitive));
	}

	/**
	 * 给定类是否为Boolean或者boolean
	 * 
	 * @param clazz 类
	 * @return 是否为Boolean或者boolean
	 * @since 4.5.2
	 */
	public static boolean isBoolean(Class<?> clazz) {
		return (clazz == Boolean.class || clazz == boolean.class);
	}
}
