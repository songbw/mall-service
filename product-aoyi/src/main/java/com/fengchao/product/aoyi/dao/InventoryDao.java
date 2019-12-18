package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.bean.InventoryMpus;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.utils.MyBatisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.SQLException;
import java.util.List;

/**
 * @author songbw
 * @date 2019/10/28 19:11
 */
@Slf4j
@Component
public class InventoryDao {

    @Autowired
    private MyBatisUtil batisUtil ;

//    private SqlSession sqlSession;

//    public InventoryDao(SqlSessionFactory sqlSessionFactory) throws SQLException {
//        log.info("inventory dao 我被初始化了");
//        this.sqlSession = sqlSessionFactory.openSession();
//        this.sqlSession.getConnection().setAutoCommit(false);
//    }

//    private SqlSession getSqlSession() {
//        sqlSession = new SqlSessionTemplate().getSqlSessionFactory().openSession();
//        return sqlSession;
//    }

    public OperaResult inventorySub(InventoryMpus inventoryMpus) throws SQLException {
        OperaResult result = new OperaResult() ;
        SqlSession sqlSession = batisUtil.getSession() ;
        try {
            List<AoyiProdIndexX> records = sqlSession.selectList("selectForUpdateByMpu", inventoryMpus.getMpu());
            if (records == null || records.size() <= 0) {
                result.setCode(200010);
                result.setMsg("商品 " + inventoryMpus.getMpu() + " 不存在。");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result;
            }
            AoyiProdIndexX prodIndex = records.get(0) ;
            if (prodIndex.getInventory() <= 0 || prodIndex.getInventory() < inventoryMpus.getRemainNum()) {
                result.setCode(200010);
                result.setMsg("商品 " + prodIndex.getName() + " 库存不足。");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result;
            }
            if(records!=null && records.size()>0){
                prodIndex.setInventory(prodIndex.getInventory() - inventoryMpus.getRemainNum());
                sqlSession.update("batchUpdate", prodIndex);
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

    public OperaResult inventoryAdd(InventoryMpus inventoryMpus) throws SQLException {
        OperaResult result = new OperaResult() ;
        SqlSession sqlSession = batisUtil.getSession() ;
        try {
            List<AoyiProdIndexX> records = sqlSession.selectList("selectForUpdateByMpu", inventoryMpus.getMpu());
            if (records == null || records.size() <= 0) {
                result.setCode(200010);
                result.setMsg("商品 " + inventoryMpus.getMpu() + " 不存在。");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result;
            }
            AoyiProdIndexX prodIndex = records.get(0) ;
            if(records!=null && records.size()>0){
                prodIndex.setInventory(prodIndex.getInventory() + inventoryMpus.getRemainNum());
                sqlSession.update("batchUpdate", prodIndex);
            }
            sqlSession.getConnection().commit();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            batisUtil.closeSession(sqlSession);
        }
        return result ;
    }
}
