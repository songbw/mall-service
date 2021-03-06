package com.fengchao.aggregation.controller;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.aggregation.bean.AggregationBean;
import com.fengchao.aggregation.bean.OperaResult;
import com.fengchao.aggregation.bean.PageBean;
import com.fengchao.aggregation.bean.QueryBean;
import com.fengchao.aggregation.model.Aggregation;
import com.fengchao.aggregation.model.AggregationGroup;
import com.fengchao.aggregation.service.AggregationGroupService;
import com.fengchao.aggregation.service.AggregationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/adminAggregation", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminAggregationController {

    @Autowired
    private AggregationService aggregationService;
    @Autowired
    private AggregationGroupService groupService;

    @PostMapping("create")
    public OperaResult createAggregation(@RequestBody Aggregation bean, @RequestHeader("merchant") Integer merchantId, OperaResult result){
        bean.setMerchantId(merchantId);
        aggregationService.createAggregation(bean);
        result.getData().put("aggregationId",bean.getId());
        return result;
    }

    @GetMapping("find")
    public OperaResult findAggregation(QueryBean bean, @RequestHeader("merchant") Integer merchantId, OperaResult result){
        result.getData().put("result", aggregationService.findAggregation(bean, merchantId));
        return result;
    }

    @GetMapping("findById")
    public OperaResult findAggregationById(Integer id, @RequestHeader("merchant") Integer merchantId, OperaResult result){
        Aggregation aggregation = aggregationService.findAdminAggregationById(id);
        result.getData().put("result", aggregation);
        return result;
    }

    @PostMapping("update")
    public OperaResult updateAggregation(@RequestBody Aggregation bean, @RequestHeader("merchant") Integer merchantId, OperaResult result){
        bean.setMerchantId(merchantId);
        int i = aggregationService.updateAggregation(bean);
        if(i == 0){
            result.getData().put("result", "???????????????????????????");
        }
        return result;
    }

    @PostMapping("updateContent")
    public OperaResult updateContent(@RequestBody JSONObject object, @RequestHeader("merchant") Integer merchantId, OperaResult result){
        Aggregation aggregation = new Aggregation();
        aggregation.setId(Integer.parseInt(object.getString("id")));
        aggregation.setContent(object.getString("content"));
        result.getData().put("result", aggregationService.updateContent(aggregation));
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deleteAggregation(Integer id, @RequestHeader("merchant") Integer merchantId, OperaResult result){
        result.getData().put("result",aggregationService.deleteAggregation(id));
        return result;
    }

    @PostMapping("search")
    public OperaResult serachAggregation(@RequestBody AggregationBean bean, @RequestHeader("merchant") Integer merchantId, OperaResult result){
        bean.setMerchantId(merchantId);
        PageBean pageBean = aggregationService.serachAggregation(bean);
        result.getData().put("result",pageBean);
        return result;
    }

    @PostMapping("createGroup")
    public OperaResult createGroup(@RequestBody AggregationGroup bean, @RequestHeader("merchant") Integer merchantId, OperaResult result){
        bean.setMerchantId(merchantId);
        groupService.createGroup(bean);
        result.getData().put("id",bean.getId());
        return result;
    }

    @GetMapping("findGroup")
    public OperaResult findGroup(QueryBean bean, @RequestHeader("merchant") Integer merchantId, OperaResult result){
        result.getData().put("result", groupService.findGroup(bean, merchantId));
        return result;
    }

    @PutMapping("updateGroup")
    public OperaResult updateGroup(@RequestBody AggregationGroup bean, @RequestHeader("merchant") Integer merchantId, OperaResult result){
        bean.setMerchantId(merchantId);
        result.getData().put("result",groupService.updateGroup(bean));
        return result;
    }

    @DeleteMapping("deleteGroup")
    public OperaResult deleteGroup(Integer id, @RequestHeader("merchant") Integer merchantId, OperaResult result){
        int i = groupService.deleteGroup(id);
        if(i == 0){
            result.setMsg("?????????????????????????????????????????????");
        }
        result.getData().put("result",i);
        return result;
    }
}
