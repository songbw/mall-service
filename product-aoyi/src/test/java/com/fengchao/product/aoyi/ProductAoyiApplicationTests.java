package com.fengchao.product.aoyi;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.feign.VendorsServiceClient;
import com.fengchao.product.aoyi.mapper.SkuCodeXMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexWithBLOBs;
import com.fengchao.product.aoyi.model.SkuCode;
import com.fengchao.product.aoyi.rpc.extmodel.SysCompany;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

}
