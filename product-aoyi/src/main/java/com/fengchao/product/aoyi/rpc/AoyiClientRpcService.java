package com.fengchao.product.aoyi.rpc;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import com.fengchao.product.aoyi.feign.VendorsServiceClient;
import com.fengchao.product.aoyi.rpc.extmodel.SysCompany;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.BrandResDto;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-27 上午10:34
 */
@Service
@Slf4j
public class AoyiClientRpcService {

    private AoyiClientService aoyiClientService;

    @Autowired
    public AoyiClientRpcService(AoyiClientService aoyiClientService) {
        this.aoyiClientService = aoyiClientService;
    }

    /**
     * 查询所有的商户信息
     *
     * @return
     */
    public List<BrandResDto> weipinhuiGetBrand(Integer pageNumber, Integer pageSize) {
        // 返回值
        List<BrandResDto> brandResDtoList = new ArrayList<>();

        // 执行rpc调用
        log.info("同步唯品会品牌信息 调用aoyi-client rpc服务 入参:无");

        // 将merchantIdList转成Long型
        OperaResponse<List<BrandResDto>> resultObject = aoyiClientService.weipinhuiGetBrand(pageNumber, pageSize);
        log.info("同步唯品会品牌信息 调用aoyi-client rpc服务 返回:{}", JSONUtil.toJsonString(resultObject));

        // 处理返回
        if (resultObject.getCode() == 200) {
            brandResDtoList = resultObject.getData();
        } else {
            log.warn("同步唯品会品牌信息 调用aoyi-client rpc服务 错误!");
        }

        log.info("AoyiClientRpcService#weipinhuiGetBrand 调用aoyi-client rpc服务 返回:{}",
                JSONUtil.toJsonString(brandResDtoList));

        return brandResDtoList;
    }
}
