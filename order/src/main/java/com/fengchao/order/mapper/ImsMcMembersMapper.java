package com.fengchao.order.mapper;

import com.fengchao.order.model.ImsMcMembers;
import com.fengchao.order.model.ImsMcMembersExample;
import com.fengchao.order.model.ImsMcMembersWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ImsMcMembersMapper {
    long countByExample(ImsMcMembersExample example);

    int deleteByExample(ImsMcMembersExample example);

    int deleteByPrimaryKey(Integer uid);

    int insert(ImsMcMembersWithBLOBs record);

    int insertSelective(ImsMcMembersWithBLOBs record);

    List<ImsMcMembersWithBLOBs> selectByExampleWithBLOBs(ImsMcMembersExample example);

    List<ImsMcMembers> selectByExample(ImsMcMembersExample example);

    ImsMcMembersWithBLOBs selectByPrimaryKey(Integer uid);

    int updateByExampleSelective(@Param("record") ImsMcMembersWithBLOBs record, @Param("example") ImsMcMembersExample example);

    int updateByExampleWithBLOBs(@Param("record") ImsMcMembersWithBLOBs record, @Param("example") ImsMcMembersExample example);

    int updateByExample(@Param("record") ImsMcMembers record, @Param("example") ImsMcMembersExample example);

    int updateByPrimaryKeySelective(ImsMcMembersWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ImsMcMembersWithBLOBs record);

    int updateByPrimaryKey(ImsMcMembers record);
}