package com.fengchao.order.rpc;

import com.alibaba.fastjson.JSON;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.rpc.extmodel.CouponUseInfoBean;
import com.fengchao.order.rpc.extmodel.PromotionBean;
import com.fengchao.order.feign.EquityServiceClient;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-18 下午5:13
 */
@Service
@Slf4j
public class EquityRpcService {

    private EquityServiceClient equityService;

    @Autowired
    public EquityRpcService(EquityServiceClient equityService) {
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
            OperaResponse<List<PromotionBean>> operaResponse
                    = equityService.findPromotionListByIdList(promotionIdList);
            log.info("根据id集合查询活动列表 调用equity rpc服务 返回:{}", JSONUtil.toJsonString(operaResponse));

            // 处理返回
            if (operaResponse.getCode() == 200) {
                promotionBeanList = operaResponse.getData();
            } else {
                log.warn("根据id集合查询活动列表 调用equity rpc服务 错误!");
            }
        }

        log.info("EquityRpcService#queryPromotionByIdList 调用equity rpc服务 返回:{}", JSONUtil.toJsonString(promotionBeanList));

        return promotionBeanList;
    }

    /**
     * 获取 couponuseinfobean 列表
     *
     * @param couponIdList
     * @return
     */
    public List<CouponUseInfoBean> queryCouponUseInfoByIdList(List<Integer> couponIdList) {
        // 返回值
        List<CouponUseInfoBean> couponUseInfoBeanList = new ArrayList<>();

        log.info("根据id集合查询 couponuseinfobean 列表 调用equity rpc服务 入参:{}", JSONUtil.toJsonString(couponIdList));

        if (CollectionUtils.isNotEmpty(couponIdList)) {
            OperaResult operaResult = equityService.findCouponUseInfoListByIdList(couponIdList);
            log.info("根据id集合查询 couponuseinfobean 列表 调用equity rpc服务 返回:{}", JSONUtil.toJsonString(operaResult));

            // 处理返回
            if (operaResult.getCode() == 200) {
                List<CouponUseInfoBean> _couponUseInfoBeanList = (List<CouponUseInfoBean>) operaResult.getData().get("result");

                // 转 CouponBean
                couponUseInfoBeanList = JSON.parseArray(JSON.toJSONString(_couponUseInfoBeanList), CouponUseInfoBean.class);
            } else {
                log.warn("根据id集合查询 couponuseinfobean 列表 调用equity rpc服务 错误!");
            }
        }

        log.info("EquityRpcService#queryCouponUseInfoByIdList 调用equity rpc服务 返回:{}", JSONUtil.toJsonString(couponUseInfoBeanList));

        return couponUseInfoBeanList;
    }
}
