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

    @GetMapping("/top")
    public OperaResponse topKeyword(@RequestHeader("appId") String appId, String startTime, String endTime) {
        OperaResponse response = new OperaResponse() ;
        ProductQueryBean queryBean = new ProductQueryBean();
        queryBean.setAppId(appId);
        queryBean.setStartTime(startTime);
        queryBean.setEndTime(endTime);
        return service.topKeyword(queryBean);
    }

    @GetMapping("/admin/top")
    public OperaResponse topKeywordAdmin(@RequestHeader("renter") String renterId, String startTime, String endTime, Integer pageSize) {
        OperaResponse response = new OperaResponse() ;
        ProductQueryBean queryBean = new ProductQueryBean();
        queryBean.setRenterHeader(renterId);
        queryBean.setStartTime(startTime);
        queryBean.setEndTime(endTime);
        queryBean.setPageSize(pageSize);
        return service.topKeywordAdmin(queryBean);
    }
}
