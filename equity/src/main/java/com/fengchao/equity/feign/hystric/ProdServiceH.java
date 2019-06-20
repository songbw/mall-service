package com.fengchao.equity.feign.hystric;

import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.bean.QueryProdBean;
import com.fengchao.equity.feign.ProdService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProdServiceH implements ProdService {
    @Override
    public OperaResult findCategoryList( List<String> categories) {
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("获取类别信息失败 " + categories);
        return result;
    }

    @Override
    public OperaResult findProdList(QueryProdBean queryProdBean) {
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("获取商品信息失败 " + queryProdBean);
        return result;
    }
}
