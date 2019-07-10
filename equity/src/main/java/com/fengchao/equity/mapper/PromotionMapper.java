package com.fengchao.equity.mapper;

import com.fengchao.equity.bean.PromotionInfoBean;
import com.fengchao.equity.model.Promotion;

import java.util.HashMap;
import java.util.List;

public interface PromotionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Promotion record);

    int insertSelective(Promotion record);

    Promotion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Promotion record);

    int updateByPrimaryKey(Promotion record);

    int selectCount(HashMap map);

    List<Promotion> selectLimit(HashMap map);

    List<Promotion>  selectAll();

    List<PromotionInfoBean> selectPromotionInfoByMpu(String skuId);

    int promotionEnd(int promotionId);

    int promotionEffective(int promotionId);
}