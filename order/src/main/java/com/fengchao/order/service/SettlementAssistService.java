package com.fengchao.order.service;

import com.fengchao.order.bean.bo.UserOrderBo;

import java.util.Date;
import java.util.List;

/**
 * 结算辅助的服务接口
 * 利用该接口可以获取结算相关的辅助信息
 */
public interface SettlementAssistService {

    /**
     * 根据时间范围 查询进账的 用户单
     *
     * @param startTime
     * @param endTime
     * @param appId
     * @return
     */
    List<UserOrderBo> queryIncomeUserOrderBoList(Date startTime, Date endTime, String appId);



    /**
     * 根据时间范围 查询出账的 用户单
     *
     * @param startTime
     * @param endTime
     * @param appId
     * @return
     */
    List<UserOrderBo> queryRefundUserOrderBoList(Date startTime, Date endTime, String appId);
}
