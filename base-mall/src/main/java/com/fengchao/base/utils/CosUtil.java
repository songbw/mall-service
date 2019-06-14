package com.fengchao.base.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

import java.io.File;

public class CosUtil {
    public static String baseAoyiProdUrl = "http://aoyiprod-1252099010.cossh.myqcloud.com/";
    public static String iWalletUrlT = "https://iwallet-1258175138.cos.ap-beijing.myqcloud.com";
    public static String iWalletBucketName = "iwallet-1258175138";

    private  static COSCredentials cred =  new BasicCOSCredentials("AKIDb6fBh75Ah3FG6lpeqqgLqJdnwxrb6eEl","Ws23sVxFMapz0sZ7UzWzOh9pW9hAXjka") ;
    private static ClientConfig clientConfig = new ClientConfig(new Region("ap-beijing"));
    private static COSClient cosClient = new COSClient(cred, clientConfig);

    public static String upload(String bucketName, File file, String path) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, file);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        return path;
    }

}
