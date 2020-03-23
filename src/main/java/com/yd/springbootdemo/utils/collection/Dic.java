package com.yd.springbootdemo.utils.collection;


import com.yd.springbootdemo.utils.convert.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 字典对象，扩充了HashMap中的方法
 *
 *
 */
public class Dic extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 6135423866861206530L;

	static final float DEFAULT_LOAD_FACTOR = 0.75f;
	static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

	/** 是否大小写不敏感 */
	private boolean caseInsensitive = false;

	// --------------------------------------------------------------- Static method start
	/**
	 * 创建Dict
	 * 
	 * @return Dic
	 */
	public static Dic create() {
		return new Dic();
	}

	// --------------------------------------------------------------- Static method end

	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public Dic() {
		this(false);
	}

	/**
	 * 构造
	 * 
	 * @param caseInsensitive 是否大小写不敏感
	 */
	public Dic(boolean caseInsensitive) {
		this(DEFAULT_INITIAL_CAPACITY, caseInsensitive);
	}

	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始容量
	 */
	public Dic(int initialCapacity) {
		this(initialCapacity, false);
	}

	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始容量
	 * @param caseInsensitive 是否大小写不敏感
	 */
	public Dic(int initialCapacity, boolean caseInsensitive) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR, caseInsensitive);
	}

	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始容量
	 * @param loadFactor 容量增长因子，0~1，即达到容量的百分之多少时扩容
	 */
	public Dic(int initialCapacity, float loadFactor) {
		this(initialCapacity, loadFactor, false);
	}

	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始容量
	 * @param loadFactor 容量增长因子，0~1，即达到容量的百分之多少时扩容
	 * @param caseInsensitive 是否大小写不敏感
	 * @since 4.5.16
	 */
	public Dic(int initialCapacity, float loadFactor, boolean caseInsensitive) {
		super(initialCapacity, loadFactor);
		this.caseInsensitive = caseInsensitive;
	}

	/**
	 * 构造
	 * 
	 * @param m Map
	 */
	public Dic(Map<String, Object> m) {
		super((null == m) ? new HashMap<String, Object>() : m);
	}
	// --------------------------------------------------------------- Constructor end




	/**
	 * 过滤Map保留指定键值对，如果键不存在跳过
	 * 
	 * @param keys 键列表
	 * @return Dic 结果
	 * @since 4.0.10
	 */
	public Dic filter(String... keys) {
		final Dic result = new Dic(keys.length, 1);

		for (String key : keys) {
			if (this.containsKey(key)) {
				result.put(key, this.get(key));
			}
		}
		return result;
	}

	// -------------------------------------------------------------------- Set start
	/**
	 * 设置列
	 * 
	 * @param attr 属性
	 * @param value 值
	 * @return 本身
	 */
	public Dic set(String attr, Object value) {
		this.put(attr, value);
		return this;
	}

	/**
	 * 设置列，当键或值为null时忽略
	 * 
	 * @param attr 属性
	 * @param value 值
	 * @return 本身
	 */
	public Dic setIgnoreNull(String attr, Object value) {
		if (null != attr && null != value) {
			set(attr, value);
		}
		return this;
	}
	// -------------------------------------------------------------------- Set end

	// -------------------------------------------------------------------- Get start

	public Object getObj(String key) {
		return super.get(key);
	}

	/**
	 * 获得特定类型值
	 * 
	 * @param <T> 值类型
	 * @param attr 字段名
	 * @return 字段值
	 * @since 4.6.3
	 */
	public <T> T getBean(String attr) {
		return get(attr, null);
	}

	/**
	 * 获得特定类型值
	 * 
	 * @param <T> 值类型
	 * @param attr 字段名
	 * @param defaultValue 默认值
	 * @return 字段值
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String attr, T defaultValue) {
		final Object result = get(attr);
		return (T) (result != null ? result : defaultValue);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public String getStr(String attr) {
		return Convert.toStr(get(attr), null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Integer getInt(String attr) {
		return Convert.toInt(get(attr), null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Long getLong(String attr) {
		return Convert.toLong(get(attr), null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Float getFloat(String attr) {
		return Convert.toFloat(get(attr), null);
	}

	public Short getShort(String attr) {
		return Convert.toShort(get(attr), null);
	}

	public Character getChar(String attr) {
		return Convert.toChar(get(attr), null);
	}

	public Double getDouble(String attr) {
		return Convert.toDouble(get(attr), null);
	}

	public Byte getByte(String attr) {
		return Convert.toByte(get(attr), null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Boolean getBool(String attr) {
		return Convert.toBool(get(attr), null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public BigDecimal getBigDecimal(String attr) {
		return Convert.toBigDecimal(get(attr));
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public BigInteger getBigInteger(String attr) {
		return Convert.toBigInteger(get(attr));
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public byte[] getBytes(String attr) {
		return get(attr, null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Date getDate(String attr) {
		return get(attr, null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Time getTime(String attr) {
		return get(attr, null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Timestamp getTimestamp(String attr) {
		return get(attr, null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Number getNumber(String attr) {
		return get(attr, null);
	}
	// -------------------------------------------------------------------- Get end

	@Override
	public Dic put(String key, Object value) {
		super.put(customKey(key), value);
		return this;
	}

	@Override
	public Dic clone() {
		return (Dic) super.clone();
	}

	/**
	 * 将Key转为小写
	 * 
	 * @param key KEY
	 * @return 小写KEY
	 */
	private String customKey(String key) {
		if (this.caseInsensitive && null != key) {
			key = key.toLowerCase();
		}
		return key;
	}
}
