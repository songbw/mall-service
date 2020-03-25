package com.fengchao.product.aoyi;

import com.fengchao.product.aoyi.mapper.WeipinhuiAddressMapper;
import com.fengchao.product.aoyi.model.WeipinhuiAddress;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@SpringBootTest
public class ImportWeipinhuiAddressTests {

    @Autowired
    private WeipinhuiAddressMapper weipinhuiAddressMapper;

    @Test
    public void importAddress() {
        String fileFullName = "/Users/tom/Downloads/addressimport.xlsx";

        Workbook workbook = null;
        FileInputStream inputStream = null;

        try {
            // 获取Excel文件
            File excelFile = new File(fileFullName);
            if (!excelFile.exists()) {
                log.warn("指定的Excel文件不存在！");
                throw new Exception("指定的Excel文件不存在！");
            }

            // 获取Excel工作簿
            inputStream = new FileInputStream(excelFile);

            workbook = new XSSFWorkbook(inputStream);
            // 解析sheet
            Sheet sheet = workbook.getSheetAt(0);

            int firstRowNum = sheet.getFirstRowNum();
            int rowStart = firstRowNum + 1;
            int rowEnd = sheet.getPhysicalNumberOfRows();
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row row = sheet.getRow(rowNum);

                Cell cell0 = row.getCell(0); // 苏宁地址ID
                cell0.setCellType(CellType.STRING);
                Cell cell1 = row.getCell(1); // 苏宁地址父ID
                Cell cell2 = row.getCell(2); // 地址级别
                Cell cell3 = row.getCell(3); // 苏宁地址名称
                Cell cell6 = row.getCell(6); // 唯品会地址名称
                Cell cell7 = row.getCell(7); // 唯品会地址ID
                Cell cell8 = row.getCell(8); // 唯品会地址父ID
                String suning_id = cell0.getStringCellValue();
                String suning_pid = cell1.getStringCellValue();
                String level = cell2.getStringCellValue();
                String suning_name = cell3.getStringCellValue();
                String weipinhui_name = (cell6 == null ? "" : cell6.getStringCellValue());
                String weipinhui_id = (cell7 == null ? "" : cell7.getStringCellValue());
                String weipinhui_pid = (cell8 == null ? "" : cell8.getStringCellValue());


                WeipinhuiAddress weipinhuiAddress = new WeipinhuiAddress();

                weipinhuiAddress.setSnCode(suning_id);
                weipinhuiAddress.setSnPcode(suning_pid);
                weipinhuiAddress.setSnName(suning_name);
                weipinhuiAddress.setLevel(Integer.valueOf(level));
                weipinhuiAddress.setWphCode(weipinhui_id);
                weipinhuiAddress.setWphPcode(weipinhui_pid);
                weipinhuiAddress.setWphName(weipinhui_name);

                weipinhuiAddressMapper.insertSelective(weipinhuiAddress);

                log.info("处理第{}行数据:{}", rowNum, JSONUtil.toJsonString(weipinhuiAddress));
            }
        } catch (Exception e) {
            log.error("解析Excel失败:{}", e.getMessage(), e);
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                log.error("关闭数据流出错！错误信息：" + e.getMessage());
            }
        }
    }

}
