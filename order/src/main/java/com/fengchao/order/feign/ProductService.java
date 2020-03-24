package com.fengchao.order.feign;

import com.fengchao.order.bean.InventoryMpus;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.feign.hystric.ProductServiceH;
import com.fengchao.order.model.AoyiProdIndex;
import com.fengchao.order.rpc.extmodel.Platform;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "product-aoyi", url = "${rpc.feign.client.product.url:}", fallback = ProductServiceH.class)
public interface ProductService {

    @RequestMapping(value = "/prod", method = RequestMethod.GET)
    OperaResult find(@RequestParam("mpu") String mpu, @RequestHeader("appId") String appId);

    /**
     * 根据mpu集合查询产品信息
     *
     * @param mpuIdList
     * @return
     */
    @RequestMapping(value = "/prod/findByMpuIdList", method = RequestMethod.GET)
    OperaResult findProductListByMpuIdList(@RequestParam("mpuIdList") List<String> mpuIdList);
    /**
     * 根据mpu集合查询产品信息
     *
     * @param mpuIdList
     * @return
     */
    @RequestMapping(value = "/prod/mpuIds", method = RequestMethod.GET)
    OperaResult selectProductListByMpuIdList(@RequestParam("mpuIdList") List<String> mpuIdList);

    /**
     * 批量扣库存
     * @param inventories
     * @return
     */
    @RequestMapping(value = "/prod/inventory/sub", method = RequestMethod.PUT)
    OperaResult inventorySub(@RequestBody List<InventoryMpus> inventories);

    /**
     * 批量添加库存
     * @param inventories
     * @return
     */
    @RequestMapping(value = "/prod/inventory/add", method = RequestMethod.PUT)
    OperaResult inventoryAdd(@RequestBody List<InventoryMpus> inventories);

    /**
     * 获取平台信息
     * @param appId
     * @return
     */
    @RequestMapping(value = "/platform/app", method = RequestMethod.GET)
    OperaResult selectPlatformByAppId(@RequestParam("appId") String appId);

    /**
     * 获取平台信息
     * @param appIdList
     * @return
     */
    @RequestMapping(value = "/platform/apps", method = RequestMethod.GET)
    OperaResponse<List<Platform>> selectPlatformByAppIdList(@RequestParam("appIdList") List<String> appIdList);

    /**
     * 根据mpu,code集合查询产品信息
     *
     * @param mpuIdList
     * @return
     */
    @RequestMapping(value = "/prod/sku/mpuIds", method = RequestMethod.GET)
    OperaResponse selectByMpuIdListAndSkuCodes(@RequestBody List<AoyiProdIndex> mpuIdList);

    @RequestMapping(value = "/prod/spu", method = RequestMethod.GET)
    OperaResponse findSpu(@RequestParam("mpu") String mpu, @RequestParam("code") String code);

}
