package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.service.ProductService;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;



@RestController
@RequestMapping(value = "/prod", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping("/all")
    private OperaResult findList(@RequestBody @Valid ProductQueryBean queryBean, OperaResult result) throws ProductException {
        result.getData().put("result", service.findList(queryBean)) ;
        return result;
    }

    @GetMapping
    private OperaResult find(String mpu, OperaResult result){
        if (StringUtils.isEmpty(mpu)) {
            result.setCode(200501);
            result.setMsg("mpu 不能为空");
            return result;
        }
        result.getData().put("result", service.findAndPromotion(mpu)) ;
        return result;
    }

    @PostMapping("/price")
    private OperaResult price(@RequestBody PriceQueryBean queryBean, OperaResult result) throws ProductException {
        return service.findPrice(queryBean);
    }

    /**
     *  库存
     * @param queryBean
     * @param result
     * @return
     */
    @PostMapping("/inventory")
    private OperaResult inventory(@RequestBody InventoryQueryBean queryBean, OperaResult result) throws ProductException {
        return service.findInventory(queryBean);
    }

    /**
     *  运费
     * @param queryBean
     * @param result
     * @return
     */
    @PostMapping("/carriage")
    private OperaResult shipCarriage(@RequestBody CarriageQueryBean queryBean, OperaResult result) throws ProductException {
        result.getData().put("result", service.findCarriage(queryBean)) ;
        return result;
    }

    /**
     * 从ES中查询商品列表
     * @param queryBean
     * @return
     */
    @PostMapping("/es")
    private OperaResponse search(@RequestBody ProductQueryBean queryBean) {
        return service.search(queryBean);
    }

    /**
     * 根据mpuid集合查询product列表
     *
     * @param mpuIdList
     * @param result
     * @return
     * @throws ProductException
     */
    @GetMapping("/findByMpuIdList")
    private OperaResult findByMpuIdList(@RequestParam("mpuIdList") List<String> mpuIdList, OperaResult result) throws ProductException {
        log.info("根据mup集合查询产品信息 入参:{}", JSONUtil.toJsonString(mpuIdList));
        try {
            // 查询
            List<ProductInfoBean> productInfoBeanList = service.queryProductListByMpuIdList(mpuIdList);

            result.getData().put("result", productInfoBeanList);
        } catch (Exception e) {
            log.error("根据mup集合查询产品信息 异常:{}", e.getMessage(), e);

            result.setCode(500);
            result.setMsg("根据mup集合查询产品信息 异常");
        }

        log.info("根据mup集合查询产品信息 返回:{}", JSONUtil.toJsonString(result));

        return result;
    }

    @PostMapping("/priceGAT")
    private OperaResult priceGAT(@RequestBody PriceQueryBean queryBean, OperaResult result) throws ProductException {
        return service.findPriceGAT(queryBean);
    }

    @GetMapping("/getByMpus")
    private OperaResult getProdsByMpus(@RequestParam("mpuIdList") List<String> mpuIdList, OperaResult result) throws ProductException {

        try {
            // 查询
            List<AoyiProdIndex> productInfoBeanList = service.getProdsByMpus(mpuIdList);

            result.getData().put("result", productInfoBeanList);
        } catch (Exception e) {
            log.error("根据mup集合查询产品信息 异常:{}", e.getMessage(), e);
            result.setCode(500);
            result.setMsg("根据mup集合查询产品信息 异常");
        }
        return result;
    }
}
