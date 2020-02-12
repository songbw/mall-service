package com.fengchao.product.aoyi.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;

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
}
