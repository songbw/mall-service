package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class InitCodeBean implements Serializable{
    private int expiresIn;
    private String initCode ;
}
