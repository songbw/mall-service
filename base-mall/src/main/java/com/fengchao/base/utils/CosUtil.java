package com.fengchao.base.utils;

import com.fengchao.base.config.SMSConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.tencent.cloud.CosStsClient;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.TreeMap;

@Slf4j
@Component
public class CosUtil {
    public static String baseAoyiProdUrl = "http://aoyiprod-1252099010.cossh.myqcloud.com/";
    public static String iWalletUrlT = "https://iwallet-1258175138.cos.ap-beijing.myqcloud.com";
    public static String iWalletBucketName = "iwallet-1258175138";
    private static COSClient cosClient ;

    public static COSClient getInstance() {
        if (null != cosClient)
            return cosClient;
        COSCredentials cred =  new BasicCOSCredentials(SMSConfig.TENT_cosSecretId,SMSConfig.TENT_cosSecretKey) ;
        ClientConfig clientConfig = new ClientConfig(new Region(SMSConfig.TENT_cosRegion));
        cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }

    public static String upload(String bucketName, File file, String path) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, file);
        PutObjectResult putObjectResult = CosUtil.getInstance().putObject(putObjectRequest);
        log.info("腾讯云COS上传图片返回： {}", JSONUtil.toJsonString(putObjectResult));
        return path;
    }

    public static JSONObject tryGetTempkey(){
        TreeMap<String, Object> config = new TreeMap<String, Object>();

        try {
            // 替换为您的 SecretId
//            config.put("SecretId", "AKIDH97EBUxbYxGOJTtTSU9mGsY0mVNjpu8I");
            config.put("SecretId", SMSConfig.TENT_cosSecretId);
            // 替换为您的 SecretKey
//            config.put("SecretKey", "AC7UN8kYGn8G8C4ToKfnkhYGLhzCDlP8");
            config.put("SecretKey", SMSConfig.TENT_cosSecretKey);

            // 临时密钥有效时长，单位是秒，默认1800秒，最长可设定有效期为7200秒
            config.put("durationSeconds", 1800);

            // 换成您的 bucket
            config.put("bucket", SMSConfig.TENT_iWalletBucketName);
            // 换成 bucket 所在地区
            config.put("region", SMSConfig.TENT_cosRegion);

            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径，例子：a.jpg 或者 a/* 或者 * 。
            // 如果填写了“*”，将允许用户访问所有资源；除非业务需要，否则请按照最小权限原则授予用户相应的访问权限范围。
            config.put("allowPrefix", "*");

            // 密钥的权限列表。简单上传、表单上传和分片上传需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[] {
                    // 简单上传
                    "name/cos:PutObject",
                    // 表单上传、小程序上传
                    "name/cos:PostObject",
                    // 分片上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);

            JSONObject credential = CosStsClient.getCredential(config);
            //成功返回临时密钥信息，如下打印密钥信息
            log.info("临时密钥信息 {}",credential.toString());

            return credential;

        } catch (Exception e) {
            //失败抛出异常
            log.error("异常 no valid secret !  {}",e.getMessage(),e);

            return null;
        }
    }

}
