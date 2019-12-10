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

    public PageInfo<PromotionType> selectPromotionType(int page, int size, String appId){

        PromotionTypeExample example = new PromotionTypeExample();
        PromotionTypeExample.Criteria criteria = example.createCriteria();
        criteria.andIstatusEqualTo((short)1);
        criteria.andAppIdEqualTo(appId);

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

    public Long createPromotionType(PromotionType type){
         promotionTypeMapper.insertSelective(type);
         return type.getId();
    }

    public Long updatePromotionType(PromotionType type){
         promotionTypeMapper.updateByPrimaryKeySelective(type);
         return type.getId();
    }

    public int removePromotionType(Long promotionTypeId){
        PromotionType type = new PromotionType();
        type.setId(promotionTypeId);
        type.setIstatus((short)2);
        return promotionTypeMapper.updateByPrimaryKeySelective(type);
    }
}
