package com.fengchao.product.aoyi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.bean.VendorsProfileBean;
import com.fengchao.product.aoyi.constants.ProductStatusEnum;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.feign.VendorsServiceClient;
import com.fengchao.product.aoyi.mapper.SkuCodeMapper;
import com.fengchao.product.aoyi.mapper.SkuCodeXMapper;
import com.fengchao.product.aoyi.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductAoyiApplicationTests {

	@Autowired
	private ProductDao productDao ;
	@Autowired
	private SkuCodeXMapper skuCodeMapper;
	@Autowired
	private VendorsServiceClient vendorsService;

	@Ignore
	@Test
	public void contextLoads() {
	}

	@Ignore
	@Test
	public void fixProduct() {
		List<AoyiProdIndex> aoyiProdIndexList =  productDao.selectFix() ;
		aoyiProdIndexList.forEach(aoyiProdIndex -> {
			SkuCode skuCode = skuCodeMapper.selectByMerchantId(aoyiProdIndex.getMerchantId());
			if (skuCode == null) {
				// 查询商户信息
				VendorsProfileBean profileBean = findVendorInfo(aoyiProdIndex.getMerchantId());
				if (profileBean != null) {
					// 获取最新code
					SkuCode lastCode = skuCodeMapper.selectLast();
					int code = Integer.parseInt(lastCode.getMerchantCode()) + 1;
					skuCode = new SkuCode();
					skuCode.setMerchantId(aoyiProdIndex.getMerchantId());
					skuCode.setMerchantName(profileBean.getCompany().getName());
					skuCode.setMerchantCode(code + "");
					skuCode.setSkuValue(0);
					skuCode.setCreatedAt(new Date());
					skuCode.setUpdatedAt(new Date());
					skuCodeMapper.insertSelective(skuCode);
				} else {
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

	private VendorsProfileBean findVendorInfo(int id) {
		OperaResult result = vendorsService.vendorInfo(id) ;
		if (result.getCode() == 200) {
			Object object = result.getData() ;
			String jsonString = JSON.toJSONString(object);
			VendorsProfileBean profileBean = JSONObject.parseObject(jsonString, VendorsProfileBean.class);
			return profileBean;
		}
		return null;
	}

}
