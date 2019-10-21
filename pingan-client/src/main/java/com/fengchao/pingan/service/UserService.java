package com.fengchao.pingan.service;

import com.fengchao.pingan.bean.*;
import com.fengchao.pingan.exception.PinganClientException;

public interface UserService {

    TokenResult getAccessToken(String initCode) throws PinganClientException;

    UserResult getUserInfo(String userToken) throws PinganClientException;

    /**
     * 获取init code
     * @return
     */
    OperaResponse<InitCodeBean> getInitCode() ;

    /**
     * 获取auth code
     * @return
     */
    OperaResponse<AuthCodeBean> getAuthCode() ;

    /**
     * 获取 access token
     * @param authCode
     * @return
     */
    OperaResponse<AccessToken> getAuthAccessToken(String authCode) ;

    /**
     * 刷新 accessToken
     * @param refreshToken
     * @return
     */
    OperaResponse<AccessToken> getRefreshToken(String refreshToken) ;

    /**
     * 校验 accessToken
     * @param accessToken
     * @return
     */
    OperaResponse<CheckTokenBean> checkToken(String accessToken) ;

    /**
     * 校验 RequestCode
     * @param requestCode
     * @return
     */
    OperaResponse<CheckTokenBean> checkRequestCode(String requestCode) ;

    /**
     * 获取用户信息
     * @param userAccessToken
     * @return
     */
    OperaResponse<AuthUserBean> getAuthUserInfo(String userAccessToken) ;

    /**
     * 根据request code获取用户信息
     * @param requestCode
     * @return
     */
    OperaResponse<AuthUserBean> getAuthUserInfoByRequestCode(String requestCode) ;
}
