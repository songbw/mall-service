package com.fengchao.equity.mapper;

import com.fengchao.equity.model.CouponTags;
import com.fengchao.equity.model.CouponTagsExample;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CouponTagsMapper {
    long countByExample(CouponTagsExample example);

    int deleteByExample(CouponTagsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CouponTags record);

    int insertSelective(CouponTags record);

    List<CouponTags> selectByExample(CouponTagsExample example);

    CouponTags selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CouponTags record, @Param("example") CouponTagsExample example);

    int updateByExample(@Param("record") CouponTags record, @Param("example") CouponTagsExample example);

    int updateByPrimaryKeySelective(CouponTags record);

    int updateByPrimaryKey(CouponTags record);

    CouponTags selectByName(String name);

    int selectCount(HashMap map);

    List<CouponTags> selectLimit(HashMap map);

    List<CouponTags> selectTags(List<String> ids);
}
