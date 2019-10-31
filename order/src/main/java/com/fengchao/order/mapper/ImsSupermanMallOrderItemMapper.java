package com.fengchao.order.mapper;

import com.fengchao.order.model.ImsSupermanMallOrderItem;
import com.fengchao.order.model.ImsSupermanMallOrderItemExample;
import com.fengchao.order.model.ImsSupermanMallOrderItemWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ImsSupermanMallOrderItemMapper {
    long countByExample(ImsSupermanMallOrderItemExample example);

    int deleteByExample(ImsSupermanMallOrderItemExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ImsSupermanMallOrderItemWithBLOBs record);

    int insertSelective(ImsSupermanMallOrderItemWithBLOBs record);

    List<ImsSupermanMallOrderItemWithBLOBs> selectByExampleWithBLOBs(ImsSupermanMallOrderItemExample example);

    List<ImsSupermanMallOrderItem> selectByExample(ImsSupermanMallOrderItemExample example);

    ImsSupermanMallOrderItemWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ImsSupermanMallOrderItemWithBLOBs record, @Param("example") ImsSupermanMallOrderItemExample example);

    int updateByExampleWithBLOBs(@Param("record") ImsSupermanMallOrderItemWithBLOBs record, @Param("example") ImsSupermanMallOrderItemExample example);

    int updateByExample(@Param("record") ImsSupermanMallOrderItem record, @Param("example") ImsSupermanMallOrderItemExample example);

    int updateByPrimaryKeySelective(ImsSupermanMallOrderItemWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ImsSupermanMallOrderItemWithBLOBs record);

    int updateByPrimaryKey(ImsSupermanMallOrderItem record);
}