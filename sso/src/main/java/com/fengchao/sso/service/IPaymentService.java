package com.fengchao.sso.service;

import com.fengchao.sso.bean.BackRequest;
import com.fengchao.sso.bean.PaymentBean;
import com.fengchao.sso.util.OperaResult;

public interface IPaymentService {

    OperaResult payment(PaymentBean paymentBean) ;

    String back(BackRequest beanRequest) ;

}
