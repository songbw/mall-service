package com.fengchao.sso.rpc;

import com.alibaba.fastjson.JSON;
import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.feign.VendorsServiceClient;
import com.fengchao.sso.rpc.extmodel.SysCompanyX;
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
        log.info("查询所有商户信息 调用vendors rpc服务 返回:{}", JSON.toJSONString(operaResponse));

        // 处理返回
        if (operaResponse.getCode() == 200) {
            sysCompanyList = operaResponse.getData();
        } else {
            log.warn("查询所有商户信息 调用vendors rpc服务 错误!");
        }

        log.info("VendorsRpcService#queryAllCompanyList 调用vendors rpc服务 返回:{}",
                JSON.toJSONString(sysCompanyList));

        return sysCompanyList;
    }

    public List<Integer> queryRenterMerhantList(String renterId){
        List<Integer> renterCompanyList = new ArrayList<>();

        OperaResponse<List<Integer>> response = vendorsServiceClient.queryRenterMerchantList(renterId) ;

        log.debug("vendor 服务 queryRenterMerhantList 入参renterId： {},  返回值：{}",renterId, JSON.toJSONString(response));
        if (response.getCode() == 200) {
            renterCompanyList = response.getData() ;
        } else {
            log.warn("查询所有的商户信息 调用vendors rpc服务 错误!");
        }
        return renterCompanyList;

    }

    public String queryRenterId(String appId){
        String renterId = "" ;
        OperaResponse<String> response = vendorsServiceClient.queryRenterId(appId) ;

        log.debug("vendor 服务 queryRenterId 入参appId ： {},  返回值：{}",appId, JSON.toJSONString(response));
        if (response.getCode() == 200) {
            renterId = response.getData() ;
        } else {
            log.warn("查询所有的商户信息 调用vendors rpc服务 错误!");
        }
        return renterId;

    }

    public List<Integer> queryMerhantListByAppId(String appId){
        List<Integer> renterCompanyList = new ArrayList<>();

        OperaResponse<List<Integer>> response = vendorsServiceClient.queryAppIdMerchantList(appId) ;

        log.debug("vendor 服务 queryMerhantListByAppId 入参appId： {},  返回值：{}",appId, JSON.toJSONString(response));
        if (response.getCode() == 200) {
            renterCompanyList = response.getData() ;
        } else {
            log.warn("查询所有的商户信息 调用vendors rpc服务 错误!");
        }
        return renterCompanyList;

    }

    public List<String> queryAppIdList(String renterId){
        List<String> renterCompanyList = new ArrayList<>();

        OperaResponse<List<String>> response = vendorsServiceClient.queryAppIdList(renterId) ;

        log.debug("vendor 服务 queryAppIdList 入参renterId： {},  返回值：{}",renterId, JSON.toJSONString(response));
        if (response.getCode() == 200) {
            renterCompanyList = response.getData() ;
        } else {
            log.warn("查询所有的商户信息 调用vendors rpc服务 错误!");
        }
        return renterCompanyList;

    }
}
