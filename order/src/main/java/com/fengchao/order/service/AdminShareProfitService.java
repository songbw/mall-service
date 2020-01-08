package com.fengchao.order.service;

import com.fengchao.order.bean.vo.ExportShareProfitVo;

import java.util.Date;

/**
 * 分润相关服务接口
 *
 */
public interface AdminShareProfitService {

    /**
     *
     * @param startTime
     * @param endTime
     * @param appId
     * @return
     */
    ExportShareProfitVo shareProfit(Date startTime, Date endTime, String appId);
}
