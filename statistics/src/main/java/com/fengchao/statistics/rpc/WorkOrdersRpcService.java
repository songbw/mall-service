package com.fengchao.statistics.rpc;

import com.alibaba.fastjson.JSON;
import com.fengchao.statistics.bean.OperaResponse;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.OrderServiceClient;
import com.fengchao.statistics.feign.WorkOrdersServiceClient;
import com.fengchao.statistics.rpc.extmodel.DayStatisticsBean;
import com.fengchao.statistics.rpc.extmodel.OrderDetailBean;
import com.fengchao.statistics.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author tom
 * @Date 19-7-25 下午2:29
 */
@Service
@Slf4j
public class WorkOrdersRpcService {

    private WorkOrdersServiceClient workOrdersServiceClient;

    @Autowired
    public WorkOrdersRpcService(WorkOrdersServiceClient workOrdersServiceClient) {
        this.workOrdersServiceClient = workOrdersServiceClient;
    }

    /**
     * 查询退货人数
     *
     * @return
     */
    public int queryRefundOrdersCount() {
        // 返回值
        int refundOrdersCount = 0;

        // 执行rpc调用
        log.info("查询退货人数 调用workorders rpc服务 入参:无");
        OperaResponse operaResponse = workOrdersServiceClient.refundOrdersCount();
        log.info("查询退货人数 调用workorders rpc服务 返回:{}", JSONUtil.toJsonString(operaResponse));

        // 处理返回
        if (operaResponse.getCode() == 200) {
            Map _resultMap = (Map) operaResponse.getData();
            refundOrdersCount = (int) _resultMap.get("count");
        } else {
            log.warn("查询已支付的子订单 调用order rpc服务 错误!");
        }

        log.info("OrdersRpcService#statisticOrdersAmountByCategory 调用equity rpc服务 返回:{}",
                JSONUtil.toJsonString(refundOrdersCount));

        return refundOrdersCount;
    }


}