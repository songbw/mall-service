package com.fengchao.equity.controller;

import com.fengchao.equity.bean.*;
import com.fengchao.equity.bean.vo.ExportPromotionVo;
import com.fengchao.equity.model.PromotionX;
import com.fengchao.equity.service.PromotionService;
import com.fengchao.equity.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/adminPromotion", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminPromotionController {

    @Autowired
    private PromotionService service;

    @PostMapping("create")
    public OperaResult createPromotion(@RequestBody PromotionX bean, OperaResult result){
        log.info("创建活动参数 入参:{}", JSONUtil.toJsonString(bean));
        service.createPromotion(bean);
        result.getData().put("promotionId", bean.getId());
        return result;
    }

    @GetMapping("find")
    public OperaResult findPromotion(QueryBean bean, OperaResult result){
        result.getData().put("result", service.findPromotion(bean.getOffset(), bean.getLimit(), bean.getAppId()));
        return result;
    }

    @GetMapping("findById")
    public OperaResult findPromotionById(Integer id, OperaResult result){
        result.getData().put("result", service.findPromotionById(id));
        return result;
    }

    @PostMapping("update")
    public OperaResult updatePromotion(@RequestBody PromotionX bean, OperaResult result){
        log.info("更新活动参数 入参:{}", JSONUtil.toJsonString(bean));
        PromotionResult promotionResult = service.updatePromotion(bean);
        if(promotionResult.getNum() == 2){
            result.setCode(500);
            result.setMsg("同时间有上线商品");
            result.getData().put("mpus", promotionResult.getMpus());
        }else if(promotionResult.getNum() == 3){
            result.setCode(501);
            result.setMsg("当天只能有1个秒杀活动");
            result.getData().put("promotionId", promotionResult.getPromotionId());
        }else{
            result.getData().put("result", promotionResult.getNum());
        }
        return result;
    }

    @PostMapping("search")
    public OperaResult searchPromotion(@RequestBody PromotionBean bean, OperaResult result){
        result.getData().put("result", service.searchPromotion(bean));
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deletePromotion(Integer id, OperaResult result){
        int num = service.deletePromotion(id);
        if(num == 0){
            result.setCode(700101);
            result.setMsg("删除失败");
        }else if(num == 1){
            result.getData().put("result", num);
        }else if(num == 2){
            result.setCode(700102);
            result.setMsg("活动已发布，不能删除");
        }

        return result;
    }

    @PostMapping("createContent")
    public OperaResult createContent(@RequestBody PromotionX bean, OperaResult result){
        log.info("创建活动内容参数 入参:{}", JSONUtil.toJsonString(bean));
        int num = service.createContent(bean);
        if(num != 1){
            result.setCode(500);
            result.setMsg("有商品mpu为空");
        }
        result.getData().put("result", num);
        return result;
    }

    @PutMapping("updateContent")
    public OperaResult updateContent(@RequestBody PromotionX bean, OperaResult result){
        log.info("更新活动内容参数 入参:{}", JSONUtil.toJsonString(bean));
        result.getData().put("result",service.updateContent(bean));
        return result;
    }

    @DeleteMapping("deleteContent")
    public OperaResult deleteContent(@RequestBody PromotionX bean, OperaResult result){
        log.info("删除活动内容参数 入参:{}", JSONUtil.toJsonString(bean));
        result.getData().put("result", service.deleteContent(bean));
        return result;
    }

    @GetMapping("release")
    public OperaResult findReleasePromotion(@Param("pageNo") Integer pageNo,
                                            @Param("pageSize") Integer pageSize,
                                            @Param("dailySchedule") Boolean dailySchedule,
                                            @Param("name") String name,
                                            @Param("appId") String appId,
                                            OperaResult result){
        result.getData().put("result", service.findReleasePromotion(pageNo, pageSize, dailySchedule, name, appId));
        return result;
    }


    /**
     * 导出活动异常价格商品
     *
     * @param response
     * @throws Exception
     *
     */
    @GetMapping(value = "/export")
    public void exportPromotionMpu(ExportPromotionVo bean, HttpServletResponse response) throws Exception {
        OutputStream outputStream = null;
        // 创建HSSFWorkbook对象
        HSSFWorkbook workbook = null;

        try {
            // 1.根据条件获统计数据
            List<ExportPromotionInfo> promotionInfos = service.exportPromotionMpu(bean);
            // 创建HSSFWorkbook对象
            workbook = new HSSFWorkbook();
            // 创建HSSFSheet对象
            HSSFSheet sheet = workbook.createSheet("价格异常导出");

            //
            int indexRow = 0;
            // TITLE
            HSSFRow titleRow = sheet.createRow(indexRow);
            indexRow = indexRow + 1;

            HSSFCell titleCell0 = titleRow.createCell(0);
            titleCell0.setCellValue("活动ID"); // 序号

            HSSFCell titleCell1 = titleRow.createCell(1);
            titleCell1.setCellValue("活动名称"); // 活动名称

            HSSFCell titleCell2 = titleRow.createCell(2);
            titleCell2.setCellValue("状态"); // 状态

            HSSFCell titleCell3 = titleRow.createCell(3);
            titleCell3.setCellValue("商品SKU"); // 商品SKU

            HSSFCell titleCell4 = titleRow.createCell(4);
            titleCell4.setCellValue("商品名"); // 商品名

            HSSFCell titleCell5 = titleRow.createCell(5);
            titleCell5.setCellValue("进货价");

            HSSFCell titleCell6 = titleRow.createCell(6);
            titleCell6.setCellValue("销售价");

            HSSFCell titleCell7 = titleRow.createCell(7);
            titleCell7.setCellValue("第三方平台价");

            HSSFCell titleCell8 = titleRow.createCell(8);
            titleCell8.setCellValue("活动价");

            HSSFCell titleCell9 = titleRow.createCell(9);
            titleCell9.setCellValue("运营平台");

            // CONTENT
            for (ExportPromotionInfo info : promotionInfos) { // 遍历子订单
                HSSFRow currentRow = sheet.createRow(indexRow);
                indexRow = indexRow + 1;

                HSSFCell cell0 = currentRow.createCell(0); // 序号
                cell0.setCellValue(info.getId());

                HSSFCell cell1 = currentRow.createCell(1); // 活动名称
                cell1.setCellValue(info.getName());

                HSSFCell cell2 = currentRow.createCell(2); // 状态
                Integer status = info.getStatus();
                if(status == 1){
                    cell2.setCellValue("编辑中");
                }else if(status == 3){
                    cell2.setCellValue("未开始");
                }else if(status == 4){
                    cell2.setCellValue("进行中");
                }

                HSSFCell cell3 = currentRow.createCell(3); // 商品SKU
                cell3.setCellValue(info.getSkuid());

                HSSFCell cell4 = currentRow.createCell(4); // 商品名
                cell4.setCellValue(info.getSkuName());

                HSSFCell cell5 = currentRow.createCell(5); // 进货价
                cell5.setCellValue(info.getSprice());

                HSSFCell cell6 = currentRow.createCell(6); // 销售价
                cell6.setCellValue(info.getPrice());

                HSSFCell cell7 = currentRow.createCell(7); // 第三方价格
                cell7.setCellValue(info.getComparePrice());

                HSSFCell cell8 = currentRow.createCell(8); // 活动价
                cell8.setCellValue(info.getDiscount());

                HSSFCell cell9 = currentRow.createCell(9); // 运营平台
                cell9.setCellValue(info.getAppName());
            }


            ///////// 文件名
            String fileName = "DailyStatistic" + ".xls";
            // 3. 输出文件
            try {
//                response.setHeader("content-type", "application/octet-stream");
//                response.setContentType("application/octet-stream");
//                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
//
//                outputStream = response.getOutputStream();
                outputStream = new FileOutputStream("D://" + fileName);
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
}
