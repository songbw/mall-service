package com.fengchao.aoyi.client.utils;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.aoyi.client.exception.AoyiClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpClient {

    //    private static final String AOYI_BASE_URL = "https://i.aoyi365.com/rest" ;
    private static final String AOYI_BASE_URL = "https://openapi.aoyi365.com/api/rest";

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
    public static final String AOYI_PRODPOOL_GETPRODUCTINFO = "aoyi.prodpool.query";
    public static final String AOYI_PRODPOOL_GETPRODUCTINFO_RUL = "/prodpool/aoyiApiProdPool/getProductInfo";
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

    private static final RequestConfig config;

    public static final Integer HTTP_CONNECT_TIMOUT = 1000 * 4; // 3秒

    public static final Integer HTTP_SOCKET_TIMEOUT = 1000 * 4; // 3秒
    static {
        config = RequestConfig.custom()
                .setConnectTimeout(HTTP_CONNECT_TIMOUT)
                .setSocketTimeout(HTTP_SOCKET_TIMEOUT)
                .build();
    }

    private static TrustManager[] getTrustManager() {
        return new TrustManager[]{new X509TrustManager() {
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
        }};
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

    public static <T> T post(Object body, Class<T> obj, String info, String path, String method) throws AoyiClientException {
        client = createClient();
        String app_id = APPID;
        String app_sercet = APPSERCET;
        long timestamp = System.currentTimeMillis();
        String temp = app_id + app_sercet + method + timestamp + info;
        String sign = MD5Utils.MD5(temp).toUpperCase();
        WebTarget target = client.target(AOYI_BASE_URL).path(path).queryParam("method", method).queryParam("app_id", APPID).queryParam("app_secret", APPSERCET).queryParam("timestamp", timestamp).queryParam("sign", sign);
        T bean = null;
        try {
            bean = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE), obj);
        } catch (Exception e) {
            logger.error("Aoyi Client error : {}", e);
            throw new AoyiClientException();
        }
        return bean;
    }

    public static <T> T get(String params, Class<T> obj, String path, String method) {
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

    /**
     * @param url
     * @param param
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String sendHttpPost(String url, String param, String encoding) throws Exception {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;

        try {
            httpclient = HttpClients.custom().build();
            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(param.toString(), encoding); //这里设置发送内容的编码格式
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);

            log.info("HttpClient#sendHttpPost 发送请求 url:{}, param:{}", url, param);

            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String responseContent = EntityUtils.toString(entity, encoding);

            log.info("HttpClient#sendHttpPost 返回:{}", responseContent);

            return responseContent;
        } catch (Exception e) {
            log.error("HttpClient#sendHttpPost 异常:{}", e.getMessage(), e);

            throw e;
        } finally {
            if (response != null) {
                response.close();
            }

            if (httpclient != null) {
                httpclient.close();
            }
        }
    }


    public static String doPostWithSSL(String url, String params, String encoding) throws Exception {
        CloseableHttpClient httpClient = null;

        CloseableHttpResponse response = null;

        try {
            httpClient = getSingleSSLConnection();

            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(params, encoding); //这里设置发送内容的编码格式
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);

            // 执行请求
            response = httpClient.execute(httpPost);
            log.info("https post 请求 返回:{}", JSONUtil.toJsonString(response));

            //
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                Header header = response.getFirstHeader("location");
                if (header != null) {
                    log.warn("https post 请求 出错:{}", header.getValue());
                }
                throw new Exception("https post 请求 出错!!");
            }

            HttpEntity entity = response.getEntity();
            String responseContent = EntityUtils.toString(entity, encoding);

            return responseContent;
        } catch (Exception e) {
            log.error("https post 请求异常:{}", e.getMessage(), e);
            throw new Exception(e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }

                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("https post 请求 关闭资源异常:{}", e.getMessage(), e);
                throw new Exception(e);
            }
        }
    }

    /**
     * 创建单向ssl的连接
     *
     * @return
     * @throws Exception
     */
    public static CloseableHttpClient getSingleSSLConnection() throws Exception {
        CloseableHttpClient httpClient = null;

        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] paramArrayOfX509Certificate,
                                         String paramString) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(config).build();
            return httpClient;
        } catch (Exception e) {
            log.error("https post 请求 创建ssl连接异常:{}", e.getMessage(), e);
            throw new Exception(e);
        }
    }

    public static <T> T postHeader(Object body, Class<T> obj, String baseUrl, String path, String key) throws AoyiClientException {
        client = createClient();
        WebTarget target = client.target(baseUrl).path(path);
        T bean = null;
        try {
            bean = target.request(MediaType.APPLICATION_JSON_TYPE).header("x-api-key", key).post(Entity.entity(body, MediaType.APPLICATION_JSON_TYPE), obj);
        } catch (Exception e) {
            logger.error("Aoyi Client postHeader error : {}", e);
            throw new AoyiClientException();
        }
        return bean;
    }

    public static void main(String args[]) {
        JSONObject bean = null;
        System.out.println(bean.getString("CODE"));
    }
}
