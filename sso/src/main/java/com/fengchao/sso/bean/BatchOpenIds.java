package com.fengchao.sso.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author song
 * @2020-03-09 10:13 上午
 **/
@Setter
@Getter
public class BatchOpenIds {
    private String appId ;
    private List<String> openIds ;
}
