package com.yd.springbootdemo.utils.lang;

import com.yd.springbootdemo.utils.collection.ArrayUtil;
import com.yd.springbootdemo.utils.convert.JsonUtil;
import com.yd.springbootdemo.utils.exceptions.UtilException;
import com.yd.springbootdemo.utils.io.FastOutputStream;
import com.yd.springbootdemo.utils.reflect.ReflectUtil;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 对象工具类，包括判空、克隆、序列化等操作
 *
 * @author： 叶小东
 * @date： 2019/12/24 19:54
 */
public class ObjUtil {

    /**
     * 克隆对象<br data-tomark-pass>
     * 如果对象实现Cloneable接口，调用其clone方法<br data-tomark-pass>
     * 如果实现Serializable接口，执行深度克隆<br data-tomark-pass>
     * 否则返回<code>null</code>
     *
     * @param <T> 对象类型
     * @param obj 被克隆对象
     * @return 克隆后的对象
     */
    public static <T> T clone(T obj) {
        T result = ArrayUtil.clone(obj);
        if (null == result) {
            if (obj instanceof Cloneable) {
                result = ReflectUtil.invoke(obj, "clone");
            }
            else {
                result = cloneByStream(obj);
            }
        }
        return result;
    }

    /**
     * 序列化后拷贝流的方式克隆
     * 对象必须实现Serializable接口
     * 最后使用json序列化兜底，但效率很低
     * @param <T> 对象类型
     * @param obj 被克隆对象
     * @return 克隆后的对象
     * @throws UtilException IO异常和ClassNotFoundException封装
     */
    @SuppressWarnings("unchecked")   public static <T> T cloneByStream(T obj) {
        if (null == obj || !(obj instanceof Serializable)) {
            if(null == obj){
                return null;
            }
            try{
                obj = JsonUtil.jsonStrToObj(JsonUtil.toJsonStr(obj), obj == null ? null : (Class<T>) obj.getClass());
            }catch(Exception e){
                return null;
            }
            return obj;
        }
        final FastOutputStream byteOut = new FastOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(byteOut);
            out.writeObject(obj);
            out.flush();
            final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteOut.toByteArray()));
            return (T) in.readObject();
        } catch (Exception e) {
            throw new UtilException(e);
        } finally {
            if (null != out){
                try {
                    out.close();
                } catch (Exception e){

                }
            }
        }
    }
}



