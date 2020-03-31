package com.fengchao.order.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.FreightsServiceClient;
import com.fengchao.order.rpc.extmodel.ShipTemplateBean;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author tom
 * @Date 19-7-19 下午6:28
 */
@Service
@Slf4j
public class FreightsRpcService {

    private FreightsServiceClient freightsServiceClient;

    @Autowired
    public FreightsRpcService(FreightsServiceClient freightsServiceClient) {
        this.freightsServiceClient = freightsServiceClient;
    }

    /**
     * 根据mpu集合查询产品信息
     *
     * @param merchantIdList
     * @return
     */
    public List<ShipTemplateBean> queryMerchantExceptionFee(List<Integer> merchantIdList) throws Exception {
        // 返回值
        List<ShipTemplateBean> shipTemplateBeanList = new ArrayList<>();

        log.info("查询商户运费模版 调用freight rpc服务 入参:{}", JSONUtil.toJsonString(merchantIdList));

        if (CollectionUtils.isNotEmpty(merchantIdList)) {
            OperaResponse operaResponse =
                    freightsServiceClient.queryMerchantExceptionFee(StringUtils.join(merchantIdList, ","));

            log.info("查询商户运费模版 调用freight rpc服务 原始返回:{}", JSONUtil.toJsonString(operaResponse));

            if (operaResponse.getCode() == 200) {
                log.info("查询商户运费模版 ====={}", operaResponse.getData());
                log.info("查询商户运费模版 ======={}", JSONUtil.toJsonString(operaResponse.getData()));

                Map<String, Object> data = (Map) operaResponse.getData();
                Object object = data.get("result");

                if (object == null) {
                    return shipTemplateBeanList;
                }

                String jsonString = JSON.toJSONString(object);
                log.info("查询商户运费模版 转字符串======={}", jsonString);
                shipTemplateBeanList = JSONObject.parseArray(jsonString, ShipTemplateBean.class);
            } else {
                log.warn("查询商户运费模版 调用freight rpc服务 错误");
                throw new Exception("查询商户运费模版错误");
            }
        }

        log.debug("FreightsRpcService#findProductListByMpuIdList 调用product rpc服务 返回:{}",
                JSONUtil.toJsonString(shipTemplateBeanList));

        return shipTemplateBeanList;
    }
}
