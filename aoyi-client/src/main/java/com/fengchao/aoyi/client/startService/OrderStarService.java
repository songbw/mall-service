package com.fengchao.aoyi.client.startService;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.bean.StarBackBean;
import com.fengchao.aoyi.client.starBean.*;

/**
 * @author songbw
 * @date 2020/1/2 15:01
 */
public interface OrderStarService {

    /**
     * 简要描述： 订单下单，下单失败时可能是因为库存不足，或者商品下架
     * 1：【必须先预占库存才能下单】
     * 2：【预占库存到下单到我方，需在30分内，超时则预占库存失效，需重新生成订单，预占库存下单】
     * @param bean
     * @return
     */
    OperaResponse addOrder(StarOrderBean bean) ;

    /**
     * 确认收货
     * 买家确认收货通知我方，只有在订单状态为已发货的情况下，才允许发起，其他状态不允许
     * @param orderSn
     * @return
     */
    OperaResponse confirmOrder(String orderSn) ;

    /**
     * 退货退款申请
     * 【卖家已发货，使用退货退款申请接口，针对单个商品全部退】
     * @param bean
     * @return
     */
    OperaResponse applyRefundAndGoods(ApplyRefundAndGoodsBean bean) ;

    /**
     * 退款申请
     * 【卖家未发货，使用退款申请接口】
     * @param bean
     * @return
     */
    OperaResponse applyRefund(ApplyRefundAndGoodsBean bean) ;

    /**
     * 订单运费查询
     * @param bean
     * @return
     */
    OperaResponse getOrderFreight(FreightBean bean) ;

    /**
     * 取消退货退款申请
     * @param bean
     * @return
     */
    OperaResponse cancelApplyRefund(CancelApplyRefundBean bean) ;

    /**
     * 查询物流信息
     * @param orderSn
     * @return
     */
    OperaResponse findExpressInfoByOrderSn(String orderSn) ;

    /**
     * 根据星链子订单编号  查询订单
     * @param orderSn
     * @return
     */
    OperaResponse findOrderByOrderSn(String orderSn) ;

    /**
     * 查询物流公司信息
     * @return
     */
    OperaResponse getLogisticsCompanyList() ;

    /**
     * 查询退货退款状态
     * -- 退款返回状态  及状态名称
     * (1,"申请中"),
     * (2,"审核不通过"),
     * (3,"待平台退款"),
     * (6,"退款处理中"),
     * (7,"退款成功"),
     * (8,"退款关闭"),     -- 买家发起退货退款，又取消了退货退款   确认收货后，7天内，还可以再发起退款
     * (9,"平台驳回"),
     * (10,"超时未修改自动关闭"),  --买家发起退货，商家审核通过，3天内买家未发货，则是此状态
     * (11,"已发货无法退款"),
     * (12,"财务审核不通过"),
     * (13,"财务审核通过");
     * --退货退款状态  及状态名称
     * (1,"申请中"),
     * (2,"审核不通过"),
     * (3,"供应商审核通过"),
     * (4,"买家已发货"),
     * (5,"待平台退款"),       --账期用户不涉及
     * (6,"退款处理中"),       --账期用户不涉及
     * (7,"退款成功"),     -- 退货退款成功，结束
     * (8,"退款关闭"),     -- 买家发起退货退款，又取消了退货退款   确认收货后，7天内，还可以再发起退款
     * (9,"平台驳回"),
     * (10,"超时未修改自动关闭"),  --买家发起退货，商家审核通过，3天内买家未发货，则是此状态
     * (12,"财务审核不通过"),
     * (13,"财务审核通过");
     * @param serviceSn
     * @return
     */
    OperaResponse getReturnOrderStatuts(String serviceSn) ;

    /**
     * 买家发货
     * 卖家审核通过后，买家发货,物流公司需要按上面那个接口的数据提供
     * @param bean
     * @return
     */
    OperaResponse getReturnOrderStatutsNotify(ReturnOrderGoodsBean bean) ;

    /**
     * 变更提醒类型	oldStatus	oldStatusName	     newStatus	newStatusName	updateType
     * 供应商发货	    20	   已支付待发货	            30	      已发货待收货	     1
     * 财务审核不通过	    3	   商审核通过，待平台退款	    12	      财务审核不通过	     2
     * 财务审核不通过	    5	   供应商确认收货，待平台退款   12	      财务审核不通过	     2
     * 供应商审核通过	    1	   供应商审核退款中	        3	      供应商审核通过	     2
     * 供应商审核不通过	1	   供应商审核退款中	        2	      供应商审核不通过	 2
     * 供应商确认收货	    3	   供应商审核通过	            5	      确认收货，待平台退款	 2
     * 退货关闭	        3	   供应商审核通过	            10	      退货关闭	         2
     * 财务审核通过	    3	   供应商审核通过	            13	      财务审核通过	     2
     * 财务审核通过	    5	   商家确认收货	            13	      财务审核通过	     2
     * @param bean
     * @return
     */
    String notify(StarBackBean bean) ;


}
