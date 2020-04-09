package com.fengchao.equity.feign.hystric;

import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.feign.VendorsService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
public class VendorsServiceH implements VendorsService {

    @Override
    public OperaResult vendorInfo(int id) {
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("多商户服务失败 " + id);
        return result;
    }

    @Override
    public OperaResult
    getEmployeeInfoByPhone(String phone){
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("多商户服务获取员工信息失败 phone=" + phone);
        return result;
    }

    @Override
    public OperaResult
    getEmployeeInfoByPhoneList(@RequestParam("phoneList") List<String> phoneList){
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("多商户服务获取员工号列表失败 ");
        return result;
    }
}
