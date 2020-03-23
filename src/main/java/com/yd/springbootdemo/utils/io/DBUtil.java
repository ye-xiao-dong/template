package com.yd.springbootdemo.utils.io;

import com.yd.springbootdemo.utils.collection.CollUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 数据库写入工具
 *
 */
public class DBUtil {

    /**
     * 写入数据库方法，有则更新，无则插入
     * 注：
     *
     * 传入的Mapper中必须存在以下方法，且参数类型一致
     *  1. updateByPrimaryKeySelective：参数为list集合中元素类型
     *  2. insertBatch：参数为List类，不能指定具体子类以及元素类型
     *  3. 自己传入的methodName：参数为list集合中元素类型
     *
     * 传入的list集合的元素必须存在 getId 和 setId 方法，且id类型为Integer
     * 传入的list集合的元素必须重写equals方法，且逻辑与methodName的判断重复逻辑一致
     *
     * @param list       需要写入的数据集合
     * @param mapper     用于数据库操作的Mapper
     * @param methodName 用于判断是否存在的select方法名
     * @param <T>        集合中元素类型
     * @return           新增或更新的条数
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T> int writeToDB(List<T> list, Object mapper, String methodName, boolean directInsert) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int result = 0;

        if(list == null || list.isEmpty()) {
            return result;
        }

        // 拿到mapper对象的五个指定的方法
        Class tClass = list.get(0).getClass();
        Method setId = tClass.getMethod("setId", Integer.class);
        Method getId = tClass.getMethod("getId");
        Class mapperClass = mapper.getClass();
        Method select = mapperClass.getMethod(methodName, tClass);
        Method update = mapperClass.getMethod("updateByPrimaryKeySelective", tClass);
        Method insert = mapperClass.getMethod("insertBatch", List.class);


        if (directInsert){
            result += insert(list, insert, mapper);
        }
        else {

            List<T> needInsert = new LinkedList();
            for (T t : list) {
                T tTemp = (T) select.invoke(mapper, t);
                if (tTemp == null && !needInsert.contains(t)) {
                    // 新增
                    needInsert.add(t);
                }
                else if (tTemp != null){
                    // 更新
                    setId.invoke(t, getId.invoke(tTemp));
                    result += (int) update.invoke(mapper, t);
                }
            }

            if(!needInsert .isEmpty()){
                result += insert(needInsert, insert, mapper);
            }
        }

        return result;
    }

    private static <T> int insert(List<T> needInsert, Method insert, Object mapper) throws InvocationTargetException, IllegalAccessException {
        int result = 0;
        int maxInsertCount = 3000;
        if (needInsert.size() > maxInsertCount){
            List<List<T>> splits = CollUtil.split(needInsert, maxInsertCount);
            needInsert = null;
            for (List<T> split : splits) {
                result += (int) insert.invoke(mapper, split);
            }
        }
        else {
            result += (int) insert.invoke(mapper, needInsert);
        }

        return result;
    }

    /**
     * 求字符串A相对于字符串B的文本相似度
     * 通过LCS算法求出最长子序列的长度，然后和字符串B对比
     *
     * @param strA 字符串A
     * @param strB 字符串B
     * @return
     */
    public static double similarity(String strA, String strB){

        if (strA == null && strB == null){
            return 1;
        }
        else if (strA == null || strB == null) {
            return 0;
        }

        double result = 0;
        String pubStr = longestCommonSubsequence(strA, strB);
        if (pubStr.isEmpty() && strB.isEmpty()){
            result = 1;
        }
        else if (!strB.isEmpty()){
            result = (double) pubStr.length() / (double) strB.length();
        }

        return result;
    }

    /**
     * 获取最长子序列
     *
     * @param left
     * @param right
     * @return
     */
    private static String longestCommonSubsequence(final CharSequence left, final CharSequence right) {

        if (left == null || right == null) {
            return null;
        }

        final StringBuilder longestCommonSubstringArray = new StringBuilder(Math.max(left.length(), right.length()));

        //获取动态规划数组
        final int[][] lcsLengthArray = longestCommonSubstringLengthArray(left, right);

        //下面是通过数组返回的公共字符串，从后向前计算，然后再反转
        //行-1,此处i，j为数组的右下角倒数一个
        int i = left.length() - 1;

        //列-1
        int j = right.length() - 1;
        int k = lcsLengthArray[left.length()][right.length()] - 1;
        while (k >= 0) {
            if (left.charAt(i) == right.charAt(j)) {
                longestCommonSubstringArray.append(left.charAt(i));
                i = i - 1;
                j = j - 1;
                k = k - 1;
            } else if (lcsLengthArray[i + 1][j] < lcsLengthArray[i][j + 1]) {

                //如果该点下边小于该点右边，在数组中（行减一，向上走），在left字符串中，向左走一位
                //右边大是left不匹配，要减去left的一位
                i = i - 1;
            } else {

                //如果该点下边小于该点右边，在数组中（列减一，向左走），在right字符串中，向左走一位
                //下边大是right不匹配，要减去right的一位
                j = j - 1;
            }
        }

        return longestCommonSubstringArray.reverse().toString();
    }

    /**
     * 获取最长公共子序列数组
     *
     * @param left
     * @param right
     * @return
     */
    private static int[][] longestCommonSubstringLengthArray(final CharSequence left, final CharSequence right) {
        final int[][] lcsLengthArray = new int[left.length() + 1][right.length() + 1];
        for (int i = 0; i < left.length(); i++) {
            for (int j = 0; j < right.length(); j++) {
                if (i == 0) {
                    lcsLengthArray[i][j] = 0;
                }
                if (j == 0) {
                    lcsLengthArray[i][j] = 0;
                }
                if (left.charAt(i) == right.charAt(j)) {
                    lcsLengthArray[i + 1][j + 1] = lcsLengthArray[i][j] + 1;
                }
                else {
                    lcsLengthArray[i + 1][j + 1] = Math.max(lcsLengthArray[i + 1][j], lcsLengthArray[i][j + 1]);
                }
            }
        }
        return lcsLengthArray;
    }
}
