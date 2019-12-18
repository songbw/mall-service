package com.fengchao.sso.mapper;

import com.fengchao.sso.model.BalanceDetail;
import com.fengchao.sso.model.BalanceDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BalanceDetailMapper {
    long countByExample(BalanceDetailExample example);

    int deleteByExample(BalanceDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BalanceDetail record);

    int insertSelective(BalanceDetail record);

    List<BalanceDetail> selectByExample(BalanceDetailExample example);

    BalanceDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BalanceDetail record, @Param("example") BalanceDetailExample example);

    int updateByExample(@Param("record") BalanceDetail record, @Param("example") BalanceDetailExample example);

    int updateByPrimaryKeySelective(BalanceDetail record);

    int updateByPrimaryKey(BalanceDetail record);
}