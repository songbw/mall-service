package com.fengchao.pingan;

import com.fengchao.pingan.bean.CreatePaymentOrderRequestBean;
import com.fengchao.pingan.bean.OrderRefundRequestBean;
import com.fengchao.pingan.bean.QueryPaymentOrderRequestBean;
import com.fengchao.pingan.service.PaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PinganApplicationTests {

	@Autowired
	private PaymentService paymentService ;

	@Test
	public void contextLoads() {
//		CreatePaymentOrderRequestBean paymentBean = new CreatePaymentOrderRequestBean() ;
//		paymentBean.setAmount(1000l);
//		paymentBean.setGoodsName("testtesstsetst");
//		paymentBean.setMemberNo("2041900115127");
//		paymentBean.setMerchantNo("41219001128");
//		paymentBean.setMchOrderNo("sdsfdsda12344551");
//		paymentBean.setNotifyUrl("https://api.weesharing.com/back");
//		paymentService.createPaymentOrder(paymentBean) ;

		QueryPaymentOrderRequestBean paymentOrderRequestBean = new QueryPaymentOrderRequestBean();
		paymentOrderRequestBean.setMchOrderNo("sdsfdsda12344551");
		paymentService.queryPaymentOrder(paymentOrderRequestBean) ;

		OrderRefundRequestBean orderRefundRequestBean = new OrderRefundRequestBean() ;
		orderRefundRequestBean.setOriOrderNo("120419001151271575018838");
		orderRefundRequestBean.setRefundAmt(1000l);
		orderRefundRequestBean.setRefundMchOrderNo("sdsfdsda123445511");
		paymentService.orderRefund(orderRefundRequestBean) ;
	}

}
