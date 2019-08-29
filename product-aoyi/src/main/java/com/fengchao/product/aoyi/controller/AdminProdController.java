package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.bean.QueryProdBean;
import com.fengchao.product.aoyi.bean.SerachBean;
import com.fengchao.product.aoyi.bean.vo.ProductExportResVo;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.service.AdminProdService;
import com.fengchao.product.aoyi.utils.DateUtil;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/adminProd", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class AdminProdController {

    @Autowired
    private AdminProdService prodService;

    @GetMapping("prodList")
    public OperaResult findProdList(Integer offset, Integer limit, String state, @RequestHeader("merchant") Integer merchantId, OperaResult result) {
        if (offset < 0) {
            offset = 1;
        }
        if (limit > 100) {
            result.setCode(200500);
            result.setMsg("limit 不能大于100");
            return result;
        }
        PageBean prod = prodService.findProdList(offset, limit, state, merchantId);
        result.getData().put("result", prod);
        return result;
    }

    /**
     * 搜索商品
     *
     * @param bean
     * @param merchantHeader
     * @param result
     * @return
     */
    @PostMapping("search")
    public OperaResult searchProd(@Valid @RequestBody SerachBean bean, @RequestHeader("merchant") Integer merchantHeader,
                                  OperaResult result) {
        log.info("搜索商品 入参 bean:{}, merchantId:{}", JSONUtil.toJsonString(bean), merchantHeader);

        bean.setMerchantHeader(merchantHeader);
        PageBean pageBean = prodService.selectNameList(bean);
//        PageBean pageBean = prodService.selectProductListPageable(bean);
        result.getData().put("result", pageBean);

        log.info("搜索商品 返回:{}", JSONUtil.toJsonString(result));

        return result;
    }

    /**
     * 创建商品
     *
     * @param bean
     * @param merchantId
     * @param result
     * @return
     * @throws ProductException
     */
    @PostMapping
    public OperaResult create(@RequestBody AoyiProdIndexX bean, @RequestHeader("merchant") Integer merchantId,
                              OperaResult result) throws ProductException {
        log.info("创建商品 入参 AoyiProdIndexX:{}, merchantId:{}", JSONUtil.toJsonString(bean), merchantId);

        try {
            // 入参校验
            if (bean.getMerchantId() <= 0) {
                throw new Exception("参数merchantId不合法");
            }

            if (StringUtils.isBlank(bean.getPrice()) || Float.valueOf(bean.getPrice()) <= 0) {
                throw new Exception("参数price不合法");
            }

            // 执行新增商品
            int id = prodService.add(bean);

            result.getData().put("result", id);
        } catch (Exception e) {
            log.error("创建商品 异常:{}", e.getMessage(), e);

            result.setCode(500);
            result.setMsg(e.getMessage());
        }

        log.info("创建商品 返回:{}", JSONUtil.toJsonString(result));

        return result;
    }

    @PutMapping
    public OperaResult update(@RequestBody AoyiProdIndexX bean, @RequestHeader("merchant") Integer merchantId, OperaResult result) throws ProductException {
//        bean.setMerchantId(merchantId);
        int id = prodService.update(bean);
        result.getData().put("result", id);
        return result;
    }

    @DeleteMapping
    public OperaResult delete(@RequestHeader("merchant") Integer merchantId, Integer id, OperaResult result) throws ProductException {
        prodService.delete(merchantId, id);
        return result;
    }


    @GetMapping("prodToRedis")
    public OperaResult getProdListToRedis(OperaResult result) {
        int prodList = prodService.getProdListToRedis();
        result.getData().put("result", prodList);
        return result;
    }

    @PostMapping("prodAll")
    public OperaResult findProdAll(@Valid @RequestBody QueryProdBean bean, OperaResult result) {
        PageBean pageBean = prodService.findProdAll(bean);
        result.getData().put("result", pageBean);
        return result;
    }

    /**
     * 按照条件导出订单
     * 参考：https://blog.csdn.net/ethan_10/article/details/80335350
     *
     * <p>
     * 导出title：
     * 商品供应商
     * 上海奥弋
     * 商品SKU
     * 商品状态
     * 商品名称
     * 商品品牌
     * 商品类别
     * 商品型号
     * 商品重量
     * 商品条形码
     * 销售价格(元)
     * 进货价格(元) sprice
     *
     * 测试: http://localhost:8002/adminProd/export
     *      http://localhost:8002/adminProd/export?query=海尔
     *
     * @param serachBean 导出条件
     * @param response
     */
    @GetMapping(value = "/export")
    public void exportOrder(SerachBean serachBean,
                            @RequestHeader(name = "merchant") Integer merchantHeader, // FIXME :
                            HttpServletResponse response) throws Exception {
        OutputStream outputStream = null;
        // 创建HSSFWorkbook对象
        HSSFWorkbook workbook = null;

        log.info("导出商品列表 入参:{}", JSONUtil.toJsonString(serachBean));
        try {
            // 1.根据条件获取订单集合
            serachBean.setMerchantHeader(merchantHeader);
            List<ProductExportResVo> productExportResVoList = prodService.exportProductList(serachBean);

            log.info("导出商品列表 获取导出记录{}条", productExportResVoList.size());
            if (CollectionUtils.isEmpty(productExportResVoList)) {
                throw new Exception("未找出有效的导出数据!");
            }

            // 2.开始组装excel
            // 2.0 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();
            // 创建HSSFSheet对象
            HSSFSheet sheet = workbook.createSheet("商品列表");

            // 2.1 组装title
            createTitle(sheet);

            // 2.2 组装业务数据
            createContent(sheet, productExportResVoList);


            // 2.3 文件名
            String date = DateUtil.nowDate(DateUtil.DATE_YYYYMMDD);
            String fileName = "exportproduct_" + date + ".xls";

            // 3. 输出文件
            try {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

                outputStream = response.getOutputStream();
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e) {
                log.error("导出商品列表文件 出错了:{}", e.getMessage(), e);

                throw new Exception(e);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            //
            log.info("export product file finish");
        } catch (Exception e) {
            log.error("导出文件异常:{}", e.getMessage(), e);

            response.setStatus(401);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
                Map<String, String> map = new HashMap<>();
                map.put("code", "401");
                map.put("msg", e.getMessage());
                map.put("data", null);

                writer.write(JSONUtil.toJsonString(map));
            } catch (Exception e1) {
                log.error("导出商品列表文件 错误:{}", e.getMessage(), e);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }

            if (workbook != null) {
                workbook.close();
            }
        }
    }

    // ================================================= private ===========================================

    /**
     * 创建title
     *
     * @param sheet
     */
    private void createTitle(HSSFSheet sheet) {
        HSSFRow titleRow = sheet.createRow(0);

        HSSFCell titleCell0 = titleRow.createCell(0);
        titleCell0.setCellValue("商品供应商");

        HSSFCell titleCell1 = titleRow.createCell(1);
        titleCell1.setCellValue("商品SKU");

        HSSFCell titleCell2 = titleRow.createCell(2);
        titleCell2.setCellValue("商品状态");

        HSSFCell titleCell3 = titleRow.createCell(3);
        titleCell3.setCellValue("商品名称");

        HSSFCell titleCell4 = titleRow.createCell(4);
        titleCell4.setCellValue("商品品牌");

        HSSFCell titleCell5 = titleRow.createCell(5);
        titleCell5.setCellValue("商品类别");

        HSSFCell titleCell6 = titleRow.createCell(6);
        titleCell6.setCellValue("商品型号");

        HSSFCell titleCell7 = titleRow.createCell(7);
        titleCell7.setCellValue("商品重量");

        HSSFCell titleCell8 = titleRow.createCell(8);
        titleCell8.setCellValue("商品条形码");

        HSSFCell titleCell9 = titleRow.createCell(9);
        titleCell9.setCellValue("销售价格(元)");

        HSSFCell titleCell10 = titleRow.createCell(10);
        titleCell10.setCellValue("进货价格(元)");
    }

    /**
     * 组装业务数据
     *
     * @param sheet
     * @param productExportResVoList 需要导出的数据集合
     */
    private void createContent(HSSFSheet sheet, List<ProductExportResVo> productExportResVoList) {

        int currentRowNum = 1; // 行号
        int totalRowNum = productExportResVoList.size(); // 总行数
        for (ProductExportResVo productExportResVo : productExportResVoList) {
            // 新增一行
            HSSFRow hssfRow = sheet.createRow(currentRowNum);

            // 商品供应商
            HSSFCell titleCell0 = hssfRow.createCell(0);
            titleCell0.setCellValue(productExportResVo.getMerchantName());

            // 商品SKU
            HSSFCell titleCell1 = hssfRow.createCell(1);
            titleCell1.setCellValue(productExportResVo.getSku());

            // 商品状态
            HSSFCell titleCell2 = hssfRow.createCell(2);
            titleCell2.setCellValue(productExportResVo.getState());

            // 商品名称
            HSSFCell titleCell3 = hssfRow.createCell(3);
            titleCell3.setCellValue(productExportResVo.getProductName());

            // 商品品牌
            HSSFCell titleCell4 = hssfRow.createCell(4);
            titleCell4.setCellValue(productExportResVo.getBrand());

            // 商品类别
            HSSFCell titleCell5 = hssfRow.createCell(5);
            titleCell5.setCellValue(productExportResVo.getCategory());

            // 商品型号
            HSSFCell titleCell6 = hssfRow.createCell(6);
            titleCell6.setCellValue(productExportResVo.getModel());

            // 商品重量
            HSSFCell titleCell7 = hssfRow.createCell(7);
            titleCell7.setCellValue(productExportResVo.getWeight());

            // 商品条形码
            HSSFCell titleCell8 = hssfRow.createCell(8);
            titleCell8.setCellValue(productExportResVo.getUpc());

            // 销售价格(元)
            HSSFCell titleCell9 = hssfRow.createCell(9);
            titleCell9.setCellValue(productExportResVo.getSellPrice());

            // 进货价格(元)
            HSSFCell titleCell10 = hssfRow.createCell(10);
            titleCell10.setCellValue(productExportResVo.getCostPrice());

            //
            if (currentRowNum % 100 == 0) {
                log.info("导出商品列表 第{}行, 共{}行", currentRowNum, totalRowNum);
            }
            currentRowNum++;
            productExportResVo = null; // 释放
        }
    }

}
