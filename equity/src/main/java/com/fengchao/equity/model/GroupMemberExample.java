package com.fengchao.equity.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupMemberExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GroupMemberExample() {
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

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andGroupInfoIdIsNull() {
            addCriterion("group_info_id is null");
            return (Criteria) this;
        }

        public Criteria andGroupInfoIdIsNotNull() {
            addCriterion("group_info_id is not null");
            return (Criteria) this;
        }

        public Criteria andGroupInfoIdEqualTo(Long value) {
            addCriterion("group_info_id =", value, "groupInfoId");
            return (Criteria) this;
        }

        public Criteria andGroupInfoIdNotEqualTo(Long value) {
            addCriterion("group_info_id <>", value, "groupInfoId");
            return (Criteria) this;
        }

        public Criteria andGroupInfoIdGreaterThan(Long value) {
            addCriterion("group_info_id >", value, "groupInfoId");
            return (Criteria) this;
        }

        public Criteria andGroupInfoIdGreaterThanOrEqualTo(Long value) {
            addCriterion("group_info_id >=", value, "groupInfoId");
            return (Criteria) this;
        }

        public Criteria andGroupInfoIdLessThan(Long value) {
            addCriterion("group_info_id <", value, "groupInfoId");
            return (Criteria) this;
        }

        public Criteria andGroupInfoIdLessThanOrEqualTo(Long value) {
            addCriterion("group_info_id <=", value, "groupInfoId");
            return (Criteria) this;
        }

        public Criteria andGroupInfoIdIn(List<Long> values) {
            addCriterion("group_info_id in", values, "groupInfoId");
            return (Criteria) this;
        }

        public Criteria andGroupInfoIdNotIn(List<Long> values) {
            addCriterion("group_info_id not in", values, "groupInfoId");
            return (Criteria) this;
        }

        public Criteria andGroupInfoIdBetween(Long value1, Long value2) {
            addCriterion("group_info_id between", value1, value2, "groupInfoId");
            return (Criteria) this;
        }

        public Criteria andGroupInfoIdNotBetween(Long value1, Long value2) {
            addCriterion("group_info_id not between", value1, value2, "groupInfoId");
            return (Criteria) this;
        }

        public Criteria andGroupTeamIdIsNull() {
            addCriterion("group_team_id is null");
            return (Criteria) this;
        }

        public Criteria andGroupTeamIdIsNotNull() {
            addCriterion("group_team_id is not null");
            return (Criteria) this;
        }

        public Criteria andGroupTeamIdEqualTo(Long value) {
            addCriterion("group_team_id =", value, "groupTeamId");
            return (Criteria) this;
        }

        public Criteria andGroupTeamIdNotEqualTo(Long value) {
            addCriterion("group_team_id <>", value, "groupTeamId");
            return (Criteria) this;
        }

        public Criteria andGroupTeamIdGreaterThan(Long value) {
            addCriterion("group_team_id >", value, "groupTeamId");
            return (Criteria) this;
        }

        public Criteria andGroupTeamIdGreaterThanOrEqualTo(Long value) {
            addCriterion("group_team_id >=", value, "groupTeamId");
            return (Criteria) this;
        }

        public Criteria andGroupTeamIdLessThan(Long value) {
            addCriterion("group_team_id <", value, "groupTeamId");
            return (Criteria) this;
        }

        public Criteria andGroupTeamIdLessThanOrEqualTo(Long value) {
            addCriterion("group_team_id <=", value, "groupTeamId");
            return (Criteria) this;
        }

        public Criteria andGroupTeamIdIn(List<Long> values) {
            addCriterion("group_team_id in", values, "groupTeamId");
            return (Criteria) this;
        }

        public Criteria andGroupTeamIdNotIn(List<Long> values) {
            addCriterion("group_team_id not in", values, "groupTeamId");
            return (Criteria) this;
        }

        public Criteria andGroupTeamIdBetween(Long value1, Long value2) {
            addCriterion("group_team_id between", value1, value2, "groupTeamId");
            return (Criteria) this;
        }

        public Criteria andGroupTeamIdNotBetween(Long value1, Long value2) {
            addCriterion("group_team_id not between", value1, value2, "groupTeamId");
            return (Criteria) this;
        }

        public Criteria andMemberOpenIdIsNull() {
            addCriterion("member_open_id is null");
            return (Criteria) this;
        }

        public Criteria andMemberOpenIdIsNotNull() {
            addCriterion("member_open_id is not null");
            return (Criteria) this;
        }

        public Criteria andMemberOpenIdEqualTo(String value) {
            addCriterion("member_open_id =", value, "memberOpenId");
            return (Criteria) this;
        }

        public Criteria andMemberOpenIdNotEqualTo(String value) {
            addCriterion("member_open_id <>", value, "memberOpenId");
            return (Criteria) this;
        }

        public Criteria andMemberOpenIdGreaterThan(String value) {
            addCriterion("member_open_id >", value, "memberOpenId");
            return (Criteria) this;
        }

        public Criteria andMemberOpenIdGreaterThanOrEqualTo(String value) {
            addCriterion("member_open_id >=", value, "memberOpenId");
            return (Criteria) this;
        }

        public Criteria andMemberOpenIdLessThan(String value) {
            addCriterion("member_open_id <", value, "memberOpenId");
            return (Criteria) this;
        }

        public Criteria andMemberOpenIdLessThanOrEqualTo(String value) {
            addCriterion("member_open_id <=", value, "memberOpenId");
            return (Criteria) this;
        }

        public Criteria andMemberOpenIdLike(String value) {
            addCriterion("member_open_id like", value, "memberOpenId");
            return (Criteria) this;
        }

        public Criteria andMemberOpenIdNotLike(String value) {
            addCriterion("member_open_id not like", value, "memberOpenId");
            return (Criteria) this;
        }

        public Criteria andMemberOpenIdIn(List<String> values) {
            addCriterion("member_open_id in", values, "memberOpenId");
            return (Criteria) this;
        }

        public Criteria andMemberOpenIdNotIn(List<String> values) {
            addCriterion("member_open_id not in", values, "memberOpenId");
            return (Criteria) this;
        }

        public Criteria andMemberOpenIdBetween(String value1, String value2) {
            addCriterion("member_open_id between", value1, value2, "memberOpenId");
            return (Criteria) this;
        }

        public Criteria andMemberOpenIdNotBetween(String value1, String value2) {
            addCriterion("member_open_id not between", value1, value2, "memberOpenId");
            return (Criteria) this;
        }

        public Criteria andIsSponserIsNull() {
            addCriterion("is_sponser is null");
            return (Criteria) this;
        }

        public Criteria andIsSponserIsNotNull() {
            addCriterion("is_sponser is not null");
            return (Criteria) this;
        }

        public Criteria andIsSponserEqualTo(Short value) {
            addCriterion("is_sponser =", value, "isSponser");
            return (Criteria) this;
        }

        public Criteria andIsSponserNotEqualTo(Short value) {
            addCriterion("is_sponser <>", value, "isSponser");
            return (Criteria) this;
        }

        public Criteria andIsSponserGreaterThan(Short value) {
            addCriterion("is_sponser >", value, "isSponser");
            return (Criteria) this;
        }

        public Criteria andIsSponserGreaterThanOrEqualTo(Short value) {
            addCriterion("is_sponser >=", value, "isSponser");
            return (Criteria) this;
        }

        public Criteria andIsSponserLessThan(Short value) {
            addCriterion("is_sponser <", value, "isSponser");
            return (Criteria) this;
        }

        public Criteria andIsSponserLessThanOrEqualTo(Short value) {
            addCriterion("is_sponser <=", value, "isSponser");
            return (Criteria) this;
        }

        public Criteria andIsSponserIn(List<Short> values) {
            addCriterion("is_sponser in", values, "isSponser");
            return (Criteria) this;
        }

        public Criteria andIsSponserNotIn(List<Short> values) {
            addCriterion("is_sponser not in", values, "isSponser");
            return (Criteria) this;
        }

        public Criteria andIsSponserBetween(Short value1, Short value2) {
            addCriterion("is_sponser between", value1, value2, "isSponser");
            return (Criteria) this;
        }

        public Criteria andIsSponserNotBetween(Short value1, Short value2) {
            addCriterion("is_sponser not between", value1, value2, "isSponser");
            return (Criteria) this;
        }

        public Criteria andOrderNoIsNull() {
            addCriterion("order_no is null");
            return (Criteria) this;
        }

        public Criteria andOrderNoIsNotNull() {
            addCriterion("order_no is not null");
            return (Criteria) this;
        }

        public Criteria andOrderNoEqualTo(String value) {
            addCriterion("order_no =", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotEqualTo(String value) {
            addCriterion("order_no <>", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoGreaterThan(String value) {
            addCriterion("order_no >", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoGreaterThanOrEqualTo(String value) {
            addCriterion("order_no >=", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoLessThan(String value) {
            addCriterion("order_no <", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoLessThanOrEqualTo(String value) {
            addCriterion("order_no <=", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoLike(String value) {
            addCriterion("order_no like", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotLike(String value) {
            addCriterion("order_no not like", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoIn(List<String> values) {
            addCriterion("order_no in", values, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotIn(List<String> values) {
            addCriterion("order_no not in", values, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoBetween(String value1, String value2) {
            addCriterion("order_no between", value1, value2, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotBetween(String value1, String value2) {
            addCriterion("order_no not between", value1, value2, "orderNo");
            return (Criteria) this;
        }

        public Criteria andMemberStatusIsNull() {
            addCriterion("member_status is null");
            return (Criteria) this;
        }

        public Criteria andMemberStatusIsNotNull() {
            addCriterion("member_status is not null");
            return (Criteria) this;
        }

        public Criteria andMemberStatusEqualTo(Short value) {
            addCriterion("member_status =", value, "memberStatus");
            return (Criteria) this;
        }

        public Criteria andMemberStatusNotEqualTo(Short value) {
            addCriterion("member_status <>", value, "memberStatus");
            return (Criteria) this;
        }

        public Criteria andMemberStatusGreaterThan(Short value) {
            addCriterion("member_status >", value, "memberStatus");
            return (Criteria) this;
        }

        public Criteria andMemberStatusGreaterThanOrEqualTo(Short value) {
            addCriterion("member_status >=", value, "memberStatus");
            return (Criteria) this;
        }

        public Criteria andMemberStatusLessThan(Short value) {
            addCriterion("member_status <", value, "memberStatus");
            return (Criteria) this;
        }

        public Criteria andMemberStatusLessThanOrEqualTo(Short value) {
            addCriterion("member_status <=", value, "memberStatus");
            return (Criteria) this;
        }

        public Criteria andMemberStatusIn(List<Short> values) {
            addCriterion("member_status in", values, "memberStatus");
            return (Criteria) this;
        }

        public Criteria andMemberStatusNotIn(List<Short> values) {
            addCriterion("member_status not in", values, "memberStatus");
            return (Criteria) this;
        }

        public Criteria andMemberStatusBetween(Short value1, Short value2) {
            addCriterion("member_status between", value1, value2, "memberStatus");
            return (Criteria) this;
        }

        public Criteria andMemberStatusNotBetween(Short value1, Short value2) {
            addCriterion("member_status not between", value1, value2, "memberStatus");
            return (Criteria) this;
        }

        public Criteria andIstatusIsNull() {
            addCriterion("istatus is null");
            return (Criteria) this;
        }

        public Criteria andIstatusIsNotNull() {
            addCriterion("istatus is not null");
            return (Criteria) this;
        }

        public Criteria andIstatusEqualTo(Short value) {
            addCriterion("istatus =", value, "istatus");
            return (Criteria) this;
        }

        public Criteria andIstatusNotEqualTo(Short value) {
            addCriterion("istatus <>", value, "istatus");
            return (Criteria) this;
        }

        public Criteria andIstatusGreaterThan(Short value) {
            addCriterion("istatus >", value, "istatus");
            return (Criteria) this;
        }

        public Criteria andIstatusGreaterThanOrEqualTo(Short value) {
            addCriterion("istatus >=", value, "istatus");
            return (Criteria) this;
        }

        public Criteria andIstatusLessThan(Short value) {
            addCriterion("istatus <", value, "istatus");
            return (Criteria) this;
        }

        public Criteria andIstatusLessThanOrEqualTo(Short value) {
            addCriterion("istatus <=", value, "istatus");
            return (Criteria) this;
        }

        public Criteria andIstatusIn(List<Short> values) {
            addCriterion("istatus in", values, "istatus");
            return (Criteria) this;
        }

        public Criteria andIstatusNotIn(List<Short> values) {
            addCriterion("istatus not in", values, "istatus");
            return (Criteria) this;
        }

        public Criteria andIstatusBetween(Short value1, Short value2) {
            addCriterion("istatus between", value1, value2, "istatus");
            return (Criteria) this;
        }

        public Criteria andIstatusNotBetween(Short value1, Short value2) {
            addCriterion("istatus not between", value1, value2, "istatus");
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