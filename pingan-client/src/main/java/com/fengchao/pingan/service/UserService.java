package com.fengchao.pingan.service;

import com.fengchao.pingan.bean.*;
import com.fengchao.pingan.exception.PinganClientException;

public interface UserService {

    TokenResult getAccessToken(String initCode) throws PinganClientException;

    UserResult getUserInfo(String userToken) throws PinganClientException;

    /**
     * 获取init code
     * @param bean
     * @return
     */
    OperaResponse<InitCodeBean> getInitCode(InitCodeRequestBean bean) ;
}
