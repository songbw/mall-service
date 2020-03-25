package com.fengchao.aoyi.client.utils;

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

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class FengchaoHttpClient {

    private static final RequestConfig config;

    public static final String DEFAULT_SEND_CHARSET = "UTF-8";

    public static final String DEFAULT_RES_CHARSET = "UTF-8";

    public static final Integer HTTP_CONNECT_TIMOUT = 1000 * 2; // 3秒

    public static final Integer HTTP_SOCKET_TIMEOUT = 1000 * 2; // 3秒

    static {
        config = RequestConfig.custom()
                .setConnectTimeout(HTTP_CONNECT_TIMOUT)
                .setSocketTimeout(HTTP_SOCKET_TIMEOUT)
                .build();
    }

    public static String doPost(Map<String, Object> params, String url) throws Exception {
        return doPost(params, url, DEFAULT_SEND_CHARSET, DEFAULT_RES_CHARSET, false);
    }

    public static String doPostSSL(Map<String, Object> params, String url) throws Exception {
        return doPost(params, url, DEFAULT_SEND_CHARSET, DEFAULT_RES_CHARSET, true);
    }

    public static String doPost(String body, Map<String, String> headers, String url) throws Exception {
        return doPost(body, url, headers, DEFAULT_SEND_CHARSET, DEFAULT_RES_CHARSET, false);
    }

    /**
     * HTTP Post 获取内容
     *
     * @param params     请求的参数
     * @param url        请求的url地址 ?之前的地址
     * @param reqCharset 编码格式
     * @param resCharset 编码格式
     * @param sslConnect true:需要ssl连接  false:不需要ssl
     *
     * @return 页面内容
     * @throws Exception
     */
    public static String doPost(Map<String, Object> params, String url,
                                String reqCharset, String resCharset, boolean sslConnect) throws Exception {
        CloseableHttpClient httpClient = null;

        if (sslConnect) { // 需要 ssl 连接
            httpClient = getSingleSSLConnection();
        } else {
            httpClient = HttpClients.createDefault();
        }

        CloseableHttpResponse response = null;

        try {
            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    String value = String.valueOf(entry.getValue());
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
            }
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
            httpPost.addHeader("Connection", "close");
            if (pairs != null && pairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, reqCharset == null ? DEFAULT_SEND_CHARSET : reqCharset));
            }

            // 执行请求
            response = execute(httpClient, httpPost);

            //
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                Header header = response.getFirstHeader("location");
                if (header != null) {
                    log.warn("http post 请求 出错:{}", header.getValue());
                }
                throw new Exception("http post 请求 出错!!");
            }

            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, resCharset == null ? DEFAULT_RES_CHARSET : resCharset);
            }
            EntityUtils.consume(entity);

            return result;
        } catch (Exception e) {
            log.error("http post 请求异常:{}", e.getMessage(), e);
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
                log.error("http post 请求 关闭资源异常:{}", e.getMessage(), e);
                throw new Exception(e);
            }
        }
    }

    public static String doPost(String body, String url, Map<String, String> headers,
                                String reqCharset, String resCharset, boolean sslConnect) throws Exception {
        CloseableHttpClient httpClient = null;

        if (sslConnect) { // 需要 ssl 连接
            httpClient = getSingleSSLConnection();
        } else {
            httpClient = HttpClients.createDefault();
        }

        CloseableHttpResponse response = null;

        try {
            HttpPost httpPost = new HttpPost(url);

            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            httpPost.setEntity(new StringEntity(body, reqCharset));

            // 执行请求
            response = execute(httpClient, httpPost);

            //
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new Exception("http post 请求 出错!!");
            }

            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, resCharset == null ? DEFAULT_RES_CHARSET : resCharset);
            }
            EntityUtils.consume(entity);

            return result;
        } catch (Exception e) {
            log.error("http post 请求异常:{}", e.getMessage(), e);
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
                log.error("http post 请求 关闭资源异常:{}", e.getMessage(), e);
                throw new Exception(e);
            }
        }
    }

    /**
     *
     * @param httpClient
     * @param httpPost
     * @return
     */
    public static CloseableHttpResponse execute(CloseableHttpClient httpClient, HttpPost httpPost) throws Exception {
        try {
            // 执行请求
            CloseableHttpResponse response = httpClient.execute(httpPost);
            log.info("http post 请求 返回:{}", JSONUtil.toJsonString(response));

            return response;
        } catch (Exception e) {
            log.error("http post 请求异常! :{}", e.getMessage(), e);

            throw e;
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
            log.error("http post 请求 创建ssl连接异常:{}", e.getMessage(), e);
            throw new Exception(e);
        }
    }


}
