package com.fengchao.aoyi.client.controller;

import com.fengchao.aoyi.client.bean.OperaResult;
import com.fengchao.aoyi.client.bean.dto.*;
import com.fengchao.aoyi.client.utils.JSONUtil;
import com.fengchao.aoyi.client.weipinhuiService.ProductWeipinhuiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/weipinhui", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WeipinhuiController {

    private ProductWeipinhuiService productWeipinhuiService;

    @Autowired
    public WeipinhuiController(ProductWeipinhuiService productWeipinhuiService) {
        this.productWeipinhuiService = productWeipinhuiService;
    }

    /**
     * 获取品牌列表
     * <p>
     * http://localhost:8001/weipinhui/getBrand?pageNumber=1&pageSize=20
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GetMapping("/getBrand")
    private OperaResult<List<BrandResDto>> getBrand(@RequestParam("pageNumber") Integer pageNumber,
                                                    @RequestParam("pageSize") Integer pageSize) {
        log.info("获取品牌列表 入参 pageNumber:{}, pageSize:{}", pageNumber, pageSize);

        OperaResult<List<BrandResDto>> operaResult = new OperaResult<>();
        try {
            List<BrandResDto> brandResDtoList = productWeipinhuiService.getBrand(pageNumber, pageSize);

            operaResult.setData(brandResDtoList);
            operaResult.setCode(200);
        } catch (Exception e) {
            log.error("获取品牌列表 异常:{}", e.getMessage(), e);

            operaResult.setData(null);
            operaResult.setMsg("失败:" + e.getMessage());
            operaResult.setCode(500);
        }

        log.info("获取品牌列表 返回:{}", JSONUtil.toJsonString(operaResult));

        return operaResult;
    }

    /**
     * 获取类目列表
     * <p>
     * http://localhost:8001/weipinhui/getCategory?pageNumber=1&pageSize=20
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GetMapping("/getCategory")
    private OperaResult<List<CategoryResDto>> getCategory(@RequestParam("pageNumber") Integer pageNumber,
                                                          @RequestParam("pageSize") Integer pageSize) {
        log.info("获取类目列表 入参 pageNumber:{}, pageSize:{}", pageNumber, pageSize);

        OperaResult<List<CategoryResDto>> operaResult = new OperaResult<>();
        try {
            List<CategoryResDto> categoryResDtoList = productWeipinhuiService.getCategory(pageNumber, pageSize);

            operaResult.setData(categoryResDtoList);
            operaResult.setCode(200);
        } catch (Exception e) {
            log.error("获取类目列表 异常:{}", e.getMessage(), e);

            operaResult.setData(null);
            operaResult.setMsg("失败:" + e.getMessage());
            operaResult.setCode(500);
        }

        log.info("获取类目列表 返回:{}", JSONUtil.toJsonString(operaResult));

        return operaResult;
    }

    /**
     * 获取items列表
     * <p>
     * http://localhost:8001/weipinhui/queryItemsList?pageNumber=1&pageSize=20
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GetMapping("/queryItemsList")
    private OperaResult<List<AoyiItemDetailResDto>> queryItemsList(@RequestParam("pageNumber") Integer pageNumber,
                                                                   @RequestParam("pageSize") Integer pageSize) {
        log.info("获取items列表 入参 pageNumber:{}, pageSize:{}", pageNumber, pageSize);

        OperaResult<List<AoyiItemDetailResDto>> operaResult = new OperaResult<>();
        try {
            List<AoyiItemDetailResDto> aoyiItemResDtoList = productWeipinhuiService.queryItemsList(pageNumber, pageSize);

            operaResult.setData(aoyiItemResDtoList);
            operaResult.setCode(200);
        } catch (Exception e) {
            log.error("获取items列表 异常:{}", e.getMessage(), e);

            operaResult.setData(null);
            operaResult.setMsg("失败:" + e.getMessage());
            operaResult.setCode(500);
        }

        log.info("获取items列表 返回:{}", JSONUtil.toJsonString(operaResult));

        return operaResult;
    }

    /**
     * 根据itemId查询详情
     * <p>
     * http://localhost:8001/weipinhui/queryItemDetial?itemId=50000550
     *
     * @param itemId
     * @return
     */
    @GetMapping("/queryItemDetial")
    private OperaResult<AoyiItemDetailResDto> queryItemDetial(@RequestParam("itemId") String itemId) {
        log.info("根据itemId查询详情 入参 itemId:{}", itemId);

        OperaResult<AoyiItemDetailResDto> operaResult = new OperaResult<>();
        try {
            AoyiItemDetailResDto aoyiItemDetailResDto = productWeipinhuiService.queryItemDetial(itemId);

            operaResult.setData(aoyiItemDetailResDto);
            operaResult.setCode(200);
        } catch (Exception e) {
            log.error("根据itemId查询详情 异常:{}", e.getMessage(), e);

            operaResult.setData(null);
            operaResult.setMsg("失败:" + e.getMessage());
            operaResult.setCode(500);
        }

        log.info("根据itemId查询详情 返回:{}", JSONUtil.toJsonString(operaResult));

        return operaResult;
    }

    /**
     * 库存查询接口
     * <p>
     * http://localhost:8001/weipinhui/queryItemInventory?itemId=50000550&skuId=50000550&num=1&divisionCode=2429
     *
     * @param itemId
     * @param skuId
     * @param num          数量
     * @param divisionCode 地址code
     * @return
     */
    @GetMapping("/queryItemInventory")
    private OperaResult<AoyiQueryInventoryResDto> queryItemDetial(@RequestParam("itemId") String itemId,
                                                                  @RequestParam("skuId") String skuId,
                                                                  @RequestParam("num") Integer num,
                                                                  @RequestParam("divisionCode") String divisionCode) {

        log.info("库存查询接口 入参 itemId:{} skuId:{} num:{} divisionCode:{}",
                itemId, skuId, num, divisionCode);

        OperaResult<AoyiQueryInventoryResDto> operaResult = new OperaResult<>();
        try {
            AoyiQueryInventoryResDto aoyiQueryInventoryResDto =
                    productWeipinhuiService.queryItemInventory(itemId, skuId, num, divisionCode);

            operaResult.setData(aoyiQueryInventoryResDto);
            operaResult.setCode(200);
        } catch (Exception e) {
            log.error("库存查询接口 异常:{}", e.getMessage(), e);

            operaResult.setData(null);
            operaResult.setMsg("失败:" + e.getMessage());
            operaResult.setCode(500);
        }

        log.info("库存查询接口 返回:{}", JSONUtil.toJsonString(operaResult));

        return operaResult;
    }

    /**
     * 预占订单接口
     *
     * @param aoyiRenderOrderRequest
     * @return
     */
    @PostMapping("/queryItemInventory")
    private OperaResult queryItemDetial(@RequestBody AoyiRenderOrderRequest aoyiRenderOrderRequest) {

        log.info("预占订单接口 入参:{}", JSONUtil.toJsonString(aoyiRenderOrderRequest));

        OperaResult operaResult = new OperaResult<>();
        try {
            productWeipinhuiService.renderOrder(aoyiRenderOrderRequest);

            operaResult.setCode(200);
        } catch (Exception e) {
            log.error("预占订单接口 异常:{}", e.getMessage(), e);

            operaResult.setData(null);
            operaResult.setMsg("失败:" + e.getMessage());
            operaResult.setCode(500);
        }

        log.info("预占订单接口 返回:{}", JSONUtil.toJsonString(operaResult));

        return operaResult;
    }


}
