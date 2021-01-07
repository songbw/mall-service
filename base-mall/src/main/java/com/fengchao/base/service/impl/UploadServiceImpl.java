package com.fengchao.base.service.impl;

import com.fengchao.base.dao.AyFcImagesDao;
import com.fengchao.base.feign.ProductService;
import com.fengchao.base.mapper.AyFcImagesMapper;
import com.fengchao.base.model.AyFcImages;
import com.fengchao.base.service.UploadService;
import com.fengchao.base.task.thread.pool.AsyncTask;
import com.fengchao.base.utils.CosUtil;
import com.fengchao.base.utils.JSONUtil;
import com.fengchao.base.utils.URLConnectionDownloader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private ProductService productService ;
    @Autowired
    private AyFcImagesDao ayFcImagesDao ;
    @Autowired
    private AyFcImagesMapper ayFcImagesMapper ;
    @Autowired
    private AsyncTask asyncTask ;

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
        log.debug("下载上传图片入口参数 {}", JSONUtil.toJsonString(img));
        String base = "aoyi";
        String array1[] = img.getAyImage().split(img.getType());
        // String fileName = array1[1];
        String fileName = img.getAyImage().substring(img.getAyImage().lastIndexOf("/") + 1);
        try {
            log.debug("下载上传图片 保存到本地 url:{}, fileName:{}, savePath:{}",
                    img.getAyImage(), fileName, base + img.getPath() + img.getType());
            URLConnectionDownloader.download(img.getAyImage(), fileName, base + img.getPath() + img.getType());
        } catch (Exception e) {
            log.error("下载上传图片异常:{}", e.getMessage(), e);
            productService.imageBack(img.getId(), 2) ;
        }
        log.debug("下载上传图片 本地路径:{} 上传路径:{}",
                base + img.getPath() + img.getType() + "/" + fileName, img.getPath() + img.getType() + "/" + fileName);
        CosUtil.upload(CosUtil.iWalletBucketName, new File(base + img.getPath() + img.getType() + "/" + fileName),img.getPath() + img.getType() + "/" + fileName) ;
        productService.imageBack(img.getId(), 1) ;
    }

    @Override
    public void batchDownUpload() {
        List<Future<String>> futureList = new ArrayList<Future<String>>();
        while (true) {
            List<AyFcImages> list = ayFcImagesDao.findNoUploadImage() ;
            if (list == null || list.size() == 0) {
                break;
            }
            futureList.add(asyncTask.asyncDownUpload(list, ayFcImagesMapper)) ;
        }
        //对各个线程段结果进行解析
        for (Future<String> future : futureList) {
            String str;
            if (null != future) {
                try {
                    str = future.get().toString();
                    log.debug("同步图片current thread id =" + Thread.currentThread().getName() + ",result=" + str);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("同步图片 线程运行异常！");
                }
            } else {
                log.error("同步图片 线程运行异常！");
            }
        }
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
