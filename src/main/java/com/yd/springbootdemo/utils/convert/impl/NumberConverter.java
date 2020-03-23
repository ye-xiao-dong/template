package com.yd.springbootdemo.utils.convert.impl;

import com.yd.springbootdemo.utils.convert.BoolUtil;
import com.yd.springbootdemo.utils.lang.NumUtil;
import com.yd.springbootdemo.utils.convert.AbstractConverter;
import com.yd.springbootdemo.utils.text.StrUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 数字转换器<br>
 * 支持类型为：<br>
 * <ul>
 * <li><code>java.lang.Byte</code></li>
 * <li><code>java.lang.Short</code></li>
 * <li><code>java.lang.Integer</code></li>
 * <li><code>java.lang.Long</code></li>
 * <li><code>java.lang.Float</code></li>
 * <li><code>java.lang.Double</code></li>
 * <li><code>java.math.BigDecimal</code></li>
 * <li><code>java.math.BigInteger</code></li>
 * </ul>
 * 
 *
 *
 */
public class NumberConverter extends AbstractConverter<Number> {
	private static final long serialVersionUID = 1L;

	private Class<? extends Number> targetType;

	public NumberConverter() {
		this.targetType = Number.class;
	}

	/**
	 * 构造<br>
	 * 
	 * @param clazz 需要转换的数字类型，默认 {@link Number}
	 */
	public NumberConverter(Class<? extends Number> clazz) {
		this.targetType = (null == clazz) ? Number.class : clazz;
	}

	@Override
	protected Number convertInternal(Object value) {
		final Class<?> targetType = this.targetType;
		if (Byte.class == targetType) {
			if (value instanceof Number) {
				return Byte.valueOf(((Number) value).byteValue());
			} else if(value instanceof Boolean) {
				return BoolUtil.toByteObj((Boolean)value);
			}
			final String valueStr = convertToStr(value);
			return StrUtil.isBlank(valueStr) ? null : Byte.valueOf(valueStr);
			
		} else if (Short.class == targetType) {
			if (value instanceof Number) {
				return Short.valueOf(((Number) value).shortValue());
			} else if(value instanceof Boolean) {
				return BoolUtil.toShortObj((Boolean)value);
			}
			final String valueStr = convertToStr(value);
			return StrUtil.isBlank(valueStr) ? null : Short.valueOf(valueStr);

		} else if (Integer.class == targetType) {
			if (value instanceof Number) {
				return Integer.valueOf(((Number) value).intValue());
			} else if(value instanceof Boolean) {
				return BoolUtil.toInteger((Boolean)value);
			}
			final String valueStr = convertToStr(value);
			return StrUtil.isBlank(valueStr) ? null : Integer.valueOf(NumUtil.parseInt(valueStr));
			
		} else if (AtomicInteger.class == targetType) {
			int intValue;
			if (value instanceof Number) {
				intValue = ((Number) value).intValue();
			} else if(value instanceof Boolean) {
				intValue = BoolUtil.toInt((Boolean)value);
			}
			final String valueStr = convertToStr(value);
			if (StrUtil.isBlank(valueStr)) {
				return null;
			}
			intValue = NumUtil.parseInt(valueStr);
			return new AtomicInteger(intValue);
		} else if (Long.class == targetType) {
			if (value instanceof Number) {
				return Long.valueOf(((Number) value).longValue());
			} else if(value instanceof Boolean) {
				return BoolUtil.toLongObj((Boolean)value);
			}
			final String valueStr = convertToStr(value);
			return StrUtil.isBlank(valueStr) ? null : Long.valueOf(NumUtil.parseLong(valueStr));

		} else if (AtomicLong.class == targetType) {
			long longValue;
			if (value instanceof Number) {
				longValue = ((Number) value).longValue();
			} else if(value instanceof Boolean) {
				longValue = BoolUtil.toLong((Boolean)value);
			}
			final String valueStr = convertToStr(value);
			if (StrUtil.isBlank(valueStr)) {
				return null;
			}
			longValue = NumUtil.parseLong(valueStr);
			return new AtomicLong(longValue);
			
		} else if (Float.class == targetType) {
			if (value instanceof Number) {
				return Float.valueOf(((Number) value).floatValue());
			} else if(value instanceof Boolean) {
				return BoolUtil.toFloatObj((Boolean)value);
			}
			final String valueStr = convertToStr(value);
			return StrUtil.isBlank(valueStr) ? null : Float.valueOf(valueStr);

		} else if (Double.class == targetType) {
			if (value instanceof Number) {
				return Double.valueOf(((Number) value).doubleValue());
			} else if(value instanceof Boolean) {
				return BoolUtil.toDoubleObj((Boolean)value);
			}
			final String valueStr = convertToStr(value);
			return StrUtil.isBlank(valueStr) ? null : Double.valueOf(valueStr);

		} else if (BigDecimal.class == targetType) {
			return toBigDecimal(value);

		} else if (BigInteger.class == targetType) {
			return toBigInteger(value);
			
		}else if(Number.class == targetType){
			if (value instanceof Number) {
				return (Number)value;
			} else if(value instanceof Boolean) {
				return BoolUtil.toInteger((Boolean)value);
			}
			final String valueStr = convertToStr(value);
			return StrUtil.isBlank(valueStr) ? null : NumUtil.parseNumber(valueStr);
		}

		throw new UnsupportedOperationException(StrUtil.format("Unsupport Number type: {}", this.targetType.getName()));
	}

	/**
	 * 转换为BigDecimal<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	private BigDecimal toBigDecimal(Object value) {
		if (value instanceof Long) {
			return new BigDecimal((Long) value);
		} else if (value instanceof Integer) {
			return new BigDecimal((Integer) value);
		} else if (value instanceof BigInteger) {
			return new BigDecimal((BigInteger) value);
		} else if(value instanceof Boolean) {
			return new BigDecimal((boolean)value ? 1 : 0);
		}

		//对于Double类型，先要转换为String，避免精度问题
		final String valueStr = convertToStr(value);
		if (StrUtil.isBlank(valueStr)) {
			return null;
		}
		return new BigDecimal(valueStr);
	}

	/**
	 * 转换为BigInteger<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	private BigInteger toBigInteger(Object value) {
		if (value instanceof Long) {
			return BigInteger.valueOf((Long) value);
		} else if(value instanceof Boolean) {
			return BigInteger.valueOf((boolean)value ? 1 : 0);
		}
		final String valueStr = convertToStr(value);
		if (StrUtil.isBlank(valueStr)) {
			return null;
		}
		return new BigInteger(valueStr);
	}

	@Override
	protected String convertToStr(Object value) {
		return StrUtil.trim(super.convertToStr(value));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<Number> getTargetType() {
		return (Class<Number>) this.targetType;
	}
}
