package com.yd.springbootdemo.utils.comparator;

import java.util.Comparator;
import java.util.List;

public class MultiColumnSortUtil {

    /**
     * 多条件组合排序
     *
     * @param needSortList   需要排序的集合
     * @param comparatorList 排序规则集合，按照集合顺序排序
     * @return
     */
    public static List sort(List needSortList, List<? extends Comparator> comparatorList) {

        if (comparatorList == null || comparatorList.size() <= 0) {
            return null;
        }
        Comparator comparator = comparatorList.get(0);

        for(int i = 1, j = comparatorList.size(); i < j; i++){
            comparator = comparator.thenComparing(comparatorList.get(i));
        }
        needSortList.sort(comparator);

        return needSortList;
    }

    /**
     * 多条件组合排序
     *
     * @param needSortList 需要排序的集合
     * @param comparators  排序规则，可多个，按照顺序排序
     * @return
     */
    public static List sort(List needSortList, Comparator... comparators) {

        if (comparators == null || comparators.length <= 0) {
            return null;
        }
        Comparator comparator = comparators[0];

        for(int i = 1, j = comparators.length; i < j; i++){
            comparator = comparator.thenComparing(comparators[i]);
        }
        needSortList.sort(comparator);

        return needSortList;
    }
}

