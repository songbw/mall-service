package com.fengchao.product.aoyi.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.config.ProductConfig;
import com.fengchao.product.aoyi.constants.ProductStatusEnum;
import com.fengchao.product.aoyi.constants.StarSkuStatusEnum;
import com.fengchao.product.aoyi.dao.*;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import com.fengchao.product.aoyi.mapper.*;
import com.fengchao.product.aoyi.model.*;
import com.fengchao.product.aoyi.rpc.AoyiClientRpcService;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.AoyiItemDetailResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.AoyiSkuResDto;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author songbw
 * @date 2019/10/11 16:28
 */
@Slf4j
@Component
public class AsyncTask {

    @Async("asyncServiceExecutor")
    public void executeAsyncProductTask(ProductDao productDao, WebTarget webTarget, List<AoyiProdIndex> prodIndices) {
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        prodIndices.forEach(prodIndex -> {
            Response response1 = invocationBuilder.put(Entity.entity(prodIndex, MediaType.APPLICATION_JSON));
            OperaResult result = response1.readEntity(OperaResult.class);
            // TODO 成功则更新商品表，失败则打印日志
            if (result.getCode() == 200) {
                productDao.updateSyncAt(prodIndex.getId());
            } else {
                log.info("线程" + Thread.currentThread().getName() + " 执行异步任务：" + "同步商品" + prodIndex.getMpu() + "失败, 失败原因 {}", JSONUtil.toJsonString(result));
            }
        });
    }

    @Async("asyncServiceExecutor")
    public void executeAsyncCategoryTask(WebTarget webTarget, List<AoyiBaseCategory> categories) {
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        categories.forEach(category -> {
            Response response1 = invocationBuilder.put(Entity.entity(category, MediaType.APPLICATION_JSON));
            OperaResponse result = response1.readEntity(OperaResponse.class);
            if (result.getCode() != 200) {
                log.info("线程" + Thread.currentThread().getName() + " 执行异步任务：" + "同步类目" + category.getCategoryId() + "失败, 失败原因 {}", JSONUtil.toJsonString(result));
            }
        });
    }

    @Async("asyncServiceExecutor")
    public void executeAsyncBrandTask(WebTarget webTarget, List<AoyiBaseBrandX> brands) {
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        brands.forEach(brand -> {
            Response response1 = invocationBuilder.put(Entity.entity(brand, MediaType.APPLICATION_JSON));
            OperaResponse result = response1.readEntity(OperaResponse.class);
            if (result.getCode() != 200) {
                log.info("线程" + Thread.currentThread().getName() + " 执行异步任务：" + "同步品牌" + brand.getBrandId() + "失败, 失败原因 {}", JSONUtil.toJsonString(result));
            }
        });
    }

