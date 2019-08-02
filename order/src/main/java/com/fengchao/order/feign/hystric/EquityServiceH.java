package com.fengchao.order.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.order.bean.CouponUseInfoBean;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.feign.EquityService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EquityServiceH implements EquityService {
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
        return HystrixDefaultFallback.defaultFallback();
    }

    @Override
    public OperaResult release(CouponUseInfoBean bean) {
        return HystrixDefaultFallback.defaultFallback();
    }

    @Override
    public OperaResult findPromotionListByIdList(List<Integer> idList) {
        return HystrixDefaultFallback.defaultFallback();
    }

    @Override
    public OperaResult findCouponUseInfoListByIdList(List<Integer> idList) {
        return HystrixDefaultFallback.defaultFallback();
    }

}
