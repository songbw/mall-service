package com.fengchao.order.service;

import com.fengchao.order.bean.vo.ExportExpressFeeVo;
import com.fengchao.order.bean.vo.ExportLoanSettlementVo;
import com.fengchao.order.bean.vo.ExportMerchantReceiptVo;
import com.fengchao.order.bean.vo.ExportReceiptBillVo;
import com.fengchao.order.constants.PaymentTypeEnum;

import java.util.Date;
import java.util.List;

public interface ExportStatisticService {

    /**
     * 导出商户货款结算表
     *
     * @param startTime
     * @param endTime
     * @param appIdList
     * @param merchantId
     * @return
     * @throws Exception
     */
    ExportLoanSettlementVo exportSettlement(Date startTime, Date endTime,
                                            List<String> appIdList, Integer merchantId) throws Exception;

    /**
     * 导出运费实际收款报表
     *
     * @param startTime
     * @param endTime
     * @param appIdList
     * @return
     * @throws Exception
     */
    List<ExportExpressFeeVo> exportExpressFee(Date startTime, Date endTime, List<String> appIdList) throws Exception;

    /**
     * 导出供应商发票
     *
     * @param startTime
     * @param endTime
     * @param appIdList
     * @param merchantId
     * @return
     * @throws Exception
     */
    List<ExportMerchantReceiptVo> exportMerchantReceipt(Date startTime, Date endTime,
                                                               List<String> appIdList, Integer merchantId) throws Exception;
}
