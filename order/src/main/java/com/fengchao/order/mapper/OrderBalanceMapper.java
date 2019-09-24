package com.fengchao.order.mapper;

import com.fengchao.order.model.OrderBalance;
import com.fengchao.order.model.OrderBalanceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderBalanceMapper {
    long countByExample(OrderBalanceExample example);

    int deleteByExample(OrderBalanceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderBalance record);

    int insertSelective(OrderBalance record);

    List<OrderBalance> selectByExample(OrderBalanceExample example);

    OrderBalance selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrderBalance record, @Param("example") OrderBalanceExample example);

    int updateByExample(@Param("record") OrderBalance record, @Param("example") OrderBalanceExample example);

    int updateByPrimaryKeySelective(OrderBalance record);

    int updateByPrimaryKey(OrderBalance record);
}