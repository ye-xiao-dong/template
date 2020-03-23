package com.yd.springbootdemo.utils.convert.impl;

import com.yd.springbootdemo.utils.convert.AbstractConverter;
import com.yd.springbootdemo.utils.text.CharsetUtil;

import java.nio.charset.Charset;

/**
 * 编码对象转换器
 *
 *
 */
public class CharsetConverter extends AbstractConverter<Charset>{
	private static final long serialVersionUID = 1L;

	@Override
	protected Charset convertInternal(Object value) {
		return CharsetUtil.charset(convertToStr(value));
	}

}
