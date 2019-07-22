package com.fengchao.sso.bean;

import lombok.Data;

@Data
public class OpenId {
    private String open_id ;
    private String enterprise_open_id ;
    private String auth_code ;
}
