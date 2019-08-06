package com.fengchao.elasticsearch.service;

import com.fengchao.elasticsearch.domain.*;

import java.util.List;

public interface ProductESService {

    boolean save(AoyiProdIndex product);

    PageBean query(ProductQueryBean queryBean);

    PageBean search(ProductQueryBean queryBean);

    Page<AoyiProdIndex> query(QueryDTO queryDTO, int pageNo, int size);

    AoyiProdIndex get(String skuId);

    boolean batchSave(List<AoyiProdIndex> list) ;

    int allCount();

    PageBean queryByCategoryPrefix(ProductQueryBean queryBean);
}
