package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.bean.ProductQueryBean;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.service.ProductESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/es/prod", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProdESController {

    @Autowired
    private ProductESService service ;

    @GetMapping("/detail")
    public OperaResult detail(String skuId, OperaResult result) {
        AoyiProdIndex product = service.get(skuId);
        result.getData().put("result", service.get(skuId)) ;
        return result;
    }

    @PostMapping
    public OperaResult search(@RequestBody ProductQueryBean queryBean, @RequestHeader("appId") String appId, OperaResult result) {
        queryBean.setAppId(appId);
        result.getData().put("result", service.query(queryBean)) ;
        return result;
    }

    @PostMapping("/search")
    public OperaResponse searchProfix(@RequestBody ProductQueryBean queryBean) {
        OperaResponse operaResponse = new OperaResponse();
        operaResponse.setData(service.queryByCategoryPrefix(queryBean));
        return operaResponse;
    }

    @DeleteMapping("/detail")
    public OperaResult detailDelete(Integer id, OperaResult result) {
        result.getData().put("result", service.delete(id)) ;
        return result;
    }
}