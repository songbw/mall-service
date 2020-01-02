package com.fengchao.aoyi.client.startService;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.bean.QueryBean;
import com.fengchao.aoyi.client.starBean.AddressInfoQueryBean;
import com.fengchao.aoyi.client.starBean.HoldSkuInventoryQueryBean;
import com.fengchao.aoyi.client.starBean.InventoryQueryBean;
import com.fengchao.aoyi.client.starBean.ReleaseSkuInventoryQueryBean;

/**
 * @author songbw
 * @date 2020/1/2 15:01
 */
public interface ProductStarService {

    /**
     * 同步商品获取商品SpuId列表
     * @return
     */
    OperaResponse getSpuIdList(QueryBean bean) ;

    /**
     * 根据SPUID 查询SPU详情
     * spuIds 一次最多请求50个spu，超过按50个算，用英文逗号分隔
     * @return
     */
    OperaResponse getSpuDetail(String spuIds) ;

    /**
     * 根据SpuId  查询SKU列表明细
     * @return
     */
    OperaResponse getSkuListDetailBySpuId(String spuId) ;

    /**
     * 查询品牌列表
     * @param bean 当前页 默认第一页
     * @param bean  每页大小，默认 100 最大500
     * @return
     */
    OperaResponse findBrandList(QueryBean bean) ;

    /**
     * 查询分类信息
     * @param categoryId  分类ID,不传则查询所有一级分类,反之则查询该分类下的子分类信息
     * @return
     */
    OperaResponse findProdCategory(String categoryId) ;

    /**
     * 查询商品库存
     * @param bean codes 必填, 商品ID，多个以英文逗号分隔
     * @return
     */
    OperaResponse findSkuInventory(InventoryQueryBean bean) ;

    /**
     * 查询商品价格
     * @param codes 必填, 最多查200个，多个以英文逗号拼接
     * @return
     */
    OperaResponse findSkuSalePrice(String codes) ;

    /**
     * 查询地址信息
     * @param bean
     * @return
     */
    OperaResponse getAddressInfo(AddressInfoQueryBean bean) ;

    /**
     * 预占商品库存
     * @param bean
     * codeInvList   必填 商品及库存信息json [{\"code\":\"SL-ECP-6072\",\"quantity\":\"3\"}]
     * @return
     */
    OperaResponse preHoldSkuInventory(HoldSkuInventoryQueryBean bean) ;

    /**
     * 释放预占商品库存
     * @param bean
     * codeInvList   必填
     * @return
     */
    OperaResponse releaseSkuInventory(ReleaseSkuInventoryQueryBean bean) ;
}
