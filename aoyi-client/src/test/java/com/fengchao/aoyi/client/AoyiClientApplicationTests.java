package com.fengchao.aoyi.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.bean.QueryBean;
import com.fengchao.aoyi.client.starBean.*;
import com.fengchao.aoyi.client.startService.OrderStarService;
import com.fengchao.aoyi.client.startService.ProductStarService;
import com.fengchao.aoyi.client.utils.JSONUtil;
import com.google.inject.internal.cglib.core.$ClassNameReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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
//		starOrderBean.setReceiverAreaId("4524130,4524157,4524163");
//		starOrderBean.setReceiverAreaName("广东省,深圳市,龙岗区");
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

	@Ignore
	@Test
	public void getSpuIdList() {
		QueryBean queryBean = new QueryBean() ;
		queryBean.setPageNo(1);
		queryBean.setPageSize(100);
//		queryBean.setStartTime("2018-07-05 16:43:35");
//		queryBean.setEndTime("2019-07-06 16:43:35");
		OperaResponse spuIdsResponse = productStarService.getSpuIdList(queryBean) ;
		String resJsonString = JSON.toJSONString(spuIdsResponse) ;
		JSONObject resJson = JSONObject.parseObject(resJsonString) ;
		JSONArray spuArray = resJson.getJSONObject("data").getJSONArray("spuIdList") ;
		for (int i = 0; i < spuArray.size(); i++) {
			String detailParam = "" ;
			System.out.println(i);
			if ((i + 49) > spuArray.size()) {
				detailParam = JSONUtil.toJsonString(spuArray.subList(i, spuArray.size()));
			} else {
				detailParam = JSONUtil.toJsonString(spuArray.subList(i, i + 49));
			}
			detailParam = detailParam.replace("[", "").replace("]", "").replace("\"", "") ;
			OperaResponse spuDetailRes = productStarService.getSpuDetail(detailParam) ;
			String spuDetailResString = JSON.toJSONString(spuDetailRes) ;
			JSONObject spuDetailResJson = JSONObject.parseObject(spuDetailResString) ;
			JSONArray spuDetailData = spuDetailResJson.getJSONArray("data") ;
			for (int j = 0; j < spuDetailData.size(); j++) {
				JSONObject jsonObject = spuDetailData.getJSONObject(j) ;
				SpuBean spuBean = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<SpuBean>(){});
				log.info("获取SPU信息，结果：{}",JSONUtil.toJsonString(spuBean));
			}
			i = i+ 49 ;
		}
		for (int i = 0; i < spuArray.size(); i++) {
			log.info("获取SKU信息，入参：{}", spuArray.getString(i)) ;
			OperaResponse skuDetailRes = productStarService.getSkuListDetailBySpuId(spuArray.getString(i)) ;
			if (skuDetailRes.getCode() == 200) {
				String skuDetailResString = JSON.toJSONString(skuDetailRes) ;
				JSONObject skuDetailResJson = JSONObject.parseObject(skuDetailResString) ;
				JSONArray skuDetailData = skuDetailResJson.getJSONArray("data") ;
				JSONObject jsonObject = skuDetailData.getJSONObject(0) ;
				SkuBean skuBean = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<SkuBean>(){});
				log.info("获取SKU信息，入参：{}, 结果：{}",spuArray.getString(i), JSONUtil.toJsonString(skuBean));
			}
		}
	}

}
