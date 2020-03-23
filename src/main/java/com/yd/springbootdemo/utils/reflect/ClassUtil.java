package com.yd.springbootdemo.utils.reflect;


import com.yd.springbootdemo.utils.collection.ArrayUtil;
import com.yd.springbootdemo.utils.convert.BasicType;
import com.yd.springbootdemo.utils.lang.Assert;
import com.yd.springbootdemo.utils.text.StrUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Locale;

/**
 * 类工具类 <br>
 * 
 *
 *
 */
public class ClassUtil {

	/**
	 * {@code null}安全的获取对象类型
	 * 
	 * @param <T> 对象类型
	 * @param obj 对象，如果为{@code null} 返回{@code null}
	 * @return 对象类型，提供对象如果为{@code null} 返回{@code null}
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(T obj) {
		return ((null == obj) ? null : (Class<T>) obj.getClass());
	}

	/**
	 * 获得对象数组的类数组
	 * 
	 * @param objects 对象数组，如果数组中存在{@code null}元素，则此元素被认为是Object类型
	 * @return 类数组
	 */
	public static Class<?>[] getClasses(Object... objects) {
		Class<?>[] classes = new Class<?>[objects.length];
		Object obj;
		for (int i = 0; i < objects.length; i++) {
			obj = objects[i];
			classes[i] = (null == obj) ? Object.class : obj.getClass();
		}
		return classes;
	}

	/**
	 * 指定类是否与给定的类名相同
	 * 
	 * @param clazz 类
	 * @param className 类名，可以是全类名（包含包名），也可以是简单类名（不包含包名）
	 * @param ignoreCase 是否忽略大小写
	 * @return 指定类是否与给定的类名相同
	 * @since 3.0.7
	 */
	public static boolean equals(Class<?> clazz, String className, boolean ignoreCase) {
		if (null == clazz || StrUtil.isBlank(className)) {
			return false;
		}
		if (ignoreCase) {
			return className.equalsIgnoreCase(clazz.getName()) || className.equalsIgnoreCase(clazz.getSimpleName());
		} else {
			return className.equals(clazz.getName()) || className.equals(clazz.getSimpleName());
		}
	}

