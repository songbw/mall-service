package com.fengchao.equity.mapper;

import com.fengchao.equity.model.VirtualProd;
import com.fengchao.equity.model.VirtualProdExample;
import com.fengchao.equity.model.VirtualProdX;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VirtualProdMapperX {
    long countByExample(VirtualProdExample example);

    int deleteByExample(VirtualProdExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(VirtualProd record);

    int insertSelective(VirtualProd record);

    List<VirtualProdX> selectByExample(VirtualProdExample example);

    VirtualProdX selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VirtualProd record, @Param("example") VirtualProdExample example);

    int updateByExample(@Param("record") VirtualProd record, @Param("example") VirtualProdExample example);

    int updateByPrimaryKeySelective(VirtualProd record);

    int updateByPrimaryKey(VirtualProd record);

    VirtualProdX selectByVirtualProdMpu(String mpu);
}