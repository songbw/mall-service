package com.fengchao.order.mapper;

import com.fengchao.order.model.KuaidiCode;
import com.fengchao.order.model.KuaidiCodeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KuaidiCodeMapper {
    long countByExample(KuaidiCodeExample example);

    int deleteByExample(KuaidiCodeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(KuaidiCode record);

    int insertSelective(KuaidiCode record);

    List<KuaidiCode> selectByExample(KuaidiCodeExample example);

    KuaidiCode selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") KuaidiCode record, @Param("example") KuaidiCodeExample example);

    int updateByExample(@Param("record") KuaidiCode record, @Param("example") KuaidiCodeExample example);

    int updateByPrimaryKeySelective(KuaidiCode record);

    int updateByPrimaryKey(KuaidiCode record);
}