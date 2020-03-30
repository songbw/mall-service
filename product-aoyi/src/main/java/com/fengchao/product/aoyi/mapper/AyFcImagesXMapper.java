package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.AyFcImages;

import java.util.List;

public interface AyFcImagesXMapper {

    /**
     * 批量插入
     *
     * @param ayFcImagesList
     */
    void batchInsert(List<AyFcImages> ayFcImagesList);
}