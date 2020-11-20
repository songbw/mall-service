package com.fengchao.product.aoyi.rpc;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.feign.VendorsServiceClient;
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

    public SysCompany queryMerchantById(Integer merchantId) {
        // 返回值
        SysCompany sysCompany = new SysCompany();

        // 执行rpc调用
        log.info("查询所有的商户信息 调用vendors rpc服务 入参:无");

        // 将merchantIdList转成Long型
        OperaResponse<SysCompany> resultObject = vendorsServiceClient.vendorInfo(merchantId);
        log.info("查询所有的商户信息 调用vendors rpc服务 返回:{}", JSONUtil.toJsonString(resultObject));

        // 处理返回
        if (resultObject.getCode() == 200) {
            sysCompany = resultObject.getData();
        } else {
            log.warn("查询所有的商户信息 调用vendors rpc服务 错误!");
        }

        log.info("VendorsRpcService#queryMerchantById调用vendors rpc服务 返回:{}",
                JSONUtil.toJsonString(sysCompany));
        return sysCompany;
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

    public List<String> queryAppIdList(String renterId){
        List<String> renterCompanyList = new ArrayList<>();

        OperaResponse<List<String>> response = vendorsServiceClient.queryAppIdList(renterId) ;

        log.debug("vendor 服务 queryAppIdList 入参renterId： {},  返回值：{}",renterId, JSONUtil.toJsonString(response));
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

        log.debug("vendor 服务 queryRenterId 入参appId ： {},  返回值：{}",appId, JSONUtil.toJsonString(response));
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

        log.debug("vendor 服务 queryMerhantListByAppId 入参appId： {},  返回值：{}",appId, JSONUtil.toJsonString(response));
        if (response.getCode() == 200) {
            renterCompanyList = response.getData() ;
        } else {
            log.warn("查询所有的商户信息 调用vendors rpc服务 错误!");
        }
        return renterCompanyList;

    }

    public List<SysCompany> queryMerchantListByIds(List<Integer> ids) {
        // 返回值
        List<SysCompany> sysCompanyList = new ArrayList<>();

        // 执行rpc调用
        log.info("查询所有的商户信息 调用vendors rpc服务 入参:无");

        // 将merchantIdList转成Long型
        OperaResponse<List<SysCompany>> resultObject = vendorsServiceClient.vendorInfoByIds(ids);
        log.info("查询所有的商户信息 调用vendors rpc服务 返回:{}", JSONUtil.toJsonString(resultObject));

        // 处理返回
        if (resultObject.getCode() == 200) {
            sysCompanyList = resultObject.getData();
        } else {
            log.warn("查询所有的商户信息 调用vendors rpc服务 错误!");
        }

        log.info("VendorsRpcService#queryMerchantListByIds 调用vendors rpc服务 返回:{}",
                JSONUtil.toJsonString(sysCompanyList));

        return sysCompanyList;
    }
}
