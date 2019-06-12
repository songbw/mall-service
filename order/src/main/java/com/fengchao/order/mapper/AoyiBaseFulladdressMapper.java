package com.fengchao.order.mapper;

import com.fengchao.order.model.AoyiBaseFulladdress;

import java.util.HashMap;
import java.util.List;

public interface AoyiBaseFulladdressMapper {
    int deleteByPrimaryKey(Integer aid);

    int insert(AoyiBaseFulladdress record);

    int insertSelective(AoyiBaseFulladdress record);

    AoyiBaseFulladdress selectByPrimaryKey(Integer aid);

    AoyiBaseFulladdress selectById(AoyiBaseFulladdress record);

    int updateByPrimaryKeySelective(AoyiBaseFulladdress record);

    int updateByPrimaryKey(AoyiBaseFulladdress record);

    List<AoyiBaseFulladdress> selectByLevel(HashMap map) ;

    AoyiBaseFulladdress selectByCode(HashMap map) ;
}