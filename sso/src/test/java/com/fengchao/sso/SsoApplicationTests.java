package com.fengchao.sso;

import com.fengchao.sso.bean.ThirdLoginBean;
import com.fengchao.sso.bean.TokenBean;
import com.fengchao.sso.service.ILoginService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SsoApplicationTests {

	@Autowired
	private ILoginService loginService ;

	@Ignore
	@Test
	public void contextLoads() {
		ThirdLoginBean thirdLoginBean = new ThirdLoginBean();
		thirdLoginBean.setOpenId("5560e8d51466447ca6cdaeabc84bed1f");
		thirdLoginBean.setiAppId("11");
		TokenBean tokenBean = loginService.thirdLogin(thirdLoginBean) ;
	}

}
