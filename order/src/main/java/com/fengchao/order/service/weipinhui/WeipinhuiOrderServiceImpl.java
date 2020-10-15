package com.fengchao.order.service.weipinhui;

import com.fengchao.order.bean.*;
import com.fengchao.order.dao.OrderDetailDao;
import com.fengchao.order.dao.OrdersDao;
import com.fengchao.order.dao.WeipinhuiAddressDao;
import com.fengchao.order.feign.EquityServiceClient;
import com.fengchao.order.feign.ProductService;
import com.fengchao.order.model.*;
import com.fengchao.order.rpc.AoyiRpcService;
import com.fengchao.order.rpc.extmodel.weipinhui.*;
import com.fengchao.order.utils.AlarmUtil;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WeipinhuiOrderServiceImpl implements WeipinhuiOrderService {

    private AoyiRpcService aoyiRpcService;

    private ProductService productService;

    private EquityServiceClient equityService;

    private WeipinhuiAddressDao weipinhuiAddressDao;

    private OrderDetailDao orderDetailDao;

    private OrdersDao ordersDao;

    @Autowired
    public WeipinhuiOrderServiceImpl(AoyiRpcService aoyiRpcService,
                                     ProductService productService,
                                     EquityServiceClient equityService,
                                     WeipinhuiAddressDao weipinhuiAddressDao,
                                     OrderDetailDao orderDetailDao,
                                     OrdersDao ordersDao) {
        this.aoyiRpcService = aoyiRpcService;
        this.productService = productService;
        this.equityService = equityService;
        this.weipinhuiAddressDao = weipinhuiAddressDao;
        this.orderDetailDao = orderDetailDao;
        this.ordersDao = ordersDao;
    }

    @Override
    public OperaResponse<String> queryItemInventory(String itemId, String skuId, Integer num, String divisionCode) {
        return null;
    }

    /**
     * {
     * "orderNo": "testorderno222",
     * "amount": "30.1",
     * "freight": "1.1",
     * "items": [
     * {
     * "subOrderNo": "testsuborderno222",
     * "itemId": "30007551",
     * "skuId": "30012085",
     * "number": 1,
     * "prodPrice": "29",
     * "subAmount": "29"
     * }
     * ],
     * "deliveryAddress": {
     * "divisionCode": "8",
     * "fullName":"荆涛测试",
     * "mobile": "13488689297",
     * "addressDetail": "北京是朝阳区建外soho西区11-3006"
     * }
     * }
     *
     * @param orderParamBean
     * @param orderMerchantBean
     * @param coupon
     * @param inventories
     */
    @Override
    public OperaResponse<String> renderOrder(OrderParamBean orderParamBean, OrderMerchantBean orderMerchantBean,
                                             OrderCouponBean coupon, List<InventoryMpus> inventories) {
        // 返回值
        OperaResponse<String> rpcResult = null;
        try {
            // 1.订单信息
            AoyiRenderOrderRequest aoyiRenderOrderRequest = new AoyiRenderOrderRequest();
            if (!"1001".equals(orderParamBean.getCompanyCustNo()) && !"1002".equals(orderParamBean.getCompanyCustNo())) {
                aoyiRenderOrderRequest.setSourceCode("ZhCityVip");
            }
            aoyiRenderOrderRequest.setOrderNo(orderMerchantBean.getTradeNo()); // 订单号
            // aoyiRenderOrderRequest.setAmount(); // 订单总金额(不 含运费) 单位元
            aoyiRenderOrderRequest.setFreight(String.valueOf(orderMerchantBean.getServFee())); // 运费 单位元

            // 2.组装sku
            List<OrderDetailX> orderDetailXList = orderMerchantBean.getSkus();
            List<AoyiItem> items = new ArrayList<>();
            BigDecimal amount = new BigDecimal(0); //  订单总金额(不 含运费) 单位元
            int index = 0;
            for (OrderDetailX orderDetailX : orderDetailXList) {
                AoyiItem aoyiItem = new AoyiItem();

                // aoyiItem.setSubOrderNo(orderDetailX.getSubOrderId()); // 子订单号
                aoyiItem.setSubOrderNo(orderMerchantBean.getTradeNo() + String.format("%03d", ++index));

                aoyiItem.setItemId(orderDetailX.getMpu()); // spu
                aoyiItem.setSkuId(orderDetailX.getSkuId());
                aoyiItem.setNumber(orderDetailX.getNum()); // 商品数量
                aoyiItem.setProdPrice(orderDetailX.getSalePrice().toPlainString()); // 商品销售单价 , 凤巢的销售价格
                aoyiItem.setCentPrice(orderDetailX.getSprice().toPlainString()); // 成本价
                String subAmount = orderDetailX.getSalePrice()
                        .multiply(new BigDecimal(orderDetailX.getNum())).toPlainString(); // 子订单金额
                aoyiItem.setSubAmount(subAmount); // 子订单金额

                amount = amount.add(new BigDecimal(subAmount));
                items.add(aoyiItem);
            }

            //
            aoyiRenderOrderRequest.setAmount(amount.toPlainString()); // 订单总金额(不 含运费) 单位元
            aoyiRenderOrderRequest.setItems(items); // 商品信息

            // 3.处理地址信息
            // 获取唯品会地址(3级)的code
            WeipinhuiAddress weipinhuiAddress =
                    weipinhuiAddressDao.selectBySuningAddress(orderParamBean.getCountyId(), orderParamBean.getCityId(), 3);
            if (weipinhuiAddress != null && StringUtils.isNotBlank(weipinhuiAddress.getSnCode())) {
                AoyiDeliverAddress aoyiDeliverAddress = new AoyiDeliverAddress();

                //
                aoyiDeliverAddress.setDivisionCode(weipinhuiAddress.getWphCode()); // 地址 Code 只能识别区级别地址代码 3级地址
                aoyiDeliverAddress.setFullName(orderParamBean.getReceiverName()); // 收件人名称
                aoyiDeliverAddress.setMobile(orderParamBean.getMobile()); // 收件人手机号
                aoyiDeliverAddress.setAddressDetail(orderParamBean.getAddress()); // 收件人详细地址

                //
                aoyiRenderOrderRequest.setDeliveryAddress(aoyiDeliverAddress);

                // 4. 执行rpc请求
                log.info("唯品会预下单 组装AoyiRenderOrderRequest:{}", JSONUtil.toJsonStringWithoutNull(aoyiRenderOrderRequest));
                rpcResult = aoyiRpcService.weipinhuiRend(aoyiRenderOrderRequest);
            } else {
                log.warn("未找到对应的唯品会地址 sncode: {} snpcode:{} level:3", orderParamBean.getTownId(), orderParamBean.getCityId());
                rpcResult.setCode(500);
                rpcResult.setMsg("未找到对应的唯品会地址 sncode:" + orderParamBean.getTownId() + "snpcode:" + orderParamBean.getCityId() + "level:3");
            }

            // 5. 回滚
            if (rpcResult.getCode() != 200) {
                if (coupon != null) {
                    boolean couponRelease = release(coupon.getId(), coupon.getCode());
                    if (!couponRelease) {
                        // 订单失败,释放优惠券，
                        log.warn("唯品会预下单失败; " + orderParamBean.getTradeNo() + "释放优惠券失败");
                    }
                }

                // 回滚库存
                if (inventories != null && inventories.size() > 0) {
                    log.warn("唯品会预下单失败; 回滚库存，入参:{}", JSONUtil.toJsonString(inventories));
                    OperaResult inventoryAddResult = productService.inventoryAdd(inventories);
                    log.warn("唯品会预下单失败; 回滚库存, 返回结果:{}", JSONUtil.toJsonString(inventoryAddResult));
                }

                throw new Exception(rpcResult.getMsg());
            }
        } catch (Exception e) {

            AlarmUtil.alarmAsync("唯品会预下单失败", e.getMessage());

            log.error("唯品会预下单失败:{}", e.getMessage(), e);

            rpcResult.setMessage(e.getMessage());
            rpcResult.setCode(500);
        }

        log.info("唯品会预下单 WeipinhuiOrderServiceImpl#renderOrder 返回:{}", JSONUtil.toJsonString(rpcResult));

        return rpcResult;
    }

    @Override
    public OperaResponse<String> createOrder(List<Integer> orderIds) {
        OperaResponse<String> rpcResult = new OperaResponse();
        rpcResult.setCode(200);

        try {
            // 1. 筛选出唯品会的主订单ID
            List<OrderDetail> orderDetailList = orderDetailDao.selectWeipinhuiOrderDetails(orderIds);

            if (CollectionUtils.isNotEmpty(orderDetailList)) { // 如果有唯品会的单子
                Integer orderId = orderDetailList.get(0).getOrderId(); // 主订单id

                // 查询主订单
                Orders orders = ordersDao.selectByPrimaryKey(orderId);
                if (orders != null) {
                    AoyiConfirmOrderRequest aoyiConfirmOrderRequest = new AoyiConfirmOrderRequest();
                    aoyiConfirmOrderRequest.setOrderNo(orders.getTradeNo());

                    log.info("唯品会确认订单 发起rpc调用 入参:{}", JSONUtil.toJsonStringWithoutNull(aoyiConfirmOrderRequest));

                    // 2. 发起rpc调用
                    rpcResult = aoyiRpcService.weipinhuiCreateOrder(aoyiConfirmOrderRequest);
                } else {
                    log.warn("唯品会确认订单 根据{}没有找到主订单", orderId);

                    throw new Exception("唯品会确认订单 根据" + orderId + "没有找到主订单");
                }
            } else {
                log.warn("唯品会确认订单 无唯品会订单 无需处理");
            }
        } catch (Exception e) {
            log.error("唯品会确认订单异常");

            rpcResult.setCode(500);
            rpcResult.setMsg(e.getMessage());
        }

        return rpcResult;
    }

    //======================================================== private ===========================================

    private boolean release(int id, String code) {
        CouponUseInfoBean bean = new CouponUseInfoBean();
        bean.setUserCouponCode(code);
        bean.setId(id);
        OperaResult result = equityService.release(bean);
        if (result.getCode() == 200) {
            return true;
        }
        return false;
    }

}
