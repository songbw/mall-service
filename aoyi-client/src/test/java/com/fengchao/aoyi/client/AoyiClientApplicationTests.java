package com.fengchao.aoyi.client;

import com.fengchao.aoyi.client.bean.OperaResponse;
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
		queryBean.setPageSize(100);
//		queryBean.setStartTime("2018-07-05 16:43:35");
//		queryBean.setEndTime("2019-07-06 16:43:35");
		OperaResponse response = productStarService.getSpuIdList(queryBean) ;
		productStarService.getSpuDetail("116997,116998,117004") ;
		productStarService.getSkuListDetailBySpuId("116997") ;
		productStarService.findBrandList(queryBean) ;
		productStarService.findProdCategory(null) ;
	}

}
