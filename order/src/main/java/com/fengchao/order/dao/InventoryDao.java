package com.fengchao.order.dao;

import com.fengchao.order.bean.InventoryMpus;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.model.AoyiProdIndex;
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

    private  SqlSession sqlSession;

    private String inventoryForUpdate = "select mpu, inventory from aoyi_prod_index where mpu = '99000128' and merchant_id <> 2  for update" ;

    private String batchUpdate = "update aoyi_prod_index set inventory = ? where mpu = ?" ;


    public InventoryDao(SqlSessionFactory sqlSessionFactory) throws SQLException {
        sqlSession = sqlSessionFactory.openSession();
        sqlSession.getConnection().setAutoCommit(false);
    }

    public OperaResult inventorySub(InventoryMpus inventoryMpus) {
        OperaResult result = new OperaResult() ;
        try{
            Map<String,Object> selectParam=new HashMap<>();
            selectParam.put("mpu", inventoryMpus.getMpu());
            List<AoyiProdIndex> records = sqlSession.selectList("selectForUpdateByMpu");
            if (records == null || records.size() <= 0) {
                result.setCode(200010);
                result.setMsg("商品 " + inventoryMpus.getMpu() + " 不存在。");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result;
            }
            AoyiProdIndex prodIndex = records.get(0) ;
            if (prodIndex.getInventory() <= 0 || prodIndex.getInventory() < inventoryMpus.getRemainNum()) {
                result.setCode(200010);
                result.setMsg("商品 " + prodIndex.getName() + " 库存不足。");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result;
            }
            if(records!=null && records.size()>0){
                Map<String,Object> updateParam=new HashMap<>();
                updateParam.put("inventory", records);
                updateParam.put("mpu", inventoryMpus.getMpu());
                sqlSession.update("batchUpdate", updateParam);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            sqlSession.commit();
            sqlSession.close();
        }
        return result ;
    }
}
