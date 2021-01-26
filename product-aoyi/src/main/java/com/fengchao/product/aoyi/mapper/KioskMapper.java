package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.Kiosk;
import com.fengchao.product.aoyi.model.KioskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KioskMapper {
    long countByExample(KioskExample example);

    int deleteByExample(KioskExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Kiosk record);

    int insertSelective(Kiosk record);

    List<Kiosk> selectByExample(KioskExample example);

    Kiosk selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Kiosk record, @Param("example") KioskExample example);

    int updateByExample(@Param("record") Kiosk record, @Param("example") KioskExample example);

    int updateByPrimaryKeySelective(Kiosk record);

    int updateByPrimaryKey(Kiosk record);
}