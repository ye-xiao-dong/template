package com.yd.springbootdemo.utils.collection;

import com.yd.springbootdemo.utils.convert.PageUtil;
import com.yd.springbootdemo.utils.reflect.ClassUtil;
import com.yd.springbootdemo.utils.reflect.ReflectUtil;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 集合相关工具类
 *
 * @author： 叶小东
 * @date： 2019/12/20 14:45
 */
public class CollUtil {

    /**
     * 其中一个集合在另一个集合中是否至少包含一个元素，既是两个集合是否至少有一个共同的元素
     *
     * @param coll1 集合1
     * @param coll2 集合2
     * @return 其中一个集合在另一个集合中是否至少包含一个元素
     */
    public static boolean containsAny(Collection coll1, Collection coll2) {
        if (coll1 == null || coll1.isEmpty() || coll2 == null || coll2.isEmpty() ) {
            return false;
        }
        if (coll1.size() < coll2.size()) {
            for (Object object : coll1) {
                if (coll2.contains(object)) {
                    return true;
                }
            }
        }
        else {
            for (Object object : coll2) {
                if (coll1.contains(object)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>      集合元素类型
     * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回 {@link HashSet}
     * @param ts       元素数组
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> Set<T> newHashSet(boolean isSorted, T... ts) {
        if (null == ts) {
            return isSorted ? new LinkedHashSet<T>() : new HashSet<T>();
        }
        int initialCapacity = Math.max((int) (ts.length / .75f) + 1, 16);
        HashSet<T> set = isSorted ? new LinkedHashSet<T>(initialCapacity) : new HashSet<T>(initialCapacity);
        for (T t : ts) {
            set.add(t);
        }
        return set;
    }

    /**
     * 新建一个List
     *
     * @param <T>      集合元素类型
     * @param isLinked 是否新建LinkedList
     * @param values   数组
     * @return List对象
     */
    @SafeVarargs
    public static <T> List<T> newList(boolean isLinked, T... values) {
        if (values == null || values.length == 0) {
            return isLinked ? new LinkedList<T>() : new ArrayList<T>();
        }
        List<T> arrayList = isLinked ? new LinkedList<>() : new ArrayList<>(values.length);
        for (T t : values) {
            arrayList.add(t);
        }
        return arrayList;
    }

    /**
     * 截取集合的部分
     *
     * @param <T>   集合元素类型
     * @param list  被截取的数组
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     * @return 截取后的数组，当开始位置超过最大时，返回空的List
     */
    public static <T> List<T> sub(List<T> list, int start, int end) {
        if (list == null) {
            return null;
        }
        if (start > end) {
            return new ArrayList<>();
        }
        if (start > list.size()) {
            return new ArrayList<>();
        }
        return list.subList(start, end);
    }

    /**
     * 对集合按照指定长度分段，每一个段为单独的集合，返回这个集合的列表
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param size       每个段的长度
     * @return 分段列表
     */
    public static <T> List<List> split(Collection<T> collection, int size) {
        final List<List> result = new ArrayList<>();
        ArrayList<T> subList = new ArrayList<>(size);
        for (T t : collection) {
            if (subList.size() >= size) {
                result.add(subList);
                subList = new ArrayList<>(size);
            }
            subList.add(t);
        }
        result.add(subList);
        return result;
    }

    /**
     * 过滤，此方法产生一个新集合
     * 过滤过程通过传入的Predicate实现来判断元素是否应加入返回的集合中。
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param predicate  判断器接口
     * @return 过滤后的新集合
     */
    public static <T extends Collection, E> T filter(T collection, Predicate<E> predicate) {
        if (null == collection || null == predicate) {
            return collection;
        }
        // 实例化一个新集合
        T collection2 = ReflectUtil.newInstance(ClassUtil.getClass(collection));
        // 将源集合的符合要求的元素加入新集合（成为两个集合的共用元素对象）
        for (E t : collection) {
            if (predicate.test(t)) {
                collection2.add(t);
            }
        }
        return collection2;
    }

    /**
     * 通过Editor抽取集合元素中的某些值返回为新列表
     * 例如提供的是一个Bean列表，通过Editor接口实现获取某个字段值，返回这个字段值组成的新列表
     *
     * @param collection 原集合
     * @param editor     编辑器
     * @param ignoreNull 是否忽略空值
     * @return 抽取后的新列表
     */
    public static <T extends Collection, E, R> List<R> extract(T collection, Function<E, R> editor, boolean ignoreNull) {
        if (null == collection) {
            return new ArrayList<>();
        }
        List<R> collection2 = new ArrayList<>();
        R value;
        for (E bean : collection) {
            value = editor.apply(bean);
            if (null == value && ignoreNull) {
                continue;
            }
            collection2.add(value);
        }
        return collection2;
    }

    /**
     * 集合中匹配规则的数量
     *
     * @param <T>       集合元素类型
     * @param iterable  {@link Iterable}
     * @param predicate 匹配器，为空则全部匹配
     * @return 匹配数量
     */
    public static <T> int count(Iterable<T> iterable, Predicate predicate) {
        int count = 0;
        if (null != iterable) {
            for (T t : iterable) {
                if (null == predicate || predicate.test(t)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 对指定List分页取值
     *
     * @param <T>      集合元素类型
     * @param pageNo   页码，从1开始计数，0和1效果相同
     * @param pageSize 每页的条目数
     * @param list     列表
     * @return 分页后的段落内容
     */
    public static <T> List<T> page(int pageNo, int pageSize, List<T> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>(0);
        }
        int resultSize = list.size();
        // 每页条目数大于总数直接返回所有
        if (resultSize <= pageSize) {
            if (pageNo <= 1) {
                return Collections.unmodifiableList(list);
            } else {
                // 越界直接返回空
                return new ArrayList<>(0);
            }
        }
        final int[] startEnd = PageUtil.transToStartEnd(pageNo, pageSize);
        if (startEnd[1] > resultSize) {
            startEnd[1] = resultSize;
        }
        return list.subList(startEnd[0], startEnd[1]);
    }

    /**
     * 根据元素的指定字段名分组,跳过空元素
     *
     * @param <T>        元素类型
     * @param collection 集合
     * @param groupBy    分类器
     * @return 分组列表
     */
    public static <T extends Collection, S, R, Z> Map<R, List> group(
        T collection, Function<S, R> groupBy, Function<S, Z> valueProcess, boolean ignoreNull) {
        Map<R, List> collValueMap = new HashMap<>();
        if (collection == null || collection.isEmpty()) {
            return collValueMap;
        }
        for (S item : collection) {
            if (item == null && ignoreNull) {
                continue;
            }
            R key = groupBy.apply(item);
            Z val = valueProcess.apply(item);
            if (val == null && ignoreNull) {
                continue;
            }
            if (collValueMap.containsKey(key)) {
                List<Z> coll = collValueMap.get(key);
                coll.add(val);
            } else {
                List<Z> coll = new LinkedList<>();
                coll.add(val);
                collValueMap.put(key, coll);
            }
        }
        return collValueMap;
    }

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

 	public  static  <V,K> Map<K,V> listToMap(Collection<V> tList,Function<V,K> function){
        return tList.parallelStream().collect(Collectors.toMap( function, v -> v));
    }

    public  static  <V,K> Map<K,V> listToMap(Collection<V> tList, Function<V,K> function, BinaryOperator<V> callBack){
        return tList.parallelStream().collect(Collectors.toMap( function, v -> v, callBack));
    }
}



