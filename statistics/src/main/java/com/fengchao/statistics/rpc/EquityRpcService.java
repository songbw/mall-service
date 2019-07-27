package com.fengchao.statistics.rpc;

import com.alibaba.fastjson.JSON;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.EquityServiceClient;
import com.fengchao.statistics.feign.VendorsServiceClient;
import com.fengchao.statistics.rpc.extmodel.PromotionBean;
import com.fengchao.statistics.rpc.extmodel.ResultObject;
import com.fengchao.statistics.rpc.extmodel.SysUser;
import com.fengchao.statistics.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author tom
 * @Date 19-7-27 上午10:34
 */
@Service
@Slf4j
public class EquityRpcService {

    private EquityServiceClient equityServiceClient;

    @Autowired
    public EquityRpcService(EquityServiceClient equityServiceClient) {
        this.equityServiceClient = equityServiceClient;
    }

    /**
     * 根据id集合查询活动信息
     *
     * @param promotionIdList
     * @return
     */
    public List<PromotionBean> queryPromotionByIdList(List<Integer> promotionIdList) {
        // 返回值
        List<PromotionBean> promotionBeanList = new ArrayList<>();

        // 执行rpc调用
        log.info("根据id集合查询活动信息 调用equity rpc服务 入参:{}", JSONUtil.toJsonString(promotionIdList));
        OperaResult operaResult = equityServiceClient.queryPromotionByIdList(promotionIdList);
        log.info("根据id集合查询活动信息 调用equity rpc服务 返回:{}", JSONUtil.toJsonString(operaResult));

        // 处理返回
        if (operaResult.getCode() == 200) {
            Map _resultMap = (Map) operaResult.getData().get("result");

            // 转
            promotionBeanList = JSON.parseArray(JSON.toJSONString(_resultMap), PromotionBean.class);
        } else {
            log.warn("根据id集合查询活动信息 调用equity rpc服务 错误!");
        }

        log.info("EquityRpcService#queryPromotionByIdList 调用equity rpc服务 返回:{}",
                JSONUtil.toJsonString(promotionBeanList));

        return promotionBeanList;
    }
}
