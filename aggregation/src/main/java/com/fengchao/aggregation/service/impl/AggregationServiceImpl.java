package com.fengchao.aggregation.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.aggregation.bean.AggregationBean;
import com.fengchao.aggregation.bean.OperaResult;
import com.fengchao.aggregation.bean.PageBean;
import com.fengchao.aggregation.bean.QueryBean;
import com.fengchao.aggregation.exception.AggregationException;
import com.fengchao.aggregation.feign.EquityService;
import com.fengchao.aggregation.feign.ProdService;
import com.fengchao.aggregation.mapper.*;
import com.fengchao.aggregation.model.*;
import com.fengchao.aggregation.rpc.ProductRpcService;
import com.fengchao.aggregation.service.AggregationService;
import com.fengchao.aggregation.utils.CosUtil;
import com.fengchao.aggregation.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AggregationServiceImpl implements AggregationService {

    @Autowired
    private AggregationMapper mapper;
    @Autowired
    private AggregationMpuMapper mpuMapper;
    @Autowired
    private ProductRpcService productRpcService;
    @Autowired
    private EquityService equityService;

    @Override
    public PageBean findAggregation(QueryBean bean, Integer merchantId) throws AggregationException {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize",bean.getLimit());
        map.put("order",bean.getOrder());
        map.put("appId",bean.getAppId());
        if(merchantId != 0){
            map.put("merchantId",merchantId);
        }
        List<Aggregation> brands = new ArrayList<>();
        total = mapper.selectCount(map);
        if (total > 0) {
            brands = mapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, brands, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    @Override
    public int createAggregation(Aggregation bean) throws AggregationException {
        Aggregation aggregation = mapper.selectHomePageByAppId(bean.getAppId());
        if(aggregation != null && bean.getHomePage()){
            mapper.updateStatus(bean.getAppId());
        }
        return mapper.insertSelective(bean);
    }

    @Override
    public Aggregation findAggregationById(Integer id) throws AggregationException {
        Aggregation aggregation = mapper.selectByPrimaryKey(id);
        Aggregation aggregationByIdtest = convertContent(aggregation.getContent(), aggregation.getAppId());
//        JSONArray AggregationArray = JSONObject.parseArray(aggregation.getContent());
//        if(AggregationArray == null || AggregationArray.size() < 1 ){
//            return aggregation;
//        }
//        for (int i = 0; i < AggregationArray.size(); i++) {
//            int type = AggregationArray.getJSONObject(i).getInteger("type");
//            if (type == 3) {
//                JSONArray jsonArray = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
//                for (int j = 0; j < jsonArray.size(); j++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(j);
//                    String skuid = jsonArray.getJSONObject(j).getString("mpu");
//                    if(skuid != null && !skuid.equals("")){
//                        String value = redisDAO.getValue(skuid);
////                    AoyiProdIndex product = (AoyiProdIndex)net.sf.json.JSONObject.toBean(net.sf.json.JSONObject.fromObject(value), AoyiProdIndex.class);
//                        JSONObject product = JSONObject.parseObject(value);
//                        if(product != null){
//                            jsonObject.put("imagePath",product.getString("image"));
//                            jsonObject.put("intro",product.getString("brand") + " " + product.getString("name"));
//                        }
//                    }else{
//                        String skuid1 = jsonObject.getString("skuid");
//                        System.out.println(skuid1);
//                    }
//                }
//            }
//            if (type == 4) {
//                JSONArray lists = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
//                for (int j = 0; j < lists.size(); j++) {
//                    JSONArray skus = lists.getJSONObject(j).getJSONArray("skus");
//                    for (int l = 0; l < skus.size(); l++){
//                        JSONObject jsonObject = skus.getJSONObject(l);
//                        String skuid = jsonObject.getString("mpu");
//                        if(skuid != null && !skuid.equals("")){
////                            AoyiProdIndex product = (AoyiProdIndex)net.sf.json.JSONObject.toBean(net.sf.json.JSONObject.fromObject(value), AoyiProdIndex.class);
//                            String value = redisDAO.getValue(skuid);
//                            JSONObject product = JSONObject.parseObject(value);
//                            if(product != null){
//                                jsonObject.put("price",product.getString("price"));
//                                jsonObject.put("imagePath",product.getString("image"));
//                                jsonObject.put("intro",product.getString("brand") + " " + product.getString("name"));
//                            }
//                        }else{
//                            String skuid1 = jsonObject.getString("skuid");
//                            System.out.println(skuid1);
//                        }
//                    }
//                }
//            }
//        }
        aggregation.setContent(aggregationByIdtest.getContent());
        return aggregation;
    }

    public Aggregation convertContent(String content, String appId) throws AggregationException {
        List<String> mpus = new ArrayList<>();
        Aggregation aggregation = new Aggregation();
        JSONArray AggregationArray = JSONObject.parseArray(content);
        if(content == null || content.equals("") ){
            return aggregation;
        }
        for (int i = 0; i < AggregationArray.size(); i++) {
            int type = AggregationArray.getJSONObject(i).getInteger("type");
            if (type == 3 /* PromotionType */ || type == 10 /* HorizontalGoodType */ ) {
                JSONArray jsonArray = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
                for (int j = 0; j < jsonArray.size(); j++) {
                    String mpu = jsonArray.getJSONObject(j).getString("mpu");
                    if(mpu != null && !mpu.equals("")){
                        mpus.add(mpu);
                    }
                }
            } else if (type == 4  /* GoodsType */ || type == 9 /* PromotionListType */) {
                JSONArray jsonArray = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
                for (int j = 0; j < jsonArray.size(); j++) {
                    JSONArray array = jsonArray.getJSONObject(j).getJSONArray("skus");
                    for (int m = 0; m < array.size(); m++){
                        String mpu = array.getJSONObject(m).getString("mpu");
                        if(mpu != null && !mpu.equals("")){
                            mpus.add(mpu);
                        }
                    }
                }
            }
        }
        HashSet<String> hashSet = new HashSet<>(mpus);
        mpus.clear();
        mpus.addAll(hashSet);
        Map<String, AoyiProdIndex> aoyiProdMap = new HashMap();
        Map<String, PromotionMpu> promotionMap = new HashMap();
        if(!mpus.isEmpty()){
            List<AoyiProdIndex> aoyiProdIndices = productRpcService.findProductListByMpuIdList(mpus, appId, "");
            for(AoyiProdIndex prod: aoyiProdIndices){
                aoyiProdMap.put(prod.getMpu(), prod);
            }

            OperaResult onlinePromotion = equityService.findOnlinePromotion(appId);
            if (onlinePromotion.getCode() == 200) {
                Object object = onlinePromotion.getData().get("result");
                List<PromotionMpu> promotionMpus = JSONObject.parseArray(JSON.toJSONString(object), PromotionMpu.class);
                for(PromotionMpu mpu: promotionMpus){
                    promotionMap.put(mpu.getMpu(), mpu);
                }
            }
        }
        for (int i = 0; i < AggregationArray.size(); i++) {
            int type = AggregationArray.getJSONObject(i).getInteger("type");
            if (type == 3 || type == 10) {
                JSONArray jsonArray = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
                for (int j = 0; j < jsonArray.size(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    String mpu = jsonObject.getString("mpu");
                        if (mpu != null && !mpu.equals("")) {
                            AoyiProdIndex aoyiProdIndex = aoyiProdMap.get(mpu);
                            if(aoyiProdIndex != null){
                                String imageUrl = aoyiProdIndex.getImagesUrl();
                                if (imageUrl != null && (!"".equals(imageUrl))) {
                                    String image = "";
                                    if (imageUrl.indexOf("/") == 0) {
                                        image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                                    } else {
                                        image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                                    }
                                    aoyiProdIndex.setImage(image);
                                }
                                jsonObject.put("imagePath", aoyiProdIndex.getImage());
                                jsonObject.put("price", aoyiProdIndex.getPrice());
                                jsonObject.put("name", aoyiProdIndex.getName());
                                jsonObject.put("subTitle", aoyiProdIndex.getSubTitle());
                                PromotionMpu promotionMpu = promotionMap.get(mpu);
                                if (type == 10 && promotionMpu != null) {
                                    jsonObject.put("discount", promotionMpu.getDiscount());
                                }
                            }
                        }
                    }
                }
            if (type == 4 || type == 9) {
                JSONArray jsonArray = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
                for (int j = 0; j < jsonArray.size(); j++) {
                    JSONArray array = jsonArray.getJSONObject(j).getJSONArray("skus");
                    for (int m = 0; m < array.size(); m++) {
                        JSONObject jsonObject = array.getJSONObject(m);
                        String mpu = array.getJSONObject(m).getString("mpu");
                        if (mpu != null && !mpu.equals("")) {
                            AoyiProdIndex aoyiProdIndex = aoyiProdMap.get(mpu);
                            if(aoyiProdIndex != null){
                                String imageUrl = aoyiProdIndex.getImagesUrl();
                                if (imageUrl != null && (!"".equals(imageUrl))) {
                                    String image = "";
                                    if (imageUrl.indexOf("/") == 0) {
                                        image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                                    } else {
                                        image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                                    }
                                    aoyiProdIndex.setImage(image);
                                }
                                PromotionMpu promotionMpu = promotionMap.get(mpu);
                                jsonObject.put("price", aoyiProdIndex.getPrice());
                                jsonObject.put("imagePath", aoyiProdIndex.getImage());
                                jsonObject.put("name", aoyiProdIndex.getName());
                                jsonObject.put("subTitle", aoyiProdIndex.getSubTitle());
                                if (type == 4 && promotionMpu != null) {
                                    jsonObject.put("discount", promotionMpu.getDiscount());
                                }
                            }
                        }
                    }
                }
            }
        }
        aggregation.setContent(AggregationArray.toString());
        return aggregation;
    }

    @Override
    public int updateAggregation(Aggregation bean) throws AggregationException{
        Aggregation aggregation = mapper.selectByPrimaryKey(bean.getId());
        if(((bean.getHomePage() != null && !bean.getHomePage()) || (bean.getStatus() != null && bean.getStatus() != 1))
                && aggregation.getHomePage() && aggregation.getStatus() == 1){
            return 0;
        }else if((bean.getStatus() != null && bean.getStatus() == 1 && bean.getHomePage() != null && bean.getHomePage()) ||
                (bean.getHomePage() != null && bean.getHomePage() && bean.getStatus() == null && aggregation.getStatus() == 1) ||
                (bean.getHomePage() == null && bean.getStatus() != null && bean.getStatus() == 1 && aggregation.getHomePage())){
            mapper.updateStatus(aggregation.getAppId());
        }
        return mapper.updateByPrimaryKeySelective(bean);
    }

    @Override
    public int updateContent(Aggregation bean) throws AggregationException{
        AggregationMpu aggregationSku = new AggregationMpu();
        JSONArray AggregationArray = JSONObject.parseArray(bean.getContent());
        for (int i = 0; i < AggregationArray.size(); i++) {
            mpuMapper.deleteByPrimaryKey(bean.getId());
            int type = AggregationArray.getJSONObject(i).getInteger("type");
            if (type == 3) {
                JSONObject titleObject = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONObject("settings").getJSONObject("title");
                Integer promotionActivityId = titleObject.getInteger("promotionActivityId");
                if (promotionActivityId != null && promotionActivityId != -1) {
                    aggregationSku.setLevel(i);
                    aggregationSku.setAggregationId(bean.getId());
                    aggregationSku.setPromotionId(promotionActivityId);
                    JSONArray jsonArray = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
                    for (int j = 0; j < jsonArray.size(); j++) {
                        String skuid = jsonArray.getJSONObject(j).getString("skuid");
                        aggregationSku.setMpu(skuid);
                        aggregationSku.setSkuIndex(j);
                        mpuMapper.insertSelective(aggregationSku);
                    }
                }
            }
        }
        Aggregation aggregation = new Aggregation();
        aggregation.setId(bean.getId());
        aggregation.setContent(bean.getContent());
        return mapper.updateContent(aggregation);
        }

    @Override
    public int deleteAggregation(Integer id) throws AggregationException{
        Aggregation aggregation = new Aggregation();
        aggregation.setStatus(3);
        aggregation.setId(id);
        return mapper.updateByPrimaryKeySelective(aggregation);
    }

    @Override
    public PageBean serachAggregation(AggregationBean bean) throws AggregationException{
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize",bean.getLimit());
        map.put("name",bean.getName());
        map.put("status",bean.getStatus());
        map.put("appId",bean.getAppId());
        if(bean.getHomePage() != null && !"".equals(bean.getHomePage())){
            map.put("homePage",Boolean.valueOf(bean.getHomePage()));
        }
        map.put("groupId",bean.getGroupId());
        map.put("order",bean.getOrder());
        if(bean.getMerchantId() != 0){
            map.put("merchantId",bean.getMerchantId());
        }
        List<Aggregation> brands = new ArrayList<>();
        total = mapper.selectCount(map);
        if (total > 0) {
            brands = mapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, brands, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    @Override
    public Aggregation findHomePage(String appId) throws AggregationException {
        Aggregation homePage = mapper.findHomePage(appId);
        Aggregation aggregationByIdtest = convertContent(homePage.getContent(), appId);
//        JSONArray AggregationArray = JSONObject.parseArray(homePage.getContent());
//        if(AggregationArray == null || AggregationArray.size() < 1 ){
//            return homePage;
//        }
//        for (int i = 0; i < AggregationArray.size(); i++) {
//            mpuMapper.deleteByPrimaryKey(homePage.getId());
//            int type = AggregationArray.getJSONObject(i).getInteger("type");
//            if (type == 3) {
//                    JSONArray jsonArray = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
//                    for (int j = 0; j < jsonArray.size(); j++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(j);
//                        String mpu = jsonArray.getJSONObject(j).getString("mpu");
//                        if(mpu != null && !mpu.equals("")){
//                            String value = redisDAO.getValue(mpu);
////                        AoyiProdIndex product = (AoyiProdIndex)net.sf.json.JSONObject.toBean(net.sf.json.JSONObject.fromObject(value), AoyiProdIndex.class);
//                            JSONObject product = JSONObject.parseObject(value);
//                            if(product != null){
//                                jsonObject.put("imagePath",product.getString("image"));
//                                jsonObject.put("intro",product.getString("brand") + " " + product.getString("name"));
//                            }
//                        }
//                }
//            }
//            if (type == 4) {
//                    JSONArray lists = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
//                    for (int j = 0; j < lists.size(); j++) {
//                        JSONArray mpus = lists.getJSONObject(j).getJSONArray("skus");
//                        for (int l = 0; l < mpus.size(); l++){
//                            JSONObject jsonObject = mpus.getJSONObject(l);
//                            String mpu = jsonObject.getString("mpu");
//                            if(mpu != null && !mpu.equals("")){
//                                String value = redisDAO.getValue(mpu);
////                            AoyiProdIndex product = (AoyiProdIndex)net.sf.json.JSONObject.toBean(net.sf.json.JSONObject.fromObject(value), AoyiProdIndex.class);
//                                JSONObject product = JSONObject.parseObject(value);
//                                if(product != null){
//                                    jsonObject.put("price",product.getString("price"));
//                                    jsonObject.put("imagePath",product.getString("image"));
//                                    jsonObject.put("intro",product.getString("brand") + " " + product.getString("name"));
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        homePage.setContent(aggregationByIdtest.getContent());
        return homePage;
    }

    @Override
    public Aggregation findAdminAggregationById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateMpuPriceAndStateForAggregation() {
        AggregationBean bean = new AggregationBean() ;
        bean.setLimit(20);
        HashMap map = new HashMap();

        map.put("pageSize",bean.getLimit());
        map.put("status",1);
        List<Aggregation> aggregations = new ArrayList<>();
        int total  = mapper.selectCount(map);
        if (total > 0) {
            for (int i = 0; i < total/bean.getLimit() + 1; i++) {
                map.put("pageNo", i+bean.getLimit());
                i = i + bean.getLimit() ;
                aggregations = mapper.selectAllLimit(map);
                log.debug("aggregation list is {}", JSONUtil.toJsonString(aggregations));
                for (Aggregation aggregation: aggregations) {
                    aggregation = convertContentAdmin(aggregation.getContent(), aggregation.getAppId(), aggregation.getId()) ;
                    if (aggregation.getId() != null) {
                        mapper.updateByPrimaryKeySelective(aggregation) ;
                    }
                }
            }

        }
    }

    public Aggregation convertContentAdmin(String content, String appId, Integer id) throws AggregationException {
        List<String> mpus = new ArrayList<>();
        Aggregation aggregation = new Aggregation();
        JSONArray AggregationArray = JSONObject.parseArray(content);
        if(content == null || content.equals("") ){
            return aggregation;
        }
        for (int i = 0; i < AggregationArray.size(); i++) {
            int type = AggregationArray.getJSONObject(i).getInteger("type");
            if (type == 3 /* PromotionType */ || type == 10 /* HorizontalGoodType */ ) {
                JSONArray jsonArray = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
                for (int j = 0; j < jsonArray.size(); j++) {
                    String mpu = jsonArray.getJSONObject(j).getString("mpu");
                    if(mpu != null && !mpu.equals("")){
                        mpus.add(mpu);
                    }
                }
            } else if (type == 4  /* GoodsType */ || type == 9 /* PromotionListType */) {
                JSONArray jsonArray = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
                for (int j = 0; j < jsonArray.size(); j++) {
                    JSONArray array = jsonArray.getJSONObject(j).getJSONArray("skus");
                    for (int m = 0; m < array.size(); m++){
                        String mpu = array.getJSONObject(m).getString("mpu");
                        if(mpu != null && !mpu.equals("")){
                            mpus.add(mpu);
                        }
                    }
                }
            }
        }
        HashSet<String> hashSet = new HashSet<>(mpus);
        mpus.clear();
        mpus.addAll(hashSet);
        Map<String, AoyiProdIndex> aoyiProdMap = new HashMap();
        Map<String, PromotionMpu> promotionMap = new HashMap();
        if(!mpus.isEmpty()){
            List<AoyiProdIndex> aoyiProdIndices = productRpcService.findProductListByMpuIdList(mpus, appId, "admin");
            for(AoyiProdIndex prod: aoyiProdIndices){
                aoyiProdMap.put(prod.getMpu(), prod);
            }

            OperaResult onlinePromotion = equityService.findOnlinePromotion(appId);
            if (onlinePromotion.getCode() == 200) {
                Object object = onlinePromotion.getData().get("result");
                List<PromotionMpu> promotionMpus = JSONObject.parseArray(JSON.toJSONString(object), PromotionMpu.class);
                for(PromotionMpu mpu: promotionMpus){
                    promotionMap.put(mpu.getMpu(), mpu);
                }
            }
        }
        for (int i = 0; i < AggregationArray.size(); i++) {
            int type = AggregationArray.getJSONObject(i).getInteger("type");
            if (type == 3 || type == 10) {
                JSONArray jsonArray = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
                for (int j = 0; j < jsonArray.size(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    String mpu = jsonObject.getString("mpu");
                    if (mpu != null && !mpu.equals("")) {
                        AoyiProdIndex aoyiProdIndex = aoyiProdMap.get(mpu);
                        if(aoyiProdIndex != null){
                            if (!"1".equals(aoyiProdIndex.getState())) {
                                // TODO 写map 发邮件
                                log.info("del mpu is {}", jsonObject.toJSONString());
                                jsonArray.remove(j) ;
                                j = j - 1 ;
                            }
                            String imageUrl = aoyiProdIndex.getImagesUrl();
                            if (imageUrl != null && (!"".equals(imageUrl))) {
                                String image = "";
                                if (imageUrl.indexOf("/") == 0) {
                                    image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                                } else {
                                    image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                                }
                                aoyiProdIndex.setImage(image);
                            }
                            jsonObject.put("imagePath", aoyiProdIndex.getImage());
                            jsonObject.put("price", aoyiProdIndex.getPrice());
                            jsonObject.put("name", aoyiProdIndex.getName());
                            jsonObject.put("subTitle", aoyiProdIndex.getSubTitle());
                            jsonObject.put("state", aoyiProdIndex.getState());
                            PromotionMpu promotionMpu = promotionMap.get(mpu);
                            if (type == 10 && promotionMpu != null) {
                                jsonObject.put("discount", promotionMpu.getDiscount());
                            }
                        }
                    }
                }
            }
            if (type == 4 || type == 9) {
                JSONArray jsonArray = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
                for (int j = 0; j < jsonArray.size(); j++) {
                    JSONArray array = jsonArray.getJSONObject(j).getJSONArray("skus");
                    for (int m = 0; m < array.size(); m++) {
                        JSONObject jsonObject = array.getJSONObject(m);
                        String mpu = array.getJSONObject(m).getString("mpu");
                        if (mpu != null && !mpu.equals("")) {
                            AoyiProdIndex aoyiProdIndex = aoyiProdMap.get(mpu);
                            if(aoyiProdIndex != null){
                                if (!"1".equals(aoyiProdIndex.getState())) {
                                    log.info("del mpu is {}", jsonObject.toJSONString());
                                    jsonArray.remove(j) ;
                                    j = j - 1 ;
                                }
                                String imageUrl = aoyiProdIndex.getImagesUrl();
                                if (imageUrl != null && (!"".equals(imageUrl))) {
                                    String image = "";
                                    if (imageUrl.indexOf("/") == 0) {
                                        image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                                    } else {
                                        image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                                    }
                                    aoyiProdIndex.setImage(image);
                                }
                                PromotionMpu promotionMpu = promotionMap.get(mpu);
                                jsonObject.put("price", aoyiProdIndex.getPrice());
                                jsonObject.put("imagePath", aoyiProdIndex.getImage());
                                jsonObject.put("name", aoyiProdIndex.getName());
                                jsonObject.put("subTitle", aoyiProdIndex.getSubTitle());
                                jsonObject.put("state", aoyiProdIndex.getState());
                                if (type == 4 && promotionMpu != null) {
                                    jsonObject.put("discount", promotionMpu.getDiscount());
                                }
                            }
                        }
                    }
                }
            }
        }
        aggregation.setContent(AggregationArray.toString());
        aggregation.setId(id);
        return aggregation;
    }
}
