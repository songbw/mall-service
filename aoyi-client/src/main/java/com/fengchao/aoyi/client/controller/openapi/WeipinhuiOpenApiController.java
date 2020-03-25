package com.fengchao.aoyi.client.controller.openapi;

import com.fengchao.aoyi.client.bean.OperaResult;
import com.fengchao.aoyi.client.bean.dto.weipinhui.req.AlterationMessageRequest;
import com.fengchao.aoyi.client.bean.dto.weipinhui.req.AoyiConfirmOrderRequest;
import com.fengchao.aoyi.client.bean.dto.weipinhui.req.AoyiReleaseOrderRequest;
import com.fengchao.aoyi.client.bean.dto.weipinhui.req.AoyiRenderOrderRequest;
import com.fengchao.aoyi.client.bean.dto.weipinhui.res.*;
import com.fengchao.aoyi.client.utils.JSONUtil;
import com.fengchao.aoyi.client.weipinhuiService.ProductWeipinhuiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/weipinhui", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WeipinhuiOpenApiController {

    private ProductWeipinhuiService productWeipinhuiService;

    @Autowired
    public WeipinhuiOpenApiController(ProductWeipinhuiService productWeipinhuiService) {
        this.productWeipinhuiService = productWeipinhuiService;
    }

    /**
     * 变更消息推送
     * <p>
     * http://localhost:8001/weipinhui/alterationMessage
     *
     * @return
     */
    @PostMapping("/alterationMessage")
    public OperaResult<List<BrandResDto>> alterationMessage(@RequestBody AlterationMessageRequest alterationMessageRequest) {
        log.info("变更消息推送 入参:{}", JSONUtil.toJsonString(alterationMessageRequest));

        OperaResult<List<BrandResDto>> operaResult = new OperaResult<>();
        try {
            // List<BrandResDto> brandResDtoList = productWeipinhuiService.getBrand(pageNumber, pageSize);

            operaResult.setData(null);
            operaResult.setCode(200);
        } catch (Exception e) {
            log.error("获取品牌列表 异常:{}", e.getMessage(), e);

            operaResult.setData(null);
            operaResult.setMsg("失败:" + e.getMessage());
            operaResult.setCode(500);
        }

        log.info("获取品牌列表 返回:{}", JSONUtil.toJsonString(operaResult));

        return operaResult;
    }



}
