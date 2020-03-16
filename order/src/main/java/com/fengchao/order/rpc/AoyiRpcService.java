package com.fengchao.order.rpc;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.AoyiClientService;
import com.fengchao.order.rpc.extmodel.weipinhui.AoyiConfirmOrderRequest;
import com.fengchao.order.rpc.extmodel.weipinhui.AoyiLogisticsResDto;
import com.fengchao.order.rpc.extmodel.weipinhui.AoyiQueryInventoryResDto;
import com.fengchao.order.rpc.extmodel.weipinhui.AoyiRenderOrderRequest;
import com.fengchao.order.utils.AlarmUtil;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Slf4j
public class AoyiRpcService {

    private AoyiClientService aoyiClientService;

    @Autowired
    public AoyiRpcService(AoyiClientService aoyiClientService) {
        this.aoyiClientService = aoyiClientService;
    }

    /**
     * 唯品会查询库存
     *
     * @param itemId
     * @param skuId
     * @param num
     * @param divisionCode
     * @return
     */
    public OperaResponse<AoyiQueryInventoryResDto> weipinhuiQueryItemInventory(String itemId, String skuId,
                                                                    Integer num, String divisionCode) {
        // 返回值
        OperaResponse<AoyiQueryInventoryResDto> operaResponse = null;

        try {
            log.info("唯品会查询库存 调用aoyiClient rpc服务 入参 itemid:{}, skuId:{}, num:{}, divisionCode:{}",
                    itemId, skuId, num, divisionCode);

            operaResponse = aoyiClientService.queryItemInventory(itemId, skuId, num, divisionCode);
            log.info("唯品会查询库存 调用aoyiClient rpc服务 返回:{}", JSONUtil.toJsonString(operaResponse));
        } catch (Exception e) {
            log.error("唯品会查询库存 异常:{}", e.getMessage(), e);

            operaResponse.setCode(500);
            operaResponse.setMessage(e.getMessage());
        }

        log.info("唯品会查询库存 AoyiRpcService#queryItemInventory 返回:{}", JSONUtil.toJsonString(operaResponse));

        return operaResponse;
    }

    /**
     * 唯品会预下单
     *
     * @param aoyiRenderOrderRequest
     * @return
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


    /**
     * 唯品会查询物流信息
     *
     * @param subOrderNo
     * @return
     */
    public AoyiLogisticsResDto weipinhuiQueryOrderLogistics(String subOrderNo) {
        // 返回值
        AoyiLogisticsResDto aoyiLogisticsResDto = null;

        try {
            log.info("唯品会查询物流信息 调用aoyiClient rpc服务 入参:{}", subOrderNo);

            OperaResponse<AoyiLogisticsResDto> operaResponse =
                    aoyiClientService.weipinhuiQueryOrderLogistics(subOrderNo);

            log.info("唯品会查询物流信息 调用aoyiClient rpc服务 返回:{}", JSONUtil.toJsonString(operaResponse));

            if (operaResponse.getCode() == 200) {
                aoyiLogisticsResDto = operaResponse.getData();
            } else {
                log.warn("唯品会查询物流信息 失败");
                throw new Exception("唯品会查询物流信息 失败");
            }
        } catch (Exception e) {
            log.error("唯品会查询物流信息 异常:{}", e.getMessage(), e);

            AlarmUtil.alarmAsync("唯品会查询物流信息失败", "subOrderNo:" + subOrderNo + ";" + e.getMessage());
            aoyiLogisticsResDto = null;
        }

        log.info("唯品会查询物流信息 AoyiRpcService#weipinhuiQueryOrderLogistics 返回:{}",
                JSONUtil.toJsonString(aoyiLogisticsResDto));

        return aoyiLogisticsResDto;
    }

}
