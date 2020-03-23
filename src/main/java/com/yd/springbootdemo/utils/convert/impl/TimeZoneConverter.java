package com.yd.springbootdemo.utils.convert.impl;

import com.yd.springbootdemo.utils.convert.AbstractConverter;

import java.util.TimeZone;

/**
 * TimeZone转换器
 *
 *
 */
public class TimeZoneConverter extends AbstractConverter<TimeZone>{
	private static final long serialVersionUID = 1L;

	@Override
	protected TimeZone convertInternal(Object value) {
		return TimeZone.getTimeZone(convertToStr(value));
	}

}
