package com.fengchao.elasticsearch.service.impl;

import com.fengchao.elasticsearch.domain.*;
import com.fengchao.elasticsearch.service.ProductESService;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.indices.type.TypeExist;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Repository
@Slf4j
public class ProductESServiceImpl implements ProductESService {

    public static final String INDEX = "product";

    public static final String TYPE = "dy2019";

    @Autowired
    private JestClient client;

    @Override
    public boolean save(AoyiProdIndex product) {
        Index index = new Index.Builder(product).index(INDEX).type(TYPE).build();
        try {
            JestResult jestResult = client.execute(index);
            log.info("save返回结果{}", jestResult.getJsonString());
            return jestResult.isSucceeded();
        } catch (IOException e) {
            log.error("save异常", e);
            return false;
        }
    }

    @Override
    public PageBean query(ProductQueryBean queryBean) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false).tagsSchema("default");
        searchSourceBuilder.highlighter(highlightBuilder);
        QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(queryBean.getKeyword());
        queryStringQueryBuilder
                .field("name", 10)
//                .field("category", 6)
//                .field("brand", 5)
//                .field("model")
        ;
        searchSourceBuilder.query(queryStringQueryBuilder).from(from(queryBean.getPageNo(), queryBean.getPageSize())).size(queryBean.getPageSize());
        log.debug("搜索DSL:{}", searchSourceBuilder.toString());
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(INDEX)
                .addType(TYPE)
                .build();
        try {
            SearchResult result = client.execute(search);
            if (result.isSucceeded()) {
                List<SearchResult.Hit<AoyiProdIndex, Void>> hits = result.getHits(AoyiProdIndex.class);
                List<AoyiProdIndex> products = hits.stream().map(hit -> {
                    AoyiProdIndex product = hit.source;
                    Map<String, List<String>> highlight = hit.highlight;
//                    if (highlight.containsKey("name")) {
//                        product.setName(highlight.get("name").get(0));
//                    }
                    return product;
                }).collect(toList());
                PageBean pageBean = new PageBean();
                pageBean = PageBean.build(pageBean, products, result.getTotal().intValue(), queryBean.getPageNo(), queryBean.getPageSize());
                return pageBean;
            } else {
                return null;
            }
        } catch (IOException e) {
            log.error("search异常", e);
            return null;
        }
    }

    @Override
    public PageBean search(ProductQueryBean queryBean) {
        TransportClient client1 = null;
        try {
            client1 = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("119.3.82.225"), 9200));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        SearchResponse response = client1.prepareSearch(INDEX)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("multi", "test"))                 // Query
                .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
                .setFrom(0).setSize(60).setExplain(true)
                .get();
        return null;
    }

    @Override
    public Page<AoyiProdIndex> query(QueryDTO queryDTO, int pageNo, int size) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().from(from(pageNo, size)).size(size);
//        if (queryDTO.getMinScore() != null) {
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//            RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("score").gte(queryDTO.getMinScore());
            boolQueryBuilder.must(QueryBuilders.termQuery("price", ""));
            searchSourceBuilder.query(boolQueryBuilder);
//        }
        if (queryDTO.getOrderBy() != null) {
            searchSourceBuilder.sort(queryDTO.getOrderBy(), SortOrder.DESC);
        }
        log.debug("搜索DSL:{}", searchSourceBuilder.toString());
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(INDEX)
                .addType(TYPE)
                .build();
        try {
            SearchResult result = client.execute(search);
            List<AoyiProdIndex> movies = result.getSourceAsObjectList(AoyiProdIndex.class, false);
            int took = result.getJsonObject().get("took").getAsInt();
            Page<AoyiProdIndex> page = Page.<AoyiProdIndex>builder().list(movies).pageNo(pageNo).size(size).total(result.getTotal()).took(took).build();
            return page;
        } catch (IOException e) {
            log.error("search异常", e);
            return null;
        }
    }

    @Override
    public AoyiProdIndex get(String skuId) {
        Get get = new Get.Builder(INDEX, skuId).type(TYPE).build();
        try {
            JestResult result = client.execute(get);
            AoyiProdIndex product = result.getSourceAsObject(AoyiProdIndex.class);
            return product;
        } catch (IOException e) {
            log.error("get异常", e);
            return null;
        }
    }

    @Override
    public boolean batchSave(List<AoyiProdIndex> list) {
//        try {
//            System.out.println(createMapping(INDEX,TYPE,"ik"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            Bulk.Builder bulkBuilder = new Bulk.Builder();
            for (AoyiProdIndex product : list) {
                Index index = new Index.Builder(product).index(INDEX).type(TYPE).build();
                bulkBuilder.addAction(index);
            }
            BulkResult br = client.execute(bulkBuilder.build());
            log.info("save返回结果{}", br.getJsonString());
            return br.isSucceeded();
        } catch (IOException e) {
            log.error("save异常", e);
            return false;
        }
    }

    @Override
    public int allCount() {
        return 0;
    }

    private int from(int pageNo, int size) {
        return (pageNo - 1) * size < 0 ? 0 : (pageNo - 1) * size;
    }

    private String createIndex(String indices) throws IOException {
        //判断索引是否存在
        TypeExist indexExist = new TypeExist.Builder(indices).build();
        JestResult result = client.execute(indexExist);
        System.out.println("index exist result " + result.getJsonString());
        Object indexFound = result.getValue("found");

        if (indexFound != null && indexFound.toString().equals("false")) {
            //index 不存在,创建 index
            System.out.println("index found == false");
            JestResult createIndexresult = client.execute(new CreateIndex.Builder(indices).build());
            System.out.println("create index:"+createIndexresult.isSucceeded());
            if(createIndexresult.isSucceeded()) {
                return "ok";
            }else{
                return "create index fail";
            }
        }else{
            return "ok";
        }
    }

    private String createMapping(String indices,String mappingType,String analyzer) throws IOException {
        String message = createIndex(indices);
        if(!message.equals("ok")){
            return "create index fail";
        }
        //判断mapping是否存在
        TypeExist typeExist = new TypeExist.Builder(indices).addType(mappingType).build();
        JestResult mappingResult = client.execute(typeExist);
        Object mappingFound = mappingResult.getValue("found");
        if (mappingFound != null && mappingFound.toString().equals("false")) {
            //索引和mapping不存在可以添加
            XContentBuilder builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(indices)
                    .startObject("properties")
                    .startObject("skuId").field("type", "string").field("store", "yes").endObject()
                    .startObject("name").field("type", "string").field("store", "yes").field("indexAnalyzer", analyzer).field("searchAnalyzer", analyzer).endObject()
                    .startObject("category").field("type", "string").field("store", "yes").endObject()
                    .startObject("brand").field("type", "string").field("store", "yes").endObject()
                    .endObject()
                    .endObject()
                    .endObject();
            String mappingString = builder.toString();
            //构造PutMapping
            PutMapping putMapping = new PutMapping.Builder(indices, mappingType, mappingString).build();
            JestResult maapingResult = client.execute(putMapping);
            return maapingResult.getJsonString();
        }else {
            return "mapping existing";
        }
    }
}
