package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.config.MerchantCodeBean;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
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
    private OperaResult findList(@RequestBody @Valid ProductQueryBean queryBean,@RequestHeader("appId") String appId, OperaResult result) throws ProductException {
        queryBean.setAppId(appId);
        result.getData().put("result", service.findList(queryBean)) ;
        return result;
    }

    @PostMapping("/all/v2")
    private OperaResult findListV2(@RequestBody @Valid ProductQueryBean queryBean,@RequestHeader("appId") String appId, @RequestHeader("renterId") String renterId) throws ProductException {
        OperaResult result = new OperaResult() ;
        queryBean.setAppId(appId);
        queryBean.setRenterId(renterId);
        result.getData().put("result", service.findList(queryBean)) ;
        return result;
    }

    @PostMapping("/all/categories")
    private OperaResult findListByCategories(@RequestBody ProductQueryBean queryBean, @RequestHeader("appId") String appId, OperaResult result) throws ProductException {
        queryBean.setAppId(appId);
        result.getData().put("result", service.findListByCategories(queryBean)) ;
        return result;
    }

    @PostMapping("/all/categories")
    private OperaResult findListByCategoriesV2(@RequestBody ProductQueryBean queryBean, @RequestHeader("appId") String appId, @RequestHeader("renterId") String renterId) throws ProductException {
        OperaResult result = new OperaResult() ;
        queryBean.setAppId(appId);
        queryBean.setRenterId(renterId);
        result.getData().put("result", service.findListByCategories(queryBean)) ;
        return result;
    }

    @GetMapping
    private OperaResult find(String mpu, @RequestHeader("appId") String appId, OperaResult result){
        if (StringUtils.isEmpty(mpu)) {
            result.setCode(200501);
            result.setMsg("mpu 不能为空");
            return result;
        }
        result.getData().put("result", service.findAndPromotion(mpu, appId)) ;
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
        log.info("查询库存 入参:{}", JSONUtil.toJsonString(queryBean));
        OperaResult operaResult = service.findInventory(queryBean);
        log.info("查询库存 返回:{}", JSONUtil.toJsonString(queryBean));

        return operaResult;
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
        log.debug("根据mup集合查询产品信息 入参:{}", JSONUtil.toJsonString(mpuIdList));
        if (mpuIdList.size() > 60) {
            result.setCode(200001);
            result.setMsg("mpu 列表数量超过50！");
            return result ;
        }
        try {
            // 查询
            List<ProductInfoBean> productInfoBeanList = service.queryProductListByMpuIdList(mpuIdList);

            result.getData().put("result", productInfoBeanList);
        } catch (Exception e) {
            log.error("根据mup集合查询产品信息 异常:{}", e.getMessage(), e);

            result.setCode(500);
            result.setMsg("根据mup集合查询产品信息 异常");
        }

        log.debug("根据mup集合查询产品信息 返回:{}", JSONUtil.toJsonString(result));

        return result;
    }

    /**
     * 根据mpuid集合查询product列表
     *
     * @param mpuIdList
     * @param result
     * @return
     * @throws ProductException
     */
    @GetMapping("/mpuIds")
    private OperaResult selectByMpuIdList(@RequestParam("mpuIdList") List<String> mpuIdList, OperaResult result) throws ProductException {
        log.debug("根据mup集合查询产品信息 入参:{}", JSONUtil.toJsonString(mpuIdList));
        if (mpuIdList.size() > 60) {
            result.setCode(200001);
            result.setMsg("mpu 列表数量超过50！");
            return result ;
        }
        try {
            // 查询
            List<AoyiProdIndexX> productInfoBeanList = service.selectProductListByMpuIdList(mpuIdList);

            result.getData().put("result", productInfoBeanList);
        } catch (Exception e) {
            log.error("根据mup集合查询产品信息 异常:{}", e.getMessage(), e);

            result.setCode(500);
            result.setMsg("根据mup集合查询产品信息 异常");
        }

        log.debug("根据mup集合查询产品信息 返回:{}", JSONUtil.toJsonString(result));

        return result;
    }

    @PostMapping("/priceGAT")
    private OperaResult priceGAT(@RequestBody PriceQueryBean queryBean, OperaResult result) throws ProductException {
        return service.findPriceGAT(queryBean);
    }

    @GetMapping("/getByMpus")
    private OperaResult getProdsByMpus(@RequestParam("mpuIdList") List<String> mpuIdList, OperaResult result) throws ProductException {
        log.debug("根据mup集合查询产品信息 入参:{}", JSONUtil.toJsonString(mpuIdList));
        if (mpuIdList.size() > 60) {
            result.setCode(200001);
            result.setMsg("mpu 列表数量超过50！");
            return result ;
        }
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

    /**
     *  自营库存
     * @param queryBean
     * @return
     */
    @PostMapping("/inventory/self")
    private OperaResult inventorySelf(@RequestBody InventorySelfQueryBean queryBean) {
        return service.findInventorySelf(queryBean);
    }

    /**
     * 批量减库存
     * @param inventories
     * @return
     */
    @PutMapping("/inventory/sub")
    private OperaResult inventorySub(@RequestBody List<InventoryMpus>  inventories) {
        return service.inventorySub(inventories);
    }

    /**
     * 批量增加库存
     * @param inventories
     * @return
     */
    @PutMapping("/inventory/add")
    private OperaResult inventoryAdd(@RequestBody List<InventoryMpus>  inventories) {
        return service.inventoryAdd(inventories);
    }

    /**
     * 根据mpuid,code集合查询product列表
     *
     * @param mpuIdList
     * @return
     * @throws ProductException
     */
    @PostMapping("/sku/mpuIds")
    private OperaResponse selectByMpuIdListAndSkuCodes(@RequestBody List<AoyiProdIndex> mpuIdList) {
        OperaResponse response = new OperaResponse() ;
        if (mpuIdList.size() > 100) {
            response.setCode(200001);
            response.setMsg("mpu 列表数量超过50！");
            return response ;
        }
        try {
            // 查询
            List<AoyiProdIndexX> productInfoBeanList = service.selectProductListByMpuIdListAndCode(mpuIdList);

            response.setData(productInfoBeanList);
        } catch (Exception e) {
            log.error("根据mup集合查询产品信息 异常:{}", e.getMessage(), e);

            response.setCode(500);
            response.setMsg("根据mup集合查询产品信息 异常");
        }

        return response;
    }

    @GetMapping("spu")
    private OperaResponse findSpuAndSkuByMpuAndCode(String mpu, String code){
        OperaResponse result = new OperaResponse() ;
        if (StringUtils.isEmpty(mpu)) {
            result.setCode(200501);
            result.setMsg("mpu 不能为空");
            return result;
        }
        return service.findSpuAndSku(mpu, code);
    }




}
