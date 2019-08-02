package com.fengchao.statistics.dao;

import com.fengchao.statistics.constants.StatisticPeriodTypeEnum;
import com.fengchao.statistics.mapper.MCityOrderamountMapper;
import com.fengchao.statistics.model.MCityOrderamount;
import com.fengchao.statistics.model.MCityOrderamountExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-26 下午6:41
 */
@Component
public class MCityOrderAmountDao {

    private MCityOrderamountMapper mCityOrderamountMapper;

    @Autowired
    public MCityOrderAmountDao(MCityOrderamountMapper mCityOrderamountMapper) {
        this.mCityOrderamountMapper = mCityOrderamountMapper;
    }

    /**
     * 新增
     *
     * @param mCityOrderamount
     * @return
     */
    public int insertCategoryOverview(MCityOrderamount mCityOrderamount) {
        int id = mCityOrderamountMapper.insertSelective(mCityOrderamount);
        return id;
    }

    /**
     * 根据统计周期类型 和 统计开始/结束时间 删除 统计数据
     *
     * @param period
     * @param statisticStartDate
     * @param statisticEndDate
     * @return
     */
    public int deleteCategoryOverviewByPeriodTypeAndStatisticDate(Short period,
                                                                  Date statisticStartDate, Date statisticEndDate) {
        MCityOrderamountExample mCityOrderamountExample = new MCityOrderamountExample();

        MCityOrderamountExample.Criteria criteria = mCityOrderamountExample.createCriteria();
        criteria.andPeriodTypeEqualTo(period);
        criteria.andStatisticStartTimeEqualTo(statisticStartDate);
        criteria.andStatisticEndTimeEqualTo(statisticEndDate);

        int count = mCityOrderamountMapper.deleteByExample(mCityOrderamountExample);

        return count;
    }

    /**
     * 根据时间范围获取daily型的统计数据
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<MCityOrderamount> selectDailyCategoryOverviewsByDateRange(Date startDate, Date endDate) {
        MCityOrderamountExample mCityOrderamountExample = new MCityOrderamountExample();

        MCityOrderamountExample.Criteria criteria = mCityOrderamountExample.createCriteria();
        criteria.andPeriodTypeEqualTo(StatisticPeriodTypeEnum.DAY.getValue().shortValue());
        criteria.andStatisticStartTimeBetween(startDate, endDate);

        List<MCityOrderamount> mCityOrderamountList =
                mCityOrderamountMapper.selectByExample(mCityOrderamountExample);

        return mCityOrderamountList;
    }
}