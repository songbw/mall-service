package com.fengchao.product.aoyi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.QueryBean;
import com.fengchao.product.aoyi.bean.vo.ProductExportResVo;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import com.fengchao.product.aoyi.feign.VendorsServiceClient;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexXMapper;
import com.fengchao.product.aoyi.mapper.SkuCodeXMapper;
import com.fengchao.product.aoyi.mapper.StarBrandMapper;
import com.fengchao.product.aoyi.mapper.StarCategoryMapper;
import com.fengchao.product.aoyi.model.*;
import com.fengchao.product.aoyi.rpc.extmodel.SysCompany;
import com.fengchao.product.aoyi.service.AdminProdService;
import com.fengchao.product.aoyi.starBean.SkuBean;
import com.fengchao.product.aoyi.starBean.SpuBean;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductAoyiApplicationTests {

	@Autowired
	private ProductDao productDao ;
	@Autowired
	private SkuCodeXMapper skuCodeMapper;
	@Autowired
	private VendorsServiceClient vendorsService;
	@Autowired
	private AoyiProdIndexXMapper indexXMapper ;
	@Autowired
	private AdminProdService prodService;
	@Autowired
	private AoyiClientService aoyiClientService ;
	@Autowired
	private StarBrandMapper starBrandMapper ;
	@Autowired
	private StarCategoryMapper starCategoryMapper ;

	@Ignore
	@Test
	public void contextLoads() {
//		HashMap map = new HashMap<>() ;
//		map.put("floorPriceRate", 1.10) ;
//		map.put("offset", 0);
//		map.put("pageSize", 500);
//		int count = indexXMapper.selectPriceCount(map) ;
//		List<ProductExportResVo> productExportResVos = indexXMapper.selectProductPriceListPageable(map) ;
//		System.out.println(count);
		int floorPriceRate = 110;
		DecimalFormat df = new DecimalFormat("0.00");
		String s = df.format((float)floorPriceRate/100);
		float param = Float.valueOf(s.trim()).floatValue();
		List<ProductExportResVo> productExportResVoList = new ArrayList<>() ;
		try {
//			productExportResVoList =  prodService.exportProductPriceList(param) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(productExportResVoList);
	}

	@Ignore
	@Test
	public void fixProduct() {
		List<AoyiProdIndex> aoyiProdIndexList =  productDao.selectFix() ;
		aoyiProdIndexList.forEach(aoyiProdIndex -> {
			SkuCode skuCode = skuCodeMapper.selectByMerchantId(aoyiProdIndex.getMerchantId());
			if (skuCode == null) {
				// 查询商户信息
				OperaResponse<SysCompany> vendorResponse = vendorsService.vendorInfo(aoyiProdIndex.getMerchantId());
				if (vendorResponse.getCode() == 200) {
					SysCompany profileBean = vendorResponse.getData() ;
					if (profileBean != null) {
						// 获取最新code
						SkuCode lastCode = skuCodeMapper.selectLast();
						// logger.info(aoyiProdIndexX.getMerchantId() + "");
						int code = Integer.parseInt(lastCode.getMerchantCode()) + 1;
						// 添加商户信息
						skuCode = new SkuCode();
						skuCode.setMerchantId(aoyiProdIndex.getMerchantId());
						skuCode.setMerchantName(profileBean.getName());
						skuCode.setMerchantCode(code + "");
						skuCode.setSkuValue(0);
						skuCode.setCreatedAt(new Date());
						skuCode.setUpdatedAt(new Date());
						skuCodeMapper.insertSelective(skuCode);
					} else {
//						logger.warn("未获取到商户信息 merchantId:{}信息为:{}", aoyiProdIndex.getMerchantId());
						throw new ProductException(200001, "vendor 服务失败 " + vendorResponse.getMsg());
					}
				} else {
//					logger.warn("未获取到商户信息 merchantId:{}信息为:{}", aoyiProdIndex.getMerchantId());
					throw new ProductException(200001, "商户信息为null");
				}
			}
			AoyiProdIndexWithBLOBs aoyiProdIndexWithBLOBs = new AoyiProdIndexWithBLOBs();
			BeanUtils.copyProperties(aoyiProdIndex, aoyiProdIndexWithBLOBs);
			String merchantCode = skuCode.getMerchantCode();
			int skuValue = skuCode.getSkuValue();
			AtomicInteger atomicInteger = new AtomicInteger(skuValue);
			String sku = merchantCode + String.format("%06d", atomicInteger.incrementAndGet());
			aoyiProdIndexWithBLOBs.setMpu(sku);
			aoyiProdIndexWithBLOBs.setSkuid(sku);

			// 执行插入
			productDao.updateFix(aoyiProdIndexWithBLOBs);

			skuCode.setSkuValue(atomicInteger.get());
			skuCodeMapper.updateSkuValueByPrimaryKey(skuCode);
		});
	}

	@Ignore
	@Test
	public void starProduct() {
		QueryBean queryBean = new QueryBean() ;
		queryBean.setPageNo(1);
		queryBean.setPageSize(100);
//		queryBean.setStartTime("2018-07-05 16:43:35");
//		queryBean.setEndTime("2019-07-06 16:43:35");
		OperaResponse spuIdsResponse = aoyiClientService.getSpuIdList(queryBean) ;
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
			OperaResponse spuDetailRes = aoyiClientService.getSpuDetail(detailParam) ;
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
			OperaResponse skuDetailRes = aoyiClientService.getSkuDetail(spuArray.getString(i)) ;
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

	@Ignore
	@Test
	public void starBrand() {
		QueryBean queryBean = new QueryBean() ;
		for (int i = 0; i < 100; i++) {
			queryBean.setPageNo(i + 1);
			queryBean.setPageSize(100);
			OperaResponse response = aoyiClientService.findBrandList(queryBean) ;
			if (response.getCode() == 200) {
				String data = JSONUtil.toJsonString(response.getData()) ;
				JSONObject brands = JSONObject.parseObject(data) ;
				JSONArray brandArray =  brands.getJSONArray("brandList") ;
				for (int j = 0; j < brandArray.size(); j++) {
					JSONObject jsonObject = brandArray.getJSONObject(j) ;
					StarBrand starBrand = new StarBrand() ;
					starBrand.setStarId(jsonObject.getInteger("brandId"));
					starBrand.setLogo(jsonObject.getString("brandLogo"));
					starBrand.setName(jsonObject.getString("brandName"));
					starBrandMapper.insert(starBrand) ;
				}
			}
		}
	}

	@Ignore
	@Test
	public void starCategory() {
		OperaResponse response = aoyiClientService.findProdCategory(null) ;
		if (response.getCode() == 200) {
			String data = JSONUtil.toJsonString(response.getData()) ;
			JSONObject categorys = JSONObject.parseObject(data) ;
			JSONArray categoryArray =  categorys.getJSONArray("categoryList") ;
			for (int j = 0; j < categoryArray.size(); j++) {
				JSONObject jsonObject = categoryArray.getJSONObject(j) ;
				StarCategory starCategory = new StarCategory() ;
				starCategory.setStarId(jsonObject.getInteger("cateId"));
				starCategory.setLevel(jsonObject.getInteger("level"));
				starCategory.setName(jsonObject.getString("cateName"));
				starCategory.setParentId(jsonObject.getInteger("parentId"));
				starCategoryMapper.insert(starCategory) ;
				subStarCategory(starCategory.getStarId()) ;
			}
		}
	}

	private void subStarCategory(int categoryId) {
		OperaResponse response = aoyiClientService.findProdCategory(categoryId + "") ;
		if (response.getCode() == 200) {
			String data = JSONUtil.toJsonString(response.getData()) ;
			JSONObject categorys = JSONObject.parseObject(data) ;
			JSONArray categoryArray =  categorys.getJSONArray("categoryList") ;
			for (int j = 0; j < categoryArray.size(); j++) {
				JSONObject jsonObject = categoryArray.getJSONObject(j) ;
				StarCategory starCategory = new StarCategory() ;
				starCategory.setStarId(jsonObject.getInteger("cateId"));
				starCategory.setLevel(jsonObject.getInteger("level"));
				starCategory.setName(jsonObject.getString("cateName"));
				starCategory.setParentId(jsonObject.getInteger("parentId"));
				starCategoryMapper.insert(starCategory) ;
				subStarCategory(starCategory.getStarId()) ;
			}
		}
	}

}
