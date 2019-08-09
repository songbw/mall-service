package com.fengchao.equity.controller;

import com.fengchao.equity.bean.*;
import com.fengchao.equity.model.CouponX;
import com.fengchao.equity.service.CouponService;
import com.fengchao.equity.service.CouponUseInfoService;
import com.fengchao.equity.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/coupon", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
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
        CouponX coupon = couponService.consumeCoupon(bean);
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

    @PostMapping("occupy")//占用优惠券
    public OperaResult occupyCoupon(@RequestBody CouponUseInfoBean bean, OperaResult result){
        int num = useInfoService.occupyCoupon(bean);
        if(num == 2){
            result.setCode(700001);
            result.setMsg("优惠券不在有效期，不能使用");
        }else {
            result.getData().put("result",num);
        }
        return result;
    }

    @PostMapping("release")//释放优惠券
    public OperaResult releaseCoupon(@RequestBody CouponUseInfoBean bean, OperaResult result){
        int num = useInfoService.releaseCoupon(bean);
        result.getData().put("result",num);
        return result;
    }

    @PostMapping("verify")//释放优惠券
    public OperaResult verifyCoupon(@RequestBody CouponUseInfoBean bean, OperaResult result){
        int num = useInfoService.verifyCoupon(bean);
        result.getData().put("result",num);
        return result;
    }
    /**
     * 根据id集合查询
     *
     * @param idList
     * @param result
     * @return
     */
    @GetMapping("findByIdList")
    public OperaResult findByIdList(@RequestParam("idList") List<Integer> idList, OperaResult result){
        log.info("根据id集合获取coupon列表 入参:{}", JSONUtil.toJsonString(idList));

        try {
            List<CouponBean> couponBeanList = couponService.queryCouponBeanListIdList(idList);

            result.getData().put("result", couponBeanList);
        } catch (Exception e) {
            log.info("根据id集合获取coupon列表 异常:{}", e.getMessage(), e);

            result.setCode(500);
            result.setMsg("根据id集合获取coupon列表 异常");
        }

        log.info("根据id集合获取coupon列表 返回:{}", JSONUtil.toJsonString(result));

        return result;
    }
}
