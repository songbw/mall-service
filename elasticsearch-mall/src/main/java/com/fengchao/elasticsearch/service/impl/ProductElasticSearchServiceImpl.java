package com.fengchao.elasticsearch.service.impl;

import com.fengchao.elasticsearch.domain.*;
import com.fengchao.elasticsearch.service.ProductESService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
@Slf4j
public class ProductElasticSearchServiceImpl implements ProductESService {

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
        builder.query(QueryBuilders.matchQuery("name", queryBean.getKeyword()));
        builder.from(PageBean.getOffset(queryBean.getPageNo(), queryBean.getPageSize())).size(queryBean.getPageSize()); // 分页
        request.source(builder);
        try{
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            List<Map<String, Object>> hits = new ArrayList<>();
            for (SearchHit documentFields : response.getHits()) {
                hits.add(documentFields.getSourceAsMap());
                log.info("result: {}, code: {}, status: {}", documentFields.toString(), response.status().getStatus(), response.status().name());
            }
            return PageBean.build(new PageBean(), hits, Integer.parseInt(response.getHits().getTotalHits().value + ""), queryBean.getPageNo(), queryBean.getPageSize());
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
}
