package com.fengchao.equity.dao;

import com.fengchao.equity.mapper.PromotionMapper;
import com.fengchao.equity.model.Promotion;
import com.fengchao.equity.model.PromotionExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-18 下午4:35
 */
@Component
public class PromotionDao {

    private PromotionMapper promotionMapper;

    @Autowired
    public PromotionDao(PromotionMapper promotionMapper) {
        this.promotionMapper = promotionMapper;
    }

    /**
     * 根据活动id集合，查询活动列表
     *
     * @param promotionIdList
     * @return
     */
    public List<Promotion> selectPromotionListByIdList(List<Integer> promotionIdList) {
        PromotionExample promotionExample = new PromotionExample();
        PromotionExample.Criteria criteria = promotionExample.createCriteria();

        criteria.andIdIn(promotionIdList);

        List<Promotion> promotionList = promotionMapper.selectByExample(promotionExample);

        return promotionList;
    }
}
