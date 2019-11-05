package com.fengchao.product.aoyi;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.vo.ProductExportResVo;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.feign.VendorsServiceClient;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexXMapper;
import com.fengchao.product.aoyi.mapper.SkuCodeXMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexWithBLOBs;
import com.fengchao.product.aoyi.model.SkuCode;
import com.fengchao.product.aoyi.rpc.extmodel.SysCompany;
import com.fengchao.product.aoyi.service.AdminProdService;
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
			productExportResVoList =  prodService.exportProductPriceList(param) ;
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

}
