package com.fengchao.product.aoyi.rpc;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.AoyiItemDetailResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.BrandResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.CategoryResDto;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * 查询唯品会品牌信息
     *
     * @return
     */
    public List<BrandResDto> weipinhuiGetBrand(Integer pageNumber, Integer pageSize) {
        // 返回值
        List<BrandResDto> brandResDtoList = new ArrayList<>();

        // 执行rpc调用
        log.info("查询唯品会品牌信息 调用aoyi-client rpc服务 入参 pageNumber:{}, pageSize:{}",
                pageNumber, pageSize);

        // 将merchantIdList转成Long型
        OperaResponse<List<BrandResDto>> resultObject = aoyiClientService.weipinhuiGetBrand(pageNumber, pageSize);
        log.info("查询唯品会品牌信息 调用aoyi-client rpc服务 返回:{}", JSONUtil.toJsonString(resultObject));

        // 处理返回
        if (resultObject.getCode() == 200) {
            brandResDtoList = resultObject.getData();
        } else {
            log.warn("查询唯品会品牌信息 调用aoyi-client rpc服务 错误!");
        }

        log.info("AoyiClientRpcService#weipinhuiGetBrand 调用aoyi-client rpc服务 返回:{}",
                JSONUtil.toJsonString(brandResDtoList));

        return brandResDtoList;
    }

    /**
     * 查询唯品会类目信息
     *
     * @return
     */
    public List<CategoryResDto> weipinhuiGetCategory(Integer pageNumber, Integer pageSize) {
        // 返回值
        List<CategoryResDto> categoryResDtoList = new ArrayList<>();

        // 执行rpc调用
        log.info("查询唯品会类目信息 调用aoyi-client rpc服务 入参 pageNumber:{}, pageSize:{}",
                pageNumber, pageSize);

        // 将merchantIdList转成Long型
        OperaResponse<List<CategoryResDto>> resultObject = aoyiClientService.weipinhuiGetCategory(pageNumber, pageSize);
        log.info("查询唯品会类目信息 调用aoyi-client rpc服务 返回:{}", JSONUtil.toJsonString(resultObject));

        // 处理返回
        if (resultObject.getCode() == 200) {
            categoryResDtoList = resultObject.getData();
        } else {
            log.warn("查询唯品会类目信息 调用aoyi-client rpc服务 错误!");
        }

        log.info("AoyiClientRpcService#weipinhuiGetCategory 调用aoyi-client rpc服务 返回:{}",
                JSONUtil.toJsonString(categoryResDtoList));

        return categoryResDtoList;
    }

    /**
     * 查询唯品会商品详情
     *
     * @return
     */
    public AoyiItemDetailResDto weipinhuiQueryItemDetial(String itemId) {
        // 返回值
        AoyiItemDetailResDto aoyiItemDetailResDto = null;

        // 执行rpc调用
        log.info("查询唯品会商品详情 调用aoyi-client rpc服务 入参:{}", itemId);

        // 将merchantIdList转成Long型
        OperaResponse<AoyiItemDetailResDto> resultObject = aoyiClientService.weipinhuiQueryItemDetial(itemId);
        log.info("查询唯品会商品详情 调用aoyi-client rpc服务 返回:{}", JSONUtil.toJsonString(resultObject));

        // 处理返回
        if (resultObject.getCode() == 200) {
            aoyiItemDetailResDto = resultObject.getData();
        } else {
            log.warn("查询唯品会商品详情 调用aoyi-client rpc服务 错误!");
        }

        log.info("AoyiClientRpcService#weipinhuiQueryItemDetial 调用aoyi-client rpc服务 返回:{}",
                JSONUtil.toJsonString(aoyiItemDetailResDto));

        return aoyiItemDetailResDto;
    }
}
