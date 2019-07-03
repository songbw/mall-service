package com.fengchao.aggregation.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.aggregation.bean.AggregationBean;
import com.fengchao.aggregation.bean.PageBean;
import com.fengchao.aggregation.exception.AggregationException;
import com.fengchao.aggregation.mapper.*;
import com.fengchao.aggregation.model.*;
import com.fengchao.aggregation.service.AggregationService;
import com.fengchao.aggregation.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AggregationServiceImpl implements AggregationService {

    @Autowired
    private AggregationMapper mapper;
    @Autowired
    private AggregationSkuMapper skuMapper;


    @Override
    public PageBean findAggregation(Integer offset, Integer limit, String order, Integer merchantId) throws AggregationException {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(offset, limit);
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize",limit);
        map.put("order",order);
        if(merchantId != 0){
            map.put("merchantId",merchantId);
        }
        List<Aggregation> brands = new ArrayList<>();
        total = mapper.selectCount(map);
        if (total > 0) {
            brands = mapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, brands, total, offset, limit);
        return pageBean;
    }

    @Override
    public int createAggregation(Aggregation bean) throws AggregationException {
        if(bean.getStatus() != null && bean.getStatus() == 1 && bean.getHomePage()){
            mapper.updateStatus();
        }
        return mapper.insertSelective(bean);
    }

    @Override
    public Aggregation findAggregationById(Integer id) throws AggregationException {
        Aggregation aggregation = mapper.selectByPrimaryKey(id);
        JSONArray AggregationArray = JSONObject.parseArray(aggregation.getContent());
        if(AggregationArray == null || AggregationArray.size() < 1 ){
            return aggregation;
        }
        for (int i = 0; i < AggregationArray.size(); i++) {
            int type = AggregationArray.getJSONObject(i).getInteger("type");
            if (type == 3) {
                JSONArray jsonArray = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
                for (int j = 0; j < jsonArray.size(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    String skuid = jsonArray.getJSONObject(j).getString("skuid");
                    String value = RedisUtil.getValue(skuid);
//                    AoyiProdIndex product = (AoyiProdIndex)net.sf.json.JSONObject.toBean(net.sf.json.JSONObject.fromObject(value), AoyiProdIndex.class);
                    JSONObject product = JSONObject.parseObject(value);
                    if(product != null){
                        jsonObject.put("imagePath",product.getString("image"));
                        jsonObject.put("intro",product.getString("brand") + " " + product.getString("name"));
                    }
                }
            }
            if (type == 4) {
                JSONArray lists = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
                for (int j = 0; j < lists.size(); j++) {
                    JSONArray skus = lists.getJSONObject(j).getJSONArray("skus");
                    for (int l = 0; l < skus.size(); l++){
                        JSONObject jsonObject = skus.getJSONObject(l);
                        String skuid = jsonObject.getString("skuid");
                        String value = RedisUtil.getValue(skuid);
//                        AoyiProdIndex product = (AoyiProdIndex)net.sf.json.JSONObject.toBean(net.sf.json.JSONObject.fromObject(value), AoyiProdIndex.class);
                        JSONObject product = JSONObject.parseObject(value);
                        if(product != null){
                            jsonObject.put("price",product.getString("price"));
                            jsonObject.put("imagePath",product.getString("image"));
                            jsonObject.put("intro",product.getString("brand") + " " + product.getString("name"));
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
            mapper.updateStatus();
        }
        return mapper.updateByPrimaryKeySelective(bean);
    }

    @Override
    public int updateContent(Aggregation bean) throws AggregationException{
        AggregationSku aggregationSku = new AggregationSku();
        JSONArray AggregationArray = JSONObject.parseArray(bean.getContent());
        for (int i = 0; i < AggregationArray.size(); i++) {
            skuMapper.deleteByPrimaryKey(bean.getId());
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
                        aggregationSku.setSkuId(skuid);
                        aggregationSku.setSkuIndex(j);
                        skuMapper.insertSelective(aggregationSku);
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
        return mapper.deleteByPrimaryKey(id);
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
    public Aggregation findHomePage() throws AggregationException {
        Aggregation homePage = mapper.findHomePage();
        JSONArray AggregationArray = JSONObject.parseArray(homePage.getContent());
        if(AggregationArray == null || AggregationArray.size() < 1 ){
            return homePage;
        }
        for (int i = 0; i < AggregationArray.size(); i++) {
            skuMapper.deleteByPrimaryKey(homePage.getId());
            int type = AggregationArray.getJSONObject(i).getInteger("type");
            if (type == 3) {
                    JSONArray jsonArray = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
                    for (int j = 0; j < jsonArray.size(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        String skuid = jsonArray.getJSONObject(j).getString("skuid");
                        String value = RedisUtil.getValue(skuid);
//                        AoyiProdIndex product = (AoyiProdIndex)net.sf.json.JSONObject.toBean(net.sf.json.JSONObject.fromObject(value), AoyiProdIndex.class);
                        JSONObject product = JSONObject.parseObject(value);
                        if(product != null){
                           jsonObject.put("imagePath",product.getString("image"));
                           jsonObject.put("intro",product.getString("brand") + " " + product.getString("name"));
                       }
                }
            }
            if (type == 4) {
                    JSONArray lists = AggregationArray.getJSONObject(i).getJSONObject("data").getJSONArray("list");
                    for (int j = 0; j < lists.size(); j++) {
                        JSONArray skus = lists.getJSONObject(j).getJSONArray("skus");
                        for (int l = 0; l < skus.size(); l++){
                            JSONObject jsonObject = skus.getJSONObject(l);
                            String skuid = jsonObject.getString("skuid");
                            String value = RedisUtil.getValue(skuid);
//                            AoyiProdIndex product = (AoyiProdIndex)net.sf.json.JSONObject.toBean(net.sf.json.JSONObject.fromObject(value), AoyiProdIndex.class);
                            JSONObject product = JSONObject.parseObject(value);
                            if(product != null){
                                jsonObject.put("price",product.getString("price"));
                                jsonObject.put("imagePath",product.getString("image"));
                                jsonObject.put("intro",product.getString("brand") + " " + product.getString("name"));
                            }
                        }
                    }
                }
            }
        homePage.setContent(AggregationArray.toString());
        return homePage;
    }

    @Override
    public Aggregation findAdminAggregationById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }
}
