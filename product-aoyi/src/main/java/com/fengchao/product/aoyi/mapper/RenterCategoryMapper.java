package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.RenterCategory;
import com.fengchao.product.aoyi.model.RenterCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RenterCategoryMapper {
    long countByExample(RenterCategoryExample example);

    int deleteByExample(RenterCategoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RenterCategory record);

    int insertSelective(RenterCategory record);

    List<RenterCategory> selectByExample(RenterCategoryExample example);

    RenterCategory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RenterCategory record, @Param("example") RenterCategoryExample example);

    int updateByExample(@Param("record") RenterCategory record, @Param("example") RenterCategoryExample example);

    int updateByPrimaryKeySelective(RenterCategory record);

    int updateByPrimaryKey(RenterCategory record);
}