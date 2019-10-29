package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.bean.InventoryMpus;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author songbw
 * @date 2019/10/28 19:11
 */
@Component
public class InventoryDao {

    private final SqlSession sqlSession;


    public InventoryDao(SqlSessionFactory sqlSessionFactory) throws SQLException {
        sqlSession = sqlSessionFactory.openSession();
        sqlSession.getConnection().setAutoCommit(false);
    }

    public OperaResult inventorySub(InventoryMpus inventoryMpus) throws SQLException {
        OperaResult result = new OperaResult() ;
        try{
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            sqlSession.getConnection().commit();
            sqlSession.close();
        }
        return result ;
    }

    public OperaResult inventoryAdd(InventoryMpus inventoryMpus) throws SQLException {
        OperaResult result = new OperaResult() ;
        try{
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            sqlSession.getConnection().commit();
            sqlSession.close();
        }
        return result ;
    }
}
