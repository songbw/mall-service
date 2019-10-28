package com.fengchao.product.aoyi.dao;

import com.fengchao.product.aoyi.bean.InventoryMpus;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.utils.JSONUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
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

    private String inventoryForUpdate = "select mpu, inventory from aoyi_prod_index where mpu = ? and merchant_id <> 2  for update" ;

    private String batchUpdate = "update aoyi_prod_index set inventory = ? where mpu = ?" ;


    public InventoryDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public OperaResult inventorySub(InventoryMpus inventoryMpus) {
        OperaResult result = new OperaResult() ;
//        try{
            Map<String,Object> selectParam=new HashMap<>();
            selectParam.put("mpu", inventoryMpus.getMpu());
            List<AoyiProdIndex> records = sqlSession.selectList("inventoryForUpdate", selectParam);
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
//        }
//        finally{
//            sqlSession.commit(true);
//            sqlSession.close();
//        }
        return result ;
    }
}
