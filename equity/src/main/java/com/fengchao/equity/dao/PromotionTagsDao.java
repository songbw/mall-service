package com.fengchao.equity.dao;

import com.fengchao.equity.mapper.PromotionTagsMapper;
import com.fengchao.equity.model.PromotionTags;
import com.fengchao.equity.model.PromotionTagsExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromotionTagsDao {

    @Autowired
    private PromotionTagsMapper mapper;

    public int createPromotionTags(PromotionTags bean) {
        return mapper.insertSelective(bean);
    }

    public int deletePromotionTags(Integer id) {
        PromotionTags promotionTags = new PromotionTags();
        promotionTags.setId(id);

        PromotionTagsExample example = new PromotionTagsExample();
        PromotionTagsExample.Criteria criteria = example.createCriteria();

        criteria.andIdEqualTo(id);
        criteria.andIsStatusEqualTo(2);
        return mapper.updateByExampleSelective(promotionTags, example);
    }

    public int updatePromotionTags(PromotionTags bean) {
        return mapper.updateByPrimaryKeySelective(bean);
    }

    public PageInfo<PromotionTags> findPromotionTags(Integer pageNo, Integer pageSize, String appId) {
        PromotionTagsExample example = new PromotionTagsExample();
        PromotionTagsExample.Criteria criteria = example.createCriteria();
        criteria.andIsStatusEqualTo(1);
        criteria.andAppIdEqualTo(appId);

        PageHelper.startPage(pageNo, pageSize);
        List<PromotionTags> promotionTags = mapper.selectByExample(example);
        return new PageInfo<>(promotionTags);
    }
}
