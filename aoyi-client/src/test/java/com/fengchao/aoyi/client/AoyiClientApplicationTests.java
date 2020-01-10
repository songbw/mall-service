package com.fengchao.aoyi.client;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.bean.QueryBean;
import com.fengchao.aoyi.client.starBean.*;
import com.fengchao.aoyi.client.startService.OrderStarService;
import com.fengchao.aoyi.client.startService.ProductStarService;
import com.google.inject.internal.cglib.core.$ClassNameReader;
import org.junit.Ignore;
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
	@Autowired
	private OrderStarService orderStarService ;

	@Ignore
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


	}

	@Ignore
	@Test
	public void contextOrderLoads() {
		HoldSkuInventoryQueryBean holdSkuInventoryQueryBean = new HoldSkuInventoryQueryBean() ;
		holdSkuInventoryQueryBean.setAreaId("4524130,4524157,4524163");
		holdSkuInventoryQueryBean.setOutOrderNo("202001103");
		StarCodeBean starCodeBean = new StarCodeBean() ;
		starCodeBean.setCode("SL-ECP-37234");
		starCodeBean.setQuantity("3");
		List<StarCodeBean> starCodeBeans = new ArrayList<>() ;
		starCodeBeans.add(starCodeBean) ;
		holdSkuInventoryQueryBean.setCodeInvList(starCodeBeans);
		productStarService.preHoldSkuInventory(holdSkuInventoryQueryBean) ;


		StarOrderBean starOrderBean = new StarOrderBean() ;
		starOrderBean.setOutOrderNo("202001103");
		starOrderBean.setReceiverAreaId("4524130,4524157,4524163");
		starOrderBean.setReceiverAreaName("广东省,深圳市,龙岗区");
		starOrderBean.setReceiverAddr("李朗怡亚通整合物流");
		starOrderBean.setReceiver("哇哈哈");
		starOrderBean.setReceiverPhone("18500001112");
		starOrderBean.setReceiverMobile("18500001112");
		starOrderBean.setFreight("20");
		starOrderBean.setBuyerRemark("11");
		starOrderBean.setSellerRemark("22");
		starOrderBean.setSkuList(starCodeBeans);
		orderStarService.addOrder(starOrderBean) ;

//		ReleaseSkuInventoryQueryBean releaseSkuInventoryQueryBean = new ReleaseSkuInventoryQueryBean() ;
//		releaseSkuInventoryQueryBean.setOutOrderNo("345333");
//		releaseSkuInventoryQueryBean.setCodeInvList(starCodeBeans);
//		productStarService.releaseSkuInventory(releaseSkuInventoryQueryBean) ;

//		orderStarService.confirmOrder("323929029") ;
	}

}
