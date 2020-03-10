package com.yd.springbootdemo.utils.convert;

/**
 * 分页工具类
 *
 * @author： 叶小东
 * @date： 2019/12/24 17:35
 */

public class PageUtil {

    /**
     * 将页数和每页条目数转换为开始位置
     * 此方法用于不包括结束位置的分页方法
     * 例如：
     * 页码：1，每页10 =》 0
     * 页码：2，每页10 =》 10
     *
     * @param pageNo 页码（从1计数）
     * @param pageSize 每页条目数
     * @return 开始位置
     */
    public static int getStart(int pageNo, int pageSize) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageSize < 1) {
            pageSize = 0;
        }
        return (pageNo - 1) * pageSize;
    }

    /**
     * 将页数和每页条目数转换为开始位置和结束位置
     * 此方法用于包括结束位置的分页方法
     * 例如：
     * 页码：1，每页10 =》 [0, 10]
     * 页码：2，每页10 =》 [10, 20]
     *
     * @param pageNo 页码（从1计数）
     * @param pageSize 每页条目数
     * @return 第一个数为开始位置，第二个数为结束位置
     */
    public static int[] transToStartEnd(int pageNo, int pageSize) {
        final int start = getStart(pageNo, pageSize);
        if (pageSize < 1) {
            pageSize = 0;
        }
        final int end = start + pageSize;
        return new int[] { start, end };
    }


}


