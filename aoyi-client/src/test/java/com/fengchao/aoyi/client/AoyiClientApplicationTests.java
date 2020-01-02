package com.fengchao.aoyi.client;

import com.fengchao.aoyi.client.bean.QueryBean;
import com.fengchao.aoyi.client.startService.ProductStarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AoyiClientApplicationTests {

	@Autowired
	private ProductStarService productStarService;

	@Test
	public void contextLoads() {
		QueryBean queryBean = new QueryBean() ;
		queryBean.setPageNo(1);
		queryBean.setPageSize(3);
		queryBean.setStartTime("2017-01-01 00:00:01");
		queryBean.setEndTime("2018-01-01 00:00:01");
		productStarService.getSpuIdList(queryBean) ;
	}

}
