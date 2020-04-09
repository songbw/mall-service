package com.fengchao.equity.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CardTicketExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CardTicketExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andCardIdIsNull() {
            addCriterion("card_id is null");
            return (Criteria) this;
        }

        public Criteria andCardIdIsNotNull() {
            addCriterion("card_id is not null");
            return (Criteria) this;
        }

        public Criteria andCardIdEqualTo(Integer value) {
            addCriterion("card_id =", value, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdNotEqualTo(Integer value) {
            addCriterion("card_id <>", value, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdGreaterThan(Integer value) {
            addCriterion("card_id >", value, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("card_id >=", value, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdLessThan(Integer value) {
            addCriterion("card_id <", value, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdLessThanOrEqualTo(Integer value) {
            addCriterion("card_id <=", value, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdIn(List<Integer> values) {
            addCriterion("card_id in", values, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdNotIn(List<Integer> values) {
            addCriterion("card_id not in", values, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdBetween(Integer value1, Integer value2) {
            addCriterion("card_id between", value1, value2, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdNotBetween(Integer value1, Integer value2) {
            addCriterion("card_id not between", value1, value2, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIsNull() {
            addCriterion("card is null");
            return (Criteria) this;
        }

        public Criteria andCardIsNotNull() {
            addCriterion("card is not null");
            return (Criteria) this;
        }

        public Criteria andCardEqualTo(String value) {
            addCriterion("card =", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardNotEqualTo(String value) {
            addCriterion("card <>", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardGreaterThan(String value) {
            addCriterion("card >", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardGreaterThanOrEqualTo(String value) {
            addCriterion("card >=", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardLessThan(String value) {
            addCriterion("card <", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardLessThanOrEqualTo(String value) {
            addCriterion("card <=", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardLike(String value) {
            addCriterion("card like", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardNotLike(String value) {
            addCriterion("card not like", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardIn(List<String> values) {
            addCriterion("card in", values, "card");
            return (Criteria) this;
        }

        public Criteria andCardNotIn(List<String> values) {
            addCriterion("card not in", values, "card");
            return (Criteria) this;
        }

        public Criteria andCardBetween(String value1, String value2) {
            addCriterion("card between", value1, value2, "card");
            return (Criteria) this;
        }

        public Criteria andCardNotBetween(String value1, String value2) {
            addCriterion("card not between", value1, value2, "card");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNull() {
            addCriterion("password is null");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNotNull() {
            addCriterion("password is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordEqualTo(String value) {
            addCriterion("password =", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotEqualTo(String value) {
            addCriterion("password <>", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThan(String value) {
            addCriterion("password >", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("password >=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThan(String value) {
            addCriterion("password <", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThanOrEqualTo(String value) {
            addCriterion("password <=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLike(String value) {
            addCriterion("password like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotLike(String value) {
            addCriterion("password not like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordIn(List<String> values) {
            addCriterion("password in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotIn(List<String> values) {
            addCriterion("password not in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordBetween(String value1, String value2) {
            addCriterion("password between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotBetween(String value1, String value2) {
            addCriterion("password not between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andUserCouponCodeIsNull() {
            addCriterion("user_coupon_code is null");
            return (Criteria) this;
        }

        public Criteria andUserCouponCodeIsNotNull() {
            addCriterion("user_coupon_code is not null");
            return (Criteria) this;
        }

        public Criteria andUserCouponCodeEqualTo(String value) {
            addCriterion("user_coupon_code =", value, "userCouponCode");
            return (Criteria) this;
        }

        public Criteria andUserCouponCodeNotEqualTo(String value) {
            addCriterion("user_coupon_code <>", value, "userCouponCode");
            return (Criteria) this;
        }

        public Criteria andUserCouponCodeGreaterThan(String value) {
            addCriterion("user_coupon_code >", value, "userCouponCode");
            return (Criteria) this;
        }

        public Criteria andUserCouponCodeGreaterThanOrEqualTo(String value) {
            addCriterion("user_coupon_code >=", value, "userCouponCode");
            return (Criteria) this;
        }

        public Criteria andUserCouponCodeLessThan(String value) {
            addCriterion("user_coupon_code <", value, "userCouponCode");
            return (Criteria) this;
        }

        public Criteria andUserCouponCodeLessThanOrEqualTo(String value) {
            addCriterion("user_coupon_code <=", value, "userCouponCode");
            return (Criteria) this;
        }

        public Criteria andUserCouponCodeLike(String value) {
            addCriterion("user_coupon_code like", value, "userCouponCode");
            return (Criteria) this;
        }

        public Criteria andUserCouponCodeNotLike(String value) {
            addCriterion("user_coupon_code not like", value, "userCouponCode");
            return (Criteria) this;
        }

        public Criteria andUserCouponCodeIn(List<String> values) {
            addCriterion("user_coupon_code in", values, "userCouponCode");
            return (Criteria) this;
        }

        public Criteria andUserCouponCodeNotIn(List<String> values) {
            addCriterion("user_coupon_code not in", values, "userCouponCode");
            return (Criteria) this;
        }

        public Criteria andUserCouponCodeBetween(String value1, String value2) {
            addCriterion("user_coupon_code between", value1, value2, "userCouponCode");
            return (Criteria) this;
        }

        public Criteria andUserCouponCodeNotBetween(String value1, String value2) {
            addCriterion("user_coupon_code not between", value1, value2, "userCouponCode");
            return (Criteria) this;
        }

        public Criteria andOpenIdIsNull() {
            addCriterion("open_id is null");
            return (Criteria) this;
        }

        public Criteria andOpenIdIsNotNull() {
            addCriterion("open_id is not null");
            return (Criteria) this;
        }

        public Criteria andOpenIdEqualTo(String value) {
            addCriterion("open_id =", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdNotEqualTo(String value) {
            addCriterion("open_id <>", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdGreaterThan(String value) {
            addCriterion("open_id >", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdGreaterThanOrEqualTo(String value) {
            addCriterion("open_id >=", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdLessThan(String value) {
            addCriterion("open_id <", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdLessThanOrEqualTo(String value) {
            addCriterion("open_id <=", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdLike(String value) {
            addCriterion("open_id like", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdNotLike(String value) {
            addCriterion("open_id not like", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdIn(List<String> values) {
            addCriterion("open_id in", values, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdNotIn(List<String> values) {
            addCriterion("open_id not in", values, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdBetween(String value1, String value2) {
            addCriterion("open_id between", value1, value2, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdNotBetween(String value1, String value2) {
            addCriterion("open_id not between", value1, value2, "openId");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andActivateTimeIsNull() {
            addCriterion("activate_time is null");
            return (Criteria) this;
        }

        public Criteria andActivateTimeIsNotNull() {
            addCriterion("activate_time is not null");
            return (Criteria) this;
        }

        public Criteria andActivateTimeEqualTo(Date value) {
            addCriterion("activate_time =", value, "activateTime");
            return (Criteria) this;
        }

        public Criteria andActivateTimeNotEqualTo(Date value) {
            addCriterion("activate_time <>", value, "activateTime");
            return (Criteria) this;
        }

        public Criteria andActivateTimeGreaterThan(Date value) {
            addCriterion("activate_time >", value, "activateTime");
            return (Criteria) this;
        }

        public Criteria andActivateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("activate_time >=", value, "activateTime");
            return (Criteria) this;
        }

        public Criteria andActivateTimeLessThan(Date value) {
            addCriterion("activate_time <", value, "activateTime");
            return (Criteria) this;
        }

        public Criteria andActivateTimeLessThanOrEqualTo(Date value) {
            addCriterion("activate_time <=", value, "activateTime");
            return (Criteria) this;
        }

        public Criteria andActivateTimeIn(List<Date> values) {
            addCriterion("activate_time in", values, "activateTime");
            return (Criteria) this;
        }

        public Criteria andActivateTimeNotIn(List<Date> values) {
            addCriterion("activate_time not in", values, "activateTime");
            return (Criteria) this;
        }

        public Criteria andActivateTimeBetween(Date value1, Date value2) {
            addCriterion("activate_time between", value1, value2, "activateTime");
            return (Criteria) this;
        }

        public Criteria andActivateTimeNotBetween(Date value1, Date value2) {
            addCriterion("activate_time not between", value1, value2, "activateTime");
            return (Criteria) this;
        }

        public Criteria andConsumedTimeIsNull() {
            addCriterion("consumed_time is null");
            return (Criteria) this;
        }

        public Criteria andConsumedTimeIsNotNull() {
            addCriterion("consumed_time is not null");
            return (Criteria) this;
        }

        public Criteria andConsumedTimeEqualTo(Date value) {
            addCriterion("consumed_time =", value, "consumedTime");
            return (Criteria) this;
        }

        public Criteria andConsumedTimeNotEqualTo(Date value) {
            addCriterion("consumed_time <>", value, "consumedTime");
            return (Criteria) this;
        }

        public Criteria andConsumedTimeGreaterThan(Date value) {
            addCriterion("consumed_time >", value, "consumedTime");
            return (Criteria) this;
        }

        public Criteria andConsumedTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("consumed_time >=", value, "consumedTime");
            return (Criteria) this;
        }

        public Criteria andConsumedTimeLessThan(Date value) {
            addCriterion("consumed_time <", value, "consumedTime");
            return (Criteria) this;
        }

        public Criteria andConsumedTimeLessThanOrEqualTo(Date value) {
            addCriterion("consumed_time <=", value, "consumedTime");
            return (Criteria) this;
        }

        public Criteria andConsumedTimeIn(List<Date> values) {
            addCriterion("consumed_time in", values, "consumedTime");
            return (Criteria) this;
        }

        public Criteria andConsumedTimeNotIn(List<Date> values) {
            addCriterion("consumed_time not in", values, "consumedTime");
            return (Criteria) this;
        }

        public Criteria andConsumedTimeBetween(Date value1, Date value2) {
            addCriterion("consumed_time between", value1, value2, "consumedTime");
            return (Criteria) this;
        }

        public Criteria andConsumedTimeNotBetween(Date value1, Date value2) {
            addCriterion("consumed_time not between", value1, value2, "consumedTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("end_time is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("end_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(Date value) {
            addCriterion("end_time =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Date value) {
            addCriterion("end_time <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Date value) {
            addCriterion("end_time >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("end_time >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Date value) {
            addCriterion("end_time <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("end_time <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Date> values) {
            addCriterion("end_time in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Date> values) {
            addCriterion("end_time not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Date value1, Date value2) {
            addCriterion("end_time between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("end_time not between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Short value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Short value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Short value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Short value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Short value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Short value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Short> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Short> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Short value1, Short value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Short value1, Short value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNull() {
            addCriterion("is_delete is null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNotNull() {
            addCriterion("is_delete is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteEqualTo(Short value) {
            addCriterion("is_delete =", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotEqualTo(Short value) {
            addCriterion("is_delete <>", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThan(Short value) {
            addCriterion("is_delete >", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThanOrEqualTo(Short value) {
            addCriterion("is_delete >=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThan(Short value) {
            addCriterion("is_delete <", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThanOrEqualTo(Short value) {
            addCriterion("is_delete <=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIn(List<Short> values) {
            addCriterion("is_delete in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotIn(List<Short> values) {
            addCriterion("is_delete not in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteBetween(Short value1, Short value2) {
            addCriterion("is_delete between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotBetween(Short value1, Short value2) {
            addCriterion("is_delete not between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andCorporationCodeIsNull() {
            addCriterion("corporation_code is null");
            return (Criteria) this;
        }

        public Criteria andCorporationCodeIsNotNull() {
            addCriterion("corporation_code is not null");
            return (Criteria) this;
        }

        public Criteria andCorporationCodeEqualTo(String value) {
            addCriterion("corporation_code =", value, "corporationCode");
            return (Criteria) this;
        }

        public Criteria andCorporationCodeNotEqualTo(String value) {
            addCriterion("corporation_code <>", value, "corporationCode");
            return (Criteria) this;
        }

        public Criteria andCorporationCodeGreaterThan(String value) {
            addCriterion("corporation_code >", value, "corporationCode");
            return (Criteria) this;
        }

        public Criteria andCorporationCodeGreaterThanOrEqualTo(String value) {
            addCriterion("corporation_code >=", value, "corporationCode");
            return (Criteria) this;
        }

        public Criteria andCorporationCodeLessThan(String value) {
            addCriterion("corporation_code <", value, "corporationCode");
            return (Criteria) this;
        }

        public Criteria andCorporationCodeLessThanOrEqualTo(String value) {
            addCriterion("corporation_code <=", value, "corporationCode");
            return (Criteria) this;
        }

        public Criteria andCorporationCodeLike(String value) {
            addCriterion("corporation_code like", value, "corporationCode");
            return (Criteria) this;
        }

        public Criteria andCorporationCodeNotLike(String value) {
            addCriterion("corporation_code not like", value, "corporationCode");
            return (Criteria) this;
        }

        public Criteria andCorporationCodeIn(List<String> values) {
            addCriterion("corporation_code in", values, "corporationCode");
            return (Criteria) this;
        }

        public Criteria andCorporationCodeNotIn(List<String> values) {
            addCriterion("corporation_code not in", values, "corporationCode");
            return (Criteria) this;
        }

        public Criteria andCorporationCodeBetween(String value1, String value2) {
            addCriterion("corporation_code between", value1, value2, "corporationCode");
            return (Criteria) this;
        }

        public Criteria andCorporationCodeNotBetween(String value1, String value2) {
            addCriterion("corporation_code not between", value1, value2, "corporationCode");
            return (Criteria) this;
        }

        public Criteria andWelfareOrderNoIsNull() {
            addCriterion("welfare_order_no is null");
            return (Criteria) this;
        }

        public Criteria andWelfareOrderNoIsNotNull() {
            addCriterion("welfare_order_no is not null");
            return (Criteria) this;
        }

        public Criteria andWelfareOrderNoEqualTo(String value) {
            addCriterion("welfare_order_no =", value, "welfareOrderNo");
            return (Criteria) this;
        }

        public Criteria andWelfareOrderNoNotEqualTo(String value) {
            addCriterion("welfare_order_no <>", value, "welfareOrderNo");
            return (Criteria) this;
        }

        public Criteria andWelfareOrderNoGreaterThan(String value) {
            addCriterion("welfare_order_no >", value, "welfareOrderNo");
            return (Criteria) this;
        }

        public Criteria andWelfareOrderNoGreaterThanOrEqualTo(String value) {
            addCriterion("welfare_order_no >=", value, "welfareOrderNo");
            return (Criteria) this;
        }

        public Criteria andWelfareOrderNoLessThan(String value) {
            addCriterion("welfare_order_no <", value, "welfareOrderNo");
            return (Criteria) this;
        }

        public Criteria andWelfareOrderNoLessThanOrEqualTo(String value) {
            addCriterion("welfare_order_no <=", value, "welfareOrderNo");
            return (Criteria) this;
        }

        public Criteria andWelfareOrderNoLike(String value) {
            addCriterion("welfare_order_no like", value, "welfareOrderNo");
            return (Criteria) this;
        }

        public Criteria andWelfareOrderNoNotLike(String value) {
            addCriterion("welfare_order_no not like", value, "welfareOrderNo");
            return (Criteria) this;
        }

        public Criteria andWelfareOrderNoIn(List<String> values) {
            addCriterion("welfare_order_no in", values, "welfareOrderNo");
            return (Criteria) this;
        }

        public Criteria andWelfareOrderNoNotIn(List<String> values) {
            addCriterion("welfare_order_no not in", values, "welfareOrderNo");
            return (Criteria) this;
        }

        public Criteria andWelfareOrderNoBetween(String value1, String value2) {
            addCriterion("welfare_order_no between", value1, value2, "welfareOrderNo");
            return (Criteria) this;
        }

        public Criteria andWelfareOrderNoNotBetween(String value1, String value2) {
            addCriterion("welfare_order_no not between", value1, value2, "welfareOrderNo");
            return (Criteria) this;
        }

        public Criteria andCardInfoCodeIsNull() {
            addCriterion("card_info_code is null");
            return (Criteria) this;
        }

        public Criteria andCardInfoCodeIsNotNull() {
            addCriterion("card_info_code is not null");
            return (Criteria) this;
        }

        public Criteria andCardInfoCodeEqualTo(String value) {
            addCriterion("card_info_code =", value, "cardInfoCode");
            return (Criteria) this;
        }

        public Criteria andCardInfoCodeNotEqualTo(String value) {
            addCriterion("card_info_code <>", value, "cardInfoCode");
            return (Criteria) this;
        }

        public Criteria andCardInfoCodeGreaterThan(String value) {
            addCriterion("card_info_code >", value, "cardInfoCode");
            return (Criteria) this;
        }

        public Criteria andCardInfoCodeGreaterThanOrEqualTo(String value) {
            addCriterion("card_info_code >=", value, "cardInfoCode");
            return (Criteria) this;
        }

        public Criteria andCardInfoCodeLessThan(String value) {
            addCriterion("card_info_code <", value, "cardInfoCode");
            return (Criteria) this;
        }

        public Criteria andCardInfoCodeLessThanOrEqualTo(String value) {
            addCriterion("card_info_code <=", value, "cardInfoCode");
            return (Criteria) this;
        }

        public Criteria andCardInfoCodeLike(String value) {
            addCriterion("card_info_code like", value, "cardInfoCode");
            return (Criteria) this;
        }

        public Criteria andCardInfoCodeNotLike(String value) {
            addCriterion("card_info_code not like", value, "cardInfoCode");
            return (Criteria) this;
        }

        public Criteria andCardInfoCodeIn(List<String> values) {
            addCriterion("card_info_code in", values, "cardInfoCode");
            return (Criteria) this;
        }

        public Criteria andCardInfoCodeNotIn(List<String> values) {
            addCriterion("card_info_code not in", values, "cardInfoCode");
            return (Criteria) this;
        }

        public Criteria andCardInfoCodeBetween(String value1, String value2) {
            addCriterion("card_info_code between", value1, value2, "cardInfoCode");
            return (Criteria) this;
        }

        public Criteria andCardInfoCodeNotBetween(String value1, String value2) {
            addCriterion("card_info_code not between", value1, value2, "cardInfoCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeIsNull() {
            addCriterion("employee_code is null");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeIsNotNull() {
            addCriterion("employee_code is not null");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeEqualTo(String value) {
            addCriterion("employee_code =", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeNotEqualTo(String value) {
            addCriterion("employee_code <>", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeGreaterThan(String value) {
            addCriterion("employee_code >", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeGreaterThanOrEqualTo(String value) {
            addCriterion("employee_code >=", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeLessThan(String value) {
            addCriterion("employee_code <", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeLessThanOrEqualTo(String value) {
            addCriterion("employee_code <=", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeLike(String value) {
            addCriterion("employee_code like", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeNotLike(String value) {
            addCriterion("employee_code not like", value, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeIn(List<String> values) {
            addCriterion("employee_code in", values, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeNotIn(List<String> values) {
            addCriterion("employee_code not in", values, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeBetween(String value1, String value2) {
            addCriterion("employee_code between", value1, value2, "employeeCode");
            return (Criteria) this;
        }

        public Criteria andEmployeeCodeNotBetween(String value1, String value2) {
            addCriterion("employee_code not between", value1, value2, "employeeCode");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}