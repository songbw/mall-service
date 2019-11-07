package com.fengchao.order.rpc;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.vo.BillExportReqVo;
import com.fengchao.order.feign.WorkOrderServiceClient;
import com.fengchao.order.feign.WsPayServiceClient;
import com.fengchao.order.rpc.extmodel.OrderPayMethodInfoBean;
import com.fengchao.order.rpc.extmodel.PayInfoBean;
import com.fengchao.order.rpc.extmodel.WorkOrder;
import com.fengchao.order.utils.DateUtil;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    /**
     * 获取支付交易流水单
     *
     * @return
     */
    public List<OrderPayMethodInfoBean> queryPayCandRList(BillExportReqVo billExportReqVo) {
        // 返回值
        List<OrderPayMethodInfoBean> payInfos = new ArrayList<>();

        OperaResponse<PayInfoBean> response = wsPayServiceClient.queryConsumeRefundUsing(billExportReqVo);
        log.info("获取已退款的子订单信息集合 调用workorder rpc服务 返回:{}", JSONUtil.toJsonString(response));

        // 处理返回
        if (response.getCode() == 200) {
            PayInfoBean data = response.getData();
            data.getList().forEach(OrderPayMethodInfoBean ->{
                Date orderDateEnd = null;
                String tradeDate = "";
                try {
                    if(OrderPayMethodInfoBean.getTradeDate() != null && !OrderPayMethodInfoBean.getTradeDate().equals("")){
                        orderDateEnd = new SimpleDateFormat("yyyyMMddHHmmss").parse(OrderPayMethodInfoBean.getTradeDate() );
                        tradeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderDateEnd);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                OrderPayMethodInfoBean.setTradeDate(tradeDate);
                payInfos.add(OrderPayMethodInfoBean);
            });
            if(data.getTotalPage() > 1){
                for(int i = 2; i <= data.getTotalPage(); i++){
                    billExportReqVo.setPageNum(i);
                    response = wsPayServiceClient.queryConsumeRefundUsing(billExportReqVo);
                    response.getData().getList().forEach(OrderPayMethodInfoBean ->{
                        Date orderDateEnd = null;
                        String tradeDate = "";
                        try {
                            if(OrderPayMethodInfoBean.getTradeDate() != null && !OrderPayMethodInfoBean.getTradeDate().equals("")){
                                orderDateEnd = new SimpleDateFormat("yyyyMMddHHmmss").parse(OrderPayMethodInfoBean.getTradeDate() );
                                tradeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderDateEnd);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        OrderPayMethodInfoBean.setTradeDate(tradeDate);
                        payInfos.add(OrderPayMethodInfoBean);
                    });
                }
            }
        } else {
            log.warn("获取已退款的子订单信息集合 调用workorder rpc服务 错误!");
        }

        log.info("WorkOrderRpcService#queryRefundedOrderDetailList 调用workorder rpc服务 返回:{}",
                JSONUtil.toJsonString(payInfos));

        return payInfos;
    }

    public Map<String,List<OrderPayMethodInfoBean>> queryBatchRefundMethod(List<String> refundNoList) {
        log.info("根据paymentNo集合批量获取订单支付方式的信息 调用wspay rpc服务 入参:{}", JSONUtil.toJsonString(refundNoList));

        // 返回值
        Map<String, List<OrderPayMethodInfoBean>> orderPayMethodInfoMap = new HashMap<>();

        if (CollectionUtils.isEmpty(refundNoList)) {
            log.warn("根据paymentNo集合批量获取订单支付方式的信息 调用wspay rpc服务 入参为空");
            return Collections.emptyMap();
        }

        // 执行rpc调用
        OperaResponse<Map<String, List<OrderPayMethodInfoBean>>> operaResponse = wsPayServiceClient.queryBatchRefundMethod(refundNoList);

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
