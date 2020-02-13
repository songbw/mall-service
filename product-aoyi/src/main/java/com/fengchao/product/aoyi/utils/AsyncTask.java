package com.fengchao.product.aoyi.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.bean.QueryBean;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.dao.StarSkuDao;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexMapper;
import com.fengchao.product.aoyi.mapper.StarDetailImgMapper;
import com.fengchao.product.aoyi.mapper.StarPropertyMapper;
import com.fengchao.product.aoyi.mapper.StarSkuMapper;
import com.fengchao.product.aoyi.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author songbw
 * @date 2019/10/11 16:28
 */
@Slf4j
@Component
public class AsyncTask {

    @Async
    public void executeAsyncProductTask(ProductDao productDao, WebTarget webTarget, List<AoyiProdIndex> prodIndices) {
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        prodIndices.forEach(prodIndex -> {
            Response response1 = invocationBuilder.put(Entity.entity(prodIndex, MediaType.APPLICATION_JSON));
            OperaResult result = response1.readEntity(OperaResult.class);
            // TODO 成功则更新商品表，失败则打印日志
            if (result.getCode() == 200) {
                productDao.updateSyncAt(prodIndex.getId());
            } else {
                log.info("线程" + Thread.currentThread().getName() + " 执行异步任务：" + "同步商品"+ prodIndex.getMpu()+"失败, 失败原因 {}", JSONUtil.toJsonString(result));
            }
        });
    }

    @Async
    public void executeAsyncCategoryTask(WebTarget webTarget, List<AoyiBaseCategory> categories) {
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        categories.forEach(category -> {
            Response response1 = invocationBuilder.put(Entity.entity(category, MediaType.APPLICATION_JSON));
            OperaResponse result = response1.readEntity(OperaResponse.class);
            if (result.getCode() != 200) {
                log.info("线程" + Thread.currentThread().getName() + " 执行异步任务：" + "同步类目"+ category.getCategoryId()+"失败, 失败原因 {}", JSONUtil.toJsonString(result));
            }
        });
    }

    @Async
    public void executeAsyncBrandTask(WebTarget webTarget, List<AoyiBaseBrand> brands) {
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        brands.forEach(brand -> {
            Response response1 = invocationBuilder.put(Entity.entity(brand, MediaType.APPLICATION_JSON));
            OperaResponse result = response1.readEntity(OperaResponse.class);
            if (result.getCode() != 200) {
                log.info("线程" + Thread.currentThread().getName() + " 执行异步任务：" + "同步品牌"+ brand.getBrandId()+"失败, 失败原因 {}", JSONUtil.toJsonString(result));
            }
        });
    }

