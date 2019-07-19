package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexExample;
import com.fengchao.product.aoyi.model.AoyiProdIndexWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AoyiProdIndexMapper {
    long countByExample(AoyiProdIndexExample example);

    int deleteByExample(AoyiProdIndexExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AoyiProdIndexWithBLOBs record);

    int insertSelective(AoyiProdIndexWithBLOBs record);

    List<AoyiProdIndexWithBLOBs> selectByExampleWithBLOBs(AoyiProdIndexExample example);

    List<AoyiProdIndex> selectByExample(AoyiProdIndexExample example);

    AoyiProdIndexWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AoyiProdIndexWithBLOBs record, @Param("example") AoyiProdIndexExample example);

    int updateByExampleWithBLOBs(@Param("record") AoyiProdIndexWithBLOBs record, @Param("example") AoyiProdIndexExample example);

    int updateByExample(@Param("record") AoyiProdIndex record, @Param("example") AoyiProdIndexExample example);

    int updateByPrimaryKeySelective(AoyiProdIndexWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(AoyiProdIndexWithBLOBs record);

    int updateByPrimaryKey(AoyiProdIndex record);
}