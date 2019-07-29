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

    /**
     * 根据id集合查询promotionType 列表
     *
     * @param idList
     * @return
     */
    public List<PromotionType> selectPromotionTypesByIdList(List<Long> idList) {
        PromotionTypeExample promotionTypeExample = new PromotionTypeExample();

        PromotionTypeExample.Criteria criteria = promotionTypeExample.createCriteria();
        criteria.andIdIn(idList);

        List<PromotionType> promotionTypeList = promotionTypeMapper.selectByExample(promotionTypeExample);

        return promotionTypeList;
    }

    /**
     * 查询所有的PromotionType
     *
     * @return
     */
    public List<PromotionType> selectAllPromotionTypeList() {
        PromotionTypeExample example = new PromotionTypeExample();
        example.createCriteria().andIstatusEqualTo((short) 1);

        List<PromotionType> promotionTypeList = promotionTypeMapper.selectByExample(example);

        return promotionTypeList;
    }

}
