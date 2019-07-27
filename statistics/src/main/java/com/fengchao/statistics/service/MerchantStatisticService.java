package com.fengchao.statistics.service;

import com.fengchao.statistics.rpc.extmodel.OrderDetailBean;

import java.util.List;

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


}
