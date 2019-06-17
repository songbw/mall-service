package com.fengchao.equity.mapper;

import com.fengchao.equity.model.CouponTags;

import java.util.HashMap;
import java.util.List;

public interface CouponTagsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponTags record);

    int insertSelective(CouponTags record);

    CouponTags selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponTags record);

    int updateByPrimaryKey(CouponTags record);

    int selectCount(HashMap map);

    List<CouponTags> selectLimit(HashMap map);

    List<CouponTags> selectTags(List<String> List);

    CouponTags selectByName(String name);
}