package com.fengchao.aggregation.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Email {

    /**
     * 接收方邮件
     */
    private String[] email;//

    /**
     * 主题
     */
    private String subject;//

    /**
     * 邮件内容
     */
    private String content;//
}
