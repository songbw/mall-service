package com.fengchao.aggregation.feign.hystric;

import com.fengchao.aggregation.bean.Email;
import com.fengchao.aggregation.bean.OperaResponse;
import com.fengchao.aggregation.feign.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BaseServiceH implements BaseService {


    @Override
    public OperaResponse sendMail(Email email) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }
}
