package com.fengchao.aoyi.client.weipinhuiService.impl;

import com.fengchao.aoyi.client.bean.dto.AoyiProdRquest;
import com.fengchao.aoyi.client.bean.dto.BrandResDto;
import com.fengchao.aoyi.client.config.WeipinhuiClientConfig;
import com.fengchao.aoyi.client.utils.JSONUtil;
import com.fengchao.aoyi.client.utils.SortUtils;
import com.fengchao.aoyi.client.weipinhuiService.ProductWeipinhuiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductWeipinhuiServiceImpl implements ProductWeipinhuiService {

    private WeipinhuiClientConfig weipinhuiClientConfig;

    @Autowired
    public ProductWeipinhuiServiceImpl(WeipinhuiClientConfig weipinhuiClientConfig) {
        this.weipinhuiClientConfig = weipinhuiClientConfig;
    }

    @Override
    public List<BrandResDto> getBrand(Integer pageNumber, Integer pageSize) throws Exception {
        /**
        HttpUtils httpUtils = new HttpUtils();
        Long timestamp = System.currentTimeMillis();

        AoyiProdRquest aoyiProdRquest = new AoyiProdRquest();
        aoyiProdRquest.setAppId(weipinhuiClientConfig.getAppId());
        aoyiProdRquest.setAppSecret(weipinhuiClientConfig.getAppSecret());
        aoyiProdRquest.setTimestamp(timestamp);
        aoyiProdRquest.setPageNumber(pageNumber);
        aoyiProdRquest.setPageSize(pageSize);


        Map<String, String> myselfSign = new HashMap<>();
        myselfSign.put("appId",String.valueOf(aoyiProdRquest.getAppId()));
        myselfSign.put("appSecret",String.valueOf(aoyiProdRquest.getAppSecret()));
        myselfSign.put("timestamp",String.valueOf(aoyiProdRquest.getTimestamp()));

        //
        String sortParamString = SortUtils.formatUrlParam(myselfSign, false);

        log.info("获取品牌列表 参数排序后为:{}", JSONUtil.toJsonString(aoyiProdRquest));

        String signMyself = RSAUtil.signMyself(sortParamString);

        aoyiProdRquest.setSign(signMyself);

        String info = JSON.toJSONString(aoyiProdRquest);

        System.out.println(info);

        String resultInfo = null;
        try {
            resultInfo = httpUtils.sendHttpPost(categoryUrl,info,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("==============");
        System.out.println(resultInfo);
        System.out.println("==============");
         **/


        return null;
    }
}
