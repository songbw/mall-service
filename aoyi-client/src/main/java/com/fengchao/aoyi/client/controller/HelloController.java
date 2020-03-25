package com.fengchao.aoyi.client.controller;

import com.fengchao.aoyi.client.bean.dto.weipinhui.res.AoyiAdrressResDto;
import com.fengchao.aoyi.client.utils.JSONUtil;
import com.fengchao.aoyi.client.weipinhuiService.ProductWeipinhuiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/test/weipinhui", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class HelloController {

    private ProductWeipinhuiService productWeipinhuiService;

    @Autowired
    public HelloController(ProductWeipinhuiService productWeipinhuiService) {
        this.productWeipinhuiService = productWeipinhuiService;
    }

    /**
     * 导出唯品会地址
     *
     * http://localhost:8001/test/weipinhui/export/address
     *
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/export/address")
    public void exportDailyOrderStatistic(HttpServletResponse response) throws Exception {
        OutputStream outputStream = null;
        // 创建HSSFWorkbook对象
        HSSFWorkbook workbook = null;

        try {
            log.info("导出唯品会地址 开始");

            int pageNo = 1;
            List<AoyiAdrressResDto> exportResult = new ArrayList<>();
            while (true) {
                log.info("导出唯品会地址 准备导出第{}页", pageNo);
                List<AoyiAdrressResDto> aoyiAdrressResDtoList =
                        productWeipinhuiService.queryAddress(pageNo, 20);

                exportResult.addAll(aoyiAdrressResDtoList);
                if (aoyiAdrressResDtoList.size() < 20) {
                    break;
                } else {
                    pageNo++;
                }
            }

            log.info("导出唯品会地址 共获取数据{}条", exportResult.size());


            // 3. 准备导出
            // 3.1 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();
            // 创建HSSFSheet对象
            HSSFSheet sheet = workbook.createSheet("唯品会地址"); //  加上时间

            // 3.2 TITLE
            HSSFRow title = sheet.createRow(0);

            title.createCell(0).setCellValue("地址code");
            title.createCell(1).setCellValue("名称");
            title.createCell(2).setCellValue("地址level");
            title.createCell(3).setCellValue("上级地址code");


            // 3.3 CONTENT
            int indexRow = 1; // 行
            for (AoyiAdrressResDto aoyiAdrressResDto : exportResult) { // 遍历子订单
                HSSFRow currentRow = sheet.createRow(indexRow);

                HSSFCell cell0 = currentRow.createCell(0); // 地址code
                cell0.setCellValue(StringUtils.isBlank(aoyiAdrressResDto.getDivisionCode()) ?
                        "--" : aoyiAdrressResDto.getDivisionCode());

                HSSFCell cell1 = currentRow.createCell(1); // 名称
                cell1.setCellValue(StringUtils.isBlank(aoyiAdrressResDto.getDivisionName()) ?
                        "--" : aoyiAdrressResDto.getDivisionName());

                HSSFCell cell2 = currentRow.createCell(2); // 地址level
                cell2.setCellValue(StringUtils.isBlank(aoyiAdrressResDto.getDivisionLevel()) ?
                        "--" : aoyiAdrressResDto.getDivisionLevel());

                HSSFCell cell3 = currentRow.createCell(3); // 上级地址code
                cell3.setCellValue(StringUtils.isBlank(aoyiAdrressResDto.getParentId()) ?
                        "--" : aoyiAdrressResDto.getParentId());

                indexRow++;
            }


            // 4. 输出文件
            ///////// 文件名
            String fileName = "唯品会地址.xls";

            try {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

                outputStream = response.getOutputStream();
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e) {
                log.error("导出唯品会地址 出错了:{}", e.getMessage(), e);

                throw new Exception(e);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            //
            log.info("导出唯品会地址 finish");
        } catch (Exception e) {
            log.error("导出唯品会地址:{}", e.getMessage(), e);

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
                log.error("导出唯品会地址 错误:{}", e.getMessage(), e);
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
