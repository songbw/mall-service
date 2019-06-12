package com.fengchao.order.mapper;

import com.fengchao.order.model.InvoiceInfo;

import java.util.HashMap;
import java.util.List;

public interface InvoiceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InvoiceInfo record);

    int insertSelective(InvoiceInfo record);

    InvoiceInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InvoiceInfo record);

    int updateByPrimaryKey(InvoiceInfo record);

    int selectLimitCount(HashMap map) ;

    List<InvoiceInfo> selectLimit(HashMap map) ;
}