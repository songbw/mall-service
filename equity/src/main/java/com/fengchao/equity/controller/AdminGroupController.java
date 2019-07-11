package com.fengchao.equity.controller;

import com.fengchao.equity.bean.GroupsBean;
import com.fengchao.equity.bean.OperaResponse;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.bean.PageableData;
import com.fengchao.equity.bean.vo.GroupInfoReqVo;
import com.fengchao.equity.bean.vo.GroupInfoResVo;
import com.fengchao.equity.service.AdminGroupService;
import com.fengchao.equity.utils.DateUtil;
import com.fengchao.equity.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/adminGroupBuying", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class AdminGroupController {

    @Autowired
    private AdminGroupService adminGroupService;

    @PostMapping("campaigns")
    @Deprecated
    public OperaResult createGroups(@RequestBody GroupsBean bean, OperaResult result){
        adminGroupService.createGroups(bean);
        result.getData().put("groupId",bean.getId());
        return result;
    }

    /**
     * 创建活动信息
     *
     * @param groupInfoReqVo
     * @param operaResponse
     * @return
     */
    @PostMapping("create/campaigns")
    public OperaResponse createGroupInfo(@RequestBody GroupInfoReqVo groupInfoReqVo, OperaResponse operaResponse){
        log.info("创建活动信息 入参:{}", JSONUtil.toJsonString(groupInfoReqVo));

        try {
            // 1. 校验数据
            String effectiveStartDate = DateUtil.dateTimeFormat(groupInfoReqVo.getEffectiveStartDate(),
                    DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            String effectiveEndDate = DateUtil.dateTimeFormat(groupInfoReqVo.getEffectiveEndDate(),
                    DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            String nowDate = DateUtil.nowDateTime(DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            // 1.1 活动开始时间需要晚于当前时间30分钟
            Long diffMinutes = DateUtil.diffMinutes(nowDate, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS,
                    effectiveStartDate, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            if (diffMinutes < 30) {
                throw new Exception("活动开始时间需要晚于当前时间30分钟");
            }

            // 1.2 活动持续时间需要大于等于12小时
            Long diffHours = DateUtil.diffHours(effectiveStartDate, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS,
                    effectiveEndDate, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            if (diffHours < 12) {
                throw new Exception("活动持续时间需要大于等于12小时");
            }

            // 2.创建group
            Long id = adminGroupService.createGroupInfo(groupInfoReqVo);

            // 3.处理返回
            Map<String, Long> result = new HashMap<>();
            result.put("id", id);
            operaResponse.setData(result);
        } catch (Exception e) {
            log.error("创建活动信息 异常:{}", e.getMessage(), e);

            operaResponse.setCode(500);
            operaResponse.setMsg(e.getMessage());
            operaResponse.setData(null);
        }

        log.info("创建活动信息 返回:{}", JSONUtil.toJsonString(operaResponse));
        return operaResponse;
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
        log.info("分页查询活动列表 入参：{}", JSONUtil.toJsonString(groupInfoReqVo));

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

        log.info("分页查询活动列表 返回：{}", JSONUtil.toJsonString(operaResponse));

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
