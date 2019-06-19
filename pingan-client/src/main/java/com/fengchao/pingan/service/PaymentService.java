package com.fengchao.pingan.service;

import com.fengchao.pingan.bean.PaymentBean;
import com.fengchao.pingan.bean.PaymentResult;
import com.fengchao.pingan.bean.RefundBean;
import com.fengchao.pingan.exception.PinganClientException;

public interface PaymentService {

    PaymentResult paymentOrder(PaymentBean paymentBean) throws PinganClientException;

    PaymentResult payRefund(RefundBean bean) throws PinganClientException ;

}
