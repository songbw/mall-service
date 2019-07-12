package com.fengchao.equity.mapper;

import com.fengchao.equity.model.GroupTeam;
import com.fengchao.equity.model.GroupTeamExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GroupTeamMapper {
    long countByExample(GroupTeamExample example);

    int deleteByExample(GroupTeamExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GroupTeam record);

    int insertSelective(GroupTeam record);

    List<GroupTeam> selectByExample(GroupTeamExample example);

    GroupTeam selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GroupTeam record, @Param("example") GroupTeamExample example);

    int updateByExample(@Param("record") GroupTeam record, @Param("example") GroupTeamExample example);

    int updateByPrimaryKeySelective(GroupTeam record);

    int updateByPrimaryKey(GroupTeam record);
}