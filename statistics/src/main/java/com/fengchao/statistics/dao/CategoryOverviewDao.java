package com.fengchao.statistics.dao;

import com.fengchao.statistics.constants.StatisticPeriodTypeEnum;
import com.fengchao.statistics.mapper.CategoryOverviewMapper;
import com.fengchao.statistics.model.CategoryOverview;
import com.fengchao.statistics.model.CategoryOverviewExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-25 下午5:17
 */
@Component
public class CategoryOverviewDao {

    private CategoryOverviewMapper categoryOverviewMapper;

    @Autowired
    public CategoryOverviewDao(CategoryOverviewMapper categoryOverviewMapper) {
        this.categoryOverviewMapper = categoryOverviewMapper;
    }

    /**
     * 新增
     *
     * @param categoryOverview
     * @return
     */
    public int insertCategoryOverview(CategoryOverview categoryOverview) {
        int id = categoryOverviewMapper.insertSelective(categoryOverview);
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
        CategoryOverviewExample categoryOverviewExample = new CategoryOverviewExample();

        CategoryOverviewExample.Criteria criteria = categoryOverviewExample.createCriteria();
        criteria.andPeriodTypeEqualTo(period);
        criteria.andStatisticsDateEqualTo(statisticDate);

        int count = categoryOverviewMapper.deleteByExample(categoryOverviewExample);

        return count;
    }

    /**
     * 根据时间范围获取daily型的统计数据
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<CategoryOverview> selectDailyCategoryOverviewsByDateRange(Date startDate, Date endDate) {
        CategoryOverviewExample categoryOverviewExample = new CategoryOverviewExample();

        CategoryOverviewExample.Criteria criteria = categoryOverviewExample.createCriteria();
        criteria.andPeriodTypeEqualTo(StatisticPeriodTypeEnum.DAY.getValue().shortValue());
        criteria.andStatisticStartTimeBetween(startDate, endDate);

        List<CategoryOverview> categoryOverviewList =
                categoryOverviewMapper.selectByExample(categoryOverviewExample);

        return categoryOverviewList;
    }
}
