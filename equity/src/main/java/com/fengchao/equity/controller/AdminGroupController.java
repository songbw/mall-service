package com.fengchao.equity.controller;

import com.fengchao.equity.bean.GroupsBean;
import com.fengchao.equity.service.AdminGroupService;
import com.fengchao.equity.bean.OperaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/adminGroupBuying", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminGroupController {

    @Autowired
    private AdminGroupService service;

    @PostMapping("campaigns")
    public OperaResult createGroups(@RequestBody GroupsBean bean, OperaResult result){
        service.createGroups(bean);
        result.getData().put("groupId",bean.getId());
        return result;
    }

    @GetMapping("campaigns")
    public OperaResult findGroup(GroupsBean bean, OperaResult result){
        result.getData().put("result", service.findGroups(bean));
        return result;
    }

    @PutMapping("campaigns")
    public OperaResult updateGroups(@RequestBody GroupsBean bean, OperaResult result){
        result.getData().put("result",service.updateGroups(bean));
        return result;
    }

    @DeleteMapping("campaigns/{id}")
    public OperaResult deleteGroups(@PathVariable("id")Integer id, OperaResult result){
        result.getData().put("result",service.deleteGroups(id));
        return result;
    }

}
