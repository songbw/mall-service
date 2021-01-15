package com.fengchao.product.aoyi.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.StarInventoryBean;
import com.fengchao.product.aoyi.bean.StarInventoryRequestBean;
import com.fengchao.product.aoyi.bean.supply.SupplyBean;
import com.fengchao.product.aoyi.bean.supply.SupplyInventoryBean;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import com.fengchao.product.aoyi.model.StarSku;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.AoyiItemDetailResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.AoyiQueryInventoryResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.BrandResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.CategoryResDto;
import com.fengchao.product.aoyi.utils.AlarmUtil;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        log.debug("查询唯品会品牌信息 调用aoyi-client rpc服务 入参 pageNumber:{}, pageSize:{}",
                pageNumber, pageSize);

        // 将merchantIdList转成Long型
        OperaResponse<List<BrandResDto>> resultObject = aoyiClientService.weipinhuiGetBrand(pageNumber, pageSize);
        log.debug("查询唯品会品牌信息 调用aoyi-client rpc服务 返回:{}", JSONUtil.toJsonString(resultObject));

        // 处理返回
        if (resultObject.getCode() == 200) {
            brandResDtoList = resultObject.getData();
        } else {
            log.warn("查询唯品会品牌信息 调用aoyi-client rpc服务 错误!");
        }

        log.debug("AoyiClientRpcService#weipinhuiGetBrand 调用aoyi-client rpc服务 返回:{}",
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
        log.debug("查询唯品会类目信息 调用aoyi-client rpc服务 入参 pageNumber:{}, pageSize:{}",
                pageNumber, pageSize);

        // 将merchantIdList转成Long型
        OperaResponse<List<CategoryResDto>> resultObject = aoyiClientService.weipinhuiGetCategory(pageNumber, pageSize);
        log.debug("查询唯品会类目信息 调用aoyi-client rpc服务 返回:{}", JSONUtil.toJsonString(resultObject));

        // 处理返回
        if (resultObject.getCode() == 200) {
            categoryResDtoList = resultObject.getData();
        } else {
            log.warn("查询唯品会类目信息 调用aoyi-client rpc服务 错误!");
        }

        log.debug("AoyiClientRpcService#weipinhuiGetCategory 调用aoyi-client rpc服务 返回:{}",
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
        log.debug("查询唯品会itemId列表信息 调用aoyi-client rpc服务 入参 pageNumber:{}, pageSize:{}",
                pageNumber, pageSize);

        // 将merchantIdList转成Long型
        OperaResponse<List<AoyiItemDetailResDto>> resultObject = aoyiClientService.weipinhuiQueryItemsList(pageNumber, pageSize);
        log.debug("查询唯品会itemId列表信息 调用aoyi-client rpc服务 返回:{}", JSONUtil.toJsonStringWithoutNull(resultObject));

        // 处理返回
        if (resultObject.getCode() == 200) {
            aoyiItemDetailResDtoList = resultObject.getData();
        } else {
            log.warn("查询唯品会itemId列表信息 调用aoyi-client rpc服务 错误!");
        }

        log.debug("AoyiClientRpcService#weipinhuiQueryItemsList 调用aoyi-client rpc服务 返回:{}",
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
        log.debug("查询唯品会商品详情 调用aoyi-client rpc服务 入参:{}", itemId);

        // 将merchantIdList转成Long型
        OperaResponse<AoyiItemDetailResDto> resultObject = aoyiClientService.weipinhuiQueryItemDetial(itemId);
        log.debug("查询唯品会商品详情 调用aoyi-client rpc服务 返回:{}", JSONUtil.toJsonString(resultObject));

        // 处理返回
        if (resultObject.getCode() == 200) {
            aoyiItemDetailResDto = resultObject.getData();
        } else {
            log.warn("查询唯品会商品详情 调用aoyi-client rpc服务 错误!");
            AlarmUtil.alarmAsync("查询唯品会商品详情 "+itemId+" 调用aoyi-client rpc服务 错误!", "出错了");
//            throw new Exception("查询唯品会商品详情 调用aoyi-client rpc服务 错误!!");
        }

        log.debug("AoyiClientRpcService#weipinhuiQueryItemDetial 调用aoyi-client rpc服务 返回:{}",
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
            log.debug("唯品会查询库存 调用aoyiClient rpc服务 入参 itemid:{}, skuId:{}, num:{}, divisionCode:{}",
                    itemId, skuId, num, divisionCode);

            OperaResponse<AoyiQueryInventoryResDto> operaResponse =
                    aoyiClientService.weipinhuiQueryItemInventory(itemId, skuId, num, divisionCode);
            log.debug("唯品会查询库存 调用aoyiClient rpc服务 返回:{}", JSONUtil.toJsonString(operaResponse));

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

        log.debug("唯品会查询库存 AoyiClientRpcService#queryItemInventory 返回业务数据:{}",
                JSONUtil.toJsonString(aoyiQueryInventoryResDto));

        return aoyiQueryInventoryResDto;
    }

    /**
     * 查询star库存
     * @param inventoryBean
     * @return
     */
    public List<StarInventoryBean> getStarInventory(SupplyInventoryBean inventoryBean) {
        List<String> skuIds = inventoryBean.getList().stream().filter(supplyBean -> supplyBean.getSpuId() != supplyBean.getSkuId()).map(supplyBean -> supplyBean.getSkuId()).collect(Collectors.toList());
        if (skuIds == null || skuIds.size() == 0) {
            return null ;
        }
        Map<String, String> map = inventoryBean.getList().stream().collect(Collectors.toMap(SupplyBean::getSkuId, SupplyBean::getSpuId)) ;
        List<StarInventoryBean> inventoryBeans = new ArrayList<>();
        StarInventoryRequestBean requestBean = new StarInventoryRequestBean() ;
        requestBean.setAreaId(inventoryBean.getAreaId());
        String code = "";
        for (String skuId : skuIds) {
            if (StringUtils.isEmpty(code)) {
                code = skuId;
            } else {
                code = code + "," + skuId;
            }
        }
        requestBean.setCodes(code);
        OperaResponse response = aoyiClientService.findSkuInverntory(requestBean) ;
        if (response.getCode() == 200) {
            String skuInventoryResString = JSON.toJSONString(response);
            JSONObject skuDetailResJson = JSONObject.parseObject(skuInventoryResString);
            JSONArray skuInventoryData = skuDetailResJson.getJSONObject("data").getJSONArray("skuInvList");
            for (int i = 0; i < skuInventoryData.size(); i++) {
                JSONObject jsonObject = skuInventoryData.getJSONObject(i) ;
                StarInventoryBean starInventoryBean = new StarInventoryBean();
                starInventoryBean.setCode(jsonObject.getString("code"));
                String spu = map.get(starInventoryBean.getCode()) ;
                starInventoryBean.setSkuId(spu);
                starInventoryBean.setInventoryCount(jsonObject.getString("inventoryCount"));
                if ("0".equals(jsonObject.getString("status"))) {
                    starInventoryBean.setStatus("1");
                }
                if ("1".equals(jsonObject.getString("status"))) {
                    starInventoryBean.setStatus("0");
                }
                inventoryBeans.add(starInventoryBean) ;
            }
        }
        return inventoryBeans ;
    }
}
