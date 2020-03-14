package com.fengchao.order.rpc;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.VendorsServiceClient;
import com.fengchao.order.feign.WorkOrderServiceClient;
import com.fengchao.order.rpc.extmodel.SysCompanyX;
import com.fengchao.order.rpc.extmodel.WorkOrder;
import com.fengchao.order.utils.DateUtil;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.dnd.DropTargetEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-27 上午10:34
 */
@Service
@Slf4j
public class WorkOrderRpcService {

    private WorkOrderServiceClient workOrderServiceClient;

    @Autowired
    public WorkOrderRpcService(WorkOrderServiceClient workOrderServiceClient) {
        this.workOrderServiceClient = workOrderServiceClient;
    }

    /**
     * 获取已退款的子订单id集合
     *
     * @return
     */
    public List<String> queryRefundedOrderDetailIdList(Integer merchantId, Date startTime, Date endTime) {
        // 返回值
        List<String> orderDetailNoList = new ArrayList<>();

        // 执行rpc调用
        log.info("获取已退款的子订单id集合 调用workorder rpc服务 入参 merchantId:{}, startTime:{}, endTime:{}",
                merchantId, startTime, endTime);

        String startTimeStr = DateUtil.dateTimeFormat(startTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        String endTimeStr = DateUtil.dateTimeFormat(endTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        OperaResponse<List<String>> operaResponse = workOrderServiceClient.queryRefundedOrderDetailIdList(
                merchantId == null ? null : merchantId.longValue(), startTimeStr, endTimeStr);
        log.info("获取已退款的子订单id集合 调用workorder rpc服务 返回:{}", JSONUtil.toJsonString(operaResponse));

        // 处理返回
        if (operaResponse.getCode() == 200) {
            orderDetailNoList = operaResponse.getData();
        } else {
            log.warn("获取已退款的子订单id集合 调用workorder rpc服务 错误!");
        }

        log.info("WorkOrderRpcService#queryRefundedOrderDetailIdList 调用workorder rpc服务 返回:{}",
                JSONUtil.toJsonString(orderDetailNoList));

        return orderDetailNoList;
    }

    /**
     * 获取已退款的子订单信息集合
     *
     * @return
     */
    public List<WorkOrder> queryRefundedOrderDetailList(Integer merchantId, String appId, Date startTime, Date endTime) {
        // 返回值
        List<WorkOrder> workOrderList = new ArrayList<>();

        // 执行rpc调用
        log.info("获取已退款的子订单信息集合 调用workorder rpc服务 入参 merchantId:{}, startTime:{}, endTime:{}",
                merchantId, startTime, endTime);

        String startTimeStr = DateUtil.dateTimeFormat(startTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        String endTimeStr = DateUtil.dateTimeFormat(endTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

        OperaResponse<List<WorkOrder>> operaResponse = workOrderServiceClient.queryRefundedOrderDetailList(
                merchantId == null ? null : merchantId.longValue(), appId, startTimeStr, endTimeStr);
        log.info("获取已退款的子订单信息集合 调用workorder rpc服务 返回:{}", JSONUtil.toJsonString(operaResponse));

        // 处理返回
        if (operaResponse.getCode() == 200) {
            workOrderList = operaResponse.getData();
        } else {
            log.warn("获取已退款的子订单信息集合 调用workorder rpc服务 错误!");
        }

        log.info("WorkOrderRpcService#queryRefundedOrderDetailList 调用workorder rpc服务 返回:{}",
                JSONUtil.toJsonString(workOrderList));

        return workOrderList;
    }
}
