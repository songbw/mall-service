package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.bean.ProductQueryBean;
import com.fengchao.product.aoyi.model.AoyiProdIndex;

import java.util.List;

public interface ProductESService {

    boolean save(AoyiProdIndex product);

    PageBean query(ProductQueryBean queryBean);

    PageBean search(ProductQueryBean queryBean);

    AoyiProdIndex get(String skuId);

    boolean batchSave(List<AoyiProdIndex> list) ;

    int allCount();

    PageBean queryByCategoryPrefix(ProductQueryBean queryBean);

    int delete(int id) ;
}
