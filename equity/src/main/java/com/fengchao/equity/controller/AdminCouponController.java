package com.fengchao.equity.controller;

import com.alibaba.fastjson.JSON;
import com.fengchao.equity.bean.*;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.Coupon;
import com.fengchao.equity.model.CouponUseInfoX;
import com.fengchao.equity.service.CouponService;
import com.fengchao.equity.service.CouponUseInfoService;
import com.fengchao.equity.utils.JSONUtil;
import com.fengchao.equity.utils.MyFunctions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/adminCoupon", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminCouponController {

    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponUseInfoService useInfoService;


    @PostMapping("create")
    public OperaResult createCoupon(@RequestBody CouponBean bean, OperaResult result){
        logParamIn(MyFunctions.WEB_ADMIN_CREATE_COUPON,JSONUtil.toJsonString(bean));
        result.getData().put("couponId",couponService.createCoupon(bean));
        return result;
    }

    @GetMapping("find")
    public OperaResult findCoupon(QueryBean bean, OperaResult result){
        PageBean coupon = couponService.findCoupon(bean.getOffset(), bean.getLimit(), bean.getAppId());
        result.getData().put("result", coupon);
        return result;
    }

    @PostMapping("search")
    public OperaResult searchCoupon(@RequestBody CouponSearchBean bean, OperaResult result){
        PageBean pageBean = couponService.searchCoupon(bean);
        result.getData().put("result",pageBean);
        return result;
    }

    @GetMapping("findById")
    public OperaResult findByCouponId(Integer id, OperaResult result){
        result.getData().put("result",couponService.findByCouponId(id));
        return result;
    }

    @PostMapping("update")
    public OperaResult updateCoupon(@RequestBody CouponBean bean, OperaResult result){
        logParamIn(MyFunctions.WEB_ADMIN_UPDATE_COUPON,JSONUtil.toJsonString(bean));
        int pageBean = couponService.updateCoupon(bean);
        result.getData().put("result",pageBean);
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deleteCoupon(Integer id, OperaResult result){
        logParamIn(MyFunctions.WEB_ADMIN_DELETE_COUPON,(null == id?"null":id.toString()));
        int num = couponService.deleteCoupon(id);
        if(num == 0){
            result.setCode(700101);
            result.setMsg("删除失败");
        }else if(num == 1){
            result.getData().put("result", num);
        }else if(num == 2){
            result.setCode(700102);
            result.setMsg("优惠券已发布，不能删除");
        }
        return result;
    }

    @GetMapping("usageById")
    public OperaResult findCollect(CouponUseInfoBean bean, OperaResult result){
        //log.info("获取优惠券使用详情 入参: {}", JSON.toJSONString(bean));
        result.getData().put("result", useInfoService.findCollect(bean));
        return result;
    }

    @PostMapping("importCode")
    public OperaResult importCode(@RequestBody CouponUseInfoBean bean, OperaResult result){
        logParamIn(MyFunctions.WEB_ADMIN_IMPORT_CODE,JSON.toJSONString(bean));
        int num = useInfoService.importCode(bean);
        if(num == 0){
            result.setCode(10010);
            result.setMsg("批量生成失败");
        }else if(num == 1001){
            result.setCode(10011);
            result.setMsg("优惠卷不存在");
        }else if(num == 1002){
            result.setCode(10012);
            result.setMsg("优惠卷类型不是人工分配类型");
        }else if(num == 1003){
            result.setCode(10013);
            result.setMsg("不能多次分配");
        }else{
            result.getData().put("result", num);
        }
        logParamOut(MyFunctions.WEB_ADMIN_IMPORT_CODE,JSON.toJSONString(result));
        return result;
    }

    @PostMapping("batchCode")
    public OperaResult batchCode(@RequestBody CouponUseInfoBean bean, OperaResult result){
        logParamIn(MyFunctions.WEB_ADMIN_BATCH_CODE,JSON.toJSONString(bean));
        int num = useInfoService.batchCode(bean);
        if(num == 0){
            result.setCode(10010);
            result.setMsg("批量生成失败");
        }else if(num == 1001){
            result.setCode(10011);
            result.setMsg("优惠卷不存在");
        }else if(num == 1002){
            result.setCode(10012);
            result.setMsg("优惠卷类型不是人工分配类型");
        }else if(num == 1003){
            result.setCode(10013);
            result.setMsg("不能多次分配");
        }else if(num == 1004){
            result.setCode(10014);
            result.setMsg("未在发布有效期内");
        }else if(num == 1005){
            result.setCode(10015);
            result.setMsg("优惠券已下线");
        }else{
            result.getData().put("result", num);
        }
        logParamOut(MyFunctions.WEB_ADMIN_BATCH_CODE,JSON.toJSONString(result));
        return result;
    }

    @PostMapping("consume")
    public OperaResult consumeCoupon(@RequestBody CouponUseInfoBean bean, OperaResult result){
        logParamIn(MyFunctions.WEB_ADMIN_CONSUME_COUPON,JSONUtil.toJsonString(bean));
        CouponUseInfoX coupon = couponService.adminConsumeCoupon(bean);
        if(coupon == null){
            result.setCode(40012);
            result.setMsg("优惠券不存在");
        }else if(coupon.getStatus() == 3){
            result.setCode(40013);
            result.setMsg("该券已核销");
        }else if(coupon.getStatus() == 4){
            result.setCode(40014);
            result.setMsg("该券已过期");
        }else{
            result.getData().put("id",coupon.getCouponId());
            result.getData().put("code",coupon.getCode());
        }
        return result;
    }

    @PostMapping("findByMpu")
    public OperaResult selectCouponByMpu(@RequestBody AoyiProdBean bean,
                                         @RequestHeader String appId,
                                         OperaResult result){
        bean.setAppId(appId);
        List<CouponBean> couponBeans = couponService.selectCouponByMpu(bean);
        result.getData().put("result",couponBeans);
        return result;
    }

    @GetMapping("release")
    public OperaResult findReleaseCoupon(Integer pageNo,
                                         Integer pageSize,
                                         String appId,
                                         Integer couponType,
                                         OperaResult result){
        PageableData<Coupon> releaseCoupon = couponService.findReleaseCoupon(pageNo, pageSize, appId, couponType);
        result.getData().put("result", releaseCoupon);
        return result;
    }

    @PostMapping("redemption")
    public OperaResult redemption(@RequestBody CouponUseInfoBean bean, OperaResult result){
        logParamIn(MyFunctions.WEB_ADMIN_REDEMPTION_COUPON,JSONUtil.toJsonString(bean));
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

    @GetMapping("unCollect")
    public OperaResult findUnCollect(CouponUseInfoBean bean, OperaResult result){
        result.getData().put("result", useInfoService.findUnCollect(bean));
        return result;
    }

    private void logParamIn(String functionDescription, String msg){
        log.info("{} 入参:{}",functionDescription,msg);
    }
    private void logParamOut(String functionDescription, String msg){
        log.info("{} 出参:{}",functionDescription,msg);
    }

}
