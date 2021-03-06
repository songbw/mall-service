package com.fengchao.aoyi.client.utils;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.aoyi.client.bean.OperaResult;
import com.fengchao.aoyi.client.exception.AoyiClientException;
import com.fengchao.aoyi.client.service.impl.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpClient {

//    private static final String AOYI_BASE_URL = "https://i.aoyi365.com/rest" ;
    private static final String AOYI_BASE_URL = "https://openapi.aoyi365.com/api/rest" ;

//    private static final String AOYI_BASE_URL= "http://aoyitest.aoyi365.com/rest" ;

    private static Logger logger = LoggerFactory.getLogger(HttpClient.class);
    //测试
//    public static final String APPID = "b385933d-a250-4772-9d84-8c360dbb5b38";
//    public static final String APPSERCET = "091A6314DC0E0744350A08677BD73045";
    //正式
    public static final String APPID = "21024623-babb-4303-9c67-991b0e108309";
    public static final String APPSERCET = "36EC7549BBC53650818B4F6486B1CCBC";


    //获取全部商品类目id
    public static final String AOYI_CATEGORY_GETVALIDCATEGORY = "aoyi.category.get";
    public static final String AOYI_CATEGORY_GETVALIDCATEGORY_URL = "/category/aoyiApiCategory/getCategory";
    //根据类目id 获取 商品 mpu
    public static final String AOYI_PRODPOOL_GETPRODUCTINFO  = "aoyi.prodpool.query";
    public static final String AOYI_PRODPOOL_GETPRODUCTINFO_RUL  = "/prodpool/aoyiApiProdPool/getProductInfo";
    //根据商品id 获取 商品详情
    public static final String AOYI_PRODPOOL_GETPRODUCTDETAILINFO = "aoyi.proddetail.get";
    public static final String AOYI_PRODPOOL_GETPRODUCTDETAILINFO_URL = "/prodpool/aoyiApiProdPool/getProductDetailInfo";
    //根据商品id 获取 商品图片
    public static final String AOYI_PRODPOOL_GETPRODIMAGE = "aoyi.prodimage.get";
    public static final String AOYI_PRODPOOL_GETPRODIMAGE_URL = "/prodpool/aoyiApiProdPool/getProdImage";
    //根据商品id 获取 商品上下架状态
    public static final String AOYI_PRODPOOL_GETBATCHPRODSALESTATUS = "aoyi.batchprodsalestatus.get";
    public static final String AOYI_PRODPOOL_GETBATCHPRODSALESTATUS_URL = "/prodpool/aoyiApiProdPool/getBatchProdSaleStatus";
    //批量查询商品价格接口
    public static final String AOYI_PRODUCT_PRICE = "aoyi.price.query";
    public static final String AOYI_PRODUCT_PRICE_URL = "/prodpool/aoyiApiProdPool/getProductPrice";
    public static final String AOYI_PRODUCT_PRICE_URL_GAT = "/prodpool/aoyiApiProdPool/getGatProductPriceGAT";
    //商品库存查询
    public static final String AOYI_PRODUCT_STOCK = "aoyi.inventory.get";
    public static final String AOYI_PRODUCT_STOCK_URL = "/prodpool/aoyiApiProdPool/getProductStock";
    //获取订单 预占
    public static final String AOYI_PUSH_ORDER = "aoyi.order.add";
    public static final String AOYI_PUSH_ORDER_URL = "/order/aoyiApiOrder/pushOrder";
    public static final String AOYI_PUSH_ORDER_URL_GAT = "/order/aoyiApiOrder/pushOrderGAT";
    //获取订单 确定
    public static final String AOYI_CONFRIM_ORDER = "aoyi.order.confirmOrder";
    public static final String AOYI_CONFRIM_ORDER_URL = "/order/aoyiApiOrder/confirmOrder";
    //运费
    public static final String AOYI_FREIGHTFARE = "aoyi.shipcarriage.get";
    public static final String AOYI_FREIGHTFARE_URL = "/order/aoyiApiOrder/shipCarriage";
    //物流查询
    public static final String AOYI_ORDER_LOGIST = "aoyi.orderlogist.get";
    public static final String AOYI_ORDER_LOGIST_URL = "/order/aoyiApiOrder/orderlogist";

    private static Client client = null;

    private static TrustManager[] getTrustManager() {
        return new TrustManager[] { new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                // Trust all servers
            }
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                // Trust all clients
            }
        } };
    }

    public static Client createClient() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, getTrustManager(), new SecureRandom());

            HostnameVerifier verifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostName, SSLSession sslSession) {
                    return true;
                }
            };
            return ClientBuilder.newBuilder().sslContext(ctx).hostnameVerifier(verifier)
                    .build();
        } catch (GeneralSecurityException exception) {
            // Log (or/and) throw exception
            return null;
        }
    }

    public static <T> T post(Object body, Class<T> obj, String info, String path, String method) throws AoyiClientException{
        client = createClient();
        String app_id = APPID;
        String app_sercet = APPSERCET;
        long timestamp = System.currentTimeMillis();
        String temp = app_id + app_sercet + method + timestamp + info;
        String sign = MD5Utils.MD5(temp).toUpperCase();
        WebTarget target = client.target(AOYI_BASE_URL).path(path).queryParam("method", method).queryParam("app_id", APPID).queryParam("app_secret", APPSERCET).queryParam("timestamp", timestamp).queryParam("sign", sign);
        T bean = null;
        try {
            bean = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(body,MediaType.APPLICATION_JSON_TYPE),obj);
        } catch (Exception e) {
            logger.error("Aoyi Client error : {}", e);
            throw new AoyiClientException();
        }
        return bean;
    }

    public static <T> T get(String params, Class<T> obj, String path, String method){
        client = createClient();
        String app_id = APPID;
        String app_sercet = APPSERCET;
        long timestamp = System.currentTimeMillis();
        String temp = app_id + app_sercet + method + timestamp;
        String sign = MD5Utils.MD5(temp).toUpperCase();
        WebTarget target = client.target(AOYI_BASE_URL).path(path).queryParam("method", method).queryParam("app_id", APPID).queryParam("app_secret", APPSERCET).queryParam("timestamp", timestamp).queryParam("sign", sign);
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        T bean = response.readEntity(obj);
        return bean;
    }

    public static void main(String args[]) {
        JSONObject bean = null;
        System.out.println(bean.getString("CODE"));
    }
}
