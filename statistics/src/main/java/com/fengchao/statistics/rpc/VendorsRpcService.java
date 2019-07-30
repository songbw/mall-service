package com.fengchao.statistics.rpc;

import com.alibaba.fastjson.JSON;
import com.fengchao.statistics.feign.VendorsServiceClient;
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
public class VendorsRpcService {

    private VendorsServiceClient vendorsServiceClient;

    @Autowired
    public VendorsRpcService(VendorsServiceClient vendorsServiceClient) {
        this.vendorsServiceClient = vendorsServiceClient;
    }

    /**
     * 根据id集合查询商户信息
     *
     * @param merchantIdList
     * @return
     */
    public List<SysUser> queryMerchantByIdList(List<Integer> merchantIdList) {
        // 返回值
        List<SysUser> sysUserList = new ArrayList<>();

        // 执行rpc调用
        log.info("根据id集合查询商户信息 调用vendors rpc服务 入参:{}", JSONUtil.toJsonString(merchantIdList));

        // 将merchantIdList转成Long型
        List<Long> idList = merchantIdList.stream().map(m -> m.longValue()).collect(Collectors.toList());
        ResultObject<List<SysUser>> resultObject = vendorsServiceClient.queryMerchantByIdList(idList);
        log.info("根据id集合查询商户信息 调用vendors rpc服务 返回:{}", JSONUtil.toJsonString(resultObject));

        // 处理返回
        if (resultObject.getCode() == 200) {
            Map _resultMap = (Map) resultObject.getData();

            // 转
            sysUserList = JSON.parseArray(JSON.toJSONString(_resultMap), SysUser.class);
        } else {
            log.warn("根据id集合查询商户信息 调用vendors rpc服务 错误!");
        }

        log.info("VendorsRpcService#queryMerchantByIdList 调用vendors rpc服务 返回:{}",
                JSONUtil.toJsonString(sysUserList));

        return sysUserList;
    }
}
