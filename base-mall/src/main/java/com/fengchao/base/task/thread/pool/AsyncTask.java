package com.fengchao.base.task.thread.pool;

import com.fengchao.base.mapper.AyFcImagesMapper;
import com.fengchao.base.model.AyFcImages;
import com.fengchao.base.utils.CosUtil;
import com.fengchao.base.utils.JSONUtil;
import com.fengchao.base.utils.URLConnectionDownloader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author songbw
 * @date 2021/1/7 16:10
 */
@Slf4j
@Component
public class AsyncTask {

    @Async("asyncServiceExecutor")
    public Future<String> asyncDownUpload(List<AyFcImages> list, AyFcImagesMapper ayFcImagesMapper){
        //声明future对象
        Future<String> result = new AsyncResult<String>("");
        for(AyFcImages img : list) {
            img.setStatus(1);
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
                //
                img.setStatus(2);
            }
            log.debug("下载上传图片 本地路径:{} 上传路径:{}",
                    base + img.getPath() + img.getType() + "/" + fileName, img.getPath() + img.getType() + "/" + fileName);
            CosUtil.upload(CosUtil.iWalletBucketName, new File(base + img.getPath() + img.getType() + "/" + fileName),img.getPath() + img.getType() + "/" + fileName) ;
            // 更新
            ayFcImagesMapper.updateByPrimaryKey(img) ;

        }
        return result ;

    }
}
