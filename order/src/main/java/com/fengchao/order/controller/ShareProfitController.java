package com.fengchao.order.controller;

import com.fengchao.order.bean.vo.ExportReceiptBillVo;
import com.fengchao.order.bean.vo.ExportShareProfitVo;
import com.fengchao.order.constants.ReceiptTypeEnum;
import com.fengchao.order.service.AdminShareProfitService;
import com.fengchao.order.service.SettlementAssistService;
import com.fengchao.order.utils.CalculateUtil;
import com.fengchao.order.utils.DateUtil;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/adminorder")
@Slf4j
public class ShareProfitController {

    private AdminShareProfitService adminShareProfitService;

    @Autowired
    public ShareProfitController(AdminShareProfitService adminShareProfitService) {
        this.adminShareProfitService = adminShareProfitService;
    }

    /**
     * 导出分润表
     *
     * http://localhost:8004/adminorder/export/shareprofit?startTime=2019-11-01&endTime=2019-11-30&appId=11
     *
     * @param startTime
     * @param endTime
     * @param appId
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/export/shareprofit")
    public void shareprofit(@RequestParam("startTime") String startTime,
                            @RequestParam("endTime") String endTime,
                            @RequestParam(value = "appId", required = true) String appId,
                            HttpServletResponse response) throws Exception {
        OutputStream outputStream = null;
        // 创建HSSFWorkbook对象
        HSSFWorkbook workbook = null;

        try {
            log.info("导出分润表 入参 startTime:{}, endTime:{}, appId:{}", startTime, endTime, appId);

            // 1. 参数校验
            String _stime = startTime + " 00:00:00", _etime = endTime + " 23:59:59";
            Date startDate = DateUtil.parseDateTime(_stime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            Date endDate = DateUtil.parseDateTime(_etime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

            long diffDays = DateUtil.diffDays(_stime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS, _etime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            if (diffDays > 31) {
                log.warn("导出分润表 查询日期范围超过一个月");
                throw new Exception("查询日期范围超过一个月");
            }

            // appId
            if (StringUtils.isBlank(appId)) {
                log.warn("导出分润表 appId为空");
                throw new Exception("未找到正确归属信息");
            }

            // 2. 执行查询&数据处理逻辑
            List<ExportShareProfitVo> exportShareProfitVoList = adminShareProfitService.exportShareProfit(startDate, endDate, appId);
            log.info("导出分润表 获取到需要导出数据信息:{}", JSONUtil.toJsonString(exportShareProfitVoList));

            // 3. 创建导出文件对象
            // 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();
            // 创建HSSFSheet对象
            HSSFSheet sheet = workbook.createSheet("分润信息" + DateUtil.nowDate(DateUtil.DATE_YYYYMMDD)); //  加上时间

            //
            int indexRow = 0;

            // TITLE
            HSSFRow title = sheet.createRow(indexRow);

            title.createCell(0).setCellValue("运营平台");
            title.createCell(1).setCellValue("支付方式");
            title.createCell(2).setCellValue("结算类型");
            title.createCell(3).setCellValue("金额");

            indexRow++;

            // CONTENT
            for (ExportShareProfitVo exportShareProfitVo : exportShareProfitVoList) { // 遍历子订单
                HSSFRow currentRow = sheet.createRow(indexRow);

                HSSFCell cell0 = currentRow.createCell(0); // 运营平台
                if (indexRow == 1) {
                    cell0.setCellValue(exportShareProfitVo.getAppPlatformEnum() == null
                            ? "--" : exportShareProfitVo.getAppPlatformEnum().getDesc());
                } else {
                    cell0.setCellValue("");
                }

                HSSFCell cell1 = currentRow.createCell(1); // 支付方式
                cell1.setCellValue(exportShareProfitVo.getPaymentTypeEnum() == null ?
                        "--" : exportShareProfitVo.getPaymentTypeEnum().getDesc());

                HSSFCell cell2 = currentRow.createCell(2); // 结算类型
                cell2.setCellValue(exportShareProfitVo.getSettlementTypeEnum() == null ?
                        "--" : exportShareProfitVo.getSettlementTypeEnum().getDesc());

                HSSFCell cell3 = currentRow.createCell(3); // 金额
                cell3.setCellValue(exportShareProfitVo.getAmout() == null ? "--" : CalculateUtil.converFenToYuan(exportShareProfitVo.getAmout()));

                indexRow++;
            }

            // 4. 输出文件
            ///////// 文件名
            String fileName = "分润" + startTime + "-" + endTime + ".xls";

            try {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

                outputStream = response.getOutputStream();
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e) {
                log.error("导出分润表 出错了:{}", e.getMessage(), e);

                throw new Exception(e);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            //
            log.debug("导出分润表完成!");
        } catch (Exception e) {
            log.error("导出分润表异常:{}", e.getMessage(), e);

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
                log.error("导出分润表 错误:{}", e.getMessage(), e);
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
}
