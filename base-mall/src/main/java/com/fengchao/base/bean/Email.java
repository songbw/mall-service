package com.fengchao.base.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Email {

    private String[] email;//接收方邮件

    private String subject;//主题

    private String content;//邮件内容
}
