package com.fengchao.statistics.dao;

import com.fengchao.statistics.constants.StatisticPeriodTypeEnum;
import com.fengchao.statistics.mapper.PromotionOverviewMapper;
import com.fengchao.statistics.model.PromotionOverview;
import com.fengchao.statistics.model.PromotionOverviewExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-26 下午6:41
 */
@Component
public class PromotionOverviewDao {

    private PromotionOverviewMapper promotionOverviewMapper;

    @Autowired
    public PromotionOverviewDao(PromotionOverviewMapper promotionOverviewMapper) {
        this.promotionOverviewMapper = promotionOverviewMapper;
    }

    /**
     * 新增
     *
     * @param promotionOverview
     * @return
     */
    public int insertCategoryOverview(PromotionOverview promotionOverview) {
        int id = promotionOverviewMapper.insertSelective(promotionOverview);
        return id;
    }

    /**
     * 根据统计周期类型 和 统计任务执行日期 删除 统计数据
     *
     * @param period
     * @param statisticDate
     * @return
     */
    public int deleteCategoryOverviewByPeriodTypeAndStatisticDate(Short period, Date statisticDate) {
        PromotionOverviewExample promotionOverviewExample = new PromotionOverviewExample();

        PromotionOverviewExample.Criteria criteria = promotionOverviewExample.createCriteria();
        criteria.andPeriodTypeEqualTo(period);
        criteria.andStatisticsDateEqualTo(statisticDate);

        int count = promotionOverviewMapper.deleteByExample(promotionOverviewExample);

        return count;
    }

    /**
     * 根据时间范围获取daily型的统计数据
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<PromotionOverview> selectDailyCategoryOverviewsByDateRange(Date startDate, Date endDate) {
        PromotionOverviewExample promotionOverviewExample = new PromotionOverviewExample();

        PromotionOverviewExample.Criteria criteria = promotionOverviewExample.createCriteria();
        criteria.andPeriodTypeEqualTo(StatisticPeriodTypeEnum.DAY.getValue().shortValue());
        criteria.andStatisticStartTimeBetween(startDate, endDate);

        List<PromotionOverview> promotionOverviewList =
                promotionOverviewMapper.selectByExample(promotionOverviewExample);

        return promotionOverviewList;
    }
}
