package com.fengchao.base.service.impl;

import com.fengchao.base.bean.AyFcImages;
import com.fengchao.base.feign.ProductService;
import com.fengchao.base.service.UploadService;
import com.fengchao.base.utils.CosUtil;
import com.fengchao.base.utils.JSONUtil;
import com.fengchao.base.utils.URLConnectionDownloader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private ProductService productService ;

    @Override
    public String upload(MultipartFile file) {
        String path = "/" + new Date().getTime() + "." + file.getOriginalFilename().split("\\.")[1];
        String url = CosUtil.upload(CosUtil.iWalletBucketName,saveFile(file),path) ;
        return url;
    }

    @Override
    public String uploadRelative(MultipartFile file, String path) {

        path = "/" + path + "/" + new Date().getTime() + "." + file.getOriginalFilename().split("\\.")[1];
        String url = CosUtil.upload(CosUtil.iWalletBucketName,saveFile(file),path) ;
        return url;
    }

    @Override
    public void downUpload(AyFcImages img) {
        log.info("下载上传图片入口参数 {}", JSONUtil.toJsonString(img));
        String base = "aoyi";
        String array1[] = img.getAyImage().split(img.getType());
        // String fileName = array1[1];
        String fileName = img.getAyImage().substring(img.getAyImage().lastIndexOf("/") + 1);
        try {
            log.info("下载上传图片 保存到本地 url:{}, fileName:{}, savePath:{}",
                    img.getAyImage(), fileName, base + img.getPath() + img.getType());
            URLConnectionDownloader.download(img.getAyImage(), fileName, base + img.getPath() + img.getType());
        } catch (Exception e) {
            log.error("下载上传图片异常:{}", e.getMessage(), e);
            productService.imageBack(img.getId(), 2) ;
        }
        log.info("下载上传图片 本地路径:{} 上传路径:{}",
                base + img.getPath() + img.getType() + "/" + fileName, img.getPath() + img.getType() + "/" + fileName);
        CosUtil.upload(CosUtil.iWalletBucketName, new File(base + img.getPath() + img.getType() + "/" + fileName),img.getPath() + img.getType() + "/" + fileName) ;
        productService.imageBack(img.getId(), 1) ;
    }

    private File saveFile(MultipartFile file) {
        File sf=new File("file");
        if(!sf.exists()){
            sf.mkdirs();
        }
        File convFile = new File("file/"+ file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile) ;
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convFile;
    }
}