	/**
	 * 比较判断types1和types2两组类，如果types1中所有的类都与types2对应位置的类相同，或者是其父类或接口，则返回<code>true</code>
	 *
	 * @param types1 类组1
	 * @param types2 类组2
	 * @return 是否相同、父类或接口
	 */
	public static boolean isAllAssignableFrom(Class<?>[] types1, Class<?>[] types2) {
		if (ArrayUtil.isEmpty(types1) && ArrayUtil.isEmpty(types2)) {
			return true;
		}
		if (null == types1 || null == types2) {
			// 任何一个为null不相等（之前已判断两个都为null的情况）
			return false;
		}
		if (types1.length != types2.length) {
			return false;
		}

		Class<?> type1;
		Class<?> type2;
		for (int i = 0; i < types1.length; i++) {
			type1 = types1[i];
			type2 = types2[i];
			if (isBasicType(type1) && isBasicType(type2)) {
				// 原始类型和包装类型存在不一致情况
				if (BasicType.unWrap(type1) != BasicType.unWrap(type2)) {
					return false;
				}
			} else if (false == type1.isAssignableFrom(type2)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否为包装类型
	 *
	 * @param clazz 类
	 * @return 是否为包装类型
	 */
	public static boolean isPrimitiveWrapper(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return BasicType.wrapperPrimitiveMap.containsKey(clazz);
	}

	/**
	 * 是否为基本类型（包括包装类和原始类）
	 *
	 * @param clazz 类
	 * @return 是否为基本类型
	 */
	public static boolean isBasicType(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
	}

	/**
	 * 是否为简单值类型<br>
	 * 包括：原始类型,、String、other CharSequence, a Number, a Date, a URI, a URL, a Locale or a Class.
	 *
	 * @param clazz 类
	 * @return 是否为简单值类型
	 */
	public static boolean isSimpleValueType(Class<?> clazz) {
		return isBasicType(clazz) //
				|| clazz.isEnum() //
				|| CharSequence.class.isAssignableFrom(clazz) //
				|| Number.class.isAssignableFrom(clazz) //
				|| Date.class.isAssignableFrom(clazz) //
				|| clazz.equals(URI.class) //
				|| clazz.equals(URL.class) //
				|| clazz.equals(Locale.class) //
				|| clazz.equals(Class.class);//
	}


	/**
	 * 查找指定类中的所有字段（包括非public字段）， 字段不存在则返回<code>null</code>
	 * 
	 * @param clazz 被查找字段的类
	 * @param fieldName 字段名
	 * @return 字段
	 * @throws SecurityException 安全异常
	 */
	public static Field getDeclaredField(Class<?> clazz, String fieldName) throws SecurityException {
		if (null == clazz || StrUtil.isBlank(fieldName)) {
			return null;
		}
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查找指定类中的所有字段（包括非public字段)
	 * 
	 * @param clazz 被查找字段的类
	 * @return 字段
	 * @throws SecurityException 安全异常
	 */
	public static Field[] getDeclaredFields(Class<?> clazz) throws SecurityException {
		if (null == clazz) {
			return null;
		}
		return clazz.getDeclaredFields();
	}


	/**
	 * 是否为静态方法
	 * 
	 * @param method 方法
	 * @return 是否为静态方法
	 */
	public static boolean isStatic(Method method) {
		Assert.notNull(method, "Method to provided is null.");
		return Modifier.isStatic(method.getModifiers());
	}

	/**
	 * 设置方法为可访问
	 * 
	 * @param method 方法
	 * @return 方法
	 */
	public static Method setAccessible(Method method) {
		if (null != method && false == method.isAccessible()) {
			method.setAccessible(true);
		}
		return method;
	}

	/**
	 * 是否为抽象类
	 * 
	 * @param clazz 类
	 * @return 是否为抽象类
	 */
	public static boolean isAbstract(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	/**
	 * 是否为标准的类<br>
	 * 这个类必须：
	 *
	 * <pre>
	 * 1、非接口
	 * 2、非抽象类
	 * 3、非Enum枚举
	 * 4、非数组
	 * 5、非注解
	 * 6、非原始类型（int, long等）
	 * </pre>
	 *
	 * @param clazz 类
	 * @return 是否为标准类
	 */
	public static boolean isNormalClass(Class<?> clazz) {
		return null != clazz //
				&& false == clazz.isInterface() //
				&& false == isAbstract(clazz) //
				&& false == clazz.isEnum() //
				&& false == clazz.isArray() //
				&& false == clazz.isAnnotation() //
				&& false == clazz.isSynthetic() //
				&& false == clazz.isPrimitive();//
	}

	/**
	 * 判断类是否为枚举类型
	 * 
	 * @param clazz 类
	 * @return 是否为枚举类型
	 * @since 3.2.0
	 */
	public static boolean isEnum(Class<?> clazz) {
		return null == clazz ? false : clazz.isEnum();
	}

	/**
	 * 获得给定类的第一个泛型参数
	 * 
	 * @param clazz 被检查的类，必须是已经确定泛型类型的类
	 * @return {@link Class}
	 */
	public static Class<?> getTypeArgument(Class<?> clazz) {
		return getTypeArgument(clazz, 0);
	}

	/**
	 * 获得给定类的泛型参数
	 * 
	 * @param clazz 被检查的类，必须是已经确定泛型类型的类
	 * @param index 泛型类型的索引号，既第几个泛型类型
	 * @return {@link Class}
	 */
	public static Class<?> getTypeArgument(Class<?> clazz, int index) {
		final Type argumentType = TypeUtil.getTypeArgument(clazz, index);
		if (null != argumentType && argumentType instanceof Class) {
			return (Class<?>) argumentType;
		}
		return null;
	}

	/**
	 * 获得给定类所在包的名称<br>
	 * 例如：<br>
	 * com..hutool.util.ClassUtil =》 com..hutool.util
	 * 
	 * @param clazz 类
	 * @return 包名
	 */
	public static String getPackage(Class<?> clazz) {
		if (clazz == null) {
			return StrUtil.EMPTY;
		}
		final String className = clazz.getName();
		int packageEndIndex = className.lastIndexOf(StrUtil.DOT);
		if (packageEndIndex == -1) {
			return StrUtil.EMPTY;
		}
		return className.substring(0, packageEndIndex);
	}

	/**
	 * 获得给定类所在包的路径<br>
	 * 例如：<br>
	 * com..hutool.util.ClassUtil =》 com//hutool/util
	 * 
	 * @param clazz 类
	 * @return 包名
	 */
	public static String getPackagePath(Class<?> clazz) {
		return getPackage(clazz).replace(StrUtil.C_DOT, StrUtil.C_SLASH);
	}

	/**
	 * 获取指定类型分的默认值<br>
	 * 默认值规则为：
	 * 
	 * <pre>
	 * 1、如果为原始类型，返回0
	 * 2、非原始类型返回{@code null}
	 * </pre>
	 * 
	 * @param clazz 类
	 * @return 默认值
	 * @since 3.0.8
	 */
	public static Object getDefaultValue(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			if (long.class == clazz) {
				return 0L;
			} else if (int.class == clazz) {
				return 0;
			} else if (short.class == clazz) {
				return (short) 0;
			} else if (char.class == clazz) {
				return (char) 0;
			} else if (byte.class == clazz) {
				return (byte) 0;
			} else if (double.class == clazz) {
				return 0D;
			} else if (float.class == clazz) {
				return 0f;
			} else if (boolean.class == clazz) {
				return false;
			}
		}

		return null;
	}

	/**
	 * 获得默认值列表
	 * 
	 * @param classes 值类型
	 * @return 默认值列表
	 * @since 3.0.9
	 */
	public static Object[] getDefaultValues(Class<?>... classes) {
		final Object[] values = new Object[classes.length];
		for (int i = 0; i < classes.length; i++) {
			values[i] = getDefaultValue(classes[i]);
		}
		return values;
	}

	/**
	 * 是否为JDK中定义的类或接口，判断依据：
	 * 
	 * <pre>
	 * 1、以java.、javax.开头的包名
	 * 2、ClassLoader为null
	 * </pre>
	 * 
	 * @param clazz 被检查的类
	 * @return 是否为JDK中定义的类或接口
	 * @since 4.6.5
	 */
	public static boolean isJdkClass(Class<?> clazz) {
		final Package objectPackage = clazz.getPackage();
		if(null == objectPackage) {
			return false;
		}
		final String objectPackageName = objectPackage.getName();
		if (objectPackageName.startsWith("java.") || objectPackageName.startsWith("javax.") || clazz.getClassLoader() == null) {
			return true;
		}
		return false;
	}
}