package com.fengchao.product.aoyi.feign.hystric;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.bean.VendorsProfileBean;
import com.fengchao.product.aoyi.feign.VendorsService;
import org.springframework.stereotype.Component;

@Component
public class VendorsServiceH implements VendorsService {

    @Override
    public OperaResult vendorInfo(int id) {
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("多商户服务失败 " + id);
        return result;
    }
}
