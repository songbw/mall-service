package com.fengchao.aoyi.client.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.aoyi.client.bean.FusionKioskBean;
import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.config.AoyiClientConfig;
import com.fengchao.aoyi.client.service.FusionService;
import com.fengchao.aoyi.client.utils.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @author songbw
 * @date 2021/1/27 17:59
 */
@Slf4j
@EnableConfigurationProperties({AoyiClientConfig.class})
@Service
public class FusionServiceImpl implements FusionService {


    private final AoyiClientConfig aoyiClientConfig;

    @Autowired
    public FusionServiceImpl(AoyiClientConfig aoyiClientConfig) {
        this.aoyiClientConfig = aoyiClientConfig;
    }

    @Override
    public OperaResponse getAllKiosk() {
        OperaResponse response = new OperaResponse() ;
        FusionKioskBean kioskBean = new FusionKioskBean() ;
        kioskBean.setToken(aoyiClientConfig.getFusionToken());

        JSONObject jsonObject = HttpClient.postHeader(kioskBean, JSONObject.class, aoyiClientConfig.getFusionBaseUrl(), aoyiClientConfig.getFusionKioskPath(), aoyiClientConfig.getFusionAppKey()) ;
        log.info("查询所有设备 返回结果： {}", jsonObject.toJSONString());
        if (jsonObject != null) {
            String code = jsonObject.getString("Code") ;
            if ("200".equals(code)) {
                JSONObject dataJsonObject = jsonObject.getJSONObject("Data") ;
                JSONArray kiosks = dataJsonObject.getJSONArray("Kiosks") ;
                response.setData(kiosks);
            } else {
                response.setCode(Integer.valueOf(code));
                response.setMsg(jsonObject.getString("Meta"));
            }
        }
        return response;
    }

    @Override
    public OperaResponse getAllSoltStatus(String status) {
        OperaResponse response = new OperaResponse() ;
        FusionKioskBean kioskBean = new FusionKioskBean() ;
        kioskBean.setToken(aoyiClientConfig.getFusionToken());
        kioskBean.setStatus(status);
        JSONObject jsonObject = HttpClient.postHeader(kioskBean, JSONObject.class, aoyiClientConfig.getFusionBaseUrl(), aoyiClientConfig.getFusionSoltPath(), aoyiClientConfig.getFusionAppKey()) ;
        log.info("查询所有货道状态 返回结果： {}", jsonObject.toJSONString());
        if (jsonObject != null) {
            String code = jsonObject.getString("Code") ;
            if ("200".equals(code)) {
                JSONObject dataJsonObject = jsonObject.getJSONObject("Data") ;
                JSONArray kiosks = dataJsonObject.getJSONArray("Slots") ;
                response.setData(kiosks);
            } else {
                response.setCode(Integer.valueOf(code));
                response.setMsg(jsonObject.getString("Meta"));
            }
        }
        return response;
    }
}
