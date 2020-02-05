package com.fengchao.sso.mapper;

import com.fengchao.sso.model.SLogin;
import com.fengchao.sso.model.SLoginExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SLoginMapper {
    long countByExample(SLoginExample example);

    int deleteByExample(SLoginExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SLogin record);

    int insertSelective(SLogin record);

    List<SLogin> selectByExample(SLoginExample example);

    SLogin selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SLogin record, @Param("example") SLoginExample example);

    int updateByExample(@Param("record") SLogin record, @Param("example") SLoginExample example);

    int updateByPrimaryKeySelective(SLogin record);

    int updateByPrimaryKey(SLogin record);
}