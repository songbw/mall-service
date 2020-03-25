package com.fengchao.order.service;

import com.fengchao.order.bean.vo.ExportLoanSettlementVo;
import com.fengchao.order.bean.vo.ExportReceiptBillVo;
import com.fengchao.order.constants.PaymentTypeEnum;

import java.util.Date;
import java.util.List;

public interface ExportStatisticService {

    /**
     * 导出货款结算表
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
}
