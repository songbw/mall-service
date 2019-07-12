package com.fengchao.equity.bean.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class GroupTeamResVo {

    private Long id;
    private Long groupId;

    /**
     * 发起人id （团长）
     */
    private String sponserOpenId;

    /**
     * 团购活动商品mpuId
     */
    private String mpuId;

    /**
     * team 状态 1：新建 2：进行中(组团中) 3:满员中 4：组团成功，5：组团失效/失败
     */
    private Integer teamStatus;

    /**
     * 成团(team)时效（以“秒”为单位，超过此时间，该team状态值为：组团失败
     */
    private Integer teamExpiration;

    /**
     * 逻辑删除标识 1:有效 2：无效
     */
    private Integer istatus;

    private List<GroupMemberResVo> members;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
