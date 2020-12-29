package com.fengchao.product.aoyi.rpc;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.AoyiItemDetailResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.AoyiQueryInventoryResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.BrandResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.CategoryResDto;
import com.fengchao.product.aoyi.utils.AlarmUtil;
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
     * 查询唯品会itemId列表信息
     *
     * @return
     */
    public List<AoyiItemDetailResDto> weipinhuiQueryItemsList(Integer pageNumber, Integer pageSize) {
        // 返回值
        List<AoyiItemDetailResDto> aoyiItemDetailResDtoList = new ArrayList<>();

        // 执行rpc调用
        log.info("查询唯品会itemId列表信息 调用aoyi-client rpc服务 入参 pageNumber:{}, pageSize:{}",
                pageNumber, pageSize);

        // 将merchantIdList转成Long型
        OperaResponse<List<AoyiItemDetailResDto>> resultObject = aoyiClientService.weipinhuiQueryItemsList(pageNumber, pageSize);
        log.info("查询唯品会itemId列表信息 调用aoyi-client rpc服务 返回:{}", JSONUtil.toJsonStringWithoutNull(resultObject));

        // 处理返回
        if (resultObject.getCode() == 200) {
            aoyiItemDetailResDtoList = resultObject.getData();
        } else {
            log.warn("查询唯品会itemId列表信息 调用aoyi-client rpc服务 错误!");
        }

        log.info("AoyiClientRpcService#weipinhuiQueryItemsList 调用aoyi-client rpc服务 返回:{}",
                JSONUtil.toJsonStringWithoutNull(aoyiItemDetailResDtoList));

        return aoyiItemDetailResDtoList;
    }

    /**
     * 查询唯品会商品详情
     *
     * @return
     */
    public AoyiItemDetailResDto weipinhuiQueryItemDetial(String itemId) throws Exception {
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
            AlarmUtil.alarmAsync("查询唯品会商品详情 "+itemId+" 调用aoyi-client rpc服务 错误!", "出错了");
//            throw new Exception("查询唯品会商品详情 调用aoyi-client rpc服务 错误!!");
        }

        log.info("AoyiClientRpcService#weipinhuiQueryItemDetial 调用aoyi-client rpc服务 返回:{}",
                JSONUtil.toJsonString(aoyiItemDetailResDto));

        return aoyiItemDetailResDto;
    }

    /**
     * 唯品会查询库存
     *
     * @param itemId
     * @param skuId
     * @param num
     * @param divisionCode
     * @return
     */
    public AoyiQueryInventoryResDto weipinhuiQueryItemInventory(String itemId, String skuId,
                                                       Integer num, String divisionCode) throws RuntimeException {
        // 返回值
        AoyiQueryInventoryResDto aoyiQueryInventoryResDto = null;

        try {
            log.info("唯品会查询库存 调用aoyiClient rpc服务 入参 itemid:{}, skuId:{}, num:{}, divisionCode:{}",
                    itemId, skuId, num, divisionCode);

            OperaResponse<AoyiQueryInventoryResDto> operaResponse =
                    aoyiClientService.weipinhuiQueryItemInventory(itemId, skuId, num, divisionCode);
            log.info("唯品会查询库存 调用aoyiClient rpc服务 返回:{}", JSONUtil.toJsonString(operaResponse));

            // 处理返回
            if (operaResponse.getCode() == 200) {
                aoyiQueryInventoryResDto = operaResponse.getData();
            } else {
                log.warn("唯品会查询库存 调用aoyi-client rpc服务 错误!");

                throw new Exception("唯品会查询库存 调用aoyi-client rpc服务 错误!!");
            }
        } catch (Exception e) {
            log.error("唯品会查询库存 异常:{}", e.getMessage(), e);

            aoyiQueryInventoryResDto = null; // 这里返回null，是为了兼容调用该方法的已有逻辑!
        }

        log.info("唯品会查询库存 AoyiClientRpcService#queryItemInventory 返回业务数据:{}",
                JSONUtil.toJsonString(aoyiQueryInventoryResDto));

        return aoyiQueryInventoryResDto;
    }
}
