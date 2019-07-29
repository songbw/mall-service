package com.fengchao.equity.dao;

import com.fengchao.equity.mapper.PromotionTypeMapper;
import com.fengchao.equity.model.PromotionType;
import com.fengchao.equity.model.PromotionTypeExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-18 下午4:35
 */
@Component
public class PromotionTypeDao {

    private PromotionTypeMapper promotionTypeMapper;

    @Autowired
    public PromotionTypeDao(PromotionTypeMapper promotionTypeMapper) {
        this.promotionTypeMapper = promotionTypeMapper;
    }

    public PageInfo<PromotionType> selectPromotionType(int page , int size){
        PromotionTypeExample example = new PromotionTypeExample();
        example.createCriteria().andIstatusEqualTo((short)1);
        PageHelper.startPage(page, size);
        List<PromotionType> types =  promotionTypeMapper.selectByExample(example);
        return new PageInfo<>(types);
    }

}
