package com.fengchao.equity.feign.hystric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.bean.QueryProdBean;
import com.fengchao.equity.bean.vo.PageVo;
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
    public OperaResult findProdList(QueryProdBean queryProdBean, String appId) {
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("获取商品信息失败 " + queryProdBean);
        return result;
    }

    @Override
    public OperaResult findProductListByMpuIdList(List<String> mpuIdList) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        result.setCode(404);
        result.setMsg("获取商品信息失败 " + mpuIdList);
        return result;
    }

    @Override
    public OperaResult searchProd(QueryProdBean queryProdBean, Integer merchantHeader) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        result.setCode(404);
        result.setMsg("获取商品信息失败 " + queryProdBean.getCategoryID());
        return result;
    }


    @Override
    public OperaResult selectPlatformAll(PageVo pageVo) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        result.setCode(404);
        result.setMsg("获取平台信息失败 " + pageVo);
        return result;
    }
}
