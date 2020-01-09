package com.fengchao.order.service;

import com.fengchao.order.bean.vo.ExportShareProfitVo;

import java.util.Date;
import java.util.List;

/**
 * 分润相关服务接口
 *
 */
public interface AdminShareProfitService {

    /**
     * 导出分润表
     *
     * 处理逻辑:
     * 1.获取入账的用户单，并关联该用户单的支付方式信息
     * 2.获取退款的用户单，
     *
     *
     * @param startTime
     * @param endTime
     * @param appId
     * @return
     */
    List<ExportShareProfitVo> exportShareProfit(Date startTime, Date endTime, String appId);
}
