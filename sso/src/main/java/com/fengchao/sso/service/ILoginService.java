package com.fengchao.sso.service;

import com.fengchao.sso.bean.*;
import com.fengchao.sso.model.Login;
import com.fengchao.sso.util.OperaResult;

public interface ILoginService {

    Login selectByPrimaryName(String username);

    int insertSelective(LoginBean loginBean);

    int updatePassword(LoginBean loginBean);

    void updatePasswordByUsername(String username, String newPassword);

    TokenBean thirdLogin(ThirdLoginBean loginBean) ;

    OperaResult findThirdPartyToken(String iAppId, String initCode);

    OperaResult findThirdPartyTokenGAT(String iAppId, String initCode);

    OperaResult getPingAnOpenId(String iAppId, String requestCode) ;

    OperaResponse getWXOpenIdByAppIdAndCode(String appId, String code) ;

    OperaResponse verifyCode(String telephone, String type) ;

    OperaResponse bindWXOpenId(BindWXBean bandWXBean) ;

    OperaResponse wxBindVerify(String appId, String openId) ;

}
