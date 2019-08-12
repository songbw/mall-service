package com.fengchao.elasticsearch.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fengchao.elasticsearch.domain.QueryBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CosUtil {
    public static String baseAoyiProdUrl = "http://aoyiprod-1252099010.cossh.myqcloud.com/";
    public static String iWalletUrlT = "https://iwallet-1258175138.cos.ap-beijing.myqcloud.com";

    public static void main(String args[]) {
//        QueryBean user = new QueryBean();
//        user.setPageNo(1);
//        user.setPageSize(28);
//        String json = "{\"page_no\":1,\"page_size\":28,\"status\":-1,\"start_time\":null,\"end_time\":null,\"key\":\"\",\"open_id\":null}";
        Map map = new HashMap();
        map.put("page_no", 1) ;
        map.put("page_size", 28) ;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        try {
            String json  = mapper.writeValueAsString(map) ;
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
