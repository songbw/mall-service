package com.fengchao.equity.controller;

import com.fengchao.equity.bean.*;
import com.fengchao.equity.model.Coupon;
import com.fengchao.equity.service.CouponService;
import com.fengchao.equity.service.CouponUseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/adminCoupon", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminCouponController {

    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponUseInfoService useInfoService;


    @PostMapping("create")
    public OperaResult createCoupon(@RequestBody CouponBean bean, OperaResult result){
        result.getData().put("couponId",couponService.createCoupon(bean));
        return result;
    }

    @GetMapping("find")
    public OperaResult findCoupon(Integer offset,Integer limit, OperaResult result){
        PageBean coupon = couponService.findCoupon(offset, limit);
        result.getData().put("result", coupon);
        return result;
    }

    @PostMapping("search")
    public OperaResult serachCoupon(@RequestBody CouponSearchBean bean, OperaResult result){
        PageBean pageBean = couponService.serachCoupon(bean);
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
        int pageBean = couponService.updateCoupon(bean);
        result.getData().put("result",pageBean);
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deleteCoupon(Integer id, OperaResult result){
        int pageBean = couponService.deleteCoupon(id);
        result.getData().put("result",pageBean);
        return result;
    }

    @GetMapping("usageById")
    public OperaResult findCollect(CouponUseInfoBean bean, OperaResult result){
        result.getData().put("result", useInfoService.findCollect(bean));
        return result;
    }

    @PostMapping("importCode")
    public OperaResult importCode(@RequestBody CouponUseInfoBean bean, OperaResult result){
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
        return result;
    }

    @PostMapping("batchCode")
    public OperaResult batchCode(@RequestBody CouponUseInfoBean bean, OperaResult result){
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
        }else{
            result.getData().put("result", num);
        }
        return result;
    }

    @PostMapping("consume")
    public OperaResult consumeCoupon(@RequestBody CouponUseInfoBean bean, OperaResult result){
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

    @PostMapping("findBySku")
    public OperaResult selectCouponBySku(@RequestBody AoyiProdBean bean, OperaResult result){
        List<CouponBean> couponBeans = couponService.selectCouponBySku(bean);
        result.getData().put("result",couponBeans);
        return result;
    }
}
