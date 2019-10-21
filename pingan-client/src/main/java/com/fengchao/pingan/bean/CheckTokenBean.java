package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CheckTokenBean implements Serializable {
    private boolean verifyResult;
}
