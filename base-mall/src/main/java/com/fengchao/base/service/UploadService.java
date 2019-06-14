package com.fengchao.base.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 上传服务
 */
public interface UploadService {

    String upload(MultipartFile file) ;

    String uploadRelative(MultipartFile file, String path) ;
}
