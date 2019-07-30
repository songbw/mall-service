package com.fengchao.statistics.controller;

import com.fengchao.statistics.bean.OperaResponse;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.bean.vo.OverviewResVo;
import com.fengchao.statistics.exception.StatisticsException;
import com.fengchao.statistics.service.OverviewService;
import com.fengchao.statistics.utils.JSONUtil;
import com.fengchao.statistics.utils.JobClientUtils;
import com.github.ltsopensource.jobclient.JobClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 概览统计分析
 *
 * @author zp
 */
@RestController
@RequestMapping(value = "/overview", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class OverviewController {

    @Autowired
    private OverviewService overviewService;
    @Autowired
    private JobClient jobClient;

    /**
     * 获取总揽统计数据
     *
     * @param operaResponse
     * @return
     */
    @GetMapping("sum")
    private OperaResponse overview(OperaResponse operaResponse) {
        log.info("获取总揽统计数据 入参:无");

        try {
            OverviewResVo overviewResVo = overviewService.fetchOverviewStatistic();
            operaResponse.setData(overviewResVo);
        } catch (Exception e) {
            log.error("获取总揽统计数据 异常:{}", e.getMessage(), e);

            operaResponse.setCode(500);
            operaResponse.setMsg("获取总揽统计数据异常");
        }

        log.info("获取总揽统计数据 返回:{}", JSONUtil.toJsonString(operaResponse));

        return operaResponse;
    }

//    @GetMapping("cron")
//    private OperaResult cron(OperaResult result) {
//        // JobClientUtils.orderStatisticsTrigger(jobClient);
//        return result;
//    }

}
