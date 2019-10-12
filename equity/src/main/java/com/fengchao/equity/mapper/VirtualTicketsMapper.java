package com.fengchao.equity.mapper;

import com.fengchao.equity.model.VirtualTickets;
import com.fengchao.equity.model.VirtualTicketsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VirtualTicketsMapper {
    long countByExample(VirtualTicketsExample example);

    int deleteByExample(VirtualTicketsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(VirtualTickets record);

    int insertSelective(VirtualTickets record);

    List<VirtualTickets> selectByExample(VirtualTicketsExample example);

    VirtualTickets selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VirtualTickets record, @Param("example") VirtualTicketsExample example);

    int updateByExample(@Param("record") VirtualTickets record, @Param("example") VirtualTicketsExample example);

    int updateByPrimaryKeySelective(VirtualTickets record);

    int updateByPrimaryKey(VirtualTickets record);
}