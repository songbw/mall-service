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
    public OperaResult findPrice(QueryCityPrice cityPrice) {
        OperaResult operaResult = new OperaResult();
        List<PriceBean> priceBeans = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String object = "";
        try {
            object = objectMapper.writeValueAsString(cityPrice);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONObject r = HttpClient.post(cityPrice, JSONObject.class, object, HttpClient.AOYI_PRODUCT_PRICE_URL, HttpClient.AOYI_PRODUCT_PRICE) ;
        if (r != null) {
            logger.info(r.toJSONString());
            String code = r.getString("CODE");
            if ("0".equals(code)) {
                JSONObject data = r.getJSONObject("RESULT");
                JSONArray skus = data.getJSONArray("skus");
                if (skus != null) {
                    skus.forEach(price -> {
                        JSONObject p = (JSONObject) price;
                        PriceBean priceBean = new PriceBean();
                        priceBean.setSkuId(p.getString("skuId"));
                        priceBean.setPrice(p.getString("price"));
                        priceBeans.add(priceBean);
                    });
                    operaResult.setData(priceBeans);
                    return operaResult;
                }else {
                    operaResult.setCode(100001);
                    operaResult.setMsg("获取价格返回值为空");
                    return operaResult;
                }
            } else {
                operaResult.setCode(100001);
                String message = r.getString("MESSAGE") ;
                if (message == null || "".equals(message)) {
                    operaResult.setMsg("获取价格失败");
                } else {
                    operaResult.setMsg(message);
                }
                return operaResult;
            }
        } else  {
            operaResult.setCode(100001);
            operaResult.setMsg("获取价格失败");
            return operaResult;
        }
    }

    @Override
    public OperaResult findInventory(QueryInventory inventory) {
        OperaResult operaResult = new OperaResult();
        InventoryBean inventoryBean = new InventoryBean();
        ObjectMapper objectMapper = new ObjectMapper();
        String object = "";
        try {
            object = objectMapper.writeValueAsString(inventory);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONObject r = HttpClient.post(inventory, JSONObject.class, object, HttpClient.AOYI_PRODUCT_STOCK_URL, HttpClient.AOYI_PRODUCT_STOCK) ;
        if (r == null) {
            operaResult.setCode(100002);
            operaResult.setMsg("获取库存失败");
            return operaResult;
        }
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONObject data = r.getJSONObject("RESULT");
            if (data != null) {
                inventoryBean.setSkuId(data.getString("skuIds"));
                if (data.getString("state") == null) {
                    inventoryBean.setState("0");
                } else {
                    inventoryBean.setState(data.getString("state"));
                }
                inventoryBean.setRemainNum(data.getString("remainNum"));
                operaResult.setData(inventoryBean);
                return operaResult;
            } else {
                operaResult.setCode(100002);
                operaResult.setMsg("获取库存返回值为空");
                return operaResult;
            }
        } else {
            operaResult.setCode(100002);
            String message = r.getString("MESSAGE") ;
            if (message == null || "".equals(message)) {
                operaResult.setMsg("获取库存失败");
            } else {
                operaResult.setMsg(message);
            }
            return operaResult;
        }
    }

    @Override
    public OperaResult findCarriage(QueryCarriage queryCarriage) {
        OperaResult operaResult = new OperaResult();
        FreightFareBean freightFareBean = new FreightFareBean();
        ObjectMapper objectMapper = new ObjectMapper();
        String object = "";
        try {
            object = objectMapper.writeValueAsString(queryCarriage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONObject r = HttpClient.post(queryCarriage, JSONObject.class, object, HttpClient.AOYI_FREIGHTFARE_URL, HttpClient.AOYI_FREIGHTFARE) ;
        if (r == null) {
            operaResult.setCode(100003);
            operaResult.setMsg("获取运费失败");
            return operaResult;
        }
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONObject data = r.getJSONObject("RESULT");
            freightFareBean.setFreightFare(data.getString("freightFare"));
            freightFareBean.setMerchantNo(queryCarriage.getMerchantNo());
            freightFareBean.setOrderNo(data.getString("orderNo"));
            operaResult.setData(freightFareBean);
            return operaResult;
        } else {
            operaResult.setCode(100003);
            String message = r.getString("MESSAGE") ;
            if (message == null || "".equals(message)) {
                operaResult.setMsg("获取运费失败");
            } else {
                operaResult.setMsg(message);
            }
            return operaResult;
        }
    }

    @Override
    public OperaResult category() {
        OperaResult operaResult = new OperaResult();
        List<CategoryResponse> responses = new ArrayList<>();
        JSONObject bean = HttpClient.get("", JSONObject.class, HttpClient.AOYI_CATEGORY_GETVALIDCATEGORY_URL, HttpClient.AOYI_CATEGORY_GETVALIDCATEGORY) ;
        if (bean == null) {
            operaResult.setCode(100004);
            operaResult.setMsg("获取类别失败");
            return operaResult;
        }
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
            operaResult.setData(responses);
            return operaResult;
        } else {
            operaResult.setCode(100004);
            String message = bean.getString("MESSAGE") ;
            if (message == null || "".equals(message)) {
                operaResult.setMsg("获取类别失败");
            } else {
                operaResult.setMsg(message);
            }
            return operaResult;
        }
    }

    @Override
    public OperaResult getProdSkuPool(Integer categoryId){
        OperaResult operaResult = new OperaResult();
        List<String> responses = new ArrayList<>();
        String object = "{\"categoryId\":\"" + categoryId + "\"}";
        JSONObject r = HttpClient.post(object, JSONObject.class, object, HttpClient.AOYI_PRODPOOL_GETPRODUCTINFO_RUL, HttpClient.AOYI_PRODPOOL_GETPRODUCTINFO) ;
        if (r == null) {
            operaResult.setCode(100005);
            operaResult.setMsg("获取产品列表失败");
            return operaResult;
        }
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONArray result = r.getJSONArray("RESULT") ;
            result.forEach(category -> {
                JSONObject o = (JSONObject) category;
                responses.add(o.getString("productSku"));
            });
            operaResult.setData(responses);
            return operaResult;
        } else {
            operaResult.setCode(100005);
            String message = r.getString("MESSAGE") ;
            if (message == null || "".equals(message)) {
                operaResult.setMsg("获取产品列表失败");
            } else {
                operaResult.setMsg(message);
            }
            return operaResult;
        }
    }

    @Override
    public OperaResult getProdImage(String skuId) {
        OperaResult operaResult = new OperaResult();
        List<ProdImage> responses = new ArrayList<>();
        String object = "{\"productSku\": " + skuId + "}";
        JSONObject r = HttpClient.post(object, JSONObject.class, object, HttpClient.AOYI_PRODPOOL_GETPRODIMAGE_URL, HttpClient.AOYI_PRODPOOL_GETPRODIMAGE) ;
        if (r == null) {
            operaResult.setCode(100006);
            operaResult.setMsg("获取产品图片失败");
            return operaResult;
        }
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
            operaResult.setData(responses);
            return operaResult;
        } else {
            operaResult.setCode(100006);
            String message = r.getString("MESSAGE") ;
            if (message == null || "".equals(message)) {
                operaResult.setMsg("获取产品图片失败");
            } else {
                operaResult.setMsg(message);
            }
            return operaResult;
        }
    }

    @Override
    public OperaResult getProdDetail(String skuId) {
        OperaResult operaResult = new OperaResult();
        List<AoyiProdIndex> responses = new ArrayList<>();
        String object = "{\"productSku\":\"" + skuId + "\"}";
        JSONObject r = HttpClient.post(object, JSONObject.class, object, HttpClient.AOYI_PRODPOOL_GETPRODUCTDETAILINFO_URL, HttpClient.AOYI_PRODPOOL_GETPRODUCTDETAILINFO) ;
        if (r == null) {
            operaResult.setCode(100007);
            operaResult.setMsg("获取产品详情失败");
            return operaResult;
        }
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
            OperaResult imageResult = getProdImage(skuId) ;
            List<ProdImage> prodImages = (List<ProdImage>) imageResult.getData();
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
            operaResult.setData(prodIndex);
            return operaResult;
        } else {
            operaResult.setCode(100007);
            String message = r.getString("MESSAGE") ;
            if (message == null || "".equals(message)) {
                operaResult.setMsg("获取产品详情失败");
            } else {
                operaResult.setMsg(message);
            }
            return operaResult;
        }
    }

    @Override
    public OperaResult getSaleStatus(String skuId) {
        OperaResult operaResult = new OperaResult();
        List<ProdImage> responses = new ArrayList<>();
        String object = "{\"productSku\":" + skuId + "}";
        JSONObject r = HttpClient.post(object, JSONObject.class, object, HttpClient.AOYI_PRODPOOL_GETBATCHPRODSALESTATUS_URL, HttpClient.AOYI_PRODPOOL_GETBATCHPRODSALESTATUS) ;
        if (r == null) {
            operaResult.setCode(100008);
            operaResult.setMsg("获取上下架状态失败");
            return operaResult;
        }
        String code = r.getString("CODE");
        logger.info(r.toJSONString());
        if ("0".equals(code)) {
            JSONObject result = r.getJSONObject("RESULT") ;
            operaResult.setData(result.getString("status"));
            return operaResult;
        } else {
            operaResult.setCode(100008);
            String message = r.getString("MESSAGE") ;
            if (message == null || "".equals(message)) {
                operaResult.setMsg("获取上下架状态失败");
            } else {
                operaResult.setMsg(message);
            }
            return operaResult;
        }
    }

    @Override
    public OperaResult findGATPrice(QueryCityPrice cityPrice) {
        OperaResult operaResult = new OperaResult();
        List<PriceBean> priceBeans = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String object = "";
        try {
            object = objectMapper.writeValueAsString(cityPrice);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONObject r = HttpClient.post(cityPrice, JSONObject.class, object, HttpClient.AOYI_PRODUCT_PRICE_URL_GAT, HttpClient.AOYI_PRODUCT_PRICE) ;
        if (r != null) {
            logger.info(r.toJSONString());
            String code = r.getString("CODE");
            if ("0".equals(code)) {
                JSONObject data = r.getJSONObject("RESULT");
                JSONArray skus = data.getJSONArray("skus");
                if (skus != null) {
                    skus.forEach(price -> {
                        JSONObject p = (JSONObject) price;
                        PriceBean priceBean = new PriceBean();
                        priceBean.setSkuId(p.getString("skuId"));
                        priceBean.setPrice(p.getString("price"));
                        priceBeans.add(priceBean);
                    });
                    operaResult.setData(priceBeans);
                    return operaResult;
                }else {
                    operaResult.setCode(100001);
                    operaResult.setMsg("获取价格返回值为空");
                    return operaResult;
                }
            } else {
                operaResult.setCode(100001);
                String message = r.getString("MESSAGE") ;
                if (message == null || "".equals(message)) {
                    operaResult.setMsg("获取价格失败");
                } else {
                    operaResult.setMsg(message);
                }
                return operaResult;
            }
        } else  {
            operaResult.setCode(100001);
            operaResult.setMsg("获取价格失败");
            return operaResult;
        }
    }
}
