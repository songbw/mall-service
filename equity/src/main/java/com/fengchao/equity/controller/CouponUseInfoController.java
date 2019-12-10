package com.fengchao.equity.controller;

import com.fengchao.equity.bean.CouponUseInfoBean;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.model.Coupon;
import com.fengchao.equity.service.CouponUseInfoService;
import com.fengchao.equity.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-24 下午2:11
 */
@RestController
@RequestMapping(value = "/couponUseInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class CouponUseInfoController {

    private CouponUseInfoService couponUseInfoService;

    @Autowired
    public CouponUseInfoController(CouponUseInfoService couponUseInfoService) {
        this.couponUseInfoService = couponUseInfoService;
    }

    @GetMapping("findByIdList")
    public OperaResult findByIdList(@RequestParam("idList") List<Integer> idList, OperaResult result) {
        log.info("通过id集合查询coupon_use_info列表 入参:{}", JSONUtil.toJsonString(idList));

        try {
            List<CouponUseInfoBean> couponUseInfoBeanList = couponUseInfoService.queryByIdList(idList);

            result.getData().put("result", couponUseInfoBeanList);
        } catch (Exception e) {
            log.info("通过id集合查询coupon_use_info列表 异常:{}", e.getMessage(), e);

            result.setCode(500);
            result.setMsg("通过id集合查询coupon_use_info列表 异常");
        }

        log.info("通过id集合查询coupon_use_info列表 返回:{}", JSONUtil.toJsonString(result));

        return result;
    }

    @GetMapping("collectGiftCoupon")
    public OperaResult getCollectGiftCoupon(@RequestParam("openId") String openId, String appId, OperaResult result) {
        List<Coupon> collectGiftCoupon = couponUseInfoService.getCollectGiftCoupon(openId, appId);
        result.getData().put("result", collectGiftCoupon);
        return result;
    }
}
