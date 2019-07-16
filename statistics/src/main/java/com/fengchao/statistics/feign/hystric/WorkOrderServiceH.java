package com.fengchao.statistics.feign.hystric;

import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.WorkOrdersService;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderServiceH implements WorkOrdersService {
    @Override
    public OperaResult refundCount(String timeStart, String timeEnd) {
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("获取工单服务失败 " + timeStart + " " + timeEnd);
        return result;
    }
}
