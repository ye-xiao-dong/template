package com.yd.springbootdemo.utils.collection;

import com.yd.springbootdemo.utils.lang.ObjUtil;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

/**
 * Map相关工具类
 *
 * @author： 叶小东
 * @date： 2019/12/23 11:35
 */
public class MapUtil {

    /**
     * 默认初始大小
     */
    public static final int DEFAULT_INITIAL_CAPACITY = 16;

    /**
     * 默认初始大小
     * 默认增长因子，当Map的size达到 容量*增长因子时，开始扩充Map
     */
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 过滤
     * 过滤过程通过传入的Editor实现来返回需要的元素内容，这个Filter实现可以实现以下功能：
     * 1、过滤出需要的对象，如果返回null表示这个元素对象抛弃
     *
     * @param <K>       Key类型
     * @param <V>       Value类型
     * @param map       Map
     * @param predicate 编辑器接口
     * @return 过滤后的Map
     */
    public static <K, V> Map<K, V> filter(Map<K, V> map, Predicate<Entry<K, V>> predicate) {
        if (null == map || null == predicate) {
            return map;
        }
        final Map<K, V> map2 = ObjUtil.clone(map);
        if (isEmpty(map2)) {
            return map2;
        }
        map2.clear();
        for (Entry<K, V> entry : map.entrySet()) {
            if (predicate.test(entry)) {
                map2.put(entry.getKey(), entry.getValue());
            }
        }
        return map2;
    }

    /**
     * 过滤Map保留指定键值对，如果键不存在跳过
     *
     * @param <K>  Key类型
     * @param <V>  Value类型
     * @param map  原始Map
     * @param keys 键列表
     * @return Map 结果，结果的Map类型与原Map保持一致
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> filter(Map<K, V> map, K... keys) {
        final Map<K, V> map2 = ObjUtil.clone(map);
        if (isEmpty(map2)) {
            return map2;
        }
        map2.clear();
        for (K key : keys) {
            if (map.containsKey(key)) {
                map2.put(key, map.get(key));
            }
        }
        return map2;
    }

    /**
     * Map是否为空
     *
     * @param map 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Map map) {
        return null == map || map.isEmpty();
    }

    /**
     * Map是否为非空
     *
     * @param map 集合
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Map map) {
        return null != map && false == map.isEmpty();
    }

}
