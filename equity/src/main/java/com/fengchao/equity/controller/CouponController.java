package com.fengchao.equity.controller;

import com.fengchao.equity.bean.*;
import com.fengchao.equity.exception.EquityException;
import com.fengchao.equity.model.Coupon;
import com.fengchao.equity.service.CouponService;
import com.fengchao.equity.service.CouponUseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/coupon", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CouponController {

    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponUseInfoService useInfoService;


    @GetMapping("activeCoupon")
    public OperaResult activeCoupon(CouponUseInfoBean useInfoBean, OperaResult result){
        PageBean coupon = couponService.activeCoupon(useInfoBean);
        result.getData().put("result", coupon);
        return result;
    }

    @PostMapping("collect")
    public OperaResult collectCoupon(@RequestBody CouponUseInfoBean bean, OperaResult result){
        CouponUseInfoBean couponUseInfoBean = useInfoService.collectCoupon(bean);
        if(couponUseInfoBean.getUserCouponCode().equals("0")){
            result.setCode(40010);
            result.setMsg("优惠卷不存在");
        }else if(couponUseInfoBean.getUserCouponCode().equals("1")){
            result.setCode(40011);
            result.setMsg("优惠卷已抢完");
        }else if(couponUseInfoBean.getUserCouponCode().equals("2")){
            result.setCode(40012);
            result.setMsg("领取优惠卷已达上限");
        }else if(couponUseInfoBean.getUserCouponCode().equals("3")){
            result.setCode(40013);
            result.setMsg("领取失败");
        }else{
            result.getData().put("userCouponCode", couponUseInfoBean.getUserCouponCode());
            result.getData().put("couponCollectNum", couponUseInfoBean.getCouponCollectNum());
        }
        return result;
    }

    @GetMapping("activeCategories")
    public OperaResult activeCategories(OperaResult result){
        result.getData().put("result", couponService.activeCategories());
        return result;
    }

    @PostMapping("CouponCount")
    public OperaResult selectCouponNum(@RequestBody CouponUseInfoBean bean, OperaResult result){
        result.getData().put("couponNum", useInfoService.getCouponNum(bean));
        return result;
    }

    @PostMapping("CouponByOpenId")
    public OperaResult selectCouponByOpenId(@RequestBody CouponUseInfoBean bean, OperaResult result){
        result.getData().put("result", useInfoService.selectCouponByOpenId(bean));
        return result;
    }

    @PostMapping("CouponByEquityId")
    public OperaResult selectCouponByEquityId(@RequestBody CouponUseInfoBean bean, OperaResult result){
        result.getData().put("result", useInfoService.selectCouponByEquityId(bean));
        return result;
    }

    @GetMapping("skuById")
    public OperaResult selectSkuByCouponId(CouponUseInfoBean bean, OperaResult result){
        result.getData().put("result", couponService.selectSkuByCouponId(bean));
        return result;
    }

    @PostMapping("redemption")
    public OperaResult redemption(@RequestBody CouponUseInfoBean bean, OperaResult result){
        CouponUseInfoBean couponUseInfoBean = useInfoService.redemption(bean);
        if(couponUseInfoBean.getUserCouponCode().equals("2")){
            result.setCode(40010);
            result.setMsg("优惠券不存在");
        }else if(couponUseInfoBean.getUserCouponCode().equals("3")){
            result.setCode(40011);
            result.setMsg("领取失败");
        }else{
            result.getData().put("couponCode", couponUseInfoBean.getCouponCode());
            result.getData().put("couponCollectNum", couponUseInfoBean.getCouponCollectNum());
        }
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deleteUserCoupon(CouponUseInfoBean bean, OperaResult result){
        int pageBean = useInfoService.deleteUserCoupon(bean);
        result.getData().put("result",pageBean);
        return result;
    }

    @PostMapping("consume")
    public OperaResult consume(@RequestBody CouponUseInfoBean bean, OperaResult result){
        Coupon coupon = couponService.consumeCoupon(bean);
        if(coupon == null){
            result.setCode(40012);
            result.setMsg("销券失败");
        }else{
            result.getData().put("id",coupon.getId());
            result.getData().put("code",coupon.getCode());
        }
        return result;
    }

    @GetMapping("findById")
    public OperaResult findById(CouponUseInfoBean bean, OperaResult result){
        result.getData().put("result", useInfoService.findById(bean));
        return result;
    }

    @PostMapping("consumed")//头食对接接口
    public OperaResult consumed(@RequestBody ToushiResult bean) throws EquityException {
//        result.getData().put("result",num);
        return useInfoService.consumedToushi(bean);
    }

    @PostMapping("obtain")//头食对接接口
    public OperaResult obtainCoupon(@RequestBody ToushiResult bean) throws EquityException{
        return useInfoService.obtainCoupon(bean);
    }
}
