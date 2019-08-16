package com.fengchao.equity.controller;

import com.fengchao.equity.bean.PromotionBean;
import com.fengchao.equity.bean.PromotionResult;
import com.fengchao.equity.model.PromotionX;
import com.fengchao.equity.service.PromotionService;
import com.fengchao.equity.bean.OperaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/adminPromotion", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminPromotionController {

    @Autowired
    private PromotionService service;

    @PostMapping("create")
    public OperaResult createPromotion(@RequestBody PromotionX bean, OperaResult result){
        service.createPromotion(bean);
        result.getData().put("promotionId", bean.getId());
        return result;
    }

    @GetMapping("find")
    public OperaResult findPromotion(Integer offset, Integer limit, OperaResult result){
        result.getData().put("result", service.findPromotion(offset, limit));
        return result;
    }

    @GetMapping("findById")
    public OperaResult findPromotionById(Integer id, OperaResult result){
        result.getData().put("result", service.findPromotionById(id));
        return result;
    }

    @PostMapping("update")
    public OperaResult updatePromotion(@RequestBody PromotionX bean, OperaResult result){
        PromotionResult promotionResult = service.updatePromotion(bean);
        if(promotionResult.getNum() == 2){
            result.setCode(500);
            result.setMsg("同时间有上线商品");
            result.getData().put("mpus", promotionResult.getMpus());
        }else if(promotionResult.getNum() == 3){
            result.setCode(501);
            result.setMsg("当天只能有1个秒杀活动");
            result.getData().put("promotionId", promotionResult.getPromotionId());
        }else{
            result.getData().put("result", promotionResult.getNum());
        }
        return result;
    }

    @PostMapping("search")
    public OperaResult searchPromotion(@RequestBody PromotionBean bean, OperaResult result){
        result.getData().put("result", service.searchPromotion(bean));
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deletePromotion(Integer id, OperaResult result){
        int num = service.deletePromotion(id);
        if(num == 0){
            result.setCode(700101);
            result.setMsg("删除失败");
        }else if(num == 1){
            result.getData().put("result", num);
        }else if(num == 2){
            result.setCode(700102);
            result.setMsg("活动已发布，不能删除");
        }

        return result;
    }

    @PostMapping("createContent")
    public OperaResult createContent(@RequestBody PromotionX bean, OperaResult result){
        int num = service.createContent(bean);
        if(num != 1){
            result.setCode(500);
            result.setMsg("有商品mpu为空");
        }
        result.getData().put("result", num);
        return result;
    }

    @PutMapping("updateContent")
    public OperaResult updateContent(@RequestBody PromotionX bean, OperaResult result){
        result.getData().put("result",service.updateContent(bean));
        return result;
    }

    @DeleteMapping("deleteContent")
    public OperaResult deleteContent(@RequestBody PromotionX bean, OperaResult result){
        result.getData().put("result", service.deleteContent(bean));
        return result;
    }

    @GetMapping("release")
    public OperaResult findReleasePromotion(OperaResult result){
        result.getData().put("result", service.findReleasePromotion());
        return result;
    }
}
