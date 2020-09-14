package com.fengchao.product.aoyi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.config.ESConfig;
import com.fengchao.product.aoyi.config.MerchantCodeBean;
import com.fengchao.product.aoyi.feign.EquityService;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.service.ProductESService;
import com.fengchao.product.aoyi.utils.ProductHandle;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;


@EnableConfigurationProperties({ESConfig.class})
@Repository
@Slf4j
public class ProductESServiceImpl implements ProductESService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private ESConfig esConfig;
    @Autowired
    private EquityService equityService ;

    @Override
    public boolean save(AoyiProdIndex product) {
        return false;
    }

    @Override
    public PageBean query(ProductQueryBean queryBean) {
        // 获取可读取的商户配置
        MerchantCodeBean merchantCodeBean = getMerchantCodesByAppId(queryBean.getAppId()) ;
        List<String> codes = new ArrayList<>() ;
        if (merchantCodeBean != null) {
            codes = merchantCodeBean.getCodes() ;
        }
        SearchRequest request = new SearchRequest();
        request.indices(esConfig.getEsIndex());
        if (esConfig.getEsType() != null) {
            request.searchType(esConfig.getEsType()) ;
        }

        Map<String, Object> map = new HashMap<>() ;
        map.put("keyword", queryBean.getKeyword()) ;
        map.put("appId", queryBean.getAppId()) ;
        map.put("createdAt", new Date()) ;
        saveKeyword(map);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(queryBean.getKeyword())) {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", queryBean.getKeyword());
            boolQueryBuilder.must(matchQueryBuilder) ;
        }
        // merchant code
        BoolQueryBuilder merchantCodeBoolQuery = QueryBuilders.boolQuery() ;
        for (String code: codes) {
            TermQueryBuilder shouldTermQueryBuilder = QueryBuilders.termQuery("merchant_code", code) ;
            merchantCodeBoolQuery.should(shouldTermQueryBuilder) ;
        }
        boolQueryBuilder.must(merchantCodeBoolQuery) ;

        TermQueryBuilder termQueryBuilder =  QueryBuilders.termQuery("state", "1") ;
        boolQueryBuilder.must(termQueryBuilder);

        builder.query(boolQueryBuilder);


        builder.from(PageBean.getOffset(queryBean.getPageNo(), queryBean.getPageSize())).size(queryBean.getPageSize()); // 分页
        if (!StringUtils.isEmpty(queryBean.getPriceOrder())) {
            FieldSortBuilder fieldSortBuilder = new FieldSortBuilder("price");
            if ("DESC".equals(queryBean.getPriceOrder())) {
                builder.sort(fieldSortBuilder.order(SortOrder.DESC));
            }else {
                builder.sort(fieldSortBuilder.order(SortOrder.ASC));
            }
        }
        request.source(builder);
        try{
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            log.info("result: {}", request.toString());
//            log.info("response: {}", response.toString());
            List<ProductInfoBean> aoyiProdIndices = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE);
            for (SearchHit documentFields : response.getHits()) {
                ProductInfoBean infoBean = new ProductInfoBean();
                // doc 转 json
                String sourceAsString = documentFields.getSourceAsString() ;
                // json 转对象
                AoyiProdIndexX aoyiProdIndex = objectMapper.readValue(sourceAsString, AoyiProdIndexX.class) ;
                aoyiProdIndex = ProductHandle.updateImage(aoyiProdIndex);
                BeanUtils.copyProperties(aoyiProdIndex, infoBean);
                List<PromotionInfoBean> promotionInfoBeans = findPromotionBySku(aoyiProdIndex.getMpu(), queryBean.getAppId());
                infoBean.setPromotion(promotionInfoBeans);
                aoyiProdIndices.add(infoBean);
                log.info("result: {}, code: {}, status: {}", documentFields.toString(), response.status().getStatus(), response.status().name());
            }
            return PageBean.build(new PageBean(), aoyiProdIndices, Integer.parseInt(response.getHits().getTotalHits() + ""), queryBean.getPageNo(), queryBean.getPageSize());
        }catch (Exception e){
            e.printStackTrace();
        }
        return new PageBean();

    }

    @Override
    public PageBean search(ProductQueryBean queryBean) {
        return null;
    }

    @Override
    public AoyiProdIndex get(String skuId) {
        return null;
    }

    @Override
    public boolean batchSave(List<AoyiProdIndex> list) {
        return false;
    }

    @Override
    public int allCount() {
        return 0;
    }

    @Override
    public PageBean queryByCategoryPrefix(ProductQueryBean queryBean) {
        SearchRequest request = new SearchRequest();
        request.indices(esConfig.getEsIndex());
        if (esConfig.getEsType() != null) {
            request.searchType(esConfig.getEsType()) ;
        }
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(queryBean.getKeyword())) {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", queryBean.getKeyword());
            boolQueryBuilder.must(matchQueryBuilder) ;
        }
        if (!StringUtils.isEmpty(queryBean.getCategory())) {
            TermQueryBuilder termQueryBuilder =  QueryBuilders.termQuery("category", queryBean.getCategory()) ;
            boolQueryBuilder.must(termQueryBuilder);
        }
        if (!StringUtils.isEmpty(queryBean.getSkuPrefix())) {
            PrefixQueryBuilder prefixQueryBuilder =  QueryBuilders.prefixQuery("skuid", queryBean.getSkuPrefix()) ;
            boolQueryBuilder.must(prefixQueryBuilder) ;
        }
        TermQueryBuilder stateTermQueryBuilder =  QueryBuilders.termQuery("state", "1") ;
        boolQueryBuilder.must(stateTermQueryBuilder);
        builder.query(boolQueryBuilder);
        builder.from(PageBean.getOffset(queryBean.getPageNo(), queryBean.getPageSize())).size(queryBean.getPageSize()); // 分页
        request.source(builder);
        try{
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            List<ProductInfoBean> aoyiProdIndices = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE);
            for (SearchHit documentFields : response.getHits()) {
                ProductInfoBean infoBean = new ProductInfoBean();
                // doc 转 json
                String sourceAsString = documentFields.getSourceAsString() ;
                // json 转对象
                AoyiProdIndex aoyiProdIndex = objectMapper.readValue(sourceAsString, AoyiProdIndex.class) ;
                aoyiProdIndex = ProductHandle.updateImageExample(aoyiProdIndex);
                BeanUtils.copyProperties(aoyiProdIndex, infoBean);
                List<PromotionInfoBean> promotionInfoBeans = findPromotionBySku(aoyiProdIndex.getMpu(), queryBean.getAppId());
                infoBean.setPromotion(promotionInfoBeans);
                aoyiProdIndices.add(infoBean);
                log.info("result: {}, code: {}, status: {}", documentFields.toString(), response.status().getStatus(), response.status().name());
            }
            return PageBean.build(new PageBean(), aoyiProdIndices, Integer.parseInt(response.getHits().getTotalHits() + ""), queryBean.getPageNo(), queryBean.getPageSize());
        }catch (Exception e){
            e.printStackTrace();
        }
        return new PageBean();
    }

    @Override
    public int delete(int id) {
        DeleteRequest request = new DeleteRequest(esConfig.getEsIndex(), "zhsc", id + "") ;
        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(request, RequestOptions.DEFAULT) ;
            deleteResponse.status();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private List<PromotionInfoBean> findPromotionBySku(String skuId, String appId) {
        OperaResult result = equityService.findPromotionBySkuId(skuId, appId);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<PromotionInfoBean> subOrderTS = JSONObject.parseArray(jsonString, PromotionInfoBean.class);
            return subOrderTS;
        }
        return null;
    }

    private MerchantCodeBean getMerchantCodesByAppId(String appId) {
        return  esConfig.getRegion().get(appId) ;
    }

    private void saveKeyword(Map<String, Object> map) {
        IndexRequest request = new IndexRequest("productkeyword", "keyword"); // 这里最后一个参数是es里储存的id，如果不填，es会自动生成一个，个人建议跟自己的数据库表里id保持一致，后面更新删除都会很方便
        request.source(map);
        IndexResponse response = null;
        try {
            response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // not exist: result: code: 201, status: CREATED
        // exist: result: code: 200, status: OK
        log.info("result: code: {}, status: {}", response.status().getStatus(), response.status().name());
    }
}