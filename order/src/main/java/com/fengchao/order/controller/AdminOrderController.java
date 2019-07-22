package com.fengchao.order.controller;

import com.fengchao.order.bean.vo.ExportOrdersVo;
import com.fengchao.order.bean.vo.OrderExportReqVo;
import com.fengchao.order.service.AdminOrderService;
import com.fengchao.order.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping(value = "/adminorder")
@Slf4j
public class AdminOrderController {

    private AdminOrderService adminOrderService;

    @Autowired
    public AdminOrderController(AdminOrderService adminOrderService) {
        this.adminOrderService = adminOrderService;
    }

    /**
     * 按照条件导出订单
     * 参考：https://blog.csdn.net/ethan_10/article/details/80335350
     * <p>
     * 导出title：
     * 用户id，主订单编号，子订单编号， 订单支付时间， 订单生成时间，品类， 品牌（通过mpu获取）
     * ，sku， mpu， 商品名称， 购买数量 ， 活动 ， 券码， 券来源（券商户），进货价， 销售价，  券支付金额， 订单支付金额，
     * 平台分润比， 收件人名， 省 ， 市， 区
     *
     * @param orderExportReqVo 导出条件
     * @param response
     */
    @GetMapping(value = "/export")
    public void exportOrder(OrderExportReqVo orderExportReqVo, HttpServletResponse response) {
        try {
            // 创建HSSFWorkbook对象
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 创建HSSFSheet对象
            HSSFSheet sheet = workbook.createSheet("订单结算");

            /**
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
             **/

            //输出Excel文件
//            FileOutputStream output = new FileOutputStream("/home/tom/Temp/workbook.xls");
//            workbook.write(output);
//            output.flush();

            // 1.根据条件获取订单集合
            List<ExportOrdersVo> exportOrdersVoList = adminOrderService.exportOrders(orderExportReqVo);

            //
            for (int i = 0; i < exportOrdersVoList.size(); i++) {
                ExportOrdersVo exportOrdersVo = exportOrdersVoList.get(i);

                HSSFRow currentRow = sheet.createRow(i);

                // 用户id
                HSSFCell cell0 = currentRow.createCell(0);
                cell0.setCellValue(exportOrdersVo.getOpenId());

                // ，主订单编号，
                HSSFCell cell1 = currentRow.createCell(1);
                cell1.setCellValue(exportOrdersVo.getTradeNo());

                // 子订单编号，
                HSSFCell cell2 = currentRow.createCell(2);
                cell2.setCellValue(exportOrdersVo.getSubOrderId());

                // 订单支付时间，
                HSSFCell cell3 = currentRow.createCell(3);
                cell3.setCellValue(exportOrdersVo.getPaymentTime());

                // 订单生成时间，
                HSSFCell cell4 = currentRow.createCell(4);
                cell4.setCellValue(exportOrdersVo.getCreateTime());

                // 品类，
                HSSFCell cell5 = currentRow.createCell(5);
                cell5.setCellValue(exportOrdersVo.getCategory());

                // 品牌（通过mpu获取）
                HSSFCell cell6 = currentRow.createCell(6);
                cell6.setCellValue(exportOrdersVo.getBrand());

                // sku，
                HSSFCell cell7 = currentRow.createCell(7);
                cell7.setCellValue(exportOrdersVo.getSku());

                // mpu，
                HSSFCell cell8 = currentRow.createCell(8);
                cell8.setCellValue(exportOrdersVo.getMpu());

                // 商品名称，
                HSSFCell cell9 = currentRow.createCell(9);
                cell9.setCellValue(exportOrdersVo.getCommodityName());

                // 购买数量 ，
                HSSFCell cell10 = currentRow.createCell(10);
                cell10.setCellValue(exportOrdersVo.getQuantity());

                // 活动 ，
                HSSFCell cell11 = currentRow.createCell(11);
                cell11.setCellValue(exportOrdersVo.getPromotion());

                // 券码，
                HSSFCell cell12 = currentRow.createCell(12);
                cell12.setCellValue(exportOrdersVo.getCouponCode());

                // 券来源（券商户），
                HSSFCell cell13 = currentRow.createCell(13);
                cell13.setCellValue(exportOrdersVo.getCouponSupplier());

                // 进货价，
                HSSFCell cell14 = currentRow.createCell(14);
                cell14.setCellValue(exportOrdersVo.getPurchasePrice());

                // 销售价，
                HSSFCell cell15 = currentRow.createCell(15);
                cell15.setCellValue(exportOrdersVo.getTotalRealPrice());

                // 券支付金额，
                HSSFCell cell16 = currentRow.createCell(16);
                cell16.setCellValue(exportOrdersVo.getCouponPrice());

                // 订单支付金额，
                HSSFCell cell17 = currentRow.createCell(17);
                cell17.setCellValue(exportOrdersVo.getPayPrice());

                // 平台分润比，
                HSSFCell cell18 = currentRow.createCell(18);
                cell18.setCellValue("/");

                // 收件人名
                HSSFCell cell19 = currentRow.createCell(19);
                cell19.setCellValue(exportOrdersVo.getBuyerName());

                // 省
                HSSFCell cell20 = currentRow.createCell(20);
                cell20.setCellValue(exportOrdersVo.getProvinceName());

                // 市
                HSSFCell cell21 = currentRow.createCell(21);
                cell21.setCellValue(exportOrdersVo.getCityName());

                // 区
                HSSFCell cell22 = currentRow.createCell(22);
                cell22.setCellValue(exportOrdersVo.getCountyName());
            }


            /////////
            String date = DateUtil.nowDate(DateUtil.DATE_YYYYMMDD);

            String fileName = "exportorder_" + date + ".xls";

            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            OutputStream outputStream = null;

            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();

            log.info("export file finish");
        } catch (Exception e) {
            log.error("到处订单文件异常:{}", e.getMessage(), e);
        }
    }

    public static void main(String args[]) {
        // new AdminOrderController().excelPrint();
    }

}
