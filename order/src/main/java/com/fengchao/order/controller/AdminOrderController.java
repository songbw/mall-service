package com.fengchao.order.controller;

import com.fengchao.order.bean.vo.ExportOrdersVo;
import com.fengchao.order.bean.vo.OrderExportReqVo;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.service.AdminOrderService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

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
     * // 创建HSSFRow对象
     * HSSFRow row0 = sheet.createRow(0);
     * <p>
     * //创建HSSFCell对象
     * HSSFCell cell00 = row0.createCell(0);
     * cell00.setCellValue("单元格中的中文1");
     * <p>
     * HSSFCell cell01 = row0.createCell(1);
     * cell01.setCellValue("单元格中的中文2");
     * <p>
     * HSSFRow row1 = sheet.createRow(1);
     * HSSFCell cell10 = row1.createCell(0);
     * cell10.setCellValue("单元格中的中文3");
     *
     * <p>
     * 导出title：
     * 用户id，主订单编号，子订单编号， 订单支付时间， 订单生成时间，品类， 品牌（通过mpu获取）
     * ，sku， mpu， 商品名称， 购买数量 ， 活动 ， 券码， 券来源（券商户），进货价， 销售价，  券支付金额， 订单支付金额，
     * 平台分润比， 收件人名， 省 ， 市， 区
     * <p>
     * 测试: http://localhost:8004/adminorder/export?merchantId=12&payStartDate=2019-01-10&payEndDate=2019-09-10
     *
     * @param orderExportReqVo 导出条件
     * @param response
     */
    @GetMapping(value = "/export")
    public void exportOrder(OrderExportReqVo orderExportReqVo, HttpServletResponse response) throws Exception {
        OutputStream outputStream = null;
        // 创建HSSFWorkbook对象
        HSSFWorkbook workbook = null;

        try {
            log.info("导出订单 入参:{}", JSONUtil.toJsonString(orderExportReqVo));
            // 0.入参检验
//            if (orderExportReqVo.getMerchantId() == null || orderExportReqVo.getMerchantId() <= 0) {
//                throw new Exception("参数不合法, 商户id为空");
//            }
            if (orderExportReqVo.getPayStartDate() == null) {
                throw new Exception("参数不合法, 查询开始时间为空");
            }
            if (orderExportReqVo.getPayEndDate() == null) {
                throw new Exception("参数不合法, 查询结束时间为空");
            }

            // 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();
            // 创建HSSFSheet对象
            HSSFSheet sheet = workbook.createSheet("订单结算");

            // 1.根据条件获取订单集合
            List<ExportOrdersVo> exportOrdersVoList = adminOrderService.exportOrders(orderExportReqVo);
//                    adminOrderService.exportOrdersMock();
            if (CollectionUtils.isEmpty(exportOrdersVoList)) {
                throw new Exception("未找出有效的导出数据!");
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
            log.info("export file finish");
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

    /**
     * 导出订单对账单，
     * 1.导出入账订单,入账订单的状态为:"已完成" & "已退款"
     * 2.导出出账订单,出账订单的状态是:"已退款"
     *
     * <p>
     * 导出title同订单导出接口
     * <p>
     * 测试: http://localhost:8004/adminorder/export/reconciliation?merchantId=2&payStartDate=2019-01-10&payEndDate=2019-09-10
     *
     * @param orderExportReqVo 导出条件
     * @param response
     */
    @GetMapping(value = "/export/reconciliation")
    public void exportOrderReconciliation(OrderExportReqVo orderExportReqVo, HttpServletResponse response) throws Exception {
        OutputStream outputStream = null;
        // 创建HSSFWorkbook对象
        HSSFWorkbook workbook = null;

        try {
            log.info("导出订单对账单 入参:{}", JSONUtil.toJsonString(orderExportReqVo));

            // 1.校验参数
            if (orderExportReqVo.getPayStartDate() == null) {
                throw new Exception("参数不合法, 查询开始时间为空");
            }
            if (orderExportReqVo.getPayEndDate() == null) {
                throw new Exception("参数不合法, 查询结束时间为空");
            }

            // 2.获取入账订单集合
            List<ExportOrdersVo> exportOrdersVoListIncome = adminOrderService.exportOrdersReconciliationIncome(orderExportReqVo);
            // 获取出账订单集合
            List<ExportOrdersVo> exportOrdersVoListOut = adminOrderService.exportOrdersReconciliationOut(orderExportReqVo);

            if (CollectionUtils.isEmpty(exportOrdersVoListIncome) && CollectionUtils.isEmpty(exportOrdersVoListOut)) {
                throw new Exception("未找出有效的导出数据!");
            }

            /**
            // 3. 将要导出的ExportOrdersVo以主订单维度形成map key:tradeno
            Map<String, List<ExportOrdersVo>> exportOrdersVoMapIncome = null;
            Map<String, List<ExportOrdersVo>> exportOrdersVoMapOut = null;

            // 3.1 处理入账
            if (CollectionUtils.isNotEmpty(exportOrdersVoListIncome)) {
                exportOrdersVoMapIncome = convertToExportOrdersVoMap(exportOrdersVoListIncome);
            }
            // 3.2 处理出账
            if (CollectionUtils.isNotEmpty(exportOrdersVoListOut)) {
                exportOrdersVoMapOut = convertToExportOrdersVoMap(exportOrdersVoListOut);
            }

            // 4.开始组装excel
            // 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();

            // 4.1 组装入账
            if (exportOrdersVoMapIncome != null) {
                // 创建HSSFSheet对象
                HSSFSheet sheetIncome = workbook.createSheet("对账单");

                // 组装title
                createTitle(sheetIncome);

                // 组装业务数据
                createContent(sheetIncome, exportOrdersVoMapIncome);
            }

            // 4.2 组装出账
            if (exportOrdersVoMapOut != null) {
                // 创建HSSFSheet对象
                HSSFSheet sheetOut = workbook.createSheet("出账");

                // 组装title
                createTitle(sheetOut);

                // 组装业务数据
                createContent(sheetOut, exportOrdersVoMapOut);
            } **/

            // 3. 合并导出的订单集合
            List<ExportOrdersVo> mergedExportOrdersVoList = new ArrayList<>();
            if (exportOrdersVoListIncome != null) {
                mergedExportOrdersVoList.addAll(exportOrdersVoListIncome);
            }
            if (exportOrdersVoListOut != null) {
                mergedExportOrdersVoList.addAll(exportOrdersVoListOut);
            }

            // 转map
            Map<String, List<ExportOrdersVo>> exportMap = convertToExportOrdersVoMap(mergedExportOrdersVoList);

            // 4.开始组装excel
            // 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();

            // 4.1 组装入账
            if (mergedExportOrdersVoList != null) {
                // 创建HSSFSheet对象
                HSSFSheet sheet = workbook.createSheet("对账单");

                // 组装title
                createTitle(sheet);

                // 组装业务数据
                createContent(sheet, exportMap);
            }


            // 5. 输出文件
            ///////// 文件名
            String date = DateUtil.nowDate(DateUtil.DATE_YYYYMMDD);
            String fileName = "statement_" + date + ".xls";

            //
            try {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

                outputStream = response.getOutputStream();
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e) {
                log.error("导出订单对账单 出错了:{}", e.getMessage(), e);

                throw new Exception(e);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            //
            log.info("导出订单对账单 export file finish");
        } catch (Exception e) {
            log.error("导出订单对账单异常:{}", e.getMessage(), e);

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
                log.error("导出订单对账单 错误:{}", e.getMessage(), e);
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

    // ================================ private ===============================

    /**
     * 创建title
     *
     * @param sheet
     */
    private void createTitle(HSSFSheet sheet) {
        HSSFRow titleRow = sheet.createRow(0);

        // ---主订单
        HSSFCell titleCell0 = titleRow.createCell(0);
        titleCell0.setCellValue("主订单号"); // 主订单

        HSSFCell titleCell1 = titleRow.createCell(1);
        titleCell1.setCellValue("实际支付价格 单位:元"); // 主订单

        HSSFCell titleCell2 = titleRow.createCell(2);
        titleCell2.setCellValue("优惠券价格 单位:元"); // 主订单

        HSSFCell titleCell3 = titleRow.createCell(3);
        titleCell3.setCellValue("优惠券码"); // 主订单

        HSSFCell titleCell4 = titleRow.createCell(4);
        titleCell4.setCellValue("券来源"); // 主订单

        HSSFCell titleCell5 = titleRow.createCell(5);
        titleCell5.setCellValue("运费 单位:元"); // 主订单

        HSSFCell titleCell6 = titleRow.createCell(6);
        titleCell6.setCellValue("余额消费 单位:元"); // 主订单

        HSSFCell titleCell7 = titleRow.createCell(7);
        titleCell7.setCellValue("惠民卡消费 单位:元"); // 主订单

        HSSFCell titleCell8 = titleRow.createCell(8);
        titleCell8.setCellValue("联机账户消费 单位:元"); // 主订单

        HSSFCell titleCell9 = titleRow.createCell(9);
        titleCell9.setCellValue("快捷支付 单位:元"); // 主订单


        // ---子订单
        HSSFCell titleCell10 = titleRow.createCell(10);
        titleCell10.setCellValue("用户id");

        HSSFCell titleCell11 = titleRow.createCell(11);
        titleCell11.setCellValue("子订单编号");

        HSSFCell titleCell12 = titleRow.createCell(12);
        titleCell12.setCellValue("子订单状态");

        HSSFCell titleCell13 = titleRow.createCell(13);
        titleCell13.setCellValue("订单支付时间");

        HSSFCell titleCell14 = titleRow.createCell(14);
        titleCell14.setCellValue("订单生成时间");

        HSSFCell titleCell15 = titleRow.createCell(15);
        titleCell15.setCellValue("品类");

        HSSFCell titleCell16 = titleRow.createCell(16);
        titleCell16.setCellValue("品牌");

        HSSFCell titleCell17 = titleRow.createCell(17);
        titleCell17.setCellValue("供应商");

        HSSFCell titleCell18 = titleRow.createCell(18);
        titleCell18.setCellValue("sku");

        HSSFCell titleCell19 = titleRow.createCell(19);
        titleCell19.setCellValue("mpu");

        HSSFCell titleCell20 = titleRow.createCell(20);
        titleCell20.setCellValue("商品名称");

        HSSFCell titleCell21 = titleRow.createCell(21);
        titleCell21.setCellValue("购买数量");

        HSSFCell titleCell22 = titleRow.createCell(22);
        titleCell22.setCellValue("结算类型");

        HSSFCell titleCell23 = titleRow.createCell(23);
        titleCell23.setCellValue("销售价 单位：元");

        HSSFCell titleCell24 = titleRow.createCell(24);
        titleCell24.setCellValue("sku券支付金额 单位：元");

        HSSFCell titleCell25 = titleRow.createCell(25);
        titleCell25.setCellValue("进货价 单位：元");

        HSSFCell titleCell26 = titleRow.createCell(26);
        titleCell26.setCellValue("退款金额 单位：元");

        HSSFCell titleCell27 = titleRow.createCell(27);
        titleCell27.setCellValue("收件人名");

        HSSFCell titleCell28 = titleRow.createCell(28);
        titleCell28.setCellValue("省");

        HSSFCell titleCell29 = titleRow.createCell(29);
        titleCell29.setCellValue("市");

        HSSFCell titleCell30 = titleRow.createCell(30);
        titleCell30.setCellValue("区");

        HSSFCell titleCell31 = titleRow.createCell(31);
        titleCell31.setCellValue("详细地址");
    }

    /**
     * 组装业务数据
     *
     * @param sheet
     * @param exportOrdersVoMap key: tradeNo
     */
    private void createContent(HSSFSheet sheet, Map<String, List<ExportOrdersVo>> exportOrdersVoMap) {
        Set<String> tradeNoSet = exportOrdersVoMap.keySet();
        int currentRowNum = 1; // 行号
        Set<String> checkFlag = new HashSet<>(); // 该变量标识主订单的遍历，主要用于主订单号和运费列的显示标志

        for (String tradeNo : tradeNoSet) {
            // 子订单
            List<ExportOrdersVo> exportOrdersVoList = exportOrdersVoMap.get(tradeNo);

            // 标识该主订单包含多少个子订单,用于合并'主订单号'和'运费'列的逻辑处理
            int exportOrdersVoCount = exportOrdersVoList.size();

            // 遍历子订单
            for (int index = 0; index < exportOrdersVoCount; index++) {
                ExportOrdersVo exportOrdersVo = exportOrdersVoList.get(index);

                // 新增一行
                HSSFRow currentRow = sheet.createRow(currentRowNum);

                // -- 主订单
                HSSFCell cell0 = currentRow.createCell(0); // 主订单号
                HSSFCell cell1 = currentRow.createCell(1); // 实际支付价格 单位:元
                HSSFCell cell2 = currentRow.createCell(2); // 优惠券价格 单位:元
                HSSFCell cell3 = currentRow.createCell(3); // 优惠券码
                HSSFCell cell4 = currentRow.createCell(4); // 券来源
                HSSFCell cell5 = currentRow.createCell(5); // 运费 单位:元
                HSSFCell cell6 = currentRow.createCell(6); // 余额消费 单位:元
                HSSFCell cell7 = currentRow.createCell(7); // 惠民卡消费 单位:元
                HSSFCell cell8 = currentRow.createCell(8); // 联机账户消费 单位:元
                HSSFCell cell9 = currentRow.createCell(9); // 快捷支付 单位:元

                if (checkFlag.add(tradeNo)) {
                    cell0.setCellValue(tradeNo); // 主订单号
                    cell1.setCellValue(new BigDecimal(exportOrdersVo.getPayPrice()).divide(new BigDecimal(100)).toString()); // 实际支付价格 单位:元
                    cell2.setCellValue(new BigDecimal(exportOrdersVo.getCouponPrice()).divide(new BigDecimal(100)).toString()); // 优惠券价格 单位:元
                    cell3.setCellValue(StringUtils.isBlank(exportOrdersVo.getCouponCode()) ? "-" : exportOrdersVo.getCouponCode()); // 优惠券码
                    cell4.setCellValue(StringUtils.isBlank(exportOrdersVo.getCouponSupplier()) ? "-" : exportOrdersVo.getCouponSupplier()); // 券来源
                    cell5.setCellValue(exportOrdersVo.getExpressFee()); // 运费 单位:元
                    cell6.setCellValue(exportOrdersVo.getBalanceFee()); // 余额消费 单位:元
                    cell7.setCellValue(exportOrdersVo.getHuiminCardFee()); // 惠民卡消费 单位:元
                    cell8.setCellValue(exportOrdersVo.getWoaFee()); // 联机账户消费 单位:元
                    cell9.setCellValue(exportOrdersVo.getQuickPayFee()); // 快捷支付 单位:元
                } else {
                    // 主订单号
                    //cell0.setCellValue("");
                    // 运费
                    //cell1.setCellValue("");
                    // 实际支付价格
                    // 优惠券价格
                    // 优惠码

                    // 如果子订单数大于1， 并且子订单已遍历完，则需要合并'主订单号'和'运费' 列
                    if (exportOrdersVoCount > 1 && (index + 1) == exportOrdersVoCount) { //
                        // 该子订单记录集合在excel中的起始行
                        int startLineNum = currentRowNum - exportOrdersVoCount + 1;
                        // 该子订单记录集合在excel中的结束行
                        int endLineNum = currentRowNum;

                        sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 0, 0));
                        sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 1, 1));
                        sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 2, 2));
                        sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 3, 3));
                        sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 4, 4));
                        sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 5, 5));
                        sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 6, 6));
                        sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 7, 7));
                        sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 8, 8));
                        sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 9, 9));
                    }
                }

                // -- 子订单
                // 用户id
                HSSFCell cell10 = currentRow.createCell(10);
                cell10.setCellValue(exportOrdersVo.getOpenId());

                // 子订单编号，
                HSSFCell cell11 = currentRow.createCell(11);
                cell11.setCellValue(exportOrdersVo.getSubOrderId());

                // 子订单状态
                HSSFCell cell12 = currentRow.createCell(12);
                cell12.setCellValue(exportOrdersVo.getOrderDetailStatus());

                // 订单支付时间，
                HSSFCell cell13 = currentRow.createCell(13);
                cell13.setCellValue("");
                if (exportOrdersVo.getPaymentTime() != null) {
                    cell13.setCellValue(DateUtil.dateTimeFormat(exportOrdersVo.getPaymentTime(), DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
                }

                // 订单生成时间，
                HSSFCell cell14 = currentRow.createCell(14);
                cell14.setCellValue("");
                if (exportOrdersVo.getCreateTime() != null) {
                    cell14.setCellValue(DateUtil.dateTimeFormat(exportOrdersVo.getCreateTime(), DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
                }

                // 品类，
                HSSFCell cell15 = currentRow.createCell(15);
                cell15.setCellValue(exportOrdersVo.getCategory());

                // 品牌（通过mpu获取）
                HSSFCell cell16 = currentRow.createCell(16);
                cell16.setCellValue(exportOrdersVo.getBrand());

                // 供应商
                HSSFCell cell17 = currentRow.createCell(17);
                cell17.setCellValue(exportOrdersVo.getMerchantName());

                // sku，
                HSSFCell cell18 = currentRow.createCell(18);
                cell18.setCellValue(exportOrdersVo.getSku());

                // mpu，
                HSSFCell cell19 = currentRow.createCell(19);
                cell19.setCellValue(exportOrdersVo.getMpu());

                // 商品名称，
                HSSFCell cell20 = currentRow.createCell(20);
                cell20.setCellValue(exportOrdersVo.getCommodityName());

                // 购买数量 ，
                HSSFCell cell21 = currentRow.createCell(21);
                cell21.setCellValue(exportOrdersVo.getQuantity());

                // 活动 ，
                HSSFCell cell22 = currentRow.createCell(22);
                cell22.setCellValue(exportOrdersVo.getSettlementType());

                // 销售价，(单位：元)
                HSSFCell cell23 = currentRow.createCell(23);
                cell23.setCellValue("无");
                if (exportOrdersVo.getSkuPayPrice() != null) {
                    String _price = new BigDecimal(exportOrdersVo.getSkuPayPrice()).divide(new BigDecimal(100)).toString();
                    cell23.setCellValue(_price);
                }

                // 券支付金额，(单位：元)
                HSSFCell cell24 = currentRow.createCell(24);
                cell24.setCellValue("无");
                if (exportOrdersVo.getSkuCouponDiscount() > 0) {
                    String _price = new BigDecimal(exportOrdersVo.getSkuCouponDiscount()).divide(new BigDecimal(100)).toString();
                    cell24.setCellValue(_price);
                }

                // 进货价 单位：元
                HSSFCell cell25 = currentRow.createCell(25);
                cell25.setCellValue("无");
                if (exportOrdersVo.getPurchasePrice() != null && exportOrdersVo.getPurchasePrice() >= 0) {
                    String _price = new BigDecimal(exportOrdersVo.getPurchasePrice()).divide(new BigDecimal(100)).toString();
                    cell25.setCellValue(_price);
                }

                // 退款金额 单位：元
                HSSFCell cell26 = currentRow.createCell(26);
                cell26.setCellValue("无");
                if (StringUtils.isNotBlank(exportOrdersVo.getOrderDetailRefundAmount())) {
                    log.info("==========================={}", JSONUtil.toJsonString(exportOrdersVo));
                    BigDecimal _priceBigDecimal = new BigDecimal(exportOrdersVo.getOrderDetailRefundAmount()).multiply(new BigDecimal(-1));
                    String _price = _priceBigDecimal.toString();
                    cell26.setCellValue(_price);
                }

                // 收件人名
                HSSFCell cell27 = currentRow.createCell(27);
                cell27.setCellValue(exportOrdersVo.getBuyerName());

                // 省
                HSSFCell cell28 = currentRow.createCell(28);
                cell28.setCellValue(exportOrdersVo.getProvinceName());

                // 市
                HSSFCell cell29 = currentRow.createCell(29);
                cell29.setCellValue(exportOrdersVo.getCityName());

                // 区
                HSSFCell cell30 = currentRow.createCell(30);
                cell30.setCellValue(exportOrdersVo.getCountyName());

                // 详细地址
                HSSFCell cell31 = currentRow.createCell(31);
                cell31.setCellValue(exportOrdersVo.getAddress());

                //
                currentRowNum++;
            }
        }
    }

    /**
     * 将需要导出的数据转成主订单维度的map key:tradeno
     *
     * 由于ExportOrdersVo是子订单维度，所以该转换是一个主订单tradeNo为维度的 子订单集合
     *
     * @param exportOrdersVoList
     * @return
     */
    private Map<String, List<ExportOrdersVo>> convertToExportOrdersVoMap(List<ExportOrdersVo> exportOrdersVoList) {
        Map<String, List<ExportOrdersVo>> exprotOrdersVoMap = new HashMap<>();

        for (ExportOrdersVo exportOrdersVo : exportOrdersVoList) {
            String tradeNo = exportOrdersVo.getTradeNo();
            List<ExportOrdersVo> _exportOrdersVoList = exprotOrdersVoMap.get(tradeNo);
            if (_exportOrdersVoList == null) {
                _exportOrdersVoList = new ArrayList<>();
                exprotOrdersVoMap.put(tradeNo, _exportOrdersVoList);
            }
            _exportOrdersVoList.add(exportOrdersVo);
        }

        return exprotOrdersVoMap;
    }


    public static void main(String args[]) {
        // new AdminOrderController().excelPrint();
    }

}
