package com.fengchao.aggregation.rpc;

import com.alibaba.fastjson.JSON;
import com.fengchao.aggregation.bean.OperaResult;
import com.fengchao.aggregation.feign.ProdService;
import com.fengchao.aggregation.model.AoyiProdIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author tom
 * @Date 19-7-19 下午6:28
 */
@Service
@Slf4j
public class ProductRpcService {

    private ProdService productService;

    @Autowired
    public ProductRpcService(ProdService productService) {
        this.productService = productService;
    }

    /**
     * 根据mpu集合查询产品信息
     *
     * @param mpuIdList
     * @return
     */
    public List<AoyiProdIndex> findProductListByMpuIdList(List<String> mpuIdList, String appId, String type) {
        // 返回值
        List<AoyiProdIndex> couponBeanList = new ArrayList<>();

        if (!mpuIdList.isEmpty()) {
            List<String> subMpuList = new ArrayList<>() ;
            for (int i = 0; i < mpuIdList.size(); i++) {
                if (i + 50 > mpuIdList.size()) {
                    subMpuList = mpuIdList.subList(i, mpuIdList.size());
                } else {
                    subMpuList = mpuIdList.subList(i, i + 50) ;
                }
                i = i + 49 ;
                try {
                    couponBeanList.addAll(findProductListByMpus(subMpuList, appId, type).get()) ;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        return couponBeanList;
    }

    /**
     * 根据mpu 获取产品信息
     * @param mpus
     * @param appId
     * @return
     */
    @Async
    private CompletableFuture<List<AoyiProdIndex>> findProductListByMpus(List<String> mpus, String appId, String type) {
        log.debug("findProductListByMpus 开始-----------------------------");
        List<AoyiProdIndex> couponBeanList = new ArrayList<>();

        OperaResult operaResult = new OperaResult() ;
        if ("admin".equals(type)) {
            operaResult = productService.findProductListByMpuIdListAdmin(mpus, appId);
        } else {
            operaResult = productService.findProductListByMpuIdList(mpus, appId);
        }
        // 处理返回
        if (operaResult.getCode() == 200) {
            List<AoyiProdIndex> _couponBeanList = (List<AoyiProdIndex>) operaResult.getData().get("result");

            // 转 ProductInfoBean
            couponBeanList = JSON.parseArray(JSON.toJSONString(_couponBeanList), AoyiProdIndex.class);
        } else {
            log.warn("根据mpu集合查询产品信息 调用product rpc服务 错误!");
        }
        return CompletableFuture.completedFuture(couponBeanList) ;
    }
}
