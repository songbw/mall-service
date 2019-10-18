package com.fengchao.sso.dao;

import com.fengchao.sso.bean.BalanceDetailQueryBean;
import com.fengchao.sso.bean.BalanceQueryBean;
import com.fengchao.sso.mapper.BalanceDetailMapper;
import com.fengchao.sso.mapper.BalanceMapper;
import com.fengchao.sso.model.Balance;
import com.fengchao.sso.model.BalanceDetail;
import com.fengchao.sso.model.BalanceDetailExample;
import com.fengchao.sso.model.BalanceExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author song
 * @Date 19-9-17 下午3:46
 */
@Component
@Slf4j
public class BalanceDao {

    private BalanceMapper mapper;
    private BalanceDetailMapper detailMapper;

    @Autowired
    public BalanceDao(BalanceMapper mapper, BalanceDetailMapper detailMapper) {
        this.mapper = mapper;
        this.detailMapper = detailMapper;
    }

    /**
     * 分页查询余额列表
     *
     * @param bean
     * @return
     */
    public PageInfo<Balance> selectBalanceByPageable(BalanceQueryBean bean) {
        BalanceExample balanceExample = new BalanceExample();
        BalanceExample.Criteria criteria = balanceExample.createCriteria();
        PageHelper.startPage(bean.getPageNo(), bean.getPageSize());
        List<Balance> balances = mapper.selectByExample(balanceExample);
        PageInfo<Balance> pageInfo = new PageInfo(balances);

        return pageInfo;
    }

    /**
     * 分页查询余额明细列表
     *
     * @param bean
     * @return
     */
    public PageInfo<BalanceDetail> selectBalanceDetailByPageable(BalanceDetailQueryBean bean) {
        BalanceDetailExample balanceDetailExample = new BalanceDetailExample();
        balanceDetailExample.setOrderByClause("id desc");
        BalanceDetailExample.Criteria criteria = balanceDetailExample.createCriteria();
        if (!StringUtils.isEmpty(bean.getOpenId())) {
            criteria.andOpenIdEqualTo(bean.getOpenId());
        }
        PageHelper.startPage(bean.getPageNo(), bean.getPageSize());
        List<BalanceDetail> balanceDetails = detailMapper.selectByExample(balanceDetailExample);
        PageInfo<BalanceDetail> pageInfo = new PageInfo(balanceDetails);
        return pageInfo;
    }

    /**
     * 根据openId查询余额
     * @param openId
     * @return
     */
    public Balance selectBalanceByOpenId(String openId) {
        BalanceExample balanceExample = new BalanceExample();
        BalanceExample.Criteria criteria = balanceExample.createCriteria();
        criteria.andOpenIdEqualTo(openId) ;
        List<Balance> balances = mapper.selectByExample(balanceExample);
        if (balances != null && balances.size() > 0) {
            return balances.get(0) ;
        }
        return null;
    }

    /**
     * 根据手机号更新余额表的openId
     * @param tel
     * @param openId
     */
    public void updateOpenIdByTel(String tel, String openId) {
        BalanceExample balanceExample = new BalanceExample();
        BalanceExample.Criteria criteria = balanceExample.createCriteria();
        criteria.andTelephoneEqualTo(tel) ;
        Balance balance = new Balance();
        balance.setOpenId(openId);
        Date date = new Date();
        balance.setCreatedAt(date);
        balance.setUpdatedAt(date);
        mapper.updateByExample(balance, balanceExample) ;
    }

    /**
     * 根据orderNo查询明细
     * @param orderNo
     * @return
     */
    public List<BalanceDetail> selectBalanceDetailByOrderNo(String orderNo) {
        BalanceDetailExample balanceDetailExample = new BalanceDetailExample();
        BalanceDetailExample.Criteria criteria = balanceDetailExample.createCriteria();
        criteria.andOrderNoEqualTo(orderNo);
        List<BalanceDetail> balanceDetails = detailMapper.selectByExample(balanceDetailExample);
        return balanceDetails;
    }
}
