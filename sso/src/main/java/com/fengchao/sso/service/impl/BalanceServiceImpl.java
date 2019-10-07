package com.fengchao.sso.service.impl;

import com.fengchao.sso.bean.BalanceDetailQueryBean;
import com.fengchao.sso.bean.BalanceQueryBean;
import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.dao.BalanceDao;
import com.fengchao.sso.mapper.BalanceDetailMapper;
import com.fengchao.sso.mapper.BalanceMapper;
import com.fengchao.sso.mapper.BalanceXMapper;
import com.fengchao.sso.model.Balance;
import com.fengchao.sso.model.BalanceDetail;
import com.fengchao.sso.service.IBalanceService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    public OperaResponse update(Balance bean) {
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
        Balance temp = balanceMapper.selectForUpdateByPrimaryKey(openBalance.getId()) ;
        int amount = temp.getAmount() - bean.getSaleAmount() ;
        if (amount < 0) {
            response.setCode(900403);
            response.setMsg("余额不足");
            return response;
        }
        Date date = new Date();
        temp.setUpdatedAt(date);
        temp.setAmount(amount);
        mapper.updateByPrimaryKeySelective(temp) ;
        // 记录充值初始化记录
        BalanceDetail detail = new BalanceDetail();
        bean.setCreatedAt(date);
        bean.setUpdatedAt(date);
        bean.setType(0);
        bean.setStatus(1);
        bean.setBalanceId(openBalance.getId());
        detailMapper.insertSelective(bean);
        response.setData(bean);
        return response;
    }

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
        // TODO 查询明细是否存在
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
        Balance temp = balanceMapper.selectForUpdateByPrimaryKey(openBalance.getId()) ;
        int amount = temp.getAmount() + bean.getSaleAmount() ;
        Date date = new Date();
        temp.setUpdatedAt(date);
        temp.setAmount(amount);
        mapper.updateByPrimaryKeySelective(temp) ;
        // 记录充值初始化记录
        BalanceDetail detail = new BalanceDetail();
        bean.setCreatedAt(date);
        bean.setUpdatedAt(date);
        bean.setType(1);
        bean.setStatus(1);
        bean.setBalanceId(temp.getId());
        detailMapper.insertSelective(bean);
        response.setData(bean.getId());
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
}
