package com.fengchao.product.aoyi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeipinhuiAddressExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public WeipinhuiAddressExample() {
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

        public Criteria andSnCodeIsNull() {
            addCriterion("sn_code is null");
            return (Criteria) this;
        }

        public Criteria andSnCodeIsNotNull() {
            addCriterion("sn_code is not null");
            return (Criteria) this;
        }

        public Criteria andSnCodeEqualTo(String value) {
            addCriterion("sn_code =", value, "snCode");
            return (Criteria) this;
        }

        public Criteria andSnCodeNotEqualTo(String value) {
            addCriterion("sn_code <>", value, "snCode");
            return (Criteria) this;
        }

        public Criteria andSnCodeGreaterThan(String value) {
            addCriterion("sn_code >", value, "snCode");
            return (Criteria) this;
        }

        public Criteria andSnCodeGreaterThanOrEqualTo(String value) {
            addCriterion("sn_code >=", value, "snCode");
            return (Criteria) this;
        }

        public Criteria andSnCodeLessThan(String value) {
            addCriterion("sn_code <", value, "snCode");
            return (Criteria) this;
        }

        public Criteria andSnCodeLessThanOrEqualTo(String value) {
            addCriterion("sn_code <=", value, "snCode");
            return (Criteria) this;
        }

        public Criteria andSnCodeLike(String value) {
            addCriterion("sn_code like", value, "snCode");
            return (Criteria) this;
        }

        public Criteria andSnCodeNotLike(String value) {
            addCriterion("sn_code not like", value, "snCode");
            return (Criteria) this;
        }

        public Criteria andSnCodeIn(List<String> values) {
            addCriterion("sn_code in", values, "snCode");
            return (Criteria) this;
        }

        public Criteria andSnCodeNotIn(List<String> values) {
            addCriterion("sn_code not in", values, "snCode");
            return (Criteria) this;
        }

        public Criteria andSnCodeBetween(String value1, String value2) {
            addCriterion("sn_code between", value1, value2, "snCode");
            return (Criteria) this;
        }

        public Criteria andSnCodeNotBetween(String value1, String value2) {
            addCriterion("sn_code not between", value1, value2, "snCode");
            return (Criteria) this;
        }

        public Criteria andSnPcodeIsNull() {
            addCriterion("sn_pcode is null");
            return (Criteria) this;
        }

        public Criteria andSnPcodeIsNotNull() {
            addCriterion("sn_pcode is not null");
            return (Criteria) this;
        }

        public Criteria andSnPcodeEqualTo(String value) {
            addCriterion("sn_pcode =", value, "snPcode");
            return (Criteria) this;
        }

        public Criteria andSnPcodeNotEqualTo(String value) {
            addCriterion("sn_pcode <>", value, "snPcode");
            return (Criteria) this;
        }

        public Criteria andSnPcodeGreaterThan(String value) {
            addCriterion("sn_pcode >", value, "snPcode");
            return (Criteria) this;
        }

        public Criteria andSnPcodeGreaterThanOrEqualTo(String value) {
            addCriterion("sn_pcode >=", value, "snPcode");
            return (Criteria) this;
        }

        public Criteria andSnPcodeLessThan(String value) {
            addCriterion("sn_pcode <", value, "snPcode");
            return (Criteria) this;
        }

        public Criteria andSnPcodeLessThanOrEqualTo(String value) {
            addCriterion("sn_pcode <=", value, "snPcode");
            return (Criteria) this;
        }

        public Criteria andSnPcodeLike(String value) {
            addCriterion("sn_pcode like", value, "snPcode");
            return (Criteria) this;
        }

        public Criteria andSnPcodeNotLike(String value) {
            addCriterion("sn_pcode not like", value, "snPcode");
            return (Criteria) this;
        }

        public Criteria andSnPcodeIn(List<String> values) {
            addCriterion("sn_pcode in", values, "snPcode");
            return (Criteria) this;
        }

        public Criteria andSnPcodeNotIn(List<String> values) {
            addCriterion("sn_pcode not in", values, "snPcode");
            return (Criteria) this;
        }

        public Criteria andSnPcodeBetween(String value1, String value2) {
            addCriterion("sn_pcode between", value1, value2, "snPcode");
            return (Criteria) this;
        }

        public Criteria andSnPcodeNotBetween(String value1, String value2) {
            addCriterion("sn_pcode not between", value1, value2, "snPcode");
            return (Criteria) this;
        }

        public Criteria andSnNameIsNull() {
            addCriterion("sn_name is null");
            return (Criteria) this;
        }

        public Criteria andSnNameIsNotNull() {
            addCriterion("sn_name is not null");
            return (Criteria) this;
        }

        public Criteria andSnNameEqualTo(String value) {
            addCriterion("sn_name =", value, "snName");
            return (Criteria) this;
        }

        public Criteria andSnNameNotEqualTo(String value) {
            addCriterion("sn_name <>", value, "snName");
            return (Criteria) this;
        }

        public Criteria andSnNameGreaterThan(String value) {
            addCriterion("sn_name >", value, "snName");
            return (Criteria) this;
        }

        public Criteria andSnNameGreaterThanOrEqualTo(String value) {
            addCriterion("sn_name >=", value, "snName");
            return (Criteria) this;
        }

        public Criteria andSnNameLessThan(String value) {
            addCriterion("sn_name <", value, "snName");
            return (Criteria) this;
        }

        public Criteria andSnNameLessThanOrEqualTo(String value) {
            addCriterion("sn_name <=", value, "snName");
            return (Criteria) this;
        }

        public Criteria andSnNameLike(String value) {
            addCriterion("sn_name like", value, "snName");
            return (Criteria) this;
        }

        public Criteria andSnNameNotLike(String value) {
            addCriterion("sn_name not like", value, "snName");
            return (Criteria) this;
        }

        public Criteria andSnNameIn(List<String> values) {
            addCriterion("sn_name in", values, "snName");
            return (Criteria) this;
        }

        public Criteria andSnNameNotIn(List<String> values) {
            addCriterion("sn_name not in", values, "snName");
            return (Criteria) this;
        }

        public Criteria andSnNameBetween(String value1, String value2) {
            addCriterion("sn_name between", value1, value2, "snName");
            return (Criteria) this;
        }

        public Criteria andSnNameNotBetween(String value1, String value2) {
            addCriterion("sn_name not between", value1, value2, "snName");
            return (Criteria) this;
        }

        public Criteria andWphCodeIsNull() {
            addCriterion("wph_code is null");
            return (Criteria) this;
        }

        public Criteria andWphCodeIsNotNull() {
            addCriterion("wph_code is not null");
            return (Criteria) this;
        }

        public Criteria andWphCodeEqualTo(String value) {
            addCriterion("wph_code =", value, "wphCode");
            return (Criteria) this;
        }

        public Criteria andWphCodeNotEqualTo(String value) {
            addCriterion("wph_code <>", value, "wphCode");
            return (Criteria) this;
        }

        public Criteria andWphCodeGreaterThan(String value) {
            addCriterion("wph_code >", value, "wphCode");
            return (Criteria) this;
        }

        public Criteria andWphCodeGreaterThanOrEqualTo(String value) {
            addCriterion("wph_code >=", value, "wphCode");
            return (Criteria) this;
        }

        public Criteria andWphCodeLessThan(String value) {
            addCriterion("wph_code <", value, "wphCode");
            return (Criteria) this;
        }

        public Criteria andWphCodeLessThanOrEqualTo(String value) {
            addCriterion("wph_code <=", value, "wphCode");
            return (Criteria) this;
        }

        public Criteria andWphCodeLike(String value) {
            addCriterion("wph_code like", value, "wphCode");
            return (Criteria) this;
        }

        public Criteria andWphCodeNotLike(String value) {
            addCriterion("wph_code not like", value, "wphCode");
            return (Criteria) this;
        }

        public Criteria andWphCodeIn(List<String> values) {
            addCriterion("wph_code in", values, "wphCode");
            return (Criteria) this;
        }

        public Criteria andWphCodeNotIn(List<String> values) {
            addCriterion("wph_code not in", values, "wphCode");
            return (Criteria) this;
        }

        public Criteria andWphCodeBetween(String value1, String value2) {
            addCriterion("wph_code between", value1, value2, "wphCode");
            return (Criteria) this;
        }

        public Criteria andWphCodeNotBetween(String value1, String value2) {
            addCriterion("wph_code not between", value1, value2, "wphCode");
            return (Criteria) this;
        }

        public Criteria andWphPcodeIsNull() {
            addCriterion("wph_pcode is null");
            return (Criteria) this;
        }

        public Criteria andWphPcodeIsNotNull() {
            addCriterion("wph_pcode is not null");
            return (Criteria) this;
        }

        public Criteria andWphPcodeEqualTo(String value) {
            addCriterion("wph_pcode =", value, "wphPcode");
            return (Criteria) this;
        }

        public Criteria andWphPcodeNotEqualTo(String value) {
            addCriterion("wph_pcode <>", value, "wphPcode");
            return (Criteria) this;
        }

        public Criteria andWphPcodeGreaterThan(String value) {
            addCriterion("wph_pcode >", value, "wphPcode");
            return (Criteria) this;
        }

        public Criteria andWphPcodeGreaterThanOrEqualTo(String value) {
            addCriterion("wph_pcode >=", value, "wphPcode");
            return (Criteria) this;
        }

        public Criteria andWphPcodeLessThan(String value) {
            addCriterion("wph_pcode <", value, "wphPcode");
            return (Criteria) this;
        }

        public Criteria andWphPcodeLessThanOrEqualTo(String value) {
            addCriterion("wph_pcode <=", value, "wphPcode");
            return (Criteria) this;
        }

        public Criteria andWphPcodeLike(String value) {
            addCriterion("wph_pcode like", value, "wphPcode");
            return (Criteria) this;
        }

        public Criteria andWphPcodeNotLike(String value) {
            addCriterion("wph_pcode not like", value, "wphPcode");
            return (Criteria) this;
        }

        public Criteria andWphPcodeIn(List<String> values) {
            addCriterion("wph_pcode in", values, "wphPcode");
            return (Criteria) this;
        }

        public Criteria andWphPcodeNotIn(List<String> values) {
            addCriterion("wph_pcode not in", values, "wphPcode");
            return (Criteria) this;
        }

        public Criteria andWphPcodeBetween(String value1, String value2) {
            addCriterion("wph_pcode between", value1, value2, "wphPcode");
            return (Criteria) this;
        }

        public Criteria andWphPcodeNotBetween(String value1, String value2) {
            addCriterion("wph_pcode not between", value1, value2, "wphPcode");
            return (Criteria) this;
        }

        public Criteria andWphNameIsNull() {
            addCriterion("wph_name is null");
            return (Criteria) this;
        }

        public Criteria andWphNameIsNotNull() {
            addCriterion("wph_name is not null");
            return (Criteria) this;
        }

        public Criteria andWphNameEqualTo(String value) {
            addCriterion("wph_name =", value, "wphName");
            return (Criteria) this;
        }

        public Criteria andWphNameNotEqualTo(String value) {
            addCriterion("wph_name <>", value, "wphName");
            return (Criteria) this;
        }

        public Criteria andWphNameGreaterThan(String value) {
            addCriterion("wph_name >", value, "wphName");
            return (Criteria) this;
        }

        public Criteria andWphNameGreaterThanOrEqualTo(String value) {
            addCriterion("wph_name >=", value, "wphName");
            return (Criteria) this;
        }

        public Criteria andWphNameLessThan(String value) {
            addCriterion("wph_name <", value, "wphName");
            return (Criteria) this;
        }

        public Criteria andWphNameLessThanOrEqualTo(String value) {
            addCriterion("wph_name <=", value, "wphName");
            return (Criteria) this;
        }

        public Criteria andWphNameLike(String value) {
            addCriterion("wph_name like", value, "wphName");
            return (Criteria) this;
        }

        public Criteria andWphNameNotLike(String value) {
            addCriterion("wph_name not like", value, "wphName");
            return (Criteria) this;
        }

        public Criteria andWphNameIn(List<String> values) {
            addCriterion("wph_name in", values, "wphName");
            return (Criteria) this;
        }

        public Criteria andWphNameNotIn(List<String> values) {
            addCriterion("wph_name not in", values, "wphName");
            return (Criteria) this;
        }

        public Criteria andWphNameBetween(String value1, String value2) {
            addCriterion("wph_name between", value1, value2, "wphName");
            return (Criteria) this;
        }

        public Criteria andWphNameNotBetween(String value1, String value2) {
            addCriterion("wph_name not between", value1, value2, "wphName");
            return (Criteria) this;
        }

        public Criteria andLevelIsNull() {
            addCriterion("level is null");
            return (Criteria) this;
        }

        public Criteria andLevelIsNotNull() {
            addCriterion("level is not null");
            return (Criteria) this;
        }

        public Criteria andLevelEqualTo(Integer value) {
            addCriterion("level =", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotEqualTo(Integer value) {
            addCriterion("level <>", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThan(Integer value) {
            addCriterion("level >", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("level >=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThan(Integer value) {
            addCriterion("level <", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThanOrEqualTo(Integer value) {
            addCriterion("level <=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelIn(List<Integer> values) {
            addCriterion("level in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotIn(List<Integer> values) {
            addCriterion("level not in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelBetween(Integer value1, Integer value2) {
            addCriterion("level between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("level not between", value1, value2, "level");
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