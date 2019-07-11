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
        log.info("创建拼购活动信息 入参:{}", JSONUtil.toJsonString(groupInfoReqVo));

        try {
            // 1.创建group
            Long id = adminGroupService.createGroupInfo(groupInfoReqVo);

            // 2.处理返回
            Map<String, Long> result = new HashMap<>();
            result.put("id", id);
            operaResponse.setData(result);
        } catch (Exception e) {
            log.error("创建拼购活动信息 异常:{}", e.getMessage(), e);

            operaResponse.setCode(500);
            operaResponse.setMsg(e.getMessage());
            operaResponse.setData(null);
        }

        log.info("创建拼购活动信息 返回:{}", JSONUtil.toJsonString(operaResponse));
        return operaResponse;
    }

    /**
     * 发布拼购活动
     *
     * @param groupInfoReqVo
     * @param operaResponse
     * @return
     */
    @PostMapping("publish/campaigns")
    public OperaResponse publishGroupInfo(@RequestBody GroupInfoReqVo groupInfoReqVo, OperaResponse operaResponse){
        log.info("发布拼购活动 入参:{}", JSONUtil.toJsonString(groupInfoReqVo));

        try {
            // 1.发布group
            int count = adminGroupService.publishGroupInfo(groupInfoReqVo);

            // 2.处理返回
            if (count > 1) {
                operaResponse.setData(true);
            } else {
                throw new Exception("发布拼购活动 更新状态失败");
            }
        } catch (Exception e) {
            log.error("发布拼购活动 异常:{}", e.getMessage(), e);

            operaResponse.setCode(500);
            operaResponse.setMsg(e.getMessage());
            operaResponse.setData(null);
        }

        log.info("发布拼购活动 返回:{}", JSONUtil.toJsonString(operaResponse));
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
