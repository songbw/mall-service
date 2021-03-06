package com.fengchao.order.mapper;

import com.fengchao.order.model.ShoppingCart;

import java.util.HashMap;
import java.util.List;

public interface ShoppingCartMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByOpenIdAndSku(ShoppingCart record) ;

    int insert(ShoppingCart record);

    int insertSelective(ShoppingCart record);

    ShoppingCart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShoppingCart record);

    int updateByPrimaryKey(ShoppingCart record);

    int updateNumById(ShoppingCart record) ;

    int updateIsDelById(Integer id) ;

    int updateStatusById(Integer id) ;

    ShoppingCart selectByOpenIdAndSku(ShoppingCart record);

    List<ShoppingCart> selectLimit(HashMap map) ;

    int selectLimitCount(HashMap map) ;

    List<ShoppingCart> selectByOpenId(String openId) ;

    int selectNumCount(String openId) ;
}