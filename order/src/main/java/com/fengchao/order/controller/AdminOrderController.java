package com.fengchao.order.controller;

import com.fengchao.order.bean.vo.OrderExportReqVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping(value = "/adminorder")
@Slf4j
public class AdminOrderController {

    /**
     * 按照条件导出订单
     * 参考：https://blog.csdn.net/ethan_10/article/details/80335350
     *
     * 导出title：
     * 用户id，主订单编号，子订单编号， 订单支付时间， 订单生成时间，品类， 品牌（通过mpu获取）
     * ，sku， mpu， 商品名称， 购买数量 ， 活动 ， 券码， 券来源（券商户），进货价， 销售价，  券支付金额， 订单支付金额，
     * 平台分润比， 收件人名， 省 ， 市， 区
     *
     * @param orderExportReqVo 导出条件
     * @param res
     */
    @GetMapping(value = "/export")
    public void exportOrder(OrderExportReqVo orderExportReqVo , HttpServletResponse res) {
        try {
            // 创建HSSFWorkbook对象
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 创建HSSFSheet对象
            HSSFSheet sheet = workbook.createSheet("订单结算");
            // 创建HSSFRow对象
            HSSFRow row0 = sheet.createRow(0);

            //创建HSSFCell对象
            HSSFCell cell00 = row0.createCell(0);
            cell00.setCellValue("单元格中的中文1");

            HSSFCell cell01 = row0.createCell(1);
            cell01.setCellValue("单元格中的中文2");

            HSSFRow row1 = sheet.createRow(1);
            HSSFCell cell10 = row1.createCell(0);
            cell10.setCellValue("单元格中的中文3");
            //设置单元格的值

            //输出Excel文件
//            FileOutputStream output = new FileOutputStream("/home/tom/Temp/workbook.xls");
//            workbook.write(output);
//            output.flush();

            // 1.根据条件获取订单集合





            String fileName = "upload.xls";

            res.setHeader("content-type", "application/octet-stream");
            res.setContentType("application/octet-stream");
            res.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            OutputStream outputStream = null;

            try {
                outputStream = res.getOutputStream();
                workbook.write(outputStream);
                outputStream.flush();
            } catch ( IOException e ) {
                e.printStackTrace();
            } finally {

            }

            log.info("export file finish");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        // new AdminOrderController().excelPrint();
    }

}
