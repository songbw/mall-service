package com.fengchao.base.service.impl;

import com.fengchao.base.bean.AyFcImages;
import com.fengchao.base.service.UploadService;
import com.fengchao.base.utils.CosUtil;
import com.fengchao.base.utils.URLConnectionDownloader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class UploadServiceImpl implements UploadService {

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
    public void downUpload(List<AyFcImages> images) {
        String base = "aoyi";
        images.forEach(img -> {
            String array1[] = img.getAyImage().split(img.getType());
            String fileName = array1[1];
            try {
                URLConnectionDownloader.download(img.getAyImage(), fileName, base + img.getPath() + img.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            CosUtil.upload(CosUtil.iWalletBucketName, new File(base + img.getPath() + img.getType() + fileName),img.getPath() + img.getType() + fileName) ;
        });
    }

    private File saveFile(MultipartFile file) {
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
