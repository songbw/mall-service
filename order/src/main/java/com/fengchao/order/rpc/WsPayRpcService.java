package com.fengchao.order.rpc;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.WorkOrderServiceClient;
import com.fengchao.order.feign.WsPayServiceClient;
import com.fengchao.order.rpc.extmodel.OrderPayMethodInfoBean;
import com.fengchao.order.utils.DateUtil;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author tom
 * @Date 19-7-27 上午10:34
 */
@Service
@Slf4j
public class WsPayRpcService {

    private WsPayServiceClient wsPayServiceClient;

    @Autowired
    public WsPayRpcService(WsPayServiceClient wsPayServiceClient) {
        this.wsPayServiceClient = wsPayServiceClient;
    }

    /**
     * 根据paymentNo集合批量获取订单支付方式的信息
     *
     * @return
     */
    public Map<String, List<OrderPayMethodInfoBean>> queryBatchPayMethod(List<String> paymentNoList) {
        log.info("根据paymentNo集合批量获取订单支付方式的信息 调用wspay rpc服务 入参:{}", JSONUtil.toJsonString(paymentNoList));

        // 返回值
        Map<String, List<OrderPayMethodInfoBean>> orderPayMethodInfoMap = new HashMap<>();

        if (CollectionUtils.isEmpty(paymentNoList)) {
            log.warn("根据paymentNo集合批量获取订单支付方式的信息 调用wspay rpc服务 入参为空");
            return Collections.emptyMap();
        }

        // 执行rpc调用
        OperaResponse<Map<String, List<OrderPayMethodInfoBean>>> operaResponse = wsPayServiceClient.queryBatchPayMethod(paymentNoList);

        log.info("根据paymentNo集合批量获取订单支付方式的信息 调用wspay rpc服务 返回:{}", JSONUtil.toJsonString(operaResponse));

        // 处理返回
        if (operaResponse.getCode() == 200) {
            orderPayMethodInfoMap = operaResponse.getData();
        } else {
            log.warn("根据paymentNo集合批量获取订单支付方式的信息 调用wspay rpc服务 错误!");
        }

        log.info("WsPayRpcService#queryBatchPayMethod 调用wspay rpc服务 返回:{}", JSONUtil.toJsonString(orderPayMethodInfoMap));

        return orderPayMethodInfoMap;
    }
}
