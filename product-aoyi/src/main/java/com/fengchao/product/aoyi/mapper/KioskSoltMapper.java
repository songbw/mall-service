package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.KioskSolt;
import com.fengchao.product.aoyi.model.KioskSoltExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KioskSoltMapper {
    long countByExample(KioskSoltExample example);

    int deleteByExample(KioskSoltExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(KioskSolt record);

    int insertSelective(KioskSolt record);

    List<KioskSolt> selectByExample(KioskSoltExample example);

    KioskSolt selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") KioskSolt record, @Param("example") KioskSoltExample example);

    int updateByExample(@Param("record") KioskSolt record, @Param("example") KioskSoltExample example);

    int updateByPrimaryKeySelective(KioskSolt record);

    int updateByPrimaryKey(KioskSolt record);
}