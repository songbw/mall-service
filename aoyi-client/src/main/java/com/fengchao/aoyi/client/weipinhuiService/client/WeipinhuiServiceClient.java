package com.fengchao.aoyi.client.weipinhuiService.client;

import com.alibaba.fastjson.JSON;
import com.fengchao.aoyi.client.bean.dto.*;
import com.fengchao.aoyi.client.config.WeipinhuiClientConfig;
import com.fengchao.aoyi.client.utils.HttpClient;
import com.fengchao.aoyi.client.utils.JSONUtil;
import com.fengchao.aoyi.client.utils.RSAUtil;
import com.fengchao.aoyi.client.utils.SortUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WeipinhuiServiceClient {

    private WeipinhuiClientConfig weipinhuiClientConfig;

    @Autowired
    public WeipinhuiServiceClient(WeipinhuiClientConfig weipinhuiClientConfig) {
        this.weipinhuiClientConfig = weipinhuiClientConfig;
    }

    /**
     * 唯品会获取品牌列表
     *
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */
    public WeipinhuiResponse getBrand(Integer pageNumber, Integer pageSize) throws Exception {
        try {
            Long timestamp = System.currentTimeMillis(); // 时间戳

            // 1. 准备签名字段
            Map<String, String> myselfSign = new HashMap<>();
            myselfSign.put("appId", weipinhuiClientConfig.getAppId());
            myselfSign.put("appSecret", weipinhuiClientConfig.getAppSecret());
            myselfSign.put("timestamp", String.valueOf(timestamp));

            // 排序
            String sortParamString = SortUtils.formatUrlParam(myselfSign, false);
            log.info("唯品会获取品牌列表 签名字段排序后为:{}", JSONUtil.toJsonString(sortParamString));

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

            String requestParam = JSONUtil.toJsonString(aoyiProdRquest);

            log.info("唯品会获取品牌列表 准备请求唯品会 url:{} 请求参数:{}", weipinhuiClientConfig.getUrlGetBrand(), requestParam);
            // {"success":true,"message":"操作成功！","code":200,"result":[{"brandId":"12507","brandName":"药都仁和"},{"brandId":"12508","brandName":"药房妈妈"},{"brandId":"12509","brandName":"夜线"},{"brandId":"12510","brandName":"伊尔滋"},{"brandId":"12511","brandName":"伊拉苏"},{"brandId":"12512","brandName":"咿啦看书"},{"brandId":"12513","brandName":"怡乐雅"},{"brandId":"12514","brandName":"宜格"},{"brandId":"12515","brandName":"宜欣缘"},{"brandId":"12516","brandName":"益华"},{"brandId":"1001","brandName":"飞利浦"},{"brandId":"12517","brandName":"印克"},{"brandId":"12518","brandName":"英国摩飞"},{"brandId":"12519","brandName":"英美子"},{"brandId":"12520","brandName":"樱之洁"},{"brandId":"12521","brandName":"永富"},{"brandId":"12522","brandName":"咏年堂"},{"brandId":"12523","brandName":"优酷VIP会员"},{"brandId":"12524","brandName":"优米"},{"brandId":"12525","brandName":"优尚"}],"timestamp":1578995442669}
            // {"success":false,"message":"pageNumber cannot be null","code":1000,"result":null,"timestamp":1578996160705}
            // {"success":true,"message":"操作成功！","code":200,"result":[],"timestamp":1578996323859}
            String resultInfo = HttpClient.sendHttpPost(weipinhuiClientConfig.getUrlGetBrand(), requestParam, "utf-8");

            log.info("唯品会获取品牌列表 请求唯品会 原始返回:{}", resultInfo);

            // 3. 解析返回
            WeipinhuiResponse weipinhuiResponse = JSONUtil.parse(resultInfo, WeipinhuiResponse.class);
            if (weipinhuiResponse.getCode().intValue() != 200) {
                log.warn("唯品会获取品牌列表 获取到错误返回");

                throw new Exception(weipinhuiResponse.getMessage());
            }

            return weipinhuiResponse;
        } catch (Exception e) {
            log.error("唯品会获取品牌列表 异常:{}", e.getMessage(), e);

            throw e;
        }
    }

    /**
     * 唯品会获取类目列表
     *
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */
    public WeipinhuiResponse getCategory(Integer pageNumber, Integer pageSize) throws Exception {
        try {
            Long timestamp = System.currentTimeMillis(); // 时间戳

            // 1. 准备签名字段
            Map<String, String> myselfSign = new HashMap<>();
            myselfSign.put("appId", weipinhuiClientConfig.getAppId());
            myselfSign.put("appSecret", weipinhuiClientConfig.getAppSecret());
            myselfSign.put("timestamp", String.valueOf(timestamp));

            // 排序
            String sortParamString = SortUtils.formatUrlParam(myselfSign, false);
            log.info("唯品会获取类目列表 签名字段排序后为:{}", JSONUtil.toJsonString(sortParamString));

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

            String requestParam = JSONUtil.toJsonString(aoyiProdRquest);

            log.info("唯品会获取类目列表 准备请求唯品会 url:{} 请求参数:{}",
                    weipinhuiClientConfig.getUrlGetCategory(), requestParam);

            String resultInfo = HttpClient.sendHttpPost(weipinhuiClientConfig.getUrlGetCategory(), requestParam, "utf-8");

            log.info("唯品会获取类目列表 请求唯品会 原始返回:{}", resultInfo);

            // 3. 解析返回
            WeipinhuiResponse weipinhuiResponse = JSONUtil.parse(resultInfo, WeipinhuiResponse.class);
            if (weipinhuiResponse.getCode().intValue() != 200) {
                log.warn("唯品会获取类目列表 获取到错误返回");

                throw new Exception(weipinhuiResponse.getMessage());
            }

            return weipinhuiResponse;
        } catch (Exception e) {
            log.error("唯品会获取类目列表 异常:{}", e.getMessage(), e);

            throw e;
        }
    }

    /**
     * 唯品会获取items列表
     *
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */
    public WeipinhuiResponse queryItemsList(Integer pageNumber, Integer pageSize) throws Exception {
        try {
            Long timestamp = System.currentTimeMillis(); // 时间戳

            // 1. 准备签名字段
            Map<String, String> myselfSign = new HashMap<>();
            myselfSign.put("appId", weipinhuiClientConfig.getAppId());
            myselfSign.put("appSecret", weipinhuiClientConfig.getAppSecret());
            myselfSign.put("timestamp", String.valueOf(timestamp));

            // 排序
            String sortParamString = SortUtils.formatUrlParam(myselfSign, false);
            log.info("唯品会获取items列表 签名字段排序后为:{}", JSONUtil.toJsonString(sortParamString));

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

            String requestParam = JSONUtil.toJsonString(aoyiProdRquest);

            log.info("唯品会获取items列表 准备请求唯品会 url:{} 请求参数:{}",
                    weipinhuiClientConfig.getUrlQueryItemsList(), requestParam);

            String resultInfo = HttpClient.sendHttpPost(weipinhuiClientConfig.getUrlQueryItemsList(), requestParam, "utf-8");

            log.info("唯品会获取items列表 请求唯品会 原始返回:{}", resultInfo);

            // 3. 解析返回
            WeipinhuiResponse weipinhuiResponse = JSONUtil.parse(resultInfo, WeipinhuiResponse.class);
            if (weipinhuiResponse.getCode().intValue() != 200) {
                log.warn("唯品会获取items列表 获取到错误返回");

                throw new Exception(weipinhuiResponse.getMessage());
            }

            return weipinhuiResponse;
        } catch (Exception e) {
            log.error("唯品会获取items列表 异常:{}", e.getMessage(), e);

            throw e;
        }
    }

    /**
     * 根据itemId查询详情
     *
     * @param itemId
     * @return
     * @throws Exception
     */
    public WeipinhuiResponse queryItemDetial(String itemId) throws Exception {
        try {
            Long timestamp = System.currentTimeMillis(); // 时间戳

            // 1. 组装请求参数
            AoyiProductDetailRequest aoyiProductDetailRequest = new AoyiProductDetailRequest();
            aoyiProductDetailRequest.setAppId(weipinhuiClientConfig.getAppId());
            aoyiProductDetailRequest.setAppSecret(weipinhuiClientConfig.getAppSecret());
            aoyiProductDetailRequest.setTimestamp(timestamp);

            aoyiProductDetailRequest.setItemId(itemId);

            // 2. 准备签名字段
            Map<String, String> myselfSign = new HashMap<>();
            myselfSign.put("appId", weipinhuiClientConfig.getAppId());
            myselfSign.put("appSecret", weipinhuiClientConfig.getAppSecret());
            myselfSign.put("timestamp", String.valueOf(timestamp));

            myselfSign.put("aoyiProductDetailRequest", JSON.toJSONString(aoyiProductDetailRequest));

            // 排序
            String sortParamString = SortUtils.formatUrlParam(myselfSign, false);
            log.info("唯品会根据itemId查询详情 签名字段排序后为:{}", JSONUtil.toJsonString(sortParamString));

            // 签名
            String signMyself = RSAUtil.signMyself(sortParamString);


            // 3. 请求
            aoyiProductDetailRequest.setSign(signMyself);
            String requestParam = JSONUtil.toJsonString(aoyiProductDetailRequest);

            log.info("唯品会根据itemId查询详情 准备请求唯品会 url:{} 请求参数:{}",
                    weipinhuiClientConfig.getUrlQueryItemDetial(), requestParam);

            String resultInfo = HttpClient.sendHttpPost(weipinhuiClientConfig.getUrlQueryItemDetial(), requestParam, "utf-8");

            log.info("唯品会根据itemId查询详情 请求唯品会 原始返回:{}", resultInfo);

            // 3. 解析返回
            WeipinhuiResponse weipinhuiResponse = JSONUtil.parse(resultInfo, WeipinhuiResponse.class);
            if (weipinhuiResponse.getCode().intValue() != 200) {
                log.warn("唯品会根据itemId查询详情 获取到错误返回");

                throw new Exception(weipinhuiResponse.getMessage());
            }

            return weipinhuiResponse;
        } catch (Exception e) {
            log.error("唯品会根据itemId查询详情 异常:{}", e.getMessage(), e);

            throw e;
        }
    }

    /**
     * 库存查询接口
     *
     * @param itemId
     * @param skuId
     * @param num
     * @param divisionCode
     * @return
     * @throws Exception
     */
    public WeipinhuiResponse queryItemInventory(String itemId, String skuId, Integer num, String divisionCode) throws Exception {
        try {
            Long timestamp = System.currentTimeMillis(); // 时间戳

            // 1. 组装请求参数
            AoyiProductInventoryRequest aoyiProductInventoryRequest = new AoyiProductInventoryRequest();
            aoyiProductInventoryRequest.setAppId(weipinhuiClientConfig.getAppId());
            aoyiProductInventoryRequest.setAppSecret(weipinhuiClientConfig.getAppSecret());
            aoyiProductInventoryRequest.setTimestamp(timestamp);

            aoyiProductInventoryRequest.setItemId(itemId);
            aoyiProductInventoryRequest.setSkuId(skuId);
            aoyiProductInventoryRequest.setNum(num);
            aoyiProductInventoryRequest.setDivisionCode(divisionCode);

            // 2. 准备签名字段
            Map<String, String> myselfSign = new HashMap<>();
            myselfSign.put("appId", weipinhuiClientConfig.getAppId());
            myselfSign.put("appSecret", weipinhuiClientConfig.getAppSecret());
            myselfSign.put("timestamp", String.valueOf(timestamp));

            myselfSign.put("aoyiProductInventoryRequest", JSON.toJSONString(aoyiProductInventoryRequest));

            // 排序
            String sortParamString = SortUtils.formatUrlParam(myselfSign, false);
            log.info("唯品会库存查询接口 签名字段排序后为:{}", JSONUtil.toJsonString(sortParamString));

            // 签名
            String signMyself = RSAUtil.signMyself(sortParamString);


            // 3. 请求
            aoyiProductInventoryRequest.setSign(signMyself);
            String requestParam = JSONUtil.toJsonString(aoyiProductInventoryRequest);

            String requestUrl = weipinhuiClientConfig.getUrlQueryItemInventory();
            log.info("唯品会库存查询接口 准备请求唯品会 url:{} 请求参数:{}", requestUrl, requestParam);

            String resultInfo = HttpClient.sendHttpPost(requestUrl, requestParam, "utf-8");

            log.info("唯品会库存查询接口 请求唯品会 原始返回:{}", resultInfo);

            // 3. 解析返回
            WeipinhuiResponse weipinhuiResponse = JSONUtil.parse(resultInfo, WeipinhuiResponse.class);
            if (weipinhuiResponse.getCode().intValue() != 200) {
                log.warn("唯品会库存查询接口 获取到错误返回");

                throw new Exception(weipinhuiResponse.getMessage());
            }

            return weipinhuiResponse;
        } catch (Exception e) {
            log.error("唯品会库存查询接口 异常:{}", e.getMessage(), e);

            throw e;
        }
    }

    /**
     * 预占订单接口
     *
     * @param aoyiRenderOrderRequest
     * @return
     * @throws Exception
     */
    public WeipinhuiResponse renderOrder(AoyiRenderOrderRequest aoyiRenderOrderRequest) throws Exception {
        try {
            Long timestamp = System.currentTimeMillis(); // 时间戳

            // 1. 组装请求参数
            aoyiRenderOrderRequest.setAppId(weipinhuiClientConfig.getAppId());
            aoyiRenderOrderRequest.setAppSecret(weipinhuiClientConfig.getAppSecret());
            aoyiRenderOrderRequest.setTimestamp(timestamp);

            // 2. 准备签名字段
            Map<String, String> myselfSign = new HashMap<>();
            myselfSign.put("appId", weipinhuiClientConfig.getAppId());
            myselfSign.put("appSecret", weipinhuiClientConfig.getAppSecret());
            myselfSign.put("timestamp", String.valueOf(timestamp));

            myselfSign.put("aoyiRenderOrderRequest", JSON.toJSONString(aoyiRenderOrderRequest));

            // 排序
            String sortParamString = SortUtils.formatUrlParam(myselfSign, false);
            log.info("唯品会预占订单接口 签名字段排序后为:{}", JSONUtil.toJsonString(sortParamString));

            // 签名
            String signMyself = RSAUtil.signMyself(sortParamString);


            // 3. 请求
            aoyiRenderOrderRequest.setSign(signMyself);
            String requestParam = JSONUtil.toJsonString(aoyiRenderOrderRequest);

            String requestUrl = weipinhuiClientConfig.getUrlRenderOrder();
            log.info("唯品会预占订单接口 准备请求唯品会 url:{} 请求参数:{}", requestUrl, requestParam);

            String resultInfo = HttpClient.sendHttpPost(requestUrl, requestParam, "utf-8");

            log.info("唯品会预占订单接口 请求唯品会 原始返回:{}", resultInfo);

            // 3. 解析返回
            WeipinhuiResponse weipinhuiResponse = JSONUtil.parse(resultInfo, WeipinhuiResponse.class);
            if (weipinhuiResponse.getCode().intValue() != 200) {
                log.warn("唯品会预占订单接口 获取到错误返回");

                throw new Exception(weipinhuiResponse.getMessage());
            }

            return weipinhuiResponse;
        } catch (Exception e) {
            log.error("唯品会预占订单接口 异常:{}", e.getMessage(), e);

            throw e;
        }
    }
}
