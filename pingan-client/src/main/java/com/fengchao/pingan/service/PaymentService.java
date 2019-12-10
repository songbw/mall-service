package com.fengchao.pingan.service;

import com.fengchao.pingan.bean.*;
import com.fengchao.pingan.exception.PinganClientException;

public interface PaymentService {

    PaymentResult paymentOrder(PaymentBean paymentBean) throws PinganClientException;

    OperaResponse<CreatePaymentOrderBean> createPaymentOrder(CreatePaymentOrderRequestBean paymentBean);

    PaymentResult payRefund(RefundBean bean) throws PinganClientException ;

    PaymentResult wsPayClient(PaymentBean paymentBean);

    OperaResponse<QueryPaymentOrderBean> queryPaymentOrder(QueryPaymentOrderRequestBean paymentBean);

    OperaResponse<OrderRefundBean> orderRefund(OrderRefundRequestBean paymentBean);

    String backNotify(BackNotifyRequestBean paymentBean);

}
