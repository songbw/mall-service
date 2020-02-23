package com.fengchao.order.rpc;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.AoyiClientService;
import com.fengchao.order.rpc.extmodel.Email;
import com.fengchao.order.rpc.extmodel.SMSPostBean;
import com.fengchao.order.rpc.extmodel.weipinhui.AoyiConfirmOrderRequest;
import com.fengchao.order.rpc.extmodel.weipinhui.AoyiDeliverAddress;
import com.fengchao.order.rpc.extmodel.weipinhui.AoyiItem;
import com.fengchao.order.rpc.extmodel.weipinhui.AoyiRenderOrderRequest;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AoyiRpcService {

    private AoyiClientService aoyiClientService;

    @Autowired
    public AoyiRpcService(AoyiClientService aoyiClientService) {
        this.aoyiClientService = aoyiClientService;
    }

    /**
     * 发送短信
     *
     * @param aoyiRenderOrderRequest
     * @return "SUCCESS" 成功， 其他: 失败
     * <p>
     * {
     * "orderNo": "testorderno222",
     * "amount": "30.1",
     * "freight": "1.1",
     * "items": [
     * {
     * "subOrderNo": "testsuborderno222",
     * "itemId": "30007551",
     * "skuId": "30012085",
     * "number": 1,
     * "prodPrice": "29",
     * "subAmount": "29"
     * }
     * ],
     * "deliveryAddress": {
     * "divisionCode": "8",
     * "fullName":"荆涛测试",
     * "mobile": "13488689297",
     * "addressDetail": "北京是朝阳区建外soho西区11-3006"
     * }
     * }
     */
    public OperaResponse<String> weipinhuiRend(AoyiRenderOrderRequest aoyiRenderOrderRequest) {
        // 返回值
        OperaResponse<String> operaResponse = null;

        try {
            log.info("唯品会预下单 调用aoyiClient rpc服务 入参:{}", JSONUtil.toJsonString(aoyiRenderOrderRequest));

            operaResponse = aoyiClientService.weipinhuiRenderOrder(aoyiRenderOrderRequest);
            log.info("唯品会预下单 调用aoyiClient rpc服务 返回:{}", JSONUtil.toJsonString(operaResponse));
        } catch (Exception e) {
            log.error("唯品会预下单 异常:{}", e.getMessage(), e);

            operaResponse.setCode(500);
            operaResponse.setMessage(e.getMessage());
        }

        log.info("唯品会预下单 AoyiRpcService#weipinhuiRend 返回:{}", JSONUtil.toJsonString(operaResponse));

        return operaResponse;
    }

    /**
     * 唯品会确认订单
     *
     * @param aoyiConfirmOrderRequest
     * @return
     */
    public OperaResponse<String> weipinhuiCreateOrder(AoyiConfirmOrderRequest aoyiConfirmOrderRequest) {
        // 返回值
        OperaResponse<String> operaResponse = null;

        try {
            log.info("唯品会确认订单 调用aoyiClient rpc服务 入参:{}", JSONUtil.toJsonString(aoyiConfirmOrderRequest));

            operaResponse = aoyiClientService.weipinhuiCreateOrder(aoyiConfirmOrderRequest);
            log.info("唯品会确认订单 调用aoyiClient rpc服务 返回:{}", JSONUtil.toJsonString(operaResponse));
        } catch (Exception e) {
            log.error("唯品会确认订单 异常:{}", e.getMessage(), e);

            operaResponse.setCode(500);
            operaResponse.setMessage(e.getMessage());
        }

        log.info("唯品会确认订单 AoyiRpcService#weipinhuiCreateOrder 返回:{}", JSONUtil.toJsonString(operaResponse));

        return operaResponse;
    }

}
