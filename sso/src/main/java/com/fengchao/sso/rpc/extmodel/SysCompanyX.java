package com.fengchao.sso.rpc.extmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 企业信息详情
 */
@Getter
@Setter
public class SysCompanyX {
    /**
     * 公司ID"
     */
    private Long id;

    /**
     * 公司名称
     */
    private String name;

    /**
     * 公司法人姓名
     */
    private String corporationName;

    /**
     * 公司法人身份证号
     */
    private String corporationId;

    /**
     * 公司营业执照链接
     */
    private String licenceUrl;

    /**
     * 公司法人电话
     */
    private String phone;

    /**
     * 公司注册地址
     */
    private String address;

    /**
     * 公司所属行业
     */
    private String industry;

    /**
     * 公司信息生成时间
     */
    private Date createTime;

    /**
     * 公司信息最近更新时间
     */
    private Date updateTime;

    /**
     * 公司情况注释
     */
    private String comments;

    /**
     * 公司状态,1.EDITING, 2.PENDING, 3.APPROVED, 4.REJECT
     */
    private Integer status;

}
