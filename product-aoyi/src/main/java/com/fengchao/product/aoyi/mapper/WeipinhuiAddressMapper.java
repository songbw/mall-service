package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.WeipinhuiAddress;
import com.fengchao.product.aoyi.model.WeipinhuiAddressExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WeipinhuiAddressMapper {
    long countByExample(WeipinhuiAddressExample example);

    int deleteByExample(WeipinhuiAddressExample example);

    int deleteByPrimaryKey(Long id);

    int insert(WeipinhuiAddress record);

    int insertSelective(WeipinhuiAddress record);

    List<WeipinhuiAddress> selectByExample(WeipinhuiAddressExample example);

    WeipinhuiAddress selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") WeipinhuiAddress record, @Param("example") WeipinhuiAddressExample example);

    int updateByExample(@Param("record") WeipinhuiAddress record, @Param("example") WeipinhuiAddressExample example);

    int updateByPrimaryKeySelective(WeipinhuiAddress record);

    int updateByPrimaryKey(WeipinhuiAddress record);
}