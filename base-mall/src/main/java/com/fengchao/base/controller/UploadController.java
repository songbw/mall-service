package com.fengchao.base.controller;

import com.fengchao.base.bean.AyFcImages;
import com.fengchao.base.bean.OperaResult;
import com.fengchao.base.service.UploadService;
import com.fengchao.base.utils.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UploadController {

    @Autowired
    private UploadService service;

    @PostMapping
    private OperaResult upload(@RequestParam("/upload/file") MultipartFile file, OperaResult result) {
        result.getData().put("url", service.upload(file)) ;
        return result;
    }

    @PostMapping("/upload/relative")
    private OperaResult upload(@RequestParam("file") MultipartFile file, String pathName, OperaResult result) {
        result.getData().put("url", service.uploadRelative(file, pathName)) ;
        return result;
    }

    @GetMapping("/cos/url")
    private OperaResult url(OperaResult result) {
        String cdnUrl = Config.getString("base.cdn.url");
        String baseUrl = Config.getString("base.url");
        result.getData().put("baseUrl", baseUrl) ;
        result.getData().put("cdnUrl", cdnUrl) ;
        return result;
    }

    @PostMapping("/down/upload")
    private OperaResult downUpload(@RequestBody AyFcImages images) {
        OperaResult result = new OperaResult();
        new Thread(images.getPath()){
            public void run(){
                service.downUpload(images) ;
            }
        }.start();
        return result;
    }
}
