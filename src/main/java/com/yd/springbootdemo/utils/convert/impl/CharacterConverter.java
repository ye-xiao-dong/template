package com.yd.springbootdemo.utils.convert.impl;

import com.yd.springbootdemo.utils.convert.BoolUtil;
import com.yd.springbootdemo.utils.convert.AbstractConverter;
import com.yd.springbootdemo.utils.text.StrUtil;

/**
 * 字符转换器
 * 
 *
 *
 */
public class CharacterConverter extends AbstractConverter<Character> {
	private static final long serialVersionUID = 1L;

	@Override
	protected Character convertInternal(Object value) {
		if (char.class == value.getClass()) {
			return Character.valueOf((char) value);
		} else if (value instanceof Boolean) {
			return BoolUtil.toCharacter((Boolean) value);
		} else if (boolean.class == value.getClass()) {
			return BoolUtil.toCharacter((boolean) value);
		} else {
			final String valueStr = convertToStr(value);
			if (StrUtil.isNotBlank(valueStr)) {
				return Character.valueOf(valueStr.charAt(0));
			}
		}
		return null;
	}

}
