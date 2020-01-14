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
     * 2.获取退款的用户单，并计算该用户但的退款方式信息
     * 3.合并前两步的用户单
     * 4.分摊各个用户单中子订单(mpu)的每种支付方式的金额
     * 5.以结算方式维度，聚合用户单中子订单
     * 6.在第5步的基础上，在以支付方式聚合出最终结果
     *
     *
     * @param startTime
     * @param endTime
     * @param appId
     * @return
     */
    List<ExportShareProfitVo> exportShareProfit(Date startTime, Date endTime, String appId);
}
