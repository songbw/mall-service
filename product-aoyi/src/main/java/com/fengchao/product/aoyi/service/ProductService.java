package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.model.AoyiProdIndexXWithBLOBs;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 商品服务
 */
public interface ProductService {

    PageBean findList(ProductQueryBean queryBean) throws ProductException;

    PageInfo<ProductInfoBean> findListByCategories(ProductQueryBean queryBean) throws ProductException;

    OperaResult findPrice(PriceQueryBean queryBean) throws ProductException ;

    OperaResult findInventory(InventoryQueryBean queryBean) ;

    List<FreightFareBean> findCarriage(CarriageQueryBean queryBean) throws ProductException ;

    AoyiProdIndexX find(String id) throws ProductException ;

    List<AoyiProdIndexX> findAll() throws ProductException ;

    ProductInfoBean findAndPromotion(String mpu, String appId) throws ProductException;

    AoyiProdIndexXWithBLOBs findByMpu(String mpu) ;

    /**
     * 根据mpuId集合查询product列表
     *
     * @param mpuIdList
     * @return
     */
    List<ProductInfoBean> queryProductListByMpuIdList(List<String> mpuIdList) throws Exception;

    List<AoyiProdIndexX> selectProductListByMpuIdList(List<String> mpuIdList) throws Exception ;

    /**
     * 关爱通价格查询
     * @param queryBean
     * @return
     * @throws ProductException
     */
    OperaResult findPriceGAT(PriceQueryBean queryBean) throws ProductException ;

    OperaResponse search(ProductQueryBean queryBean) ;

    List<AoyiProdIndex> getProdsByMpus(List<String> mpuIdList);

    /**
     * 查询自营库存
     * @param queryBean
     * @return
     */
    OperaResult findInventorySelf(InventorySelfQueryBean queryBean) ;

    /**
     * 批量减库存
     * @param inventories
     * @return
     */
    OperaResult inventorySub(List<InventoryMpus> inventories) ;

    OperaResult inventoryAdd(List<InventoryMpus> inventories) ;

    List<AoyiProdIndexX> selectProductListByMpuIdListAndCode(List<AoyiProdIndex> bean) ;

}
