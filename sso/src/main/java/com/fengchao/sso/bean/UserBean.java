package com.fengchao.sso.bean;

import lombok.Data;

import java.util.Date;

@Data
public class UserBean {
    private Integer id;
    private String username;
    private String openId;
    private String nickname;
    private String headImg;
    private String name;
    private String sex;
    private Integer age;
    private String idcard;
    private String portrait;
    private Integer status;
    private Integer loginId;
    private java.sql.Date birth;
    private String telephone;
    private Date createdAt;
    private Date updatedAt;
}
