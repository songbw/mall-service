package com.fengchao.aoyi.client.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @Auther: LabanYB
 * @Date: 2019/10/31 09:25
 * @Description: sort
 */
@Slf4j
public class SortUtils {

    /**
     * @param param 参数
     * @param isLower 是否小写
     * @return
     */
    public static String formatUrlParam(Map<String, String> param, boolean isLower) throws Exception {
        String params = "";
        Map<String, String> map = param;

        log.info("排序参数 原始参数:{}", JSONUtil.toJsonString(param));
        try {
            List<Map.Entry<String, String>> itmes = new ArrayList<Map.Entry<String, String>>(map.entrySet());

            //对所有传入的参数按照字段名从小到大排序
            //Collections.sort(items); 默认正序
            //可通过实现Comparator接口的compare方法来完成自定义排序
            Collections.sort(itmes, new Comparator<Map.Entry<String, String>>() {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey().toString().compareTo(o2.getKey()));
                }
            });

            //构造URL 键值对的形式
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> item : itmes) {
                if (StringUtils.isNotBlank(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    //val = URLEncoder.encode(val, encode);
                    if (isLower) {
                        sb.append(key.toLowerCase() + "=" + val);
                    } else {
                        sb.append(key + "=" + val);
                    }
                    sb.append("&");
                }
            }

            params = sb.toString();
            if (!params.isEmpty()) {
                params = params.substring(0, params.length() - 1);
            } else {
                log.warn("排序参数 结果为空");
                throw new Exception("排序参数 结果为空");
            }
        } catch (Exception e) {
            log.info("排序参数异常:{}", e.getMessage(), e);

            throw e;
        }

        log.info("排序参数 结果是:{}", params);
        return params;
    }
}
