package com.fengchao.order.mapper;

import com.fengchao.order.model.ImsSupermanMallOrder;
import com.fengchao.order.model.ImsSupermanMallOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ImsSupermanMallOrderMapper {
    long countByExample(ImsSupermanMallOrderExample example);

    int deleteByExample(ImsSupermanMallOrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ImsSupermanMallOrder record);

    int insertSelective(ImsSupermanMallOrder record);

    List<ImsSupermanMallOrder> selectByExampleWithBLOBs(ImsSupermanMallOrderExample example);

    List<ImsSupermanMallOrder> selectByExample(ImsSupermanMallOrderExample example);

    ImsSupermanMallOrder selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ImsSupermanMallOrder record, @Param("example") ImsSupermanMallOrderExample example);

    int updateByExampleWithBLOBs(@Param("record") ImsSupermanMallOrder record, @Param("example") ImsSupermanMallOrderExample example);

    int updateByExample(@Param("record") ImsSupermanMallOrder record, @Param("example") ImsSupermanMallOrderExample example);

    int updateByPrimaryKeySelective(ImsSupermanMallOrder record);

    int updateByPrimaryKeyWithBLOBs(ImsSupermanMallOrder record);

    int updateByPrimaryKey(ImsSupermanMallOrder record);
}