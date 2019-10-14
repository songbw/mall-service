package com.fengchao.equity.mapper;

import com.fengchao.equity.model.VirtualTickets;
import com.fengchao.equity.model.VirtualTicketsExample;
import com.fengchao.equity.model.VirtualTicketsX;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VirtualTicketsMapperX {
    long countByExample(VirtualTicketsExample example);

    int deleteByExample(VirtualTicketsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(VirtualTickets record);

    int insertSelective(VirtualTickets record);

    List<VirtualTicketsX> selectByExample(VirtualTicketsExample example);

    VirtualTicketsX selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VirtualTickets record, @Param("example") VirtualTicketsExample example);

    int updateByExample(@Param("record") VirtualTickets record, @Param("example") VirtualTicketsExample example);

    int updateByPrimaryKeySelective(VirtualTickets record);

    int updateByPrimaryKey(VirtualTickets record);

    VirtualTicketsX selectByCode(String code);
}