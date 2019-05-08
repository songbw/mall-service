package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.AoyiBaseFulladdress;

public interface AoyiBaseFulladdressMapper {
    int deleteByPrimaryKey(Integer aid);

    int insert(AoyiBaseFulladdress record);

    int insertSelective(AoyiBaseFulladdress record);

    AoyiBaseFulladdress selectByPrimaryKey(Integer aid);

    int updateByPrimaryKeySelective(AoyiBaseFulladdress record);

    int updateByPrimaryKey(AoyiBaseFulladdress record);
}