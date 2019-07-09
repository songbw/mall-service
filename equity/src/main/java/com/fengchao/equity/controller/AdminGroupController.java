package com.fengchao.equity.controller;

import com.alibaba.fastjson.JSON;
import com.fengchao.equity.bean.GroupsBean;
import com.fengchao.equity.bean.OperaResponse;
import com.fengchao.equity.bean.PageableData;
import com.fengchao.equity.bean.vo.GroupInfoReqVo;
import com.fengchao.equity.bean.vo.GroupInfoResVo;
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
     * @param operaResponse
     * @return
     */
    @GetMapping("list/campaigns")
    public OperaResponse queryGroupList(GroupInfoReqVo groupInfoReqVo, OperaResponse operaResponse) {
        log.info("分页查询活动列表 入参：{}", JSON.toJSONString(groupInfoReqVo));

        try {
            PageableData<GroupInfoResVo> groupInfoResVoPageableData =
                    adminGroupService.queryGroupListPageable(groupInfoReqVo);

            operaResponse.setData(groupInfoResVoPageableData);
        } catch (Exception e) {
            log.error("分页查询活动列表 异常:{}", e.getMessage(), e);

            operaResponse.setData(null);
            operaResponse.setCode(500);
            operaResponse.setMsg("分页查询活动列表 异常");
        }

        log.info("分页查询活动列表 返回：{}", JSON.toJSONString(operaResponse));

        return operaResponse;
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
