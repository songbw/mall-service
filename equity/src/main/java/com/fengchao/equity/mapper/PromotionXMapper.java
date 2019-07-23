package com.fengchao.equity.mapper;

import com.fengchao.equity.bean.PromotionInfoBean;
import com.fengchao.equity.model.PromotionX;

import java.util.HashMap;
import java.util.List;

public interface PromotionXMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionX record);

    int insertSelective(PromotionX record);

    PromotionX selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionX record);

    int updateByPrimaryKey(PromotionX record);

    int selectCount(HashMap map);

    List<PromotionX> selectLimit(HashMap map);

    List<PromotionX>  selectAll();

    List<PromotionInfoBean> selectPromotionInfoByMpu(String skuId);

    int promotionEnd(int promotionId);

    int promotionEffective(int promotionId);

    PromotionX selectPromotionName(Integer id);
}