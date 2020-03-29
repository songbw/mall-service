package com.fengchao.order.controller;

import com.fengchao.order.bean.vo.*;
import com.fengchao.order.constants.PaymentTypeEnum;
import com.fengchao.order.rpc.extmodel.OrderPayMethodInfoBean;
import com.fengchao.order.service.AdminInvoiceService;
import com.fengchao.order.service.AdminOrderService;
import com.fengchao.order.service.ExportStatisticService;
import com.fengchao.order.utils.CalculateUtil;
import com.fengchao.order.utils.DateUtil;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping(value = "/export/statistic")
@Slf4j
public class ExportStatisticController {

    private ExportStatisticService exportStatisticService;

    @Autowired
    public ExportStatisticController(ExportStatisticService exportStatisticService) {
        this.exportStatisticService = exportStatisticService;
    }

    /**
     * 导出商户货款结算表
     *
     * @param startTime
     * @param endTime
     * @param merchantId
     * @param appIds
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/settlment")
    public void exportSettlement(@RequestParam("startTime") String startTime,
                            @RequestParam("endTime") String endTime,
                            @RequestParam("merchantId") Integer merchantId,
                            @RequestParam(value = "appIds", required = false) String appIds,
                            HttpServletResponse response) throws Exception {
        OutputStream outputStream = null;
        // 创建HSSFWorkbook对象
        HSSFWorkbook workbook = null;

        try {
            log.info("导出商户货款结算表 入参 startTime:{}, endTime:{}, merchantId:{}, appIds:{}",
                    startTime, endTime, merchantId, appIds);
            // 0.入参检验
//            if (orderExportReqVo.getPayStartDate() == null) {
//                throw new Exception("参数不合法, 查询开始时间为空");
//            }

            List<String> appList = Arrays.asList(appIds.split(","));

            // 1.根据条件获取订单集合
            Date startDate = DateUtil.parseDateTime(startTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            Date endDate = DateUtil.parseDateTime(endTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            ExportLoanSettlementVo exportLoanSettlementVo =
                    exportStatisticService.exportSettlement(startDate, endDate, appList, merchantId);

            if (exportLoanSettlementVo == null) {
                log.info("导出商户货款结算表 未找出有效的导出数据!");
                throw new Exception("未找出有效的导出数据!");
            }

            // 2.开始组装excel
            // 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();
            // 创建HSSFSheet对象
            HSSFSheet sheet = workbook.createSheet("货款结算表");

            // 2.1 组装
            HSSFRow row0 = sheet.createRow(0);
            HSSFCell cell00 = row0.createCell(0);
            cell00.setCellValue("货款结算单"); //
            HSSFCell cell01 = row0.createCell(1);
            cell01.setCellValue("");

            HSSFRow row1 = sheet.createRow(1);
            HSSFCell cell10 = row1.createCell(0);
            cell10.setCellValue("结算周期"); //
            HSSFCell cell11 = row1.createCell(1);
            cell11.setCellValue(exportLoanSettlementVo.getSettlementPeriod());

            HSSFRow row2 = sheet.createRow(2);
            HSSFCell cell20 = row2.createCell(0);
            cell20.setCellValue("本期已完成订单金额（元）"); //
            HSSFCell cell21 = row2.createCell(1);
            cell21.setCellValue(exportLoanSettlementVo.getCompleteOrderAmount());

            HSSFRow row3 = sheet.createRow(3);
            HSSFCell cell30 = row3.createCell(0);
            cell30.setCellValue("本期已退款订单金额（元）"); //
            HSSFCell cell31 = row3.createCell(1);
            cell31.setCellValue(exportLoanSettlementVo.getRefundOrderAmount());

            HSSFRow row4 = sheet.createRow(4);
            HSSFCell cell40 = row4.createCell(0);
            cell40.setCellValue("本期实际成交订单金额（元）"); //
            HSSFCell cell41 = row4.createCell(1);
            cell41.setCellValue(exportLoanSettlementVo.getRealyOrderAmount());

            HSSFRow row5 = sheet.createRow(5);
            HSSFCell cell50 = row5.createCell(0);
            cell50.setCellValue("供应商优惠券金额（元）"); //
            HSSFCell cell51 = row5.createCell(1);
            cell51.setCellValue(exportLoanSettlementVo.getCouponAmount());

            HSSFRow row6 = sheet.createRow(6);
            HSSFCell cell60 = row6.createCell(0);
            cell60.setCellValue("本期运费金额（元）"); //
            HSSFCell cell61 = row6.createCell(1);
            cell61.setCellValue(exportLoanSettlementVo.getCouponAmount());

            HSSFRow row7 = sheet.createRow(7);
            HSSFCell cell70 = row7.createCell(0);
            cell70.setCellValue("当期应付款（元）"); //
            HSSFCell cell71 = row7.createCell(1);
            cell71.setCellValue(exportLoanSettlementVo.getPayAmout());

            ///////// 文件名
            String date = DateUtil.nowDate(DateUtil.DATE_YYYYMMDD);
            String fileName = "settlement" + date + ".xls";

            // 3. 输出文件
            try {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

                outputStream = response.getOutputStream();
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e) {
                log.error("导出商户货款结算表 出错了:{}", e.getMessage(), e);

                throw new Exception(e);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            //
            log.debug("export file finish");
        } catch (Exception e) {
            log.error("导出商户货款结算表异常:{}", e.getMessage(), e);

//            response.setHeader("content-type", "application/json;charset=UTF-8");
//            response.setContentType("application/json");
            // response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setStatus(400);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
                Map<String, String> map = new HashMap<>();
                map.put("code", "400");
                map.put("msg", e.getMessage());
                map.put("data", null);

                writer.write(JSONUtil.toJsonString(map));
            } catch (Exception e1) {
                log.error("导出商户货款结算表 错误:{}", e.getMessage(), e);
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


    /**
     * 导出运费实际收款报表
     *
     * @param startTime
     * @param endTime
     * @param appIds
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/expressfee")
    public void exportExpressFee(@RequestParam("startTime") String startTime,
                                 @RequestParam("endTime") String endTime,
                                 @RequestParam(value = "appId", required = false) String appIds,
                                 HttpServletResponse response) throws Exception {
        OutputStream outputStream = null;
        // 创建HSSFWorkbook对象
        HSSFWorkbook workbook = null;

        try {
            log.info("导出运费实际收款报表 入参 startTime:{}, endTime:{}, appIds:{}", startTime, endTime, appIds);
            // 0.入参检验
//            if (orderExportReqVo.getPayStartDate() == null) {
//                throw new Exception("参数不合法, 查询开始时间为空");
//            }

            List<String> appList = Arrays.asList(appIds.split(","));

            // 1. 获取导出数据
            Date startDate = DateUtil.parseDateTime(startTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            Date endDate = DateUtil.parseDateTime(endTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            List<ExportExpressFeeVo> exportExpressFeeVoList =
                    exportStatisticService.exportExpressFee(startDate, endDate, appList);

            if (CollectionUtils.isEmpty(exportExpressFeeVoList)) {
                log.warn("导出运费实际收款报表 未找出有效的导出数据!");
                throw new Exception("未找出有效的导出数据!");
            }

            // 2.开始组装excel
            // 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();
            // 创建HSSFSheet对象
            HSSFSheet sheet = workbook.createSheet("运费实际收款报表");

            // 2.1 组装
            // title
            HSSFRow titleRow0 = sheet.createRow(0);
            HSSFCell cell00 = titleRow0.createCell(0);
            cell00.setCellValue(startTime + "-" + endTime + "  运费实际收款表"); //

            HSSFRow titleRow1 = sheet.createRow(1);
            HSSFCell cell10 = titleRow0.createCell(0);
            cell10.setCellValue("供应商名称"); //
            HSSFCell cell11 = titleRow0.createCell(1);
            cell11.setCellValue("运费实际收款金额（元）"); //
            HSSFCell cell12 = titleRow0.createCell(2);
            cell12.setCellValue("供应商运费金额（元）"); //

            // content
            int index = 2;
            for (ExportExpressFeeVo exportExpressFeeVo : exportExpressFeeVoList) {
                HSSFRow row = sheet.createRow(index);

                HSSFCell cell0 = row.createCell(0);
                cell0.setCellValue(exportExpressFeeVo.getMerchantName()); //

                HSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(exportExpressFeeVo.getUserExpressFee());

                HSSFCell cell2 = row.createCell(2);
                cell2.setCellValue(exportExpressFeeVo.getMerchantExpressFee());
            }


            ///////// 文件名
            String date = DateUtil.nowDate(DateUtil.DATE_YYYYMMDD);
            String fileName = "expressFee" + date + ".xls";

            // 3. 输出文件
            try {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

                outputStream = response.getOutputStream();
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e) {
                log.error("导出运费实际收款报表 出错了:{}", e.getMessage(), e);

                throw new Exception(e);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            //
            log.debug("export file finish");
        } catch (Exception e) {
            log.error("导出运费实际收款报表异常:{}", e.getMessage(), e);

//            response.setHeader("content-type", "application/json;charset=UTF-8");
//            response.setContentType("application/json");
            // response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setStatus(400);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
                Map<String, String> map = new HashMap<>();
                map.put("code", "400");
                map.put("msg", e.getMessage());
                map.put("data", null);

                writer.write(JSONUtil.toJsonString(map));
            } catch (Exception e1) {
                log.error("导出运费实际收款报表 错误:{}", e.getMessage(), e);
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

    /**
     * 导出供应商发票
     *
     * @param startTime
     * @param endTime
     * @param appIds
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/merchant/receipt")
    public void exportMerchantReceipt(@RequestParam("startTime") String startTime,
                                      @RequestParam("endTime") String endTime,
                                      @RequestParam("merchantId") Integer merchantId,
                                      @RequestParam(value = "appId", required = false) String appIds,
                                 HttpServletResponse response) throws Exception {
        OutputStream outputStream = null;
        // 创建HSSFWorkbook对象
        HSSFWorkbook workbook = null;

        try {
            log.info("导出供应商发票 入参 startTime:{}, endTime:{}, appIds:{}", startTime, endTime, appIds);
            // 0.入参检验
//            if (orderExportReqVo.getPayStartDate() == null) {
//                throw new Exception("参数不合法, 查询开始时间为空");
//            }

            List<String> appIdList = Arrays.asList(appIds.split(","));

            // 1. 获取导出数据
            Date startDate = DateUtil.parseDateTime(startTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            Date endDate = DateUtil.parseDateTime(endTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            List<ExportMerchantReceiptVo>  exportMerchantReceiptVoList =
                    exportStatisticService.exportMerchantReceipt(startDate, endDate, appIdList, merchantId);

            if (CollectionUtils.isEmpty(exportMerchantReceiptVoList)) {
                log.warn("导出供应商发票 未找出有效的导出数据!");
                throw new Exception("未找出有效的导出数据!");
            }

            // 2.开始组装excel
            // 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();
            // 创建HSSFSheet对象
            HSSFSheet sheet = workbook.createSheet("供应商发票");

            // 2.1 组装
            // title
            HSSFRow titleRow0 = sheet.createRow(0);
            HSSFCell titlecell0 = titleRow0.createCell(0);
            titlecell0.setCellValue("商品名称"); //
            HSSFCell titlecell1 = titleRow0.createCell(0);
            titlecell1.setCellValue("品类"); //
            HSSFCell titlecell2 = titleRow0.createCell(0);
            titlecell2.setCellValue("销售单位"); //
            HSSFCell titlecell3 = titleRow0.createCell(0);
            titlecell3.setCellValue("数量"); //
            HSSFCell titlecell4 = titleRow0.createCell(0);
            titlecell4.setCellValue("进货单价(元)"); //
            HSSFCell titlecell5 = titleRow0.createCell(0);
            titlecell5.setCellValue("税率"); //
            HSSFCell titlecell6 = titleRow0.createCell(0);
            titlecell6.setCellValue("含税金额"); //
            HSSFCell titlecell7 = titleRow0.createCell(0);
            titlecell7.setCellValue("税额"); //


            // content
            int index = 1;
            for (ExportMerchantReceiptVo exportMerchantReceiptVo : exportMerchantReceiptVoList) {
                HSSFRow row = sheet.createRow(index);

                HSSFCell cell0 = row.createCell(0);
                cell0.setCellValue(exportMerchantReceiptVo.getProductName()); //

                HSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(exportMerchantReceiptVo.getCategoryName());

                HSSFCell cell2 = row.createCell(2);
                cell2.setCellValue(exportMerchantReceiptVo.getSaleUnit());

                HSSFCell cell3 = row.createCell(3);
                cell3.setCellValue(exportMerchantReceiptVo.getNum());

                HSSFCell cell4 = row.createCell(4);
                cell4.setCellValue(exportMerchantReceiptVo.getSprice());

                HSSFCell cell5 = row.createCell(5);
                cell5.setCellValue(exportMerchantReceiptVo.getTaxRate());

                HSSFCell cell6 = row.createCell(6);
                cell6.setCellValue(exportMerchantReceiptVo.getAmount());

                HSSFCell cell7 = row.createCell(7);
                cell7.setCellValue(exportMerchantReceiptVo.getTaxAmount());
            }


            ///////// 文件名
            String date = DateUtil.nowDate(DateUtil.DATE_YYYYMMDD);
            String fileName = "expressFee" + date + ".xls";

            // 3. 输出文件
            try {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

                outputStream = response.getOutputStream();
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e) {
                log.error("导出供应商发票 出错了:{}", e.getMessage(), e);

                throw new Exception(e);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            //
            log.debug("export file finish");
        } catch (Exception e) {
            log.error("导出供应商发票异常:{}", e.getMessage(), e);

//            response.setHeader("content-type", "application/json;charset=UTF-8");
//            response.setContentType("application/json");
            // response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setStatus(400);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
                Map<String, String> map = new HashMap<>();
                map.put("code", "400");
                map.put("msg", e.getMessage());
                map.put("data", null);

                writer.write(JSONUtil.toJsonString(map));
            } catch (Exception e1) {
                log.error("导出供应商发票报表 错误:{}", e.getMessage(), e);
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



    public static void main(String args[]) {
        // new AdminOrderController().excelPrint();
    }

}
