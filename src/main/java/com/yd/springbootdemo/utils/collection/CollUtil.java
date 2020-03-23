package com.yd.springbootdemo.utils.collection;

import com.yd.springbootdemo.utils.lang.ObjUtil;
import com.yd.springbootdemo.utils.lang.PageUtil;
import com.yd.springbootdemo.utils.convert.ConverterRegistry;
import com.yd.springbootdemo.utils.exceptions.UtilException;
import com.yd.springbootdemo.utils.lang.Assert;
import com.yd.springbootdemo.utils.lang.func.Choose;
import com.yd.springbootdemo.utils.lang.func.Editor;
import com.yd.springbootdemo.utils.map.MapUtil;
import com.yd.springbootdemo.utils.reflect.ClassUtil;
import com.yd.springbootdemo.utils.reflect.ReflectUtil;
import com.yd.springbootdemo.utils.reflect.TypeUtil;
import com.yd.springbootdemo.utils.text.CharUtil;
import com.yd.springbootdemo.utils.text.StrUtil;

import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * 集合相关工具类
 * <p>
 * 此工具方法针对{@link Collection}及其实现类封装的工具。
 * <p>
 * 由于{@link Collection} 实现了{@link Iterable}接口，因此部分工具此类不提供，而是在{@link IterUtil} 中提供
 *
 * @see IterUtil
 * @since 3.1.1
 */
public class CollUtil {


    /**
     * 两个集合的交集
     * 针对一个集合中存在多个相同元素的情况，保留两边集合全部相同元素
     *
     * @param <T>   集合元素类型
     * @param coll1 集合1
     * @param coll2 集合2
     * @return 交集的集合，返回 {@link ArrayList}
     */
    public static <T extends Collection<E>, E> T intersection(T coll1, T coll2) {
        T result = ReflectUtil.newInstance(ClassUtil.getClass(coll1));
        if (isNotEmpty(coll1) && isNotEmpty(coll2)) {
            final Set<E> set1 = new HashSet<>(coll1);
            final Set<E> set2 = new HashSet<>(coll2);
            for (E t1 : coll1) {
                if (set2.contains(t1)) {
                    result.add(t1);
                }
            }
            for (E t2 : coll2) {
                if (set1.contains(t2)) {
                    result.add(t2);
                }
            }
        }
        return result;
    }

    /**
     * 多个集合的交集<br>
     * 针对一个集合中存在多个相同元素的情况，保留两边集合全部相同元素
     *
     * @param <T>        集合元素类型
     * @param coll1      集合1
     * @param coll2      集合2
     * @param otherColls 其它集合
     * @return 并集的集合，返回 {@link ArrayList}
     */
    @SafeVarargs
    public static <T extends Collection<E>, E> T intersection(T coll1, T coll2, T... otherColls) {
        T intersection = intersection(coll1, coll2);
        if (isEmpty(intersection)) {
            return intersection;
        }
        for (T coll : otherColls) {
            intersection = intersection(intersection, coll);
            if (isEmpty(intersection)) {
                return intersection;
            }
        }
        return intersection;
    }

    /**
     * 两个集合的差集<br>
     * 针对一个集合中存在多个相同元素的情况，保留集合各自的全部相同元素
     *
     * @param <T>   集合元素类型
     * @param coll1 集合1
     * @param coll2 集合2
     * @return 差集的集合，返回 {@link ArrayList}
     */
    public static <T extends Collection<E>, E> T disjunction(T coll1, T coll2) {
        T result = ReflectUtil.newInstance(ClassUtil.getClass(coll1));
        final Set<E> set1 = new HashSet<>();
        final Set<E> set2 = new HashSet<>();
        if (isNotEmpty(coll2)) {
            set2.addAll(coll2);
        }
        if (isNotEmpty(coll1)) {
            set1.addAll(coll1);
            for (E t1 : coll1) {
                if (!set2.contains(t1)) {
                    result.add(t1);
                }
            }
        }
        if (isNotEmpty(coll2)) {
            for (E t2 : coll2) {
                if (!set1.contains(t2)) {
                    result.add(t2);
                }
            }
        }
        return result;
    }

    /**
     * 判断指定集合是否包含指定值，如果集合为空（null或者空），返回{@code false}，否则找到元素返回{@code true}
     *
     * @param collection 集合
     * @param value      需要查找的值
     * @return 如果集合为空（null或者空），返回{@code false}，否则找到元素返回{@code true}
     * @since 4.1.10
     */
    public static boolean contains(Collection<?> collection, Object value) {
        return isNotEmpty(collection) && collection.contains(value);
    }

