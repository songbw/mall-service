package com.fengchao.order.service;

import com.fengchao.order.bean.vo.ExportReceiptBillVo;
import com.fengchao.order.constants.ReceiptTypeEnum;

import java.util.Date;
import java.util.List;

public interface AdminInvoiceService {

    /**
     * 导出商品开票信息
     *
     * @param startTime
     * @param endTime
     * @param appId
     * @param receiptTypeEnum 发票类型
     * @return
     * @throws Exception
     */
    List<ExportReceiptBillVo> exportInvoice(Date startTime, Date endTime,
                                                       String appId, ReceiptTypeEnum receiptTypeEnum) throws Exception;
}
