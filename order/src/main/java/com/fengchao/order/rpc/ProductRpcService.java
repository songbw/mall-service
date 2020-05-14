package com.fengchao.order.rpc;

import com.alibaba.fastjson.JSON;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.feign.ProductService;
import com.fengchao.order.rpc.extmodel.Platform;
import com.fengchao.order.rpc.extmodel.ProductInfoBean;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author tom
 * @Date 19-7-19 下午6:28
 */
@Service
@Slf4j
public class ProductRpcService {

    private ProductService productService;

    @Autowired
    public ProductRpcService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 根据mpu集合查询产品信息
     *
     * @param mpuIdList
     * @return
     */
    public List<ProductInfoBean> findProductListByMpuIdList(List<String> mpuIdList) {
        // 返回值
        List<ProductInfoBean> couponBeanList = new ArrayList<>();

        log.debug("根据mpu集合查询产品信息 调用product rpc服务 入参:{}", JSONUtil.toJsonString(mpuIdList));

        if (CollectionUtils.isNotEmpty(mpuIdList)) {
            List<String> subMpuList = new ArrayList<>() ;
            for (int i = 0; i < mpuIdList.size(); i++) {
                if (i + 50 > mpuIdList.size()) {
                    subMpuList = mpuIdList.subList(i, mpuIdList.size());
                } else {
                    subMpuList = mpuIdList.subList(i, i + 50) ;
                }
                i = i + 49 ;
                couponBeanList.addAll(findProductListByMpus(subMpuList)) ;
            }
        }
        log.debug("ProductRpcService#findProductListByMpuIdList 调用product rpc服务 返回:{}", JSONUtil.toJsonString(couponBeanList));

        return couponBeanList;
    }

    /**
     * 根据mpu 获取产品信息
     * @param mpus
     * @return
     */
    public List<ProductInfoBean> findProductListByMpus(List<String> mpus) {
        List<ProductInfoBean> couponBeanList = new ArrayList<>();
        OperaResult operaResult = productService.findProductListByMpuIdList(mpus);
        log.debug("根据mpu集合查询产品信息 调用product rpc服务 返回:{}", JSONUtil.toJsonString(operaResult));
        // 处理返回
        if (operaResult.getCode() == 200) {
            List<ProductInfoBean> _couponBeanList = (List<ProductInfoBean>) operaResult.getData().get("result");

            // 转 ProductInfoBean
            couponBeanList = JSON.parseArray(JSON.toJSONString(_couponBeanList), ProductInfoBean.class);
        } else {
            log.warn("根据mpu集合查询产品信息 调用product rpc服务 错误!");
        }
        return couponBeanList ;
    }


    /**
     * 根据appId 获取平台信息
     * @param appId
     * @return
     */
    public Platform findPlatformByAppId(String appId) {
        Platform platform = new Platform();
        OperaResult operaResult = productService.selectPlatformByAppId(appId);
        log.info("根据appId获取平台信息 调用product rpc服务 返回:{}", JSONUtil.toJsonString(operaResult));
        // 处理返回
        if (operaResult.getCode() == 200) {
            Object object = operaResult.getData();

            // 转 ProductInfoBean
            platform  = JSON.parseObject(JSON.toJSONString(object), Platform.class);
        } else {
            log.warn("根据mpu集合查询产品信息 调用product rpc服务 错误!");
        }
        return platform ;
    }

    /**
     * 根据appIdList 获取平台信息
     * @param appIdList
     * @return
     */
    public List<Platform> findPlatformByAppIdList(List<String> appIdList) {
        List<Platform> platformList = new ArrayList<>();

        log.info("根据appIdList获取平台信息 调用product rpc服务 原始入参:{}", JSONUtil.toJsonString(appIdList));

        if (CollectionUtils.isEmpty(appIdList)) {
            log.info("根据appIdList获取平台信息 rpc 入参为空 返回");
            return platformList;
        }

        // 处理一下appIdList
        Set<String> appIdSet = new HashSet<>(appIdList);
        // 再转list
        List<String> _appIdList = new ArrayList<>(appIdSet);
        log.info("根据appIdList获取平台信息 调用product rpc服务 入参:{}", JSONUtil.toJsonString(_appIdList));
        OperaResponse operaResult = productService.selectPlatformByAppIdList(_appIdList);

        log.info("根据appIdList获取平台信息 调用product rpc服务 返回:{}", JSONUtil.toJsonString(operaResult));
        // 处理返回
        if (operaResult.getCode() == 200) {
            platformList = (List) operaResult.getData();
        } else {
            log.warn("根据appIdList获取平台信息 调用product rpc服务 错误!");
        }

        log.info("根据appIdList获取平台信息 rpc 返回:{}", JSONUtil.toJsonString(platformList));
        return platformList ;
    }
}
