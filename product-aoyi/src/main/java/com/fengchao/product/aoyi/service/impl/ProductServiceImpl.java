package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.db.annotation.DataSource;
import com.fengchao.product.aoyi.db.config.DataSourceNames;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import com.fengchao.product.aoyi.mapper.AoyiProdIndexMapper;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.service.ProductService;
import com.fengchao.product.aoyi.utils.CosUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private AoyiProdIndexMapper mapper;
    @Autowired
    private AoyiClientService aoyiClientService;
//    @Autowired
//    private PromotionMapper promotionMapper;

    @DataSource(DataSourceNames.TWO)
    @Override
    public PageBean findList(ProductQueryBean queryBean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int offset = PageBean.getOffset(queryBean.getPageNo(), queryBean.getPageSize());
        HashMap map = new HashMap();
        map.put("pageNo", offset);
        map.put("pageSize", queryBean.getPageSize());
        if(queryBean.getCategory()!=null&&!queryBean.getCategory().equals(""))
            map.put("category", queryBean.getCategory());
        if(queryBean.getBrand()!=null&&!queryBean.getBrand().equals(""))
            map.put("brand", queryBean.getBrand());
        List<AoyiProdIndex> prodIndices = new ArrayList<>();
        total = mapper.selectLimitCount(map);
        if (total > 0) {
            mapper.selectLimit(map).forEach(aoyiProdIndex -> {
                String imageUrl = aoyiProdIndex.getImagesUrl();
                if (imageUrl != null && (!"".equals(imageUrl))) {
                    String image = "";
                    if (imageUrl.indexOf("/") == 0) {
                        image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                    } else {
                        image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                    }
                    aoyiProdIndex.setImage(image);
                }
                if (aoyiProdIndex.getImageExtend() != null) {
                    aoyiProdIndex.setImage(aoyiProdIndex.getImageExtend());
                }
                if (aoyiProdIndex.getImagesUrlExtend() != null) {
                    aoyiProdIndex.setImagesUrl(aoyiProdIndex.getImagesUrlExtend());
                }
                if (aoyiProdIndex.getIntroductionUrlExtend() != null) {
                    aoyiProdIndex.setIntroductionUrl(aoyiProdIndex.getIntroductionUrlExtend());
                }
                prodIndices.add(aoyiProdIndex);
            });
        }
        pageBean = PageBean.build(pageBean, prodIndices, total, queryBean.getPageNo(), queryBean.getPageSize());
        return pageBean;
    }

    @Override
    public OperaResult findPrice(PriceQueryBean queryBean) {
        List<PriceSkus> list = new ArrayList<>();
        QueryCityPrice cityPrice = new QueryCityPrice();
        cityPrice.setCityId(queryBean.getCityId());
        queryBean.getSkus().forEach(priceBean -> {
            PriceSkus priceSkus = new PriceSkus();
            priceSkus.setSkuId(priceBean.getSkuId());
            list.add(priceSkus) ;
        });
        cityPrice.setSkus(list);
        return aoyiClientService.price(cityPrice);
    }

    @Override
    public List<InventoryBean> findInventory(InventoryQueryBean queryBean) {
        List<InventoryBean> inventoryBeans = new ArrayList<>() ;
        for (InventoryBean sku : queryBean.getSkus()) {
            QueryInventory inventory = new QueryInventory();
            inventory.setCityId(queryBean.getCityId());
            inventory.setCountyId(queryBean.getCountyId());
            List<InventorySkus> ilist = new ArrayList<>();
            InventorySkus inventorySkus = new InventorySkus();
            inventorySkus.setNum(sku.getRemainNum());
            inventorySkus.setSkuId(sku.getSkuId());
            ilist.add(inventorySkus);
            inventory.setSkuIds(ilist);
            OperaResult operaResult = aoyiClientService.inventory(inventory);
            Map result = (Map) operaResult.getData().get("result");
            InventoryBean inventoryBean = new InventoryBean();
            inventoryBean.setSkuId((String) result.get("skuId"));
            inventoryBean.setState((String) result.get("state"));
            inventoryBean.setPrice((String) result.get("price"));
            inventoryBean.setRemainNum(sku.getRemainNum());
            inventoryBeans.add(inventoryBean);
        }
//        inventoryBeans = queryBean.getSkus();
        return inventoryBeans;
    }

    @Override
    public List<FreightFareBean> findCarriage(CarriageQueryBean queryBean) {
        List<FreightFareBean> freightFareBeans = new ArrayList<>();
        List<CarriageParam> params = queryBean.getCarriages();
        for (CarriageParam param : params) {
            FreightFareBean freightFareBean = new FreightFareBean();
            QueryCarriage carriage = new QueryCarriage();
            carriage.setAmount(param.getAmount());
            carriage.setMerchantNo(param.getMerchantNo());
            carriage.setOrderNo(queryBean.getOrderId());
            OperaResult operaResult = aoyiClientService.shipCarriage(carriage);
            Map result = (Map) operaResult.getData().get("result");
            freightFareBean.setFreightFare((String) result.get("freightFare"));
            freightFareBean.setMerchantNo((String) result.get("merchantNo"));
            freightFareBean.setOrderNo((String) result.get("orderNo"));
            freightFareBeans.add(freightFareBean);
        }
        return freightFareBeans;

    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public AoyiProdIndex find(String id) {
        AoyiProdIndex aoyiProdIndex = mapper.selectBySkuId(id);
        if (aoyiProdIndex.getImageExtend() != null) {
            aoyiProdIndex.setImage(aoyiProdIndex.getImageExtend());
        }
        if (aoyiProdIndex.getImagesUrlExtend() != null) {
            aoyiProdIndex.setImagesUrl(aoyiProdIndex.getImagesUrlExtend());
        }
        if (aoyiProdIndex.getIntroductionUrlExtend() != null) {
            aoyiProdIndex.setIntroductionUrl(aoyiProdIndex.getIntroductionUrlExtend());
        }
        return aoyiProdIndex;
    }

    @Override
    public List<AoyiProdIndex> findAll() {
        HashMap map = new HashMap();
        map.put("pageNo",0);
        map.put("pageSize",1000);
        List<AoyiProdIndex> prodIndices = new ArrayList<>();
        mapper.selectAll(map).forEach(aoyiProdIndex -> {
            String imageUrl = aoyiProdIndex.getImagesUrl();
            if (imageUrl != null && (!"".equals(imageUrl))) {
                String image = "";
                if (imageUrl.indexOf("/") == 0) {
                    image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
                } else {
                    image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
                }
                aoyiProdIndex.setImage(image);
            }
            if (aoyiProdIndex.getImageExtend() != null) {
                aoyiProdIndex.setImage(aoyiProdIndex.getImageExtend());
            }
            if (aoyiProdIndex.getImagesUrlExtend() != null) {
                aoyiProdIndex.setImagesUrl(aoyiProdIndex.getImagesUrlExtend());
            }
            if (aoyiProdIndex.getIntroductionUrlExtend() != null) {
                aoyiProdIndex.setIntroductionUrl(aoyiProdIndex.getIntroductionUrlExtend());
            }
            prodIndices.add(aoyiProdIndex);
        });
        return prodIndices;
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public ProductInfoBean findAndPromotion(String skuId) {
        ProductInfoBean infoBean = new ProductInfoBean();
        AoyiProdIndex aoyiProdIndex = mapper.selectBySkuId(skuId);
        String imageUrl = aoyiProdIndex.getImagesUrl();
        if (imageUrl != null && (!"".equals(imageUrl))) {
            String image = "";
            if (imageUrl.indexOf("/") == 0) {
                image = CosUtil.iWalletUrlT + imageUrl.split(":")[0];
            } else {
                image = CosUtil.baseAoyiProdUrl + imageUrl.split(":")[0];
            }
            aoyiProdIndex.setImage(image);
        }
        if (aoyiProdIndex.getImageExtend() != null) {
            aoyiProdIndex.setImage(aoyiProdIndex.getImageExtend());
        }
        if (aoyiProdIndex.getImagesUrlExtend() != null) {
            aoyiProdIndex.setImagesUrl(aoyiProdIndex.getImagesUrlExtend());
        }
        if (aoyiProdIndex.getIntroductionUrlExtend() != null) {
            aoyiProdIndex.setIntroductionUrl(aoyiProdIndex.getIntroductionUrlExtend());
        }
        BeanUtils.copyProperties(aoyiProdIndex, infoBean);
        List<PromotionInfoBean> promotionInfoBeans = null;
//                promotionMapper.selectPromotionInfoBySku(aoyiProdIndex.getSkuid());
        infoBean.setPromotion(promotionInfoBeans);
        return infoBean;
    }
}
