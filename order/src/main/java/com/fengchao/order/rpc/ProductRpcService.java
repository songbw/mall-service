package com.fengchao.order.rpc;

import com.alibaba.fastjson.JSON;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.feign.ProductService;
import com.fengchao.order.rpc.extmodel.ProductInfoBean;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

        log.info("根据mpu集合查询产品信息 调用product rpc服务 入参:{}", JSONUtil.toJsonString(mpuIdList));

        if (CollectionUtils.isNotEmpty(mpuIdList)) {
            OperaResult operaResult = productService.findProductListByMpuIdList(mpuIdList);
            log.info("根据mpu集合查询产品信息 调用product rpc服务 返回:{}", JSONUtil.toJsonString(operaResult));

            // 处理返回
            if (operaResult.getCode() == 200) {
                List<ProductInfoBean> _couponBeanList = (List<ProductInfoBean>) operaResult.getData().get("result");

                // 转 ProductInfoBean
                couponBeanList = JSON.parseArray(JSON.toJSONString(_couponBeanList), ProductInfoBean.class);
            } else {
                log.warn("根据mpu集合查询产品信息 调用product rpc服务 错误!");
            }
        }

        log.info("ProductRpcService#findProductListByMpuIdList 调用product rpc服务 返回:{}", JSONUtil.toJsonString(couponBeanList));

        return couponBeanList;
    }
}
