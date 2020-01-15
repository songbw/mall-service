package com.fengchao.aoyi.client.controller;

import com.fengchao.aoyi.client.bean.OperaResult;
import com.fengchao.aoyi.client.bean.dto.AoyiItemDetailResDto;
import com.fengchao.aoyi.client.bean.dto.BrandResDto;
import com.fengchao.aoyi.client.bean.dto.CategoryResDto;
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
     *
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
     *
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
     *
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
     *
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


}
