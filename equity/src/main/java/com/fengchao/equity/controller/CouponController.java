package com.fengchao.equity.controller;

import com.fengchao.equity.bean.*;
import com.fengchao.equity.model.CouponUseInfoX;
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
            result.setMsg("优惠券不存在");
        }else if(couponUseInfoBean.getUserCouponCode().equals("1")){
            result.setCode(40011);
            result.setMsg("优惠券已抢完");
        }else if(couponUseInfoBean.getUserCouponCode().equals("2")){
            result.setCode(40012);
            result.setMsg("领取优惠券已达上限");
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
            result.setMsg("兑换优惠券已达上限");
        }else if(couponUseInfoBean.getUserCouponCode().equals("4")){
            result.setCode(40014);
            result.setMsg("优惠券已失效");
        }else if(couponUseInfoBean.getUserCouponCode().equals("5")){
            result.setCode(40015);
            result.setMsg("兑换失败");
        }else if(couponUseInfoBean.getUserCouponCode().equals("6")){
            result.setCode(40016);
            result.setMsg("优惠券已下线");
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
        CouponUseInfoX coupon = couponService.consumeCoupon(bean);
        if(coupon == null){
            result.setCode(40012);
            result.setMsg("优惠券不存在");
        }else if(coupon.getStatus() == 3){
            result.setCode(40013);
            result.setMsg("该券已核销");
        }else{
            result.getData().put("id",coupon.getCouponId());
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

    @PostMapping("verify")//验证优惠券
    public OperaResult verifyCoupon(@RequestBody CouponUseInfoBean bean, OperaResult result){
        int num = useInfoService.verifyCoupon(bean);
        if(num == 0){
            result.setCode(500);
            result.setMsg("优惠券不存在");
        }else if(num == 1){
            result.setMsg("优惠券在有效期内，可以使用");
        }else if(num == 2){
            result.setCode(500);
            result.setMsg("优惠券不在有效期，不能使用");
        }
        return result;
    }

    @GetMapping("giftCoupon")
    public OperaResult giftCoupon(String openId, String iAppId, OperaResult result){
        List<Object> beans = couponService.giftCoupon(openId, iAppId);
        if(beans.get(0).equals(2)){
            result.setCode(500);
            result.setMsg("该用户不存在");
        }else{
            result.getData().put("result", beans);
        }
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

    @GetMapping("batchCoupon")
    public OperaResult findCouponListByIdList(@RequestParam("ids") List<Integer> ids, String openId, OperaResult result){
        List<CouponBean> couponBeanList = couponService.findCouponListByIdList(ids, openId);
        result.getData().put("result", couponBeanList);
        return result;
    }

    @PostMapping("mpus")
    public OperaResult findCouponListByMpuList(@RequestBody List<AoyiProdBean> beans, OperaResult result){
        List<CouponAndPromBean> mpuList = couponService.findCouponListByMpuList(beans);
        result.getData().put("result", mpuList);
        return result;
    }
}
