package com.fengchao.order.rpc;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.VendorsServiceClient;
import com.fengchao.order.rpc.extmodel.SysCompanyX;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
     * @return
     */
    public List<SysCompanyX> queryAllCompanyList() {
        // 返回值
        List<SysCompanyX> sysCompanyList = new ArrayList<>();

        // 执行rpc调用
        log.info("查询所有商户信息 调用vendors rpc服务 入参:无");

        // 将merchantIdList转成Long型
        OperaResponse<List<SysCompanyX>> operaResponse = vendorsServiceClient.queryAllCompanyList();
        log.info("查询所有商户信息 调用vendors rpc服务 返回:{}", JSONUtil.toJsonString(operaResponse));

        // 处理返回
        if (operaResponse.getCode() == 200) {
            sysCompanyList = operaResponse.getData();
        } else {
            log.warn("查询所有商户信息 调用vendors rpc服务 错误!");
        }

        log.info("VendorsRpcService#queryAllCompanyList 调用vendors rpc服务 返回:{}",
                JSONUtil.toJsonString(sysCompanyList));

        return sysCompanyList;
    }
}
