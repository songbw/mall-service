package com.fengchao.statistics.dao;

import com.fengchao.statistics.mapper.MerchantOverviewMapper;
import com.fengchao.statistics.model.MerchantOverview;
import com.fengchao.statistics.model.MerchantOverviewExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author tom
 * @Date 19-7-26 下午6:41
 */
@Component
public class MerchantOverviewDao {

    private MerchantOverviewMapper merchantOverviewMapper;

    @Autowired
    public MerchantOverviewDao(MerchantOverviewMapper merchantOverviewMapper) {
        this.merchantOverviewMapper = merchantOverviewMapper;
    }

    /**
     * 新增
     *
     * @param merchantOverview
     * @return
     */
    public int insertCategoryOverview(MerchantOverview merchantOverview) {
        int id = merchantOverviewMapper.insertSelective(merchantOverview);
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
        MerchantOverviewExample merchantOverviewExample = new MerchantOverviewExample();

        MerchantOverviewExample.Criteria criteria = merchantOverviewExample.createCriteria();
        criteria.andPeriodTypeEqualTo(period);
        criteria.andStatisticsDateEqualTo(statisticDate);

        int count = merchantOverviewMapper.deleteByExample(merchantOverviewExample);

        return count;
    }
}
