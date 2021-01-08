package com.fengchao.base.mapper;

import com.fengchao.base.model.AyFcImages;
import com.fengchao.base.model.AyFcImagesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AyFcImagesMapper {
    long countByExample(AyFcImagesExample example);

    int deleteByExample(AyFcImagesExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AyFcImages record);

    int insertSelective(AyFcImages record);

    List<AyFcImages> selectByExample(AyFcImagesExample example);

    AyFcImages selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AyFcImages record, @Param("example") AyFcImagesExample example);

    int updateByExample(@Param("record") AyFcImages record, @Param("example") AyFcImagesExample example);

    int updateByPrimaryKeySelective(AyFcImages record);

    int updateByPrimaryKey(AyFcImages record);
}