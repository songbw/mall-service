package com.fengchao.order.mapper;

import com.fengchao.order.model.Receiver;

import java.util.HashMap;
import java.util.List;

public interface ReceiverMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Receiver record);

    int insertSelective(Receiver record);

    Receiver selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Receiver record);

    int updateByPrimaryKey(Receiver record);

    int selectLimitCount(HashMap map) ;

    List<Receiver> selectLimit(HashMap map) ;

    int updateStatusById(Receiver record) ;

    int updateStatusByOpenId(Receiver record) ;

    List<Receiver> selectByOpenId(String openId);
}