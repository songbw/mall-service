package com.fengchao.base.controller;

import com.fengchao.base.bean.AyFcImages;
import com.fengchao.base.bean.OperaResponse;
import com.fengchao.base.bean.OperaResult;
import com.fengchao.base.config.SMSConfig;
import com.fengchao.base.service.UploadService;
import com.fengchao.base.utils.Config;
import com.fengchao.base.utils.CosUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

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
        result.getData().put("baseUrl", SMSConfig.TENT_cosBaseUrl) ;
        result.getData().put("cdnUrl", SMSConfig.TENT_cosCDNUrl) ;
        return result;
    }

    @PostMapping("/down/upload")
    private OperaResult downUpload(@RequestBody AyFcImages images) {
        OperaResult result = new OperaResult();
        service.downUpload(images) ;
        return result;
    }

    @GetMapping("/cos/sts")
    private String getTempKey(){
        OperaResponse<String> result = new OperaResponse<>();
        JSONObject cred = CosUtil.tryGetTempkey();
        if (null == cred){
            result.setCode(400);
            result.setMsg("fail");
        }else{
            //result.setData(cred);
            result.setMsg("ok");
        }
        return cred.toString();
    }

}
