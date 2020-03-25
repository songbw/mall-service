package com.fengchao.aoyi.client.weipinhuiService;

import com.fengchao.aoyi.client.bean.dto.weipinhui.req.AoyiConfirmOrderRequest;
import com.fengchao.aoyi.client.bean.dto.weipinhui.req.AoyiReleaseOrderRequest;
import com.fengchao.aoyi.client.bean.dto.weipinhui.req.AoyiRenderOrderRequest;
import com.fengchao.aoyi.client.bean.dto.weipinhui.res.*;

import java.util.List;

/**
 * 唯品会商品相关接口服务
 *
 */
public interface ProductWeipinhuiService {

    /**
     * 获取品牌列表
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    List<BrandResDto> getBrand(Integer pageNumber, Integer pageSize) throws Exception;

    /**
     * 获取类目列表
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    List<CategoryResDto> getCategory(Integer pageNumber, Integer pageSize) throws Exception;

    /**
     * 获取items列表
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    List<AoyiItemDetailResDto> queryItemsList(Integer pageNumber, Integer pageSize) throws Exception;

    /**
     * 根据itemId查询详情
     *
     * @param itemId
     * @return
     */
    AoyiItemDetailResDto queryItemDetial(String itemId) throws Exception;

    /**
     * 库存查询接口
     *
     * @param itemId
     * @param skuId
     * @param num 库存个数
     * @param divisionCode 地址code
     * @return
     * @throws Exception
     */
    AoyiQueryInventoryResDto queryItemInventory(String itemId, String skuId, Integer num, String divisionCode) throws Exception;

    /**
     * 预占订单接口
     *
     * @param aoyiRenderOrderRequest
     * @throws Exception
     */
    void renderOrder(AoyiRenderOrderRequest aoyiRenderOrderRequest) throws Exception;

    /**
     * 确认订单接口
     *
     * @param aoyiConfirmOrderRequest
     * @throws Exception
     */
    void createOrder(AoyiConfirmOrderRequest aoyiConfirmOrderRequest) throws Exception;

    /**
     * 取消订单接口
     *
     * @param aoyiReleaseOrderRequest
     * @throws Exception
     */
    void releaseOrder(AoyiReleaseOrderRequest aoyiReleaseOrderRequest) throws Exception;

    /**
     * 获取地址接口
     *
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<AoyiAdrressResDto> queryAddress(Integer pageNumber, Integer pageSize) throws Exception;

    /**
     * 物流查询接口
     *
     * @param subOrderNo
     * @return
     * @throws Exception
     */
    AoyiLogisticsResDto queryOrderLogistics(String subOrderNo) throws Exception;
}
