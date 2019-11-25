package com.fengchao.sso.service.impl;

import com.fengchao.sso.bean.*;
import com.fengchao.sso.dao.BalanceConsumeAndRefundDao;
import com.fengchao.sso.dao.BalanceDao;
import com.fengchao.sso.mapper.BalanceDetailMapper;
import com.fengchao.sso.mapper.BalanceMapper;
import com.fengchao.sso.mapper.BalanceXMapper;
import com.fengchao.sso.model.Balance;
import com.fengchao.sso.model.BalanceDetail;
import com.fengchao.sso.service.IBalanceService;
import com.fengchao.sso.util.DateUtil;
import com.fengchao.sso.util.RedisDAO;
import com.github.pagehelper.PageInfo;
import io.micrometer.core.instrument.util.StringEscapeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class BalanceServiceImpl implements IBalanceService {

    @Autowired
    private BalanceDao balanceDao;
    @Autowired
    private BalanceMapper mapper;
    @Autowired
    private BalanceDetailMapper detailMapper;
    @Autowired
    private BalanceXMapper balanceMapper;
    @Autowired
    private BalanceConsumeAndRefundDao balanceConsumeAndRefundDao;
    @Autowired
    private RedisDAO redisDAO;

    @Override
    public OperaResponse add(Balance bean) {
        OperaResponse response = new OperaResponse();
        if (StringUtils.isEmpty(bean.getOpenId())) {
            response.setCode(900401);
            response.setMsg("openId 不能为Null");
            return response;
        }
        Balance temp = balanceDao.selectBalanceByOpenId(bean.getOpenId()) ;
        if (temp != null) {
            response.setCode(900402);
            response.setMsg("openId 已存在");
            return response;
        }
        Date date = new Date();
        bean.setCreatedAt(date);
        bean.setUpdatedAt(date);
        int id = mapper.insertSelective(bean) ;
        // 记录充值初始化记录
        BalanceDetail detail = new BalanceDetail();
        detail.setCreatedAt(date);
        detail.setUpdatedAt(date);
        detail.setType(2);
        detail.setStatus(1);
        detail.setSaleAmount(bean.getAmount());
        detail.setBalanceId(id);
        detail.setOpenId(bean.getOpenId());
        detailMapper.insertSelective(detail);
        response.setData(id);
        return response;
    }

    @Override
    public OperaResponse update(BalanceBean bean) {
        OperaResponse response = new OperaResponse();
        if (bean.getId() == null || bean.getId() <= 0) {
            response.setCode(900402);
            response.setMsg("id 必须大于0");
            return response;
        }
        Balance temp = balanceMapper.selectForUpdateByPrimaryKey(bean.getId()) ;
        Date date = new Date();
        temp.setUpdatedAt(date);
        temp.setAmount(temp.getAmount() + bean.getAmount());
        mapper.updateByPrimaryKeySelective(temp) ;
        // 记录充值初始化记录
        BalanceDetail detail = new BalanceDetail();
        detail.setCreatedAt(date);
        detail.setUpdatedAt(date);
        detail.setOpenId(temp.getOpenId());
        detail.setType(2);
        detail.setStatus(1);
        detail.setSaleAmount(bean.getAmount());
        detail.setBalanceId(bean.getId());
        detail.setOperator(bean.getUsername());
        detailMapper.insertSelective(detail);
        response.setData(bean.getId());
        return response;
    }

    @Override
    public OperaResponse findByOpenId(String openId) {
        OperaResponse response = new OperaResponse();
        Balance balance = balanceDao.selectBalanceByOpenId(openId) ;
        response.setData(balance);
        return response;
    }

    @Override
    public Integer delete(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public OperaResponse findList(BalanceQueryBean bean) {
        OperaResponse response = new OperaResponse();
        PageInfo<Balance> balancePageInfo =  balanceDao.selectBalanceByPageable(bean) ;
        response.setData(balancePageInfo);
        return response;
    }

    @Override
    public OperaResponse findDetailList(BalanceDetailQueryBean bean) {
        OperaResponse response = new OperaResponse();
        PageInfo<BalanceDetail> balancePageInfo =  balanceDao.selectBalanceDetailByPageable(bean) ;
        response.setData(balancePageInfo);
        return response;
    }

    @Transactional
    @Override
    public OperaResponse consume(BalanceDetail bean) {
        OperaResponse response = new OperaResponse();
        if (StringUtils.isEmpty(bean.getOpenId())) {
            response.setCode(900402);
            response.setMsg("openId 不能为null");
            return response;
        }
        if (StringUtils.isEmpty(bean.getOrderNo())) {
            response.setCode(900401);
            response.setMsg("orderNos 不能为Null");
            return response;
        }
        if (bean.getSaleAmount() == null || bean.getSaleAmount() < 1) {
            response.setCode(900401);
            response.setMsg("saleAmount 不能小于0");
            return response;
        }
        Balance openBalance = balanceDao.selectBalanceByOpenId(bean.getOpenId()) ;
        if (openBalance == null) {
            response.setCode(900404);
            response.setMsg("账号不存在");
            return response;
        }
        List<BalanceDetail> balanceDetails = balanceDao.selectBalanceDetailByOrderNo(bean.getOrderNo()) ;
        if (balanceDetails != null && balanceDetails.size() > 0) {
            response.setCode(900404);
            response.setMsg("支付单号重复。");
            return response;
        }
        Date date = new Date();
        OperaResponse consumeResult = new OperaResponse();
        openBalance.setAmount(bean.getSaleAmount());
        try {
            consumeResult = balanceConsumeAndRefundDao.balanceConsume(openBalance) ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (consumeResult.getCode() != 200) {
            return consumeResult ;
        }
        // 记录充值初始化记录
        BalanceDetail detail = new BalanceDetail();
        bean.setCreatedAt(date);
        bean.setUpdatedAt(date);
        bean.setType(0);
        bean.setStatus(1);
        bean.setBalanceId(openBalance.getId());
        detailMapper.insertSelective(bean);
        BalanceDetailBean detailBean = new BalanceDetailBean();
        BeanUtils.copyProperties(bean, detailBean);
        detailBean.setTelephone(openBalance.getTelephone());
        response.setData(detailBean);
        return response;
    }

    @Transactional
    @Override
    public OperaResponse refund(BalanceDetail bean) {
        OperaResponse response = new OperaResponse();
        if (StringUtils.isEmpty(bean.getOpenId())) {
            response.setCode(900402);
            response.setMsg("openId 必须大于0");
            return response;
        }
        if (StringUtils.isEmpty(bean.getOrderNo())) {
            response.setCode(900401);
            response.setMsg("orderNo 不能为Null");
            return response;
        }
        if (StringUtils.isEmpty(bean.getRefundNo())) {
            response.setCode(900401);
            response.setMsg("refundNo 不能为Null");
            return response;
        }
        if (bean.getSaleAmount() == null || bean.getSaleAmount() < 1) {
            response.setCode(900401);
            response.setMsg("saleAmount 不能小于0");
            return response;
        }
        // 查询明细是否存在
        List<BalanceDetail> balanceDetails =  balanceDao.selectBalanceDetailByOrderNo(bean.getOrderNo()) ;
        if (balanceDetails == null || balanceDetails.size() <= 0) {
            response.setCode(900402);
            response.setMsg("orderNo 不存在");
            return response;
        }
        int paymentAmount = 0;
        int refundedAount = 0;
        for (BalanceDetail balanceDetail : balanceDetails) {
            // 支付金额
            if (balanceDetail.getType() == 0) {
                paymentAmount = balanceDetail.getSaleAmount() ;
            }
            // 退款金额
            if (balanceDetail.getType() == 1) {
                refundedAount = refundedAount + balanceDetail.getSaleAmount();
            }
            if (bean.getRefundNo().equals(balanceDetail.getRefundNo())) {
                response.setCode(900404);
                response.setMsg("退款单号重复。");
                return response;
            }
        }
        // 已支付金额 - 已退款金额之和 >= 本次退款金额
        if ((paymentAmount - refundedAount) < bean.getSaleAmount()) {
            response.setCode(900403);
            response.setMsg("支付金额小于退款金额。");
            return response;
        }
        Balance openBalance = balanceDao.selectBalanceByOpenId(bean.getOpenId()) ;
        if (openBalance == null) {
            response.setCode(900404);
            response.setMsg("账号不存在");
            return response;
        }
        openBalance.setAmount(bean.getSaleAmount());
        Date date = new Date();
        OperaResponse refundResult = new OperaResponse();
        try {
            refundResult = balanceConsumeAndRefundDao.balanceRefund(openBalance) ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (refundResult.getCode() != 200) {
            return refundResult ;
        }
        // 记录充值初始化记录
        BalanceDetail detail = new BalanceDetail();
        bean.setCreatedAt(date);
        bean.setUpdatedAt(date);
        bean.setType(1);
        bean.setStatus(1);
        bean.setBalanceId(openBalance.getId());
        detailMapper.insertSelective(bean);
        BalanceDetailBean detailBean = new BalanceDetailBean();
        BeanUtils.copyProperties(bean, detailBean);
        detailBean.setTelephone(openBalance.getTelephone());
        response.setData(detailBean);
        return response;
    }

    @Override
    public OperaResponse updateDetailStatus(BalanceDetail bean) {
        OperaResponse response = new OperaResponse();
        if (bean.getId() > 0) {
            response.setCode(900402);
            response.setMsg("id 必须大于0");
            return response;
        }
        bean.setUpdatedAt(new Date());
        detailMapper.updateByPrimaryKeySelective(bean) ;
        response.setData(bean.getId());
        return response;
    }

    @Override
    public OperaResponse init(List<BalanceBean> beans) {
        OperaResponse response = new OperaResponse();
        List<String> tels = new ArrayList<>() ;
        beans.forEach(bean -> {
            if (StringUtils.isEmpty(bean.getTelephone()) || bean.getAmount() == null) {
                response.setCode(900401);
                response.setMsg("批量初始化出错列表");
                tels.add(bean.getTelephone()) ;
            } else {
                Date date = new Date();
                Balance balance = balanceDao.selectBalanceByTel(bean.getTelephone()) ;
                if (balance == null) {
                    balance = new Balance();
                    balance.setCreatedAt(date);
                    balance.setUpdatedAt(date);
                    balance.setTelephone(bean.getTelephone());
                    balance.setAmount(bean.getAmount());
                    mapper.insertSelective(balance) ;
                } else {
                    balance.setUpdatedAt(date);
                    balance.setTelephone(bean.getTelephone());
                    balance.setAmount(bean.getAmount());
                    mapper.updateByPrimaryKeySelective(balance) ;
                }
                // 记录初始化记录
                BalanceDetail detail = new BalanceDetail();
                detail.setCreatedAt(date);
                detail.setUpdatedAt(date);
                detail.setType(-1);
                detail.setStatus(1);
                detail.setSaleAmount(bean.getAmount());
                detail.setBalanceId(balance.getId());
                detail.setOperator(bean.getUsername());
                detailMapper.insertSelective(detail);

            }
        });
        if (tels != null && tels.size() > 0) {
            response.setData(tels);
        }
        return response;
    }

    @Override
    public void exportSum(BalanceQueryBean queryBean, HttpServletResponse response) {
        //  查询余额信息
        List<BalanceSumBean> balanceSumBeans = new ArrayList<>() ;
        log.info("开始");
        List<BalanceSumBean> initBalances = balanceMapper.selectInitAmount(queryBean) ;
        log.info("init");
        initBalances.forEach(sumBean -> {
            //  根据ID查询充值总额
            queryBean.setBalanceId(sumBean.getId());
            queryBean.setType(2);
            Integer chargeSum = balanceMapper.selectSumSaleAmountByTypeAndBalanceIdAndCreatedAt(queryBean) ;
            if (chargeSum != null) {
                sumBean.setChargeAmount(chargeSum);
            }
            //  根据ID查询支付总额
            queryBean.setType(0);
            Integer paymentSum  = balanceMapper.selectSumSaleAmountByTypeAndBalanceIdAndCreatedAt(queryBean) ;
            if (paymentSum != null) {
                sumBean.setPaymentAmount(paymentSum);
            }
            //  根据ID查询退款总额
            queryBean.setType(1);
            Integer refundSum = balanceMapper.selectSumSaleAmountByTypeAndBalanceIdAndCreatedAt(queryBean) ;
            if (refundSum != null) {
                sumBean.setRefundAmount(refundSum);
            }
            balanceSumBeans.add(sumBean) ;
        });
        // TODO 导出数据
        OutputStream outputStream = null;
        // 创建HSSFWorkbook对象
        HSSFWorkbook workbook = null;
        workbook = new HSSFWorkbook();
        // 创建HSSFSheet对象
        HSSFSheet sheet = workbook.createSheet("总表");
        HSSFSheet sheetCharge = workbook.createSheet("余额的充值记录");
        List<String> sumNames = new ArrayList<>() ;
        sumNames.add("会员电话") ;
        sumNames.add("OPEN ID") ;
        sumNames.add("总余额（最新时点）") ;
        sumNames.add("初始余额") ;
        sumNames.add("充值总额") ;
        sumNames.add("支付总额") ;
        sumNames.add("退款总额") ;
        createTitle(sheet, sumNames);
        createContentSum(sheet, balanceSumBeans);

        queryBean.setType(2);
        List<BalanceSumBean> balanceDetails = balanceMapper.selectChargeList(queryBean) ;
        List<String> chargeNames = new ArrayList<>() ;
        chargeNames.add("会员电话") ;
        chargeNames.add("OPEN ID") ;
        chargeNames.add("充值金额") ;
        chargeNames.add("充值时间") ;
        chargeNames.add("充值操作人") ;
        createTitle(sheetCharge, chargeNames);
        createContentCharge(sheetCharge, balanceDetails);
        // 2.3 文件名
        String date = DateUtil.nowDate(DateUtil.DATE_YYYYMMDD);
        String fileName = null;
        try {
            fileName = new String("余额对账报表".getBytes(), "ISO8859-1") + queryBean.getStart() +" - "+ queryBean.getEnd()+ ".xls";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 3. 输出文件
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
//            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            log.error("导出商品价格列表文件 出错了:{}", e.getMessage(), e);

        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 创建余额报表总表title
     *
     * @param sheet
     */
    private void createTitle(HSSFSheet sheet, List<String> cellNames) {
        HSSFRow titleRow = sheet.createRow(0);
        for (int i = 0; i < cellNames.size(); i++) {
            titleRow.createCell(i).setCellValue(cellNames.get(i));
        }
    }

    /**
     * 组装业务数据
     *
     * @param sheet
     * @param balanceSumBeans 需要导出的数据集合
     */
    private void createContentSum(HSSFSheet sheet, List<BalanceSumBean> balanceSumBeans) {

        int currentRowNum = 1; // 行号
        int totalRowNum = balanceSumBeans.size(); // 总行数
        DecimalFormat df = new DecimalFormat("0.00");
        for (BalanceSumBean balanceSum : balanceSumBeans) {
            // 新增一行
            HSSFRow hssfRow = sheet.createRow(currentRowNum);

            HSSFCell titleCell0 = hssfRow.createCell(0);
            titleCell0.setCellValue(balanceSum.getTelephone());

            HSSFCell titleCell1 = hssfRow.createCell(1);
            titleCell1.setCellValue(balanceSum.getOpenId());

            HSSFCell titleCell2 = hssfRow.createCell(2);
            titleCell2.setCellValue(df.format((float)balanceSum.getAmount() / 100));

            HSSFCell titleCell3 = hssfRow.createCell(3);
            titleCell3.setCellValue(df.format((float)balanceSum.getInitAmount() / 100));

            HSSFCell titleCell4 = hssfRow.createCell(4);
            titleCell4.setCellValue(df.format((float)balanceSum.getChargeAmount() / 100));

            HSSFCell titleCell5 = hssfRow.createCell(5);
            titleCell5.setCellValue(df.format((float)balanceSum.getPaymentAmount() / 100));

            HSSFCell titleCell6 = hssfRow.createCell(6);
            titleCell6.setCellValue(df.format((float)balanceSum.getRefundAmount() / 100));

            if (currentRowNum % 100 == 0) {
                log.info("导出余额列表 第{}行, 共{}行", currentRowNum, totalRowNum);
            }
            currentRowNum++;
            balanceSumBeans = null; // 释放
        }
    }

    /**
     * 组装业务数据
     *
     * @param sheet
     * @param balanceDetails 需要导出的数据集合
     */
    private void createContentCharge(HSSFSheet sheet, List<BalanceSumBean> balanceDetails) {

        int currentRowNum = 1; // 行号
        int totalRowNum = balanceDetails.size(); // 总行数
        DecimalFormat df = new DecimalFormat("0.00");
        for (BalanceSumBean balanceDetail : balanceDetails) {
            // 新增一行
            HSSFRow hssfRow = sheet.createRow(currentRowNum);

            HSSFCell titleCell0 = hssfRow.createCell(0);
            titleCell0.setCellValue(balanceDetail.getTelephone());

            HSSFCell titleCell1 = hssfRow.createCell(1);
            titleCell1.setCellValue(balanceDetail.getOpenId());

            HSSFCell titleCell2 = hssfRow.createCell(2);
            titleCell2.setCellValue(df.format((float)balanceDetail.getInitAmount() / 100));

            HSSFCell titleCell3 = hssfRow.createCell(3);
            titleCell3.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(balanceDetail.getCreatedAt()));

            HSSFCell titleCell4 = hssfRow.createCell(4);
            titleCell4.setCellValue(balanceDetail.getOperator());

            if (currentRowNum % 100 == 0) {
                log.info("导出余额充值列表 第{}行, 共{}行", currentRowNum, totalRowNum);
            }
            currentRowNum++;
            balanceDetails = null; // 释放
        }
    }

    @Override
    public void exportRecharge(BalanceQueryBean queryBean, HttpServletResponse response) {
        queryBean.setType(2);
        List<BalanceDetail> balanceDetails = balanceDao.selectBalanceDetailByTypeAndDate(queryBean) ;
        // TODO 导出数据
    }
}
