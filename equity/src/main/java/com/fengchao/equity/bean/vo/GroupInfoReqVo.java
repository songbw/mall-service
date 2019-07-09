package com.fengchao.equity.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class GroupInfoReqVo extends PageVo {

    /**
     *
     */
    private Long id;

    /**
     * 活动名称
     */
    private String name;

    /**
     *
     */
    private String skuId;

    /**
     * 活动生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date effectiveStartDate;

    /**
     * 活动结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date effectiveEndDate;

    /**
     * 活动价格
     */
    private Integer groupBuyingPrice;

    /**
     * 活动商品总数量(库存量)
     */
    private Integer groupBuyingQuantity;

    /**
     * 成团人数
     */
    private Integer groupMemberQuantity;

    /**
     * 每人限购数量
     */
    private Integer limitedPerMember;

    /**
     *
     */
    private String description;

    /**
     * 活动状态（1：未开始，2：进行中，3：已结束）
     */
    private Integer groupStatus;

    /**
     * 逻辑删除标识 1:有效 2：无效
     */
    private Integer istatus;

    /**
     * 操作人id
     */
    private Integer operator;


}
