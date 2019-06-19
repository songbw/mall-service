package com.fengchao.pingan.service;

import com.fengchao.pingan.bean.TokenResult;
import com.fengchao.pingan.bean.UserResult;
import com.fengchao.pingan.exception.PinganClientException;

public interface UserService {

    TokenResult getAccessToken(String initCode) throws PinganClientException;

    UserResult getUserInfo(String userToken) throws PinganClientException;
}
