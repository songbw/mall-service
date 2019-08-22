package com.fengchao.sso.rpc;

import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.feign.OrderServiceClient;
import com.fengchao.sso.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-18 下午5:13
 */
@Service
@Slf4j
public class OrderRpcService {

    private OrderServiceClient orderServiceClient;

    @Autowired
    public OrderRpcService(OrderServiceClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    /**
     * 根据主订单id集合批量更新子订单状态
     *
     * @param orderIdList
     * @param status
     * @return
     */
    public int batchUpdateOrderDetailStatusByOrderIds(List<Integer> orderIdList, int status) {
        int count = 0;

        log.info("根据主订单id集合批量更新子订单状态 调用order rpc服务 入参 orderIdList:{}, status:{}",
                JSONUtil.toJsonString(orderIdList), status);

        if (CollectionUtils.isNotEmpty(orderIdList)) {
            OperaResponse operaResponse = orderServiceClient.batchUpdateOrderDetailStatusByOrderIds(orderIdList, status);
            log.info("根据主订单id集合批量更新子订单状态 调用order rpc服务 返回:{}", JSONUtil.toJsonString(operaResponse));

            // 处理返回
            if (operaResponse.getCode() == 200) {
                count = (int) operaResponse.getData();
            } else {
                log.warn("根据主订单id集合批量更新子订单状态 调用order rpc服务 错误!");
            }
        }

        log.info("OrderRpcService#batchUpdateOrderDetailStatusByOrderIds 调用order rpc服务 返回:{}", count);

        return count;
    }

}
