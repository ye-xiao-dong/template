package com.yd.springbootdemo.utils.convert;

import com.alibaba.fastjson.JSON;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * 描述：接收bean参数转换为map
 *
 */

public class BeanUtil {
    /**
     * 将object转换为MultiValueMap
     * @param object 对应bean
     * @return 转换后的MultiValueMap
     */
    public static MultiValueMap<String, Object> objectToMap(Object object){
        return mapToMultiValueMap(convertBean(object,Map.class));
    }

    /**
     * 将bean转换为另一种bean实体
     * @param o
     * @param entityClass
     * @param <T>
     * @return
     */
    private static <T> T convertBean(Object o,Class<T> entityClass) {
        if (null == o){
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(o),entityClass);
    }

    /**
     * 将map转换为MultiValueMap
     * @param paramsMap Map
     * @return MultiValueMap
     */
    private static MultiValueMap<String, Object> mapToMultiValueMap(Map<String, Object> paramsMap) {
        MultiValueMap<String, Object> resultMap = new LinkedMultiValueMap<>();
        for (String key : paramsMap.keySet()){
            resultMap.add(key,paramsMap.get(key));
        }
        return resultMap;
    }
}
