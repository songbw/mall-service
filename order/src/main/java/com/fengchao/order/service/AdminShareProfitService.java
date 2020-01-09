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
     * 合并出账和入账的用户单
     * 主要合并sku的数量 和 出账入账的金额
     *
     * @param startTime
     * @param endTime
     * @param appId
     * @return
     */
    List<UserOrderBo> mergeIncomeAndRefundUserOrder(Date startTime, Date endTime, String appId);
}