    @Async("asyncServiceExecutor")
    public void executeAsyncStarProd(AoyiClientService aoyiClientService, ProductDao productDao, AoyiProdIndexMapper aoyiProdIndexMapper, StarDetailImgMapper starDetailImgMapper, StarPropertyMapper starPropertyMapper, StarSkuMapper starSkuMapper, StarSkuDao starSkuDao) {
        QueryBean queryBean = new QueryBean();
        queryBean.setPageSize(100);
        for (int x = 0; x < 1000; x++) {
            queryBean.setPageNo(x + 1);
            //		queryBean.setStartTime("2018-07-05 16:43:35");
            //		queryBean.setEndTime("2019-07-06 16:43:35");
            OperaResponse spuIdsResponse = aoyiClientService.getSpuIdList(queryBean);
            if (spuIdsResponse.getCode() == 200) {
                String resJsonString = JSON.toJSONString(spuIdsResponse);
                JSONObject resJson = JSONObject.parseObject(resJsonString);
                JSONArray spuArray = resJson.getJSONObject("data").getJSONArray("spuIdList");
                for (int i = 0; i < spuArray.size(); i++) {
                    String detailParam = "";
                    if ((i + 29) > spuArray.size()) {
                        detailParam = JSONUtil.toJsonString(spuArray.subList(i, spuArray.size()));
                    } else {
                        detailParam = JSONUtil.toJsonString(spuArray.subList(i, i + 29));
                    }
                    detailParam = detailParam.replace("[", "").replace("]", "").replace("\"", "");
                    OperaResponse spuDetailRes = aoyiClientService.getSpuDetail(detailParam);
                    if (spuDetailRes.getCode() == 200) {
                        String spuDetailResString = JSON.toJSONString(spuDetailRes);
                        JSONObject spuDetailResJson = JSONObject.parseObject(spuDetailResString);
                        JSONArray spuDetailData = spuDetailResJson.getJSONArray("data");
                        for (int j = 0; j < spuDetailData.size(); j++) {
                            JSONObject jsonObject = spuDetailData.getJSONObject(j);
                            StarSpu spuBean = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<StarSpu>() {
                            });
                            AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = new AoyiProdIndexWithBLOBs();
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
                            aoyiProdIndexWithBLOBs.setType(2);
                            List<AoyiProdIndex> aoyiProdIndices = productDao.findBySkuId(spuBean.getSpuId(), aoyiProdIndexWithBLOBs.getMerchantId());
                            if (aoyiProdIndices == null || aoyiProdIndices.size() == 0) {
                                // insert spu
                                aoyiProdIndexMapper.insertSelective(aoyiProdIndexWithBLOBs);
                                // insert spu
//							starSpuMapper.insertSelective(spuBean) ;
                                JSONArray detailImgArray = jsonObject.getJSONArray("detailImgUrlList");
                                for (int k = 0; k < detailImgArray.size(); k++) {
                                    String detailImgJson = detailImgArray.getString(k);
                                    StarDetailImg starDetailImg = new StarDetailImg();
                                    starDetailImg.setImgUrl(detailImgJson);
                                    // set spu id
                                    starDetailImg.setSpuId(aoyiProdIndexWithBLOBs.getId());
                                    // isert detail img
                                    starDetailImgMapper.insertSelective(starDetailImg);
                                }
                                JSONArray propertyArray = jsonObject.getJSONArray("spuPropertyList");
                                for (int k = 0; k < propertyArray.size(); k++) {
                                    JSONObject propertyJson = propertyArray.getJSONObject(k);
                                    StarProperty starProperty = JSON.parseObject(propertyJson.toJSONString(), new TypeReference<StarProperty>() {
                                    });
                                    // set spu id
                                    starProperty.setProductId(aoyiProdIndexWithBLOBs.getId());
                                    // set type 0
                                    starProperty.setType(0);
                                    // isert property
                                    starPropertyMapper.insertSelective(starProperty);
                                }
                                log.info("获取SPU信息，结果：{}", JSONUtil.toJsonString(spuBean));
                            }
                        }
                        i = i + 49;
                    }
                }
                for (int i = 0; i < spuArray.size(); i++) {
                    log.info("获取SKU信息，入参：{}", spuArray.getString(i));
                    OperaResponse skuDetailRes = aoyiClientService.getSkuDetail(spuArray.getString(i));
                    if (skuDetailRes.getCode() == 200) {
                        String skuDetailResString = JSON.toJSONString(skuDetailRes);
                        JSONObject skuDetailResJson = JSONObject.parseObject(skuDetailResString);
                        JSONArray skuDetailData = skuDetailResJson.getJSONArray("data");
                        for (int h = 0; h < skuDetailData.size(); h++) {
                            JSONObject jsonObject = skuDetailData.getJSONObject(h);
                            StarSku skuBean = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<StarSku>() {
                            });
                            skuBean.setSpuId(spuArray.getString(i));
                            // insert spu
                            List<StarSku> starSkus = starSkuDao.selectBySpuIdAndCode(spuArray.getString(i), skuBean.getCode());
                            if (starSkus == null || starSkus.size() == 0) {
                                starSkuMapper.insertSelective(skuBean);
                                JSONArray propertyArray = jsonObject.getJSONArray("skuPropertyList");
                                for (int j = 0; j < propertyArray.size(); j++) {
                                    JSONObject propertyJson = propertyArray.getJSONObject(j);
                                    StarProperty starProperty = JSON.parseObject(propertyJson.toJSONString(), new TypeReference<StarProperty>() {
                                    });
                                    // set spu id
                                    starProperty.setProductId(skuBean.getId());
                                    // set type 1
                                    starProperty.setType(1);
                                    // isert property
                                    starPropertyMapper.insertSelective(starProperty);
                                }
                                log.info("获取SKU信息，入参：{}, 结果：{}", spuArray.getString(i), JSONUtil.toJsonString(skuBean));
                            }
                        }
                    }
                }
            }
        }
    }

    @Async("asyncServiceExecutor")
    public void executeAsyncStarProdPrice(AoyiClientService aoyiClientService, StarSkuDao starSkuDao, ProductDao productDao) {
        List<StarSku> starSkus = starSkuDao.selectAll();
        List<String> codes = new ArrayList<>();
        String code = "";
        int i = 1;
        for (StarSku starSku : starSkus) {
            if (i % 200 == 0) {
                codes.add(code);
                code = "";
//                i = 1;
            }
            if (StringUtils.isEmpty(code)) {
                i = i + 1;
                code = starSku.getCode();
            } else {
                i = i + 1;
                code = code + "," + starSku.getCode();
            }
            if (i >= starSkus.size()) {
                codes.add(code);
//                i = 1;
            }
        }
        log.info("test star codes count: {}", codes.size());
        List<String> spus = new ArrayList<>();
        codes.forEach(c -> {
            OperaResponse priceResponse = aoyiClientService.findSkuSalePrice(c);
            String skuPriceResString = JSON.toJSONString(priceResponse);
            JSONObject skuDetailResJson = JSONObject.parseObject(skuPriceResString);
            JSONArray skuPriceData = skuDetailResJson.getJSONArray("data");
            for (int j = 0; j < skuPriceData.size(); j++) {
                JSONObject skuPriceJson = skuPriceData.getJSONObject(j);
                String channelPrice = skuPriceJson.getString("channelPrice");
                String retailPrice = skuPriceJson.getString("retailPrice");
                String skuCode = skuPriceJson.getString("code");
                Integer status = skuPriceJson.getInteger("status");
                if (StarSkuStatusEnum.PUT_ON.getValue() == status) {
                    BigDecimal bigDecimalC = new BigDecimal(channelPrice);
                    int sprice = bigDecimalC.multiply(new BigDecimal("100")).intValue();

//                    BigDecimal bigDecimalPrice = new BigDecimal(0) ;
//                    bigDecimalPrice = bigDecimalC.divide(new BigDecimal(0.9), 2, BigDecimal.ROUND_HALF_UP) ;
//                    int price = bigDecimalPrice.multiply(new BigDecimal("100")).intValue() ;

                    BigDecimal bigDecimalR = new BigDecimal(retailPrice);
                    int advisePrice = bigDecimalR.multiply(new BigDecimal("100")).intValue();
                    StarSku starSku = new StarSku();
                    starSku.setCode(skuCode);
                    starSku.setSprice(sprice);
                    starSku.setAdvisePrice(advisePrice);


//                    if (price > advisePrice) {
//                        starSku.setPrice(price);
//                    } else {
//                        starSku.setPrice(advisePrice);
//                    }
                    // 销售价格使用建议销售价
                    starSku.setPrice(advisePrice);

                    starSku.setStatus(ProductStatusEnum.PUT_OFF.getValue());
                    List<StarSku> starSkus1 = starSkuDao.selectByCode(skuCode);
                    if (!starSkus1.isEmpty()) {
                        StarSku checkSku = starSkus1.get(0);
                        // 销售价格哪个小就用那个
                        if (checkSku.getPrice() < starSku.getPrice() && checkSku.getPrice() > starSku.getSprice()) {
                            starSku.setPrice(checkSku.getPrice());
                        }
                        starSkuDao.updatePriceByCode(starSku);
                        String spuId = checkSku.getSpuId();
                        log.info("spu id is : {}", spuId);
                        if (!spus.contains(spuId)) {
                            spus.add(starSkus1.get(0).getSpuId());
                            // 更新spu表价格
                            PriceBean priceBean = new PriceBean();
                            priceBean.setSkuId(starSkus1.get(0).getSpuId());
                            priceBean.setMerchantId(4);
                            priceBean.setPrice(new BigDecimal(starSku.getPrice()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());
                            priceBean.setSPrice(channelPrice);
                            productDao.updatePrice(priceBean);
                        }
                    }
                } else {
                    StarSku starSku = new StarSku();
                    starSku.setCode(skuCode);
                    starSku.setStatus(ProductStatusEnum.PUT_ON.getValue());
                    starSkuDao.updateStatusByCode(starSku);
                }
            }
        });
    }

    @Async("asyncServiceExecutor")
    public void executeAsyncStarCategory(AoyiClientService aoyiClientService, StarCategoryMapper starCategoryMapper) {
        OperaResponse response = aoyiClientService.findProdCategory(null);
        if (response.getCode() == 200) {
            String data = JSONUtil.toJsonString(response.getData());
            JSONObject categorys = JSONObject.parseObject(data);
            JSONArray categoryArray = categorys.getJSONArray("categoryList");
            for (int j = 0; j < categoryArray.size(); j++) {
                JSONObject jsonObject = categoryArray.getJSONObject(j);
                StarCategory starCategory = new StarCategory();
                starCategory.setStarId(jsonObject.getInteger("cateId"));
                starCategory.setLevel(jsonObject.getInteger("level"));
                starCategory.setName(jsonObject.getString("cateName"));
                starCategory.setParentId(jsonObject.getInteger("parentId"));
                starCategoryMapper.insert(starCategory);
                subStarCategory(aoyiClientService, starCategory.getStarId(), starCategoryMapper);
            }
        }
    }

    private void subStarCategory(AoyiClientService aoyiClientService, int categoryId, StarCategoryMapper starCategoryMapper) {
        OperaResponse response = aoyiClientService.findProdCategory(categoryId + "");
        if (response.getCode() == 200) {
            String data = JSONUtil.toJsonString(response.getData());
            JSONObject categorys = JSONObject.parseObject(data);
            JSONArray categoryArray = categorys.getJSONArray("categoryList");
            for (int j = 0; j < categoryArray.size(); j++) {
                JSONObject jsonObject = categoryArray.getJSONObject(j);
                StarCategory starCategory = new StarCategory();
                starCategory.setStarId(jsonObject.getInteger("cateId"));
                starCategory.setLevel(jsonObject.getInteger("level"));
                starCategory.setName(jsonObject.getString("cateName"));
                starCategory.setParentId(jsonObject.getInteger("parentId"));
                starCategoryMapper.insert(starCategory);
                subStarCategory(aoyiClientService, starCategory.getStarId(), starCategoryMapper);
            }
        }
    }

    @Async("asyncServiceExecutor")
    public void executeAsyncWphItemDetail(AoyiClientRpcService aoyiClientRpcService, AoyiBaseBrandDao aoyiBaseBrandDao, ProductConfig productConfig, ProductDao productDao, StarSkuDao starSkuDao, StarPropertyDao starPropertyDao, AyFcImagesDao ayFcImagesDao) {
        ProductQueryBean productQueryBean = new ProductQueryBean();
        productQueryBean.setPageSize(200);
        productQueryBean.setMerchantId(2);
        productQueryBean.setSkuProfix("3");
        productQueryBean.setPageNo(1);
        PageInfo<AoyiProdIndexWithBLOBs> pageInfo = productDao.selectNameIsNullPageable(productQueryBean);
        for (int i = 1; i < pageInfo.getPages(); i++) {
            productQueryBean.setPageNo(i);
            pageInfo = productDao.selectNameIsNullPageable(productQueryBean);

            List<AoyiProdIndexWithBLOBs> aoyiProdIndexList = pageInfo.getList();
            int pageNum = i;
            log.info("同步商品详情 获取itermIdList 第{}页{}条 >>>>> 数据:{}",
                    pageNum, aoyiProdIndexList.size(), JSONUtil.toJsonString(productQueryBean));

            if (CollectionUtils.isEmpty(aoyiProdIndexList)) {
                log.error("同步商品详情 获取itermId列表 第{}页 无数据 任务停止!");
                return;
            }

            // 2. 遍历itemId列表，查询详情信息
            int itemIdIndex = 0; // 该for需要的itemId的计数
            for (AoyiProdIndexWithBLOBs aoyiProdIndex : aoyiProdIndexList) { // 遍历itemId列表
                String itemId = aoyiProdIndex.getSkuid();
                itemIdIndex++;

                // 3. rpc 获取商品详情
                AoyiItemDetailResDto aoyiItemDetailResDto = null;
                try {
                    aoyiItemDetailResDto = aoyiClientRpcService.weipinhuiQueryItemDetial(itemId);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("aoyi rpc 出错！");
                }
                log.debug("同步商品详情 第{}页 第{}个itemId:{} rpc获取商品详情: {}",
                        pageNum, itemIdIndex, itemId, JSONUtil.toJsonString(aoyiItemDetailResDto));

                if (aoyiItemDetailResDto == null) {
                    log.warn("同步商品详情 第{}页 第{}个itemId:{} rpc获取商品详情为空 继续...", pageNum, itemIdIndex, itemId);
                    continue;
                }

                // 4. 入库处理 分为两步 4.1 更新 spu， 4.2 插入 sku
                // 4.1 更新spu
                // 获取brand名称
                AoyiBaseBrand aoyiBaseBrand =
                        aoyiBaseBrandDao.selectByBrandId(Integer.valueOf(aoyiItemDetailResDto.getBrandId()));

                aoyiProdIndex.setName(aoyiItemDetailResDto.getItemTitle()); // 商品名称spu名称
                // aoyiProdIndex.setState(aoyiItemDetailResDto.getCanSell() == "true" ? "1" : "0"); // 是否出售 "false" - 是否上架 0：下架；1：上架
                aoyiProdIndex.setBrand(aoyiBaseBrand == null ? "" : aoyiBaseBrand.getBrandName());
                aoyiProdIndex.setBrandId(org.apache.commons.lang.StringUtils.isNotBlank(aoyiItemDetailResDto.getBrandId())
                        ? Integer.valueOf(aoyiItemDetailResDto.getBrandId()) : 0); // 品牌 id
                aoyiProdIndex.setCategory(aoyiItemDetailResDto.getCategoryId()); // 类目三级 id "50023439"

                // 处理spu主图
                List<String> imageUrlList = JSONUtil.parse(aoyiItemDetailResDto.getItemImage(), List.class);
                List<AyFcImages> mainSpuImageList =
                        handleAyFcImages(imageUrlList, aoyiItemDetailResDto.getCategoryId(), itemId, "WEIPINHUI_ZT", productConfig);

                String imagesUrl =
                        String.join(":", mainSpuImageList.stream().map(m -> m.getFcImage()).collect(Collectors.toList()));
                aoyiProdIndex.setImagesUrl(imagesUrl); // 商品主图 [http://pic.aoyi365.com/aoyi_tmall/50000550/ZT/1.jpg,http://pic.aoyi365.com/aoyi_tmall/50 000550/ZT/2.jpg,http://pic.aoyi365.com/aoyi_tmall/50000550/ZT/3.jpg,http://pic.aoyi365.co m/aoyi_tmall/50000550/ZT/4.jpg,]", // 商品主图

                // 处理spu详情图
                List<String> detailImageUrlList = JSONUtil.parse(aoyiItemDetailResDto.getItemDetailImage(), List.class);
                List<AyFcImages> detailSpuImageList =
                        handleAyFcImages(detailImageUrlList, aoyiItemDetailResDto.getCategoryId(), itemId, "WEIPINHUI_XQ", productConfig);
                String introductionUrl =
                        String.join(":", detailSpuImageList.stream().map(d -> d.getFcImage()).collect(Collectors.toList()));
                aoyiProdIndex.setIntroductionUrl(introductionUrl); // 商品详情图 [http://pic.aoyi365

                // x.. 获取sku list
                List<AoyiSkuResDto> aoyiSkuResDtoList = aoyiItemDetailResDto.getAoyiSkusResponses(); // rpc获取到的sku集合
                if (CollectionUtils.isEmpty(aoyiSkuResDtoList)) {
                    log.warn("同步商品详情 第{}页 第{}个itemId:{} 其商品详情为空 继续......", pageNum, itemIdIndex, itemId);
                    continue;
                }
                // 处理销售价格
                aoyiProdIndex.setPrice(aoyiSkuResDtoList.get(0).getSellPrice());
                aoyiProdIndex.setSprice(aoyiSkuResDtoList.get(0).getPriceCent());

                // x..执行更新spu
                log.debug("同步商品详情 第{}页 第{}个itemId:{} 更新spu 数据库入参:{}",
                        pageNum, itemIdIndex, itemId, JSONUtil.toJsonStringWithoutNull(aoyiProdIndex));
                AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = convertToAoyiProdIndexWithBLOBs(aoyiProdIndex);
                productDao.updateByPrimaryKeySelective(aoyiProdIndexWithBLOBs);

                // 4.2 新增sku
                // 4.2.1 首先判断是否已存在sku
                // skuId集合
                List<String> skuIdList =
                        aoyiSkuResDtoList.stream().map(a -> a.getSkuId()).collect(Collectors.toList());

                // 数据库查询
                List<StarSku> exsitStarSkuList = starSkuDao.selectBySkuIdList(skuIdList);
                List<String> exsitSkuIdList =
                        exsitStarSkuList.stream().map(e -> e.getSkuId()).collect(Collectors.toList());

                // 4.2.2 过滤掉已经存在sku
                List<StarSku> insertStarSkuList = new ArrayList<>(); // 过滤掉已存在的sku之后，剩下的需要插入的sku信息
                List<StarPropertyBean> candidateStarPropertyList = new ArrayList<>(); // 待插入的商品规格列表信息
                List<AyFcImages> skuImageList = new ArrayList<>(); // 待插入ay_fc_images表的信息
                // 遍历需要插入的数据，根据sku判断其中有无已经存在的数据
                for (AoyiSkuResDto aoyiSkuResDto : aoyiSkuResDtoList) { // 遍历需要插入的数据，根据sku判断其中有无已经存在的数据
                    if (!exsitSkuIdList.contains(aoyiSkuResDto.getSkuId())) { // 如果不存在
                        // a.组装sku
                        StarSku starSku = new StarSku();
                        // String code;
                        // purchaseQty;

                        // c. 组装该sku的图片信息
                        List<AyFcImages> _ayFcImagesList = handleAyFcImages(Arrays.asList(aoyiSkuResDto.getSkuImageUrl()),
                                aoyiItemDetailResDto.getCategoryId(), itemId, "WEIPINHUI_XQ", productConfig);
                        skuImageList.addAll(_ayFcImagesList);
                        //                            starSku.setGoodsLogo(aoyiSkuResDto.getSkuImageUrl());
                        starSku.setGoodsLogo(productConfig.getFengchaoImagePrefix() + _ayFcImagesList.get(0).getFcImage());
                        starSku.setSkuId(aoyiSkuResDto.getSkuId());
                        starSku.setCode(aoyiSkuResDto.getSkuId());
                        starSku.setStatus("false".equals(aoyiSkuResDto.getCanSell()) ? 0 : 1);
                        starSku.setSpuId(itemId);
                        starSku.setAdvisePrice(PriceUtil.convertYuanToFen(aoyiSkuResDto.getSellPrice())); // 建议销售价格 单位分
                        starSku.setSprice(PriceUtil.convertYuanToFen(aoyiSkuResDto.getPriceCent())); // 采购价格 单位分
                        starSku.setPrice(PriceUtil.convertYuanToFen(aoyiSkuResDto.getSellPrice())); // 实际销售价格 单位分
                        starSku.setMerchantCode("3");

                        insertStarSkuList.add(starSku); //

                        // b.组装该sku的商品规格
                        List<StarPropertyBean> _starPropertyList = assembleProdSepc(aoyiSkuResDto.getSkuId(),
                                aoyiSkuResDto.getSepca(), aoyiSkuResDto.getSepcb(), aoyiSkuResDto.getSepcc());

                        if (CollectionUtils.isNotEmpty(_starPropertyList)) {
                            candidateStarPropertyList.addAll(_starPropertyList);
                        }
                        //

                    }
                }

                // 4.3 批量插入 sku 和 sku的商品规格
                // 4.3.1 批量插入 sku
                log.debug("同步商品详情 第{}页 第{}个itemId:{}, 插入sku列表:{}个 数据:{}",
                        pageNum, itemIdIndex, itemId, insertStarSkuList.size(), JSONUtil.toJsonStringWithoutNull(insertStarSkuList));
                if (CollectionUtils.isNotEmpty(insertStarSkuList)) {
                    starSkuDao.batchInsert(insertStarSkuList);
                    //                totalSkuCount = totalSkuCount + insertStarSkuList.size();
                }

                // x..  这里在获取一下刚才批量插入的sku数据, 主要是为了获取新插入的id，然后作为sku_property的属性
                Map<String, StarSku> starSkuMap = new HashMap<>(); // key: skuId, value: starSku;
                if (CollectionUtils.isNotEmpty(insertStarSkuList)) {
                    List<String> _skuIdList = insertStarSkuList.stream().map(j -> j.getSkuId()).collect(Collectors.toList());
                    List<StarSku> _starSkuList = starSkuDao.selectBySkuIdList(_skuIdList);

                    starSkuMap = _starSkuList.stream().collect(Collectors.toMap(_s -> _s.getSkuId(), _s -> _s));
                }

                // 4.3.2 批量插入 sku的商品规格
                log.debug("同步商品详情 第{}页 第{}个itemId:{}, 待处理的proerty列表:{}个 数据:{}",
                        pageNum, itemIdIndex, itemId, candidateStarPropertyList.size(), JSONUtil.toJsonStringWithoutNull(candidateStarPropertyList));

                if (CollectionUtils.isNotEmpty(candidateStarPropertyList)) {
                    // 首先查询数据库是否已经存在
                    List<Integer> candidateProductionIdList = // 待插入的productionid集合
                            candidateStarPropertyList.stream().map(s -> s.getProductId()).collect(Collectors.toList());

                    // 已存在的数据
                    List<StarProperty> exsitStarPropertyList = starPropertyDao.selectByProductIds(candidateProductionIdList);
                    List<Integer> exsitPropertyIdList = exsitStarPropertyList.stream().map(e -> e.getProductId()).collect(Collectors.toList());

                    // 需要出入的数据
                    List<StarProperty> insertStarPropertyList = new ArrayList<>();

                    // 过滤
                    for (StarPropertyBean candidateStarProperty : candidateStarPropertyList) {
                        if (!exsitPropertyIdList.contains(candidateStarProperty.getProductId())) {
                            // !!xx  将productId修改为startSku中的id
                            StarSku _starSku = starSkuMap.get(candidateStarProperty.getWphSkuId());
                            if (_starSku != null) {
                                candidateStarProperty.setProductId(_starSku.getId());
                            }

                            insertStarPropertyList.add(candidateStarProperty);
                        }
                    }

                    log.debug("同步商品详情 第{}页 第{}个itemId:{}, 插入的proerty列表:{}个 数据:{}",
                            pageNum, itemIdIndex, itemId, insertStarPropertyList.size(), JSONUtil.toJsonStringWithoutNull(insertStarPropertyList));
                    // 执行批量插入商品规格
                    if (CollectionUtils.isNotEmpty(insertStarPropertyList)) {
                        starPropertyDao.batchInsert(insertStarPropertyList);
                    }
                    //                totalPropertyCount = totalPropertyCount + insertStarPropertyList.size();
                }

                // 5. 处理图片信息: mainSpuImageList detailSpuImageList skuImageList
                mainSpuImageList.addAll(detailSpuImageList);
                mainSpuImageList.addAll(skuImageList);

                log.debug("同步商品详情 第{}页 第{}个itemId:{} 插入图片数据:{}条 数据:{}",
                        pageNum, itemIdIndex, itemId, mainSpuImageList.size(), JSONUtil.toJsonStringWithoutNull(mainSpuImageList));
                if (CollectionUtils.isNotEmpty(mainSpuImageList)) {
                    ayFcImagesDao.batchInsert(mainSpuImageList);
                }
            }// end for
        }

    }

    @Async("asyncServiceExecutor")
    public Future<String> executeAsyncWphItemDetailForSub(List<String> itemList, AoyiClientRpcService aoyiClientRpcService, AoyiBaseBrandDao aoyiBaseBrandDao, ProductConfig productConfig, ProductDao productDao, StarSkuDao starSkuDao, StarPropertyDao starPropertyDao, AyFcImagesDao ayFcImagesDao, AoyiProdIndexMapper aoyiProdIndexMapper) {

        //声明future对象
        Future<String> result = new AsyncResult<String>("");

        log.debug("同步商品详情 获取itermIdList {}条 >>>>> 数据:",
                itemList.size());

        if (CollectionUtils.isEmpty(itemList)) {
            result = new AsyncResult <String>("fail,time=" + System.currentTimeMillis() + ",thread id=" + Thread.currentThread().getName() + ",itemList is null");
        }

        // 2. 遍历itemId列表，查询详情信息
        int itemIdIndex = 0; // 该for需要的itemId的计数
        for (String itemId : itemList) {
            log.info("同步商品详情 获取itermId is {}条 >>>>>",
                    itemId);
            AoyiProdIndexWithBLOBs aoyiProdIndex = new AoyiProdIndexWithBLOBs() ;
            aoyiProdIndex.setSkuid(itemId);
            aoyiProdIndex.setMpu(itemId);
            aoyiProdIndex.setMerchantId(2);
            aoyiProdIndex.setType(2);
            aoyiProdIndex.setMerchantCode(itemId.substring(0,2));
            aoyiProdIndex.setState(String.valueOf(ProductStatusEnum.PUT_ON.getValue())); // 下架状态
            itemIdIndex++;

            // 3. rpc 获取商品详情
            AoyiItemDetailResDto aoyiItemDetailResDto = null;
            try {
                aoyiItemDetailResDto = aoyiClientRpcService.weipinhuiQueryItemDetial(itemId);
            } catch (Exception e) {
                log.info("aoyi rpc 出错！{}", e);
                result = new AsyncResult <String>("fail,time=" + System.currentTimeMillis() + ",thread id=" + Thread.currentThread().getName() + ",itemId "+ itemId+" aoyi rpc 出错！");
                continue;
            }
            log.debug("同步商品详情 第{}个itemId:{} rpc获取商品详情: {}",
                    itemIdIndex, itemId, JSONUtil.toJsonString(aoyiItemDetailResDto));

            if (aoyiItemDetailResDto == null) {
                log.info("1.同步商品详情 第{}个itemId:{} rpc获取商品详情为空 继续...", itemIdIndex, itemId);
                result = new AsyncResult <String>("fail,time=" + System.currentTimeMillis() + ",thread id=" + Thread.currentThread().getName() + ",itemId "+ itemId+" aoyi rpc 出错！");
                continue;
            }

            // 4. 入库处理 分为两步 4.1 更新 spu， 4.2 插入 sku
            // 4.1 更新spu
            // 获取brand名称
            String brandIdStr = aoyiItemDetailResDto.getBrandId() ;
            int brandId = Integer.valueOf(brandIdStr) ;
            AoyiBaseBrand aoyiBaseBrand = new AoyiBaseBrand() ;
            aoyiBaseBrand = aoyiBaseBrandDao.selectByBrandId(brandId);

            aoyiProdIndex.setName(aoyiItemDetailResDto.getItemTitle()); // 商品名称spu名称
            // aoyiProdIndex.setState(aoyiItemDetailResDto.getCanSell() == "true" ? "1" : "0"); // 是否出售 "false" - 是否上架 0：下架；1：上架
            aoyiProdIndex.setBrand(aoyiBaseBrand == null ? "" : aoyiBaseBrand.getBrandName());
            aoyiProdIndex.setBrandId(org.apache.commons.lang.StringUtils.isNotBlank(aoyiItemDetailResDto.getBrandId())
                    ? Integer.valueOf(aoyiItemDetailResDto.getBrandId()) : 0); // 品牌 id
            aoyiProdIndex.setCategory(aoyiItemDetailResDto.getCategoryId()); // 类目三级 id "50023439"

            // 处理spu主图
            List<String> imageUrlList = JSONUtil.parse(aoyiItemDetailResDto.getItemImage(), List.class);
            List<AyFcImages> mainSpuImageList =
                    handleAyFcImages(imageUrlList, aoyiItemDetailResDto.getCategoryId(), itemId, "WEIPINHUI_ZT", productConfig);

            String imagesUrl =
                    String.join(":", mainSpuImageList.stream().map(m -> m.getFcImage()).collect(Collectors.toList()));
            aoyiProdIndex.setImagesUrl(imagesUrl); // 商品主图 [http://pic.aoyi365.com/aoyi_tmall/50000550/ZT/1.jpg,http://pic.aoyi365.com/aoyi_tmall/50 000550/ZT/2.jpg,http://pic.aoyi365.com/aoyi_tmall/50000550/ZT/3.jpg,http://pic.aoyi365.co m/aoyi_tmall/50000550/ZT/4.jpg,]", // 商品主图

            // 处理spu详情图
            List<String> detailImageUrlList = JSONUtil.parse(aoyiItemDetailResDto.getItemDetailImage(), List.class);
            List<AyFcImages> detailSpuImageList =
                    handleAyFcImages(detailImageUrlList, aoyiItemDetailResDto.getCategoryId(), itemId, "WEIPINHUI_XQ", productConfig);
            String introductionUrl =
                    String.join(":", detailSpuImageList.stream().map(d -> d.getFcImage()).collect(Collectors.toList()));
            aoyiProdIndex.setIntroductionUrl(introductionUrl); // 商品详情图 [http://pic.aoyi365

            // x.. 获取sku list
            List<AoyiSkuResDto> aoyiSkuResDtoList = aoyiItemDetailResDto.getAoyiSkusResponses(); // rpc获取到的sku集合
            if (CollectionUtils.isEmpty(aoyiSkuResDtoList)) {
                log.info("2.同步商品详情 第{}个itemId:{} 其商品详情为空 继续......", itemIdIndex, itemId);
                result = new AsyncResult <String>("fail,time=" + System.currentTimeMillis() + ",thread id=" + Thread.currentThread().getName() + ",itemId "+ itemId+" aoyi rpc 出错！");
                continue;
            }
            // 处理销售价格
            aoyiProdIndex.setPrice(aoyiSkuResDtoList.get(0).getSellPrice());
            aoyiProdIndex.setSprice(aoyiSkuResDtoList.get(0).getPriceCent());

            // x..执行更新spu
            log.info("同步商品详情 第{}个itemId:{} 更新spu 数据库入参:{}",
                    itemIdIndex, itemId, JSONUtil.toJsonStringWithoutNull(aoyiProdIndex));
//            AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = convertToAoyiProdIndexWithBLOBs(aoyiProdIndex);
//                productDao.updateByPrimaryKeySelective(aoyiProdIndexWithBLOBs);
            aoyiProdIndexMapper.insertSelective(aoyiProdIndex) ;


            // 4.2 新增sku
            // 4.2.1 首先判断是否已存在sku
            // skuId集合
            List<String> skuIdList =
                    aoyiSkuResDtoList.stream().map(a -> a.getSkuId()).collect(Collectors.toList());

            // 数据库查询
            List<StarSku> exsitStarSkuList = starSkuDao.selectBySkuIdList(skuIdList);
            List<String> exsitSkuIdList =
                    exsitStarSkuList.stream().map(e -> e.getSkuId()).collect(Collectors.toList());

            // 4.2.2 过滤掉已经存在sku
            List<StarSku> insertStarSkuList = new ArrayList<>(); // 过滤掉已存在的sku之后，剩下的需要插入的sku信息
            List<StarPropertyBean> candidateStarPropertyList = new ArrayList<>(); // 待插入的商品规格列表信息
            List<AyFcImages> skuImageList = new ArrayList<>(); // 待插入ay_fc_images表的信息
            // 遍历需要插入的数据，根据sku判断其中有无已经存在的数据
            for (AoyiSkuResDto aoyiSkuResDto : aoyiSkuResDtoList) { // 遍历需要插入的数据，根据sku判断其中有无已经存在的数据
                if (!exsitSkuIdList.contains(aoyiSkuResDto.getSkuId())) { // 如果不存在
                    // a.组装sku
                    StarSku starSku = new StarSku();
                    // String code;
                    // purchaseQty;

                    // c. 组装该sku的图片信息
                    List<AyFcImages> _ayFcImagesList = handleAyFcImages(Arrays.asList(aoyiSkuResDto.getSkuImageUrl()),
                            aoyiItemDetailResDto.getCategoryId(), itemId, "WEIPINHUI_XQ", productConfig);
                    skuImageList.addAll(_ayFcImagesList);
                    //                            starSku.setGoodsLogo(aoyiSkuResDto.getSkuImageUrl());
                    starSku.setGoodsLogo(productConfig.getFengchaoImagePrefix() + _ayFcImagesList.get(0).getFcImage());
                    starSku.setSkuId(aoyiSkuResDto.getSkuId());
                    starSku.setCode(aoyiSkuResDto.getSkuId());
                    starSku.setStatus("false".equals(aoyiSkuResDto.getCanSell()) ? 0 : 1);
                    starSku.setSpuId(itemId);
                    starSku.setAdvisePrice(PriceUtil.convertYuanToFen(aoyiSkuResDto.getSellPrice())); // 建议销售价格 单位分
                    starSku.setSprice(PriceUtil.convertYuanToFen(aoyiSkuResDto.getPriceCent())); // 采购价格 单位分
                    starSku.setPrice(PriceUtil.convertYuanToFen(aoyiSkuResDto.getSellPrice())); // 实际销售价格 单位分
                    starSku.setMerchantCode("3");

                    insertStarSkuList.add(starSku); //

                    // b.组装该sku的商品规格
                    List<StarPropertyBean> _starPropertyList = assembleProdSepc(aoyiSkuResDto.getSkuId(),
                            aoyiSkuResDto.getSepca(), aoyiSkuResDto.getSepcb(), aoyiSkuResDto.getSepcc());

                    if (CollectionUtils.isNotEmpty(_starPropertyList)) {
                        candidateStarPropertyList.addAll(_starPropertyList);
                    }
                    //

                }
            }

            // 4.3 批量插入 sku 和 sku的商品规格
            // 4.3.1 批量插入 sku
            log.debug("同步商品详情 第{}个itemId:{}, 插入sku列表:{}个 数据:{}",
                    itemIdIndex, itemId, insertStarSkuList.size(), JSONUtil.toJsonStringWithoutNull(insertStarSkuList));
            if (CollectionUtils.isNotEmpty(insertStarSkuList)) {
                starSkuDao.batchInsert(insertStarSkuList);
                //                totalSkuCount = totalSkuCount + insertStarSkuList.size();
            }

            // x..  这里在获取一下刚才批量插入的sku数据, 主要是为了获取新插入的id，然后作为sku_property的属性
            Map<String, StarSku> starSkuMap = new HashMap<>(); // key: skuId, value: starSku;
            if (CollectionUtils.isNotEmpty(insertStarSkuList)) {
                List<String> _skuIdList = insertStarSkuList.stream().map(j -> j.getSkuId()).collect(Collectors.toList());
                List<StarSku> _starSkuList = starSkuDao.selectBySkuIdList(_skuIdList);

                starSkuMap = _starSkuList.stream().collect(Collectors.toMap(_s -> _s.getSkuId(), _s -> _s));
            }

            // 4.3.2 批量插入 sku的商品规格
            log.debug("同步商品详情 第{}个itemId:{}, 待处理的proerty列表:{}个 数据:{}",
                    itemIdIndex, itemId, candidateStarPropertyList.size(), JSONUtil.toJsonStringWithoutNull(candidateStarPropertyList));

            if (CollectionUtils.isNotEmpty(candidateStarPropertyList)) {
                // 首先查询数据库是否已经存在
                List<Integer> candidateProductionIdList = // 待插入的productionid集合
                        candidateStarPropertyList.stream().map(s -> s.getProductId()).collect(Collectors.toList());

                // 已存在的数据
                List<StarProperty> exsitStarPropertyList = starPropertyDao.selectByProductIds(candidateProductionIdList);
                List<Integer> exsitPropertyIdList = exsitStarPropertyList.stream().map(e -> e.getProductId()).collect(Collectors.toList());

                // 需要出入的数据
                List<StarProperty> insertStarPropertyList = new ArrayList<>();

                // 过滤
                for (StarPropertyBean candidateStarProperty : candidateStarPropertyList) {
                    if (!exsitPropertyIdList.contains(candidateStarProperty.getProductId())) {
                        // !!xx  将productId修改为startSku中的id
                        StarSku _starSku = starSkuMap.get(candidateStarProperty.getWphSkuId());
                        if (_starSku != null) {
                            candidateStarProperty.setProductId(_starSku.getId());
                        }

                        insertStarPropertyList.add(candidateStarProperty);
                    }
                }

                log.debug("同步商品详情 第{}个itemId:{}, 插入的proerty列表:{}个 数据:{}",
                        itemIdIndex, itemId, insertStarPropertyList.size(), JSONUtil.toJsonStringWithoutNull(insertStarPropertyList));
                // 执行批量插入商品规格
                if (CollectionUtils.isNotEmpty(insertStarPropertyList)) {
                    starPropertyDao.batchInsert(insertStarPropertyList);
                }
                //                totalPropertyCount = totalPropertyCount + insertStarPropertyList.size();
            }

            // 5. 处理图片信息: mainSpuImageList detailSpuImageList skuImageList
            mainSpuImageList.addAll(detailSpuImageList);
            mainSpuImageList.addAll(skuImageList);

            log.debug("同步商品详情 第{}个itemId:{} 插入图片数据:{}条 数据:{}",
                    itemIdIndex, itemId, mainSpuImageList.size(), JSONUtil.toJsonStringWithoutNull(mainSpuImageList));
            if (CollectionUtils.isNotEmpty(mainSpuImageList)) {
                ayFcImagesDao.batchInsert(mainSpuImageList);
            }
        }// end for
        return result ;
    }

    /**
     * 处理图片地址
     *
     * @param urlList ["aoyi_vip/aoyi30010k/30008601/ZT/30014191/1.jpg","aoyi_vip/aoyi30010k/30008601/ZT/30014191/2.jpg","aoyi_vip/aoyi30010k/30008601/ZT/30014191/3.jpg"aoyi_vip/aoyi30010k/30008601/ZT/30014191/4.jpg","aoyi_vip/aoyi30010k/30008601/ZT/30014191/6.jpg"]
     * @return
     */
    private List<AyFcImages> handleAyFcImages(List<String> urlList, String categoryId, String spuId, String type, ProductConfig productConfig) {
        List<AyFcImages> ayFcImagesList = new ArrayList<>();

        for (String imageUrl : urlList) {
            String path = "/" + categoryId + "/" + spuId + "/";
            String name = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());

            AyFcImages ayFcImages = new AyFcImages();
            ayFcImages.setFcImage(path + type + "/" + name); //
            ayFcImages.setAyImage(productConfig.getWeipinhuiImagePrefix() + imageUrl);
            ayFcImages.setPath(path);
            ayFcImages.setType(type);
            ayFcImages.setCreatedAt(new Date());
            ayFcImages.setUpdatedAt(new Date());

            ayFcImagesList.add(ayFcImages);
        }

        return ayFcImagesList;
    }

    /**
     * @param aoyiProdIndex
     * @return
     */
    private AoyiProdIndexWithBLOBs convertToAoyiProdIndexWithBLOBs(AoyiProdIndexWithBLOBs aoyiProdIndex) {
        AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = new AoyiProdIndexWithBLOBs();

        aoyiProdIndexWithBLOBs.setId(aoyiProdIndex.getId());
        aoyiProdIndexWithBLOBs.setSkuid(aoyiProdIndex.getSkuid());
        aoyiProdIndexWithBLOBs.setBrand(aoyiProdIndex.getBrand());
        aoyiProdIndexWithBLOBs.setCategory(aoyiProdIndex.getCategory());
        aoyiProdIndexWithBLOBs.setImage(aoyiProdIndex.getImage());
        aoyiProdIndexWithBLOBs.setModel(aoyiProdIndex.getModel());
        aoyiProdIndexWithBLOBs.setName(aoyiProdIndex.getName());
        aoyiProdIndexWithBLOBs.setWeight(aoyiProdIndex.getWeight());
        aoyiProdIndexWithBLOBs.setUpc(aoyiProdIndex.getUpc());
        aoyiProdIndexWithBLOBs.setSaleunit(aoyiProdIndex.getSaleunit());
        aoyiProdIndexWithBLOBs.setState(aoyiProdIndex.getState());
        aoyiProdIndexWithBLOBs.setPrice(aoyiProdIndex.getPrice());
        aoyiProdIndexWithBLOBs.setIprice(aoyiProdIndex.getIprice());
        aoyiProdIndexWithBLOBs.setSprice(aoyiProdIndex.getSprice());
        aoyiProdIndexWithBLOBs.setImagesUrl(aoyiProdIndex.getImagesUrl());
        aoyiProdIndexWithBLOBs.setIntroductionUrl(aoyiProdIndex.getIntroductionUrl());
        aoyiProdIndexWithBLOBs.setMerchantId(aoyiProdIndex.getMerchantId());
        aoyiProdIndexWithBLOBs.setInventory(aoyiProdIndex.getInventory());
        aoyiProdIndexWithBLOBs.setBrandId(aoyiProdIndex.getBrandId());
        aoyiProdIndexWithBLOBs.setMpu(aoyiProdIndex.getMpu());
        aoyiProdIndexWithBLOBs.setType(aoyiProdIndex.getType());
        aoyiProdIndexWithBLOBs.setCompareUrl(aoyiProdIndex.getCompareUrl());
        aoyiProdIndexWithBLOBs.setSubTitle(aoyiProdIndex.getSubTitle());
        aoyiProdIndexWithBLOBs.setComparePrice(aoyiProdIndex.getComparePrice());
        aoyiProdIndexWithBLOBs.setTaxRate(aoyiProdIndex.getTaxRate());
        aoyiProdIndexWithBLOBs.setFloorPrice(aoyiProdIndex.getFloorPrice());
        aoyiProdIndexWithBLOBs.setCrossBorder(aoyiProdIndex.getCrossBorder());

        return aoyiProdIndexWithBLOBs;
    }

    /**
     * 组装sku的商品规格
     *
     * @param skuId
     * @param sepca
     * @param sepcb
     * @param sepcc
     * @return
     */
    private List<StarPropertyBean> assembleProdSepc(String skuId, String sepca, String sepcb, String sepcc) {
        List<StarPropertyBean> starPropertyList = new ArrayList<>();

        if (org.apache.commons.lang.StringUtils.isNotBlank(sepca)) {
            StarPropertyBean starProperty = new StarPropertyBean();
            starProperty.setType(1);
            starProperty.setName("sepca");
            starProperty.setWphSkuId(skuId);
            starProperty.setVal(sepca);

            starPropertyList.add(starProperty);
        }

        if (org.apache.commons.lang.StringUtils.isNotBlank(sepcb)) {
            StarPropertyBean starProperty = new StarPropertyBean();
            starProperty.setType(1);
            starProperty.setName("sepcb");
            starProperty.setWphSkuId(skuId);
            starProperty.setVal(sepcb);

            starPropertyList.add(starProperty);
        }

        if (org.apache.commons.lang.StringUtils.isNotBlank(sepcc)) {
            StarPropertyBean starProperty = new StarPropertyBean();
            starProperty.setType(1);
            starProperty.setName("sepcc");
            starProperty.setWphSkuId(skuId);
            starProperty.setVal(sepcc);

            starPropertyList.add(starProperty);
        }

        return starPropertyList;
    }

}
