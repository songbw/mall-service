package com.fengchao.equity.bean.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class GroupMemberResVo {

    private Long id;

    /**
     * 拼团活动ID
     */
    private Long groupId;

    /**
     * 拼团活动teamID
     */
    private Long groupTeamId;

    /**
     * 参购人id
     */
    private String memberOpenId;

    /**
     * 是否是团长 1:是 2.否
     */
    private Integer isSponser;

    /**
     * 订单no
     */
    private String orderNo;

    /**
     * 1:预备 2：正式 3：失效 4:退款中
     */
    private Integer memberStatus;

    /**
     * 1:有效 2：无效
     */
    private Integer istatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
