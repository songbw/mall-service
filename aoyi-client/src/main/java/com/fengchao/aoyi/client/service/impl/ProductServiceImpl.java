package com.fengchao.aoyi.client.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.aoyi.client.bean.*;
import com.fengchao.aoyi.client.exception.AoyiClientException;
import com.fengchao.aoyi.client.service.ProductService;
import com.fengchao.aoyi.client.utils.CosUtil;
import com.fengchao.aoyi.client.utils.HttpClient;
import com.fengchao.aoyi.client.utils.MD5Utils;
import com.fengchao.aoyi.client.utils.URLConnectionDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public List<PriceBean> findPrice(QueryCityPrice cityPrice) throws AoyiClientException {
        List<PriceBean> priceBeans = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String object = "";
        try {
            object = objectMapper.writeValueAsString(cityPrice);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONObject r = HttpClient.post(cityPrice, JSONObject.class, object, HttpClient.AOYI_PRODUCT_PRICE_URL, HttpClient.AOYI_PRODUCT_PRICE) ;
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONObject data = r.getJSONObject("RESULT");
            JSONArray skus = data.getJSONArray("skus");
            skus.forEach(price -> {
                JSONObject p = (JSONObject) price;
                PriceBean priceBean = new PriceBean();
                priceBean.setSkuId(p.getString("skuId"));
                priceBean.setPrice(p.getString("price"));
                priceBeans.add(priceBean);
            });
            return priceBeans;
        } else {
            throw new AoyiClientException(100001, "获取价格失败");
        }
    }

    @Override
    public InventoryBean findInventory(QueryInventory inventory) throws AoyiClientException {
        InventoryBean inventoryBean = new InventoryBean();
        ObjectMapper objectMapper = new ObjectMapper();
        String object = "";
        try {
            object = objectMapper.writeValueAsString(inventory);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONObject r = HttpClient.post(inventory, JSONObject.class, object, HttpClient.AOYI_PRODUCT_STOCK_URL, HttpClient.AOYI_PRODUCT_STOCK) ;
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONObject data = r.getJSONObject("RESULT");
            inventoryBean.setSkuId(data.getString("skuIds"));
            inventoryBean.setState(data.getString("state"));
            inventoryBean.setRemainNum(data.getString("remainNum"));
            return inventoryBean;
        } else {
            throw new AoyiClientException(100002, "获取库存失败");
        }
    }

    @Override
    public FreightFareBean findCarriage(QueryCarriage queryCarriage) throws AoyiClientException {
        FreightFareBean freightFareBean = new FreightFareBean();
        ObjectMapper objectMapper = new ObjectMapper();
        String object = "";
        try {
            object = objectMapper.writeValueAsString(queryCarriage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONObject r = HttpClient.post(queryCarriage, JSONObject.class, object, HttpClient.AOYI_FREIGHTFARE_URL, HttpClient.AOYI_FREIGHTFARE) ;
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONObject data = r.getJSONObject("RESULT");
            freightFareBean.setFreightFare(data.getString("freightFare"));
            freightFareBean.setMerchantNo(queryCarriage.getMerchantNo());
            freightFareBean.setOrderNo(data.getString("orderNo"));
            return freightFareBean;
        } else {
            throw new AoyiClientException(100003, "获取运费失败");
        }
    }

    @Override
    public List<CategoryResponse> category() throws AoyiClientException {
        List<CategoryResponse> responses = new ArrayList<>();
        JSONObject bean = HttpClient.get("", JSONObject.class, HttpClient.AOYI_CATEGORY_GETVALIDCATEGORY_URL, HttpClient.AOYI_CATEGORY_GETVALIDCATEGORY) ;
        logger.info(bean.toJSONString());
        String code = bean.getString("CODE");
        if ("0".equals(code)) {
            JSONArray result = bean.getJSONArray("RESULT") ;
            result.forEach(category -> {
                CategoryResponse categoryResponse = new CategoryResponse();
                JSONObject o = (JSONObject) category;
                categoryResponse.setCategoryId(o.getString("categoryId"));
                categoryResponse.setCategoryName(o.getString("categoryName"));
                responses.add(categoryResponse);
            });
            return responses;
        } else {
            throw new AoyiClientException(100004, "获取类别失败");
        }
    }

    @Override
    public List<String> getProdSkuPool(Integer categoryId) throws AoyiClientException {
        List<String> responses = new ArrayList<>();
        String object = "{\"categoryId\":\"" + categoryId + "\"}";
        JSONObject r = HttpClient.post(object, JSONObject.class, object, HttpClient.AOYI_PRODPOOL_GETPRODUCTINFO_RUL, HttpClient.AOYI_PRODPOOL_GETPRODUCTINFO) ;
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONArray result = r.getJSONArray("RESULT") ;
            result.forEach(category -> {
                JSONObject o = (JSONObject) category;
                responses.add(o.getString("productSku"));
            });
            return responses;
        } else {
            throw new AoyiClientException(100005, "获取产品列表失败");
        }
    }

    @Override
    public List<ProdImage> getProdImage(String skuId) throws AoyiClientException {
        List<ProdImage> responses = new ArrayList<>();
        String object = "{\"productSku\": " + skuId + "}";
        JSONObject r = HttpClient.post(object, JSONObject.class, object, HttpClient.AOYI_PRODPOOL_GETPRODIMAGE_URL, HttpClient.AOYI_PRODPOOL_GETPRODIMAGE) ;
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONObject result = r.getJSONObject("RESULT") ;
            JSONArray ztArray = result.getJSONArray("zt_image") ;
            JSONArray xqArray = result.getJSONArray("xq_image") ;
            if (ztArray != null && ztArray.size() > 0) {
                ztArray.forEach(img -> {
                    ProdImage  prodImage = new ProdImage();
                    JSONObject o = (JSONObject) img;
                    prodImage.setUrl(o.getString("zt_image"));
                    prodImage.setType("ZT");
                    responses.add(prodImage);
                });
            }
            if (xqArray != null && xqArray.size() > 0) {
                xqArray.forEach(img -> {
                    ProdImage  prodImage = new ProdImage();
                    JSONObject o = (JSONObject) img;
                    prodImage.setUrl(o.getString("xq_image"));
                    prodImage.setType("XQ");
                    responses.add(prodImage);
                });
            }
            return responses;
        } else {
            throw new AoyiClientException(100006, "获取产品图片失败");
        }
    }

    @Override
    public AoyiProdIndex getProdDetail(String skuId) throws AoyiClientException {
        List<AoyiProdIndex> responses = new ArrayList<>();
        String object = "{\"productSku\":\"" + skuId + "\"}";
        JSONObject r = HttpClient.post(object, JSONObject.class, object, HttpClient.AOYI_PRODPOOL_GETPRODUCTDETAILINFO_URL, HttpClient.AOYI_PRODPOOL_GETPRODUCTDETAILINFO) ;
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONObject data = r.getJSONObject("RESULT");
            AoyiProdIndex prodIndex = new AoyiProdIndex();
            prodIndex.setSkuid(data.getString("skuId"));
            prodIndex.setCategory(data.getString("categoryId"));
            prodIndex.setWeight(data.getString("productWeight"));
            prodIndex.setImage(data.getString("image"));
            prodIndex.setBrand(data.getString("brand"));
            prodIndex.setModel(data.getString("model"));
            prodIndex.setName(data.getString("name"));
            prodIndex.setUpc(data.getString("upc"));
            prodIndex.setSaleunit(data.getString("saleUnit"));
            prodIndex.setState(data.getString("state"));
            prodIndex.setIntroduction(data.getString("introduction"));
            prodIndex.setProdParams(data.getString("prodParams"));
            String zturl = "";
            String xqurl = "";
            List<ProdImage> prodImages = getProdImage(skuId);
            for (int i = 0; i < prodImages.size(); i++) {
                ProdImage prodImage = prodImages.get(i) ;
                String path = "/"+ prodIndex.getCategory() + "/"+ skuId + "/";
                if ("ZT".equals(prodImage.getType())) {
                    String ztarray[] = prodImage.getUrl().split("ZT");
                    if ("".equals(zturl)) {
                        zturl = path + "ZT" + ztarray[1];
                    } else {
                        zturl = zturl + ":"+ path + "ZT" + ztarray[1];
                    }
                }
                if ("XQ".equals(prodImage.getType())) {
                    String xqarray[] = prodImage.getUrl().split("XQ");
                    if ("".equals(xqurl)) {
                        xqurl = path + "XQ" + xqarray[1];
                    } else {
                        xqurl = xqurl + ":"+ path + "XQ" + xqarray[1];
                    }
                }
            }
            for (int i = 0; i < prodImages.size(); i++) {
                ProdImage prodImage = prodImages.get(i) ;
                String base = "aoyi";
                String path = "/"+ prodIndex.getCategory() + "/"+ skuId + "/" + prodImage.getType();
                String array1[] = prodImage.getUrl().split(prodImage.getType());
                String fileName = array1[1];
                try {
                    URLConnectionDownloader.download(prodImage.getUrl(), fileName, base + path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CosUtil.upload(CosUtil.iWalletBucketName, new File(base + path + fileName),path+fileName) ;
            }
            prodIndex.setIntroductionUrl(xqurl);
            prodIndex.setImagesUrl(zturl);
            return prodIndex;
        } else {
            throw new AoyiClientException(100007, "获取产品详情失败");
        }
    }

    @Override
    public String getSaleStatus(String skuId) throws AoyiClientException {
        List<ProdImage> responses = new ArrayList<>();
        String object = "{\"productSku\":" + skuId + "}";
        JSONObject r = HttpClient.post(object, JSONObject.class, object, HttpClient.AOYI_PRODPOOL_GETBATCHPRODSALESTATUS_URL, HttpClient.AOYI_PRODPOOL_GETBATCHPRODSALESTATUS) ;
        String code = r.getString("CODE");
        logger.info(r.toJSONString());
        if ("0".equals(code)) {
            JSONObject result = r.getJSONObject("RESULT") ;
            return result.getString("status");
        } else {
            throw new AoyiClientException(100007, "获取上下架状态失败");
        }
    }
}
