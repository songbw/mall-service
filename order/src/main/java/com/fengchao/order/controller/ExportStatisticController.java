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

    private AdminOrderService adminOrderService;

    private ExportStatisticService exportStatisticService;

    @Autowired
    public ExportStatisticController(AdminOrderService adminOrderService,
                                     ExportStatisticService exportStatisticService) {
        this.adminOrderService = adminOrderService;
        this.exportStatisticService = exportStatisticService;
    }

    /**
     * 导出货款结算表
     *
     * @param startTime
     * @param endTime
     * @param merchantId
     * @param appIds
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/export")
    public void exportOrder(@RequestParam("startTime") String startTime,
                            @RequestParam("endTime") String endTime,
                            @RequestParam("merchantId") String merchantId,
                            @RequestParam(value = "appId", required = false) String appIds,
                            HttpServletResponse response) throws Exception {
        OutputStream outputStream = null;
        // 创建HSSFWorkbook对象
        HSSFWorkbook workbook = null;

        try {
            log.info("导出货款结算表 入参 startTime:{}, endTime:{}, merchantId:{}, appId:{}",
                    startTime, endTime, merchantId, appIds);
            // 0.入参检验
//            if (orderExportReqVo.getPayStartDate() == null) {
//                throw new Exception("参数不合法, 查询开始时间为空");
//            }
//            if (orderExportReqVo.getPayEndDate() == null) {
//                throw new Exception("参数不合法, 查询结束时间为空");
//            }

            List<String> appList = Arrays.asList(appIds.split(","));

            // 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();
            // 创建HSSFSheet对象
            HSSFSheet sheet = workbook.createSheet("订单结算");

            // 1.根据条件获取订单集合
            exportStatisticService.exportSettlement(startTime, endTime, appList, merchantId);


            //                    adminOrderService.exportOrdersMock();
            if (CollectionUtils.isEmpty(exportOrdersVoList) && CollectionUtils.isEmpty(exportOrdersVoListOut)) {
                throw new Exception("未找出有效的导出数据!");
            }

            // 合并导出的订单集合
            List<ExportOrdersVo> mergedExportOrdersVoList = new ArrayList<>();
            if (exportOrdersVoList != null) {
                mergedExportOrdersVoList.addAll(exportOrdersVoList);
            }
            if (exportOrdersVoListOut != null) {
                mergedExportOrdersVoList.addAll(exportOrdersVoListOut);
            }

            // 将要导出的ExportOrdersVo以主订单维度形成map
            Map<String, List<ExportOrdersVo>> exportOrdersVoMap = convertToExportOrdersVoMap(exportOrdersVoList);

            // 2.开始组装excel
            // 2.1 组装title
            createTitle(sheet);

            // 2.2 组装业务数据
            createContent(sheet, exportOrdersVoMap);


            ///////// 文件名
            String date = DateUtil.nowDate(DateUtil.DATE_YYYYMMDD);
            String fileName = "exportorder_" + date + ".xls";

            // 3. 输出文件
            try {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

                outputStream = response.getOutputStream();
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e) {
                log.error("导出订单文件 出错了:{}", e.getMessage(), e);

                throw new Exception(e);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            //
            log.debug("export file finish");
        } catch (Exception e) {
            log.error("导出订单文件异常:{}", e.getMessage(), e);

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
                log.error("导出订单文件 错误:{}", e.getMessage(), e);
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
