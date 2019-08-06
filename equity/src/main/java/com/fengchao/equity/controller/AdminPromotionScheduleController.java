package com.fengchao.equity.controller;

import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.model.PromotionSchedule;
import com.fengchao.equity.service.PromotionScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/adminPromotion/schedule", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class AdminPromotionScheduleController {

    @Autowired
    private PromotionScheduleService scheduleService;

    @PostMapping("create")
    public OperaResult createSchedule(@RequestBody PromotionSchedule bean, OperaResult result){
        scheduleService.createSchedule(bean);
        result.getData().put("scheduleId", bean.getId());
        return result;
    }

    @GetMapping("find")
    public OperaResult findSchedule(Integer offset, Integer limit, OperaResult result){
        result.getData().put("result", scheduleService.findSchedule(offset, limit));
        return result;
    }

    @GetMapping("findById")
    public OperaResult findScheduleById(Integer id, OperaResult result){
        result.getData().put("result", scheduleService.findScheduleById(id));
        return result;
    }

    @PutMapping("update")
    public OperaResult updateSchedule(@RequestBody PromotionSchedule bean, OperaResult result){
        result.getData().put("result", scheduleService.updateSchedule(bean));
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deletePromotion(Integer id, OperaResult result){
        result.getData().put("result", scheduleService.deletePromotion(id));
        return result;
    }

}
