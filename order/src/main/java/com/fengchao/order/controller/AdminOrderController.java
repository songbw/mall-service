package com.fengchao.order.controller;

import com.fengchao.order.bean.vo.*;
import com.fengchao.order.constants.PaymentTypeEnum;
import com.fengchao.order.constants.ReceiptTypeEnum;
import com.fengchao.order.rpc.extmodel.OrderPayMethodInfoBean;
import com.fengchao.order.service.AdminInvoiceService;
import com.fengchao.order.service.AdminOrderService;
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
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping(value = "/adminorder")
@Slf4j
public class AdminOrderController {

    private AdminOrderService adminOrderService;

    private AdminInvoiceService adminInvoiceService;

    @Autowired
    public AdminOrderController(AdminOrderService adminOrderService,
                                AdminInvoiceService adminInvoiceService) {
        this.adminOrderService = adminOrderService;
        this.adminInvoiceService = adminInvoiceService;
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
            // 获取出账订单集合
            List<ExportOrdersVo> exportOrdersVoListOut = adminOrderService.exportOrdersReconciliationOut(orderExportReqVo);

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
            log.debug("导出订单对账单 export file finish");
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

    @GetMapping(value = "/export/admin")
    public void exportOrderAdmin(OrderExportReqVo orderExportReqVo, HttpServletResponse response) throws Exception {
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
            // 获取出账订单集合
            List<ExportOrdersVo> exportOrdersVoListOut = adminOrderService.exportOrdersReconciliationOut(orderExportReqVo);

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
            Map<String, List<ExportOrdersVo>> exportOrdersVoMap = convertToExportOrdersVoMapByPay(mergedExportOrdersVoList);

            // 2.开始组装excel
            // 2.1 组装title
            createAdminTitle(sheet);

            // 2.2 组装业务数据
            createAdminContent(sheet, exportOrdersVoMap);


            ///////// 文件名
            String date = DateUtil.nowDate(DateUtil.DATE_YYYYMMDD);
            String fileName = "admin_exportorder_" + date + ".xls";

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
    @GetMapping(value = "/export/admin/reconciliation")
    public void exportAdminOrderReconciliation(OrderExportReqVo orderExportReqVo, HttpServletResponse response) throws Exception {
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
            Map<String, List<ExportOrdersVo>> exportMap = convertToExportOrdersVoMapByPay(mergedExportOrdersVoList);

            // log.info("导出订单对账单 Map<String, List<ExportOrdersVo>>:{}", JSONUtil.toJsonString(exportMap));

            // 4.开始组装excel
            // 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();

            // 4.1 组装入账
            if (mergedExportOrdersVoList != null) {
                // 创建HSSFSheet对象
                HSSFSheet sheet = workbook.createSheet("对账单");

                // 组装title
                createAdminTitle(sheet);

                // 组装业务数据
                createAdminContent(sheet, exportMap);
            }


            // 5. 输出文件
            ///////// 文件名
            String date = DateUtil.nowDate(DateUtil.DATE_YYYYMMDD);
            String fileName = "admin_statement_" + date + ".xls";

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
            log.debug("导出订单对账单 export file finish");
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

    /**
     * 导出交易流水单，
     * 1.导出入账订单,入账订单的状态为:"已完成" & "已退款"
     * 2.导出出账订单,出账订单的状态是:"已退款"
     *
     * <p>
     * 导出title同订单导出接口
     * <p>
     * 测试: http://localhost:8004/adminorder/export/reconciliation?merchantId=2&payStartDate=2019-01-10&payEndDate=2019-09-10
     *
     * @param billExportReqVo 导出条件
     * @param response
     */
    @PostMapping(value = "/export/bill")
    public void exportOrderBill(@RequestBody BillExportReqVo billExportReqVo, HttpServletResponse response) throws Exception {
        OutputStream outputStream = null;
        // 创建HSSFWorkbook对象
        HSSFWorkbook workbook = null;

        try {
            log.info("导出交易流水单 入参:{}", JSONUtil.toJsonString(billExportReqVo));

            // 1.校验参数
            if (billExportReqVo.getStartDate() == null) {
                throw new Exception("参数不合法, 查询开始时间为空");
            }
            if (billExportReqVo.getEndDate() == null) {
                throw new Exception("参数不合法, 查询结束时间为空");
            }
            if (billExportReqVo.getPayType() == null) {
                throw new Exception("参数不合法, 查询结束时间为空");
            }

            // 2.获取交易订单集合
            Map<String, List<OrderPayMethodInfoBean>> exportBill = adminOrderService.exportCandRBill(billExportReqVo);
            List<OrderPayMethodInfoBean> exportConsume = exportBill.get("consume");
            List<OrderPayMethodInfoBean> exportRefund = exportBill.get("refund");

            if (CollectionUtils.isEmpty(exportConsume) && CollectionUtils.isEmpty(exportRefund)) {
                throw new Exception("未找出有效的导出数据!");
            }
            // 4.开始组装excel
            // 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();

            // 4.1 组装入账
            if (exportRefund != null) {
                // 创建HSSFSheet对象
                HSSFSheet sheet1 = workbook.createSheet("完成交易单");
                HSSFSheet sheet2 = workbook.createSheet("退款交易单");
                // 组装title
                createBillTitle(sheet1, billExportReqVo);
                createBillTitle(sheet2, billExportReqVo);

                // 组装业务数据
                createBillContent(sheet1, exportConsume);
                createBillContent(sheet2, exportRefund);
            }

            // 5. 输出文件
            ///////// 文件名
            String date = DateUtil.nowDate(DateUtil.DATE_YYYYMMDD);
            String fileName = billExportReqVo.getPayType() +  "_bill_" + date + ".xls";

            //
            try {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

                outputStream = response.getOutputStream();
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e) {
                log.error("导出交易流水单 出错了:{}", e.getMessage(), e);

                throw new Exception(e);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            //
            log.debug("导出交易流水单 export file finish");
        } catch (Exception e) {
            log.error("导出交易流水单异常:{}", e.getMessage(), e);

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
                log.error("导出交易流水单 错误:{}", e.getMessage(), e);
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

    private void createBillContent(HSSFSheet sheet, List<OrderPayMethodInfoBean> exportConsume) {
        int currentRowNum = 1; // 行号
        for (int index = 0; index < exportConsume.size(); index++) {
            OrderPayMethodInfoBean payMethodInfoBean = exportConsume.get(index);

            // 新增一行
            HSSFRow currentRow = sheet.createRow(currentRowNum);

            // -- 主订单
            HSSFCell cell0 = currentRow.createCell(0); // 支付单号
            HSSFCell cell1 = currentRow.createCell(1); // 卡号
            HSSFCell cell2 = currentRow.createCell(2); // 第三方
            HSSFCell cell3 = currentRow.createCell(3); // 交易类型
            HSSFCell cell4 = currentRow.createCell(4); // 交易时间
            HSSFCell cell5 = currentRow.createCell(5); // 交易金额
            HSSFCell cell6 = currentRow.createCell(6); // 交易状态
            HSSFCell cell7 = currentRow.createCell(7); //AppId

            cell0.setCellValue(payMethodInfoBean.getOrderNo()); // 主订单号
            cell1.setCellValue(payMethodInfoBean.getCardNo()); // 实际支付价格 单位:元
            cell2.setCellValue(payMethodInfoBean.getOutTradeNo());// 交易类型
            cell3.setCellValue(payMethodInfoBean.getTradeType());// 交易类型
            cell4.setCellValue(payMethodInfoBean.getTradeDate()); // 优惠券码
            cell5.setCellValue(new BigDecimal(payMethodInfoBean.getActPayFee()).divide(new BigDecimal(100)).setScale(2).toString()); // 券来源
            Integer status = payMethodInfoBean.getStatus();
            if(status == 0){
                cell6.setCellValue("新创建");
            }else if(status == 1){
                cell6.setCellValue("成功");
            }else if(status == 2){
                cell6.setCellValue("失败");
            }else if(status == 3){
                cell6.setCellValue("超时");
            }
            cell7.setCellValue(payMethodInfoBean.getAppId());

            if(payMethodInfoBean.getPayType().equals("balance")){
                HSSFCell cell8 = currentRow.createCell(8); // 余额手机号
                cell8.setCellValue(payMethodInfoBean.getPayer());
            }
            // 状态
            currentRowNum ++;
        }
    }

    private void createBillTitle(HSSFSheet sheet, BillExportReqVo billExportReqVo) {
        HSSFRow titleRow = sheet.createRow(0);

        // ---主订单
        HSSFCell titleCell0 = titleRow.createCell(0);
        titleCell0.setCellValue("支付单号"); // 支付单号

        HSSFCell titleCell1 = titleRow.createCell(1);
        titleCell1.setCellValue("openID");

        HSSFCell titleCell2 = titleRow.createCell(2);
        titleCell2.setCellValue("第三方流水号"); // 第三方流水号

        HSSFCell titleCell3 = titleRow.createCell(3);
        titleCell3.setCellValue("交易类型"); // 交易类型

        HSSFCell titleCell4 = titleRow.createCell(4);
        titleCell4.setCellValue("交易时间"); // 交易时间

        HSSFCell titleCell5 = titleRow.createCell(5);
        titleCell5.setCellValue("交易金额"); // 交易金额

        HSSFCell titleCell6 = titleRow.createCell(6);
        titleCell6.setCellValue("交易状态"); // 交易状态

        HSSFCell titleCell7 = titleRow.createCell(7);
        titleCell7.setCellValue("运营平台"); // 交易状态

        if(billExportReqVo.getPayType().equals("balance")){
            HSSFCell titleCell8 = titleRow.createCell(8);
            titleCell8.setCellValue("手机号"); // 主订单
        }
    }


    /**
     * 导出发货概况
     *
     * @param response
     * @throws Exception
     *
     * http://localhost:8004/adminorder/export/daily/order
     */
    @GetMapping(value = "/export/daily/order")
    public void exportDailyOrderStatistic(HttpServletResponse response) throws Exception {
        OutputStream outputStream = null;
        // 创建HSSFWorkbook对象
        HSSFWorkbook workbook = null;

        try {
            log.info("每日统计 入参: 无");

            // 1.根据条件获统计数据
            Map<String, Object> statisticMap = adminOrderService.exportDailyOrderStatistic();
            List<DailyExportOrderStatisticVo> dailyExportOrderStatisticVoList = (List) statisticMap.get("data");
            String statisticTime = (String) statisticMap.get("statisticTime");
            int increasedCount = (Integer) statisticMap.get("increasedCount"); // 新增订单数量
            Long totalCompletedOrderCount = (Long) statisticMap.get("totalCompletedOrderCount"); // 总计 已完成子订单数量
            Long totalDeliveredOrderCount = (Long) statisticMap.get("totalDeliveredOrderCount"); // 总计 已发货子订单数量
            Long totalUnDeliveryOrderCount = (Long) statisticMap.get("totalUnDeliveryOrderCount"); // 总计 未发货子订单数量
            Long totalApplyRefundOrderCount = (Long) statisticMap.get("totalApplyRefundOrderCount"); // 总计 售后子订单数量
            Long totalOrderDetailCount = (Long) statisticMap.get("totalOrderDetailCount"); // 总计 所有子订单数量

            // 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();
            // 创建HSSFSheet对象
            HSSFSheet sheet = workbook.createSheet("每日统计" + statisticTime); //  加上时间

            //
            int indexRow = 0;

            // PRE TITLE 1
            HSSFRow preTitleRow1 = sheet.createRow(indexRow);

            preTitleRow1.createCell(0).setCellValue("无锡商城供应商发货概况记录表");
            preTitleRow1.createCell(1).setCellValue("");
            preTitleRow1.createCell(2).setCellValue("");
            preTitleRow1.createCell(3).setCellValue("");
            preTitleRow1.createCell(4).setCellValue("");
            preTitleRow1.createCell(5).setCellValue("");
            preTitleRow1.createCell(6).setCellValue("");
            preTitleRow1.createCell(7).setCellValue("");
            preTitleRow1.createCell(8).setCellValue("");

            sheet.addMergedRegion(new CellRangeAddress(indexRow, indexRow, 0, 8));
            indexRow = indexRow + 1;


            // PRE TITLE 2
            HSSFRow preTitleRow2 = sheet.createRow(indexRow);
            indexRow = indexRow + 1;
            preTitleRow2.createCell(0).setCellValue("已发货:" + totalDeliveredOrderCount);
            preTitleRow2.createCell(1).setCellValue("待发货:" + totalUnDeliveryOrderCount);
            preTitleRow2.createCell(2).setCellValue("今日新增有效订单:" + increasedCount);
            preTitleRow2.createCell(3).setCellValue("已完成:" + totalCompletedOrderCount);
            preTitleRow2.createCell(4).setCellValue("");
            preTitleRow2.createCell(5).setCellValue("");
            preTitleRow2.createCell(6).setCellValue("");
            preTitleRow2.createCell(7).setCellValue("");
            preTitleRow2.createCell(8).setCellValue("");


            // TITLE
            HSSFRow titleRow = sheet.createRow(indexRow);
            indexRow = indexRow + 1;

            HSSFCell titleCell0 = titleRow.createCell(0);
            titleCell0.setCellValue("序号"); // 主订单

            HSSFCell titleCell1 = titleRow.createCell(1);
            titleCell1.setCellValue("供应商名称"); // 主订单

            HSSFCell titleCell2 = titleRow.createCell(2);
            titleCell2.setCellValue("已完成"); // 主订单

            HSSFCell titleCell3 = titleRow.createCell(3);
            titleCell3.setCellValue("已发货数量"); // 主订单

            HSSFCell titleCell4 = titleRow.createCell(4);
            titleCell4.setCellValue("待发货数量"); // 主订单

            HSSFCell titleCell5 = titleRow.createCell(5);
            titleCell5.setCellValue("最早待发订单子单号");

            HSSFCell titleCell6 = titleRow.createCell(6);
            titleCell6.setCellValue("交易时间");

            HSSFCell titleCell7 = titleRow.createCell(7);
            titleCell7.setCellValue("售后数量");

            HSSFCell titleCell8 = titleRow.createCell(8);
            titleCell8.setCellValue("订单数量");

            // CONTENT
            int snum = 1;
            for (DailyExportOrderStatisticVo dailyExportOrderStatisticVo : dailyExportOrderStatisticVoList) { // 遍历子订单
                HSSFRow currentRow = sheet.createRow(indexRow);
                indexRow = indexRow + 1;

                HSSFCell cell0 = currentRow.createCell(0); // 序号
                cell0.setCellValue(snum++);

                HSSFCell cell1 = currentRow.createCell(1); // 供应商名称
                cell1.setCellValue(dailyExportOrderStatisticVo.getSupplierName());

                HSSFCell cell2 = currentRow.createCell(2); // 已完成
                cell2.setCellValue(dailyExportOrderStatisticVo.getCompletedOrderCount());

                HSSFCell cell3 = currentRow.createCell(3); // 已发货数量
                cell3.setCellValue(dailyExportOrderStatisticVo.getDeliveredOrderCount());

                HSSFCell cell4 = currentRow.createCell(4); // 待发货数量
                cell4.setCellValue(dailyExportOrderStatisticVo.getUnDeliveryOrderCount() == null ?
                        0 : dailyExportOrderStatisticVo.getUnDeliveryOrderCount());

                HSSFCell cell5 = currentRow.createCell(5); // 最早待发订单子单号
                cell5.setCellValue(dailyExportOrderStatisticVo.getUnDeliveryEarliestOrderNo() == null ?
                        "--" : dailyExportOrderStatisticVo.getUnDeliveryEarliestOrderNo());

                HSSFCell cell6 = currentRow.createCell(6); // 交易时间
                cell6.setCellValue(dailyExportOrderStatisticVo.getUnDeliveryEarliestOrderTime() == null ?
                        "--" : dailyExportOrderStatisticVo.getUnDeliveryEarliestOrderTime());

                HSSFCell cell7 = currentRow.createCell(7); // 售后数量
                cell7.setCellValue(dailyExportOrderStatisticVo.getApplyRefundCount());

                HSSFCell cell8 = currentRow.createCell(8); // 订单数量
                cell8.setCellValue(dailyExportOrderStatisticVo.getOrderDetailCount());
            }

            // 最后一行
            HSSFRow lastRow = sheet.createRow(indexRow);
            HSSFCell cell0 = lastRow.createCell(0);
            cell0.setCellValue("");

            HSSFCell cell1 = lastRow.createCell(1);
            cell1.setCellValue("总计");

            HSSFCell cell2 = lastRow.createCell(2); // 已完成
            cell2.setCellValue(totalCompletedOrderCount);

            HSSFCell cell3 = lastRow.createCell(3); // 已发货数量
            cell3.setCellValue(totalDeliveredOrderCount);

            HSSFCell cell4 = lastRow.createCell(4); // 待发货数量
            cell4.setCellValue(totalUnDeliveryOrderCount);

            HSSFCell cell5 = lastRow.createCell(5);
            cell5.setCellValue("");

            HSSFCell cell6 = lastRow.createCell(6); // 交易时间
            cell6.setCellValue("");

            HSSFCell cell7 = lastRow.createCell(7); // 售后数量
            cell7.setCellValue(totalApplyRefundOrderCount);

            HSSFCell cell8 = lastRow.createCell(8); // 订单数量
            cell8.setCellValue(totalOrderDetailCount);


            ///////// 文件名
            String fileName = "DailyStatistic" + statisticTime + ".xls";
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
            log.error("每日统计异常:{}", e.getMessage(), e);

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
                log.error("每日统计 错误:{}", e.getMessage(), e);
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
     * 导出商品开票信息
     *
     * http://localhost:8004/adminorder/export/receiptBill?startTime=2019-11-01&endTime=2019-11-30&appId=11&receiptType=1
     *
     * @param startTime  yyyy-MM-dd
     * @param endTime
     * @param receiptType
     *    1 : "balance" 惠民商城余额;  "card" 惠民优选卡; "woa" 惠民商城联机账户 这三种支付方式的发票
     *    2 : bank 中投快捷支付的发票
     *    3 :
     * @param appId
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/export/receiptBill")
    public void exportInvoiceBill(@RequestParam("startTime") String startTime,
                                  @RequestParam("endTime") String endTime,
                                  @RequestParam("receiptType") String receiptType,
                                  @RequestParam(value = "appId", required = true) String appId,
                                  HttpServletResponse response) throws Exception {
        OutputStream outputStream = null;
        // 创建HSSFWorkbook对象
        HSSFWorkbook workbook = null;

        try {
            log.info("导出商品开票信息 入参 startTime:{}, endTime:{}, appId:{}, receiptType:{}",
                    startTime, endTime, appId, receiptType);

            // 1. 参数校验
            String _stime = startTime + " 00:00:00";
            String _etime = endTime + " 23:59:59";
            Date startDate = DateUtil.parseDateTime(_stime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            Date endDate = DateUtil.parseDateTime(_etime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

            long diffDays = DateUtil.diffDays(_stime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS, _etime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            if (diffDays > 93) {
                log.warn("导出商品开票信息 查询日期范围超过三个月");
                throw new Exception("查询日期范围超过三个月");
            }

            // 发票类型
            if (StringUtils.isBlank(receiptType)) {
                log.warn("导出商品开票信息 无效的发票类型");
                throw new Exception("无效的发票类型");
            }
            PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnumByName(receiptType);
            if (paymentTypeEnum == null) {
                log.warn("导出商品开票信息 发票类型不正确");
                throw new Exception("发票类型不正确");
            }

            // appId
            if (StringUtils.isBlank(appId)) {
                log.warn("导出商品开票信息 appId为空");
                throw new Exception("未找到正确归属信息");
            }

            // 2. 执行查询&数据处理逻辑
            List<ExportReceiptBillVo> exportReceiptBillVoList = adminInvoiceService.exportInvoice(startDate, endDate, appId, paymentTypeEnum);
            log.info("导出商品开票信息 获取到需要导出数据的个数:{}", exportReceiptBillVoList.size());

            // 3. 创建导出文件对象
            // 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();
            // 创建HSSFSheet对象
            HSSFSheet sheet = workbook.createSheet("开票信息" + DateUtil.nowDate(DateUtil.DATE_YYYYMMDD)); //  加上时间

            //
            int indexRow = 0;

            // TITLE
            HSSFRow title = sheet.createRow(indexRow);

            title.createCell(0).setCellValue("商品名称");
            title.createCell(1).setCellValue("品类");
            title.createCell(2).setCellValue("销售单位");
            title.createCell(3).setCellValue("数量");
            title.createCell(4).setCellValue("实际销售单价(元)");
            title.createCell(5).setCellValue("税率");
            title.createCell(6).setCellValue("含税金额");
            title.createCell(7).setCellValue("税额");
            title.createCell(8).setCellValue("实际进货金额(元)");

            indexRow++;

            // CONTENT
            for (ExportReceiptBillVo exportReceiptBillVo : exportReceiptBillVoList) { // 遍历子订单
                HSSFRow currentRow = sheet.createRow(indexRow);

                HSSFCell cell0 = currentRow.createCell(0); // 商品名称
                cell0.setCellValue(StringUtils.isBlank(exportReceiptBillVo.getName()) ? "--" : exportReceiptBillVo.getName());

                HSSFCell cell1 = currentRow.createCell(1); // 品类
                cell1.setCellValue(StringUtils.isBlank(exportReceiptBillVo.getCategory()) ? "--" : exportReceiptBillVo.getCategory());

                HSSFCell cell2 = currentRow.createCell(2); // 销售单位
                cell2.setCellValue(StringUtils.isBlank(exportReceiptBillVo.getUnit()) ? "--" : exportReceiptBillVo.getUnit());

                HSSFCell cell3 = currentRow.createCell(3); // 数量
                cell3.setCellValue(exportReceiptBillVo.getCount() == null ? "--" : String.valueOf(exportReceiptBillVo.getCount()));

                HSSFCell cell4 = currentRow.createCell(4); // 实际销售单价（含税单价）
                cell4.setCellValue(exportReceiptBillVo.getUnitPrice() == null ?
                        "--" : CalculateUtil.converFenToYuan(exportReceiptBillVo.getUnitPrice()));

                HSSFCell cell5 = currentRow.createCell(5); // 税率
                cell5.setCellValue(StringUtils.isBlank(exportReceiptBillVo.getTaxRate()) ?
                        "--" : exportReceiptBillVo.getTaxRate());

                HSSFCell cell6 = currentRow.createCell(6); // 含税金额
                cell6.setCellValue(exportReceiptBillVo.getTotalPrice() == null ?
                        "--" : CalculateUtil.converFenToYuan(exportReceiptBillVo.getTotalPrice()));

                HSSFCell cell7 = currentRow.createCell(7); // 税额
                cell7.setCellValue(exportReceiptBillVo.getTaxPrice() == null ?
                        "--" : CalculateUtil.converFenToYuan(exportReceiptBillVo.getTaxPrice()));

                HSSFCell cell8 = currentRow.createCell(8); // 税额
                cell8.setCellValue(exportReceiptBillVo.getSprice() == null ?
                        "--" : CalculateUtil.converFenToYuan(exportReceiptBillVo.getSprice()));

                indexRow++;
            }

            // 4. 输出文件
            ///////// 文件名
            String fileName = "receipt" + DateUtil.nowDate(DateUtil.DATE_YYYYMMDD) + ".xls";

            try {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

                outputStream = response.getOutputStream();
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e) {
                log.error("导出商品开票信息 出错了:{}", e.getMessage(), e);

                throw new Exception(e);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            //
            log.debug("导出商品开票信息完成!");
        } catch (Exception e) {
            log.error("导出商品开票信息异常:{}", e.getMessage(), e);

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
                log.error("导出商品开票信息 错误:{}", e.getMessage(), e);
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

//        HSSFCell titleCell1 = titleRow.createCell(1);
//        titleCell1.setCellValue("实际支付价格 单位:元"); // 主订单

        HSSFCell titleCell1 = titleRow.createCell(1);
        titleCell1.setCellValue("优惠券价格 单位:元"); // 主订单

        HSSFCell titleCell2 = titleRow.createCell(2);
        titleCell2.setCellValue("优惠券码"); // 主订单

        HSSFCell titleCell3 = titleRow.createCell(3);
        titleCell3.setCellValue("券来源"); // 主订单

        HSSFCell titleCell4 = titleRow.createCell(4);
        titleCell4.setCellValue("运费 单位:元"); // 主订单

//        HSSFCell titleCell6 = titleRow.createCell(6);
//        titleCell6.setCellValue("余额消费 单位:元"); // 主订单
//
//        HSSFCell titleCell7 = titleRow.createCell(7);
//        titleCell7.setCellValue("惠民卡消费 单位:元"); // 主订单
//
//        HSSFCell titleCell8 = titleRow.createCell(8);
//        titleCell8.setCellValue("联机账户消费 单位:元"); // 主订单
//
//        HSSFCell titleCell9 = titleRow.createCell(9);
//        titleCell9.setCellValue("快捷支付 单位:元"); // 主订单


        // ---子订单
        HSSFCell titleCell5 = titleRow.createCell(5);
        titleCell5.setCellValue("用户id");

        HSSFCell titleCell6 = titleRow.createCell(6);
        titleCell6.setCellValue("子订单编号");

        HSSFCell titleCell7 = titleRow.createCell(7);
        titleCell7.setCellValue("子订单状态");

        HSSFCell titleCell8 = titleRow.createCell(8);
        titleCell8.setCellValue("订单支付时间");

        HSSFCell titleCell9 = titleRow.createCell(9);
        titleCell9.setCellValue("订单生成时间");

        HSSFCell titleCell10 = titleRow.createCell(10);
        titleCell10.setCellValue("品类");

        HSSFCell titleCell11 = titleRow.createCell(11);
        titleCell11.setCellValue("品牌");

        HSSFCell titleCell12 = titleRow.createCell(12);
        titleCell12.setCellValue("供应商");

        HSSFCell titleCell13 = titleRow.createCell(13);
        titleCell13.setCellValue("sku");

        HSSFCell titleCell14 = titleRow.createCell(14);
        titleCell14.setCellValue("mpu");

        HSSFCell titleCell15 = titleRow.createCell(15);
        titleCell15.setCellValue("商品名称");

        HSSFCell titleCell16 = titleRow.createCell(16);
        titleCell16.setCellValue("购买数量");

        HSSFCell titleCell17 = titleRow.createCell(17);
        titleCell17.setCellValue("结算类型");

//        HSSFCell titleCell19 = titleRow.createCell(19);
//        titleCell19.setCellValue("销售价 单位：元");

        HSSFCell titleCell18 = titleRow.createCell(18);
        titleCell18.setCellValue("sku券支付金额 单位：元");

        HSSFCell titleCell19 = titleRow.createCell(19);
        titleCell19.setCellValue("进货价 单位：元");

        HSSFCell titleCell20 = titleRow.createCell(20);
        titleCell20.setCellValue("退款金额 单位：元");

//        HSSFCell titleCell26 = titleRow.createCell(26);
//        titleCell26.setCellValue("余额退款 单位:元"); // 主订单
//
//        HSSFCell titleCell27 = titleRow.createCell(27);
//        titleCell27.setCellValue("惠民卡退款 单位:元"); // 主订单
//
//        HSSFCell titleCell28 = titleRow.createCell(28);
//        titleCell28.setCellValue("联机账户退款 单位:元"); // 主订单
//
//        HSSFCell titleCell29 = titleRow.createCell(29);
//        titleCell29.setCellValue("快捷支付退款 单位:元"); // 主订单

        HSSFCell titleCell21 = titleRow.createCell(21);
        titleCell21.setCellValue("奥弋订单号");

        HSSFCell titleCell22 = titleRow.createCell(22);
        titleCell22.setCellValue("收件人名");

        HSSFCell titleCell23 = titleRow.createCell(23);
        titleCell23.setCellValue("省");

        HSSFCell titleCell24 = titleRow.createCell(24);
        titleCell24.setCellValue("市");

        HSSFCell titleCell25 = titleRow.createCell(25);
        titleCell25.setCellValue("区");

        HSSFCell titleCell26 = titleRow.createCell(26);
        titleCell26.setCellValue("详细地址");

        HSSFCell titleCell27 = titleRow.createCell(27);
        titleCell27.setCellValue("收件人手机号");

        HSSFCell titleCell28 = titleRow.createCell(28);
        titleCell28.setCellValue("物流单号");

        HSSFCell titleCell29 = titleRow.createCell(29);
        titleCell29.setCellValue("运营平台");
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
//                HSSFCell cell1 = currentRow.createCell(1); // 实际支付价格 单位:元
                HSSFCell cell1 = currentRow.createCell(1); // 优惠券价格 单位:元
                HSSFCell cell2 = currentRow.createCell(2); // 优惠券码
                HSSFCell cell3 = currentRow.createCell(3); // 券来源
                HSSFCell cell4 = currentRow.createCell(4); // 运费 单位:元
//                HSSFCell cell6 = currentRow.createCell(6); // 余额消费 单位:元
//                HSSFCell cell7 = currentRow.createCell(7); // 惠民卡消费 单位:元
//                HSSFCell cell8 = currentRow.createCell(8); // 联机账户消费 单位:元
//                HSSFCell cell9 = currentRow.createCell(9); // 快捷支付 单位:元

                if (checkFlag.add(tradeNo)) {
                    cell0.setCellValue(tradeNo); // 主订单号
//                    cell1.setCellValue(new BigDecimal(exportOrdersVo.getPayPrice()).divide(new BigDecimal(100)).toString()); // 实际支付价格 单位:元
                    cell1.setCellValue(new BigDecimal(exportOrdersVo.getCouponPrice()).divide(new BigDecimal(100)).toString()); // 优惠券价格 单位:元
                    cell2.setCellValue(StringUtils.isBlank(exportOrdersVo.getCouponCode()) ? "-" : exportOrdersVo.getCouponCode()); // 优惠券码
                    cell3.setCellValue(StringUtils.isBlank(exportOrdersVo.getCouponSupplier()) ? "-" : exportOrdersVo.getCouponSupplier()); // 券来源
                    cell4.setCellValue(exportOrdersVo.getExpressFee()); // 运费 单位:元
//                    cell6.setCellValue(exportOrdersVo.getBalanceFee()); // 余额消费 单位:元
//                    cell7.setCellValue(exportOrdersVo.getHuiminCardFee()); // 惠民卡消费 单位:元
//                    cell8.setCellValue(exportOrdersVo.getWoaFee()); // 联机账户消费 单位:元
//                    cell9.setCellValue(exportOrdersVo.getQuickPayFee()); // 快捷支付 单位:元
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
//                        sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 5, 5));
//                        sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 6, 6));
//                        sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 7, 7));
//                        sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 8, 8));
//                        sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 9, 9));
                    }
                }

                // -- 子订单
                // 用户id
                HSSFCell cell5 = currentRow.createCell(5);
                cell5.setCellValue(exportOrdersVo.getOpenId());

                // 子订单编号，
                HSSFCell cell6 = currentRow.createCell(6);
                cell6.setCellValue(exportOrdersVo.getSubOrderId());

                // 子订单状态
                HSSFCell cell7 = currentRow.createCell(7);
                cell7.setCellValue(exportOrdersVo.getOrderDetailStatus());

                // 订单支付时间，
                HSSFCell cell8 = currentRow.createCell(8);
                cell8.setCellValue("");
                if (exportOrdersVo.getPaymentTime() != null) {
                    cell8.setCellValue(DateUtil.dateTimeFormat(exportOrdersVo.getPaymentTime(), DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
                }

                // 订单生成时间，
                HSSFCell cell9 = currentRow.createCell(9);
                cell9.setCellValue("");
                if (exportOrdersVo.getCreateTime() != null) {
                    cell9.setCellValue(DateUtil.dateTimeFormat(exportOrdersVo.getCreateTime(), DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
                }

                // 品类，
                HSSFCell cell10 = currentRow.createCell(10);
                cell10.setCellValue(exportOrdersVo.getCategory());

                // 品牌（通过mpu获取）
                HSSFCell cell11 = currentRow.createCell(11);
                cell11.setCellValue(exportOrdersVo.getBrand());

                // 供应商
                HSSFCell cell12 = currentRow.createCell(12);
                cell12.setCellValue(exportOrdersVo.getMerchantName());

                // sku，
                HSSFCell cell13 = currentRow.createCell(13);
                cell13.setCellValue(exportOrdersVo.getSku());

                // mpu，
                HSSFCell cell14 = currentRow.createCell(14);
                cell14.setCellValue(exportOrdersVo.getMpu());

                // 商品名称，
                HSSFCell cell15 = currentRow.createCell(15);
                cell15.setCellValue(exportOrdersVo.getCommodityName());

                // 购买数量 ，
                HSSFCell cell16 = currentRow.createCell(16);
                cell16.setCellValue(exportOrdersVo.getQuantity());

                // 活动 ，
                HSSFCell cell17 = currentRow.createCell(17);
                cell17.setCellValue(exportOrdersVo.getSettlementType());

//                // 销售价，(单位：元)
//                HSSFCell cell19 = currentRow.createCell(19);
//                cell19.setCellValue("无");
//                if (exportOrdersVo.getSkuPayPrice() != null) {
//                    String _price = new BigDecimal(exportOrdersVo.getSkuPayPrice()).divide(new BigDecimal(100)).toString();
//                    cell19.setCellValue(_price);
//                }

                // 券支付金额，(单位：元)
                HSSFCell cell18 = currentRow.createCell(18);
                cell18.setCellValue("无");
                if (exportOrdersVo.getSkuCouponDiscount() > 0) {
                    String _price = new BigDecimal(exportOrdersVo.getSkuCouponDiscount()).divide(new BigDecimal(100)).toString();
                    cell18.setCellValue(_price);
                }

                // 进货价 单位：元
                HSSFCell cell19 = currentRow.createCell(19);
                cell19.setCellValue("无");
                if (exportOrdersVo.getPurchasePrice() != null && exportOrdersVo.getPurchasePrice() >= 0) {
                    String _price = new BigDecimal(exportOrdersVo.getPurchasePrice()).divide(new BigDecimal(100)).toString();
                    cell19.setCellValue(_price);
                }

                // 退款金额 单位：元
                HSSFCell cell20 = currentRow.createCell(20);
                cell20.setCellValue("无");
                if (StringUtils.isNotBlank(exportOrdersVo.getOrderDetailRefundAmount())) {
                    BigDecimal _priceBigDecimal = new BigDecimal(exportOrdersVo.getOrderDetailRefundAmount()).multiply(new BigDecimal(-1));
                    String _price = _priceBigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                    cell20.setCellValue(_price);
                }

//                //余额退款
//                HSSFCell cell26 = currentRow.createCell(26);
//                cell26.setCellValue(exportOrdersVo.getBalanceRefund());
//
//                //惠民卡退款
//                HSSFCell cell27 = currentRow.createCell(27);
//                cell27.setCellValue(exportOrdersVo.getHuiminCardRefund());
//
//                //联机账户退款
//                HSSFCell cell28 = currentRow.createCell(28);
//                cell28.setCellValue(exportOrdersVo.getWoaRefund());
//
//                //快捷支付退款
//                HSSFCell cell29 = currentRow.createCell(29);
//                cell29.setCellValue(exportOrdersVo.getQuickPayRefund());

                // aoyiID
                HSSFCell cell21 = currentRow.createCell(21);
                cell21.setCellValue(exportOrdersVo.getAoyiID());

                // 收件人名
                HSSFCell cell22 = currentRow.createCell(22);
                cell22.setCellValue(exportOrdersVo.getBuyerName());

                // 省
                HSSFCell cell23 = currentRow.createCell(23);
                cell23.setCellValue(exportOrdersVo.getProvinceName());

                // 市
                HSSFCell cell24 = currentRow.createCell(24);
                cell24.setCellValue(exportOrdersVo.getCityName());

                // 区
                HSSFCell cell25 = currentRow.createCell(25);
                cell25.setCellValue(exportOrdersVo.getCountyName());

                // 详细地址
                HSSFCell cell26 = currentRow.createCell(26);
                cell26.setCellValue(exportOrdersVo.getAddress());

                // 收件人手机号
                HSSFCell cell27 = currentRow.createCell(27);
                cell27.setCellValue(exportOrdersVo.getMobile());
                // 物流单号
                HSSFCell cell28 = currentRow.createCell(28);
                cell28.setCellValue(exportOrdersVo.getLogisticsId());

                HSSFCell cell29 = currentRow.createCell(29);
                cell29.setCellValue(exportOrdersVo.getAppId());

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

    /**
     * 创建title
     *
     * @param sheet
     */
    private void createAdminTitle(HSSFSheet sheet) {
        HSSFRow titleRow = sheet.createRow(0);

        // ---主订单

        HSSFCell titleCell0 = titleRow.createCell(0);
        titleCell0.setCellValue("支付单号"); // 主订单

        HSSFCell titleCell1 = titleRow.createCell(1);
        titleCell1.setCellValue("余额消费 单位:元"); // 主订单

        HSSFCell titleCell2 = titleRow.createCell(2);
        titleCell2.setCellValue("惠民卡消费 单位:元"); // 主订单

        HSSFCell titleCell3 = titleRow.createCell(3);
        titleCell3.setCellValue("联机账户消费 单位:元"); // 主订单

        HSSFCell titleCell4 = titleRow.createCell(4);
        titleCell4.setCellValue("快捷支付 单位:元"); // 主订单

        HSSFCell titleCell5 = titleRow.createCell(5);
        titleCell5.setCellValue("支付宝支付 单位:元"); // 主订单

        HSSFCell titleCell6 = titleRow.createCell(6);
        titleCell6.setCellValue("微信支付 单位:元"); // 主订单

        HSSFCell titleCell7 = titleRow.createCell(7);
        titleCell7.setCellValue("平安支付 单位:元"); // 主订单

        HSSFCell titleCell8 = titleRow.createCell(8);
        titleCell8.setCellValue("云城支付 单位:元"); // 主订单

        /////////
        /////////

        HSSFCell titleCell9 = titleRow.createCell(9);
        titleCell9.setCellValue("主订单号"); // 主订单

        HSSFCell titleCell10 = titleRow.createCell(10);
        titleCell10.setCellValue("实际支付价格 单位:元"); // 主订单

        HSSFCell titleCell11 = titleRow.createCell(11);
        titleCell11.setCellValue("优惠券价格 单位:元"); // 主订单

        HSSFCell titleCell12 = titleRow.createCell(12);
        titleCell12.setCellValue("优惠券码"); // 主订单

        HSSFCell titleCell13 = titleRow.createCell(13);
        titleCell13.setCellValue("券来源"); // 主订单

        HSSFCell titleCell14 = titleRow.createCell(14);
        titleCell14.setCellValue("运费 单位:元"); // 主订单

        // ---子订单
        HSSFCell titleCell15 = titleRow.createCell(15);
        titleCell15.setCellValue("用户id");

        HSSFCell titleCell16 = titleRow.createCell(16);
        titleCell16.setCellValue("子订单编号");

        HSSFCell titleCell17 = titleRow.createCell(17);
        titleCell17.setCellValue("子订单状态");

        HSSFCell titleCell18 = titleRow.createCell(18);
        titleCell18.setCellValue("订单支付时间");

        HSSFCell titleCell19 = titleRow.createCell(19);
        titleCell19.setCellValue("订单生成时间");

        HSSFCell titleCell20 = titleRow.createCell(20);
        titleCell20.setCellValue("品类");

        HSSFCell titleCell21 = titleRow.createCell(21);
        titleCell21.setCellValue("品牌");

        HSSFCell titleCell22 = titleRow.createCell(22);
        titleCell22.setCellValue("供应商");

        HSSFCell titleCell23 = titleRow.createCell(23);
        titleCell23.setCellValue("sku");

        HSSFCell titleCell24 = titleRow.createCell(24);
        titleCell24.setCellValue("mpu");

        HSSFCell titleCell25 = titleRow.createCell(25);
        titleCell25.setCellValue("商品名称");

        HSSFCell titleCell26 = titleRow.createCell(26);
        titleCell26.setCellValue("购买数量");

        HSSFCell titleCell27 = titleRow.createCell(27);
        titleCell27.setCellValue("结算类型");

        HSSFCell titleCell28 = titleRow.createCell(28);
        titleCell28.setCellValue("销售价 单位：元");

        HSSFCell titleCell29 = titleRow.createCell(29);
        titleCell29.setCellValue("sku券支付金额 单位：元");

        HSSFCell titleCell30 = titleRow.createCell(30);
        titleCell30.setCellValue("进货价 单位：元");

        //退款号
        HSSFCell titleCell31 = titleRow.createCell(31);
        titleCell31.setCellValue("退款号");

        //余额退款
        HSSFCell titleCell32 = titleRow.createCell(32);
        titleCell32.setCellValue("余额退款 单位：元");

        //惠民卡退款
        HSSFCell titleCell33 = titleRow.createCell(33);
        titleCell33.setCellValue("惠民卡退款 单位：元");

        //联机账户退款
        HSSFCell titleCell34 = titleRow.createCell(34);
        titleCell34.setCellValue("联机账户退款 单位：元");

        //快捷支付退款
        HSSFCell titleCell35 = titleRow.createCell(35);
        titleCell35.setCellValue("快捷支付退款 单位：元");

        //支付宝退款
        HSSFCell titleCell36 = titleRow.createCell(36);
        titleCell36.setCellValue("支付宝退款 单位：元");

        //微信退款
        HSSFCell titleCell37 = titleRow.createCell(37);
        titleCell37.setCellValue("微信退款 单位：元");

        // 平安退款
        HSSFCell titleCell38 = titleRow.createCell(38);
        titleCell38.setCellValue("平安退款 单位：元");

        // 云城退款
        HSSFCell titleCell39 = titleRow.createCell(39);
        titleCell39.setCellValue("云城退款 单位：元");

        // aoyiID
        HSSFCell titleCell40 = titleRow.createCell(40);
        titleCell40.setCellValue("奥弋订单号");

        // 收件人名
        HSSFCell titleCell41 = titleRow.createCell(41);
        titleCell41.setCellValue("收件人名");

        HSSFCell titleCell42 = titleRow.createCell(42);
        titleCell42.setCellValue("省");

        HSSFCell titleCell43 = titleRow.createCell(43);
        titleCell43.setCellValue("市");

        HSSFCell titleCell44 = titleRow.createCell(44);
        titleCell44.setCellValue("区");

        HSSFCell titleCell45 = titleRow.createCell(45);
        titleCell45.setCellValue("详细地址");

        HSSFCell titleCell46 = titleRow.createCell(46);
        titleCell46.setCellValue("收件人手机号");

        HSSFCell titleCell47 = titleRow.createCell(47);
        titleCell47.setCellValue("物流单号");

        HSSFCell titleCell48 = titleRow.createCell(48);
        titleCell48.setCellValue("备注信息");

        HSSFCell titleCell49 = titleRow.createCell(49);
        titleCell49.setCellValue("运营平台");

        HSSFCell titleCell50 = titleRow.createCell(50);
        titleCell50.setCellValue("子订单外部订单号");
    }

    /**
     * 将需要导出的数据转成主订单维度的map key:tradeno
     *
     * 由于ExportOrdersVo是子订单维度，所以该转换是一个主订单tradeNo为维度的 子订单集合
     *
     * @param exportOrdersVoList
     * @return
     */
    private Map<String, List<ExportOrdersVo>> convertToExportOrdersVoMapByPay(List<ExportOrdersVo> exportOrdersVoList) {
        Map<String, List<ExportOrdersVo>> exprotOrdersVoMap = new HashMap<>();

        for (ExportOrdersVo exportOrdersVo : exportOrdersVoList) {
            String paymentNo = exportOrdersVo.getPaymentNo();
            List<ExportOrdersVo> _exportOrdersVoList = exprotOrdersVoMap.get(paymentNo);
            if (_exportOrdersVoList == null) {
                _exportOrdersVoList = new ArrayList<>();
                exprotOrdersVoMap.put(paymentNo, _exportOrdersVoList);
            }
            _exportOrdersVoList.add(exportOrdersVo);
        }

        return exprotOrdersVoMap;
    }

    /**
     * 组装业务数据
     *
     * @param sheet
     * @param exportOrdersVoMap key: tradeNo
     */
    private void createAdminContent(HSSFSheet sheet, Map<String, List<ExportOrdersVo>> exportOrdersVoMap) {
        Set<String> paymentNoSet = exportOrdersVoMap.keySet();
        int currentRowNum = 1; // 行号
        Set<String> checkFlag = new HashSet<>(); // 该变量标识主订单的遍历，主要用于主订单号和运费列的显示标志

        for (String paymentNo : paymentNoSet) {
            // 子订单
            List<ExportOrdersVo> exportOrdersVoList = exportOrdersVoMap.get(paymentNo);

            // 将需要导出的数据转成主订单维度的map key:tradeno
            Map<String, List<ExportOrdersVo>> stringListMap = convertToExportOrdersVoMap(exportOrdersVoList);
                Set<String> tradeNoSet = stringListMap.keySet();
                // 该子订单记录集合在excel中的起始行
                int startLineNum = currentRowNum;
                int endLineNum = currentRowNum + exportOrdersVoList.size() - 1;

                Set<String> tradeFlag = new HashSet<>();
                for (String tradeNo : tradeNoSet) { // 遍历主订单
                    List<ExportOrdersVo> ordersVos = stringListMap.get(tradeNo);
                    int tradeNoCount = ordersVos.size();

                    for (int num = 0; num < tradeNoCount; num++) { // 遍历子订单
                        HSSFRow currentRow = sheet.createRow(currentRowNum);
                        ExportOrdersVo ordersVo = ordersVos.get(num);
                        // -- 主订单
                        HSSFCell cell0 = currentRow.createCell(0); // 支付单号
                        HSSFCell cell1 = currentRow.createCell(1); // 余额消费 单位:元
                        HSSFCell cell2 = currentRow.createCell(2); // 惠民卡消费 单位:元
                        HSSFCell cell3 = currentRow.createCell(3); // 联机账户消费 单位:元
                        HSSFCell cell4 = currentRow.createCell(4); // 快捷支付 单位:元
                        HSSFCell cell5 = currentRow.createCell(5); // 支付宝 单位:元
                        HSSFCell cell6 = currentRow.createCell(6); // 微信 单位:元
                        HSSFCell cell7 = currentRow.createCell(7); // 平安 单位:元
                        HSSFCell cell8 = currentRow.createCell(8); // 云城 单位:元

                        if (checkFlag.add(paymentNo)) {
                            cell0.setCellValue(paymentNo); // 主订单号
                            cell1.setCellValue(ordersVo.getBalanceFee()); // 余额消费 单位:元
                            cell2.setCellValue(ordersVo.getHuiminCardFee()); // 惠民卡消费 单位:元
                            cell3.setCellValue(ordersVo.getWoaFee()); // 联机账户消费 单位:元
                            cell4.setCellValue(ordersVo.getQuickPayFee()); // 快捷支付 单位:元
                            cell5.setCellValue(ordersVo.getFcalipayFee()); // 支付宝 单位:元
                            cell6.setCellValue(ordersVo.getFcwxFee()); // 微信 单位:元
                            cell7.setCellValue(ordersVo.getPinganFee()); // 平安 单位:元
                            cell8.setCellValue(ordersVo.getYunchengFee()); // 云城 单位:元
                        }


                        ///////

                        /////


                        /////



                        HSSFCell cell9 = currentRow.createCell(9); // 主订单号
                        HSSFCell cell10 = currentRow.createCell(10); // 实际支付价格 单位:元
                        HSSFCell cell11 = currentRow.createCell(11); // 优惠券价格 单位:元
                        HSSFCell cell12 = currentRow.createCell(12); //  优惠券码
                        HSSFCell cell13 = currentRow.createCell(13); // 券来源
                        HSSFCell cell14 = currentRow.createCell(14); // 运费 单位:元

                        if (tradeFlag.add(tradeNo)) {
                            cell9.setCellValue(tradeNo); // 券来源
                            cell10.setCellValue(new BigDecimal(ordersVo.getPayPrice()).divide(new BigDecimal(100)).toString()); // 运费 单位:元
                            cell11.setCellValue(new BigDecimal(ordersVo.getCouponPrice()).divide(new BigDecimal(100)).toString()); // 余额消费 单位:元
                            cell12.setCellValue(StringUtils.isBlank(ordersVo.getCouponCode()) ? "-" : ordersVo.getCouponCode()); // 惠民卡消费 单位:元
                            cell13.setCellValue(StringUtils.isBlank(ordersVo.getCouponSupplier()) ? "-" : ordersVo.getCouponSupplier()); // 联机账户消费 单位:元
                            cell14.setCellValue(ordersVo.getExpressFee()); // 快捷支付 单位:元
                        } else {
                            // 主订单号
                            //cell0.setCellValue("");
                            // 运费
                            //cell1.setCellValue("");
                            // 实际支付价格
                            // 优惠券价格
                            // 优惠码

                            // 如果子订单数大于1， 并且子订单已遍历完，则需要合并'主订单号'和'运费' 列
                            if (tradeNoCount > 1 && (num + 1) == tradeNoCount) { //
                                // 该子订单记录集合在excel中的起始行
                                int startNum = currentRowNum - tradeNoCount + 1;
                                // 该子订单记录集合在excel中的结束行
                                int endNum = currentRowNum;

                                sheet.addMergedRegion(new CellRangeAddress(startNum, endNum, 9, 9));
                                sheet.addMergedRegion(new CellRangeAddress(startNum, endNum, 10, 10));
                                sheet.addMergedRegion(new CellRangeAddress(startNum, endNum, 11, 11));
                                sheet.addMergedRegion(new CellRangeAddress(startNum, endNum, 12, 12));
                                sheet.addMergedRegion(new CellRangeAddress(startNum, endNum, 13, 13));
                                sheet.addMergedRegion(new CellRangeAddress(startNum, endNum, 14, 14));
                            }
                        }

                        // -- 子订单
                        // 用户id
                        HSSFCell cell15 = currentRow.createCell(15);
                        cell15.setCellValue(ordersVo.getOpenId());

                        // 子订单编号，
                        HSSFCell cell16 = currentRow.createCell(16);
                        cell16.setCellValue(ordersVo.getSubOrderId());

                        // 子订单状态
                        HSSFCell cell17 = currentRow.createCell(17);
                        cell17.setCellValue(ordersVo.getOrderDetailStatus());

                        // 订单支付时间，
                        HSSFCell cell18 = currentRow.createCell(18);
                        cell18.setCellValue("");
                        if (ordersVo.getPaymentTime() != null) {
                            cell18.setCellValue(DateUtil.dateTimeFormat(ordersVo.getPaymentTime(), DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
                        }

                        // 订单生成时间，
                        HSSFCell cell19 = currentRow.createCell(19);
                        cell19.setCellValue("");
                        if (ordersVo.getCreateTime() != null) {
                            cell19.setCellValue(DateUtil.dateTimeFormat(ordersVo.getCreateTime(), DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
                        }

                        // 品类，
                        HSSFCell cell20 = currentRow.createCell(20);
                        cell20.setCellValue(ordersVo.getCategory());

                        // 品牌（通过mpu获取）
                        HSSFCell cell21 = currentRow.createCell(21);
                        cell21.setCellValue(ordersVo.getBrand());

                        // 供应商
                        HSSFCell cell22 = currentRow.createCell(22);
                        cell22.setCellValue(ordersVo.getMerchantName());

                        // sku，
                        HSSFCell cell23 = currentRow.createCell(23);
                        cell23.setCellValue(ordersVo.getSku());

                        // mpu，
                        HSSFCell cell24 = currentRow.createCell(24);
                        cell24.setCellValue(ordersVo.getMpu());

                        // 商品名称，
                        HSSFCell cell25 = currentRow.createCell(25);
                        cell25.setCellValue(ordersVo.getCommodityName());

                        // 购买数量 ，
                        HSSFCell cell26 = currentRow.createCell(26);
                        cell26.setCellValue(ordersVo.getQuantity());

                        // 结算类型
                        HSSFCell cell27 = currentRow.createCell(27);
                        cell27.setCellValue(ordersVo.getSettlementType());

                        // 销售价，(单位：元)
                        HSSFCell cell28 = currentRow.createCell(28);
                        cell28.setCellValue("无");
                        if (ordersVo.getSkuPayPrice() != null) {
                            String _price = new BigDecimal(ordersVo.getSkuPayPrice()).divide(new BigDecimal(100)).toString();
                            cell28.setCellValue(_price);
                        }

                        // 券支付金额，(单位：元)
                        HSSFCell cell29 = currentRow.createCell(29);
                        cell29.setCellValue("无");
                        if (ordersVo.getSkuCouponDiscount() > 0) {
                            String _price = new BigDecimal(ordersVo.getSkuCouponDiscount()).divide(new BigDecimal(100)).toString();
                            cell29.setCellValue(_price);
                        }

                        // 进货价 单位：元
                        HSSFCell cell30 = currentRow.createCell(30);
                        cell30.setCellValue("无");
                        if (ordersVo.getPurchasePrice() != null && ordersVo.getPurchasePrice() >= 0) {
                            String _price = new BigDecimal(ordersVo.getPurchasePrice()).divide(new BigDecimal(100)).toString();
                            cell30.setCellValue(_price);
                        }

//                // 退款金额 单位：元
//                HSSFCell cell26 = currentRow.createCell(26);
//                cell26.setCellValue("无");
//                if (StringUtils.isNotBlank(exportOrdersVo.getOrderDetailRefundAmount())) {
//                    BigDecimal _priceBigDecimal = new BigDecimal(exportOrdersVo.getOrderDetailRefundAmount()).multiply(new BigDecimal(-1));
//                    String _price = _priceBigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
//                    cell26.setCellValue(_price);
//                }

                            //退款号
                        HSSFCell cell31 = currentRow.createCell(31);
                        cell31.setCellValue(ordersVo.getRefundNo());

                        //余额退款
                        HSSFCell cell32 = currentRow.createCell(32);
                        cell32.setCellValue(ordersVo.getBalanceRefund());

                        //惠民卡退款
                        HSSFCell cell33 = currentRow.createCell(33);
                        cell33.setCellValue(ordersVo.getHuiminCardRefund());

                        //联机账户退款
                        HSSFCell cell34 = currentRow.createCell(34);
                        cell34.setCellValue(ordersVo.getWoaRefund());

                        //快捷支付退款
                        HSSFCell cell35 = currentRow.createCell(35);
                        cell35.setCellValue(ordersVo.getQuickPayRefund());

                        //支付宝退款
                        HSSFCell cell36 = currentRow.createCell(36);
                        cell36.setCellValue(ordersVo.getFcalipayRefund());

                        //微信退款
                        HSSFCell cell37 = currentRow.createCell(37);
                        cell37.setCellValue(ordersVo.getFcwxRefund());

                        // 平安退款
                        HSSFCell cell38 = currentRow.createCell(38);
                        cell38.setCellValue(ordersVo.getPinganRefund());

                        // 云城退款
                        HSSFCell cell39 = currentRow.createCell(39);
                        cell39.setCellValue(ordersVo.getYunchengRefund());

                        // aoyiID
                        HSSFCell cell40 = currentRow.createCell(40);
                        cell40.setCellValue(ordersVo.getAoyiID());

                        // 收件人名
                        HSSFCell cell41 = currentRow.createCell(41);
                        cell41.setCellValue(ordersVo.getBuyerName());

                        // 省
                        HSSFCell cell42 = currentRow.createCell(42);
                        cell42.setCellValue(ordersVo.getProvinceName());

                        // 市
                        HSSFCell cell43 = currentRow.createCell(43);
                        cell43.setCellValue(ordersVo.getCityName());

                        // 区
                        HSSFCell cell44 = currentRow.createCell(44);
                        cell44.setCellValue(ordersVo.getCountyName());

                        // 详细地址
                        HSSFCell cell45 = currentRow.createCell(45);
                        cell45.setCellValue(ordersVo.getAddress());

                        // 收件人手机号
                        HSSFCell cell46 = currentRow.createCell(46);
                        cell46.setCellValue(ordersVo.getMobile());

                        // 物流单号
                        HSSFCell cell47 = currentRow.createCell(47);
                        cell47.setCellValue(ordersVo.getLogisticsId());

                        // 备注信息
                        HSSFCell cell48 = currentRow.createCell(48);
                        cell48.setCellValue(ordersVo.getRemark());

                        // 运营平台
                        HSSFCell cell49 = currentRow.createCell(49);
                        cell49.setCellValue(ordersVo.getAppId());

                        // 子订单外部订单号
                        HSSFCell cell50 = currentRow.createCell(50);
                        cell50.setCellValue(ordersVo.getThirdOrderSn());

                        //
                        currentRowNum++;
                    }

                }
                if(startLineNum < endLineNum){
                    sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 0, 0));
                    sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 1, 1));
                    sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 2, 2));
                    sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 3, 3));
                    sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 4, 4));
                    sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 5, 5));
                    sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 6, 6));
                    sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 7, 7));
                    sheet.addMergedRegion(new CellRangeAddress(startLineNum, endLineNum, 8, 8));
                }
            }
    }

    public static void main(String args[]) {
        // new AdminOrderController().excelPrint();
    }

}
