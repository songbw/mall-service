package com.fengchao.elasticsearch.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.elasticsearch.domain.*;
import com.fengchao.elasticsearch.service.ProductESService;
import com.fengchao.elasticsearch.utils.CosUtil;
import com.fengchao.elasticsearch.utils.ProductHandle;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@Slf4j
public class ProductESServiceImpl implements ProductESService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public boolean save(AoyiProdIndex product) {
        return false;
    }

    @Override
    public PageBean query(ProductQueryBean queryBean) {
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(queryBean.getKeyword())) {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", queryBean.getKeyword());
            boolQueryBuilder.must(matchQueryBuilder) ;
        }
        TermQueryBuilder termQueryBuilder =  QueryBuilders.termQuery("state", "1") ;
        boolQueryBuilder.must(termQueryBuilder);
        builder.query(boolQueryBuilder);
        builder.from(PageBean.getOffset(queryBean.getPageNo(), queryBean.getPageSize())).size(queryBean.getPageSize()); // 分页
        request.source(builder);
        try{
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            List<AoyiProdIndex> aoyiProdIndices = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE);
            for (SearchHit documentFields : response.getHits()) {
                // doc 转 json
                String sourceAsString = documentFields.getSourceAsString() ;
                // json 转对象
                AoyiProdIndex aoyiProdIndex = objectMapper.readValue(sourceAsString, AoyiProdIndex.class) ;
                aoyiProdIndex = ProductHandle.updateImage(aoyiProdIndex);
                aoyiProdIndices.add(aoyiProdIndex);
                log.info("result: {}, code: {}, status: {}", documentFields.toString(), response.status().getStatus(), response.status().name());
            }
            return PageBean.build(new PageBean(), aoyiProdIndices, Integer.parseInt(response.getHits().getTotalHits().value + ""), queryBean.getPageNo(), queryBean.getPageSize());
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
    public Page<AoyiProdIndex> query(QueryDTO queryDTO, int pageNo, int size) {
        return null;
    }

    @Override
    public AoyiProdIndex get(java.lang.String skuId) {
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
            List<AoyiProdIndex> aoyiProdIndices = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE);
            for (SearchHit documentFields : response.getHits()) {
                // doc 转 json
                String sourceAsString = documentFields.getSourceAsString() ;
                // json 转对象
                AoyiProdIndex aoyiProdIndex = objectMapper.readValue(sourceAsString, AoyiProdIndex.class) ;
                aoyiProdIndex = ProductHandle.updateImage(aoyiProdIndex);
                aoyiProdIndices.add(aoyiProdIndex);
                log.info("result: {}, code: {}, status: {}", documentFields.toString(), response.status().getStatus(), response.status().name());
            }
            return PageBean.build(new PageBean(), aoyiProdIndices, Integer.parseInt(response.getHits().getTotalHits().value + ""), queryBean.getPageNo(), queryBean.getPageSize());
        }catch (Exception e){
            e.printStackTrace();
        }
        return new PageBean();
    }
}
