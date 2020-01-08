package com.fengchao.order.service.impl;

import com.fengchao.order.bean.bo.OrderDetailBo;
import com.fengchao.order.bean.bo.OrdersBo;
import com.fengchao.order.bean.bo.UserOrderBo;
import com.fengchao.order.bean.vo.ExportShareProfitVo;
import com.fengchao.order.rpc.extmodel.OrderPayMethodInfoBean;
import com.fengchao.order.service.AdminShareProfitService;
import com.fengchao.order.service.SettlementAssistService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminShareProfitServiceImpl implements AdminShareProfitService {

    private SettlementAssistService settlementAssistService;


    public ExportShareProfitVo exportShareProfit(Date startTime, Date endTime, String appId) {
        List<UserOrderBo> mergedUserOrderBoList = mergeIncomeAndRefundUserOrder(startTime, endTime, appId);

        // 分配金额

        return null;

    }

    @Override
    public List<UserOrderBo> mergeIncomeAndRefundUserOrder(Date startTime, Date endTime, String appId) {
        // 1. 查询入账和出账的用户单
        // 获取入账的用户单 TODO : 如果为空？
        List<UserOrderBo> incomeUserOrderBoList = settlementAssistService.queryIncomeUserOrderBoList(startTime, endTime, appId);
        // 获取出账的用户单 TODO : 如果为空？
        List<UserOrderBo> refundUserOrderBoList = settlementAssistService.queryRefundUserOrderBoList(startTime, endTime, appId);

        // x. 将出账的用户单转map key: paymentNo, value:UserOrderBo  TODO : 如果为空？
        Map<String, UserOrderBo> refundUserOrderBoMap = refundUserOrderBoList.stream().collect(Collectors.toMap(r -> r.getPaymentNo(), r -> r));

        // 2. 合并入账和出账的用户单，合并内容有: 金额信息、sku的数量
        List<UserOrderBo> mergedUserOrderBoList = new ArrayList<>();

        //
        for (UserOrderBo incomeUserOrderBo : incomeUserOrderBoList) { // 遍历入账用户单
            UserOrderBo mergedUserOrderBo = incomeUserOrderBo; // 合并后的

            // 2.1 获取该入账用户单关联的出账用户单
            UserOrderBo refundUserOrderBo = refundUserOrderBoMap.get(incomeUserOrderBo.getPaymentNo());
            // 设置该用户单的退款信息
            mergedUserOrderBo.setRefundMethodInfoBeanList(refundUserOrderBo == null ? null : refundUserOrderBo.getPayMethodInfoBeanList());

            // 2.2 合并入账和退款子订单的个数
            if (refundUserOrderBo != null) { // 如果该入账用户单存在退款用户单
                mergeIncomeAndRefundOrderDetailNum(mergedUserOrderBo, refundUserOrderBo);
            }

            // 2.3 合并支付和退款信息, 这个合并逻辑并没有改变原有的数据，而是新增了合并后的属性(支付方式)MergedMethodInfoBeanList
            List<OrderPayMethodInfoBean> mergedPayMethodInfoList =
                    mergePayMethodInfoBean(mergedUserOrderBo.getPayMethodInfoBeanList(), refundUserOrderBo == null ? null : refundUserOrderBo.getPayMethodInfoBeanList());
            // 设置支付(出账，入账)合并后的信息
            mergedUserOrderBo.setMergedMethodInfoBeanList(mergedPayMethodInfoList);

            // 移除退款的用户单
            if (refundUserOrderBo != null) {
                refundUserOrderBoMap.remove(incomeUserOrderBo.getPaymentNo());
            }

            //
            mergedUserOrderBoList.add(mergedUserOrderBo);
        }

        // 3. 如果还有退款用户单的信息, 那么将这些用户单加入mergedUserOrderBoList
        if (CollectionUtils.isNotEmpty(refundUserOrderBoList)) { // 如果还有出账的用户单
            for (UserOrderBo refundUserOrderBo : refundUserOrderBoMap.values()) {
                UserOrderBo mergedUserOrderBo = refundUserOrderBo; // 合并后的

                List<OrderPayMethodInfoBean> mergedPayMethodInfoList = mergePayMethodInfoBean(Collections.emptyList(), refundUserOrderBo.getRefundMethodInfoBeanList());
                mergedUserOrderBo.setMergedMethodInfoBeanList(mergedPayMethodInfoList);

                mergedUserOrderBoList.add(mergedUserOrderBo);
            }
        }


        return mergedUserOrderBoList;
    }

    //========================================================== private ============================================

    /**
     * 合并某个用户单的入账和出账的金额
     * <p>
     * 注意：这个合并逻辑并没有改变原有的数据，而是新增了合并后的属性(支付方式)MergedMethodInfoBeanList
     *
     * @param incomePayMethodList 某个用户单的入账信息 一定不为null
     * @param refundPayMehtodList 某个用户单的退款信息  可能为空(null || size:0)
     * @return
     */
    private List<OrderPayMethodInfoBean> mergePayMethodInfoBean(List<OrderPayMethodInfoBean> incomePayMethodList, List<OrderPayMethodInfoBean> refundPayMehtodList) {
        // 返回值
        List<OrderPayMethodInfoBean> mergedPayMethodInfoBeanList = new ArrayList<>();

        // 将出账信息转map key:payType(参考PaymentTypeEnum) value: OrderPayMethodInfoBean
        Map<String, OrderPayMethodInfoBean> refundPayMethodMap = CollectionUtils.isEmpty(refundPayMehtodList) ?
                Collections.emptyMap() : refundPayMehtodList.stream().collect(Collectors.toMap(r -> r.getPayType(), r -> r));

        // 合并
        for (OrderPayMethodInfoBean incomePayMehtod : incomePayMethodList) {
            OrderPayMethodInfoBean mergedOrderPayMethodInfoBean = copyOrderPayMethodInfoBean(incomePayMehtod); // incomePayMehtod;

            // 获取退款的方式信息
            OrderPayMethodInfoBean _refundPayMethodBean = refundPayMethodMap.get(incomePayMehtod.getPayType());

            // 如果存在相应的退款方式, 则从入账中减去退款
            if (_refundPayMethodBean != null) { //
                mergedOrderPayMethodInfoBean.setActPayFee(String.valueOf(Integer.valueOf(incomePayMehtod.getActPayFee()) - Integer.valueOf(_refundPayMethodBean.getActPayFee())));

                // 移除
                refundPayMethodMap.remove(incomePayMehtod.getPayType());
            }

            mergedPayMethodInfoBeanList.add(mergedOrderPayMethodInfoBean);
        }

        if (refundPayMethodMap.size() > 0) { // 如果还有退款方式， 则把这种方式放入到mergedPayMethodInfoBeanList
            for (String payType : refundPayMethodMap.keySet()) {
                OrderPayMethodInfoBean _refundPayMethodInfoBean = refundPayMethodMap.get(payType);

                OrderPayMethodInfoBean mergedOrderPayMethodInfoBean = copyOrderPayMethodInfoBean(_refundPayMethodInfoBean); // incomePayMehtod;
                mergedOrderPayMethodInfoBean.setActPayFee(String.valueOf(0 - Integer.valueOf(mergedOrderPayMethodInfoBean.getActPayFee())));

                //
                mergedPayMethodInfoBeanList.add(mergedOrderPayMethodInfoBean);
            }
        }

        return mergedPayMethodInfoBeanList;
    }

    /**
     * @param old
     * @return
     */
    private OrderPayMethodInfoBean copyOrderPayMethodInfoBean(OrderPayMethodInfoBean old) {
        OrderPayMethodInfoBean newobj = new OrderPayMethodInfoBean(); // incomePayMehtod;

        newobj.setPayType(old.getPayType());
        newobj.setOrderNo(old.getOrderNo()); // 支付订单号
        newobj.setOutTradeNo(old.getOutTradeNo()); // 订单号
        newobj.setTradeNo(old.getTradeNo()); // 联机账户订单号
        newobj.setBody(old.getBody()); // 商品描述
        newobj.setRemark(old.getRemark()); // 用户自定义
        newobj.setTotalFee(old.getTotalFee()); // 交易总金额
        newobj.setActPayFee(old.getActPayFee()); // 交易实际金额 单位 分
        newobj.setStatus(old.getStatus()); // 交易状态: 1: 成功, 2: 失败, 0: 新创建
        newobj.setTradeDate(old.getTradeDate()); // 交易时间
        newobj.setCreateDate(old.getCreateDate()); // 创建时间
        newobj.setLimitPay(old.getLimitPay()); // 支付限制
        newobj.setCardNo(old.getCardNo()); // 卡号或OpenID
        newobj.setPayer(old.getPayer()); // 手机号
        newobj.setTradeType(old.getTradeType()); // tradeType
        newobj.setAppId(old.getAppId()); // appId

        return newobj;
    }

    /**
     * 合并入账和退款子订单的个数
     *
     * @param incomeUserOrderBo 入账用户单 一定不为空
     * @param refundUserOrderBo 与入账用户单关联的出账(退款)用户单 可能为空
     */
    private void mergeIncomeAndRefundOrderDetailNum(UserOrderBo incomeUserOrderBo, UserOrderBo refundUserOrderBo) {


        // 1. 将退款的用户单以mpu为维度转为mpa key: mpu, value: OrderDetailBo
        Map<String, OrderDetailBo> refundOrderDetailBoMap = new HashMap<>();

        // 获取该退款的用户单的商户单
        List<OrdersBo> refundMerchantOrdersBoList = refundUserOrderBo.getMerchantOrderList();
        for (OrdersBo refundOrdersBo : refundMerchantOrdersBoList) { // 遍历商户单
            List<OrderDetailBo> refundOrderDetailBoList = refundOrdersBo.getOrderDetailBoList();
            for (OrderDetailBo orderDetailBo : refundOrderDetailBoList) { // 遍历订单详情
                OrderDetailBo _orderDetailBo = refundOrderDetailBoMap.get(orderDetailBo.getMpu());

                refundOrderDetailBoMap.put(_orderDetailBo.getMpu(), _orderDetailBo);
            }
        }

        // 2. 合并数量， 查看入账的子订单，是否存在在退款的子订单中，如果存在，则将数量设置为0
        for (OrdersBo incomeOrdersBo : incomeUserOrderBo.getMerchantOrderList()) { // 遍历
            for (OrderDetailBo incomeOrderDetailBo : incomeOrdersBo.getOrderDetailBoList()) {
                OrderDetailBo _orderDetailBo = refundOrderDetailBoMap.get(incomeOrderDetailBo.getMpu());
                if (_orderDetailBo != null) { // 如果有退款子订单，那么将该mpu的数量设置为0(因为退款是以mpu为维度退的)
                    incomeOrderDetailBo.setNum(0);
                }
            }
        }
    }


}
