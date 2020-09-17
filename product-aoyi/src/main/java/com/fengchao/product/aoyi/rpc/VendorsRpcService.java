package com.fengchao.product.aoyi.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.feign.VendorsServiceClient;
import com.fengchao.product.aoyi.rpc.extmodel.RenterCompany;
import com.fengchao.product.aoyi.rpc.extmodel.SysCompany;
import com.fengchao.product.aoyi.utils.JSONUtil;
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
     * 查询所有的商户信息
     *
     * @return
     */
    public List<SysCompany> queryAllMerchantList() {
        // 返回值
        List<SysCompany> sysCompanyList = new ArrayList<>();

        // 执行rpc调用
        log.info("查询所有的商户信息 调用vendors rpc服务 入参:无");

        // 将merchantIdList转成Long型
        OperaResponse<List<SysCompany>> resultObject = vendorsServiceClient.queryAllMerchantList();
        log.info("查询所有的商户信息 调用vendors rpc服务 返回:{}", JSONUtil.toJsonString(resultObject));

        // 处理返回
        if (resultObject.getCode() == 200) {
            sysCompanyList = resultObject.getData();
        } else {
            log.warn("查询所有的商户信息 调用vendors rpc服务 错误!");
        }

        log.info("VendorsRpcService#queryAllMerchantList 调用vendors rpc服务 返回:{}",
                JSONUtil.toJsonString(sysCompanyList));

        return sysCompanyList;
    }

    public List<Integer> queryRenterMerhantList(String renterId){
        List<Integer> renterCompanyList = new ArrayList<>();

        OperaResponse<List<Integer>> response = vendorsServiceClient.queryRenterMerchantList(renterId) ;

        log.debug("vendor 服务 queryRenterMerhantList 入参renterId： {},  返回值：{}",renterId, JSONUtil.toJsonString(response));
        if (response.getCode() == 200) {
            renterCompanyList = response.getData() ;
        } else {
            log.warn("查询所有的商户信息 调用vendors rpc服务 错误!");
        }
        return renterCompanyList;

    }
}
