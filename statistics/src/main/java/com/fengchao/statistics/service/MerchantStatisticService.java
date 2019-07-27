package com.fengchao.statistics.service;

import com.fengchao.statistics.bean.vo.MerchantCityRangeStatisticResVo;
import com.fengchao.statistics.rpc.extmodel.OrderDetailBean;

import java.util.List;
import java.util.Map;

/**
 * @Author tom
 * @Date 19-7-27 下午4:20
 */
public interface MerchantStatisticService {

    /**
     * 每天 按照商户-城市的维度，统计订单支付总额
     *
     * @param payedOrderDetailBeanList 需要统计的原始数据-已支付的订单详情
     * @param startDateTime
     * @param endDateTime
     * @throws Exception
     */
    void statisticDailyOrderAmoutByCity(List<OrderDetailBean> payedOrderDetailBeanList,
                                        String startDateTime,
                                        String endDateTime)  throws Exception;


    /**
     * 根据时间范围获取每日统计的数据
     *
     * @param startDate
     * @param endDate
     * @param merchantId
     * @return
     * @throws Exception
     */
    Map<String, List<MerchantCityRangeStatisticResVo>> fetchStatisticDailyResult(String startDate,
                                                                                 String endDate,
                                                                                 Integer merchantId) throws Exception;
}
