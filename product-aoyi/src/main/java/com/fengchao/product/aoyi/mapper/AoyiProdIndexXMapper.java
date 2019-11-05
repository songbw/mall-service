package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.bean.QueryProdBean;
import com.fengchao.product.aoyi.bean.vo.ProductExportResVo;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.model.AoyiProdIndexXWithBLOBs;

import java.util.HashMap;
import java.util.List;

public interface AoyiProdIndexXMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AoyiProdIndexXWithBLOBs record);

    int insertSelective(AoyiProdIndexX record);

    AoyiProdIndexXWithBLOBs selectByPrimaryKey(Integer id);

    AoyiProdIndexX selectBySkuId(String skuId);

    AoyiProdIndexX selectByMpu(String sku);

    int updateByPrimaryKeySelective(AoyiProdIndexX record);

    int updateByPrimaryKeyWithBLOBs(AoyiProdIndexXWithBLOBs record);

    int updateByPrimaryKey(AoyiProdIndexX record);

    int selectLimitCount(HashMap map) ;

    List<AoyiProdIndexX> selectLimit(HashMap map) ;

    List<AoyiProdIndexX> selectAll(HashMap map) ;

    List<AoyiProdIndexX> selectSearchLimit(HashMap map) ;

    int selectSearchCount(HashMap map) ;

    int selectPriceCount(HashMap map) ;

    List<AoyiProdIndexX> selectProdAll();

    int selectSkuByCouponIdCount(QueryProdBean bean);

    List<AoyiProdIndexX> selectSkuByCouponIdLimit(QueryProdBean bean);

    /**
     * 分页查询商品列表
     *
     * @param map
     * @return
     */
    List<AoyiProdIndexX> selectProductListPageable(HashMap map);

    List<ProductExportResVo> selectProductPriceListPageable(HashMap map) ;

    AoyiProdIndexX selectForUpdateByMpu(String mpu);

    int batchUpdate(AoyiProdIndexX prodIndex);
}