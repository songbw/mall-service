package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.KioskImg;
import com.fengchao.product.aoyi.model.KioskImgExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KioskImgMapper {
    long countByExample(KioskImgExample example);

    int deleteByExample(KioskImgExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(KioskImg record);

    int insertSelective(KioskImg record);

    List<KioskImg> selectByExample(KioskImgExample example);

    KioskImg selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") KioskImg record, @Param("example") KioskImgExample example);

    int updateByExample(@Param("record") KioskImg record, @Param("example") KioskImgExample example);

    int updateByPrimaryKeySelective(KioskImg record);

    int updateByPrimaryKey(KioskImg record);
}