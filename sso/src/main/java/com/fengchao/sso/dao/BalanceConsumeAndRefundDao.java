package com.fengchao.sso.dao;

import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.model.Balance;
import com.fengchao.sso.util.MyBatisUtil;
import com.fengchao.sso.util.OperaResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author songbw
 * @date 2019/11/7 18:45
 */
@Slf4j
@Component
public class BalanceConsumeAndRefundDao {

    @Autowired
    private MyBatisUtil batisUtil ;

    public OperaResponse balanceConsume(Balance balance) throws SQLException {
        OperaResponse result = new OperaResponse() ;
        SqlSession sqlSession = batisUtil.getSession() ;
        try {
            List<Balance> records = sqlSession.selectList("selectForUpdateByPrimaryKey", balance.getId());
            if (records == null || records.size() <= 0) {
                result.setCode(200010);
                result.setMsg("余额 " + balance.getTelephone() + " 不存在。");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result;
            }
            Balance consumeBalance = records.get(0) ;
            int amount = consumeBalance.getAmount() - balance.getAmount() ;
            if (amount < 0) {
                result.setCode(900403);
                result.setMsg("余额不足");
                return result;
            }
            Date date = new Date();
            consumeBalance.setUpdatedAt(date);
            consumeBalance.setAmount(amount);
            if(records!=null && records.size()>0){
                sqlSession.update("batchUpdate", consumeBalance);
            }
            sqlSession.getConnection().commit();
        }catch (Exception e) {
            log.info("我出错了");
            e.printStackTrace();
        } finally {
            batisUtil.closeSession(sqlSession);
        }
        return result ;
    }

    public OperaResponse balanceRefund(Balance balance) throws SQLException {
        OperaResponse result = new OperaResponse() ;
        SqlSession sqlSession = batisUtil.getSession() ;
        try {
            List<Balance> records = sqlSession.selectList("selectForUpdateByPrimaryKey", balance.getId());
            Balance refundBalance = records.get(0) ;
            int amount = refundBalance.getAmount() + balance.getAmount() ;
            Date date = new Date();
            refundBalance.setUpdatedAt(date);
            refundBalance.setAmount(amount);
            if(records!=null && records.size()>0){
                sqlSession.update("batchUpdate", refundBalance);
            }
            sqlSession.getConnection().commit();
        }catch (Exception e) {
            log.info("我出错了");
            e.printStackTrace();
        } finally {
            batisUtil.closeSession(sqlSession);
        }
        return result ;
    }
}
