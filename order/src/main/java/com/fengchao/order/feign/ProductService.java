package com.fengchao.order.feign;

import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.feign.hystric.ProductServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "product-aoyi", url = "${rpc.feign.client.product.url:}", fallback = ProductServiceH.class)
public interface ProductService {

    @RequestMapping(value = "/prod", method = RequestMethod.GET)
    OperaResult find(@RequestParam("mpu") String mpu);

    /**
     * 根据mpu集合查询产品信息
     *
     * @param mpuIdList
     * @return
     */
    @RequestMapping(value = "/prod/findByMpuIdList", method = RequestMethod.GET)
    OperaResult findProductListByMpuIdList(@RequestParam("mpuIdList") List<String> mpuIdList);

}
