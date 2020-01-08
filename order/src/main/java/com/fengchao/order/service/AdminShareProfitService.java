package com.fengchao.order.service;

import com.fengchao.order.bean.bo.UserOrderBo;
import com.fengchao.order.bean.vo.ExportShareProfitVo;

import java.util.Date;
import java.util.List;

/**
 * 分润相关服务接口
 *
 */
public interface AdminShareProfitService {

    /**
     * 导出分润报表
     *
     * @param startTime
     * @param endTime
     * @param appId
     * @return
     */
    List<UserOrderBo> mergeIncomeAndRefundUserOrder(Date startTime, Date endTime, String appId);
}
