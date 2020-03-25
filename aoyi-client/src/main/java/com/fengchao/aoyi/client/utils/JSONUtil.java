package com.fengchao.aoyi.client.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class JSONUtil {

    /**
     *
     * @param obj
     * @return
     */
    public static final String toJsonString(Object obj) {
        String json = null;

        try {
            json = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
        } catch (Exception e) {
            log.error("JSONUtil#toJsonString 异常:{}", e.getMessage(), e);

            json = null;
        }

        return json;
    }

    /**
     *
     * @param obj
     * @return
     */
    public static final String toJsonStringWithoutNull(Object obj) {
        String json = null;

        try {
            json = JSON.toJSONString(obj);
        } catch (Exception e) {
            log.error("JSONUtil#toJsonString 异常:{}", e.getMessage(), e);

            json = null;
        }

        return json;
    }

    /**
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parse(String json, Class<T> clazz) {
        T object = null;

        try {
            object = JSON.parseObject(json, clazz);
        } catch (Exception e) {
            log.error("JSONUtil#parse 异常:{}", e.getMessage(), e);

            object = null;
        }

        return object;
    }

    /**
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> parseList(String json, Class<T> clazz) {
        List<T> list = null;

        try {
            list = JSON.parseArray(json, clazz);
        } catch (Exception e) {
            log.error("JSONUtil#parseList 异常:{}", e.getMessage(), e);

            list = null;
        }

        return list;
    }
}
