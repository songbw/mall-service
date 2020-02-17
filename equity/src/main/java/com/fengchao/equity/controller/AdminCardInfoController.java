package com.fengchao.equity.controller;

import com.fengchao.equity.bean.CardInfoBean;
import com.fengchao.equity.bean.CardTicketBean;
import com.fengchao.equity.bean.ExportCardBean;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.bean.page.PageableData;
import com.fengchao.equity.model.CardInfo;
import com.fengchao.equity.model.CardInfoX;
import com.fengchao.equity.model.CardTicket;
import com.fengchao.equity.service.CardInfoService;
import com.fengchao.equity.service.CardTicketService;
import com.fengchao.equity.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/adminCard", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminCardInfoController {


    @Autowired
    private CardInfoService service;
    @Autowired
    private CardTicketService ticketService;

    @PostMapping("create")
    public OperaResult createCardInfo(@RequestBody CardInfoBean bean, OperaResult result){
        log.info("创建createCardTicket礼品券参数 入参:{}", JSONUtil.toJsonString(bean));
        result.getData().put("result",service.createCardInfo(bean));
        return result;
    }

    @GetMapping("find")
    public OperaResult findCardInfo(CardInfoBean bean, OperaResult result){
        PageableData<CardInfo> coupon = service.findCardInfo(bean);
        result.getData().put("result", coupon);
        return result;
    }

