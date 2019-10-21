package com.fengchao.sso.service;

import com.fengchao.sso.bean.LoginBean;
import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.bean.ThirdLoginBean;
import com.fengchao.sso.bean.TokenBean;
import com.fengchao.sso.model.Login;
import com.fengchao.sso.model.Token;
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
}
