package com.fengchao.order.rpc;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.OrderBean;
import com.fengchao.order.feign.VendorsServiceClient;
import com.fengchao.order.rpc.extmodel.SysCompanyX;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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

    public List<Integer> queryRenterMerhantList(String renterId){
        List<Integer> renterCompanyList = new ArrayList<>();

        OperaResponse<List<Integer>> response = vendorsServiceClient.queryRenterMerchantList(renterId) ;

        log.info("vendor 服务 queryRenterMerhantList 入参renterId： {},  返回值：{}",renterId, JSONUtil.toJsonString(response));
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

    public List<String> queryAppIdListByRenterId(String renterId) {
        List<String> appIds = new ArrayList<>();

        OperaResponse<List<String>> response = vendorsServiceClient.queryAppIdList(renterId) ;

        log.info("vendor 服务 queryAppIdListByRenterId 入参renterId： {},  返回值：{}",renterId, JSONUtil.toJsonString(response));
        if (response.getCode() == 200) {
            appIds = response.getData() ;
        } else {
            log.warn("查询所有的商户信息 调用vendors rpc服务 错误!");
        }
        return appIds;
    }

    public List<String> queryAppIdListByMerchantId(Integer merchantId) {
        List<String> appIds = new ArrayList<>();

        OperaResponse<List<String>> response = vendorsServiceClient.queryAppIdsByMerchantId(merchantId) ;

        log.info("vendor 服务 queryAppIdListByRenterId 入参merchantId： {},  返回值：{}",merchantId, JSONUtil.toJsonString(response));
        if (response.getCode() == 200) {
            appIds = response.getData() ;
        } else {
            log.warn("查询所有的商户信息 调用vendors rpc服务 错误!");
        }
        return appIds;
    }

    public void setMerchantListForOrderBean(OrderBean queryBean) {
        log.info("setMerchantListForOrderBean 入参：{}", JSONUtil.toJsonString(queryBean));
        List<String> appIds = null ;
        if ("0".equals(queryBean.getRenterHeader())) {
            // 平台管理员
            // 获取所有租户下的所有商户信息
            if (queryBean.getMerchantHeader() != 0) {
                appIds = queryAppIdListByMerchantId(queryBean.getMerchantHeader()) ;
            }
            if (StringUtils.isBlank(queryBean.getAppId()) && queryBean.getMerchantHeader() == 0) {
                if (StringUtils.isNotBlank(queryBean.getRenterId())) {
                    appIds = queryAppIdListByRenterId(queryBean.getRenterId()) ;
                }
            }

        } else {
            // 租户
            appIds = queryAppIdListByRenterId(queryBean.getRenterHeader()) ;
        }
        queryBean.setAppIds(appIds);
        log.info("setMerchantListForOrderBean 返回值：{}", JSONUtil.toJsonString(queryBean));
    }
}
