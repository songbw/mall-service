package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Collect implements Serializable {

    private int type;
    private int points;
}
