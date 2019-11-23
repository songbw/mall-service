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
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        if (!StringUtils.isEmpty(bean.getTelephone())) {
            criteria.andTelephoneEqualTo(bean.getTelephone()) ;
        }
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
        log.info("updateOpenIdByTel 入参 is {}, {}", tel, openId);
        BalanceExample balanceExample = new BalanceExample();
        BalanceExample.Criteria criteria = balanceExample.createCriteria();
        if (!StringUtil.isEmpty(tel)) {
            criteria.andTelephoneEqualTo(tel) ;
            Balance balance = selectBalanceByTel(tel) ;
            if (balance != null) {
                balance.setOpenId(openId);
                Date date = new Date();
                balance.setUpdatedAt(date);
                int id = mapper.updateByExample(balance, balanceExample) ;
                log.info("updateOpenIdByTel return is {}", id);
            } else {
                log.info("updateOpenIdByTel 余额信息为null");
            }
        }
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

    /**
     * 根据tel查询余额
     * @param tel
     * @return
     */
    public Balance selectBalanceByTel(String tel) {
        BalanceExample balanceExample = new BalanceExample();
        BalanceExample.Criteria criteria = balanceExample.createCriteria();
        criteria.andTelephoneEqualTo(tel) ;
        List<Balance> balances = mapper.selectByExample(balanceExample);
        if (balances != null && balances.size() > 0) {
            return balances.get(0) ;
        }
        return null;
    }

    /**
     * 根据类型和时间段查询明细
     * @param queryBean
     * @return
     */
    public List<BalanceDetail> selectBalanceDetailByTypeAndDate(BalanceQueryBean queryBean) {
        BalanceDetailExample balanceDetailExample = new BalanceDetailExample();
        BalanceDetailExample.Criteria criteria = balanceDetailExample.createCriteria();
        criteria.andTypeEqualTo(queryBean.getType());
        try {
            Date start = new SimpleDateFormat("yyyy-MM-dd").parse(queryBean.getStart());
            Date end = new SimpleDateFormat("yyyy-MM-dd").parse(queryBean.getEnd());
            criteria.andUpdatedAtBetween(start, end) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<BalanceDetail> balanceDetails = detailMapper.selectByExample(balanceDetailExample);
        return balanceDetails;
    }

    /**
     * 根据余额ID和类型和时间段查询明细
     * @param queryBean
     * @return
     */
    public List<BalanceDetail> selectBalanceDetailByBalanceIdAndTypeAndDate(BalanceQueryBean queryBean) {
        BalanceDetailExample balanceDetailExample = new BalanceDetailExample();
        BalanceDetailExample.Criteria criteria = balanceDetailExample.createCriteria();
        criteria.andBalanceIdEqualTo(queryBean.getBalanceId()) ;
        criteria.andTypeEqualTo(queryBean.getType());
        try {
            Date start = new SimpleDateFormat("yyyy-MM-dd").parse(queryBean.getStart());
            Date end = new SimpleDateFormat("yyyy-MM-dd").parse(queryBean.getEnd());
            criteria.andUpdatedAtBetween(start, end) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<BalanceDetail> balanceDetails = detailMapper.selectByExample(balanceDetailExample);
        return balanceDetails;
    }

    /**
     * 查询余额列表
     *
     * @param bean
     * @return
     */
    public List<Balance> selectBalanceList(BalanceQueryBean bean) {
        BalanceExample balanceExample = new BalanceExample();
        BalanceExample.Criteria criteria = balanceExample.createCriteria();
        if (!StringUtils.isEmpty(bean.getTelephone())) {
            criteria.andTelephoneEqualTo(bean.getTelephone()) ;
        }
        List<Balance> balances = mapper.selectByExample(balanceExample);
        return balances;
    }
}
