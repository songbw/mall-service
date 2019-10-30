package com.fengchao.order.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImsSupermanMallOrderExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ImsSupermanMallOrderExample() {
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

        public Criteria andPidIsNull() {
            addCriterion("pid is null");
            return (Criteria) this;
        }

        public Criteria andPidIsNotNull() {
            addCriterion("pid is not null");
            return (Criteria) this;
        }

        public Criteria andPidEqualTo(Integer value) {
            addCriterion("pid =", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotEqualTo(Integer value) {
            addCriterion("pid <>", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThan(Integer value) {
            addCriterion("pid >", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThanOrEqualTo(Integer value) {
            addCriterion("pid >=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThan(Integer value) {
            addCriterion("pid <", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThanOrEqualTo(Integer value) {
            addCriterion("pid <=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidIn(List<Integer> values) {
            addCriterion("pid in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotIn(List<Integer> values) {
            addCriterion("pid not in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidBetween(Integer value1, Integer value2) {
            addCriterion("pid between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotBetween(Integer value1, Integer value2) {
            addCriterion("pid not between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andUniacidIsNull() {
            addCriterion("uniacid is null");
            return (Criteria) this;
        }

        public Criteria andUniacidIsNotNull() {
            addCriterion("uniacid is not null");
            return (Criteria) this;
        }

        public Criteria andUniacidEqualTo(Integer value) {
            addCriterion("uniacid =", value, "uniacid");
            return (Criteria) this;
        }

        public Criteria andUniacidNotEqualTo(Integer value) {
            addCriterion("uniacid <>", value, "uniacid");
            return (Criteria) this;
        }

        public Criteria andUniacidGreaterThan(Integer value) {
            addCriterion("uniacid >", value, "uniacid");
            return (Criteria) this;
        }

        public Criteria andUniacidGreaterThanOrEqualTo(Integer value) {
            addCriterion("uniacid >=", value, "uniacid");
            return (Criteria) this;
        }

        public Criteria andUniacidLessThan(Integer value) {
            addCriterion("uniacid <", value, "uniacid");
            return (Criteria) this;
        }

        public Criteria andUniacidLessThanOrEqualTo(Integer value) {
            addCriterion("uniacid <=", value, "uniacid");
            return (Criteria) this;
        }

        public Criteria andUniacidIn(List<Integer> values) {
            addCriterion("uniacid in", values, "uniacid");
            return (Criteria) this;
        }

        public Criteria andUniacidNotIn(List<Integer> values) {
            addCriterion("uniacid not in", values, "uniacid");
            return (Criteria) this;
        }

        public Criteria andUniacidBetween(Integer value1, Integer value2) {
            addCriterion("uniacid between", value1, value2, "uniacid");
            return (Criteria) this;
        }

        public Criteria andUniacidNotBetween(Integer value1, Integer value2) {
            addCriterion("uniacid not between", value1, value2, "uniacid");
            return (Criteria) this;
        }

        public Criteria andShopidIsNull() {
            addCriterion("shopid is null");
            return (Criteria) this;
        }

        public Criteria andShopidIsNotNull() {
            addCriterion("shopid is not null");
            return (Criteria) this;
        }

        public Criteria andShopidEqualTo(Integer value) {
            addCriterion("shopid =", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidNotEqualTo(Integer value) {
            addCriterion("shopid <>", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidGreaterThan(Integer value) {
            addCriterion("shopid >", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidGreaterThanOrEqualTo(Integer value) {
            addCriterion("shopid >=", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidLessThan(Integer value) {
            addCriterion("shopid <", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidLessThanOrEqualTo(Integer value) {
            addCriterion("shopid <=", value, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidIn(List<Integer> values) {
            addCriterion("shopid in", values, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidNotIn(List<Integer> values) {
            addCriterion("shopid not in", values, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidBetween(Integer value1, Integer value2) {
            addCriterion("shopid between", value1, value2, "shopid");
            return (Criteria) this;
        }

        public Criteria andShopidNotBetween(Integer value1, Integer value2) {
            addCriterion("shopid not between", value1, value2, "shopid");
            return (Criteria) this;
        }

        public Criteria andUidIsNull() {
            addCriterion("uid is null");
            return (Criteria) this;
        }

        public Criteria andUidIsNotNull() {
            addCriterion("uid is not null");
            return (Criteria) this;
        }

        public Criteria andUidEqualTo(Integer value) {
            addCriterion("uid =", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotEqualTo(Integer value) {
            addCriterion("uid <>", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThan(Integer value) {
            addCriterion("uid >", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThanOrEqualTo(Integer value) {
            addCriterion("uid >=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThan(Integer value) {
            addCriterion("uid <", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThanOrEqualTo(Integer value) {
            addCriterion("uid <=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidIn(List<Integer> values) {
            addCriterion("uid in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotIn(List<Integer> values) {
            addCriterion("uid not in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidBetween(Integer value1, Integer value2) {
            addCriterion("uid between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotBetween(Integer value1, Integer value2) {
            addCriterion("uid not between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andOrdersnIsNull() {
            addCriterion("ordersn is null");
            return (Criteria) this;
        }

        public Criteria andOrdersnIsNotNull() {
            addCriterion("ordersn is not null");
            return (Criteria) this;
        }

        public Criteria andOrdersnEqualTo(String value) {
            addCriterion("ordersn =", value, "ordersn");
            return (Criteria) this;
        }

        public Criteria andOrdersnNotEqualTo(String value) {
            addCriterion("ordersn <>", value, "ordersn");
            return (Criteria) this;
        }

        public Criteria andOrdersnGreaterThan(String value) {
            addCriterion("ordersn >", value, "ordersn");
            return (Criteria) this;
        }

        public Criteria andOrdersnGreaterThanOrEqualTo(String value) {
            addCriterion("ordersn >=", value, "ordersn");
            return (Criteria) this;
        }

        public Criteria andOrdersnLessThan(String value) {
            addCriterion("ordersn <", value, "ordersn");
            return (Criteria) this;
        }

        public Criteria andOrdersnLessThanOrEqualTo(String value) {
            addCriterion("ordersn <=", value, "ordersn");
            return (Criteria) this;
        }

        public Criteria andOrdersnLike(String value) {
            addCriterion("ordersn like", value, "ordersn");
            return (Criteria) this;
        }

        public Criteria andOrdersnNotLike(String value) {
            addCriterion("ordersn not like", value, "ordersn");
            return (Criteria) this;
        }

        public Criteria andOrdersnIn(List<String> values) {
            addCriterion("ordersn in", values, "ordersn");
            return (Criteria) this;
        }

        public Criteria andOrdersnNotIn(List<String> values) {
            addCriterion("ordersn not in", values, "ordersn");
            return (Criteria) this;
        }

        public Criteria andOrdersnBetween(String value1, String value2) {
            addCriterion("ordersn between", value1, value2, "ordersn");
            return (Criteria) this;
        }

        public Criteria andOrdersnNotBetween(String value1, String value2) {
            addCriterion("ordersn not between", value1, value2, "ordersn");
            return (Criteria) this;
        }

        public Criteria andPartner3IdIsNull() {
            addCriterion("partner3_id is null");
            return (Criteria) this;
        }

        public Criteria andPartner3IdIsNotNull() {
            addCriterion("partner3_id is not null");
            return (Criteria) this;
        }

        public Criteria andPartner3IdEqualTo(Integer value) {
            addCriterion("partner3_id =", value, "partner3Id");
            return (Criteria) this;
        }

        public Criteria andPartner3IdNotEqualTo(Integer value) {
            addCriterion("partner3_id <>", value, "partner3Id");
            return (Criteria) this;
        }

        public Criteria andPartner3IdGreaterThan(Integer value) {
            addCriterion("partner3_id >", value, "partner3Id");
            return (Criteria) this;
        }

        public Criteria andPartner3IdGreaterThanOrEqualTo(Integer value) {
            addCriterion("partner3_id >=", value, "partner3Id");
            return (Criteria) this;
        }

        public Criteria andPartner3IdLessThan(Integer value) {
            addCriterion("partner3_id <", value, "partner3Id");
            return (Criteria) this;
        }

        public Criteria andPartner3IdLessThanOrEqualTo(Integer value) {
            addCriterion("partner3_id <=", value, "partner3Id");
            return (Criteria) this;
        }

        public Criteria andPartner3IdIn(List<Integer> values) {
            addCriterion("partner3_id in", values, "partner3Id");
            return (Criteria) this;
        }

        public Criteria andPartner3IdNotIn(List<Integer> values) {
            addCriterion("partner3_id not in", values, "partner3Id");
            return (Criteria) this;
        }

        public Criteria andPartner3IdBetween(Integer value1, Integer value2) {
            addCriterion("partner3_id between", value1, value2, "partner3Id");
            return (Criteria) this;
        }

        public Criteria andPartner3IdNotBetween(Integer value1, Integer value2) {
            addCriterion("partner3_id not between", value1, value2, "partner3Id");
            return (Criteria) this;
        }

        public Criteria andPartner2IdIsNull() {
            addCriterion("partner2_id is null");
            return (Criteria) this;
        }

        public Criteria andPartner2IdIsNotNull() {
            addCriterion("partner2_id is not null");
            return (Criteria) this;
        }

        public Criteria andPartner2IdEqualTo(Integer value) {
            addCriterion("partner2_id =", value, "partner2Id");
            return (Criteria) this;
        }

        public Criteria andPartner2IdNotEqualTo(Integer value) {
            addCriterion("partner2_id <>", value, "partner2Id");
            return (Criteria) this;
        }

        public Criteria andPartner2IdGreaterThan(Integer value) {
            addCriterion("partner2_id >", value, "partner2Id");
            return (Criteria) this;
        }

        public Criteria andPartner2IdGreaterThanOrEqualTo(Integer value) {
            addCriterion("partner2_id >=", value, "partner2Id");
            return (Criteria) this;
        }

        public Criteria andPartner2IdLessThan(Integer value) {
            addCriterion("partner2_id <", value, "partner2Id");
            return (Criteria) this;
        }

        public Criteria andPartner2IdLessThanOrEqualTo(Integer value) {
            addCriterion("partner2_id <=", value, "partner2Id");
            return (Criteria) this;
        }

        public Criteria andPartner2IdIn(List<Integer> values) {
            addCriterion("partner2_id in", values, "partner2Id");
            return (Criteria) this;
        }

        public Criteria andPartner2IdNotIn(List<Integer> values) {
            addCriterion("partner2_id not in", values, "partner2Id");
            return (Criteria) this;
        }

        public Criteria andPartner2IdBetween(Integer value1, Integer value2) {
            addCriterion("partner2_id between", value1, value2, "partner2Id");
            return (Criteria) this;
        }

        public Criteria andPartner2IdNotBetween(Integer value1, Integer value2) {
            addCriterion("partner2_id not between", value1, value2, "partner2Id");
            return (Criteria) this;
        }

        public Criteria andPartner1IdIsNull() {
            addCriterion("partner1_id is null");
            return (Criteria) this;
        }

        public Criteria andPartner1IdIsNotNull() {
            addCriterion("partner1_id is not null");
            return (Criteria) this;
        }

        public Criteria andPartner1IdEqualTo(Integer value) {
            addCriterion("partner1_id =", value, "partner1Id");
            return (Criteria) this;
        }

        public Criteria andPartner1IdNotEqualTo(Integer value) {
            addCriterion("partner1_id <>", value, "partner1Id");
            return (Criteria) this;
        }

        public Criteria andPartner1IdGreaterThan(Integer value) {
            addCriterion("partner1_id >", value, "partner1Id");
            return (Criteria) this;
        }

        public Criteria andPartner1IdGreaterThanOrEqualTo(Integer value) {
            addCriterion("partner1_id >=", value, "partner1Id");
            return (Criteria) this;
        }

        public Criteria andPartner1IdLessThan(Integer value) {
            addCriterion("partner1_id <", value, "partner1Id");
            return (Criteria) this;
        }

        public Criteria andPartner1IdLessThanOrEqualTo(Integer value) {
            addCriterion("partner1_id <=", value, "partner1Id");
            return (Criteria) this;
        }

        public Criteria andPartner1IdIn(List<Integer> values) {
            addCriterion("partner1_id in", values, "partner1Id");
            return (Criteria) this;
        }

        public Criteria andPartner1IdNotIn(List<Integer> values) {
            addCriterion("partner1_id not in", values, "partner1Id");
            return (Criteria) this;
        }

        public Criteria andPartner1IdBetween(Integer value1, Integer value2) {
            addCriterion("partner1_id between", value1, value2, "partner1Id");
            return (Criteria) this;
        }

        public Criteria andPartner1IdNotBetween(Integer value1, Integer value2) {
            addCriterion("partner1_id not between", value1, value2, "partner1Id");
            return (Criteria) this;
        }

        public Criteria andTotalIsNull() {
            addCriterion("total is null");
            return (Criteria) this;
        }

        public Criteria andTotalIsNotNull() {
            addCriterion("total is not null");
            return (Criteria) this;
        }

        public Criteria andTotalEqualTo(Integer value) {
            addCriterion("total =", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotEqualTo(Integer value) {
            addCriterion("total <>", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThan(Integer value) {
            addCriterion("total >", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThanOrEqualTo(Integer value) {
            addCriterion("total >=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThan(Integer value) {
            addCriterion("total <", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThanOrEqualTo(Integer value) {
            addCriterion("total <=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalIn(List<Integer> values) {
            addCriterion("total in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotIn(List<Integer> values) {
            addCriterion("total not in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalBetween(Integer value1, Integer value2) {
            addCriterion("total between", value1, value2, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotBetween(Integer value1, Integer value2) {
            addCriterion("total not between", value1, value2, "total");
            return (Criteria) this;
        }

        public Criteria andPriceIsNull() {
            addCriterion("price is null");
            return (Criteria) this;
        }

        public Criteria andPriceIsNotNull() {
            addCriterion("price is not null");
            return (Criteria) this;
        }

        public Criteria andPriceEqualTo(BigDecimal value) {
            addCriterion("price =", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotEqualTo(BigDecimal value) {
            addCriterion("price <>", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThan(BigDecimal value) {
            addCriterion("price >", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("price >=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThan(BigDecimal value) {
            addCriterion("price <", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("price <=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceIn(List<BigDecimal> values) {
            addCriterion("price in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotIn(List<BigDecimal> values) {
            addCriterion("price not in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price not between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPayCreditIsNull() {
            addCriterion("pay_credit is null");
            return (Criteria) this;
        }

        public Criteria andPayCreditIsNotNull() {
            addCriterion("pay_credit is not null");
            return (Criteria) this;
        }

        public Criteria andPayCreditEqualTo(BigDecimal value) {
            addCriterion("pay_credit =", value, "payCredit");
            return (Criteria) this;
        }

        public Criteria andPayCreditNotEqualTo(BigDecimal value) {
            addCriterion("pay_credit <>", value, "payCredit");
            return (Criteria) this;
        }

        public Criteria andPayCreditGreaterThan(BigDecimal value) {
            addCriterion("pay_credit >", value, "payCredit");
            return (Criteria) this;
        }

        public Criteria andPayCreditGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("pay_credit >=", value, "payCredit");
            return (Criteria) this;
        }

        public Criteria andPayCreditLessThan(BigDecimal value) {
            addCriterion("pay_credit <", value, "payCredit");
            return (Criteria) this;
        }

        public Criteria andPayCreditLessThanOrEqualTo(BigDecimal value) {
            addCriterion("pay_credit <=", value, "payCredit");
            return (Criteria) this;
        }

        public Criteria andPayCreditIn(List<BigDecimal> values) {
            addCriterion("pay_credit in", values, "payCredit");
            return (Criteria) this;
        }

        public Criteria andPayCreditNotIn(List<BigDecimal> values) {
            addCriterion("pay_credit not in", values, "payCredit");
            return (Criteria) this;
        }

        public Criteria andPayCreditBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pay_credit between", value1, value2, "payCredit");
            return (Criteria) this;
        }

        public Criteria andPayCreditNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pay_credit not between", value1, value2, "payCredit");
            return (Criteria) this;
        }

        public Criteria andPayTypeIsNull() {
            addCriterion("pay_type is null");
            return (Criteria) this;
        }

        public Criteria andPayTypeIsNotNull() {
            addCriterion("pay_type is not null");
            return (Criteria) this;
        }

        public Criteria andPayTypeEqualTo(Byte value) {
            addCriterion("pay_type =", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotEqualTo(Byte value) {
            addCriterion("pay_type <>", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThan(Byte value) {
            addCriterion("pay_type >", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("pay_type >=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThan(Byte value) {
            addCriterion("pay_type <", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThanOrEqualTo(Byte value) {
            addCriterion("pay_type <=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeIn(List<Byte> values) {
            addCriterion("pay_type in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotIn(List<Byte> values) {
            addCriterion("pay_type not in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeBetween(Byte value1, Byte value2) {
            addCriterion("pay_type between", value1, value2, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("pay_type not between", value1, value2, "payType");
            return (Criteria) this;
        }

        public Criteria andPaymentNoIsNull() {
            addCriterion("payment_no is null");
            return (Criteria) this;
        }

        public Criteria andPaymentNoIsNotNull() {
            addCriterion("payment_no is not null");
            return (Criteria) this;
        }

        public Criteria andPaymentNoEqualTo(String value) {
            addCriterion("payment_no =", value, "paymentNo");
            return (Criteria) this;
        }

        public Criteria andPaymentNoNotEqualTo(String value) {
            addCriterion("payment_no <>", value, "paymentNo");
            return (Criteria) this;
        }

        public Criteria andPaymentNoGreaterThan(String value) {
            addCriterion("payment_no >", value, "paymentNo");
            return (Criteria) this;
        }

        public Criteria andPaymentNoGreaterThanOrEqualTo(String value) {
            addCriterion("payment_no >=", value, "paymentNo");
            return (Criteria) this;
        }

        public Criteria andPaymentNoLessThan(String value) {
            addCriterion("payment_no <", value, "paymentNo");
            return (Criteria) this;
        }

        public Criteria andPaymentNoLessThanOrEqualTo(String value) {
            addCriterion("payment_no <=", value, "paymentNo");
            return (Criteria) this;
        }

        public Criteria andPaymentNoLike(String value) {
            addCriterion("payment_no like", value, "paymentNo");
            return (Criteria) this;
        }

        public Criteria andPaymentNoNotLike(String value) {
            addCriterion("payment_no not like", value, "paymentNo");
            return (Criteria) this;
        }

        public Criteria andPaymentNoIn(List<String> values) {
            addCriterion("payment_no in", values, "paymentNo");
            return (Criteria) this;
        }

        public Criteria andPaymentNoNotIn(List<String> values) {
            addCriterion("payment_no not in", values, "paymentNo");
            return (Criteria) this;
        }

        public Criteria andPaymentNoBetween(String value1, String value2) {
            addCriterion("payment_no between", value1, value2, "paymentNo");
            return (Criteria) this;
        }

        public Criteria andPaymentNoNotBetween(String value1, String value2) {
            addCriterion("payment_no not between", value1, value2, "paymentNo");
            return (Criteria) this;
        }

        public Criteria andPayTimeIsNull() {
            addCriterion("pay_time is null");
            return (Criteria) this;
        }

        public Criteria andPayTimeIsNotNull() {
            addCriterion("pay_time is not null");
            return (Criteria) this;
        }

        public Criteria andPayTimeEqualTo(Integer value) {
            addCriterion("pay_time =", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeNotEqualTo(Integer value) {
            addCriterion("pay_time <>", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeGreaterThan(Integer value) {
            addCriterion("pay_time >", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("pay_time >=", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeLessThan(Integer value) {
            addCriterion("pay_time <", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeLessThanOrEqualTo(Integer value) {
            addCriterion("pay_time <=", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeIn(List<Integer> values) {
            addCriterion("pay_time in", values, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeNotIn(List<Integer> values) {
            addCriterion("pay_time not in", values, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeBetween(Integer value1, Integer value2) {
            addCriterion("pay_time between", value1, value2, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("pay_time not between", value1, value2, "payTime");
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

        public Criteria andAdminRemarkIsNull() {
            addCriterion("admin_remark is null");
            return (Criteria) this;
        }

        public Criteria andAdminRemarkIsNotNull() {
            addCriterion("admin_remark is not null");
            return (Criteria) this;
        }

        public Criteria andAdminRemarkEqualTo(String value) {
            addCriterion("admin_remark =", value, "adminRemark");
            return (Criteria) this;
        }

        public Criteria andAdminRemarkNotEqualTo(String value) {
            addCriterion("admin_remark <>", value, "adminRemark");
            return (Criteria) this;
        }

        public Criteria andAdminRemarkGreaterThan(String value) {
            addCriterion("admin_remark >", value, "adminRemark");
            return (Criteria) this;
        }

        public Criteria andAdminRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("admin_remark >=", value, "adminRemark");
            return (Criteria) this;
        }

        public Criteria andAdminRemarkLessThan(String value) {
            addCriterion("admin_remark <", value, "adminRemark");
            return (Criteria) this;
        }

        public Criteria andAdminRemarkLessThanOrEqualTo(String value) {
            addCriterion("admin_remark <=", value, "adminRemark");
            return (Criteria) this;
        }

        public Criteria andAdminRemarkLike(String value) {
            addCriterion("admin_remark like", value, "adminRemark");
            return (Criteria) this;
        }

        public Criteria andAdminRemarkNotLike(String value) {
            addCriterion("admin_remark not like", value, "adminRemark");
            return (Criteria) this;
        }

        public Criteria andAdminRemarkIn(List<String> values) {
            addCriterion("admin_remark in", values, "adminRemark");
            return (Criteria) this;
        }

        public Criteria andAdminRemarkNotIn(List<String> values) {
            addCriterion("admin_remark not in", values, "adminRemark");
            return (Criteria) this;
        }

        public Criteria andAdminRemarkBetween(String value1, String value2) {
            addCriterion("admin_remark between", value1, value2, "adminRemark");
            return (Criteria) this;
        }

        public Criteria andAdminRemarkNotBetween(String value1, String value2) {
            addCriterion("admin_remark not between", value1, value2, "adminRemark");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNull() {
            addCriterion("username is null");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNotNull() {
            addCriterion("username is not null");
            return (Criteria) this;
        }

        public Criteria andUsernameEqualTo(String value) {
            addCriterion("username =", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotEqualTo(String value) {
            addCriterion("username <>", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThan(String value) {
            addCriterion("username >", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("username >=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThan(String value) {
            addCriterion("username <", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThanOrEqualTo(String value) {
            addCriterion("username <=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLike(String value) {
            addCriterion("username like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotLike(String value) {
            addCriterion("username not like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameIn(List<String> values) {
            addCriterion("username in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotIn(List<String> values) {
            addCriterion("username not in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameBetween(String value1, String value2) {
            addCriterion("username between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotBetween(String value1, String value2) {
            addCriterion("username not between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andMobileIsNull() {
            addCriterion("mobile is null");
            return (Criteria) this;
        }

        public Criteria andMobileIsNotNull() {
            addCriterion("mobile is not null");
            return (Criteria) this;
        }

        public Criteria andMobileEqualTo(String value) {
            addCriterion("mobile =", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotEqualTo(String value) {
            addCriterion("mobile <>", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThan(String value) {
            addCriterion("mobile >", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThanOrEqualTo(String value) {
            addCriterion("mobile >=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThan(String value) {
            addCriterion("mobile <", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThanOrEqualTo(String value) {
            addCriterion("mobile <=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLike(String value) {
            addCriterion("mobile like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotLike(String value) {
            addCriterion("mobile not like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileIn(List<String> values) {
            addCriterion("mobile in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotIn(List<String> values) {
            addCriterion("mobile not in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileBetween(String value1, String value2) {
            addCriterion("mobile between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotBetween(String value1, String value2) {
            addCriterion("mobile not between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andAddressIsNull() {
            addCriterion("address is null");
            return (Criteria) this;
        }

        public Criteria andAddressIsNotNull() {
            addCriterion("address is not null");
            return (Criteria) this;
        }

        public Criteria andAddressEqualTo(String value) {
            addCriterion("address =", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotEqualTo(String value) {
            addCriterion("address <>", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThan(String value) {
            addCriterion("address >", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThanOrEqualTo(String value) {
            addCriterion("address >=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThan(String value) {
            addCriterion("address <", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThanOrEqualTo(String value) {
            addCriterion("address <=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLike(String value) {
            addCriterion("address like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotLike(String value) {
            addCriterion("address not like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressIn(List<String> values) {
            addCriterion("address in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotIn(List<String> values) {
            addCriterion("address not in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressBetween(String value1, String value2) {
            addCriterion("address between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotBetween(String value1, String value2) {
            addCriterion("address not between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andZipcodeIsNull() {
            addCriterion("zipcode is null");
            return (Criteria) this;
        }

        public Criteria andZipcodeIsNotNull() {
            addCriterion("zipcode is not null");
            return (Criteria) this;
        }

        public Criteria andZipcodeEqualTo(String value) {
            addCriterion("zipcode =", value, "zipcode");
            return (Criteria) this;
        }

        public Criteria andZipcodeNotEqualTo(String value) {
            addCriterion("zipcode <>", value, "zipcode");
            return (Criteria) this;
        }

        public Criteria andZipcodeGreaterThan(String value) {
            addCriterion("zipcode >", value, "zipcode");
            return (Criteria) this;
        }

        public Criteria andZipcodeGreaterThanOrEqualTo(String value) {
            addCriterion("zipcode >=", value, "zipcode");
            return (Criteria) this;
        }

        public Criteria andZipcodeLessThan(String value) {
            addCriterion("zipcode <", value, "zipcode");
            return (Criteria) this;
        }

        public Criteria andZipcodeLessThanOrEqualTo(String value) {
            addCriterion("zipcode <=", value, "zipcode");
            return (Criteria) this;
        }

        public Criteria andZipcodeLike(String value) {
            addCriterion("zipcode like", value, "zipcode");
            return (Criteria) this;
        }

        public Criteria andZipcodeNotLike(String value) {
            addCriterion("zipcode not like", value, "zipcode");
            return (Criteria) this;
        }

        public Criteria andZipcodeIn(List<String> values) {
            addCriterion("zipcode in", values, "zipcode");
            return (Criteria) this;
        }

        public Criteria andZipcodeNotIn(List<String> values) {
            addCriterion("zipcode not in", values, "zipcode");
            return (Criteria) this;
        }

        public Criteria andZipcodeBetween(String value1, String value2) {
            addCriterion("zipcode between", value1, value2, "zipcode");
            return (Criteria) this;
        }

        public Criteria andZipcodeNotBetween(String value1, String value2) {
            addCriterion("zipcode not between", value1, value2, "zipcode");
            return (Criteria) this;
        }

        public Criteria andExpressTitleIsNull() {
            addCriterion("express_title is null");
            return (Criteria) this;
        }

        public Criteria andExpressTitleIsNotNull() {
            addCriterion("express_title is not null");
            return (Criteria) this;
        }

        public Criteria andExpressTitleEqualTo(String value) {
            addCriterion("express_title =", value, "expressTitle");
            return (Criteria) this;
        }

        public Criteria andExpressTitleNotEqualTo(String value) {
            addCriterion("express_title <>", value, "expressTitle");
            return (Criteria) this;
        }

        public Criteria andExpressTitleGreaterThan(String value) {
            addCriterion("express_title >", value, "expressTitle");
            return (Criteria) this;
        }

        public Criteria andExpressTitleGreaterThanOrEqualTo(String value) {
            addCriterion("express_title >=", value, "expressTitle");
            return (Criteria) this;
        }

        public Criteria andExpressTitleLessThan(String value) {
            addCriterion("express_title <", value, "expressTitle");
            return (Criteria) this;
        }

        public Criteria andExpressTitleLessThanOrEqualTo(String value) {
            addCriterion("express_title <=", value, "expressTitle");
            return (Criteria) this;
        }

        public Criteria andExpressTitleLike(String value) {
            addCriterion("express_title like", value, "expressTitle");
            return (Criteria) this;
        }

        public Criteria andExpressTitleNotLike(String value) {
            addCriterion("express_title not like", value, "expressTitle");
            return (Criteria) this;
        }

        public Criteria andExpressTitleIn(List<String> values) {
            addCriterion("express_title in", values, "expressTitle");
            return (Criteria) this;
        }

        public Criteria andExpressTitleNotIn(List<String> values) {
            addCriterion("express_title not in", values, "expressTitle");
            return (Criteria) this;
        }

        public Criteria andExpressTitleBetween(String value1, String value2) {
            addCriterion("express_title between", value1, value2, "expressTitle");
            return (Criteria) this;
        }

        public Criteria andExpressTitleNotBetween(String value1, String value2) {
            addCriterion("express_title not between", value1, value2, "expressTitle");
            return (Criteria) this;
        }

        public Criteria andExpressAliasIsNull() {
            addCriterion("express_alias is null");
            return (Criteria) this;
        }

        public Criteria andExpressAliasIsNotNull() {
            addCriterion("express_alias is not null");
            return (Criteria) this;
        }

        public Criteria andExpressAliasEqualTo(String value) {
            addCriterion("express_alias =", value, "expressAlias");
            return (Criteria) this;
        }

        public Criteria andExpressAliasNotEqualTo(String value) {
            addCriterion("express_alias <>", value, "expressAlias");
            return (Criteria) this;
        }

        public Criteria andExpressAliasGreaterThan(String value) {
            addCriterion("express_alias >", value, "expressAlias");
            return (Criteria) this;
        }

        public Criteria andExpressAliasGreaterThanOrEqualTo(String value) {
            addCriterion("express_alias >=", value, "expressAlias");
            return (Criteria) this;
        }

        public Criteria andExpressAliasLessThan(String value) {
            addCriterion("express_alias <", value, "expressAlias");
            return (Criteria) this;
        }

        public Criteria andExpressAliasLessThanOrEqualTo(String value) {
            addCriterion("express_alias <=", value, "expressAlias");
            return (Criteria) this;
        }

        public Criteria andExpressAliasLike(String value) {
            addCriterion("express_alias like", value, "expressAlias");
            return (Criteria) this;
        }

        public Criteria andExpressAliasNotLike(String value) {
            addCriterion("express_alias not like", value, "expressAlias");
            return (Criteria) this;
        }

        public Criteria andExpressAliasIn(List<String> values) {
            addCriterion("express_alias in", values, "expressAlias");
            return (Criteria) this;
        }

        public Criteria andExpressAliasNotIn(List<String> values) {
            addCriterion("express_alias not in", values, "expressAlias");
            return (Criteria) this;
        }

        public Criteria andExpressAliasBetween(String value1, String value2) {
            addCriterion("express_alias between", value1, value2, "expressAlias");
            return (Criteria) this;
        }

        public Criteria andExpressAliasNotBetween(String value1, String value2) {
            addCriterion("express_alias not between", value1, value2, "expressAlias");
            return (Criteria) this;
        }

        public Criteria andExpressNoIsNull() {
            addCriterion("express_no is null");
            return (Criteria) this;
        }

        public Criteria andExpressNoIsNotNull() {
            addCriterion("express_no is not null");
            return (Criteria) this;
        }

        public Criteria andExpressNoEqualTo(String value) {
            addCriterion("express_no =", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoNotEqualTo(String value) {
            addCriterion("express_no <>", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoGreaterThan(String value) {
            addCriterion("express_no >", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoGreaterThanOrEqualTo(String value) {
            addCriterion("express_no >=", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoLessThan(String value) {
            addCriterion("express_no <", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoLessThanOrEqualTo(String value) {
            addCriterion("express_no <=", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoLike(String value) {
            addCriterion("express_no like", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoNotLike(String value) {
            addCriterion("express_no not like", value, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoIn(List<String> values) {
            addCriterion("express_no in", values, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoNotIn(List<String> values) {
            addCriterion("express_no not in", values, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoBetween(String value1, String value2) {
            addCriterion("express_no between", value1, value2, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressNoNotBetween(String value1, String value2) {
            addCriterion("express_no not between", value1, value2, "expressNo");
            return (Criteria) this;
        }

        public Criteria andExpressFeeIsNull() {
            addCriterion("express_fee is null");
            return (Criteria) this;
        }

        public Criteria andExpressFeeIsNotNull() {
            addCriterion("express_fee is not null");
            return (Criteria) this;
        }

        public Criteria andExpressFeeEqualTo(BigDecimal value) {
            addCriterion("express_fee =", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeNotEqualTo(BigDecimal value) {
            addCriterion("express_fee <>", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeGreaterThan(BigDecimal value) {
            addCriterion("express_fee >", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("express_fee >=", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeLessThan(BigDecimal value) {
            addCriterion("express_fee <", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("express_fee <=", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeIn(List<BigDecimal> values) {
            addCriterion("express_fee in", values, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeNotIn(List<BigDecimal> values) {
            addCriterion("express_fee not in", values, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("express_fee between", value1, value2, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("express_fee not between", value1, value2, "expressFee");
            return (Criteria) this;
        }

        public Criteria andCreditTypeIsNull() {
            addCriterion("credit_type is null");
            return (Criteria) this;
        }

        public Criteria andCreditTypeIsNotNull() {
            addCriterion("credit_type is not null");
            return (Criteria) this;
        }

        public Criteria andCreditTypeEqualTo(String value) {
            addCriterion("credit_type =", value, "creditType");
            return (Criteria) this;
        }

        public Criteria andCreditTypeNotEqualTo(String value) {
            addCriterion("credit_type <>", value, "creditType");
            return (Criteria) this;
        }

        public Criteria andCreditTypeGreaterThan(String value) {
            addCriterion("credit_type >", value, "creditType");
            return (Criteria) this;
        }

        public Criteria andCreditTypeGreaterThanOrEqualTo(String value) {
            addCriterion("credit_type >=", value, "creditType");
            return (Criteria) this;
        }

        public Criteria andCreditTypeLessThan(String value) {
            addCriterion("credit_type <", value, "creditType");
            return (Criteria) this;
        }

        public Criteria andCreditTypeLessThanOrEqualTo(String value) {
            addCriterion("credit_type <=", value, "creditType");
            return (Criteria) this;
        }

        public Criteria andCreditTypeLike(String value) {
            addCriterion("credit_type like", value, "creditType");
            return (Criteria) this;
        }

        public Criteria andCreditTypeNotLike(String value) {
            addCriterion("credit_type not like", value, "creditType");
            return (Criteria) this;
        }

        public Criteria andCreditTypeIn(List<String> values) {
            addCriterion("credit_type in", values, "creditType");
            return (Criteria) this;
        }

        public Criteria andCreditTypeNotIn(List<String> values) {
            addCriterion("credit_type not in", values, "creditType");
            return (Criteria) this;
        }

        public Criteria andCreditTypeBetween(String value1, String value2) {
            addCriterion("credit_type between", value1, value2, "creditType");
            return (Criteria) this;
        }

        public Criteria andCreditTypeNotBetween(String value1, String value2) {
            addCriterion("credit_type not between", value1, value2, "creditType");
            return (Criteria) this;
        }

        public Criteria andRewardCreditIsNull() {
            addCriterion("reward_credit is null");
            return (Criteria) this;
        }

        public Criteria andRewardCreditIsNotNull() {
            addCriterion("reward_credit is not null");
            return (Criteria) this;
        }

        public Criteria andRewardCreditEqualTo(BigDecimal value) {
            addCriterion("reward_credit =", value, "rewardCredit");
            return (Criteria) this;
        }

        public Criteria andRewardCreditNotEqualTo(BigDecimal value) {
            addCriterion("reward_credit <>", value, "rewardCredit");
            return (Criteria) this;
        }

        public Criteria andRewardCreditGreaterThan(BigDecimal value) {
            addCriterion("reward_credit >", value, "rewardCredit");
            return (Criteria) this;
        }

        public Criteria andRewardCreditGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("reward_credit >=", value, "rewardCredit");
            return (Criteria) this;
        }

        public Criteria andRewardCreditLessThan(BigDecimal value) {
            addCriterion("reward_credit <", value, "rewardCredit");
            return (Criteria) this;
        }

        public Criteria andRewardCreditLessThanOrEqualTo(BigDecimal value) {
            addCriterion("reward_credit <=", value, "rewardCredit");
            return (Criteria) this;
        }

        public Criteria andRewardCreditIn(List<BigDecimal> values) {
            addCriterion("reward_credit in", values, "rewardCredit");
            return (Criteria) this;
        }

        public Criteria andRewardCreditNotIn(List<BigDecimal> values) {
            addCriterion("reward_credit not in", values, "rewardCredit");
            return (Criteria) this;
        }

        public Criteria andRewardCreditBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("reward_credit between", value1, value2, "rewardCredit");
            return (Criteria) this;
        }

        public Criteria andRewardCreditNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("reward_credit not between", value1, value2, "rewardCredit");
            return (Criteria) this;
        }

        public Criteria andCashCreditIsNull() {
            addCriterion("cash_credit is null");
            return (Criteria) this;
        }

        public Criteria andCashCreditIsNotNull() {
            addCriterion("cash_credit is not null");
            return (Criteria) this;
        }

        public Criteria andCashCreditEqualTo(BigDecimal value) {
            addCriterion("cash_credit =", value, "cashCredit");
            return (Criteria) this;
        }

        public Criteria andCashCreditNotEqualTo(BigDecimal value) {
            addCriterion("cash_credit <>", value, "cashCredit");
            return (Criteria) this;
        }

        public Criteria andCashCreditGreaterThan(BigDecimal value) {
            addCriterion("cash_credit >", value, "cashCredit");
            return (Criteria) this;
        }

        public Criteria andCashCreditGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("cash_credit >=", value, "cashCredit");
            return (Criteria) this;
        }

        public Criteria andCashCreditLessThan(BigDecimal value) {
            addCriterion("cash_credit <", value, "cashCredit");
            return (Criteria) this;
        }

        public Criteria andCashCreditLessThanOrEqualTo(BigDecimal value) {
            addCriterion("cash_credit <=", value, "cashCredit");
            return (Criteria) this;
        }

        public Criteria andCashCreditIn(List<BigDecimal> values) {
            addCriterion("cash_credit in", values, "cashCredit");
            return (Criteria) this;
        }

        public Criteria andCashCreditNotIn(List<BigDecimal> values) {
            addCriterion("cash_credit not in", values, "cashCredit");
            return (Criteria) this;
        }

        public Criteria andCashCreditBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cash_credit between", value1, value2, "cashCredit");
            return (Criteria) this;
        }

        public Criteria andCashCreditNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cash_credit not between", value1, value2, "cashCredit");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeIsNull() {
            addCriterion("dispatch_type is null");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeIsNotNull() {
            addCriterion("dispatch_type is not null");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeEqualTo(Byte value) {
            addCriterion("dispatch_type =", value, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeNotEqualTo(Byte value) {
            addCriterion("dispatch_type <>", value, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeGreaterThan(Byte value) {
            addCriterion("dispatch_type >", value, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("dispatch_type >=", value, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeLessThan(Byte value) {
            addCriterion("dispatch_type <", value, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeLessThanOrEqualTo(Byte value) {
            addCriterion("dispatch_type <=", value, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeIn(List<Byte> values) {
            addCriterion("dispatch_type in", values, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeNotIn(List<Byte> values) {
            addCriterion("dispatch_type not in", values, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeBetween(Byte value1, Byte value2) {
            addCriterion("dispatch_type between", value1, value2, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andDispatchTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("dispatch_type not between", value1, value2, "dispatchType");
            return (Criteria) this;
        }

        public Criteria andCustomDeliveryIsNull() {
            addCriterion("custom_delivery is null");
            return (Criteria) this;
        }

        public Criteria andCustomDeliveryIsNotNull() {
            addCriterion("custom_delivery is not null");
            return (Criteria) this;
        }

        public Criteria andCustomDeliveryEqualTo(String value) {
            addCriterion("custom_delivery =", value, "customDelivery");
            return (Criteria) this;
        }

        public Criteria andCustomDeliveryNotEqualTo(String value) {
            addCriterion("custom_delivery <>", value, "customDelivery");
            return (Criteria) this;
        }

        public Criteria andCustomDeliveryGreaterThan(String value) {
            addCriterion("custom_delivery >", value, "customDelivery");
            return (Criteria) this;
        }

        public Criteria andCustomDeliveryGreaterThanOrEqualTo(String value) {
            addCriterion("custom_delivery >=", value, "customDelivery");
            return (Criteria) this;
        }

        public Criteria andCustomDeliveryLessThan(String value) {
            addCriterion("custom_delivery <", value, "customDelivery");
            return (Criteria) this;
        }

        public Criteria andCustomDeliveryLessThanOrEqualTo(String value) {
            addCriterion("custom_delivery <=", value, "customDelivery");
            return (Criteria) this;
        }

        public Criteria andCustomDeliveryLike(String value) {
            addCriterion("custom_delivery like", value, "customDelivery");
            return (Criteria) this;
        }

        public Criteria andCustomDeliveryNotLike(String value) {
            addCriterion("custom_delivery not like", value, "customDelivery");
            return (Criteria) this;
        }

        public Criteria andCustomDeliveryIn(List<String> values) {
            addCriterion("custom_delivery in", values, "customDelivery");
            return (Criteria) this;
        }

        public Criteria andCustomDeliveryNotIn(List<String> values) {
            addCriterion("custom_delivery not in", values, "customDelivery");
            return (Criteria) this;
        }

        public Criteria andCustomDeliveryBetween(String value1, String value2) {
            addCriterion("custom_delivery between", value1, value2, "customDelivery");
            return (Criteria) this;
        }

        public Criteria andCustomDeliveryNotBetween(String value1, String value2) {
            addCriterion("custom_delivery not between", value1, value2, "customDelivery");
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

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Byte value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Byte value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Byte value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Byte value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Byte value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Byte> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Byte> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Byte value1, Byte value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andCommissionStatusIsNull() {
            addCriterion("commission_status is null");
            return (Criteria) this;
        }

        public Criteria andCommissionStatusIsNotNull() {
            addCriterion("commission_status is not null");
            return (Criteria) this;
        }

        public Criteria andCommissionStatusEqualTo(Byte value) {
            addCriterion("commission_status =", value, "commissionStatus");
            return (Criteria) this;
        }

        public Criteria andCommissionStatusNotEqualTo(Byte value) {
            addCriterion("commission_status <>", value, "commissionStatus");
            return (Criteria) this;
        }

        public Criteria andCommissionStatusGreaterThan(Byte value) {
            addCriterion("commission_status >", value, "commissionStatus");
            return (Criteria) this;
        }

        public Criteria andCommissionStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("commission_status >=", value, "commissionStatus");
            return (Criteria) this;
        }

        public Criteria andCommissionStatusLessThan(Byte value) {
            addCriterion("commission_status <", value, "commissionStatus");
            return (Criteria) this;
        }

        public Criteria andCommissionStatusLessThanOrEqualTo(Byte value) {
            addCriterion("commission_status <=", value, "commissionStatus");
            return (Criteria) this;
        }

        public Criteria andCommissionStatusIn(List<Byte> values) {
            addCriterion("commission_status in", values, "commissionStatus");
            return (Criteria) this;
        }

        public Criteria andCommissionStatusNotIn(List<Byte> values) {
            addCriterion("commission_status not in", values, "commissionStatus");
            return (Criteria) this;
        }

        public Criteria andCommissionStatusBetween(Byte value1, Byte value2) {
            addCriterion("commission_status between", value1, value2, "commissionStatus");
            return (Criteria) this;
        }

        public Criteria andCommissionStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("commission_status not between", value1, value2, "commissionStatus");
            return (Criteria) this;
        }

        public Criteria andCheckoutIsNull() {
            addCriterion("checkout is null");
            return (Criteria) this;
        }

        public Criteria andCheckoutIsNotNull() {
            addCriterion("checkout is not null");
            return (Criteria) this;
        }

        public Criteria andCheckoutEqualTo(Byte value) {
            addCriterion("checkout =", value, "checkout");
            return (Criteria) this;
        }

        public Criteria andCheckoutNotEqualTo(Byte value) {
            addCriterion("checkout <>", value, "checkout");
            return (Criteria) this;
        }

        public Criteria andCheckoutGreaterThan(Byte value) {
            addCriterion("checkout >", value, "checkout");
            return (Criteria) this;
        }

        public Criteria andCheckoutGreaterThanOrEqualTo(Byte value) {
            addCriterion("checkout >=", value, "checkout");
            return (Criteria) this;
        }

        public Criteria andCheckoutLessThan(Byte value) {
            addCriterion("checkout <", value, "checkout");
            return (Criteria) this;
        }

        public Criteria andCheckoutLessThanOrEqualTo(Byte value) {
            addCriterion("checkout <=", value, "checkout");
            return (Criteria) this;
        }

        public Criteria andCheckoutIn(List<Byte> values) {
            addCriterion("checkout in", values, "checkout");
            return (Criteria) this;
        }

        public Criteria andCheckoutNotIn(List<Byte> values) {
            addCriterion("checkout not in", values, "checkout");
            return (Criteria) this;
        }

        public Criteria andCheckoutBetween(Byte value1, Byte value2) {
            addCriterion("checkout between", value1, value2, "checkout");
            return (Criteria) this;
        }

        public Criteria andCheckoutNotBetween(Byte value1, Byte value2) {
            addCriterion("checkout not between", value1, value2, "checkout");
            return (Criteria) this;
        }

        public Criteria andWxpayServiceProviderIsNull() {
            addCriterion("wxpay_service_provider is null");
            return (Criteria) this;
        }

        public Criteria andWxpayServiceProviderIsNotNull() {
            addCriterion("wxpay_service_provider is not null");
            return (Criteria) this;
        }

        public Criteria andWxpayServiceProviderEqualTo(Byte value) {
            addCriterion("wxpay_service_provider =", value, "wxpayServiceProvider");
            return (Criteria) this;
        }

        public Criteria andWxpayServiceProviderNotEqualTo(Byte value) {
            addCriterion("wxpay_service_provider <>", value, "wxpayServiceProvider");
            return (Criteria) this;
        }

        public Criteria andWxpayServiceProviderGreaterThan(Byte value) {
            addCriterion("wxpay_service_provider >", value, "wxpayServiceProvider");
            return (Criteria) this;
        }

        public Criteria andWxpayServiceProviderGreaterThanOrEqualTo(Byte value) {
            addCriterion("wxpay_service_provider >=", value, "wxpayServiceProvider");
            return (Criteria) this;
        }

        public Criteria andWxpayServiceProviderLessThan(Byte value) {
            addCriterion("wxpay_service_provider <", value, "wxpayServiceProvider");
            return (Criteria) this;
        }

        public Criteria andWxpayServiceProviderLessThanOrEqualTo(Byte value) {
            addCriterion("wxpay_service_provider <=", value, "wxpayServiceProvider");
            return (Criteria) this;
        }

        public Criteria andWxpayServiceProviderIn(List<Byte> values) {
            addCriterion("wxpay_service_provider in", values, "wxpayServiceProvider");
            return (Criteria) this;
        }

        public Criteria andWxpayServiceProviderNotIn(List<Byte> values) {
            addCriterion("wxpay_service_provider not in", values, "wxpayServiceProvider");
            return (Criteria) this;
        }

        public Criteria andWxpayServiceProviderBetween(Byte value1, Byte value2) {
            addCriterion("wxpay_service_provider between", value1, value2, "wxpayServiceProvider");
            return (Criteria) this;
        }

        public Criteria andWxpayServiceProviderNotBetween(Byte value1, Byte value2) {
            addCriterion("wxpay_service_provider not between", value1, value2, "wxpayServiceProvider");
            return (Criteria) this;
        }

        public Criteria andPayuRidIsNull() {
            addCriterion("payu_rid is null");
            return (Criteria) this;
        }

        public Criteria andPayuRidIsNotNull() {
            addCriterion("payu_rid is not null");
            return (Criteria) this;
        }

        public Criteria andPayuRidEqualTo(Integer value) {
            addCriterion("payu_rid =", value, "payuRid");
            return (Criteria) this;
        }

        public Criteria andPayuRidNotEqualTo(Integer value) {
            addCriterion("payu_rid <>", value, "payuRid");
            return (Criteria) this;
        }

        public Criteria andPayuRidGreaterThan(Integer value) {
            addCriterion("payu_rid >", value, "payuRid");
            return (Criteria) this;
        }

        public Criteria andPayuRidGreaterThanOrEqualTo(Integer value) {
            addCriterion("payu_rid >=", value, "payuRid");
            return (Criteria) this;
        }

        public Criteria andPayuRidLessThan(Integer value) {
            addCriterion("payu_rid <", value, "payuRid");
            return (Criteria) this;
        }

        public Criteria andPayuRidLessThanOrEqualTo(Integer value) {
            addCriterion("payu_rid <=", value, "payuRid");
            return (Criteria) this;
        }

        public Criteria andPayuRidIn(List<Integer> values) {
            addCriterion("payu_rid in", values, "payuRid");
            return (Criteria) this;
        }

        public Criteria andPayuRidNotIn(List<Integer> values) {
            addCriterion("payu_rid not in", values, "payuRid");
            return (Criteria) this;
        }

        public Criteria andPayuRidBetween(Integer value1, Integer value2) {
            addCriterion("payu_rid between", value1, value2, "payuRid");
            return (Criteria) this;
        }

        public Criteria andPayuRidNotBetween(Integer value1, Integer value2) {
            addCriterion("payu_rid not between", value1, value2, "payuRid");
            return (Criteria) this;
        }

        public Criteria andSettlementStatusIsNull() {
            addCriterion("settlement_status is null");
            return (Criteria) this;
        }

        public Criteria andSettlementStatusIsNotNull() {
            addCriterion("settlement_status is not null");
            return (Criteria) this;
        }

        public Criteria andSettlementStatusEqualTo(Byte value) {
            addCriterion("settlement_status =", value, "settlementStatus");
            return (Criteria) this;
        }

        public Criteria andSettlementStatusNotEqualTo(Byte value) {
            addCriterion("settlement_status <>", value, "settlementStatus");
            return (Criteria) this;
        }

        public Criteria andSettlementStatusGreaterThan(Byte value) {
            addCriterion("settlement_status >", value, "settlementStatus");
            return (Criteria) this;
        }

        public Criteria andSettlementStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("settlement_status >=", value, "settlementStatus");
            return (Criteria) this;
        }

        public Criteria andSettlementStatusLessThan(Byte value) {
            addCriterion("settlement_status <", value, "settlementStatus");
            return (Criteria) this;
        }

        public Criteria andSettlementStatusLessThanOrEqualTo(Byte value) {
            addCriterion("settlement_status <=", value, "settlementStatus");
            return (Criteria) this;
        }

        public Criteria andSettlementStatusIn(List<Byte> values) {
            addCriterion("settlement_status in", values, "settlementStatus");
            return (Criteria) this;
        }

        public Criteria andSettlementStatusNotIn(List<Byte> values) {
            addCriterion("settlement_status not in", values, "settlementStatus");
            return (Criteria) this;
        }

        public Criteria andSettlementStatusBetween(Byte value1, Byte value2) {
            addCriterion("settlement_status between", value1, value2, "settlementStatus");
            return (Criteria) this;
        }

        public Criteria andSettlementStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("settlement_status not between", value1, value2, "settlementStatus");
            return (Criteria) this;
        }

        public Criteria andPartner3CommissionIsNull() {
            addCriterion("partner3_commission is null");
            return (Criteria) this;
        }

        public Criteria andPartner3CommissionIsNotNull() {
            addCriterion("partner3_commission is not null");
            return (Criteria) this;
        }

        public Criteria andPartner3CommissionEqualTo(BigDecimal value) {
            addCriterion("partner3_commission =", value, "partner3Commission");
            return (Criteria) this;
        }

        public Criteria andPartner3CommissionNotEqualTo(BigDecimal value) {
            addCriterion("partner3_commission <>", value, "partner3Commission");
            return (Criteria) this;
        }

        public Criteria andPartner3CommissionGreaterThan(BigDecimal value) {
            addCriterion("partner3_commission >", value, "partner3Commission");
            return (Criteria) this;
        }

        public Criteria andPartner3CommissionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("partner3_commission >=", value, "partner3Commission");
            return (Criteria) this;
        }

        public Criteria andPartner3CommissionLessThan(BigDecimal value) {
            addCriterion("partner3_commission <", value, "partner3Commission");
            return (Criteria) this;
        }

        public Criteria andPartner3CommissionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("partner3_commission <=", value, "partner3Commission");
            return (Criteria) this;
        }

        public Criteria andPartner3CommissionIn(List<BigDecimal> values) {
            addCriterion("partner3_commission in", values, "partner3Commission");
            return (Criteria) this;
        }

        public Criteria andPartner3CommissionNotIn(List<BigDecimal> values) {
            addCriterion("partner3_commission not in", values, "partner3Commission");
            return (Criteria) this;
        }

        public Criteria andPartner3CommissionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("partner3_commission between", value1, value2, "partner3Commission");
            return (Criteria) this;
        }

        public Criteria andPartner3CommissionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("partner3_commission not between", value1, value2, "partner3Commission");
            return (Criteria) this;
        }

        public Criteria andPartner2CommissionIsNull() {
            addCriterion("partner2_commission is null");
            return (Criteria) this;
        }

        public Criteria andPartner2CommissionIsNotNull() {
            addCriterion("partner2_commission is not null");
            return (Criteria) this;
        }

        public Criteria andPartner2CommissionEqualTo(BigDecimal value) {
            addCriterion("partner2_commission =", value, "partner2Commission");
            return (Criteria) this;
        }

        public Criteria andPartner2CommissionNotEqualTo(BigDecimal value) {
            addCriterion("partner2_commission <>", value, "partner2Commission");
            return (Criteria) this;
        }

        public Criteria andPartner2CommissionGreaterThan(BigDecimal value) {
            addCriterion("partner2_commission >", value, "partner2Commission");
            return (Criteria) this;
        }

        public Criteria andPartner2CommissionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("partner2_commission >=", value, "partner2Commission");
            return (Criteria) this;
        }

        public Criteria andPartner2CommissionLessThan(BigDecimal value) {
            addCriterion("partner2_commission <", value, "partner2Commission");
            return (Criteria) this;
        }

        public Criteria andPartner2CommissionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("partner2_commission <=", value, "partner2Commission");
            return (Criteria) this;
        }

        public Criteria andPartner2CommissionIn(List<BigDecimal> values) {
            addCriterion("partner2_commission in", values, "partner2Commission");
            return (Criteria) this;
        }

        public Criteria andPartner2CommissionNotIn(List<BigDecimal> values) {
            addCriterion("partner2_commission not in", values, "partner2Commission");
            return (Criteria) this;
        }

        public Criteria andPartner2CommissionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("partner2_commission between", value1, value2, "partner2Commission");
            return (Criteria) this;
        }

        public Criteria andPartner2CommissionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("partner2_commission not between", value1, value2, "partner2Commission");
            return (Criteria) this;
        }

        public Criteria andPartner1CommissionIsNull() {
            addCriterion("partner1_commission is null");
            return (Criteria) this;
        }

        public Criteria andPartner1CommissionIsNotNull() {
            addCriterion("partner1_commission is not null");
            return (Criteria) this;
        }

        public Criteria andPartner1CommissionEqualTo(BigDecimal value) {
            addCriterion("partner1_commission =", value, "partner1Commission");
            return (Criteria) this;
        }

        public Criteria andPartner1CommissionNotEqualTo(BigDecimal value) {
            addCriterion("partner1_commission <>", value, "partner1Commission");
            return (Criteria) this;
        }

        public Criteria andPartner1CommissionGreaterThan(BigDecimal value) {
            addCriterion("partner1_commission >", value, "partner1Commission");
            return (Criteria) this;
        }

        public Criteria andPartner1CommissionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("partner1_commission >=", value, "partner1Commission");
            return (Criteria) this;
        }

        public Criteria andPartner1CommissionLessThan(BigDecimal value) {
            addCriterion("partner1_commission <", value, "partner1Commission");
            return (Criteria) this;
        }

        public Criteria andPartner1CommissionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("partner1_commission <=", value, "partner1Commission");
            return (Criteria) this;
        }

        public Criteria andPartner1CommissionIn(List<BigDecimal> values) {
            addCriterion("partner1_commission in", values, "partner1Commission");
            return (Criteria) this;
        }

        public Criteria andPartner1CommissionNotIn(List<BigDecimal> values) {
            addCriterion("partner1_commission not in", values, "partner1Commission");
            return (Criteria) this;
        }

        public Criteria andPartner1CommissionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("partner1_commission between", value1, value2, "partner1Commission");
            return (Criteria) this;
        }

        public Criteria andPartner1CommissionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("partner1_commission not between", value1, value2, "partner1Commission");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNull() {
            addCriterion("createtime is null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNotNull() {
            addCriterion("createtime is not null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeEqualTo(Integer value) {
            addCriterion("createtime =", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotEqualTo(Integer value) {
            addCriterion("createtime <>", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThan(Integer value) {
            addCriterion("createtime >", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("createtime >=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThan(Integer value) {
            addCriterion("createtime <", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThanOrEqualTo(Integer value) {
            addCriterion("createtime <=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIn(List<Integer> values) {
            addCriterion("createtime in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotIn(List<Integer> values) {
            addCriterion("createtime not in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeBetween(Integer value1, Integer value2) {
            addCriterion("createtime between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotBetween(Integer value1, Integer value2) {
            addCriterion("createtime not between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNull() {
            addCriterion("updatetime is null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNotNull() {
            addCriterion("updatetime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeEqualTo(Integer value) {
            addCriterion("updatetime =", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotEqualTo(Integer value) {
            addCriterion("updatetime <>", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThan(Integer value) {
            addCriterion("updatetime >", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("updatetime >=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThan(Integer value) {
            addCriterion("updatetime <", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThanOrEqualTo(Integer value) {
            addCriterion("updatetime <=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIn(List<Integer> values) {
            addCriterion("updatetime in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotIn(List<Integer> values) {
            addCriterion("updatetime not in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeBetween(Integer value1, Integer value2) {
            addCriterion("updatetime between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotBetween(Integer value1, Integer value2) {
            addCriterion("updatetime not between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andJdOrderIdIsNull() {
            addCriterion("jd_order_id is null");
            return (Criteria) this;
        }

        public Criteria andJdOrderIdIsNotNull() {
            addCriterion("jd_order_id is not null");
            return (Criteria) this;
        }

        public Criteria andJdOrderIdEqualTo(String value) {
            addCriterion("jd_order_id =", value, "jdOrderId");
            return (Criteria) this;
        }

        public Criteria andJdOrderIdNotEqualTo(String value) {
            addCriterion("jd_order_id <>", value, "jdOrderId");
            return (Criteria) this;
        }

        public Criteria andJdOrderIdGreaterThan(String value) {
            addCriterion("jd_order_id >", value, "jdOrderId");
            return (Criteria) this;
        }

        public Criteria andJdOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("jd_order_id >=", value, "jdOrderId");
            return (Criteria) this;
        }

        public Criteria andJdOrderIdLessThan(String value) {
            addCriterion("jd_order_id <", value, "jdOrderId");
            return (Criteria) this;
        }

        public Criteria andJdOrderIdLessThanOrEqualTo(String value) {
            addCriterion("jd_order_id <=", value, "jdOrderId");
            return (Criteria) this;
        }

        public Criteria andJdOrderIdLike(String value) {
            addCriterion("jd_order_id like", value, "jdOrderId");
            return (Criteria) this;
        }

        public Criteria andJdOrderIdNotLike(String value) {
            addCriterion("jd_order_id not like", value, "jdOrderId");
            return (Criteria) this;
        }

        public Criteria andJdOrderIdIn(List<String> values) {
            addCriterion("jd_order_id in", values, "jdOrderId");
            return (Criteria) this;
        }

        public Criteria andJdOrderIdNotIn(List<String> values) {
            addCriterion("jd_order_id not in", values, "jdOrderId");
            return (Criteria) this;
        }

        public Criteria andJdOrderIdBetween(String value1, String value2) {
            addCriterion("jd_order_id between", value1, value2, "jdOrderId");
            return (Criteria) this;
        }

        public Criteria andJdOrderIdNotBetween(String value1, String value2) {
            addCriterion("jd_order_id not between", value1, value2, "jdOrderId");
            return (Criteria) this;
        }

        public Criteria andFromJdIsNull() {
            addCriterion("from_jd is null");
            return (Criteria) this;
        }

        public Criteria andFromJdIsNotNull() {
            addCriterion("from_jd is not null");
            return (Criteria) this;
        }

        public Criteria andFromJdEqualTo(Integer value) {
            addCriterion("from_jd =", value, "fromJd");
            return (Criteria) this;
        }

        public Criteria andFromJdNotEqualTo(Integer value) {
            addCriterion("from_jd <>", value, "fromJd");
            return (Criteria) this;
        }

        public Criteria andFromJdGreaterThan(Integer value) {
            addCriterion("from_jd >", value, "fromJd");
            return (Criteria) this;
        }

        public Criteria andFromJdGreaterThanOrEqualTo(Integer value) {
            addCriterion("from_jd >=", value, "fromJd");
            return (Criteria) this;
        }

        public Criteria andFromJdLessThan(Integer value) {
            addCriterion("from_jd <", value, "fromJd");
            return (Criteria) this;
        }

        public Criteria andFromJdLessThanOrEqualTo(Integer value) {
            addCriterion("from_jd <=", value, "fromJd");
            return (Criteria) this;
        }

        public Criteria andFromJdIn(List<Integer> values) {
            addCriterion("from_jd in", values, "fromJd");
            return (Criteria) this;
        }

        public Criteria andFromJdNotIn(List<Integer> values) {
            addCriterion("from_jd not in", values, "fromJd");
            return (Criteria) this;
        }

        public Criteria andFromJdBetween(Integer value1, Integer value2) {
            addCriterion("from_jd between", value1, value2, "fromJd");
            return (Criteria) this;
        }

        public Criteria andFromJdNotBetween(Integer value1, Integer value2) {
            addCriterion("from_jd not between", value1, value2, "fromJd");
            return (Criteria) this;
        }

        public Criteria andJdAddressIsNull() {
            addCriterion("jd_address is null");
            return (Criteria) this;
        }

        public Criteria andJdAddressIsNotNull() {
            addCriterion("jd_address is not null");
            return (Criteria) this;
        }

        public Criteria andJdAddressEqualTo(String value) {
            addCriterion("jd_address =", value, "jdAddress");
            return (Criteria) this;
        }

        public Criteria andJdAddressNotEqualTo(String value) {
            addCriterion("jd_address <>", value, "jdAddress");
            return (Criteria) this;
        }

        public Criteria andJdAddressGreaterThan(String value) {
            addCriterion("jd_address >", value, "jdAddress");
            return (Criteria) this;
        }

        public Criteria andJdAddressGreaterThanOrEqualTo(String value) {
            addCriterion("jd_address >=", value, "jdAddress");
            return (Criteria) this;
        }

        public Criteria andJdAddressLessThan(String value) {
            addCriterion("jd_address <", value, "jdAddress");
            return (Criteria) this;
        }

        public Criteria andJdAddressLessThanOrEqualTo(String value) {
            addCriterion("jd_address <=", value, "jdAddress");
            return (Criteria) this;
        }

        public Criteria andJdAddressLike(String value) {
            addCriterion("jd_address like", value, "jdAddress");
            return (Criteria) this;
        }

        public Criteria andJdAddressNotLike(String value) {
            addCriterion("jd_address not like", value, "jdAddress");
            return (Criteria) this;
        }

        public Criteria andJdAddressIn(List<String> values) {
            addCriterion("jd_address in", values, "jdAddress");
            return (Criteria) this;
        }

        public Criteria andJdAddressNotIn(List<String> values) {
            addCriterion("jd_address not in", values, "jdAddress");
            return (Criteria) this;
        }

        public Criteria andJdAddressBetween(String value1, String value2) {
            addCriterion("jd_address between", value1, value2, "jdAddress");
            return (Criteria) this;
        }

        public Criteria andJdAddressNotBetween(String value1, String value2) {
            addCriterion("jd_address not between", value1, value2, "jdAddress");
            return (Criteria) this;
        }

        public Criteria andJdAddressDescIsNull() {
            addCriterion("jd_address_desc is null");
            return (Criteria) this;
        }

        public Criteria andJdAddressDescIsNotNull() {
            addCriterion("jd_address_desc is not null");
            return (Criteria) this;
        }

        public Criteria andJdAddressDescEqualTo(String value) {
            addCriterion("jd_address_desc =", value, "jdAddressDesc");
            return (Criteria) this;
        }

        public Criteria andJdAddressDescNotEqualTo(String value) {
            addCriterion("jd_address_desc <>", value, "jdAddressDesc");
            return (Criteria) this;
        }

        public Criteria andJdAddressDescGreaterThan(String value) {
            addCriterion("jd_address_desc >", value, "jdAddressDesc");
            return (Criteria) this;
        }

        public Criteria andJdAddressDescGreaterThanOrEqualTo(String value) {
            addCriterion("jd_address_desc >=", value, "jdAddressDesc");
            return (Criteria) this;
        }

        public Criteria andJdAddressDescLessThan(String value) {
            addCriterion("jd_address_desc <", value, "jdAddressDesc");
            return (Criteria) this;
        }

        public Criteria andJdAddressDescLessThanOrEqualTo(String value) {
            addCriterion("jd_address_desc <=", value, "jdAddressDesc");
            return (Criteria) this;
        }

        public Criteria andJdAddressDescLike(String value) {
            addCriterion("jd_address_desc like", value, "jdAddressDesc");
            return (Criteria) this;
        }

        public Criteria andJdAddressDescNotLike(String value) {
            addCriterion("jd_address_desc not like", value, "jdAddressDesc");
            return (Criteria) this;
        }

        public Criteria andJdAddressDescIn(List<String> values) {
            addCriterion("jd_address_desc in", values, "jdAddressDesc");
            return (Criteria) this;
        }

        public Criteria andJdAddressDescNotIn(List<String> values) {
            addCriterion("jd_address_desc not in", values, "jdAddressDesc");
            return (Criteria) this;
        }

        public Criteria andJdAddressDescBetween(String value1, String value2) {
            addCriterion("jd_address_desc between", value1, value2, "jdAddressDesc");
            return (Criteria) this;
        }

        public Criteria andJdAddressDescNotBetween(String value1, String value2) {
            addCriterion("jd_address_desc not between", value1, value2, "jdAddressDesc");
            return (Criteria) this;
        }

        public Criteria andFreightIsNull() {
            addCriterion("freight is null");
            return (Criteria) this;
        }

        public Criteria andFreightIsNotNull() {
            addCriterion("freight is not null");
            return (Criteria) this;
        }

        public Criteria andFreightEqualTo(BigDecimal value) {
            addCriterion("freight =", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightNotEqualTo(BigDecimal value) {
            addCriterion("freight <>", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightGreaterThan(BigDecimal value) {
            addCriterion("freight >", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("freight >=", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightLessThan(BigDecimal value) {
            addCriterion("freight <", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightLessThanOrEqualTo(BigDecimal value) {
            addCriterion("freight <=", value, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightIn(List<BigDecimal> values) {
            addCriterion("freight in", values, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightNotIn(List<BigDecimal> values) {
            addCriterion("freight not in", values, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("freight between", value1, value2, "freight");
            return (Criteria) this;
        }

        public Criteria andFreightNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("freight not between", value1, value2, "freight");
            return (Criteria) this;
        }

        public Criteria andJdTotalPriceIsNull() {
            addCriterion("jd_total_price is null");
            return (Criteria) this;
        }

        public Criteria andJdTotalPriceIsNotNull() {
            addCriterion("jd_total_price is not null");
            return (Criteria) this;
        }

        public Criteria andJdTotalPriceEqualTo(BigDecimal value) {
            addCriterion("jd_total_price =", value, "jdTotalPrice");
            return (Criteria) this;
        }

        public Criteria andJdTotalPriceNotEqualTo(BigDecimal value) {
            addCriterion("jd_total_price <>", value, "jdTotalPrice");
            return (Criteria) this;
        }

        public Criteria andJdTotalPriceGreaterThan(BigDecimal value) {
            addCriterion("jd_total_price >", value, "jdTotalPrice");
            return (Criteria) this;
        }

        public Criteria andJdTotalPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("jd_total_price >=", value, "jdTotalPrice");
            return (Criteria) this;
        }

        public Criteria andJdTotalPriceLessThan(BigDecimal value) {
            addCriterion("jd_total_price <", value, "jdTotalPrice");
            return (Criteria) this;
        }

        public Criteria andJdTotalPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("jd_total_price <=", value, "jdTotalPrice");
            return (Criteria) this;
        }

        public Criteria andJdTotalPriceIn(List<BigDecimal> values) {
            addCriterion("jd_total_price in", values, "jdTotalPrice");
            return (Criteria) this;
        }

        public Criteria andJdTotalPriceNotIn(List<BigDecimal> values) {
            addCriterion("jd_total_price not in", values, "jdTotalPrice");
            return (Criteria) this;
        }

        public Criteria andJdTotalPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("jd_total_price between", value1, value2, "jdTotalPrice");
            return (Criteria) this;
        }

        public Criteria andJdTotalPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("jd_total_price not between", value1, value2, "jdTotalPrice");
            return (Criteria) this;
        }

        public Criteria andJdTotalTaxIsNull() {
            addCriterion("jd_total_tax is null");
            return (Criteria) this;
        }

        public Criteria andJdTotalTaxIsNotNull() {
            addCriterion("jd_total_tax is not null");
            return (Criteria) this;
        }

        public Criteria andJdTotalTaxEqualTo(BigDecimal value) {
            addCriterion("jd_total_tax =", value, "jdTotalTax");
            return (Criteria) this;
        }

        public Criteria andJdTotalTaxNotEqualTo(BigDecimal value) {
            addCriterion("jd_total_tax <>", value, "jdTotalTax");
            return (Criteria) this;
        }

        public Criteria andJdTotalTaxGreaterThan(BigDecimal value) {
            addCriterion("jd_total_tax >", value, "jdTotalTax");
            return (Criteria) this;
        }

        public Criteria andJdTotalTaxGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("jd_total_tax >=", value, "jdTotalTax");
            return (Criteria) this;
        }

        public Criteria andJdTotalTaxLessThan(BigDecimal value) {
            addCriterion("jd_total_tax <", value, "jdTotalTax");
            return (Criteria) this;
        }

        public Criteria andJdTotalTaxLessThanOrEqualTo(BigDecimal value) {
            addCriterion("jd_total_tax <=", value, "jdTotalTax");
            return (Criteria) this;
        }

        public Criteria andJdTotalTaxIn(List<BigDecimal> values) {
            addCriterion("jd_total_tax in", values, "jdTotalTax");
            return (Criteria) this;
        }

        public Criteria andJdTotalTaxNotIn(List<BigDecimal> values) {
            addCriterion("jd_total_tax not in", values, "jdTotalTax");
            return (Criteria) this;
        }

        public Criteria andJdTotalTaxBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("jd_total_tax between", value1, value2, "jdTotalTax");
            return (Criteria) this;
        }

        public Criteria andJdTotalTaxNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("jd_total_tax not between", value1, value2, "jdTotalTax");
            return (Criteria) this;
        }

        public Criteria andJdConfimIsNull() {
            addCriterion("jd_confim is null");
            return (Criteria) this;
        }

        public Criteria andJdConfimIsNotNull() {
            addCriterion("jd_confim is not null");
            return (Criteria) this;
        }

        public Criteria andJdConfimEqualTo(Boolean value) {
            addCriterion("jd_confim =", value, "jdConfim");
            return (Criteria) this;
        }

        public Criteria andJdConfimNotEqualTo(Boolean value) {
            addCriterion("jd_confim <>", value, "jdConfim");
            return (Criteria) this;
        }

        public Criteria andJdConfimGreaterThan(Boolean value) {
            addCriterion("jd_confim >", value, "jdConfim");
            return (Criteria) this;
        }

        public Criteria andJdConfimGreaterThanOrEqualTo(Boolean value) {
            addCriterion("jd_confim >=", value, "jdConfim");
            return (Criteria) this;
        }

        public Criteria andJdConfimLessThan(Boolean value) {
            addCriterion("jd_confim <", value, "jdConfim");
            return (Criteria) this;
        }

        public Criteria andJdConfimLessThanOrEqualTo(Boolean value) {
            addCriterion("jd_confim <=", value, "jdConfim");
            return (Criteria) this;
        }

        public Criteria andJdConfimIn(List<Boolean> values) {
            addCriterion("jd_confim in", values, "jdConfim");
            return (Criteria) this;
        }

        public Criteria andJdConfimNotIn(List<Boolean> values) {
            addCriterion("jd_confim not in", values, "jdConfim");
            return (Criteria) this;
        }

        public Criteria andJdConfimBetween(Boolean value1, Boolean value2) {
            addCriterion("jd_confim between", value1, value2, "jdConfim");
            return (Criteria) this;
        }

        public Criteria andJdConfimNotBetween(Boolean value1, Boolean value2) {
            addCriterion("jd_confim not between", value1, value2, "jdConfim");
            return (Criteria) this;
        }

        public Criteria andConfimTimeIsNull() {
            addCriterion("confim_time is null");
            return (Criteria) this;
        }

        public Criteria andConfimTimeIsNotNull() {
            addCriterion("confim_time is not null");
            return (Criteria) this;
        }

        public Criteria andConfimTimeEqualTo(Integer value) {
            addCriterion("confim_time =", value, "confimTime");
            return (Criteria) this;
        }

        public Criteria andConfimTimeNotEqualTo(Integer value) {
            addCriterion("confim_time <>", value, "confimTime");
            return (Criteria) this;
        }

        public Criteria andConfimTimeGreaterThan(Integer value) {
            addCriterion("confim_time >", value, "confimTime");
            return (Criteria) this;
        }

        public Criteria andConfimTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("confim_time >=", value, "confimTime");
            return (Criteria) this;
        }

        public Criteria andConfimTimeLessThan(Integer value) {
            addCriterion("confim_time <", value, "confimTime");
            return (Criteria) this;
        }

        public Criteria andConfimTimeLessThanOrEqualTo(Integer value) {
            addCriterion("confim_time <=", value, "confimTime");
            return (Criteria) this;
        }

        public Criteria andConfimTimeIn(List<Integer> values) {
            addCriterion("confim_time in", values, "confimTime");
            return (Criteria) this;
        }

        public Criteria andConfimTimeNotIn(List<Integer> values) {
            addCriterion("confim_time not in", values, "confimTime");
            return (Criteria) this;
        }

        public Criteria andConfimTimeBetween(Integer value1, Integer value2) {
            addCriterion("confim_time between", value1, value2, "confimTime");
            return (Criteria) this;
        }

        public Criteria andConfimTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("confim_time not between", value1, value2, "confimTime");
            return (Criteria) this;
        }

        public Criteria andIsSpliteIsNull() {
            addCriterion("is_splite is null");
            return (Criteria) this;
        }

        public Criteria andIsSpliteIsNotNull() {
            addCriterion("is_splite is not null");
            return (Criteria) this;
        }

        public Criteria andIsSpliteEqualTo(Boolean value) {
            addCriterion("is_splite =", value, "isSplite");
            return (Criteria) this;
        }

        public Criteria andIsSpliteNotEqualTo(Boolean value) {
            addCriterion("is_splite <>", value, "isSplite");
            return (Criteria) this;
        }

        public Criteria andIsSpliteGreaterThan(Boolean value) {
            addCriterion("is_splite >", value, "isSplite");
            return (Criteria) this;
        }

        public Criteria andIsSpliteGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_splite >=", value, "isSplite");
            return (Criteria) this;
        }

        public Criteria andIsSpliteLessThan(Boolean value) {
            addCriterion("is_splite <", value, "isSplite");
            return (Criteria) this;
        }

        public Criteria andIsSpliteLessThanOrEqualTo(Boolean value) {
            addCriterion("is_splite <=", value, "isSplite");
            return (Criteria) this;
        }

        public Criteria andIsSpliteIn(List<Boolean> values) {
            addCriterion("is_splite in", values, "isSplite");
            return (Criteria) this;
        }

        public Criteria andIsSpliteNotIn(List<Boolean> values) {
            addCriterion("is_splite not in", values, "isSplite");
            return (Criteria) this;
        }

        public Criteria andIsSpliteBetween(Boolean value1, Boolean value2) {
            addCriterion("is_splite between", value1, value2, "isSplite");
            return (Criteria) this;
        }

        public Criteria andIsSpliteNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_splite not between", value1, value2, "isSplite");
            return (Criteria) this;
        }

        public Criteria andSourceIsNull() {
            addCriterion("source is null");
            return (Criteria) this;
        }

        public Criteria andSourceIsNotNull() {
            addCriterion("source is not null");
            return (Criteria) this;
        }

        public Criteria andSourceEqualTo(Boolean value) {
            addCriterion("source =", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceNotEqualTo(Boolean value) {
            addCriterion("source <>", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceGreaterThan(Boolean value) {
            addCriterion("source >", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceGreaterThanOrEqualTo(Boolean value) {
            addCriterion("source >=", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceLessThan(Boolean value) {
            addCriterion("source <", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceLessThanOrEqualTo(Boolean value) {
            addCriterion("source <=", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceIn(List<Boolean> values) {
            addCriterion("source in", values, "source");
            return (Criteria) this;
        }

        public Criteria andSourceNotIn(List<Boolean> values) {
            addCriterion("source not in", values, "source");
            return (Criteria) this;
        }

        public Criteria andSourceBetween(Boolean value1, Boolean value2) {
            addCriterion("source between", value1, value2, "source");
            return (Criteria) this;
        }

        public Criteria andSourceNotBetween(Boolean value1, Boolean value2) {
            addCriterion("source not between", value1, value2, "source");
            return (Criteria) this;
        }

        public Criteria andThirdNoIsNull() {
            addCriterion("third_no is null");
            return (Criteria) this;
        }

        public Criteria andThirdNoIsNotNull() {
            addCriterion("third_no is not null");
            return (Criteria) this;
        }

        public Criteria andThirdNoEqualTo(String value) {
            addCriterion("third_no =", value, "thirdNo");
            return (Criteria) this;
        }

        public Criteria andThirdNoNotEqualTo(String value) {
            addCriterion("third_no <>", value, "thirdNo");
            return (Criteria) this;
        }

        public Criteria andThirdNoGreaterThan(String value) {
            addCriterion("third_no >", value, "thirdNo");
            return (Criteria) this;
        }

        public Criteria andThirdNoGreaterThanOrEqualTo(String value) {
            addCriterion("third_no >=", value, "thirdNo");
            return (Criteria) this;
        }

        public Criteria andThirdNoLessThan(String value) {
            addCriterion("third_no <", value, "thirdNo");
            return (Criteria) this;
        }

        public Criteria andThirdNoLessThanOrEqualTo(String value) {
            addCriterion("third_no <=", value, "thirdNo");
            return (Criteria) this;
        }

        public Criteria andThirdNoLike(String value) {
            addCriterion("third_no like", value, "thirdNo");
            return (Criteria) this;
        }

        public Criteria andThirdNoNotLike(String value) {
            addCriterion("third_no not like", value, "thirdNo");
            return (Criteria) this;
        }

        public Criteria andThirdNoIn(List<String> values) {
            addCriterion("third_no in", values, "thirdNo");
            return (Criteria) this;
        }

        public Criteria andThirdNoNotIn(List<String> values) {
            addCriterion("third_no not in", values, "thirdNo");
            return (Criteria) this;
        }

        public Criteria andThirdNoBetween(String value1, String value2) {
            addCriterion("third_no between", value1, value2, "thirdNo");
            return (Criteria) this;
        }

        public Criteria andThirdNoNotBetween(String value1, String value2) {
            addCriterion("third_no not between", value1, value2, "thirdNo");
            return (Criteria) this;
        }

        public Criteria andThirdAddressIsNull() {
            addCriterion("third_address is null");
            return (Criteria) this;
        }

        public Criteria andThirdAddressIsNotNull() {
            addCriterion("third_address is not null");
            return (Criteria) this;
        }

        public Criteria andThirdAddressEqualTo(String value) {
            addCriterion("third_address =", value, "thirdAddress");
            return (Criteria) this;
        }

        public Criteria andThirdAddressNotEqualTo(String value) {
            addCriterion("third_address <>", value, "thirdAddress");
            return (Criteria) this;
        }

        public Criteria andThirdAddressGreaterThan(String value) {
            addCriterion("third_address >", value, "thirdAddress");
            return (Criteria) this;
        }

        public Criteria andThirdAddressGreaterThanOrEqualTo(String value) {
            addCriterion("third_address >=", value, "thirdAddress");
            return (Criteria) this;
        }

        public Criteria andThirdAddressLessThan(String value) {
            addCriterion("third_address <", value, "thirdAddress");
            return (Criteria) this;
        }

        public Criteria andThirdAddressLessThanOrEqualTo(String value) {
            addCriterion("third_address <=", value, "thirdAddress");
            return (Criteria) this;
        }

        public Criteria andThirdAddressLike(String value) {
            addCriterion("third_address like", value, "thirdAddress");
            return (Criteria) this;
        }

        public Criteria andThirdAddressNotLike(String value) {
            addCriterion("third_address not like", value, "thirdAddress");
            return (Criteria) this;
        }

        public Criteria andThirdAddressIn(List<String> values) {
            addCriterion("third_address in", values, "thirdAddress");
            return (Criteria) this;
        }

        public Criteria andThirdAddressNotIn(List<String> values) {
            addCriterion("third_address not in", values, "thirdAddress");
            return (Criteria) this;
        }

        public Criteria andThirdAddressBetween(String value1, String value2) {
            addCriterion("third_address between", value1, value2, "thirdAddress");
            return (Criteria) this;
        }

        public Criteria andThirdAddressNotBetween(String value1, String value2) {
            addCriterion("third_address not between", value1, value2, "thirdAddress");
            return (Criteria) this;
        }

        public Criteria andThirdRealPriceIsNull() {
            addCriterion("third_real_price is null");
            return (Criteria) this;
        }

        public Criteria andThirdRealPriceIsNotNull() {
            addCriterion("third_real_price is not null");
            return (Criteria) this;
        }

        public Criteria andThirdRealPriceEqualTo(BigDecimal value) {
            addCriterion("third_real_price =", value, "thirdRealPrice");
            return (Criteria) this;
        }

        public Criteria andThirdRealPriceNotEqualTo(BigDecimal value) {
            addCriterion("third_real_price <>", value, "thirdRealPrice");
            return (Criteria) this;
        }

        public Criteria andThirdRealPriceGreaterThan(BigDecimal value) {
            addCriterion("third_real_price >", value, "thirdRealPrice");
            return (Criteria) this;
        }

        public Criteria andThirdRealPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("third_real_price >=", value, "thirdRealPrice");
            return (Criteria) this;
        }

        public Criteria andThirdRealPriceLessThan(BigDecimal value) {
            addCriterion("third_real_price <", value, "thirdRealPrice");
            return (Criteria) this;
        }

        public Criteria andThirdRealPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("third_real_price <=", value, "thirdRealPrice");
            return (Criteria) this;
        }

        public Criteria andThirdRealPriceIn(List<BigDecimal> values) {
            addCriterion("third_real_price in", values, "thirdRealPrice");
            return (Criteria) this;
        }

        public Criteria andThirdRealPriceNotIn(List<BigDecimal> values) {
            addCriterion("third_real_price not in", values, "thirdRealPrice");
            return (Criteria) this;
        }

        public Criteria andThirdRealPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("third_real_price between", value1, value2, "thirdRealPrice");
            return (Criteria) this;
        }

        public Criteria andThirdRealPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("third_real_price not between", value1, value2, "thirdRealPrice");
            return (Criteria) this;
        }

        public Criteria andThirdExpFeeIsNull() {
            addCriterion("third_exp_fee is null");
            return (Criteria) this;
        }

        public Criteria andThirdExpFeeIsNotNull() {
            addCriterion("third_exp_fee is not null");
            return (Criteria) this;
        }

        public Criteria andThirdExpFeeEqualTo(BigDecimal value) {
            addCriterion("third_exp_fee =", value, "thirdExpFee");
            return (Criteria) this;
        }

        public Criteria andThirdExpFeeNotEqualTo(BigDecimal value) {
            addCriterion("third_exp_fee <>", value, "thirdExpFee");
            return (Criteria) this;
        }

        public Criteria andThirdExpFeeGreaterThan(BigDecimal value) {
            addCriterion("third_exp_fee >", value, "thirdExpFee");
            return (Criteria) this;
        }

        public Criteria andThirdExpFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("third_exp_fee >=", value, "thirdExpFee");
            return (Criteria) this;
        }

        public Criteria andThirdExpFeeLessThan(BigDecimal value) {
            addCriterion("third_exp_fee <", value, "thirdExpFee");
            return (Criteria) this;
        }

        public Criteria andThirdExpFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("third_exp_fee <=", value, "thirdExpFee");
            return (Criteria) this;
        }

        public Criteria andThirdExpFeeIn(List<BigDecimal> values) {
            addCriterion("third_exp_fee in", values, "thirdExpFee");
            return (Criteria) this;
        }

        public Criteria andThirdExpFeeNotIn(List<BigDecimal> values) {
            addCriterion("third_exp_fee not in", values, "thirdExpFee");
            return (Criteria) this;
        }

        public Criteria andThirdExpFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("third_exp_fee between", value1, value2, "thirdExpFee");
            return (Criteria) this;
        }

        public Criteria andThirdExpFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("third_exp_fee not between", value1, value2, "thirdExpFee");
            return (Criteria) this;
        }

        public Criteria andThirdPackageIdIsNull() {
            addCriterion("third_package_id is null");
            return (Criteria) this;
        }

        public Criteria andThirdPackageIdIsNotNull() {
            addCriterion("third_package_id is not null");
            return (Criteria) this;
        }

        public Criteria andThirdPackageIdEqualTo(String value) {
            addCriterion("third_package_id =", value, "thirdPackageId");
            return (Criteria) this;
        }

        public Criteria andThirdPackageIdNotEqualTo(String value) {
            addCriterion("third_package_id <>", value, "thirdPackageId");
            return (Criteria) this;
        }

        public Criteria andThirdPackageIdGreaterThan(String value) {
            addCriterion("third_package_id >", value, "thirdPackageId");
            return (Criteria) this;
        }

        public Criteria andThirdPackageIdGreaterThanOrEqualTo(String value) {
            addCriterion("third_package_id >=", value, "thirdPackageId");
            return (Criteria) this;
        }

        public Criteria andThirdPackageIdLessThan(String value) {
            addCriterion("third_package_id <", value, "thirdPackageId");
            return (Criteria) this;
        }

        public Criteria andThirdPackageIdLessThanOrEqualTo(String value) {
            addCriterion("third_package_id <=", value, "thirdPackageId");
            return (Criteria) this;
        }

        public Criteria andThirdPackageIdLike(String value) {
            addCriterion("third_package_id like", value, "thirdPackageId");
            return (Criteria) this;
        }

        public Criteria andThirdPackageIdNotLike(String value) {
            addCriterion("third_package_id not like", value, "thirdPackageId");
            return (Criteria) this;
        }

        public Criteria andThirdPackageIdIn(List<String> values) {
            addCriterion("third_package_id in", values, "thirdPackageId");
            return (Criteria) this;
        }

        public Criteria andThirdPackageIdNotIn(List<String> values) {
            addCriterion("third_package_id not in", values, "thirdPackageId");
            return (Criteria) this;
        }

        public Criteria andThirdPackageIdBetween(String value1, String value2) {
            addCriterion("third_package_id between", value1, value2, "thirdPackageId");
            return (Criteria) this;
        }

        public Criteria andThirdPackageIdNotBetween(String value1, String value2) {
            addCriterion("third_package_id not between", value1, value2, "thirdPackageId");
            return (Criteria) this;
        }

        public Criteria andThirdApi1IsNull() {
            addCriterion("third_api1 is null");
            return (Criteria) this;
        }

        public Criteria andThirdApi1IsNotNull() {
            addCriterion("third_api1 is not null");
            return (Criteria) this;
        }

        public Criteria andThirdApi1EqualTo(Boolean value) {
            addCriterion("third_api1 =", value, "thirdApi1");
            return (Criteria) this;
        }

        public Criteria andThirdApi1NotEqualTo(Boolean value) {
            addCriterion("third_api1 <>", value, "thirdApi1");
            return (Criteria) this;
        }

        public Criteria andThirdApi1GreaterThan(Boolean value) {
            addCriterion("third_api1 >", value, "thirdApi1");
            return (Criteria) this;
        }

        public Criteria andThirdApi1GreaterThanOrEqualTo(Boolean value) {
            addCriterion("third_api1 >=", value, "thirdApi1");
            return (Criteria) this;
        }

        public Criteria andThirdApi1LessThan(Boolean value) {
            addCriterion("third_api1 <", value, "thirdApi1");
            return (Criteria) this;
        }

        public Criteria andThirdApi1LessThanOrEqualTo(Boolean value) {
            addCriterion("third_api1 <=", value, "thirdApi1");
            return (Criteria) this;
        }

        public Criteria andThirdApi1In(List<Boolean> values) {
            addCriterion("third_api1 in", values, "thirdApi1");
            return (Criteria) this;
        }

        public Criteria andThirdApi1NotIn(List<Boolean> values) {
            addCriterion("third_api1 not in", values, "thirdApi1");
            return (Criteria) this;
        }

        public Criteria andThirdApi1Between(Boolean value1, Boolean value2) {
            addCriterion("third_api1 between", value1, value2, "thirdApi1");
            return (Criteria) this;
        }

        public Criteria andThirdApi1NotBetween(Boolean value1, Boolean value2) {
            addCriterion("third_api1 not between", value1, value2, "thirdApi1");
            return (Criteria) this;
        }

        public Criteria andThirdApi3IsNull() {
            addCriterion("third_api3 is null");
            return (Criteria) this;
        }

        public Criteria andThirdApi3IsNotNull() {
            addCriterion("third_api3 is not null");
            return (Criteria) this;
        }

        public Criteria andThirdApi3EqualTo(Boolean value) {
            addCriterion("third_api3 =", value, "thirdApi3");
            return (Criteria) this;
        }

        public Criteria andThirdApi3NotEqualTo(Boolean value) {
            addCriterion("third_api3 <>", value, "thirdApi3");
            return (Criteria) this;
        }

        public Criteria andThirdApi3GreaterThan(Boolean value) {
            addCriterion("third_api3 >", value, "thirdApi3");
            return (Criteria) this;
        }

        public Criteria andThirdApi3GreaterThanOrEqualTo(Boolean value) {
            addCriterion("third_api3 >=", value, "thirdApi3");
            return (Criteria) this;
        }

        public Criteria andThirdApi3LessThan(Boolean value) {
            addCriterion("third_api3 <", value, "thirdApi3");
            return (Criteria) this;
        }

        public Criteria andThirdApi3LessThanOrEqualTo(Boolean value) {
            addCriterion("third_api3 <=", value, "thirdApi3");
            return (Criteria) this;
        }

        public Criteria andThirdApi3In(List<Boolean> values) {
            addCriterion("third_api3 in", values, "thirdApi3");
            return (Criteria) this;
        }

        public Criteria andThirdApi3NotIn(List<Boolean> values) {
            addCriterion("third_api3 not in", values, "thirdApi3");
            return (Criteria) this;
        }

        public Criteria andThirdApi3Between(Boolean value1, Boolean value2) {
            addCriterion("third_api3 between", value1, value2, "thirdApi3");
            return (Criteria) this;
        }

        public Criteria andThirdApi3NotBetween(Boolean value1, Boolean value2) {
            addCriterion("third_api3 not between", value1, value2, "thirdApi3");
            return (Criteria) this;
        }

        public Criteria andThirdApi4IsNull() {
            addCriterion("third_api_4 is null");
            return (Criteria) this;
        }

        public Criteria andThirdApi4IsNotNull() {
            addCriterion("third_api_4 is not null");
            return (Criteria) this;
        }

        public Criteria andThirdApi4EqualTo(Boolean value) {
            addCriterion("third_api_4 =", value, "thirdApi4");
            return (Criteria) this;
        }

        public Criteria andThirdApi4NotEqualTo(Boolean value) {
            addCriterion("third_api_4 <>", value, "thirdApi4");
            return (Criteria) this;
        }

        public Criteria andThirdApi4GreaterThan(Boolean value) {
            addCriterion("third_api_4 >", value, "thirdApi4");
            return (Criteria) this;
        }

        public Criteria andThirdApi4GreaterThanOrEqualTo(Boolean value) {
            addCriterion("third_api_4 >=", value, "thirdApi4");
            return (Criteria) this;
        }

        public Criteria andThirdApi4LessThan(Boolean value) {
            addCriterion("third_api_4 <", value, "thirdApi4");
            return (Criteria) this;
        }

        public Criteria andThirdApi4LessThanOrEqualTo(Boolean value) {
            addCriterion("third_api_4 <=", value, "thirdApi4");
            return (Criteria) this;
        }

        public Criteria andThirdApi4In(List<Boolean> values) {
            addCriterion("third_api_4 in", values, "thirdApi4");
            return (Criteria) this;
        }

        public Criteria andThirdApi4NotIn(List<Boolean> values) {
            addCriterion("third_api_4 not in", values, "thirdApi4");
            return (Criteria) this;
        }

        public Criteria andThirdApi4Between(Boolean value1, Boolean value2) {
            addCriterion("third_api_4 between", value1, value2, "thirdApi4");
            return (Criteria) this;
        }

        public Criteria andThirdApi4NotBetween(Boolean value1, Boolean value2) {
            addCriterion("third_api_4 not between", value1, value2, "thirdApi4");
            return (Criteria) this;
        }

        public Criteria andThirdUpdateDateIsNull() {
            addCriterion("third_update_date is null");
            return (Criteria) this;
        }

        public Criteria andThirdUpdateDateIsNotNull() {
            addCriterion("third_update_date is not null");
            return (Criteria) this;
        }

        public Criteria andThirdUpdateDateEqualTo(Date value) {
            addCriterion("third_update_date =", value, "thirdUpdateDate");
            return (Criteria) this;
        }

        public Criteria andThirdUpdateDateNotEqualTo(Date value) {
            addCriterion("third_update_date <>", value, "thirdUpdateDate");
            return (Criteria) this;
        }

        public Criteria andThirdUpdateDateGreaterThan(Date value) {
            addCriterion("third_update_date >", value, "thirdUpdateDate");
            return (Criteria) this;
        }

        public Criteria andThirdUpdateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("third_update_date >=", value, "thirdUpdateDate");
            return (Criteria) this;
        }

        public Criteria andThirdUpdateDateLessThan(Date value) {
            addCriterion("third_update_date <", value, "thirdUpdateDate");
            return (Criteria) this;
        }

        public Criteria andThirdUpdateDateLessThanOrEqualTo(Date value) {
            addCriterion("third_update_date <=", value, "thirdUpdateDate");
            return (Criteria) this;
        }

        public Criteria andThirdUpdateDateIn(List<Date> values) {
            addCriterion("third_update_date in", values, "thirdUpdateDate");
            return (Criteria) this;
        }

        public Criteria andThirdUpdateDateNotIn(List<Date> values) {
            addCriterion("third_update_date not in", values, "thirdUpdateDate");
            return (Criteria) this;
        }

        public Criteria andThirdUpdateDateBetween(Date value1, Date value2) {
            addCriterion("third_update_date between", value1, value2, "thirdUpdateDate");
            return (Criteria) this;
        }

        public Criteria andThirdUpdateDateNotBetween(Date value1, Date value2) {
            addCriterion("third_update_date not between", value1, value2, "thirdUpdateDate");
            return (Criteria) this;
        }

        public Criteria andExpCreateTimeIsNull() {
            addCriterion("exp_create_time is null");
            return (Criteria) this;
        }

        public Criteria andExpCreateTimeIsNotNull() {
            addCriterion("exp_create_time is not null");
            return (Criteria) this;
        }

        public Criteria andExpCreateTimeEqualTo(String value) {
            addCriterion("exp_create_time =", value, "expCreateTime");
            return (Criteria) this;
        }

        public Criteria andExpCreateTimeNotEqualTo(String value) {
            addCriterion("exp_create_time <>", value, "expCreateTime");
            return (Criteria) this;
        }

        public Criteria andExpCreateTimeGreaterThan(String value) {
            addCriterion("exp_create_time >", value, "expCreateTime");
            return (Criteria) this;
        }

        public Criteria andExpCreateTimeGreaterThanOrEqualTo(String value) {
            addCriterion("exp_create_time >=", value, "expCreateTime");
            return (Criteria) this;
        }

        public Criteria andExpCreateTimeLessThan(String value) {
            addCriterion("exp_create_time <", value, "expCreateTime");
            return (Criteria) this;
        }

        public Criteria andExpCreateTimeLessThanOrEqualTo(String value) {
            addCriterion("exp_create_time <=", value, "expCreateTime");
            return (Criteria) this;
        }

        public Criteria andExpCreateTimeLike(String value) {
            addCriterion("exp_create_time like", value, "expCreateTime");
            return (Criteria) this;
        }

        public Criteria andExpCreateTimeNotLike(String value) {
            addCriterion("exp_create_time not like", value, "expCreateTime");
            return (Criteria) this;
        }

        public Criteria andExpCreateTimeIn(List<String> values) {
            addCriterion("exp_create_time in", values, "expCreateTime");
            return (Criteria) this;
        }

        public Criteria andExpCreateTimeNotIn(List<String> values) {
            addCriterion("exp_create_time not in", values, "expCreateTime");
            return (Criteria) this;
        }

        public Criteria andExpCreateTimeBetween(String value1, String value2) {
            addCriterion("exp_create_time between", value1, value2, "expCreateTime");
            return (Criteria) this;
        }

        public Criteria andExpCreateTimeNotBetween(String value1, String value2) {
            addCriterion("exp_create_time not between", value1, value2, "expCreateTime");
            return (Criteria) this;
        }

        public Criteria andCreditrefundIsNull() {
            addCriterion("creditrefund is null");
            return (Criteria) this;
        }

        public Criteria andCreditrefundIsNotNull() {
            addCriterion("creditrefund is not null");
            return (Criteria) this;
        }

        public Criteria andCreditrefundEqualTo(BigDecimal value) {
            addCriterion("creditrefund =", value, "creditrefund");
            return (Criteria) this;
        }

        public Criteria andCreditrefundNotEqualTo(BigDecimal value) {
            addCriterion("creditrefund <>", value, "creditrefund");
            return (Criteria) this;
        }

        public Criteria andCreditrefundGreaterThan(BigDecimal value) {
            addCriterion("creditrefund >", value, "creditrefund");
            return (Criteria) this;
        }

        public Criteria andCreditrefundGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("creditrefund >=", value, "creditrefund");
            return (Criteria) this;
        }

        public Criteria andCreditrefundLessThan(BigDecimal value) {
            addCriterion("creditrefund <", value, "creditrefund");
            return (Criteria) this;
        }

        public Criteria andCreditrefundLessThanOrEqualTo(BigDecimal value) {
            addCriterion("creditrefund <=", value, "creditrefund");
            return (Criteria) this;
        }

        public Criteria andCreditrefundIn(List<BigDecimal> values) {
            addCriterion("creditrefund in", values, "creditrefund");
            return (Criteria) this;
        }

        public Criteria andCreditrefundNotIn(List<BigDecimal> values) {
            addCriterion("creditrefund not in", values, "creditrefund");
            return (Criteria) this;
        }

        public Criteria andCreditrefundBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("creditrefund between", value1, value2, "creditrefund");
            return (Criteria) this;
        }

        public Criteria andCreditrefundNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("creditrefund not between", value1, value2, "creditrefund");
            return (Criteria) this;
        }

        public Criteria andCardrefundIsNull() {
            addCriterion("cardrefund is null");
            return (Criteria) this;
        }

        public Criteria andCardrefundIsNotNull() {
            addCriterion("cardrefund is not null");
            return (Criteria) this;
        }

        public Criteria andCardrefundEqualTo(BigDecimal value) {
            addCriterion("cardrefund =", value, "cardrefund");
            return (Criteria) this;
        }

        public Criteria andCardrefundNotEqualTo(BigDecimal value) {
            addCriterion("cardrefund <>", value, "cardrefund");
            return (Criteria) this;
        }

        public Criteria andCardrefundGreaterThan(BigDecimal value) {
            addCriterion("cardrefund >", value, "cardrefund");
            return (Criteria) this;
        }

        public Criteria andCardrefundGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("cardrefund >=", value, "cardrefund");
            return (Criteria) this;
        }

        public Criteria andCardrefundLessThan(BigDecimal value) {
            addCriterion("cardrefund <", value, "cardrefund");
            return (Criteria) this;
        }

        public Criteria andCardrefundLessThanOrEqualTo(BigDecimal value) {
            addCriterion("cardrefund <=", value, "cardrefund");
            return (Criteria) this;
        }

        public Criteria andCardrefundIn(List<BigDecimal> values) {
            addCriterion("cardrefund in", values, "cardrefund");
            return (Criteria) this;
        }

        public Criteria andCardrefundNotIn(List<BigDecimal> values) {
            addCriterion("cardrefund not in", values, "cardrefund");
            return (Criteria) this;
        }

        public Criteria andCardrefundBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cardrefund between", value1, value2, "cardrefund");
            return (Criteria) this;
        }

        public Criteria andCardrefundNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cardrefund not between", value1, value2, "cardrefund");
            return (Criteria) this;
        }

        public Criteria andOnlinerefundIsNull() {
            addCriterion("onlinerefund is null");
            return (Criteria) this;
        }

        public Criteria andOnlinerefundIsNotNull() {
            addCriterion("onlinerefund is not null");
            return (Criteria) this;
        }

        public Criteria andOnlinerefundEqualTo(BigDecimal value) {
            addCriterion("onlinerefund =", value, "onlinerefund");
            return (Criteria) this;
        }

        public Criteria andOnlinerefundNotEqualTo(BigDecimal value) {
            addCriterion("onlinerefund <>", value, "onlinerefund");
            return (Criteria) this;
        }

        public Criteria andOnlinerefundGreaterThan(BigDecimal value) {
            addCriterion("onlinerefund >", value, "onlinerefund");
            return (Criteria) this;
        }

        public Criteria andOnlinerefundGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("onlinerefund >=", value, "onlinerefund");
            return (Criteria) this;
        }

        public Criteria andOnlinerefundLessThan(BigDecimal value) {
            addCriterion("onlinerefund <", value, "onlinerefund");
            return (Criteria) this;
        }

        public Criteria andOnlinerefundLessThanOrEqualTo(BigDecimal value) {
            addCriterion("onlinerefund <=", value, "onlinerefund");
            return (Criteria) this;
        }

        public Criteria andOnlinerefundIn(List<BigDecimal> values) {
            addCriterion("onlinerefund in", values, "onlinerefund");
            return (Criteria) this;
        }

        public Criteria andOnlinerefundNotIn(List<BigDecimal> values) {
            addCriterion("onlinerefund not in", values, "onlinerefund");
            return (Criteria) this;
        }

        public Criteria andOnlinerefundBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("onlinerefund between", value1, value2, "onlinerefund");
            return (Criteria) this;
        }

        public Criteria andOnlinerefundNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("onlinerefund not between", value1, value2, "onlinerefund");
            return (Criteria) this;
        }

        public Criteria andWechatrefundIsNull() {
            addCriterion("wechatrefund is null");
            return (Criteria) this;
        }

        public Criteria andWechatrefundIsNotNull() {
            addCriterion("wechatrefund is not null");
            return (Criteria) this;
        }

        public Criteria andWechatrefundEqualTo(BigDecimal value) {
            addCriterion("wechatrefund =", value, "wechatrefund");
            return (Criteria) this;
        }

        public Criteria andWechatrefundNotEqualTo(BigDecimal value) {
            addCriterion("wechatrefund <>", value, "wechatrefund");
            return (Criteria) this;
        }

        public Criteria andWechatrefundGreaterThan(BigDecimal value) {
            addCriterion("wechatrefund >", value, "wechatrefund");
            return (Criteria) this;
        }

        public Criteria andWechatrefundGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("wechatrefund >=", value, "wechatrefund");
            return (Criteria) this;
        }

        public Criteria andWechatrefundLessThan(BigDecimal value) {
            addCriterion("wechatrefund <", value, "wechatrefund");
            return (Criteria) this;
        }

        public Criteria andWechatrefundLessThanOrEqualTo(BigDecimal value) {
            addCriterion("wechatrefund <=", value, "wechatrefund");
            return (Criteria) this;
        }

        public Criteria andWechatrefundIn(List<BigDecimal> values) {
            addCriterion("wechatrefund in", values, "wechatrefund");
            return (Criteria) this;
        }

        public Criteria andWechatrefundNotIn(List<BigDecimal> values) {
            addCriterion("wechatrefund not in", values, "wechatrefund");
            return (Criteria) this;
        }

        public Criteria andWechatrefundBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("wechatrefund between", value1, value2, "wechatrefund");
            return (Criteria) this;
        }

        public Criteria andWechatrefundNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("wechatrefund not between", value1, value2, "wechatrefund");
            return (Criteria) this;
        }

        public Criteria andAlipayrefundIsNull() {
            addCriterion("alipayrefund is null");
            return (Criteria) this;
        }

        public Criteria andAlipayrefundIsNotNull() {
            addCriterion("alipayrefund is not null");
            return (Criteria) this;
        }

        public Criteria andAlipayrefundEqualTo(BigDecimal value) {
            addCriterion("alipayrefund =", value, "alipayrefund");
            return (Criteria) this;
        }

        public Criteria andAlipayrefundNotEqualTo(BigDecimal value) {
            addCriterion("alipayrefund <>", value, "alipayrefund");
            return (Criteria) this;
        }

        public Criteria andAlipayrefundGreaterThan(BigDecimal value) {
            addCriterion("alipayrefund >", value, "alipayrefund");
            return (Criteria) this;
        }

        public Criteria andAlipayrefundGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("alipayrefund >=", value, "alipayrefund");
            return (Criteria) this;
        }

        public Criteria andAlipayrefundLessThan(BigDecimal value) {
            addCriterion("alipayrefund <", value, "alipayrefund");
            return (Criteria) this;
        }

        public Criteria andAlipayrefundLessThanOrEqualTo(BigDecimal value) {
            addCriterion("alipayrefund <=", value, "alipayrefund");
            return (Criteria) this;
        }

        public Criteria andAlipayrefundIn(List<BigDecimal> values) {
            addCriterion("alipayrefund in", values, "alipayrefund");
            return (Criteria) this;
        }

        public Criteria andAlipayrefundNotIn(List<BigDecimal> values) {
            addCriterion("alipayrefund not in", values, "alipayrefund");
            return (Criteria) this;
        }

        public Criteria andAlipayrefundBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("alipayrefund between", value1, value2, "alipayrefund");
            return (Criteria) this;
        }

        public Criteria andAlipayrefundNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("alipayrefund not between", value1, value2, "alipayrefund");
            return (Criteria) this;
        }

        public Criteria andIsrefundIsNull() {
            addCriterion("isrefund is null");
            return (Criteria) this;
        }

        public Criteria andIsrefundIsNotNull() {
            addCriterion("isrefund is not null");
            return (Criteria) this;
        }

        public Criteria andIsrefundEqualTo(Integer value) {
            addCriterion("isrefund =", value, "isrefund");
            return (Criteria) this;
        }

        public Criteria andIsrefundNotEqualTo(Integer value) {
            addCriterion("isrefund <>", value, "isrefund");
            return (Criteria) this;
        }

        public Criteria andIsrefundGreaterThan(Integer value) {
            addCriterion("isrefund >", value, "isrefund");
            return (Criteria) this;
        }

        public Criteria andIsrefundGreaterThanOrEqualTo(Integer value) {
            addCriterion("isrefund >=", value, "isrefund");
            return (Criteria) this;
        }

        public Criteria andIsrefundLessThan(Integer value) {
            addCriterion("isrefund <", value, "isrefund");
            return (Criteria) this;
        }

        public Criteria andIsrefundLessThanOrEqualTo(Integer value) {
            addCriterion("isrefund <=", value, "isrefund");
            return (Criteria) this;
        }

        public Criteria andIsrefundIn(List<Integer> values) {
            addCriterion("isrefund in", values, "isrefund");
            return (Criteria) this;
        }

        public Criteria andIsrefundNotIn(List<Integer> values) {
            addCriterion("isrefund not in", values, "isrefund");
            return (Criteria) this;
        }

        public Criteria andIsrefundBetween(Integer value1, Integer value2) {
            addCriterion("isrefund between", value1, value2, "isrefund");
            return (Criteria) this;
        }

        public Criteria andIsrefundNotBetween(Integer value1, Integer value2) {
            addCriterion("isrefund not between", value1, value2, "isrefund");
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