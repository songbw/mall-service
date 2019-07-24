package com.fengchao.order.feign;

import com.fengchao.order.bean.CouponUseInfoBean;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.feign.hystric.EquityServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "equity", url = "${rpc.feign.client.equit.url:}", fallback = EquityServiceH.class)
public interface EquityService {

    @RequestMapping(value = "/coupon/consume", method = RequestMethod.POST)
    OperaResult consume(@RequestBody CouponUseInfoBean bean);

    /**
     * 根据id集合获取Promotion列表
     *
     * @param idList
     * @return
     */
    @RequestMapping(value = "/promotion/findByIdList", method = RequestMethod.GET)
    OperaResult findPromotionListByIdList(@RequestParam("idList") List<Integer> idList);

    /**
     * 根据id集合获取Coupon列表
     *
     * @param idList
     * @return
     */
    @RequestMapping(value = "/couponUseInfo/findByIdList", method = RequestMethod.GET)
    OperaResult findCouponUseInfoListByIdList(@RequestParam("idList") List<Integer> idList);
}
