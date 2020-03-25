package com.fengchao.product.aoyi.feign;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.feign.hystric.BaseServiceH;
import com.fengchao.product.aoyi.feign.hystric.ESServiceH;
import com.fengchao.product.aoyi.model.AyFcImages;
import com.fengchao.product.aoyi.rpc.extmodel.Email;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "base", fallback = BaseServiceH.class)
public interface BaseService {

    @RequestMapping(value = "/down/upload", method = RequestMethod.POST)
    OperaResult downUpload(@RequestBody AyFcImages images);

    @RequestMapping(value = "/mail/send", method = RequestMethod.POST)
    OperaResponse sendMail(@RequestBody Email email);
}