    @Async
    public void executeAsyncStarProd(AoyiClientService aoyiClientService, ProductDao productDao, AoyiProdIndexMapper aoyiProdIndexMapper, StarDetailImgMapper starDetailImgMapper, StarPropertyMapper starPropertyMapper, StarSkuMapper starSkuMapper, StarSkuDao starSkuDao) {
        QueryBean queryBean = new QueryBean() ;
        queryBean.setPageSize(100);
        for (int x = 0; x < 1000; x++) {
            queryBean.setPageNo(x + 1);
            //		queryBean.setStartTime("2018-07-05 16:43:35");
            //		queryBean.setEndTime("2019-07-06 16:43:35");
            OperaResponse spuIdsResponse = aoyiClientService.getSpuIdList(queryBean) ;
            if (spuIdsResponse.getCode() == 200) {
                String resJsonString = JSON.toJSONString(spuIdsResponse) ;
                JSONObject resJson = JSONObject.parseObject(resJsonString) ;
                JSONArray spuArray = resJson.getJSONObject("data").getJSONArray("spuIdList") ;
                for (int i = 0; i < spuArray.size(); i++) {
                    String detailParam = "" ;
                    System.out.println(i);
                    if ((i + 49) > spuArray.size()) {
                        detailParam = JSONUtil.toJsonString(spuArray.subList(i, spuArray.size()));
                    } else {
                        detailParam = JSONUtil.toJsonString(spuArray.subList(i, i + 49));
                    }
                    detailParam = detailParam.replace("[", "").replace("]", "").replace("\"", "") ;
                    OperaResponse spuDetailRes = aoyiClientService.getSpuDetail(detailParam) ;
                    if (spuDetailRes.getCode() == 200) {
                        String spuDetailResString = JSON.toJSONString(spuDetailRes) ;
                        JSONObject spuDetailResJson = JSONObject.parseObject(spuDetailResString) ;
                        JSONArray spuDetailData = spuDetailResJson.getJSONArray("data") ;
                        for (int j = 0; j < spuDetailData.size(); j++) {
                            JSONObject jsonObject = spuDetailData.getJSONObject(j) ;
                            StarSpu spuBean = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<StarSpu>(){});
                            AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = new AoyiProdIndexWithBLOBs() ;
                            Date date = new Date();
                            aoyiProdIndexWithBLOBs.setCreatedAt(date);
                            aoyiProdIndexWithBLOBs.setUpdatedAt(date);
                            aoyiProdIndexWithBLOBs.setMpu(spuBean.getGoodsCode());
                            aoyiProdIndexWithBLOBs.setSkuid(spuBean.getSpuId());
                            aoyiProdIndexWithBLOBs.setMerchantId(4);
                            aoyiProdIndexWithBLOBs.setName(spuBean.getName());
                            aoyiProdIndexWithBLOBs.setImage(spuBean.getMainImgUrl());
                            aoyiProdIndexWithBLOBs.setState(spuBean.getStatus() + "");
                            aoyiProdIndexWithBLOBs.setIntroduction(spuBean.getDetailInfo());
                            aoyiProdIndexWithBLOBs.setCrossBorder(spuBean.getCrossBorder());
                            List<AoyiProdIndex> aoyiProdIndices =  productDao.findBySkuId(spuBean.getSpuId(), aoyiProdIndexWithBLOBs.getMerchantId()) ;
                            if (aoyiProdIndices == null || aoyiProdIndices.size() == 0) {
                                // insert spu
                                aoyiProdIndexMapper.insertSelective(aoyiProdIndexWithBLOBs) ;
                                // insert spu
//							starSpuMapper.insertSelective(spuBean) ;
                                JSONArray detailImgArray = jsonObject.getJSONArray("detailImgUrlList");
                                for (int k = 0; k < detailImgArray.size(); k++) {
                                    String detailImgJson = detailImgArray.getString(k) ;
                                    StarDetailImg starDetailImg = new StarDetailImg() ;
                                    starDetailImg.setImgUrl(detailImgJson);
                                    // set spu id
                                    starDetailImg.setSpuId(aoyiProdIndexWithBLOBs.getId());
                                    // isert detail img
                                    starDetailImgMapper.insertSelective(starDetailImg) ;
                                }
                                JSONArray propertyArray = jsonObject.getJSONArray("spuPropertyList") ;
                                for (int k = 0; k < propertyArray.size(); k++) {
                                    JSONObject propertyJson = propertyArray.getJSONObject(k) ;
                                    StarProperty starProperty  = JSON.parseObject(propertyJson.toJSONString(), new TypeReference<StarProperty>(){});
                                    // set spu id
                                    starProperty.setProductId(aoyiProdIndexWithBLOBs.getId());
                                    // set type 0
                                    starProperty.setType(0);
                                    // isert property
                                    starPropertyMapper.insertSelective(starProperty) ;
                                }
                                log.info("获取SPU信息，结果：{}",JSONUtil.toJsonString(spuBean));
                            }
                        }
                        i = i+ 49 ;
                    }
                }
                for (int i = 0; i < spuArray.size(); i++) {
                    log.info("获取SKU信息，入参：{}", spuArray.getString(i)) ;
                    OperaResponse skuDetailRes = aoyiClientService.getSkuDetail(spuArray.getString(i)) ;
                    if (skuDetailRes.getCode() == 200) {
                        String skuDetailResString = JSON.toJSONString(skuDetailRes) ;
                        JSONObject skuDetailResJson = JSONObject.parseObject(skuDetailResString) ;
                        JSONArray skuDetailData = skuDetailResJson.getJSONArray("data") ;
                        JSONObject jsonObject = skuDetailData.getJSONObject(0) ;
                        StarSku skuBean = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<StarSku>(){});
                        skuBean.setSpuId(spuArray.getString(i));
                        // insert spu
                        List<StarSku> starSkus = starSkuDao.selectBySpuIdAndCode(spuArray.getString(i), skuBean.getCode()) ;
                        if (starSkus == null || starSkus.size() == 0) {
                            starSkuMapper.insertSelective(skuBean) ;
                            JSONArray propertyArray = jsonObject.getJSONArray("skuPropertyList") ;
                            for (int j = 0; j < propertyArray.size(); j++) {
                                JSONObject propertyJson = propertyArray.getJSONObject(j) ;
                                StarProperty starProperty  = JSON.parseObject(propertyJson.toJSONString(), new TypeReference<StarProperty>(){});
                                // set spu id
                                starProperty.setProductId(skuBean.getId());
                                // set type 1
                                starProperty.setType(1);
                                // isert property
                                starPropertyMapper.insertSelective(starProperty) ;
                            }
                            log.info("获取SKU信息，入参：{}, 结果：{}",spuArray.getString(i), JSONUtil.toJsonString(skuBean));
                        }
                    }
                }
            }
        }
    }

    @Async
    public void executeAsyncStarProdPrice(AoyiClientService aoyiClientService, StarSkuDao starSkuDao) {
        List<StarSku> starSkus = starSkuDao.selectAll() ;
        List<String> codes = new ArrayList<>() ;
        String code = "" ;
        int i = 1 ;
        for (StarSku starSku: starSkus) {
            if (i%200 == 0 || i > starSkus.size()) {
                codes.add(code) ;
                code = "";
            }
            if (StringUtils.isEmpty(code)) {
                i = i + 1;
                code = starSku.getCode() ;
            } else {
                i = i + 1;
                code = code + "," + starSku.getCode() ;
            }
        }
        codes.forEach(c -> {
            OperaResponse priceResponse = aoyiClientService.findSkuSalePrice(c) ;
            String skuPriceResString = JSON.toJSONString(priceResponse) ;
            JSONObject skuDetailResJson = JSONObject.parseObject(skuPriceResString) ;
            JSONArray skuPriceData = skuDetailResJson.getJSONArray("data") ;
            for (int j = 0; j < skuPriceData.size(); j++) {
                JSONObject skuPriceJson = skuPriceData.getJSONObject(j) ;
                String channelPrice = skuPriceJson.getString("channelPrice") ;
                String retailPrice = skuPriceJson.getString("retailPrice") ;
                String skuCode = skuPriceJson.getString("code") ;
                BigDecimal bigDecimalC = new BigDecimal(channelPrice) ;
                int sprice = bigDecimalC.multiply(new BigDecimal("100")).intValue() ;
                BigDecimal bigDecimalR = new BigDecimal(retailPrice) ;
                int advisePrice = bigDecimalR.multiply(new BigDecimal("100")).intValue() ;
                StarSku starSku = new StarSku() ;
                starSku.setCode(skuCode);
                starSku.setSprice(sprice);
                starSku.setAdvisePrice(advisePrice);
                starSkuDao.updatePriceByCode(starSku);
            }
        });
    }
}
