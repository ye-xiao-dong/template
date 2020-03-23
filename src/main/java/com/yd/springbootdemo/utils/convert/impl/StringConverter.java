package com.yd.springbootdemo.utils.convert.impl;

import com.yd.springbootdemo.utils.convert.AbstractConverter;

/**
 * 字符串转换器
 *
 *
 */
public class StringConverter extends AbstractConverter<String>{
	private static final long serialVersionUID = 1L;

	@Override
	protected String convertInternal(Object value) {
		return convertToStr(value);
	}

}
