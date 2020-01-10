package com.fengchao.order.service.impl;

import com.fengchao.order.bean.bo.OrderDetailBo;
import com.fengchao.order.bean.bo.OrdersBo;
import com.fengchao.order.bean.bo.UserOrderBo;
import com.fengchao.order.bean.vo.ExportShareProfitVo;
import com.fengchao.order.constants.PaymentTypeEnum;
import com.fengchao.order.constants.SettlementTypeEnum;
import com.fengchao.order.service.AdminShareProfitService;
import com.fengchao.order.service.SettlementAssistService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AdminShareProfitServiceImpl implements AdminShareProfitService {

    private SettlementAssistService settlementAssistService;

    @Autowired
    public AdminShareProfitServiceImpl(SettlementAssistService settlementAssistService) {
        this.settlementAssistService = settlementAssistService;
    }

    @Override
    public List<ExportShareProfitVo> exportShareProfit(Date startTime, Date endTime, String appId) {
        List<UserOrderBo> mergedUserOrderBoList = settlementAssistService.mergeIncomeAndRefundUserOrder(startTime, endTime, appId);
        settlementAssistService.assignPaymentAmout(mergedUserOrderBoList);

        if (CollectionUtils.isEmpty(mergedUserOrderBoList)) {
            log.warn("导出分润表 获取出账和入账的用户单为空");
            return Collections.emptyList();
        }

        // 按照结算类型聚合子订单 key: 结算类型(0：普通类结算， 1：秒杀类结算， 2：精品类结算)， value: List<OrderDetailBo>
        Map<Integer, List<OrderDetailBo>> orderDetailBoMap = new HashMap<>();

        // 遍历所有子订单
        for (UserOrderBo userOrderBo : mergedUserOrderBoList) {
            List<OrdersBo> ordersBoList = userOrderBo.getMerchantOrderList();
            for (OrdersBo ordersBo : ordersBoList) {
                List<OrderDetailBo> orderDetailBoList = ordersBo.getOrderDetailBoList();
                for (OrderDetailBo orderDetailBo : orderDetailBoList) {
                    List<OrderDetailBo> _list = orderDetailBoMap.get(orderDetailBo.getSettlementType());
                    if (_list == null) {
                        _list = new ArrayList();

                        orderDetailBoMap.put(orderDetailBo.getSettlementType(), _list);
                    }

                    _list.add(orderDetailBo);
                }
            }
        } // end 遍历所有子订单

        // 准备导出数据
        List<ExportShareProfitVo> exportShareProfitVoList = new ArrayList<>();
        for (Integer settlementType : orderDetailBoMap.keySet()) {
            SettlementTypeEnum settlementTypeEnum = SettlementTypeEnum.getSettlementTypeEnum(settlementType); // 结算方式
            List<OrderDetailBo> _orderDetailBoList = orderDetailBoMap.get(settlementType); // 该结算方式下的所有子订单

            List<ExportShareProfitVo> _list = assembleTotalAmountInSettlementType(settlementTypeEnum, _orderDetailBoList);

            exportShareProfitVoList.addAll(_list);
        }

        return exportShareProfitVoList;
    }


    //========================================================== private ============================================

    /**
     * 计算某个指定结算类型下的所有子订单中，不同支付方式所消费的金额
     *
     * @param settlementTypeEnum 指定的结算类型
     * @param orderDetailBoList 某个指定的结算类型下的子订单集合
     * @return
     */
    private List<ExportShareProfitVo> assembleTotalAmountInSettlementType(SettlementTypeEnum settlementTypeEnum, List<OrderDetailBo> orderDetailBoList) {
        Integer balanceAmount = 0;
        Integer cardAmount = 0;
        Integer woaAmount = 0;
        Integer bankAmount = 0;

        for (OrderDetailBo orderDetailBo : orderDetailBoList) {
            balanceAmount = balanceAmount + orderDetailBo.getShareBalanceAmount();
            cardAmount = cardAmount + orderDetailBo.getShareCardAmount();
            woaAmount = woaAmount + orderDetailBo.getShareWoaAmount();
            bankAmount = bankAmount + orderDetailBo.getShareBankAmount();

        }

        List<ExportShareProfitVo> exportShareProfitVoList = new ArrayList<>();

        // 余额
        ExportShareProfitVo balanceExportVo = new ExportShareProfitVo();
        balanceExportVo.setAmout(balanceAmount);
        balanceExportVo.setSettlementTypeEnum(settlementTypeEnum);
        balanceExportVo.setPaymentTypeEnum(PaymentTypeEnum.BALANCE);

        exportShareProfitVoList.add(balanceExportVo);

        // 市民卡
        ExportShareProfitVo cardExportVo = new ExportShareProfitVo();
        cardExportVo.setAmout(cardAmount);
        cardExportVo.setSettlementTypeEnum(settlementTypeEnum);
        cardExportVo.setPaymentTypeEnum(PaymentTypeEnum.CARD);

        exportShareProfitVoList.add(cardExportVo);

        // 联机账户
        ExportShareProfitVo woaExportVo = new ExportShareProfitVo();
        woaExportVo.setAmout(woaAmount);
        woaExportVo.setSettlementTypeEnum(settlementTypeEnum);
        woaExportVo.setPaymentTypeEnum(PaymentTypeEnum.WOA);

        exportShareProfitVoList.add(woaExportVo);

        // 联机账户
        ExportShareProfitVo bankExportVo = new ExportShareProfitVo();
        bankExportVo.setAmout(bankAmount);
        bankExportVo.setSettlementTypeEnum(settlementTypeEnum);
        bankExportVo.setPaymentTypeEnum(PaymentTypeEnum.BANK);

        exportShareProfitVoList.add(bankExportVo);

        return exportShareProfitVoList;
    }




}
