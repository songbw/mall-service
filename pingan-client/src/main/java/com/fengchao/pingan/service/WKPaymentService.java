package com.fengchao.pingan.service;

import com.fengchao.pingan.bean.*;

/**
 * @author song
 * @2020-02-26 4:51 下午
 **/
public interface WKPaymentService {

    WKOperaResponse<WKRefund> refundApply(WKRefundRequestBean bean);

    String paymentNotify(WKPaymentNotifyRequestBean bean) ;

    String refundNotify(WKRefundNotifyRequestBean bean) ;

}
