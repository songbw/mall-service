package com.fengchao.equity.bean;

import lombok.Data;

@Data
public class ThirdOffShelfResult {
    private String sign;
    private String timestamp;
    private ThirdOffShelfParam data;
}
