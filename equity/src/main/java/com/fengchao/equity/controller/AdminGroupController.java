package com.fengchao.equity.controller;

import com.fengchao.equity.bean.GroupsBean;
import com.fengchao.equity.bean.OperaResponse;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.bean.PageableData;
import com.fengchao.equity.bean.vo.GroupInfoReqVo;
import com.fengchao.equity.bean.vo.GroupInfoResVo;
import com.fengchao.equity.bean.vo.GroupTeamResVo;
import com.fengchao.equity.service.AdminGroupService;
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
            if (count > 0) {
                operaResponse.setData(true);
            } else {
                log.error("发布拼购活动 count:{}", count);
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
    public OperaResponse queryGroupInfoList(GroupInfoReqVo groupInfoReqVo, OperaResponse operaResponse) {
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

    @PostMapping("update/campaigns")
    public OperaResponse updateGroupInfo(GroupInfoReqVo groupInfoReqVo, OperaResponse operaResponse) {
        log.info("更新拼购活动信息 入参:{}", JSONUtil.toJsonString(groupInfoReqVo));

        try {
            // 执行更新
            int count = adminGroupService.updateGroupInfo(groupInfoReqVo);

            if (count > 0) {
                operaResponse.setData(true);
            } else {
                log.error("更新拼购活动信息失败 count:{}", count);
                throw new Exception("更新拼购活动信息失败");
            }
        } catch (Exception e) {
            log.error("更新拼购活动信息 异常:{}", e.getMessage(), e);

            operaResponse.setData(null);
            operaResponse.setCode(500);
            operaResponse.setMsg(e.getMessage());
        }

        log.info("更新拼购活动信息 返回:{}", JSONUtil.toJsonString(operaResponse));

        return operaResponse;
    }

    /**
     * 删除拼购活动
     *
     * @param groupInfoReqVo
     * @param operaResponse
     * @return
     */
    @PostMapping("delete/campaigns")
    public OperaResponse deleteGroupInfo(GroupInfoReqVo groupInfoReqVo, OperaResponse operaResponse) {
        log.info("删除拼购活动信息 入参:{}", JSONUtil.toJsonString(groupInfoReqVo));

        try {
            // 执行更新
            int count = adminGroupService.deleteGroupInfoById(groupInfoReqVo.getId());

            if (count > 0) {
                operaResponse.setData(true);
            } else {
                log.error("删除拼购活动信息失败 count:{}", count);
                throw new Exception("删除拼购活动信息失败");
            }
        } catch (Exception e) {
            log.error("删除拼购活动信息 异常:{}", e.getMessage(), e);

            operaResponse.setData(null);
            operaResponse.setCode(500);
            operaResponse.setMsg(e.getMessage());
        }

        log.info("删除拼购活动信息 返回:{}", JSONUtil.toJsonString(operaResponse));

        return operaResponse;
    }


    /**
     * 根据id获取活动详情
     *
     * @param groupId
     * @param operaResponse
     * @return
     */
    @GetMapping("campaigns/detail")
    public OperaResponse queryGroupInfo(@RequestParam("groupId") Long groupId, OperaResponse operaResponse) {
        log.info("根据id获取活动详情 入参:{}", groupId);

        try {
            // 执行更新
            GroupInfoResVo groupInfoResVo = adminGroupService.queryGroupInfoById(groupId);

            operaResponse.setData(groupInfoResVo);
        } catch (Exception e) {
            log.error("根据id:{}获取活动详情 异常:{}", groupId, e.getMessage(), e);

            operaResponse.setData(null);
            operaResponse.setCode(500);
            operaResponse.setMsg(e.getMessage());
        }

        log.info("根据id:{}获取活动详情 返回:{}", groupId, JSONUtil.toJsonString(operaResponse));

        return operaResponse;
    }


    /**
     * 获取某活动下的team详情
     *
     * @param groupInfoReqVo
     * @param teamStatus
     * @param operaResponse
     * @return
     */
    @GetMapping("campaigns/list/teams")
    public OperaResponse queryGroupInfo(GroupInfoReqVo groupInfoReqVo,
                                        @RequestParam("teamStatus") Integer teamStatus,
                                        OperaResponse operaResponse) {
        log.info("获取活动team详情 入参：{}", JSONUtil.toJsonString(groupInfoReqVo));

        try {
            PageableData<GroupTeamResVo> groupTeamResVoPageableData =
                    adminGroupService.queryTeamListPageable(groupInfoReqVo);

            operaResponse.setData(groupTeamResVoPageableData);
        } catch (Exception e) {
            log.error("获取活动team详情 异常:{}", e.getMessage(), e);

            operaResponse.setData(null);
            operaResponse.setCode(500);
            operaResponse.setMsg("获取活动team详情 异常");
        }

        log.info("获取活动team详情 返回：{}", JSONUtil.toJsonString(operaResponse));

        return operaResponse;
    }


}
