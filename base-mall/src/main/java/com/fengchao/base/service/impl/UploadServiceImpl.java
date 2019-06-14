package com.fengchao.base.service.impl;

import com.fengchao.base.service.UploadService;
import com.fengchao.base.utils.CosUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

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
