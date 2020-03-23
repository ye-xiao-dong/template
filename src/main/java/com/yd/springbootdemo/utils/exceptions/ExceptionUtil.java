package com.yd.springbootdemo.utils.exceptions;

import com.yd.springbootdemo.utils.text.StrUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;

/**
 * 异常工具类
 * 
 *
 *
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
			return StrUtil.NULL;
		}
		return StrUtil.format("{}: {}", e.getClass().getSimpleName(), e.getMessage());
	}

	/**
	 * 获得消息，调用异常类的getMessage方法
	 * 
	 * @param e 异常
	 * @return 消息
	 */
	public static String getSimpleMessage(Throwable e) {
		return (null == e) ? StrUtil.NULL : e.getMessage();
	}

	/**
	 * 使用运行时异常包装编译异常<br>
	 * 
	 * 如果
	 * 
	 * @param throwable 异常
	 * @return 运行时异常
	 */
	public static RuntimeException wrapRuntime(Throwable throwable) {
		if (throwable instanceof RuntimeException) {
			return (RuntimeException) throwable;
		}
		return new RuntimeException(throwable);
	}


	/**
	 * 包装异常并重新抛出此异常<br>
	 * {@link RuntimeException} 和{@link Error} 直接抛出，其它检查异常包装为{@link UndeclaredThrowableException} 后抛出
	 * 
	 * @param throwable 异常
	 */
	public static void wrapAndThrow(Throwable throwable) {
		if (throwable instanceof RuntimeException) {
			throw (RuntimeException) throwable;
		}
		if (throwable instanceof Error) {
			throw (Error) throwable;
		}
		throw new UndeclaredThrowableException(throwable);
	}


	/**
	 * 判断是否由指定异常类引起
	 * 
	 * @param throwable 异常
	 * @param causeClasses 定义的引起异常的类
	 * @return 是否由指定异常类引起
	 * @since 4.1.13
	 */
	@SuppressWarnings("unchecked")
	public static boolean isCausedBy(Throwable throwable, Class<? extends Exception>... causeClasses) {
		return null != getCausedBy(throwable, causeClasses);
	}

	/**
	 * 获取由指定异常类引起的异常
	 * 
	 * @param throwable 异常
	 * @param causeClasses 定义的引起异常的类
	 * @return 是否由指定异常类引起
	 * @since 4.1.13
	 */
	@SuppressWarnings("unchecked")
	public static Throwable getCausedBy(Throwable throwable, Class<? extends Exception>... causeClasses) {
		Throwable cause = throwable;
		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeClasses) {
				if (causeClass.isInstance(cause)) {
					return cause;
				}
			}
			cause = cause.getCause();
		}
		return null;
	}


	/**
	 * 获取异常链上所有异常的集合，如果{@link Throwable} 对象没有cause，返回只有一个节点的List<br>
	 * 如果传入null，返回空集合
	 * 
	 * <p>
	 * 此方法来自Apache-Commons-Lang3
	 * </p>
	 * 
	 * @param throwable 异常对象，可以为null
	 * @return 异常链中所有异常集合
	 * @since 4.6.2
	 */
	public static List<Throwable> getThrowableList(Throwable throwable) {
		final List<Throwable> list = new ArrayList<Throwable>();
		while (throwable != null && false == list.contains(throwable)) {
			list.add(throwable);
			throwable = throwable.getCause();
		}
		return list;
	}

	/**
	 * 获取异常链中最尾端的异常，即异常最早发生的异常对象。<br>
	 * 此方法通过调用{@link Throwable#getCause()} 直到没有cause为止，如果异常本身没有cause，返回异常本身<br>
	 * 传入null返回也为null
	 * 
	 * <p>
	 * 此方法来自Apache-Commons-Lang3
	 * </p>
	 * 
	 * @param throwable 异常对象，可能为null
	 * @return 最尾端异常，传入null参数返回也为null
	 */
	public static Throwable getRootCause(final Throwable throwable) {
		final List<Throwable> list = getThrowableList(throwable);
		return list.size() < 1 ? null : list.get(list.size() - 1);
	}

	/**
	 * 获取异常链中最尾端的异常的消息，消息格式为：{SimpleClassName}: {ThrowableMessage}
	 * 
	 * @param th 异常
	 * @return 消息
	 * @since 4.6.2
	 */
	public static String getRootCauseMessage(final Throwable th) {
		return getMessage(getRootCause(th));
	}

	/**
	 * 获取printStackTrace打印的字符串
	 * @param e
	 * @return
	 */
	public static String getAllPrintString (Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
		} finally {
			try {
				sw.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			pw.close();
		}
		return sw.toString();
	}
}