    /**
     * 其中一个集合在另一个集合中是否至少包含一个元素，既是两个集合是否至少有一个共同的元素
     *
     * @param coll1 集合1
     * @param coll2 集合2
     * @return 其中一个集合在另一个集合中是否至少包含一个元素
     * @see #intersection
     * @since 2.1
     */
    public static boolean containsAny(Collection<?> coll1, Collection<?> coll2) {
        if (isEmpty(coll1) || isEmpty(coll2)) {
            return false;
        }
        if (coll1.size() < coll2.size()) {
            for (Object object : coll1) {
                if (coll2.contains(object)) {
                    return true;
                }
            }
        } else {
            for (Object object : coll2) {
                if (coll1.contains(object)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 集合1中是否包含集合2中所有的元素，既集合2是否为集合1的子集
     *
     * @param coll1 集合1
     * @param coll2 集合2
     * @return 集合1中是否包含集合2中所有的元素
     * @since 4.5.12
     */
    public static boolean containsAll(Collection<?> coll1, Collection<?> coll2) {
        if (isEmpty(coll1) || isEmpty(coll2) || coll1.size() < coll2.size()) {
            return false;
        }

        for (Object object : coll2) {
            if (!coll1.contains(object)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串<br>
     * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
     *
     * @param <T>         集合元素类型
     * @param iterable    {@link Iterable}
     * @param conjunction 分隔符
     * @return 连接后的字符串
     * @see IterUtil#join(Iterable, CharSequence)
     */
    public static <T> String joinToString(Iterable<T> iterable, CharSequence conjunction) {
        return IterUtil.join(iterable, conjunction);
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串<br>
     * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
     *
     * @param <T>         集合元素类型
     * @param iterator    集合
     * @param conjunction 分隔符
     * @return 连接后的字符串
     * @see IterUtil#join(Iterator, CharSequence)
     */
    public static <T> String joinToString(Iterator<T> iterator, CharSequence conjunction) {
        return IterUtil.join(iterator, conjunction);
    }
    // ----------------------------------------------------------------------------------------------- new HashMap

    /**
     * 新建一个HashMap
     *
     * @param <K> Key类型
     * @param <V> Value类型
     * @return HashMap对象
     * @see MapUtil#newHashMap()
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return MapUtil.newHashMap();
    }

    /**
     * 新建一个HashMap
     *
     * @param <K>     Key类型
     * @param <V>     Value类型
     * @param size    初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75
     * @param isOrder Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
     * @return HashMap对象
     * @see MapUtil#newHashMap(int, boolean)
     * @since 3.0.4
     */
    public static <K, V> HashMap<K, V> newHashMap(int size, boolean isOrder) {
        return MapUtil.newHashMap(size, isOrder);
    }

    /**
     * 新建一个HashMap
     *
     * @param <K>  Key类型
     * @param <V>  Value类型
     * @param size 初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75
     * @return HashMap对象
     * @see MapUtil#newHashMap(int)
     */
    public static <K, V> HashMap<K, V> newHashMap(int size) {
        return MapUtil.newHashMap(size);
    }

    // ----------------------------------------------------------------------------------------------- new HashSet

    /**
     * 新建一个HashSet
     *
     * @param <T> 集合元素类型
     * @param ts  元素数组
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> HashSet<T> newHashSet(T... ts) {
        return newHashSet(false, ts);
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
    public static <T> HashSet<T> newHashSet(boolean isSorted, T... ts) {
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
     * 新建一个HashSet
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(Collection<T> collection) {
        return newHashSet(false, collection);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>        集合元素类型
     * @param isSorted   是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param collection 集合，用于初始化Set
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(boolean isSorted, Collection<T> collection) {
        return isSorted ? new LinkedHashSet<T>(collection) : new HashSet<T>(collection);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>      集合元素类型
     * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param iter     {@link Iterator}
     * @return HashSet对象
     * @since 3.0.8
     */
    public static <T> HashSet<T> newHashSet(boolean isSorted, Iterator<T> iter) {
        if (null == iter) {
            return newHashSet(isSorted, (T[]) null);
        }
        final HashSet<T> set = isSorted ? new LinkedHashSet<T>() : new HashSet<T>();
        while (iter.hasNext()) {
            set.add(iter.next());
        }
        return set;
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>        集合元素类型
     * @param isSorted   是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param enumration {@link Enumeration}
     * @return HashSet对象
     * @since 3.0.8
     */
    public static <T> HashSet<T> newHashSet(boolean isSorted, Enumeration<T> enumration) {
        if (null == enumration) {
            return newHashSet(isSorted, (T[]) null);
        }
        final HashSet<T> set = isSorted ? new LinkedHashSet<T>() : new HashSet<T>();
        while (enumration.hasMoreElements()) {
            set.add(enumration.nextElement());
        }
        return set;
    }

    // ----------------------------------------------------------------------------------------------- List

    /**
     * 新建一个空List
     *
     * @param <T>      集合元素类型
     * @param isLinked 是否新建LinkedList
     * @return List对象
     * @since 4.1.2
     */
    public static <T> List<T> newList(boolean isLinked) {
        return isLinked ? new LinkedList<T>() : new ArrayList<T>();
    }

    /**
     * 新建一个List
     *
     * @param <T>      集合元素类型
     * @param isLinked 是否新建LinkedList
     * @param values   数组
     * @return List对象
     * @since 4.1.2
     */
    @SafeVarargs
    public static <T> List<T> newList(boolean isLinked, T... values) {
        if (ArrayUtil.isEmpty(values)) {
            return newList(isLinked);
        }
        List<T> arrayList = isLinked ? new LinkedList<>() : new ArrayList<>(values.length);
        for (T t : values) {
            arrayList.add(t);
        }
        return arrayList;
    }

    /**
     * 新建一个List
     *
     * @param <T>        集合元素类型
     * @param isLinked   是否新建LinkedList
     * @param collection 集合
     * @return List对象
     * @since 4.1.2
     */
    public static <T> List<T> newList(boolean isLinked, Collection<T> collection) {
        if (null == collection) {
            return newList(isLinked);
        }
        return isLinked ? new LinkedList<T>(collection) : new ArrayList<T>(collection);
    }

    /**
     * 新建一个List<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>      集合元素类型
     * @param isLinked 是否新建LinkedList
     * @param iterable {@link Iterable}
     * @return List对象
     * @since 4.1.2
     */
    public static <T> List<T> newList(boolean isLinked, Iterable<T> iterable) {
        if (null == iterable) {
            return newList(isLinked);
        }
        return newList(isLinked, iterable.iterator());
    }

    /**
     * 新建一个ArrayList<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>      集合元素类型
     * @param isLinked 是否新建LinkedList
     * @param iter     {@link Iterator}
     * @return ArrayList对象
     * @since 4.1.2
     */
    public static <T> List<T> newList(boolean isLinked, Iterator<T> iter) {
        final List<T> list = newList(isLinked);
        if (null != iter) {
            while (iter.hasNext()) {
                list.add(iter.next());
            }
        }
        return list;
    }

    /**
     * 新建一个List<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>        集合元素类型
     * @param isLinked   是否新建LinkedList
     * @param enumration {@link Enumeration}
     * @return ArrayList对象
     * @since 3.0.8
     */
    public static <T> List<T> newList(boolean isLinked, Enumeration<T> enumration) {
        final List<T> list = newList(isLinked);
        if (null != enumration) {
            while (enumration.hasMoreElements()) {
                list.add(enumration.nextElement());
            }
        }
        return list;
    }

    /**
     * 新建一个ArrayList
     *
     * @param <T>    集合元素类型
     * @param values 数组
     * @return ArrayList对象
     */
    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(T... values) {
        return (ArrayList<T>) newList(false, values);
    }

    /**
     * 数组转为ArrayList
     *
     * @param <T>    集合元素类型
     * @param values 数组
     * @return ArrayList对象
     * @since 4.0.11
     */
    @SafeVarargs
    public static <T> ArrayList<T> toList(T... values) {
        return newArrayList(values);
    }

    /**
     * 新建一个ArrayList
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return ArrayList对象
     */
    public static <T> ArrayList<T> newArrayList(Collection<T> collection) {
        return (ArrayList<T>) newList(false, collection);
    }

    /**
     * 新建一个ArrayList<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>      集合元素类型
     * @param iterable {@link Iterable}
     * @return ArrayList对象
     * @since 3.1.0
     */
    public static <T> ArrayList<T> newArrayList(Iterable<T> iterable) {
        return (ArrayList<T>) newList(false, iterable);
    }

    /**
     * 新建一个ArrayList<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>  集合元素类型
     * @param iter {@link Iterator}
     * @return ArrayList对象
     * @since 3.0.8
     */
    public static <T> ArrayList<T> newArrayList(Iterator<T> iter) {
        return (ArrayList<T>) newList(false, iter);
    }

    /**
     * 新建一个ArrayList<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>        集合元素类型
     * @param enumration {@link Enumeration}
     * @return ArrayList对象
     * @since 3.0.8
     */
    public static <T> ArrayList<T> newArrayList(Enumeration<T> enumration) {
        return (ArrayList<T>) newList(false, enumration);
    }

    /**
     * 新建{@link BlockingQueue}<br>
     * 在队列为空时，获取元素的线程会等待队列变为非空。当队列满时，存储元素的线程会等待队列可用。
     *
     * @param capacity 容量
     * @param isLinked 是否为链表形式
     * @return {@link BlockingQueue}
     * @since 3.3.0
     */
    public static <T> BlockingQueue<T> newBlockingQueue(int capacity, boolean isLinked) {
        BlockingQueue<T> queue;
        if (isLinked) {
            queue = new LinkedBlockingDeque<>(capacity);
        } else {
            queue = new ArrayBlockingQueue<>(capacity);
        }
        return queue;
    }

    /**
     * 创建新的集合对象
     *
     * @param <T>            集合类型
     * @param collectionType 集合类型
     * @return 集合类型对应的实例
     * @since 3.0.8
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Collection<T> create(Class<?> collectionType) {
        Collection<T> list = null;
        if (collectionType.isAssignableFrom(AbstractCollection.class)) {
            // 抽象集合默认使用ArrayList
            list = new ArrayList<>();
        }

        // Set
        else if (collectionType.isAssignableFrom(HashSet.class)) {
            list = new HashSet<>();
        } else if (collectionType.isAssignableFrom(LinkedHashSet.class)) {
            list = new LinkedHashSet<>();
        } else if (collectionType.isAssignableFrom(TreeSet.class)) {
            list = new TreeSet<>();
        } else if (collectionType.isAssignableFrom(EnumSet.class)) {
            list = (Collection<T>) EnumSet.noneOf((Class<Enum>) ClassUtil.getTypeArgument(collectionType));
        }

        // List
        else if (collectionType.isAssignableFrom(ArrayList.class)) {
            list = new ArrayList<>();
        } else if (collectionType.isAssignableFrom(LinkedList.class)) {
            list = new LinkedList<>();
        }

        // Others，直接实例化
        else {
            try {
                list = (Collection<T>) ReflectUtil.newInstance(collectionType);
            } catch (Exception e) {
                throw new UtilException(e);
            }
        }
        return list;
    }

    /**
     * 创建Map<br>
     * 传入抽象Map{@link AbstractMap}和{@link Map}类将默认创建{@link HashMap}
     *
     * @param <K>     map键类型
     * @param <V>     map值类型
     * @param mapType map类型
     * @return {@link Map}实例
     * @see MapUtil#createMap(Class)
     */
    public static <K, V> Map<K, V> createMap(Class<?> mapType) {
        return MapUtil.createMap(mapType);
    }

    /**
     * 去重集合,在源集合上去重
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return {@link ArrayList}
     */
    public static <T extends Collection<E>, E> T unique(T collection) {
        if (isEmpty(collection)) {
            return collection;
        }
        Set<E> set = new HashSet<>(collection);
        collection.clear();
        collection.addAll(set);
        return collection;
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
    public static <T> List<List<T>> split(Collection<T> collection, int size) {
        final List<List<T>> result = new ArrayList<>();

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
     * 过滤，此方法产生一个新集合<br>
     * 过滤过程通过传入的Predicate实现来判断元素是否应加入返回的集合中。
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param predicate  判断器接口
     * @return 过滤后的新集合
     */
    public static <T extends Collection<E>, E> T filter(T collection, Predicate<E> predicate) {
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
     * 去掉集合中的多个元素，此方法直接修改原集合
     *
     * @param <T>         集合类型
     * @param <E>         集合元素类型
     * @param collection  集合
     * @param elesRemoved 被去掉的元素数组
     * @return 原集合
     * @since 4.1.0
     */
    @SuppressWarnings("unchecked")
    public static <T extends Collection<E>, E> T removeAny(T collection, E... elesRemoved) {
        collection.removeAll(newHashSet(elesRemoved));
        return collection;
    }


    /**
     * 去除{@code null} 元素，此方法直接修改原集合
     *
     * @param <T>        集合类型
     * @param <E>        集合元素类型
     * @param collection 集合
     * @return 处理后的集合
     * @since 3.2.2
     */
    public static <T extends Collection<E>, E> T removeNull(T collection) {
        return filter(collection, Objects::nonNull);
    }

    /**
     * 去除{@code null}或者"" 元素，此方法直接修改原集合
     *
     * @param collection 集合
     * @return 处理后的集合
     * @since 3.2.2
     */
    public static <T extends Collection<E>, E extends CharSequence> T removeEmpty(T collection) {
        return filter(collection, StrUtil::isNotEmpty);
    }

    /**
     * 去除{@code null}或者""或者空白字符串 元素，此方法直接修改原集合
     *
     * @param collection 集合
     * @return 处理后的集合
     * @since 3.2.2
     */
    public static <T extends Collection<E>, E extends CharSequence> T removeBlank(T collection) {
        return filter(collection, StrUtil::isNotBlank);
    }

    /**
     * 通过Editor抽取集合元素中的某些值返回为新列表<br>
     * 例如提供的是一个Bean列表，通过Editor接口实现获取某个字段值，返回这个字段值组成的新列表
     *
     * @param collection 原集合
     * @param editor     编辑器
     * @return 抽取后的新列表
     */
    public static <T extends Collection<E>, E, R> List<R> extract(T collection, Function<E, R> editor) {
        return extract(collection, editor, true);
    }

    /**
     * 通过Editor抽取集合元素中的某些值返回为新列表<br>
     * 例如提供的是一个Bean列表，通过Editor接口实现获取某个字段值，返回这个字段值组成的新列表
     *
     * @param collection 原集合
     * @param editor     编辑器
     * @param ignoreNull 是否忽略空值
     * @return 抽取后的新列表
     * @since 4.5.7
     */
    public static <T extends Collection<E>, E, R> List<R> extract(T collection, Function<E, R> editor, boolean ignoreNull) {
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
     * 查找第一个匹配元素对象
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param predicate  过滤器，满足过滤条件的第一个元素将被返回
     * @return 满足过滤条件的第一个元素
     * @since 3.1.0
     */
    public static <T> T findOne(Iterable<T> collection, Predicate<T> predicate) {
        if (null != collection) {
            for (T t : collection) {
                if (predicate.test(t)) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * 过滤<br>
     * 过滤过程通过传入的Editor实现来返回需要的元素内容，这个Editor实现可以实现以下功能：
     * <p>
     * <pre>
     * 1、过滤出需要的对象，如果返回null表示这个元素对象抛弃
     * 2、修改元素对象，返回集合中为修改后的对象
     * </pre>
     *
     * @param <K>    Key类型
     * @param <V>    Value类型
     * @param map    Map
     * @param editor 编辑器接口
     * @return 过滤后的Map
     * @see MapUtil#filter(Map, Editor)
     */
    public static <K, V> Map<K, V> filterMap(Map<K, V> map, Editor<Entry<K, V>> editor) {
        return MapUtil.filter(map, editor);
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

    // ---------------------------------------------------------------------- isEmpty

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Map是否为空
     *
     * @param map 集合
     * @return 是否为空
     * @see MapUtil#isEmpty(Map)
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return MapUtil.isEmpty(map);
    }

    /**
     * Iterable是否为空
     *
     * @param iterable Iterable对象
     * @return 是否为空
     * @see IterUtil#isEmpty(Iterable)
     */
    public static boolean isEmpty(Iterable<?> iterable) {
        return IterUtil.isEmpty(iterable);
    }

    /**
     * Iterator是否为空
     *
     * @param Iterator Iterator对象
     * @return 是否为空
     * @see IterUtil#isEmpty(Iterator)
     */
    public static boolean isEmpty(Iterator<?> Iterator) {
        return IterUtil.isEmpty(Iterator);
    }

    /**
     * Enumeration是否为空
     *
     * @param enumeration {@link Enumeration}
     * @return 是否为空
     */
    public static boolean isEmpty(Enumeration<?> enumeration) {
        return null == enumeration || !enumeration.hasMoreElements();
    }

    // ---------------------------------------------------------------------- isNotEmpty

    /**
     * 集合是否为非空
     *
     * @param collection 集合
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Map是否为非空
     *
     * @param map 集合
     * @return 是否为非空
     * @see MapUtil#isNotEmpty(Map)
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return MapUtil.isNotEmpty(map);
    }

    /**
     * Iterable是否为空
     *
     * @param iterable Iterable对象
     * @return 是否为空
     * @see IterUtil#isNotEmpty(Iterable)
     */
    public static boolean isNotEmpty(Iterable<?> iterable) {
        return IterUtil.isNotEmpty(iterable);
    }

    /**
     * Iterator是否为空
     *
     * @param Iterator Iterator对象
     * @return 是否为空
     * @see IterUtil#isNotEmpty(Iterator)
     */
    public static boolean isNotEmpty(Iterator<?> Iterator) {
        return IterUtil.isNotEmpty(Iterator);
    }

    /**
     * Enumeration是否为空
     *
     * @param enumeration {@link Enumeration}
     * @return 是否为空
     */
    public static boolean isNotEmpty(Enumeration<?> enumeration) {
        return null != enumeration && enumeration.hasMoreElements();
    }

    /**
     * 是否包含{@code null}元素
     *
     * @param iterable 被检查的Iterable对象，如果为{@code null} 返回false
     * @return 是否包含{@code null}元素
     * @see IterUtil#hasNull(Iterable)
     * @since 3.0.7
     */
    public static boolean hasNull(Iterable<?> iterable) {
        return IterUtil.hasNull(iterable);
    }

    // ---------------------------------------------------------------------- zip

    /**
     * 映射键值（参考Python的zip()函数）<br>
     * 例如：<br>
     * keys = a,b,c,d<br>
     * values = 1,2,3,4<br>
     * delimiter = , 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
     * 如果两个数组长度不同，则只对应最短部分
     *
     * @param keys      键列表
     * @param values    值列表
     * @param delimiter 分隔符
     * @param isOrder   是否有序
     * @return Map
     * @since 3.0.4
     */
    public static Map<String, String> zip(String keys, String values, String delimiter, boolean isOrder) {
        return ArrayUtil.zip(StrUtil.split(keys, delimiter), StrUtil.split(values, delimiter), isOrder);
    }

    /**
     * 映射键值（参考Python的zip()函数），返回Map无序<br>
     * 例如：<br>
     * keys = a,b,c,d<br>
     * values = 1,2,3,4<br>
     * delimiter = , 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
     * 如果两个数组长度不同，则只对应最短部分
     *
     * @param keys      键列表
     * @param values    值列表
     * @param delimiter 分隔符
     * @return Map
     */
    public static Map<String, String> zip(String keys, String values, String delimiter) {
        return zip(keys, values, delimiter, false);
    }

    /**
     * 映射键值（参考Python的zip()函数）<br>
     * 例如：<br>
     * keys = [a,b,c,d]<br>
     * values = [1,2,3,4]<br>
     * 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
     * 如果两个数组长度不同，则只对应最短部分
     *
     * @param <K>    键类型
     * @param <V>    值类型
     * @param keys   键列表
     * @param values 值列表
     * @return Map
     */
    public static <K, V> Map<K, V> zip(Collection<K> keys, Collection<V> values) {
        if (isEmpty(keys) || isEmpty(values)) {
            return new HashMap<>(0);
        }

        int entryCount = Math.min(keys.size(), values.size());
        final Map<K, V> map = newHashMap(entryCount);

        final Iterator<K> keyIterator = keys.iterator();
        final Iterator<V> valueIterator = values.iterator();
        while (entryCount > 0) {
            map.put(keyIterator.next(), valueIterator.next());
            entryCount--;
        }

        return map;
    }

    /**
     * 行转列，合并相同的键，值合并为列表<br>
     * 将Map列表中相同key的值组成列表做为Map的value<br>
     * 是{@link #toMapList(Map)}的逆方法<br>
     * 比如传入数据：
     * <p>
     * <pre>
     * [
     *  {a: 1, b: 1, c: 1}
     *  {a: 2, b: 2}
     *  {a: 3, b: 3}
     *  {a: 4}
     * ]
     * </pre>
     * <p>
     * 结果是：
     * <p>
     * <pre>
     * {
     *   a: [1,2,3,4]
     *   b: [1,2,3,]
     *   c: [1]
     * }
     * </pre>
     *
     * @param <K>     键类型
     * @param <V>     值类型
     * @param mapList Map列表
     * @return Map
     * @see MapUtil#toListMap(Iterable)
     */
    public static <K, V> Map<K, List<V>> toListMap(Iterable<? extends Map<K, V>> mapList) {
        return MapUtil.toListMap(mapList);
    }

    /**
     * 列转行。将Map中值列表分别按照其位置与key组成新的map。<br>
     * 是{@link #toListMap(Iterable)}的逆方法<br>
     * 比如传入数据：
     * <p>
     * <pre>
     * {
     *   a: [1,2,3,4]
     *   b: [1,2,3,]
     *   c: [1]
     * }
     * </pre>
     * <p>
     * 结果是：
     * <p>
     * <pre>
     * [
     *  {a: 1, b: 1, c: 1}
     *  {a: 2, b: 2}
     *  {a: 3, b: 3}
     *  {a: 4}
     * ]
     * </pre>
     *
     * @param <K>     键类型
     * @param <V>     值类型
     * @param listMap 列表Map
     * @return Map列表
     * @see MapUtil#toMapList(Map)
     */
    public static <K, V> List<Map<K, V>> toMapList(Map<K, ? extends Iterable<V>> listMap) {
        return MapUtil.toMapList(listMap);
    }

    /**
     * 将指定对象全部加入到集合中<br>
     * 提供的对象如果为集合类型，会自动转换为目标元素类型<br>
     *
     * @param <T>        元素类型
     * @param collection 被加入的集合
     * @param value      对象，可能为Iterator、Iterable、Enumeration、Array
     * @return 被加入集合
     */
    public static <T> Collection<T> addAll(Collection<T> collection, Object value) {
        return addAll(collection, value, TypeUtil.getTypeArgument(collection.getClass()));
    }

    /**
     * 将指定对象全部加入到集合中<br>
     * 提供的对象如果为集合类型，会自动转换为目标元素类型<br>
     *
     * @param <T>         元素类型
     * @param collection  被加入的集合
     * @param value       对象，可能为Iterator、Iterable、Enumeration、Array，或者与集合元素类型一致
     * @param elementType 元素类型，为空时，使用Object类型来接纳所有类型
     * @return 被加入集合
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Collection<T> addAll(Collection<T> collection, Object value, Type elementType) {
        if (null == collection || null == value) {
            return collection;
        }
        if (TypeUtil.isUnknow(elementType)) {
            // 元素类型为空时，使用Object类型来接纳所有类型
            elementType = Object.class;
        }

        Iterator iter;
        if (value instanceof Iterator) {
            iter = (Iterator) value;
        } else if (value instanceof Iterable) {
            iter = ((Iterable) value).iterator();
        } else if (value instanceof Enumeration) {
            iter = new EnumerationIter<>((Enumeration) value);
        } else if (ArrayUtil.isArray(value)) {
            iter = new ArrayIter<>(value);
        } else if (value instanceof CharSequence) {
            // String按照逗号分隔的列表对待
            iter = StrUtil.splitTrim((CharSequence) value, CharUtil.COMMA).iterator();
        } else {
            // 其它类型按照单一元素处理
            iter = CollUtil.newArrayList(value).iterator();
        }

        final ConverterRegistry convert = ConverterRegistry.getInstance();
        while (iter.hasNext()) {
            collection.add((T) convert.convert(elementType, iter.next()));
        }

        return collection;
    }

    /**
     * 加入全部
     *
     * @param <T>        集合元素类型
     * @param collection 被加入的集合 {@link Collection}
     * @param iterator   要加入的{@link Iterator}
     * @return 原集合
     */
    public static <T> Collection<T> addAll(Collection<T> collection, Iterator<T> iterator) {
        if (null != collection && null != iterator) {
            while (iterator.hasNext()) {
                collection.add(iterator.next());
            }
        }
        return collection;
    }

    /**
     * 加入全部
     *
     * @param <T>        集合元素类型
     * @param collection 被加入的集合 {@link Collection}
     * @param iterable   要加入的内容{@link Iterable}
     * @return 原集合
     */
    public static <T> Collection<T> addAll(Collection<T> collection, Iterable<T> iterable) {
        return addAll(collection, iterable.iterator());
    }

    /**
     * 加入全部
     *
     * @param <T>         集合元素类型
     * @param collection  被加入的集合 {@link Collection}
     * @param enumeration 要加入的内容{@link Enumeration}
     * @return 原集合
     */
    public static <T> Collection<T> addAll(Collection<T> collection, Enumeration<T> enumeration) {
        if (null != collection && null != enumeration) {
            while (enumeration.hasMoreElements()) {
                collection.add(enumeration.nextElement());
            }
        }
        return collection;
    }

    /**
     * 加入全部
     *
     * @param <T>        集合元素类型
     * @param collection 被加入的集合 {@link Collection}
     * @param values     要加入的内容数组
     * @return 原集合
     * @since 3.0.8
     */
    public static <T> Collection<T> addAll(Collection<T> collection, T[] values) {
        if (null != collection && null != values) {
            Collections.addAll(collection, values);
        }
        return collection;
    }

    /**
     * 将另一个集合中的元素加入到另一个集合中，如果集合中已经存在此元素则忽略之
     *
     * @param <T>       集合元素类型
     * @param coll      集合
     * @param otherColl 其它集合
     * @return 此列表
     */
    public static <T extends Collection<E>, E> T merge(T coll, T otherColl) {
        Set<E> set = new HashSet<>(coll);
        for (E t : otherColl) {
            if (!set.contains(t)) {
                coll.add(t);
            }
        }
        return coll;
    }

    /**
     * 获取集合中指定下标的元素值，下标可以为负数，例如-1表示最后一个元素<br>
     * 如果元素越界，返回null
     *
     * @param <T>        元素类型
     * @param collection 集合
     * @param index      下标，支持负数
     * @return 元素值
     * @since 4.0.6
     */
    public static <T> T get(Collection<T> collection, int index) {
        if (null == collection) {
            return null;
        }

        final int size = collection.size();
        if (0 == size) {
            return null;
        }

        if (index < 0) {
            index += size;
        }

        // 检查越界
        if (index >= size) {
            return null;
        }

        if (collection instanceof List) {
            final List<T> list = ((List<T>) collection);
            return list.get(index);
        } else {
            int i = 0;
            for (T t : collection) {
                if (i > index) {
                    break;
                } else if (i == index) {
                    return t;
                }
                i++;
            }
        }
        return null;
    }

    /**
     * 获取集合中指定多个下标的元素值，下标可以为负数，例如-1表示最后一个元素
     *
     * @param <T>        元素类型
     * @param collection 集合
     * @param indexes    下标，支持负数
     * @return 元素值列表
     * @since 4.0.6
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getAny(Collection<T> collection, int... indexes) {
        final int size = collection.size();
        final ArrayList<T> result = new ArrayList<>();
        if (collection instanceof List) {
            final List<T> list = ((List<T>) collection);
            for (int index : indexes) {
                if (index < 0) {
                    index += size;
                }
                result.add(list.get(index));
            }
        } else {
            Object[] array = ((Collection<T>) collection).toArray();
            for (int index : indexes) {
                if (index < 0) {
                    index += size;
                }
                result.add((T) array[index]);
            }
        }
        return result;
    }

    /**
     * 获取集合的第一个元素
     *
     * @param <T>      集合元素类型
     * @param iterable {@link Iterable}
     * @return 第一个元素
     * @see IterUtil#getFirst(Iterable)
     * @since 3.0.1
     */
    public static <T> T getFirst(Iterable<T> iterable) {
        return IterUtil.getFirst(iterable);
    }

    /**
     * 获取集合的第一个元素
     *
     * @param <T>      集合元素类型
     * @param iterator {@link Iterator}
     * @return 第一个元素
     * @see IterUtil#getFirst(Iterator)
     * @since 3.0.1
     */
    public static <T> T getFirst(Iterator<T> iterator) {
        return IterUtil.getFirst(iterator);
    }

    /**
     * 获取集合的最后一个元素
     *
     * @param <T>        集合元素类型
     * @param collection {@link Collection}
     * @return 最后一个元素
     * @since 4.1.10
     */
    public static <T> T getLast(Collection<T> collection) {
        return get(collection, -1);
    }


    // ------------------------------------------------------------------------------------------------- sort

    /**
     * 将多个集合排序并显示不同的段落（分页）<br>
     * 采用{@link BoundedPriorityQueue}实现分页取局部
     *
     * @param <T>        集合元素类型
     * @param pageNo     页码，从1开始计数，0和1效果相同
     * @param pageSize   每页的条目数
     * @param comparator 比较器
     * @param colls      集合数组
     * @return 分页后的段落内容
     */
    @SafeVarargs
    public static <T> List<T> sortPageAll(int pageNo, int pageSize, Comparator<T> comparator, Collection<T>... colls) {
        final List<T> list = new ArrayList<>(pageNo * pageSize);
        for (Collection<T> coll : colls) {
            list.addAll(coll);
        }
        if (null != comparator) {
            list.sort(comparator);
        }

        return page(pageNo, pageSize, list);
    }

    /**
     * 对指定List分页取值
     *
     * @param <T>      集合元素类型
     * @param pageNo   页码，从1开始计数，0和1效果相同
     * @param pageSize 每页的条目数
     * @param list     列表
     * @return 分页后的段落内容
     * @since 4.1.20
     */
    public static <T> List<T> page(int pageNo, int pageSize, List<T> list) {
        if (isEmpty(list)) {
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
     * 排序集合，排序不会修改原集合
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param comparator 比较器
     * @return treeSet
     */
    public static <T> List<T> sort(Collection<T> collection, Comparator<? super T> comparator) {
        List<T> list = new ArrayList<T>(collection);
        list.sort(comparator);
        return list;
    }

    /**
     * 针对List排序，排序会修改原List
     *
     * @param <T>  元素类型
     * @param list 被排序的List
     * @param c    {@link Comparator}
     * @return 原list
     * @see Collections#sort(List, Comparator)
     */
    public static <T> List<T> sort(List<T> list, Comparator<? super T> c) {
        list.sort(c);
        return list;
    }

    // ------------------------------------------------------------------------------------------------- forEach

    /**
     * 循环遍历 {@link Iterator}，使用{@link Consumer} 接受遍历的每条数据，并针对每条数据做处理
     *
     * @param <T>      集合元素类型
     * @param iterator {@link Iterator}
     * @param consumer {@link Consumer} 遍历的每条数据处理器
     */
    public static <T> void forEach(Iterator<T> iterator, Consumer<T> consumer) {
        Assert.notNull(iterator);
        while (iterator.hasNext()) {
            consumer.accept(iterator.next());
        }
    }

    /**
     * 循环遍历 {@link Enumeration}，使用{@link Consumer} 接受遍历的每条数据，并针对每条数据做处理
     *
     * @param <T>         集合元素类型
     * @param enumeration {@link Enumeration}
     * @param consumer    {@link Consumer} 遍历的每条数据处理器
     */
    public static <T> void forEach(Enumeration<T> enumeration, Consumer<T> consumer) {
        while (enumeration.hasMoreElements()) {
            consumer.accept(enumeration.nextElement());
        }
    }

    /**
     * 循环遍历Map，使用{@link KVConsumer} 接受遍历的每条数据，并针对每条数据做处理
     *
     * @param <K>        Key类型
     * @param <V>        Value类型
     * @param map        {@link Map}
     * @param kvConsumer {@link KVConsumer} 遍历的每条数据处理器
     */
    public static <K, V> void forEach(Map<K, V> map, KVConsumer<K, V> kvConsumer) {
        int index = 0;
        for (Entry<K, V> entry : map.entrySet()) {
            kvConsumer.accept(entry.getKey(), entry.getValue(), index);
            index++;
        }
    }


    /**
     * 根据元素的指定字段名分组,跳过空元素
     *
     * @param <T>        元素类型
     * @param collection 集合
     * @param groupBy    分类器
     * @return 分组列表
     */
    public static <T extends Collection<S>, S, R, Z> Map<R, List<Z>> group(
            T collection, Function<S, R> groupBy, Function<S, Z> valueProcess) {
        return group(collection, groupBy, valueProcess, true);
    }


    /**
     * 根据元素的指定字段名分组,跳过空元素
     *
     * @param <T>        元素类型
     * @param collection 集合
     * @param groupBy    分类器
     * @return 分组列表
     */
    public static <T extends Collection<S>, S, R, Z> Map<R, List<Z>> group(
            T collection, Function<S, R> groupBy, Function<S, Z> valueProcess, boolean ignoreNull) {
        Map<R, List<Z>> collValueMap = new HashMap<>();
        if (isEmpty(collection)) {
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
     * 根据指定的键值选择规则，将集合转换为map,出现重复直接覆盖,跳过空元素
     *
     * @param <T>         元素类型
     * @param collection  集合
     * @param keySelector 键选择器
     * @param valSelector 值选择器
     * @return 分组列表
     */
    public static <R, S, T> Map<R, S> map(Collection<T> collection,
                                          Function<T, R> keySelector,
                                          Function<T, S> valSelector) {
        return map(collection, keySelector, valSelector, null, true);
    }

    /**
     * 根据指定的键值选择规则，将集合转换为map,出现重复直接覆盖
     *
     * @param <T>         元素类型
     * @param collection  集合
     * @param keySelector 键选择器
     * @param valSelector 值选择器
     * @param ignoreNull  是否跳过空元素
     * @return 分组列表
     */
    public static <R, S, T> Map<R, S> map(Collection<T> collection,
                                          Function<T, R> keySelector,
                                          Function<T, S> valSelector,
                                          boolean ignoreNull) {
        return map(collection, keySelector, valSelector, null, ignoreNull);
    }

    /**
     * 根据指定的键值选择规则，将集合转换为map
     *
     * @param <T>         元素类型
     * @param collection  集合
     * @param keySelector 键选择器
     * @param valSelector 值选择器
     * @param ignoreNull  是否跳过空元素
     * @return 分组列表
     */
    public static <R, S, T> Map<R, S> map(Collection<T> collection,
                                          Function<T, R> keySelector,
                                          Function<T, S> valSelector,
                                          Choose<S> whenConflict, boolean ignoreNull) {
        Map<R, S> map = new HashMap<>();
        if (isEmpty(collection)) {
            return map;
        }
        for (T item : collection) {
            if (item == null && ignoreNull) {
                continue;
            }
            R key = keySelector.apply(item);
            S val = valSelector.apply(item);
            if (map.containsKey(key)) {
                if (whenConflict != null) {
                    val = whenConflict.choose(val, map.get(key));
                }
            }
            map.put(key, val);
        }
        return map;
    }

    /**
     * 反序给定List，会在原List基础上直接修改
     *
     * @param <T>  元素类型
     * @param list 被反转的List
     * @return 反转后的List
     * @since 4.0.6
     */
    public static <T> List<T> reverse(List<T> list) {
        Collections.reverse(list);
        return list;
    }

    /**
     * 反序给定List，会创建一个新的List，原List数据不变
     *
     * @param <T>  元素类型
     * @param list 被反转的List
     * @return 反转后的List
     * @since 4.0.6
     */
    public static <T> List<T> reverseNew(List<T> list) {
        final List<T> list2 = ObjUtil.clone(list);
        return reverse(list2);
    }

    /**
     * 取最大值
     *
     * @param <T>  元素类型
     * @param coll 集合
     * @return 最大值
     * @see Collections#max(Collection)
     * @since 4.6.5
     */
    public static <T extends Comparable<? super T>> T max(Collection<T> coll) {
        return Collections.max(coll);
    }

    /**
     * 取最大值
     *
     * @param <T>  元素类型
     * @param coll 集合
     * @return 最大值
     * @see Collections#min(Collection)
     * @since 4.6.5
     */
    public static <T extends Comparable<? super T>> T min(Collection<T> coll) {
        return Collections.min(coll);
    }

    // ---------------------------------------------------------------------------------------------- Interface start

    /**
     * 只对集合以及集合的第一层元素节点深拷贝，不但拷贝集合，集合中的第一层元素也拷贝，
     * 当集合中元素未实现Cloneble或Serializable时，将使用效率低下的json，请慎重使用。
     *
     * @param <T>  元素类型
     * @param coll 集合
     * @return 最大值
     * @since 4.6.5
     */
    public static <T extends Collection<E>, E> T deepClone(T coll) {
        T res = ReflectUtil.newInstance(ClassUtil.getClass(coll));
        for (E e : coll) {
            E clone = ObjUtil.clone(e);
            res.add(clone);
        }
        return res;
    }

    /**
     * 针对两个参数做相应的操作，例如Map中的KEY和VALUE
     *
     * @param <K> KEY类型
     * @param <V> VALUE类型
     * @author Looly
     */
    public interface KVConsumer<K, V> {
        /**
         * 接受并处理一对参数
         *
         * @param key   键
         * @param value 值
         * @param index 参数在集合中的索引
         */
        void accept(K key, V value, int index);
    }

    // ---------------------------------------------------------------------------------------------- Interface end

    /**
     * list转换map
     * @param list     需要转换的list
     * @param function 分类依据
     * @param <V>
     * @param <K>
     * @return
     */
    public static <V,K>Map<K,V> listToMap (Collection<V> list, Function<V,K> function){
        return list.parallelStream().collect(Collectors.toMap(function, v-> v));
    }

    /**
     *
     * @param list     需要转换的list
     * @param function 分类依据
     * @param callBack 键冲突解决方法
     * @param <V>
     * @param <K>
     * @return
     */
    public static <V,K>Map<K,V> listToMap (Collection<V> list, Function<V,K> function, BinaryOperator<V> callBack){
        return list.parallelStream().collect(Collectors.toMap(function, v-> v,callBack));
    }

}
