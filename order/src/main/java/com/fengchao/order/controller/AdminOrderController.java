package com.fengchao.order.controller;

import com.fengchao.order.bean.vo.ExportOrdersVo;
import com.fengchao.order.bean.vo.OrderExportReqVo;
import com.fengchao.order.service.AdminOrderService;
import com.fengchao.order.utils.DateUtil;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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
     *
     *              // 创建HSSFRow对象
     *              HSSFRow row0 = sheet.createRow(0);
     *
     *              //创建HSSFCell对象
     *              HSSFCell cell00 = row0.createCell(0);
     *              cell00.setCellValue("单元格中的中文1");
     *
     *              HSSFCell cell01 = row0.createCell(1);
     *              cell01.setCellValue("单元格中的中文2");
     *
     *              HSSFRow row1 = sheet.createRow(1);
     *              HSSFCell cell10 = row1.createCell(0);
     *              cell10.setCellValue("单元格中的中文3");
     *
     * <p>
     * 导出title：
     * 用户id，主订单编号，子订单编号， 订单支付时间， 订单生成时间，品类， 品牌（通过mpu获取）
     * ，sku， mpu， 商品名称， 购买数量 ， 活动 ， 券码， 券来源（券商户），进货价， 销售价，  券支付金额， 订单支付金额，
     * 平台分润比， 收件人名， 省 ， 市， 区
     *
     * 测试: http://localhost:8004/adminorder/export?merchantId=12&payStartDate=2019-01-10&payEndDate=2019-09-10
     *
     * @param orderExportReqVo 导出条件
     * @param response
     */
    @GetMapping(value = "/export")
    public void exportOrder(OrderExportReqVo orderExportReqVo, HttpServletResponse response) {
        try {
            log.info("导出订单 入参:{}", JSONUtil.toJsonString(orderExportReqVo));
            // 0.入参检验
            if (orderExportReqVo.getMerchantId() == null || orderExportReqVo.getMerchantId() <= 0) {
                throw new Exception("参数不合法, 商户id为空");
            }
            if (orderExportReqVo.getPayStartDate() == null) {
                throw new Exception("参数不合法, 查询开始时间为空");
            }
            if (orderExportReqVo.getPayEndDate() == null) {
                throw new Exception("参数不合法, 查询结束时间为空");
            }

            // 创建HSSFWorkbook对象
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 创建HSSFSheet对象
            HSSFSheet sheet = workbook.createSheet("订单结算");

            // 1.根据条件获取订单集合
            List<ExportOrdersVo> exportOrdersVoList = adminOrderService.exportOrders(orderExportReqVo);
//                    adminOrderService.exportOrdersMock();

            if (CollectionUtils.isEmpty(exportOrdersVoList)) {
                throw new Exception("未找出有效的导出数据!");
            }

            // 2.开始组装excel
            // 2.1 组装title
            HSSFRow titleRow = sheet.createRow(0);
            HSSFCell titleCell0 = titleRow.createCell(0);
            titleCell0.setCellValue("用户id");
            HSSFCell titleCell1 = titleRow.createCell(1);
            titleCell1.setCellValue("主订单编号");
            HSSFCell titleCell2 = titleRow.createCell(2);
            titleCell2.setCellValue("子订单编号");
            HSSFCell titleCell3 = titleRow.createCell(3);
            titleCell3.setCellValue("订单支付时间");
            HSSFCell titleCell4 = titleRow.createCell(4);
            titleCell4.setCellValue("订单生成时间");
            HSSFCell titleCell5 = titleRow.createCell(5);
            titleCell5.setCellValue("品类");
            HSSFCell titleCell6 = titleRow.createCell(6);
            titleCell6.setCellValue("品牌");
            HSSFCell titleCell7 = titleRow.createCell(7);
            titleCell7.setCellValue("sku");
            HSSFCell titleCell8 = titleRow.createCell(8);
            titleCell8.setCellValue("mpu");
            HSSFCell titleCell9 = titleRow.createCell(9);
            titleCell9.setCellValue("商品名称");
            HSSFCell titleCell10 = titleRow.createCell(10);
            titleCell10.setCellValue("购买数量 ");
            HSSFCell titleCell11 = titleRow.createCell(11);
            titleCell11.setCellValue("活动 ");
            HSSFCell titleCell12 = titleRow.createCell(12);
            titleCell12.setCellValue("券码");
            HSSFCell titleCell13 = titleRow.createCell(13);
            titleCell13.setCellValue("券来源");
            HSSFCell titleCell14 = titleRow.createCell(14);
            titleCell14.setCellValue("进货价");
            HSSFCell titleCell15 = titleRow.createCell(15);
            titleCell15.setCellValue("销售价");
            HSSFCell titleCell16 = titleRow.createCell(16);
            titleCell16.setCellValue("券支付金额");
            HSSFCell titleCell17 = titleRow.createCell(17);
            titleCell17.setCellValue("订单支付金额");
            HSSFCell titleCell18 = titleRow.createCell(18);
            titleCell18.setCellValue("平台分润比");
            HSSFCell titleCell19 = titleRow.createCell(19);
            titleCell19.setCellValue("收件人名");
            HSSFCell titleCell20 = titleRow.createCell(20);
            titleCell20.setCellValue("省");
            HSSFCell titleCell21 = titleRow.createCell(21);
            titleCell21.setCellValue("市");
            HSSFCell titleCell22 = titleRow.createCell(22);
            titleCell22.setCellValue("区");

            // 2.2 组装业务数据
            for (int index = 0; index < exportOrdersVoList.size(); index++) {
                ExportOrdersVo exportOrdersVo = exportOrdersVoList.get(index);

                HSSFRow currentRow = sheet.createRow(index + 1);

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
                cell14.setCellValue(exportOrdersVo.getPurchasePrice() == null ? "无" : String.valueOf(exportOrdersVo.getPurchasePrice()));

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
