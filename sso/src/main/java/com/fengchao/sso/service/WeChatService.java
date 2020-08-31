package com.fengchao.sso.service;

import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.bean.WeChatAccessTokenBean;
import com.fengchao.sso.bean.WeChatUserInfoBean;

/**
 * @author songbw
 * @date 2019/12/13 10:42
 */
public interface WeChatService {

    OperaResponse<WeChatAccessTokenBean> getAccessToken(String appId, String code) ;

    OperaResponse<WeChatAccessTokenBean> getMiniAccessToken(String appId, String code) ;

    OperaResponse<WeChatUserInfoBean> getUserInfo(String accessToken, String openId) ;
}
