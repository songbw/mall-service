package com.fengchao.equity.controller;

import com.fengchao.equity.bean.OperaResponse;
import com.fengchao.equity.bean.PromotionBean;
import com.fengchao.equity.model.Promotion;
import com.fengchao.equity.service.PromotionService;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/promotion", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class PromotionController {

    @Autowired
    private PromotionService service;

    @GetMapping("findPromotion")
    public OperaResult findPromotionToUser(Integer id, Boolean detail, OperaResult result){
        result.getData().put("result", service.findPromotionToUser(id, detail));
        return result;
    }

    @GetMapping("mpu")
    public OperaResult findPromotionByMpu(@RequestParam("skuId")String Mpu, OperaResult result){
        result.getData().put("result", service.findPromotionByMpu(Mpu));
        return result;
    }

    @GetMapping("getCurrentScheduleMpu")
    public OperaResult findCurrentSchedule(Integer num, OperaResult result){
        result.getData().put("result", service.findCurrentSchedule(num));
        return result;
    }

    @GetMapping("findById")
    public OperaResult findPromotionName(Integer id, OperaResult result){
        result.getData().put("result", service.findPromotionName(id));
        return result;
    }

    @GetMapping("findByIdList")
    public OperaResponse findPromotionListByIdList(@RequestParam("idList") List<Integer> idList,
                                                 OperaResponse<List<PromotionBean>> operaResponse) {
        log.info("查询活动列表 根据id集合查询 入参:{}", JSONUtil.toJsonString(idList));

        try {
            // 查询
            List<PromotionBean> promotionList = service.findPromotionListByIdList(idList);

            operaResponse.setData(promotionList);
        } catch (Exception e) {
            log.error("查询活动列表 根据id集合查询 异常:{}", e.getMessage(), e);

            operaResponse.setData(null);
            operaResponse.setCode(500);
            operaResponse.setMsg("查询活动列表 根据id集合查询 异常");
        }

        log.info("查询活动列表 根据id集合查询 返回:{}", JSONUtil.toJsonString(operaResponse));
        return operaResponse;
    }

    @GetMapping("findOnline")
    public OperaResult findOnlineMpu(OperaResult result){
        result.getData().put("result", service.findOnlineMpu());
        return result;
    }
}
