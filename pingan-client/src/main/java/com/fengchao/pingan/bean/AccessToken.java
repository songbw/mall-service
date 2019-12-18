package com.fengchao.pingan.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccessToken implements Serializable {
    private String expiresIn;
    private String accessToken;
}
