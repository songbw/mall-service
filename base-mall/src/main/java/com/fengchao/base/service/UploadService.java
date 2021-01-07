package com.fengchao.base.service;

import com.fengchao.base.model.AyFcImages;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传服务
 */
public interface UploadService {

    String upload(MultipartFile file) ;

    String uploadRelative(MultipartFile file, String path) ;

    void downUpload(AyFcImages images) ;

    void batchDownUpload() ;
}
