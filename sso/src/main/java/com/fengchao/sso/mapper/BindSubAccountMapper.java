package com.fengchao.sso.mapper;

import com.fengchao.sso.model.BindSubAccount;
import com.fengchao.sso.model.BindSubAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BindSubAccountMapper {
    long countByExample(BindSubAccountExample example);

    int deleteByExample(BindSubAccountExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BindSubAccount record);

    int insertSelective(BindSubAccount record);

    List<BindSubAccount> selectByExample(BindSubAccountExample example);

    BindSubAccount selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BindSubAccount record, @Param("example") BindSubAccountExample example);

    int updateByExample(@Param("record") BindSubAccount record, @Param("example") BindSubAccountExample example);

    int updateByPrimaryKeySelective(BindSubAccount record);

    int updateByPrimaryKey(BindSubAccount record);
}