//    @PostMapping("search")
//    public OperaResult serachCoupon(@RequestBody CouponSearchBean bean, OperaResult result){
//        PageableData<CardTicket> pageBean = service.serachCoupon(bean);
//        result.getData().put("result",pageBean);
//        return result;
//    }

    @GetMapping("findById")
    public OperaResult findByCardId(CardInfoBean bean, OperaResult result){
        result.getData().put("result",service.findByCardId(bean));
        return result;
    }

    @PutMapping("update")
    public OperaResult updateCardInfo(@RequestBody CardInfo bean, OperaResult result){
        result.getData().put("result",service.updateCardInfo(bean));
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deleteCardInfo(Integer id, OperaResult result){
        result.getData().put("result",service.deleteCardInfo(id));
        return result;
    }

    @PostMapping("assigns")
    public OperaResult assignsCardTicket(@RequestBody CardTicketBean bean, OperaResult result){
        result.getData().put("result",ticketService.assignsCardTicket(bean));
        return result;
    }

    @PutMapping("activate")
    public OperaResult activateCardTicket(@RequestBody List<CardTicket> beans, OperaResult result){
        result.getData().put("result",ticketService.activatesCardTicket(beans));
        return result;
    }


    @GetMapping("export")
    public void  exportCardTicket(ExportCardBean bean, HttpServletResponse response) throws Exception {
        OutputStream outputStream = null;
        // 创建HSSFWorkbook对象
        HSSFWorkbook workbook = null;

        SimpleDateFormat sf=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

        try{
            log.info("导出提货卡信息 入参:{}", JSONUtil.toJsonString(bean));
            List<CardInfoX> infoXList = ticketService.exportCardTicket(bean);
            Map<String, String> platformMap = ticketService.selectPlatformAll();

            // 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();

            HSSFSheet sheet = workbook.createSheet("提货卡");
            HSSFRow titleRow = sheet.createRow(0);

            //
            HSSFCell titleCell0 = titleRow.createCell(0);
            titleCell0.setCellValue("ID"); // id

            HSSFCell titleCell1 = titleRow.createCell(1);
            titleCell1.setCellValue("名称");

            HSSFCell titleCell2 = titleRow.createCell(2);
            titleCell2.setCellValue("卡金额"); // 卡金额

            HSSFCell titleCell3 = titleRow.createCell(3);
            titleCell3.setCellValue("卡类型"); // 卡类型

            HSSFCell titleCell4 = titleRow.createCell(4);
            titleCell4.setCellValue("状态"); // 状态

            HSSFCell titleCell5 = titleRow.createCell(5);
            titleCell5.setCellValue("有效期天数"); // 有效期天数

            HSSFCell titleCell6 = titleRow.createCell(6);
            titleCell6.setCellValue("创建时间"); // 创建时间

            HSSFCell titleCell7 = titleRow.createCell(7);
            titleCell7.setCellValue("优惠券ID"); // 优惠券

            HSSFCell titleCell8 = titleRow.createCell(8);
            titleCell8.setCellValue("卡券ID"); // 卡券ID

            HSSFCell titleCell9 = titleRow.createCell(9);
            titleCell9.setCellValue("卡号"); // 卡号

            HSSFCell titleCell10 = titleRow.createCell(10);
            titleCell10.setCellValue("密码"); // 密码

            HSSFCell titleCell11 = titleRow.createCell(11);
            titleCell11.setCellValue("卡券状态"); // 卡券状态

            HSSFCell titleCell12 = titleRow.createCell(12);
            titleCell12.setCellValue("用户ID"); // 用户ID

            HSSFCell titleCell13 = titleRow.createCell(13);
            titleCell13.setCellValue("创建时间");

            HSSFCell titleCell14 = titleRow.createCell(14);
            titleCell14.setCellValue("激活时间");

            HSSFCell titleCell15 = titleRow.createCell(15);
            titleCell15.setCellValue("使用时间");

            HSSFCell titleCell16 = titleRow.createCell(16);
            titleCell16.setCellValue("备注信息");

            HSSFCell titleCell17 = titleRow.createCell(17);
            titleCell17.setCellValue("运营平台");

            //组装内容
            int currentRowNum = 1;
            HSSFRow currentRow = sheet.createRow(currentRowNum);
            for (CardInfoX cardInfo:infoXList){
                HSSFCell cell0 = currentRow.createCell(0); // id
                HSSFCell cell1 = currentRow.createCell(1); // 名称
                HSSFCell cell2 = currentRow.createCell(2); // 卡金额
                HSSFCell cell3 = currentRow.createCell(3); // 卡类型
                HSSFCell cell4 = currentRow.createCell(4); // 状态
                HSSFCell cell5 = currentRow.createCell(5); // 有效期天数
                HSSFCell cell6 = currentRow.createCell(6); // 创建时间
                HSSFCell cell7 = currentRow.createCell(7); // 优惠券ID

                cell0.setCellValue(cardInfo.getId());
                cell1.setCellValue(cardInfo.getName());
                cell2.setCellValue(cardInfo.getAmount());
                Short type = cardInfo.getType();
                if(type == 1){
                    cell3.setCellValue("礼包券");
                }else if(type == 2){
                    cell3.setCellValue("代金券");
                }
                Short status = cardInfo.getStatus();
                if(status == 1){
                    cell4.setCellValue("编辑中");
                }else if(status == 2){
                    cell4.setCellValue("已发布");
                }
                cell5.setCellValue(cardInfo.getEffectiveDays() == null ? "" : String.valueOf(cardInfo.getEffectiveDays()));
                cell6.setCellValue(cardInfo.getCreateTime() == null ? "" : sf.format(cardInfo.getCreateTime()));
                cell7.setCellValue(cardInfo.getCouponIds().toString());

                int count = cardInfo.getTickets().size();
                if(count < 1){
                    currentRowNum++;
                    currentRow = sheet.createRow(currentRowNum);
                }else {
                    for(int num = 0; num < count; num ++){
                        CardTicket ticket = cardInfo.getTickets().get(num);
                        HSSFCell cell8 = currentRow.createCell(8); // 卡券ID
                        cell8.setCellValue(ticket.getId());

                        HSSFCell cell9 = currentRow.createCell(9); // 卡号
                        cell9.setCellValue(ticket.getCard());

                        HSSFCell cell10 = currentRow.createCell(10); // 密码
                        cell10.setCellValue(ticket.getPassword());

                        HSSFCell cell11 = currentRow.createCell(11); // 卡券状态
                        Short statusTicket = ticket.getStatus();
                        if(statusTicket == 1){
                            cell11.setCellValue("创建");
                        }else if(statusTicket == 2){
                            cell11.setCellValue("已激活");
                        }else if(statusTicket == 3){
                            cell11.setCellValue("已使用");
                        }else if(statusTicket == 4){
                            cell11.setCellValue("已过期");
                        }

                        HSSFCell cell12 = currentRow.createCell(12); // 用户ID
                        cell12.setCellValue(ticket.getOpenId());

                        HSSFCell cell13 = currentRow.createCell(13); // 创建时间
                        cell13.setCellValue(ticket.getCreateTime() == null ? "" : sf.format(ticket.getCreateTime()));

                        HSSFCell cell14 = currentRow.createCell(14); // 激活时间
                        cell14.setCellValue(ticket.getActivateTime() == null ? "" : sf.format(ticket.getActivateTime()));

                        HSSFCell cell15 = currentRow.createCell(15); // 使用时间
                        cell15.setCellValue(ticket.getConsumedTime() == null ? "" : sf.format(ticket.getConsumedTime()));

                        HSSFCell cell16 = currentRow.createCell(16); // 备注信息
                        cell16.setCellValue(ticket.getRemark());

                        HSSFCell cell17 = currentRow.createCell(17); // 运营平台
                        if(StringUtils.isNotEmpty(cardInfo.getAppId())){
                            String platformName = platformMap.get(cardInfo.getAppId());
                            cell17.setCellValue(platformName);
                        }

                        // 如果子订单数大于1， 并且子订单已遍历完，则需要合并'主订单号'和'运费' 列
                        if (count > 1 && (num + 1) == count) { //
                            // 该子订单记录集合在excel中的起始行
                            int startNum = currentRowNum - count + 1;
                            // 该子订单记录集合在excel中的结束行
                            int endNum = currentRowNum;

                            sheet.addMergedRegion(new CellRangeAddress(startNum, endNum, 0, 0));
                            sheet.addMergedRegion(new CellRangeAddress(startNum, endNum, 1, 1));
                            sheet.addMergedRegion(new CellRangeAddress(startNum, endNum, 2, 2));
                            sheet.addMergedRegion(new CellRangeAddress(startNum, endNum, 3, 3));
                            sheet.addMergedRegion(new CellRangeAddress(startNum, endNum, 4, 4));
                            sheet.addMergedRegion(new CellRangeAddress(startNum, endNum, 5, 5));
                            sheet.addMergedRegion(new CellRangeAddress(startNum, endNum, 6, 6));
                            sheet.addMergedRegion(new CellRangeAddress(startNum, endNum, 7, 7));
                        }
                        currentRowNum++;
                        currentRow = sheet.createRow(currentRowNum);
                    }
                }
                }

            ///////// 文件名
            String fileName = "admin_Card" + ".xls";

            // 3. 输出文件
            try {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

                outputStream = response.getOutputStream();
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e) {
                log.error("导出提货券文件 出错了:{}", e.getMessage(), e);

                throw new Exception(e);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }catch (Exception e) {
            log.error("导出提货券信息异常:{}", e.getMessage(), e);
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
                log.error("导出提货券信息 错误:{}", e.getMessage(), e);
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
