package com.fengchao.equity.controller;

import com.fengchao.equity.bean.CouponBean;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping(value = "/coupon", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class UserCouponInfoController {



    @GetMapping("findByIdList")
    public OperaResult findByIdList(@RequestParam("idList") List<Integer> idList, OperaResult result){
//        log.info("根据id集合获取coupon列表 入参:{}", JSONUtil.toJsonString(idList));
//
//        try {
//            List<CouponBean> couponBeanList = couponService.queryCouponBeanListIdList(idList);
//
//            result.getData().put("result", couponBeanList);
//        } catch (Exception e) {
//            log.info("根据id集合获取coupon列表 异常:{}", e.getMessage(), e);
//
//            result.setCode(500);
//            result.setMsg("根据id集合获取coupon列表 异常");
//        }
//
//        log.info("根据id集合获取coupon列表 返回:{}", JSONUtil.toJsonString(result));

        return null;
    }
}
