package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ExportCardBean {

    private List<Integer> ids;
    private Short status;
    private String appId;
}
