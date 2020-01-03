package com.fengchao.aoyi.client;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.bean.QueryBean;
import com.fengchao.aoyi.client.starBean.*;
import com.fengchao.aoyi.client.startService.ProductStarService;
import com.google.inject.internal.cglib.core.$ClassNameReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

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
		productStarService.findProdCategory("3992") ;
		InventoryQueryBean inventoryQueryBean = new InventoryQueryBean() ;
		inventoryQueryBean.setCodes("SL-ECP-37234");
		productStarService.findSkuInventory(inventoryQueryBean) ;
		productStarService.findSkuSalePrice("SL-ECP-37234") ;
		AddressInfoQueryBean addressInfoQueryBean = new AddressInfoQueryBean() ;
		addressInfoQueryBean.setProvinceName("重庆市");
		addressInfoQueryBean.setCityName("重庆市");
		addressInfoQueryBean.setRegionName("涪陵区");
		productStarService.getAddressInfo(addressInfoQueryBean) ;
		HoldSkuInventoryQueryBean holdSkuInventoryQueryBean = new HoldSkuInventoryQueryBean() ;
		holdSkuInventoryQueryBean.setAreaId("4524130,4524157,4524163");
		holdSkuInventoryQueryBean.setOutOrderNo("2345");
		StarCodeBean starCodeBean = new StarCodeBean() ;
		starCodeBean.setCode("SL-ECP-37234");
		starCodeBean.setQuantity("3");
		List<StarCodeBean> starCodeBeans = new ArrayList<>() ;
		starCodeBeans.add(starCodeBean) ;
		holdSkuInventoryQueryBean.setCodeInvList(starCodeBeans);
		productStarService.preHoldSkuInventory(holdSkuInventoryQueryBean) ;
		ReleaseSkuInventoryQueryBean releaseSkuInventoryQueryBean = new ReleaseSkuInventoryQueryBean() ;
		releaseSkuInventoryQueryBean.setOutOrderNo("345333");
		releaseSkuInventoryQueryBean.setCodeInvList(starCodeBeans);
		productStarService.releaseSkuInventory(releaseSkuInventoryQueryBean) ;
	}

}
