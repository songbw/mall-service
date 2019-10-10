package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.bean.vo.ProductExportResVo;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;

import java.util.List;

public interface AdminProdService {
    PageBean findProdList(Integer offset, Integer limit, String state, Integer merchantId);

    PageBean selectNameList(SerachBean bean);

    /**
     * 分页查询商品列表
     *
     * @param bean
     * @return
     */
    PageBean selectProductListPageable(SerachBean bean);

    int getProdListToRedis();

    int add(AoyiProdIndex bean) throws ProductException;

    int update(AoyiProdIndex bean) throws ProductException;

    void delete(Integer merchantId, Integer id) throws ProductException;

    PageBean findProdAll(QueryProdBean bean);

    /**
     * 获取商品导出数据
     *
     * @param bean
     * @return
     * @throws Exception
     */
    List<ProductExportResVo> exportProductList(SerachBean bean) throws Exception;

    /**
     * 修改库存
     * @param inventory
     * @return
     */
    OperaResult inventoryUpdate(InventoryMpus inventory) ;
}
