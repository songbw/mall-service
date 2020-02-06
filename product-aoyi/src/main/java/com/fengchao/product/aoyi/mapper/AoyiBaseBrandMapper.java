package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.AoyiBaseBrand;
import com.fengchao.product.aoyi.model.AoyiBaseBrandExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AoyiBaseBrandMapper {
    long countByExample(AoyiBaseBrandExample example);

    int deleteByExample(AoyiBaseBrandExample example);

    int deleteByPrimaryKey(Integer brandId);

    int insert(AoyiBaseBrand record);

    int insertSelective(AoyiBaseBrand record);

    List<AoyiBaseBrand> selectByExampleWithBLOBs(AoyiBaseBrandExample example);

    List<AoyiBaseBrand> selectByExample(AoyiBaseBrandExample example);

    AoyiBaseBrand selectByPrimaryKey(Integer brandId);

    int updateByExampleSelective(@Param("record") AoyiBaseBrand record, @Param("example") AoyiBaseBrandExample example);

    int updateByExampleWithBLOBs(@Param("record") AoyiBaseBrand record, @Param("example") AoyiBaseBrandExample example);

    int updateByExample(@Param("record") AoyiBaseBrand record, @Param("example") AoyiBaseBrandExample example);

    int updateByPrimaryKeySelective(AoyiBaseBrand record);

    int updateByPrimaryKeyWithBLOBs(AoyiBaseBrand record);

    int updateByPrimaryKey(AoyiBaseBrand record);
}