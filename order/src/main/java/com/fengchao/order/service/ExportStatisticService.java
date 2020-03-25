package com.fengchao.order.service;

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
     * @param appId
     * @param merchantId
     * @return
     * @throws Exception
     */
    List<ExportReceiptBillVo> exportSettlement(Date startTime, Date endTime,
                                            String appId, String merchantId) throws Exception;
}
