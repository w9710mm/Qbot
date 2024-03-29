package com.mm.qbot.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.utils
 * @Description: 序列化工具
 * @date 2021/10/30 18:04
 */
public class ObjectRedisSerializer implements RedisSerializer<Object> {

    /**
     * 定义序列化和反序列化转化类
     */
    private Converter<Object, byte[]> serializer;
    private Converter<byte[], Object> deserializer = new DeserializingConverter();

    /**
     * 定义转换空字节数组
     */
    private static final byte[] EMPTY_ARRAY = new byte[0];

    public ObjectRedisSerializer() {
        serializer = new SerializingConverter();
    }

    @Override
    public byte[] serialize(Object obj) throws SerializationException {
        byte[] byteArray = null;
        if (null == obj) {
            System.err.println("----------------------------->:Redis待序列化的对象为空.");
            byteArray = EMPTY_ARRAY;
        } else {
            try {
                byteArray = serializer.convert(obj);
            } catch (Exception e) {
                System.err.println("----------------------------->Redis序列化对象失败,异常："+e.getMessage());
                byteArray = EMPTY_ARRAY;
            }
        }
        return byteArray;
    }

    @Override
    public Object deserialize(byte[] datas) throws SerializationException {
        Object obj = null;
        if((null == datas)|| (datas.length == 0)){
            System.out.println("---------------------------------->Redis待反序列化的对象为空.");
        }else{
            try {
                obj = deserializer.convert(datas);
            } catch (Exception e) {
                System.out.println("------------------------------------->Redis反序列化对象失败,异常："+e.getMessage());
            }
        }
        return obj;
    }
}