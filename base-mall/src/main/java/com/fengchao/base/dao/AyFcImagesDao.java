package com.fengchao.base.dao;

import com.fengchao.base.mapper.AyFcImagesMapper;
import com.fengchao.base.model.AyFcImages;
import com.fengchao.base.model.AyFcImagesExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author song
 * @Date 19-8-29 下午2:12
 */
@Component
public class AyFcImagesDao {

    private AyFcImagesMapper fcImagesMapper;

    @Autowired
    public AyFcImagesDao(AyFcImagesMapper fcImagesMapper) {
        this.fcImagesMapper = fcImagesMapper;
    }


    /**
     * 获取未上传图片列表
     * @return
     */
    public List<AyFcImages> findNoUploadImage() {
        AyFcImagesExample example = new AyFcImagesExample();
        AyFcImagesExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(0);

        PageHelper.startPage(1, 2000);
        List<AyFcImages> images = fcImagesMapper.selectByExample(example);
        PageInfo<AyFcImages> pageInfo = new PageInfo(images);
        return images;
    }

    /**
     * 更新图片上传状态
     * @param ayFcImages
     */
    public void updateStatus(AyFcImages ayFcImages) {
        AyFcImagesExample example = new AyFcImagesExample();
        AyFcImagesExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(ayFcImages.getId()) ;
        int status = ayFcImages.getStatus() ;
        ayFcImages = new AyFcImages() ;
        ayFcImages.setStatus(status);
        fcImagesMapper.updateByExampleSelective(ayFcImages, example) ;
    }
}
