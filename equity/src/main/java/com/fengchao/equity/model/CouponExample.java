package com.fengchao.equity.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CouponExample() {
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

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantIdIsNull() {
            addCriterion("supplier_merchant_id is null");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantIdIsNotNull() {
            addCriterion("supplier_merchant_id is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantIdEqualTo(String value) {
            addCriterion("supplier_merchant_id =", value, "supplierMerchantId");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantIdNotEqualTo(String value) {
            addCriterion("supplier_merchant_id <>", value, "supplierMerchantId");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantIdGreaterThan(String value) {
            addCriterion("supplier_merchant_id >", value, "supplierMerchantId");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantIdGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_merchant_id >=", value, "supplierMerchantId");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantIdLessThan(String value) {
            addCriterion("supplier_merchant_id <", value, "supplierMerchantId");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantIdLessThanOrEqualTo(String value) {
            addCriterion("supplier_merchant_id <=", value, "supplierMerchantId");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantIdLike(String value) {
            addCriterion("supplier_merchant_id like", value, "supplierMerchantId");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantIdNotLike(String value) {
            addCriterion("supplier_merchant_id not like", value, "supplierMerchantId");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantIdIn(List<String> values) {
            addCriterion("supplier_merchant_id in", values, "supplierMerchantId");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantIdNotIn(List<String> values) {
            addCriterion("supplier_merchant_id not in", values, "supplierMerchantId");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantIdBetween(String value1, String value2) {
            addCriterion("supplier_merchant_id between", value1, value2, "supplierMerchantId");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantIdNotBetween(String value1, String value2) {
            addCriterion("supplier_merchant_id not between", value1, value2, "supplierMerchantId");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantNameIsNull() {
            addCriterion("supplier_merchant_name is null");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantNameIsNotNull() {
            addCriterion("supplier_merchant_name is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantNameEqualTo(String value) {
            addCriterion("supplier_merchant_name =", value, "supplierMerchantName");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantNameNotEqualTo(String value) {
            addCriterion("supplier_merchant_name <>", value, "supplierMerchantName");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantNameGreaterThan(String value) {
            addCriterion("supplier_merchant_name >", value, "supplierMerchantName");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantNameGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_merchant_name >=", value, "supplierMerchantName");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantNameLessThan(String value) {
            addCriterion("supplier_merchant_name <", value, "supplierMerchantName");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantNameLessThanOrEqualTo(String value) {
            addCriterion("supplier_merchant_name <=", value, "supplierMerchantName");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantNameLike(String value) {
            addCriterion("supplier_merchant_name like", value, "supplierMerchantName");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantNameNotLike(String value) {
            addCriterion("supplier_merchant_name not like", value, "supplierMerchantName");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantNameIn(List<String> values) {
            addCriterion("supplier_merchant_name in", values, "supplierMerchantName");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantNameNotIn(List<String> values) {
            addCriterion("supplier_merchant_name not in", values, "supplierMerchantName");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantNameBetween(String value1, String value2) {
            addCriterion("supplier_merchant_name between", value1, value2, "supplierMerchantName");
            return (Criteria) this;
        }

        public Criteria andSupplierMerchantNameNotBetween(String value1, String value2) {
            addCriterion("supplier_merchant_name not between", value1, value2, "supplierMerchantName");
            return (Criteria) this;
        }

        public Criteria andReleaseTotalIsNull() {
            addCriterion("release_total is null");
            return (Criteria) this;
        }

        public Criteria andReleaseTotalIsNotNull() {
            addCriterion("release_total is not null");
            return (Criteria) this;
        }

        public Criteria andReleaseTotalEqualTo(Integer value) {
            addCriterion("release_total =", value, "releaseTotal");
            return (Criteria) this;
        }

        public Criteria andReleaseTotalNotEqualTo(Integer value) {
            addCriterion("release_total <>", value, "releaseTotal");
            return (Criteria) this;
        }

        public Criteria andReleaseTotalGreaterThan(Integer value) {
            addCriterion("release_total >", value, "releaseTotal");
            return (Criteria) this;
        }

        public Criteria andReleaseTotalGreaterThanOrEqualTo(Integer value) {
            addCriterion("release_total >=", value, "releaseTotal");
            return (Criteria) this;
        }

        public Criteria andReleaseTotalLessThan(Integer value) {
            addCriterion("release_total <", value, "releaseTotal");
            return (Criteria) this;
        }

        public Criteria andReleaseTotalLessThanOrEqualTo(Integer value) {
            addCriterion("release_total <=", value, "releaseTotal");
            return (Criteria) this;
        }

        public Criteria andReleaseTotalIn(List<Integer> values) {
            addCriterion("release_total in", values, "releaseTotal");
            return (Criteria) this;
        }

        public Criteria andReleaseTotalNotIn(List<Integer> values) {
            addCriterion("release_total not in", values, "releaseTotal");
            return (Criteria) this;
        }

        public Criteria andReleaseTotalBetween(Integer value1, Integer value2) {
            addCriterion("release_total between", value1, value2, "releaseTotal");
            return (Criteria) this;
        }

        public Criteria andReleaseTotalNotBetween(Integer value1, Integer value2) {
            addCriterion("release_total not between", value1, value2, "releaseTotal");
            return (Criteria) this;
        }

        public Criteria andReleaseNumIsNull() {
            addCriterion("release_num is null");
            return (Criteria) this;
        }

        public Criteria andReleaseNumIsNotNull() {
            addCriterion("release_num is not null");
            return (Criteria) this;
        }

        public Criteria andReleaseNumEqualTo(Integer value) {
            addCriterion("release_num =", value, "releaseNum");
            return (Criteria) this;
        }

        public Criteria andReleaseNumNotEqualTo(Integer value) {
            addCriterion("release_num <>", value, "releaseNum");
            return (Criteria) this;
        }

        public Criteria andReleaseNumGreaterThan(Integer value) {
            addCriterion("release_num >", value, "releaseNum");
            return (Criteria) this;
        }

        public Criteria andReleaseNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("release_num >=", value, "releaseNum");
            return (Criteria) this;
        }

        public Criteria andReleaseNumLessThan(Integer value) {
            addCriterion("release_num <", value, "releaseNum");
            return (Criteria) this;
        }

        public Criteria andReleaseNumLessThanOrEqualTo(Integer value) {
            addCriterion("release_num <=", value, "releaseNum");
            return (Criteria) this;
        }

        public Criteria andReleaseNumIn(List<Integer> values) {
            addCriterion("release_num in", values, "releaseNum");
            return (Criteria) this;
        }

        public Criteria andReleaseNumNotIn(List<Integer> values) {
            addCriterion("release_num not in", values, "releaseNum");
            return (Criteria) this;
        }

        public Criteria andReleaseNumBetween(Integer value1, Integer value2) {
            addCriterion("release_num between", value1, value2, "releaseNum");
            return (Criteria) this;
        }

        public Criteria andReleaseNumNotBetween(Integer value1, Integer value2) {
            addCriterion("release_num not between", value1, value2, "releaseNum");
            return (Criteria) this;
        }

        public Criteria andReleaseStartDateIsNull() {
            addCriterion("release_start_date is null");
            return (Criteria) this;
        }

        public Criteria andReleaseStartDateIsNotNull() {
            addCriterion("release_start_date is not null");
            return (Criteria) this;
        }

        public Criteria andReleaseStartDateEqualTo(Date value) {
            addCriterion("release_start_date =", value, "releaseStartDate");
            return (Criteria) this;
        }

        public Criteria andReleaseStartDateNotEqualTo(Date value) {
            addCriterion("release_start_date <>", value, "releaseStartDate");
            return (Criteria) this;
        }

        public Criteria andReleaseStartDateGreaterThan(Date value) {
            addCriterion("release_start_date >", value, "releaseStartDate");
            return (Criteria) this;
        }

        public Criteria andReleaseStartDateGreaterThanOrEqualTo(Date value) {
            addCriterion("release_start_date >=", value, "releaseStartDate");
            return (Criteria) this;
        }

        public Criteria andReleaseStartDateLessThan(Date value) {
            addCriterion("release_start_date <", value, "releaseStartDate");
            return (Criteria) this;
        }

        public Criteria andReleaseStartDateLessThanOrEqualTo(Date value) {
            addCriterion("release_start_date <=", value, "releaseStartDate");
            return (Criteria) this;
        }

        public Criteria andReleaseStartDateIn(List<Date> values) {
            addCriterion("release_start_date in", values, "releaseStartDate");
            return (Criteria) this;
        }

        public Criteria andReleaseStartDateNotIn(List<Date> values) {
            addCriterion("release_start_date not in", values, "releaseStartDate");
            return (Criteria) this;
        }

        public Criteria andReleaseStartDateBetween(Date value1, Date value2) {
            addCriterion("release_start_date between", value1, value2, "releaseStartDate");
            return (Criteria) this;
        }

        public Criteria andReleaseStartDateNotBetween(Date value1, Date value2) {
            addCriterion("release_start_date not between", value1, value2, "releaseStartDate");
            return (Criteria) this;
        }

        public Criteria andReleaseEndDateIsNull() {
            addCriterion("release_end_date is null");
            return (Criteria) this;
        }

        public Criteria andReleaseEndDateIsNotNull() {
            addCriterion("release_end_date is not null");
            return (Criteria) this;
        }

        public Criteria andReleaseEndDateEqualTo(Date value) {
            addCriterion("release_end_date =", value, "releaseEndDate");
            return (Criteria) this;
        }

        public Criteria andReleaseEndDateNotEqualTo(Date value) {
            addCriterion("release_end_date <>", value, "releaseEndDate");
            return (Criteria) this;
        }

        public Criteria andReleaseEndDateGreaterThan(Date value) {
            addCriterion("release_end_date >", value, "releaseEndDate");
            return (Criteria) this;
        }

        public Criteria andReleaseEndDateGreaterThanOrEqualTo(Date value) {
            addCriterion("release_end_date >=", value, "releaseEndDate");
            return (Criteria) this;
        }

        public Criteria andReleaseEndDateLessThan(Date value) {
            addCriterion("release_end_date <", value, "releaseEndDate");
            return (Criteria) this;
        }

        public Criteria andReleaseEndDateLessThanOrEqualTo(Date value) {
            addCriterion("release_end_date <=", value, "releaseEndDate");
            return (Criteria) this;
        }

        public Criteria andReleaseEndDateIn(List<Date> values) {
            addCriterion("release_end_date in", values, "releaseEndDate");
            return (Criteria) this;
        }

        public Criteria andReleaseEndDateNotIn(List<Date> values) {
            addCriterion("release_end_date not in", values, "releaseEndDate");
            return (Criteria) this;
        }

        public Criteria andReleaseEndDateBetween(Date value1, Date value2) {
            addCriterion("release_end_date between", value1, value2, "releaseEndDate");
            return (Criteria) this;
        }

        public Criteria andReleaseEndDateNotBetween(Date value1, Date value2) {
            addCriterion("release_end_date not between", value1, value2, "releaseEndDate");
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

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andEffectiveStartDateIsNull() {
            addCriterion("effective_start_date is null");
            return (Criteria) this;
        }

        public Criteria andEffectiveStartDateIsNotNull() {
            addCriterion("effective_start_date is not null");
            return (Criteria) this;
        }

        public Criteria andEffectiveStartDateEqualTo(Date value) {
            addCriterion("effective_start_date =", value, "effectiveStartDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveStartDateNotEqualTo(Date value) {
            addCriterion("effective_start_date <>", value, "effectiveStartDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveStartDateGreaterThan(Date value) {
            addCriterion("effective_start_date >", value, "effectiveStartDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveStartDateGreaterThanOrEqualTo(Date value) {
            addCriterion("effective_start_date >=", value, "effectiveStartDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveStartDateLessThan(Date value) {
            addCriterion("effective_start_date <", value, "effectiveStartDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveStartDateLessThanOrEqualTo(Date value) {
            addCriterion("effective_start_date <=", value, "effectiveStartDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveStartDateIn(List<Date> values) {
            addCriterion("effective_start_date in", values, "effectiveStartDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveStartDateNotIn(List<Date> values) {
            addCriterion("effective_start_date not in", values, "effectiveStartDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveStartDateBetween(Date value1, Date value2) {
            addCriterion("effective_start_date between", value1, value2, "effectiveStartDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveStartDateNotBetween(Date value1, Date value2) {
            addCriterion("effective_start_date not between", value1, value2, "effectiveStartDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveEndDateIsNull() {
            addCriterion("effective_end_date is null");
            return (Criteria) this;
        }

        public Criteria andEffectiveEndDateIsNotNull() {
            addCriterion("effective_end_date is not null");
            return (Criteria) this;
        }

        public Criteria andEffectiveEndDateEqualTo(Date value) {
            addCriterion("effective_end_date =", value, "effectiveEndDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveEndDateNotEqualTo(Date value) {
            addCriterion("effective_end_date <>", value, "effectiveEndDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveEndDateGreaterThan(Date value) {
            addCriterion("effective_end_date >", value, "effectiveEndDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveEndDateGreaterThanOrEqualTo(Date value) {
            addCriterion("effective_end_date >=", value, "effectiveEndDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveEndDateLessThan(Date value) {
            addCriterion("effective_end_date <", value, "effectiveEndDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveEndDateLessThanOrEqualTo(Date value) {
            addCriterion("effective_end_date <=", value, "effectiveEndDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveEndDateIn(List<Date> values) {
            addCriterion("effective_end_date in", values, "effectiveEndDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveEndDateNotIn(List<Date> values) {
            addCriterion("effective_end_date not in", values, "effectiveEndDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveEndDateBetween(Date value1, Date value2) {
            addCriterion("effective_end_date between", value1, value2, "effectiveEndDate");
            return (Criteria) this;
        }

        public Criteria andEffectiveEndDateNotBetween(Date value1, Date value2) {
            addCriterion("effective_end_date not between", value1, value2, "effectiveEndDate");
            return (Criteria) this;
        }

        public Criteria andExcludeDatesIsNull() {
            addCriterion("exclude_dates is null");
            return (Criteria) this;
        }

        public Criteria andExcludeDatesIsNotNull() {
            addCriterion("exclude_dates is not null");
            return (Criteria) this;
        }

        public Criteria andExcludeDatesEqualTo(String value) {
            addCriterion("exclude_dates =", value, "excludeDates");
            return (Criteria) this;
        }

        public Criteria andExcludeDatesNotEqualTo(String value) {
            addCriterion("exclude_dates <>", value, "excludeDates");
            return (Criteria) this;
        }

        public Criteria andExcludeDatesGreaterThan(String value) {
            addCriterion("exclude_dates >", value, "excludeDates");
            return (Criteria) this;
        }

        public Criteria andExcludeDatesGreaterThanOrEqualTo(String value) {
            addCriterion("exclude_dates >=", value, "excludeDates");
            return (Criteria) this;
        }

        public Criteria andExcludeDatesLessThan(String value) {
            addCriterion("exclude_dates <", value, "excludeDates");
            return (Criteria) this;
        }

        public Criteria andExcludeDatesLessThanOrEqualTo(String value) {
            addCriterion("exclude_dates <=", value, "excludeDates");
            return (Criteria) this;
        }

        public Criteria andExcludeDatesLike(String value) {
            addCriterion("exclude_dates like", value, "excludeDates");
            return (Criteria) this;
        }

        public Criteria andExcludeDatesNotLike(String value) {
            addCriterion("exclude_dates not like", value, "excludeDates");
            return (Criteria) this;
        }

        public Criteria andExcludeDatesIn(List<String> values) {
            addCriterion("exclude_dates in", values, "excludeDates");
            return (Criteria) this;
        }

        public Criteria andExcludeDatesNotIn(List<String> values) {
            addCriterion("exclude_dates not in", values, "excludeDates");
            return (Criteria) this;
        }

        public Criteria andExcludeDatesBetween(String value1, String value2) {
            addCriterion("exclude_dates between", value1, value2, "excludeDates");
            return (Criteria) this;
        }

        public Criteria andExcludeDatesNotBetween(String value1, String value2) {
            addCriterion("exclude_dates not between", value1, value2, "excludeDates");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andUrlIsNull() {
            addCriterion("url is null");
            return (Criteria) this;
        }

        public Criteria andUrlIsNotNull() {
            addCriterion("url is not null");
            return (Criteria) this;
        }

        public Criteria andUrlEqualTo(String value) {
            addCriterion("url =", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotEqualTo(String value) {
            addCriterion("url <>", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThan(String value) {
            addCriterion("url >", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThanOrEqualTo(String value) {
            addCriterion("url >=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThan(String value) {
            addCriterion("url <", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThanOrEqualTo(String value) {
            addCriterion("url <=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLike(String value) {
            addCriterion("url like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotLike(String value) {
            addCriterion("url not like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlIn(List<String> values) {
            addCriterion("url in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotIn(List<String> values) {
            addCriterion("url not in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlBetween(String value1, String value2) {
            addCriterion("url between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotBetween(String value1, String value2) {
            addCriterion("url not between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNull() {
            addCriterion("create_date is null");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNotNull() {
            addCriterion("create_date is not null");
            return (Criteria) this;
        }

        public Criteria andCreateDateEqualTo(Date value) {
            addCriterion("create_date =", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotEqualTo(Date value) {
            addCriterion("create_date <>", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThan(Date value) {
            addCriterion("create_date >", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("create_date >=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThan(Date value) {
            addCriterion("create_date <", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThanOrEqualTo(Date value) {
            addCriterion("create_date <=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateIn(List<Date> values) {
            addCriterion("create_date in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotIn(List<Date> values) {
            addCriterion("create_date not in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateBetween(Date value1, Date value2) {
            addCriterion("create_date between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotBetween(Date value1, Date value2) {
            addCriterion("create_date not between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andCategoryIsNull() {
            addCriterion("category is null");
            return (Criteria) this;
        }

        public Criteria andCategoryIsNotNull() {
            addCriterion("category is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryEqualTo(String value) {
            addCriterion("category =", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotEqualTo(String value) {
            addCriterion("category <>", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryGreaterThan(String value) {
            addCriterion("category >", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryGreaterThanOrEqualTo(String value) {
            addCriterion("category >=", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryLessThan(String value) {
            addCriterion("category <", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryLessThanOrEqualTo(String value) {
            addCriterion("category <=", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryLike(String value) {
            addCriterion("category like", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotLike(String value) {
            addCriterion("category not like", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryIn(List<String> values) {
            addCriterion("category in", values, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotIn(List<String> values) {
            addCriterion("category not in", values, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryBetween(String value1, String value2) {
            addCriterion("category between", value1, value2, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotBetween(String value1, String value2) {
            addCriterion("category not between", value1, value2, "category");
            return (Criteria) this;
        }

        public Criteria andTagsIsNull() {
            addCriterion("tags is null");
            return (Criteria) this;
        }

        public Criteria andTagsIsNotNull() {
            addCriterion("tags is not null");
            return (Criteria) this;
        }

        public Criteria andTagsEqualTo(String value) {
            addCriterion("tags =", value, "tags");
            return (Criteria) this;
        }

        public Criteria andTagsNotEqualTo(String value) {
            addCriterion("tags <>", value, "tags");
            return (Criteria) this;
        }

        public Criteria andTagsGreaterThan(String value) {
            addCriterion("tags >", value, "tags");
            return (Criteria) this;
        }

        public Criteria andTagsGreaterThanOrEqualTo(String value) {
            addCriterion("tags >=", value, "tags");
            return (Criteria) this;
        }

        public Criteria andTagsLessThan(String value) {
            addCriterion("tags <", value, "tags");
            return (Criteria) this;
        }

        public Criteria andTagsLessThanOrEqualTo(String value) {
            addCriterion("tags <=", value, "tags");
            return (Criteria) this;
        }

        public Criteria andTagsLike(String value) {
            addCriterion("tags like", value, "tags");
            return (Criteria) this;
        }

        public Criteria andTagsNotLike(String value) {
            addCriterion("tags not like", value, "tags");
            return (Criteria) this;
        }

        public Criteria andTagsIn(List<String> values) {
            addCriterion("tags in", values, "tags");
            return (Criteria) this;
        }

        public Criteria andTagsNotIn(List<String> values) {
            addCriterion("tags not in", values, "tags");
            return (Criteria) this;
        }

        public Criteria andTagsBetween(String value1, String value2) {
            addCriterion("tags between", value1, value2, "tags");
            return (Criteria) this;
        }

        public Criteria andTagsNotBetween(String value1, String value2) {
            addCriterion("tags not between", value1, value2, "tags");
            return (Criteria) this;
        }

        public Criteria andImageUrlIsNull() {
            addCriterion("image_url is null");
            return (Criteria) this;
        }

        public Criteria andImageUrlIsNotNull() {
            addCriterion("image_url is not null");
            return (Criteria) this;
        }

        public Criteria andImageUrlEqualTo(String value) {
            addCriterion("image_url =", value, "imageUrl");
            return (Criteria) this;
        }

        public Criteria andImageUrlNotEqualTo(String value) {
            addCriterion("image_url <>", value, "imageUrl");
            return (Criteria) this;
        }

        public Criteria andImageUrlGreaterThan(String value) {
            addCriterion("image_url >", value, "imageUrl");
            return (Criteria) this;
        }

        public Criteria andImageUrlGreaterThanOrEqualTo(String value) {
            addCriterion("image_url >=", value, "imageUrl");
            return (Criteria) this;
        }

        public Criteria andImageUrlLessThan(String value) {
            addCriterion("image_url <", value, "imageUrl");
            return (Criteria) this;
        }

        public Criteria andImageUrlLessThanOrEqualTo(String value) {
            addCriterion("image_url <=", value, "imageUrl");
            return (Criteria) this;
        }

        public Criteria andImageUrlLike(String value) {
            addCriterion("image_url like", value, "imageUrl");
            return (Criteria) this;
        }

        public Criteria andImageUrlNotLike(String value) {
            addCriterion("image_url not like", value, "imageUrl");
            return (Criteria) this;
        }

        public Criteria andImageUrlIn(List<String> values) {
            addCriterion("image_url in", values, "imageUrl");
            return (Criteria) this;
        }

        public Criteria andImageUrlNotIn(List<String> values) {
            addCriterion("image_url not in", values, "imageUrl");
            return (Criteria) this;
        }

        public Criteria andImageUrlBetween(String value1, String value2) {
            addCriterion("image_url between", value1, value2, "imageUrl");
            return (Criteria) this;
        }

        public Criteria andImageUrlNotBetween(String value1, String value2) {
            addCriterion("image_url not between", value1, value2, "imageUrl");
            return (Criteria) this;
        }

        public Criteria andCodeIsNull() {
            addCriterion("code is null");
            return (Criteria) this;
        }

        public Criteria andCodeIsNotNull() {
            addCriterion("code is not null");
            return (Criteria) this;
        }

        public Criteria andCodeEqualTo(String value) {
            addCriterion("code =", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotEqualTo(String value) {
            addCriterion("code <>", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThan(String value) {
            addCriterion("code >", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThanOrEqualTo(String value) {
            addCriterion("code >=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThan(String value) {
            addCriterion("code <", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThanOrEqualTo(String value) {
            addCriterion("code <=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLike(String value) {
            addCriterion("code like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotLike(String value) {
            addCriterion("code not like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeIn(List<String> values) {
            addCriterion("code in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotIn(List<String> values) {
            addCriterion("code not in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeBetween(String value1, String value2) {
            addCriterion("code between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotBetween(String value1, String value2) {
            addCriterion("code not between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andRulesDescriptionIsNull() {
            addCriterion("rules_description is null");
            return (Criteria) this;
        }

        public Criteria andRulesDescriptionIsNotNull() {
            addCriterion("rules_description is not null");
            return (Criteria) this;
        }

        public Criteria andRulesDescriptionEqualTo(String value) {
            addCriterion("rules_description =", value, "rulesDescription");
            return (Criteria) this;
        }

        public Criteria andRulesDescriptionNotEqualTo(String value) {
            addCriterion("rules_description <>", value, "rulesDescription");
            return (Criteria) this;
        }

        public Criteria andRulesDescriptionGreaterThan(String value) {
            addCriterion("rules_description >", value, "rulesDescription");
            return (Criteria) this;
        }

        public Criteria andRulesDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("rules_description >=", value, "rulesDescription");
            return (Criteria) this;
        }

        public Criteria andRulesDescriptionLessThan(String value) {
            addCriterion("rules_description <", value, "rulesDescription");
            return (Criteria) this;
        }

        public Criteria andRulesDescriptionLessThanOrEqualTo(String value) {
            addCriterion("rules_description <=", value, "rulesDescription");
            return (Criteria) this;
        }

        public Criteria andRulesDescriptionLike(String value) {
            addCriterion("rules_description like", value, "rulesDescription");
            return (Criteria) this;
        }

        public Criteria andRulesDescriptionNotLike(String value) {
            addCriterion("rules_description not like", value, "rulesDescription");
            return (Criteria) this;
        }

        public Criteria andRulesDescriptionIn(List<String> values) {
            addCriterion("rules_description in", values, "rulesDescription");
            return (Criteria) this;
        }

        public Criteria andRulesDescriptionNotIn(List<String> values) {
            addCriterion("rules_description not in", values, "rulesDescription");
            return (Criteria) this;
        }

        public Criteria andRulesDescriptionBetween(String value1, String value2) {
            addCriterion("rules_description between", value1, value2, "rulesDescription");
            return (Criteria) this;
        }

        public Criteria andRulesDescriptionNotBetween(String value1, String value2) {
            addCriterion("rules_description not between", value1, value2, "rulesDescription");
            return (Criteria) this;
        }

        public Criteria andPerLimitedIsNull() {
            addCriterion("per_limited is null");
            return (Criteria) this;
        }

        public Criteria andPerLimitedIsNotNull() {
            addCriterion("per_limited is not null");
            return (Criteria) this;
        }

        public Criteria andPerLimitedEqualTo(Integer value) {
            addCriterion("per_limited =", value, "perLimited");
            return (Criteria) this;
        }

        public Criteria andPerLimitedNotEqualTo(Integer value) {
            addCriterion("per_limited <>", value, "perLimited");
            return (Criteria) this;
        }

        public Criteria andPerLimitedGreaterThan(Integer value) {
            addCriterion("per_limited >", value, "perLimited");
            return (Criteria) this;
        }

        public Criteria andPerLimitedGreaterThanOrEqualTo(Integer value) {
            addCriterion("per_limited >=", value, "perLimited");
            return (Criteria) this;
        }

        public Criteria andPerLimitedLessThan(Integer value) {
            addCriterion("per_limited <", value, "perLimited");
            return (Criteria) this;
        }

        public Criteria andPerLimitedLessThanOrEqualTo(Integer value) {
            addCriterion("per_limited <=", value, "perLimited");
            return (Criteria) this;
        }

        public Criteria andPerLimitedIn(List<Integer> values) {
            addCriterion("per_limited in", values, "perLimited");
            return (Criteria) this;
        }

        public Criteria andPerLimitedNotIn(List<Integer> values) {
            addCriterion("per_limited not in", values, "perLimited");
            return (Criteria) this;
        }

        public Criteria andPerLimitedBetween(Integer value1, Integer value2) {
            addCriterion("per_limited between", value1, value2, "perLimited");
            return (Criteria) this;
        }

        public Criteria andPerLimitedNotBetween(Integer value1, Integer value2) {
            addCriterion("per_limited not between", value1, value2, "perLimited");
            return (Criteria) this;
        }

        public Criteria andScopesIsNull() {
            addCriterion("scopes is null");
            return (Criteria) this;
        }

        public Criteria andScopesIsNotNull() {
            addCriterion("scopes is not null");
            return (Criteria) this;
        }

        public Criteria andScopesEqualTo(String value) {
            addCriterion("scopes =", value, "scopes");
            return (Criteria) this;
        }

        public Criteria andScopesNotEqualTo(String value) {
            addCriterion("scopes <>", value, "scopes");
            return (Criteria) this;
        }

        public Criteria andScopesGreaterThan(String value) {
            addCriterion("scopes >", value, "scopes");
            return (Criteria) this;
        }

        public Criteria andScopesGreaterThanOrEqualTo(String value) {
            addCriterion("scopes >=", value, "scopes");
            return (Criteria) this;
        }

        public Criteria andScopesLessThan(String value) {
            addCriterion("scopes <", value, "scopes");
            return (Criteria) this;
        }

        public Criteria andScopesLessThanOrEqualTo(String value) {
            addCriterion("scopes <=", value, "scopes");
            return (Criteria) this;
        }

        public Criteria andScopesLike(String value) {
            addCriterion("scopes like", value, "scopes");
            return (Criteria) this;
        }

        public Criteria andScopesNotLike(String value) {
            addCriterion("scopes not like", value, "scopes");
            return (Criteria) this;
        }

        public Criteria andScopesIn(List<String> values) {
            addCriterion("scopes in", values, "scopes");
            return (Criteria) this;
        }

        public Criteria andScopesNotIn(List<String> values) {
            addCriterion("scopes not in", values, "scopes");
            return (Criteria) this;
        }

        public Criteria andScopesBetween(String value1, String value2) {
            addCriterion("scopes between", value1, value2, "scopes");
            return (Criteria) this;
        }

        public Criteria andScopesNotBetween(String value1, String value2) {
            addCriterion("scopes not between", value1, value2, "scopes");
            return (Criteria) this;
        }

        public Criteria andScenarioTypeIsNull() {
            addCriterion("scenario_type is null");
            return (Criteria) this;
        }

        public Criteria andScenarioTypeIsNotNull() {
            addCriterion("scenario_type is not null");
            return (Criteria) this;
        }

        public Criteria andScenarioTypeEqualTo(Integer value) {
            addCriterion("scenario_type =", value, "scenarioType");
            return (Criteria) this;
        }

        public Criteria andScenarioTypeNotEqualTo(Integer value) {
            addCriterion("scenario_type <>", value, "scenarioType");
            return (Criteria) this;
        }

        public Criteria andScenarioTypeGreaterThan(Integer value) {
            addCriterion("scenario_type >", value, "scenarioType");
            return (Criteria) this;
        }

        public Criteria andScenarioTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("scenario_type >=", value, "scenarioType");
            return (Criteria) this;
        }

        public Criteria andScenarioTypeLessThan(Integer value) {
            addCriterion("scenario_type <", value, "scenarioType");
            return (Criteria) this;
        }

        public Criteria andScenarioTypeLessThanOrEqualTo(Integer value) {
            addCriterion("scenario_type <=", value, "scenarioType");
            return (Criteria) this;
        }

        public Criteria andScenarioTypeIn(List<Integer> values) {
            addCriterion("scenario_type in", values, "scenarioType");
            return (Criteria) this;
        }

        public Criteria andScenarioTypeNotIn(List<Integer> values) {
            addCriterion("scenario_type not in", values, "scenarioType");
            return (Criteria) this;
        }

        public Criteria andScenarioTypeBetween(Integer value1, Integer value2) {
            addCriterion("scenario_type between", value1, value2, "scenarioType");
            return (Criteria) this;
        }

        public Criteria andScenarioTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("scenario_type not between", value1, value2, "scenarioType");
            return (Criteria) this;
        }

        public Criteria andCategoriesIsNull() {
            addCriterion("categories is null");
            return (Criteria) this;
        }

        public Criteria andCategoriesIsNotNull() {
            addCriterion("categories is not null");
            return (Criteria) this;
        }

        public Criteria andCategoriesEqualTo(String value) {
            addCriterion("categories =", value, "categories");
            return (Criteria) this;
        }

        public Criteria andCategoriesNotEqualTo(String value) {
            addCriterion("categories <>", value, "categories");
            return (Criteria) this;
        }

        public Criteria andCategoriesGreaterThan(String value) {
            addCriterion("categories >", value, "categories");
            return (Criteria) this;
        }

        public Criteria andCategoriesGreaterThanOrEqualTo(String value) {
            addCriterion("categories >=", value, "categories");
            return (Criteria) this;
        }

        public Criteria andCategoriesLessThan(String value) {
            addCriterion("categories <", value, "categories");
            return (Criteria) this;
        }

        public Criteria andCategoriesLessThanOrEqualTo(String value) {
            addCriterion("categories <=", value, "categories");
            return (Criteria) this;
        }

        public Criteria andCategoriesLike(String value) {
            addCriterion("categories like", value, "categories");
            return (Criteria) this;
        }

        public Criteria andCategoriesNotLike(String value) {
            addCriterion("categories not like", value, "categories");
            return (Criteria) this;
        }

        public Criteria andCategoriesIn(List<String> values) {
            addCriterion("categories in", values, "categories");
            return (Criteria) this;
        }

        public Criteria andCategoriesNotIn(List<String> values) {
            addCriterion("categories not in", values, "categories");
            return (Criteria) this;
        }

        public Criteria andCategoriesBetween(String value1, String value2) {
            addCriterion("categories between", value1, value2, "categories");
            return (Criteria) this;
        }

        public Criteria andCategoriesNotBetween(String value1, String value2) {
            addCriterion("categories not between", value1, value2, "categories");
            return (Criteria) this;
        }

        public Criteria andBrandsIsNull() {
            addCriterion("brands is null");
            return (Criteria) this;
        }

        public Criteria andBrandsIsNotNull() {
            addCriterion("brands is not null");
            return (Criteria) this;
        }

        public Criteria andBrandsEqualTo(String value) {
            addCriterion("brands =", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsNotEqualTo(String value) {
            addCriterion("brands <>", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsGreaterThan(String value) {
            addCriterion("brands >", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsGreaterThanOrEqualTo(String value) {
            addCriterion("brands >=", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsLessThan(String value) {
            addCriterion("brands <", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsLessThanOrEqualTo(String value) {
            addCriterion("brands <=", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsLike(String value) {
            addCriterion("brands like", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsNotLike(String value) {
            addCriterion("brands not like", value, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsIn(List<String> values) {
            addCriterion("brands in", values, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsNotIn(List<String> values) {
            addCriterion("brands not in", values, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsBetween(String value1, String value2) {
            addCriterion("brands between", value1, value2, "brands");
            return (Criteria) this;
        }

        public Criteria andBrandsNotBetween(String value1, String value2) {
            addCriterion("brands not between", value1, value2, "brands");
            return (Criteria) this;
        }

        public Criteria andCollectTypeIsNull() {
            addCriterion("collect_type is null");
            return (Criteria) this;
        }

        public Criteria andCollectTypeIsNotNull() {
            addCriterion("collect_type is not null");
            return (Criteria) this;
        }

        public Criteria andCollectTypeEqualTo(Integer value) {
            addCriterion("collect_type =", value, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeNotEqualTo(Integer value) {
            addCriterion("collect_type <>", value, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeGreaterThan(Integer value) {
            addCriterion("collect_type >", value, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("collect_type >=", value, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeLessThan(Integer value) {
            addCriterion("collect_type <", value, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeLessThanOrEqualTo(Integer value) {
            addCriterion("collect_type <=", value, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeIn(List<Integer> values) {
            addCriterion("collect_type in", values, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeNotIn(List<Integer> values) {
            addCriterion("collect_type not in", values, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeBetween(Integer value1, Integer value2) {
            addCriterion("collect_type between", value1, value2, "collectType");
            return (Criteria) this;
        }

        public Criteria andCollectTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("collect_type not between", value1, value2, "collectType");
            return (Criteria) this;
        }

        public Criteria andPointsIsNull() {
            addCriterion("points is null");
            return (Criteria) this;
        }

        public Criteria andPointsIsNotNull() {
            addCriterion("points is not null");
            return (Criteria) this;
        }

        public Criteria andPointsEqualTo(Integer value) {
            addCriterion("points =", value, "points");
            return (Criteria) this;
        }

        public Criteria andPointsNotEqualTo(Integer value) {
            addCriterion("points <>", value, "points");
            return (Criteria) this;
        }

        public Criteria andPointsGreaterThan(Integer value) {
            addCriterion("points >", value, "points");
            return (Criteria) this;
        }

        public Criteria andPointsGreaterThanOrEqualTo(Integer value) {
            addCriterion("points >=", value, "points");
            return (Criteria) this;
        }

        public Criteria andPointsLessThan(Integer value) {
            addCriterion("points <", value, "points");
            return (Criteria) this;
        }

        public Criteria andPointsLessThanOrEqualTo(Integer value) {
            addCriterion("points <=", value, "points");
            return (Criteria) this;
        }

        public Criteria andPointsIn(List<Integer> values) {
            addCriterion("points in", values, "points");
            return (Criteria) this;
        }

        public Criteria andPointsNotIn(List<Integer> values) {
            addCriterion("points not in", values, "points");
            return (Criteria) this;
        }

        public Criteria andPointsBetween(Integer value1, Integer value2) {
            addCriterion("points between", value1, value2, "points");
            return (Criteria) this;
        }

        public Criteria andPointsNotBetween(Integer value1, Integer value2) {
            addCriterion("points not between", value1, value2, "points");
            return (Criteria) this;
        }

        public Criteria andCustomerTypeIsNull() {
            addCriterion("customer_type is null");
            return (Criteria) this;
        }

        public Criteria andCustomerTypeIsNotNull() {
            addCriterion("customer_type is not null");
            return (Criteria) this;
        }

        public Criteria andCustomerTypeEqualTo(Integer value) {
            addCriterion("customer_type =", value, "customerType");
            return (Criteria) this;
        }

        public Criteria andCustomerTypeNotEqualTo(Integer value) {
            addCriterion("customer_type <>", value, "customerType");
            return (Criteria) this;
        }

        public Criteria andCustomerTypeGreaterThan(Integer value) {
            addCriterion("customer_type >", value, "customerType");
            return (Criteria) this;
        }

        public Criteria andCustomerTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("customer_type >=", value, "customerType");
            return (Criteria) this;
        }

        public Criteria andCustomerTypeLessThan(Integer value) {
            addCriterion("customer_type <", value, "customerType");
            return (Criteria) this;
        }

        public Criteria andCustomerTypeLessThanOrEqualTo(Integer value) {
            addCriterion("customer_type <=", value, "customerType");
            return (Criteria) this;
        }

        public Criteria andCustomerTypeIn(List<Integer> values) {
            addCriterion("customer_type in", values, "customerType");
            return (Criteria) this;
        }

        public Criteria andCustomerTypeNotIn(List<Integer> values) {
            addCriterion("customer_type not in", values, "customerType");
            return (Criteria) this;
        }

        public Criteria andCustomerTypeBetween(Integer value1, Integer value2) {
            addCriterion("customer_type between", value1, value2, "customerType");
            return (Criteria) this;
        }

        public Criteria andCustomerTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("customer_type not between", value1, value2, "customerType");
            return (Criteria) this;
        }

        public Criteria andUsersIsNull() {
            addCriterion("users is null");
            return (Criteria) this;
        }

        public Criteria andUsersIsNotNull() {
            addCriterion("users is not null");
            return (Criteria) this;
        }

        public Criteria andUsersEqualTo(String value) {
            addCriterion("users =", value, "users");
            return (Criteria) this;
        }

        public Criteria andUsersNotEqualTo(String value) {
            addCriterion("users <>", value, "users");
            return (Criteria) this;
        }

        public Criteria andUsersGreaterThan(String value) {
            addCriterion("users >", value, "users");
            return (Criteria) this;
        }

        public Criteria andUsersGreaterThanOrEqualTo(String value) {
            addCriterion("users >=", value, "users");
            return (Criteria) this;
        }

        public Criteria andUsersLessThan(String value) {
            addCriterion("users <", value, "users");
            return (Criteria) this;
        }

        public Criteria andUsersLessThanOrEqualTo(String value) {
            addCriterion("users <=", value, "users");
            return (Criteria) this;
        }

        public Criteria andUsersLike(String value) {
            addCriterion("users like", value, "users");
            return (Criteria) this;
        }

        public Criteria andUsersNotLike(String value) {
            addCriterion("users not like", value, "users");
            return (Criteria) this;
        }

        public Criteria andUsersIn(List<String> values) {
            addCriterion("users in", values, "users");
            return (Criteria) this;
        }

        public Criteria andUsersNotIn(List<String> values) {
            addCriterion("users not in", values, "users");
            return (Criteria) this;
        }

        public Criteria andUsersBetween(String value1, String value2) {
            addCriterion("users between", value1, value2, "users");
            return (Criteria) this;
        }

        public Criteria andUsersNotBetween(String value1, String value2) {
            addCriterion("users not between", value1, value2, "users");
            return (Criteria) this;
        }

        public Criteria andCouponTypeIsNull() {
            addCriterion("coupon_type is null");
            return (Criteria) this;
        }

        public Criteria andCouponTypeIsNotNull() {
            addCriterion("coupon_type is not null");
            return (Criteria) this;
        }

        public Criteria andCouponTypeEqualTo(Integer value) {
            addCriterion("coupon_type =", value, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeNotEqualTo(Integer value) {
            addCriterion("coupon_type <>", value, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeGreaterThan(Integer value) {
            addCriterion("coupon_type >", value, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("coupon_type >=", value, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeLessThan(Integer value) {
            addCriterion("coupon_type <", value, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeLessThanOrEqualTo(Integer value) {
            addCriterion("coupon_type <=", value, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeIn(List<Integer> values) {
            addCriterion("coupon_type in", values, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeNotIn(List<Integer> values) {
            addCriterion("coupon_type not in", values, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeBetween(Integer value1, Integer value2) {
            addCriterion("coupon_type between", value1, value2, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("coupon_type not between", value1, value2, "couponType");
            return (Criteria) this;
        }

        public Criteria andEffectiveDaysIsNull() {
            addCriterion("effective_days is null");
            return (Criteria) this;
        }

        public Criteria andEffectiveDaysIsNotNull() {
            addCriterion("effective_days is not null");
            return (Criteria) this;
        }

        public Criteria andEffectiveDaysEqualTo(Integer value) {
            addCriterion("effective_days =", value, "effectiveDays");
            return (Criteria) this;
        }

        public Criteria andEffectiveDaysNotEqualTo(Integer value) {
            addCriterion("effective_days <>", value, "effectiveDays");
            return (Criteria) this;
        }

        public Criteria andEffectiveDaysGreaterThan(Integer value) {
            addCriterion("effective_days >", value, "effectiveDays");
            return (Criteria) this;
        }

        public Criteria andEffectiveDaysGreaterThanOrEqualTo(Integer value) {
            addCriterion("effective_days >=", value, "effectiveDays");
            return (Criteria) this;
        }

        public Criteria andEffectiveDaysLessThan(Integer value) {
            addCriterion("effective_days <", value, "effectiveDays");
            return (Criteria) this;
        }

        public Criteria andEffectiveDaysLessThanOrEqualTo(Integer value) {
            addCriterion("effective_days <=", value, "effectiveDays");
            return (Criteria) this;
        }

        public Criteria andEffectiveDaysIn(List<Integer> values) {
            addCriterion("effective_days in", values, "effectiveDays");
            return (Criteria) this;
        }

        public Criteria andEffectiveDaysNotIn(List<Integer> values) {
            addCriterion("effective_days not in", values, "effectiveDays");
            return (Criteria) this;
        }

        public Criteria andEffectiveDaysBetween(Integer value1, Integer value2) {
            addCriterion("effective_days between", value1, value2, "effectiveDays");
            return (Criteria) this;
        }

        public Criteria andEffectiveDaysNotBetween(Integer value1, Integer value2) {
            addCriterion("effective_days not between", value1, value2, "effectiveDays");
            return (Criteria) this;
        }

        public Criteria andCouponRulesIsNull() {
            addCriterion("coupon_rules is null");
            return (Criteria) this;
        }

        public Criteria andCouponRulesIsNotNull() {
            addCriterion("coupon_rules is not null");
            return (Criteria) this;
        }

        public Criteria andCouponRulesEqualTo(String value) {
            addCriterion("coupon_rules =", value, "couponRules");
            return (Criteria) this;
        }

        public Criteria andCouponRulesNotEqualTo(String value) {
            addCriterion("coupon_rules <>", value, "couponRules");
            return (Criteria) this;
        }

        public Criteria andCouponRulesGreaterThan(String value) {
            addCriterion("coupon_rules >", value, "couponRules");
            return (Criteria) this;
        }

        public Criteria andCouponRulesGreaterThanOrEqualTo(String value) {
            addCriterion("coupon_rules >=", value, "couponRules");
            return (Criteria) this;
        }

        public Criteria andCouponRulesLessThan(String value) {
            addCriterion("coupon_rules <", value, "couponRules");
            return (Criteria) this;
        }

        public Criteria andCouponRulesLessThanOrEqualTo(String value) {
            addCriterion("coupon_rules <=", value, "couponRules");
            return (Criteria) this;
        }

        public Criteria andCouponRulesLike(String value) {
            addCriterion("coupon_rules like", value, "couponRules");
            return (Criteria) this;
        }

        public Criteria andCouponRulesNotLike(String value) {
            addCriterion("coupon_rules not like", value, "couponRules");
            return (Criteria) this;
        }

        public Criteria andCouponRulesIn(List<String> values) {
            addCriterion("coupon_rules in", values, "couponRules");
            return (Criteria) this;
        }

        public Criteria andCouponRulesNotIn(List<String> values) {
            addCriterion("coupon_rules not in", values, "couponRules");
            return (Criteria) this;
        }

        public Criteria andCouponRulesBetween(String value1, String value2) {
            addCriterion("coupon_rules between", value1, value2, "couponRules");
            return (Criteria) this;
        }

        public Criteria andCouponRulesNotBetween(String value1, String value2) {
            addCriterion("coupon_rules not between", value1, value2, "couponRules");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNull() {
            addCriterion("app_id is null");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNotNull() {
            addCriterion("app_id is not null");
            return (Criteria) this;
        }

        public Criteria andAppIdEqualTo(String value) {
            addCriterion("app_id =", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotEqualTo(String value) {
            addCriterion("app_id <>", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThan(String value) {
            addCriterion("app_id >", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThanOrEqualTo(String value) {
            addCriterion("app_id >=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThan(String value) {
            addCriterion("app_id <", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThanOrEqualTo(String value) {
            addCriterion("app_id <=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLike(String value) {
            addCriterion("app_id like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotLike(String value) {
            addCriterion("app_id not like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdIn(List<String> values) {
            addCriterion("app_id in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotIn(List<String> values) {
            addCriterion("app_id not in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdBetween(String value1, String value2) {
            addCriterion("app_id between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotBetween(String value1, String value2) {
            addCriterion("app_id not between", value1, value2, "appId");
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