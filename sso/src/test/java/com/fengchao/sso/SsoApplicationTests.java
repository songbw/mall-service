package com.fengchao.sso;

import com.fengchao.sso.bean.*;
import com.fengchao.sso.mapper.BalanceMapper;
import com.fengchao.sso.mapper.BalanceXMapper;
import com.fengchao.sso.service.IBalanceService;
import com.fengchao.sso.service.ILoginService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SsoApplicationTests {

	@Autowired
	private ILoginService loginService ;
	@Autowired
	private BalanceXMapper balanceMapper ;

	@Ignore
	@Test
	public void contextLoads() {
		ThirdLoginBean thirdLoginBean = new ThirdLoginBean();
		thirdLoginBean.setOpenId("5560e8d51466447ca6cdaeabc84bed1f");
		thirdLoginBean.setiAppId("11");
		TokenBean tokenBean = loginService.thirdLogin(thirdLoginBean) ;
	}

	@Ignore
	@Test
	public void stringTest() {
		BalanceQueryBean queryBean = new BalanceQueryBean() ;
		queryBean.setStart("2019-10-23"); ;
		queryBean.setEnd("2019-11-25");
		queryBean.setBalanceId(2402);
		queryBean.setType(0);
		int refundSum = balanceMapper.selectSumSaleAmountByTypeAndBalanceIdAndCreatedAt(queryBean) ;
		System.out.println(refundSum);
//		List<BalanceSumBean> balanceSumBeans = new ArrayList<>() ;
//		List<BalanceSumBean> initBalances = balanceMapper.selectInitAmount(queryBean) ;
//		initBalances.forEach(sumBean -> {
//			//  根据ID查询充值总额
//			queryBean.setType(2);
//			Integer chargeSum = balanceMapper.selectSumSaleAmountByTypeAndBalanceIdAndCreatedAt(queryBean) ;
//			if (chargeSum != null) {
//				sumBean.setChargeAmount(chargeSum);
//			}
//			//  根据ID查询支付总额
//			queryBean.setType(0);
//			Integer paymentSum  = balanceMapper.selectSumSaleAmountByTypeAndBalanceIdAndCreatedAt(queryBean) ;
//			if (paymentSum != null) {
//				sumBean.setPaymentAmount(paymentSum);
//			}
//			//  根据ID查询退款总额
//			queryBean.setType(1);
//			Integer refundSum = balanceMapper.selectSumSaleAmountByTypeAndBalanceIdAndCreatedAt(queryBean) ;
//			if (refundSum != null) {
//				sumBean.setRefundAmount(refundSum);
//			}
//			balanceSumBeans.add(sumBean) ;
//		});
	}

}
