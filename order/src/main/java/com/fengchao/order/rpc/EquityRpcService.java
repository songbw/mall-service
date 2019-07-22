package com.fengchao.order.rpc;

import com.alibaba.fastjson.JSON;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.rpc.extmodel.CouponBean;
import com.fengchao.order.rpc.extmodel.ProductInfoBean;
import com.fengchao.order.rpc.extmodel.PromotionBean;
import com.fengchao.order.feign.EquityService;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-18 下午5:13
 */
@Service
@Slf4j
public class EquityRpcService {

    private EquityService equityService;

    @Autowired
    public EquityRpcService(EquityService equityService) {
        this.equityService = equityService;
    }

    /**
     * 获取promotionbean 列表
     *
     * @param promotionIdList
     * @return
     */
    public List<PromotionBean> queryPromotionByIdList(List<Integer> promotionIdList) {
        // 返回值
        List<PromotionBean> promotionBeanList = new ArrayList<>();
                log.info("根据id集合查询活动列表 调用equity rpc服务 入参:{}", JSONUtil.toJsonString(promotionIdList));

        if (CollectionUtils.isNotEmpty(promotionIdList)) {
            OperaResult operaResult = equityService.findPromotionListByIdList(promotionIdList);
            log.info("根据id集合查询活动列表 调用equity rpc服务 返回:{}", JSONUtil.toJsonString(operaResult));

            // 处理返回
            if (operaResult.getCode() == 200) {
                List<PromotionBean> _promotionBeanList = (List<PromotionBean>) operaResult.getData().get("result");

                // 转 PromotionBean
                promotionBeanList = JSON.parseArray(JSON.toJSONString(_promotionBeanList), PromotionBean.class);
            } else {
                log.warn("根据id集合查询活动列表 调用equity rpc服务 错误!");
            }
        }

        log.info("EquityRpcService#queryPromotionByIdList 调用equity rpc服务 返回:{}", JSONUtil.toJsonString(promotionBeanList));

        return promotionBeanList;
    }

    /**
     * 获取couponbean 列表
     *
     * @param couponIdList
     * @return
     */
    public List<CouponBean> queryCouponByIdList(List<Integer> couponIdList) {
        // 返回值
        List<CouponBean> couponBeanList = new ArrayList<>();

        log.info("根据id集合查询coupon列表 调用equity rpc服务 入参:{}", JSONUtil.toJsonString(couponIdList));

        if (CollectionUtils.isNotEmpty(couponIdList)) {
            OperaResult operaResult = equityService.findCouponListByIdList(couponIdList);
            log.info("根据id集合查询coupon列表 调用equity rpc服务 返回:{}", JSONUtil.toJsonString(operaResult));

            // 处理返回
            if (operaResult.getCode() == 200) {
                List<CouponBean> _couponBeanList = (List<CouponBean>) operaResult.getData().get("result");

                // 转 CouponBean
                couponBeanList = JSON.parseArray(JSON.toJSONString(_couponBeanList), CouponBean.class);
            } else {
                log.warn("根据id集合查询coupon列表 调用equity rpc服务 错误!");
            }
        }

        log.info("EquityRpcService#queryCouponByIdList 调用equity rpc服务 返回:{}", JSONUtil.toJsonString(couponBeanList));

        return couponBeanList;
    }
}
