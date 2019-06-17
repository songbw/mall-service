package com.fengchao.equity.service;

import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.model.CouponTags;

public interface CouponTagsService {
    int createTags(CouponTags bean);

    PageBean findTags(Integer offset, Integer limit);

    int updateTags(CouponTags bean);

    int deleteTags(Integer id);
}
