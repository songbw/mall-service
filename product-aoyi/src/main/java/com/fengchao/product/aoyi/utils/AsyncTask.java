package com.fengchao.product.aoyi.utils;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.dao.ProductDao;
import com.fengchao.product.aoyi.model.AoyiBaseBrandX;
import com.fengchao.product.aoyi.model.AoyiBaseCategory;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author songbw
 * @date 2019/10/11 16:28
 */
@Slf4j
@Component
public class AsyncTask {

    @Async
    public void executeAsyncProductTask(ProductDao productDao, WebTarget webTarget, List<AoyiProdIndex> prodIndices) {
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        prodIndices.forEach(prodIndex -> {
            Response response1 = invocationBuilder.put(Entity.entity(prodIndex, MediaType.APPLICATION_JSON));
            OperaResult result = response1.readEntity(OperaResult.class);
            // TODO 成功则更新商品表，失败则打印日志
            if (result.getCode() == 200) {
                productDao.updateSyncAt(prodIndex.getId());
            } else {
                log.info("线程" + Thread.currentThread().getName() + " 执行异步任务：" + "同步商品"+ prodIndex.getMpu()+"失败, 失败原因 {}", JSONUtil.toJsonString(result));
            }
        });
    }

    @Async
    public void executeAsyncCategoryTask(WebTarget webTarget, List<AoyiBaseCategory> categories) {
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        categories.forEach(category -> {
            Response response1 = invocationBuilder.put(Entity.entity(category, MediaType.APPLICATION_JSON));
            OperaResponse result = response1.readEntity(OperaResponse.class);
            if (result.getCode() != 200) {
                log.info("线程" + Thread.currentThread().getName() + " 执行异步任务：" + "同步类目"+ category.getCategoryId()+"失败, 失败原因 {}", JSONUtil.toJsonString(result));
            }
        });
    }

    @Async
    public void executeAsyncBrandTask(WebTarget webTarget, List<AoyiBaseBrandX> brands) {
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        brands.forEach(brand -> {
            Response response1 = invocationBuilder.put(Entity.entity(brand, MediaType.APPLICATION_JSON));
            OperaResponse result = response1.readEntity(OperaResponse.class);
            if (result.getCode() != 200) {
                log.info("线程" + Thread.currentThread().getName() + " 执行异步任务：" + "同步品牌"+ brand.getBrandId()+"失败, 失败原因 {}", JSONUtil.toJsonString(result));
            }
        });
    }
}
