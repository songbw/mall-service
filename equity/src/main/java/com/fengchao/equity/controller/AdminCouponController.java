package com.fengchao.equity.controller;

import com.fengchao.equity.bean.*;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.Coupon;
import com.fengchao.equity.model.CouponUseInfoX;
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
        int num = couponService.deleteCoupon(id);
        if(num == 0){
            result.setCode(700101);
            result.setMsg("????????????");
        }else if(num == 1){
            result.getData().put("result", num);
        }else if(num == 2){
            result.setCode(700102);
            result.setMsg("?????????????????????????????????");
        }
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
            result.setMsg("??????????????????");
        }else if(num == 1001){
            result.setCode(10011);
            result.setMsg("??????????????????");
        }else if(num == 1002){
            result.setCode(10012);
            result.setMsg("???????????????????????????????????????");
        }else if(num == 1003){
            result.setCode(10013);
            result.setMsg("??????????????????");
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
            result.setMsg("??????????????????");
        }else if(num == 1001){
            result.setCode(10011);
            result.setMsg("??????????????????");
        }else if(num == 1002){
            result.setCode(10012);
            result.setMsg("???????????????????????????????????????");
        }else if(num == 1003){
            result.setCode(10013);
            result.setMsg("??????????????????");
        }else if(num == 1004){
            result.setCode(10014);
            result.setMsg("????????????????????????");
        }else if(num == 1005){
            result.setCode(10015);
            result.setMsg("??????????????????");
        }else{
            result.getData().put("result", num);
        }
        return result;
    }

    @PostMapping("consume")
    public OperaResult consumeCoupon(@RequestBody CouponUseInfoBean bean, OperaResult result){
        CouponUseInfoX coupon = couponService.adminConsumeCoupon(bean);
        if(coupon == null){
            result.setCode(40012);
            result.setMsg("??????????????????");
        }else if(coupon.getStatus() == 3){
            result.setCode(40013);
            result.setMsg("???????????????");
        }else if(coupon.getStatus() == 4){
            result.setCode(40014);
            result.setMsg("???????????????");
        }else{
            result.getData().put("id",coupon.getCouponId());
            result.getData().put("code",coupon.getCode());
        }
        return result;
    }

    @PostMapping("findByMpu")
    public OperaResult selectCouponByMpu(@RequestBody AoyiProdBean bean, OperaResult result){
        List<CouponBean> couponBeans = couponService.selectCouponByMpu(bean);
        result.getData().put("result",couponBeans);
        return result;
    }

    @GetMapping("release")
    public OperaResult findReleaseCoupon(Integer pageNo, Integer pageSize, OperaResult result){
        PageableData<Coupon> releaseCoupon = couponService.findReleaseCoupon(pageNo, pageSize);
        result.getData().put("result", releaseCoupon);
        return result;
    }
}
