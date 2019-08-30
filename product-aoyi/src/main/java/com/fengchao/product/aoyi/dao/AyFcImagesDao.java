package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.mapper.AyFcImagesMapper;
import com.fengchao.product.aoyi.model.AyFcImages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
