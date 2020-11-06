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
import com.fengchao.product.aoyi.rpc.VendorsRpcService;
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
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
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

    private RestHighLevelClient restHighLevelClient;
    private ESConfig esConfig;
    private EquityService equityService ;
    private ProductHandle productHandle ;
    private VendorsRpcService vendorsRpcService ;

    @Autowired
    public ProductESServiceImpl(RestHighLevelClient restHighLevelClient, ESConfig esConfig, EquityService equityService, ProductHandle productHandle, VendorsRpcService vendorsRpcService) {
        this.restHighLevelClient = restHighLevelClient;
        this.esConfig = esConfig;
        this.equityService = equityService;
        this.productHandle = productHandle;
        this.vendorsRpcService = vendorsRpcService ;
    }

    @Override
    public boolean save(AoyiProdIndex product) {
        return false;
    }

    @Override
    public PageBean query(ProductQueryBean queryBean) {
        productHandle.setClientProductQueryBean(queryBean);
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

        // merchant id
        BoolQueryBuilder merchantIdBoolQuery = QueryBuilders.boolQuery() ;
        for (int merchantId: queryBean.getMerchantIds()) {
            TermQueryBuilder shouldTermQueryBuilder = QueryBuilders.termQuery("merchant_id", merchantId) ;
            merchantIdBoolQuery.should(shouldTermQueryBuilder) ;
        }
        boolQueryBuilder.must(merchantIdBoolQuery) ;

        // category
        BoolQueryBuilder categoryBoolQuery = QueryBuilders.boolQuery() ;
        for (String category: queryBean.getCategories()) {
            TermQueryBuilder shouldTermQueryBuilder = QueryBuilders.termQuery("category", category) ;
            categoryBoolQuery.should(shouldTermQueryBuilder) ;
        }
        boolQueryBuilder.must(categoryBoolQuery) ;

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

                productHandle.setProductXClient(aoyiProdIndex, queryBean.getRenterId());
                if ("1".equals(aoyiProdIndex.getState())) {
                    BeanUtils.copyProperties(aoyiProdIndex, infoBean);
                    List<PromotionInfoBean> promotionInfoBeans = findPromotionBySku(aoyiProdIndex.getMpu(), queryBean.getAppId());
                    infoBean.setPromotion(promotionInfoBeans);
                    aoyiProdIndices.add(infoBean);
                }
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
                aoyiProdIndex = productHandle.updateImageExample(aoyiProdIndex);
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

    @Override
    public OperaResponse topKeyword(ProductQueryBean queryBean) {
        // topKeyword
        OperaResponse operaResponse = new OperaResponse();
        SearchRequest request = new SearchRequest("productkeyword");
        // 通过SearchSourceBuilder构建搜索参数
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 通过QueryBuilders构建ES查询条件，这里查询所有文档，复杂的查询语句设置请参考前面的章节。
//        builder.query(QueryBuilders.matchAllQuery());
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder stateTermQueryBuilder =  QueryBuilders.termQuery("appId", queryBean.getAppId()) ;
        boolQueryBuilder.must(stateTermQueryBuilder);
        if (!StringUtils.isEmpty(queryBean.getStartTime()) && !StringUtils.isEmpty(queryBean.getEndTime())) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createdAt").gte(queryBean.getStartTime()).lte(queryBean.getEndTime()) ;
            boolQueryBuilder.must(rangeQueryBuilder);
        }
        builder.query(boolQueryBuilder);
        // 创建terms桶聚合，聚合名字=by_shop, 字段=shop_id，根据shop_id分组
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("topCount")
                .field("keyword");
        // 设置聚合查询
        builder.aggregation(aggregationBuilder);
        // 设置搜索条件
        request.source(builder) ;
        builder.size(0) ;

        try{
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            log.debug("topKeyword result: {}", request.toString());
            // 处理聚合查询结果
            Aggregations aggregations = response.getAggregations();

            // 根据by_shop名字查询terms聚合结果
            Terms topCountAggregation = aggregations.get("topCount");
            List<HotWord> hotWorks = new ArrayList<>();

            // 遍历terms聚合结果
            for (Terms.Bucket bucket  : topCountAggregation.getBuckets()) {
                // 因为是根据shop_id分组，因此可以直接将桶的key转换成int类型
//                int shopId = bucket.getKeyAsNumber().intValue();
                HotWord hotWork = new HotWord();
                hotWork.setCount(bucket.getDocCount());
                hotWork.setWork(bucket.getKeyAsString()) ;
                hotWorks.add(hotWork) ;
                log.debug("topKeyword response: {}, {}", bucket.getDocCount(), bucket.getKeyAsString());
            }
            operaResponse.setData(hotWorks);
            return operaResponse ;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public OperaResponse topKeywordAdmin(ProductQueryBean queryBean) {
        OperaResponse operaResponse = new OperaResponse();
        List<String> appIds = new ArrayList<>();
        // topKeyword
        if (!StringUtils.isEmpty(queryBean.getRenterHeader()) && !"0".equals(queryBean.getRenterHeader())) {
            appIds = vendorsRpcService.queryAppIdList(queryBean.getRenterHeader()) ;
            if (appIds == null || appIds.size() ==0) {
                return operaResponse;
            }
        }

        SearchRequest request = new SearchRequest("productkeyword");
        // 通过SearchSourceBuilder构建搜索参数
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 通过QueryBuilders构建ES查询条件，这里查询所有文档，复杂的查询语句设置请参考前面的章节。
//        builder.query(QueryBuilders.matchAllQuery());
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (appIds != null && appIds.size() > 0) {
            for (String appId: appIds) {
                TermQueryBuilder appTermQueryBuilder =  QueryBuilders.termQuery("appId", appId) ;
                boolQueryBuilder.must(appTermQueryBuilder);
            }
        }
        if (!StringUtils.isEmpty(queryBean.getStartTime()) && !StringUtils.isEmpty(queryBean.getEndTime())) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createdAt").gte(queryBean.getStartTime()).lte(queryBean.getEndTime()) ;
            boolQueryBuilder.must(rangeQueryBuilder);
        }
        builder.query(boolQueryBuilder);

        // 创建terms桶聚合，聚合名字=by_shop, 字段=shop_id，根据shop_id分组
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("appCount")
                .field("appId");

        // 嵌套聚合
        // 设置Avg指标聚合，聚合名字=avg_price, 字段=price，计算平均价格
        aggregationBuilder.subAggregation(AggregationBuilders.terms("topCount").field("keyword").size(queryBean.getPageSize()));

        // 设置聚合查询
        builder.aggregation(aggregationBuilder);
        // 设置搜索条件
        request.source(builder) ;
        builder.size(0) ;

        try{
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            log.info("topKeyword result: {}", request.toString());
            // 处理聚合查询结果
            Aggregations aggregations = response.getAggregations();

            // 根据by_shop名字查询terms聚合结果
            Terms appCountAggregation = aggregations.get("appCount");
            List<AdminHotWord> adminHotWords = new ArrayList<>();

            // 遍历terms聚合结果
            for (Terms.Bucket bucket  : appCountAggregation.getBuckets()) {
                // 因为是根据shop_id分组，因此可以直接将桶的key转换成int类型
                AdminHotWord adminHotWord = new AdminHotWord();
                adminHotWord.setAppId(bucket.getKeyAsString());
                List<HotWord> hotWords = new ArrayList<>();
                log.debug("appCount response: {}, {}", bucket.getDocCount(), bucket.getKeyAsString());
                Terms topCountAggregation = bucket.getAggregations().get("topCount") ;
                for (Terms.Bucket bucket1 : topCountAggregation.getBuckets()) {
                    HotWord hotWork = new HotWord();
                    hotWork.setCount(bucket1.getDocCount());
                    hotWork.setWork(bucket1.getKeyAsString()) ;
                    hotWords.add(hotWork) ;
                    log.debug("topKeyword response: {}, {}", bucket1.getDocCount(), bucket1.getKeyAsString());
                }
                adminHotWord.setHotWordList(hotWords);
                adminHotWords.add(adminHotWord);
            }
            operaResponse.setData(adminHotWords);
            return operaResponse ;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
