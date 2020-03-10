package com.yd.springbootdemo.utils.convert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 将JSON对象和Map、List进行转换
 *
 * @author： 叶小东
 * @date： 2019/12/24 17:28
 */
public class JsonUtil {

    private static Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder().serializeNulls();
        gson = builder.create();
    }

    public static String toJsonStr(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T jsonStrToObj(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

}


