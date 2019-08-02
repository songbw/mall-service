package com.fengchao.statistics.controller;

import com.fengchao.statistics.bean.OperaResponse;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.bean.vo.MerchantCityRangeStatisticResVo;
import com.fengchao.statistics.bean.vo.MerchantOverviewResVo;
import com.fengchao.statistics.exception.StatisticsException;
import com.fengchao.statistics.service.MerchantStatisticService;
import com.fengchao.statistics.service.PeriodOverviewService;
import com.fengchao.statistics.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 时间段概览统计分析
 * @author zp
 */
@RestController
@RequestMapping(value = "/statistic/merchant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class MerchantRangeStatisticController {

    @Autowired
    private MerchantStatisticService merchantStatisticService;

    @GetMapping("/city/overview")
    private OperaResponse overview(String startDate, String endDate, Integer merchantId, OperaResponse operaResponse) {
        log.info("根据时间范围获取按照商户-城市(天)维度的统计结果, 入参 startDate:{}, endDate:{}, merchantId:{}",
                startDate, endDate, merchantId);

        try {
            if (merchantId == null || merchantId <= 0) {
                throw new Exception("入参 merchantId不合法");
            }

            Map<String, List<MerchantCityRangeStatisticResVo>> resultMap =
                    merchantStatisticService.fetchStatisticDailyResult(startDate, endDate, merchantId);
            operaResponse.setData(resultMap);
        } catch (Exception e) {
            log.info("根据时间范围获取按商户-城市(天)维度的统计结果 异常:{}", e.getMessage(), e);

            operaResponse.setCode(500);
            operaResponse.setMsg("根据时间范围获取按照商户-城市(天)维度的统计结果异常:" + e.getMessage());
            operaResponse.setData(null);
        }

        log.info("根据时间范围获取按照商户-城市(天)维度的统计结果 返回:{}", JSONUtil.toJsonString(operaResponse));

        return operaResponse;
    }

}
