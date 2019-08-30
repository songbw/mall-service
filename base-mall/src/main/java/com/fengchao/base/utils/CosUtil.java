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

    private  static COSCredentials cred =  new BasicCOSCredentials("AKIDmMgkY6GO9h16vUQsgEW8WfZGjyzlq8f4","c4OVP5ZqxrY2FCtKUO2QYR3QXP1NVO3T") ;
    private static ClientConfig clientConfig = new ClientConfig(new Region("ap-beijing"));
    private static COSClient cosClient = new COSClient(cred, clientConfig);

    public static String upload(String bucketName, File file, String path) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, file);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        return path;
    }

}
