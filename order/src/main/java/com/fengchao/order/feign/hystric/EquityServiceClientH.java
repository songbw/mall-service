package com.fengchao.order.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.order.bean.*;
import com.fengchao.order.feign.EquityServiceClient;
import com.fengchao.order.model.AoyiProdIndex;
import com.fengchao.order.rpc.extmodel.PromotionBean;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EquityServiceClientH implements EquityServiceClient {

    @Setter
    private Throwable cause;

    @Override
    public OperaResult consume(CouponUseInfoBean bean) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("权益优惠券服务失败 ");
        result.getData().put("error", bean) ;
        return result;
    }

    @Override
    public OperaResult occupy(CouponUseInfoBean bean) {
        return HystrixDefaultFallback.defaultFallback(cause);
    }

    @Override
    public OperaResult release(CouponUseInfoBean bean) {
        return HystrixDefaultFallback.defaultFallback(cause);
    }

    @Override
    public OperaResponse<List<PromotionBean>> findPromotionListByIdList(List<Integer> idList) {
        return HystrixDefaultFallback.fallbackResponse(cause);
    }

    @Override
    public OperaResult findCouponUseInfoListByIdList(List<Integer> idList) {
        return HystrixDefaultFallback.defaultFallback(cause);
    }

    @Override
    public OperaResult findPromotionByMpuList(List<String> mpuList, String appId) {
        return HystrixDefaultFallback.defaultFallback(cause);
    }

    @Override
    public OperaResult createVirtual(VirtualTicketsBean bean) {
        return HystrixDefaultFallback.defaultFallback(cause);
    }

    @Override
    public OperaResult findCouponListByMpuList(List<AoyiProdIndex> beans, String appId) {
        return HystrixDefaultFallback.defaultFallback(cause);
    }

    @Override
    public OperaResult promotionVerify(List<PromotionVerifyBean> beans, String appId) {
        return HystrixDefaultFallback.defaultFallback(cause);
    }

}
