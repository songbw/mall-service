package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.EnterpriseSpu;
import com.fengchao.product.aoyi.model.EnterpriseSpuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EnterpriseSpuMapper {
    long countByExample(EnterpriseSpuExample example);

    int deleteByExample(EnterpriseSpuExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EnterpriseSpu record);

    int insertSelective(EnterpriseSpu record);

    List<EnterpriseSpu> selectByExample(EnterpriseSpuExample example);

    EnterpriseSpu selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EnterpriseSpu record, @Param("example") EnterpriseSpuExample example);

    int updateByExample(@Param("record") EnterpriseSpu record, @Param("example") EnterpriseSpuExample example);

    int updateByPrimaryKeySelective(EnterpriseSpu record);

    int updateByPrimaryKey(EnterpriseSpu record);
}