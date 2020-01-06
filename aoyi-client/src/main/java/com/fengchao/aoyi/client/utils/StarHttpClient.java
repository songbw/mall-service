package com.fengchao.aoyi.client.utils;

import com.fengchao.aoyi.client.exception.AoyiClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Slf4j
public class StarHttpClient {

    public static final String STAR_GOOD_SPU_LIST = "goods/getSpuIdList";
    public static final String STAR_GOOD_SPU_DETAIL = "/goods/getSpuDetail";
    public static final String STAR_GOOD_SKU_DETAIL = "/goods/getSkuListDetailBySpuId";
    public static final String STAR_GOOD_BRAND = "/goods/findBrandList";
    public static final String STAR_GOOD_CATEGORY = "/goods/findProdCategory";
    public static final String STAR_GOOD_FIND_SKU_INVENTORY = "/goods/findSkuInventory";
    public static final String STAR_GOOD_FIND_SKU_PRICE = "/goods/findSkuSalePrice";
    public static final String STAR_ADDRESS_INFO = "/address/getAddressInfo";
    public static final String STAR_GOOD_HOLD_SKU_INVENTORY = "/goods/preHoldSkuInventory";
    public static final String STAR_GOOD_RELEASE_SKU_INVENTORY = "/goods/releaseSkuInventory";
    public static final String STAR_ORDER_ADD_ORDER = "/order/addOrder";
    public static final String STAR_ORDER_CONFIRM_ORDER = "/order/confirmOrder";
    public static final String STAR_ORDER_APPLY_REFUND_GOODS = "/order/applyRefundAndGoods";
    public static final String STAR_ORDER_APPLY_REFUND = "/order/applyRefund";
    public static final String STAR_ORDER_FREIGHT = "/order/getOrderFreight";
    public static final String STAR_ORDER_CANCEL_APPLY_REFUND = "/order/cancelApplyRefund";
    public static final String STAR_ORDER_EXPRESS = "/order/findExpressInfoByOrderSn";
    public static final String STAR_ORDER_FIND_BY_ORDERSN = "/order/findOrderByOrderSn";
    public static final String STAR_ORDER_LOGISTICS_COMPANY = "/order/getLogisticsCompanyList";
    public static final String STAR_ORDER_RETURN_STATUS = "/order/getReturnOrderStatuts";
    public static final String STAR_ORDER_RETURN_GOODS = "/order/returnOrderGoods";

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

    public static <T> T post(Map<String, String>  body, Class<T> obj, String path, String method, String appKey, String appSecret) throws AoyiClientException{
        body = setBaseParams(body, appKey, appSecret) ;
        Form form = new Form() ;
        for (Map.Entry<String, String> entry : body.entrySet()) {
            form.param(entry.getKey(), entry.getValue());
        }
        client = createClient();
        WebTarget target = client.target(path).path(method);
        T bean = null;
        try {
            bean = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE),obj);
        } catch (Exception e) {
            log.error("Star Client error : {}", e);
            throw new AoyiClientException();
        }
        return bean;
    }

    /**
     * 设置接口基础参数
     * @param params
     * @return
     */
    private static Map<String, String> setBaseParams(Map<String, String> params, String appKey, String appSecret){
        params.put("appKey", appKey);
        params.put("currentTime", System.currentTimeMillis() + "");
        String sign = getSign(params, appSecret);
        params.put("sign", sign);
        return params;
    }

    /**
     * 获取签名
     * @param params
     * @param appSecret
     * @return
     */
    public static String getSign(Map<String, String> params, String appSecret) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!entry.getKey().equals("sign")) {
                // 拼接参数值字符串并进行utf-8解码，防止中文乱码产生
                String value = "";
                try {
                    value = URLDecoder.decode(entry.getValue(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtils.isEmpty(value)) {
                    continue;
                }
                params.put(entry.getKey(), value);
            }
        }
        // 将参数以参数名的字典升序排序
        Map<String, String> sortParams = new TreeMap<>(params);
        Set<Map.Entry<String, String>> entrys = sortParams.entrySet();
        // 遍历排序的字典,并拼接格式
        StringBuilder valueSb = new StringBuilder();
        for (Map.Entry<String, String> entry : entrys) {
            if (org.apache.commons.lang.StringUtils.isNotBlank(entry.getValue())) {
                valueSb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        valueSb.append(appSecret);
        String sign = valueSb.toString();
        try {
            sign = DigestUtils.md5Hex(sign.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sign;
    }

}
