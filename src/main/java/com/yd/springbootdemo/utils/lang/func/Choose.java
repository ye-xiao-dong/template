package com.yd.springbootdemo.utils.lang.func;


@FunctionalInterface
public interface Choose<T> {
    T choose(T t, T t1);
}
