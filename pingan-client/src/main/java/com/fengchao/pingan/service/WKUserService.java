package com.fengchao.pingan.service;

import com.fengchao.pingan.bean.WKOperaResponse;
import com.fengchao.pingan.bean.WKUserInfo;
import com.fengchao.pingan.bean.WKUserRequestBean;

/**
 * @author song
 * @2020-02-26 4:12 下午
 **/
public interface WKUserService {

    WKOperaResponse<WKUserInfo> getWKUserInfo(WKUserRequestBean bean);
}
