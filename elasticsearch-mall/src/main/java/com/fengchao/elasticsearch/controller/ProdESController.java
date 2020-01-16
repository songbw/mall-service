package com.fengchao.elasticsearch.controller;

import com.fengchao.elasticsearch.domain.AoyiProdIndex;
import com.fengchao.elasticsearch.domain.OperaResponse;
import com.fengchao.elasticsearch.domain.OperaResult;
import com.fengchao.elasticsearch.domain.ProductQueryBean;
import com.fengchao.elasticsearch.service.ProductESService;
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
