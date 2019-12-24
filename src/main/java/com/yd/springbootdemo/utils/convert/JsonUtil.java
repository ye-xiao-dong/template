package com.yd.springbootdemo.utils.convert;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 将JSON对象和Map、List进行转换
 *
 * @author： 叶小东
 * @date： 2019/12/24 17:28
 */
public class JsonUtil {

    private static Gson gson;
    private static JsonParser jsonParser;

    static {
        GsonBuilder builder = new GsonBuilder().serializeNulls();
        gson = builder.create();
        jsonParser = new JsonParser();
    }

    public static String toJsonStr(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T jsonStrToObj(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T jsonStrToObj(String json, TypeToken<T> token) {
        return gson.fromJson(json, token.getType());
    }

    public static <T> ArrayList<T> jsonStrToList(String json, Class<T> classOfT) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjs = gson.fromJson(json, type);
        ArrayList<T> listOfT = new ArrayList<>();
        for (JsonObject jsonObj : jsonObjs) {
            listOfT.add(gson.fromJson(jsonObj, classOfT));
        }
        return listOfT;
    }

    public static <T> List<T> jsonArrToList(JsonArray jsonArray, Class<T> classOfT) {
        ArrayList<T> list = new ArrayList<>();
        Type type = new TypeToken<ArrayList<JsonObject>>(){}.getType();
        ArrayList<JsonObject> jsonObjs = gson.fromJson(jsonArray, type);
        for (JsonObject jsonObj : jsonObjs) {
            list.add(gson.fromJson(jsonObj, classOfT));
        }
        return list;
    }
}


