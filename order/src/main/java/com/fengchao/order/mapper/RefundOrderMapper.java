package com.fengchao.order.mapper;

import com.fengchao.order.model.RefundOrder;

import java.util.List;

public interface RefundOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RefundOrder record);

    int insertSelective(RefundOrder record);

    RefundOrder selectByPrimaryKey(Integer id);

    List<RefundOrder> selectBySubOrderId(RefundOrder record);

    int updateByPrimaryKeySelective(RefundOrder record);

    int updateByPrimaryKey(RefundOrder record);

    int updateStatusById(RefundOrder record);
}