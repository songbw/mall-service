package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.AyFcImagesMapper;
import com.fengchao.product.aoyi.model.AyFcImages;
import com.fengchao.product.aoyi.model.AyFcImagesExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author song
 * @Date 19-8-29 下午2:12
 */
@Component
public class AyFcImagesDao {

    private AyFcImagesMapper mapper;

    @Autowired
    public AyFcImagesDao(AyFcImagesMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 新增
     * @param bean
     * @return
     */
    public Long insert(AyFcImages bean) {
        mapper.insertSelective(bean);
        return bean.getId();
    }

    /**
     * 获取未上传图片列表
     * @return
     */
    public List<AyFcImages> findNoUploadImage() {
        AyFcImagesExample example = new AyFcImagesExample();
        AyFcImagesExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(0);

        PageHelper.startPage(1, 5000);
        List<AyFcImages> images = mapper.selectByExample(example);
        PageInfo<AyFcImages> pageInfo = new PageInfo(images);
        return images;
    }

    /**
     * 更新图片上传状态
     * @param ayFcImages
     */
    public void updateStatus(AyFcImages ayFcImages) {
        mapper.updateByPrimaryKeySelective(ayFcImages) ;
    }
}
