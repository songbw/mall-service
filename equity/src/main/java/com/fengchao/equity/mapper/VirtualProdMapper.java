package com.fengchao.equity.mapper;

import com.fengchao.equity.model.VirtualProd;
import com.fengchao.equity.model.VirtualProdExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VirtualProdMapper {
    long countByExample(VirtualProdExample example);

    int deleteByExample(VirtualProdExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(VirtualProd record);

    int insertSelective(VirtualProd record);

    List<VirtualProd> selectByExample(VirtualProdExample example);

    VirtualProd selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VirtualProd record, @Param("example") VirtualProdExample example);

    int updateByExample(@Param("record") VirtualProd record, @Param("example") VirtualProdExample example);

    int updateByPrimaryKeySelective(VirtualProd record);

    int updateByPrimaryKey(VirtualProd record);
}