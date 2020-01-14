package com.fengchao.aoyi.client.weipinhuiService.impl;

import com.fengchao.aoyi.client.bean.dto.AoyiProdRquest;
import com.fengchao.aoyi.client.bean.dto.BrandResDto;
import com.fengchao.aoyi.client.config.WeipinhuiClientConfig;
import com.fengchao.aoyi.client.utils.HttpClient;
import com.fengchao.aoyi.client.utils.JSONUtil;
import com.fengchao.aoyi.client.utils.RSAUtil;
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
        Long timestamp = System.currentTimeMillis();

        // 1. 准备签名字段
        Map<String, String> myselfSign = new HashMap<>();
        myselfSign.put("appId", weipinhuiClientConfig.getAppId());
        myselfSign.put("appSecret", weipinhuiClientConfig.getAppSecret());
        myselfSign.put("timestamp", String.valueOf(timestamp));

        // 排序
        String sortParamString = SortUtils.formatUrlParam(myselfSign, false);
        log.info("获取品牌列表 签名字段排序后为:{}", JSONUtil.toJsonString(sortParamString));

        // 签名
        String signMyself = RSAUtil.signMyself(sortParamString);



        // 2. 组装请求参数
        AoyiProdRquest aoyiProdRquest = new AoyiProdRquest();
        aoyiProdRquest.setAppId(weipinhuiClientConfig.getAppId());
        aoyiProdRquest.setAppSecret(weipinhuiClientConfig.getAppSecret());
        aoyiProdRquest.setTimestamp(timestamp);

        aoyiProdRquest.setPageNumber(pageNumber);
        aoyiProdRquest.setPageSize(pageSize);
        aoyiProdRquest.setSign(signMyself); //

        String info = JSONUtil.toJsonString(aoyiProdRquest);

        log.info("获取品牌列表 请求参数:{}", info);

        String resultInfo = null;
        try {
            // HttpUtils httpUtils = new HttpUtils();
            // {"success":true,"message":"操作成功！","code":200,"result":[{"brandId":"12507","brandName":"药都仁和"},{"brandId":"12508","brandName":"药房妈妈"},{"brandId":"12509","brandName":"夜线"},{"brandId":"12510","brandName":"伊尔滋"},{"brandId":"12511","brandName":"伊拉苏"},{"brandId":"12512","brandName":"咿啦看书"},{"brandId":"12513","brandName":"怡乐雅"},{"brandId":"12514","brandName":"宜格"},{"brandId":"12515","brandName":"宜欣缘"},{"brandId":"12516","brandName":"益华"},{"brandId":"1001","brandName":"飞利浦"},{"brandId":"12517","brandName":"印克"},{"brandId":"12518","brandName":"英国摩飞"},{"brandId":"12519","brandName":"英美子"},{"brandId":"12520","brandName":"樱之洁"},{"brandId":"12521","brandName":"永富"},{"brandId":"12522","brandName":"咏年堂"},{"brandId":"12523","brandName":"优酷VIP会员"},{"brandId":"12524","brandName":"优米"},{"brandId":"12525","brandName":"优尚"}],"timestamp":1578995442669}
            // {"success":false,"message":"pageNumber cannot be null","code":1000,"result":null,"timestamp":1578996160705}
            // {"success":true,"message":"操作成功！","code":200,"result":[],"timestamp":1578996323859}
            resultInfo = HttpClient.sendHttpPost(weipinhuiClientConfig.getUrlGetBrand(), info,"utf-8");
        } catch (Exception e) {
            log.error("获取品牌列表 异常:{}", e.getMessage(), e);
        }

        System.out.println("==============");
        System.out.println(resultInfo);
        System.out.println("==============");


        return null;
    }
}
