package com.fengchao.order.feign;

import com.fengchao.order.bean.CouponUseInfoBean;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.bean.VirtualTicketsBean;
import com.fengchao.order.feign.hystric.EquityServiceClientH;
import com.fengchao.order.model.AoyiProdIndex;
import com.fengchao.order.rpc.extmodel.PromotionBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "equity", url = "${rpc.feign.client.equit.url:}", fallback = EquityServiceClientH.class)
public interface EquityServiceClient {

    /**
     * 优惠券核销
     * @param bean
     * @return
     */
    @RequestMapping(value = "/coupon/consume", method = RequestMethod.POST)
    OperaResult consume(@RequestBody CouponUseInfoBean bean);

    /**
     * 预占优惠券
     * @param bean
     * @return
     */
    @RequestMapping(value = "/coupon/occupy", method = RequestMethod.POST)
    OperaResult occupy(@RequestBody CouponUseInfoBean bean);

    /**
     * 优惠券释放
     * @param bean
     * @return
     */
    @RequestMapping(value = "/coupon/release", method = RequestMethod.POST)
    OperaResult release(@RequestBody CouponUseInfoBean bean);

    /**
     * 根据id集合获取Promotion列表
     *
     * @param idList
     * @return
     */
    @RequestMapping(value = "/promotion/findByIdList", method = RequestMethod.GET)
    OperaResponse<List<PromotionBean>> findPromotionListByIdList(@RequestParam("idList") List<Integer> idList);

    /**
     * 根据id集合获取Coupon列表
     *
     * @param idList
     * @return
     */
    @RequestMapping(value = "/couponUseInfo/findByIdList", method = RequestMethod.GET)
    OperaResult findCouponUseInfoListByIdList(@RequestParam("idList") List<Integer> idList);

    @RequestMapping(value = "/promotion/mpuList", method = RequestMethod.GET)
    OperaResult findPromotionByMpuList(@RequestParam("mpuList") List<String> mpuList);

    /**
     * 生成用户虚拟商品
     * @param bean
     * @return
     */
    @RequestMapping(value = "/virtual/create", method = RequestMethod.POST)
    OperaResult createVirtual(@RequestBody VirtualTicketsBean bean);

    /**
     * 根据MPU批量获取活动和优惠券信息
     * @param beans
     * @return
     */
    @RequestMapping(value = "/coupon/mpus", method = RequestMethod.POST)
    OperaResult findCouponListByMpuList(@RequestBody List<AoyiProdIndex> beans);
}
