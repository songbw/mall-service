package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.bean.vo.ProductExportResVo;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexWithBLOBs;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.model.StarSku;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AdminProdService {
    PageBean findProdList(Integer offset, Integer limit, String state, Integer merchantId);
    PageInfo<AoyiProdIndexWithBLOBs> findProdListV2(ProductQueryBean queryBean);

    PageBean selectNameList(SerachBean bean);

    PageInfo<AoyiProdIndexX> selectNameListV2(ProductQueryBean queryBean);

    /**
     * 分页查询商品列表
     *
     * @param bean
     * @return
     */
    PageBean selectProductListPageable(SerachBean bean);

    int getProdListToRedis();

    /**
     * 创建商品
     *
     * @param bean
     * @return
     * @throws ProductException
     */
    String add(AoyiProdIndexWithBLOBs bean) throws ProductException;

    OperaResult update(AoyiProdIndexWithBLOBs bean) throws ProductException;

    OperaResponse updateBatchPriceAndState(List<AoyiProdIndex> bean) throws ProductException;

    void delete(Integer merchantId, Integer id) throws ProductException;

    PageBean findProdAll(QueryProdBean bean, String appId);

    /**
     * 获取商品导出数据
     *
     * @param bean
     * @return
     * @throws Exception
     */
    List<ProductExportResVo> exportProductList(SerachBean bean) throws Exception;

    /**
     * 获取商品价格导出数据
     *
     * @param floorPriceRate
     * @return
     * @throws Exception
     */
    PageBean exportProductPriceList(float floorPriceRate, int pageNo, int pageSize) throws Exception;

    /**
     * 修改库存
     * @param inventory
     * @return
     */
    OperaResult inventoryUpdate(InventoryMpus inventory) ;

    void fix();

    OperaResponse updateSkuPriceAndState(StarSku bean);
    OperaResponse batchUpdateSkuPriceAndState(List<StarSku> beans);

    OperaResponse updateSpuState(List<AoyiProdIndex> beans);

    /**
     * 批量更新
     * @param beans
     * @return
     */
    OperaResponse updateBatch(List<AoyiProdIndex> beans);

    AoyiProdIndexX findByMpu(String mpu) ;

    OperaResponse updateBatchStateByMerchantId(AoyiProdIndex prodIndex);

    OperaResponse updateMerchantSortByMerchantId(AoyiProdIndex prodIndex);
}
