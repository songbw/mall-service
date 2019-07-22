package com.fengchao.statistics.feign.hystric;

import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.SsoService;
import org.springframework.stereotype.Component;

@Component
public class SsoServiceH implements SsoService {
    @Override
    public OperaResult count() {
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("获取SSO服务失败 ");
        return result;
    }
}
