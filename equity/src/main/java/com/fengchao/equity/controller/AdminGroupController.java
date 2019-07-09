package com.fengchao.equity.controller;

import com.alibaba.fastjson.JSON;
import com.fengchao.equity.bean.GroupsBean;
import com.fengchao.equity.bean.vo.GroupInfoReqVo;
import com.fengchao.equity.service.AdminGroupService;
import com.fengchao.equity.bean.OperaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/adminGroupBuying", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class AdminGroupController {

    @Autowired
    private AdminGroupService adminGroupService;

    @PostMapping("campaigns")
    public OperaResult createGroups(@RequestBody GroupsBean bean, OperaResult result){
        adminGroupService.createGroups(bean);
        result.getData().put("groupId",bean.getId());
        return result;
    }

    /**
     * 分页查询活动列表
     *
     * @param groupInfoReqVo
     * @param result
     * @return
     */
    @GetMapping("list/campaigns")
    public OperaResult queryGroupList(GroupInfoReqVo groupInfoReqVo, OperaResult result) {
        log.info("分页查询活动列表 入参：", JSON.toJSONString(groupInfoReqVo));


        // result.getData().put("result", adminGroupService.findGroups(bean));
        return result;
    }

    @Deprecated
    @GetMapping("campaigns")
    public OperaResult findGroup(GroupsBean bean, OperaResult result){
        result.getData().put("result", adminGroupService.findGroups(bean));
        return result;
    }





    @PutMapping("campaigns")
    public OperaResult updateGroups(@RequestBody GroupsBean bean, OperaResult result){
        result.getData().put("result", adminGroupService.updateGroups(bean));
        return result;
    }

    @DeleteMapping("campaigns/{id}")
    public OperaResult deleteGroups(@PathVariable("id")Integer id, OperaResult result){
        result.getData().put("result", adminGroupService.deleteGroups(id));
        return result;
    }

}
