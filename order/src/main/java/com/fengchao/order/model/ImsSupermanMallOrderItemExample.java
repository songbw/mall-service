package com.fengchao.order.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ImsSupermanMallOrderItemExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ImsSupermanMallOrderItemExample() {
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

        public Criteria andOrderidIsNull() {
            addCriterion("orderid is null");
            return (Criteria) this;
        }

        public Criteria andOrderidIsNotNull() {
            addCriterion("orderid is not null");
            return (Criteria) this;
        }

        public Criteria andOrderidEqualTo(Integer value) {
            addCriterion("orderid =", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotEqualTo(Integer value) {
            addCriterion("orderid <>", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidGreaterThan(Integer value) {
            addCriterion("orderid >", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidGreaterThanOrEqualTo(Integer value) {
            addCriterion("orderid >=", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidLessThan(Integer value) {
            addCriterion("orderid <", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidLessThanOrEqualTo(Integer value) {
            addCriterion("orderid <=", value, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidIn(List<Integer> values) {
            addCriterion("orderid in", values, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotIn(List<Integer> values) {
            addCriterion("orderid not in", values, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidBetween(Integer value1, Integer value2) {
            addCriterion("orderid between", value1, value2, "orderid");
            return (Criteria) this;
        }

        public Criteria andOrderidNotBetween(Integer value1, Integer value2) {
            addCriterion("orderid not between", value1, value2, "orderid");
            return (Criteria) this;
        }

        public Criteria andItemidIsNull() {
            addCriterion("itemid is null");
            return (Criteria) this;
        }

        public Criteria andItemidIsNotNull() {
            addCriterion("itemid is not null");
            return (Criteria) this;
        }

        public Criteria andItemidEqualTo(Integer value) {
            addCriterion("itemid =", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidNotEqualTo(Integer value) {
            addCriterion("itemid <>", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidGreaterThan(Integer value) {
            addCriterion("itemid >", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidGreaterThanOrEqualTo(Integer value) {
            addCriterion("itemid >=", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidLessThan(Integer value) {
            addCriterion("itemid <", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidLessThanOrEqualTo(Integer value) {
            addCriterion("itemid <=", value, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidIn(List<Integer> values) {
            addCriterion("itemid in", values, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidNotIn(List<Integer> values) {
            addCriterion("itemid not in", values, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidBetween(Integer value1, Integer value2) {
            addCriterion("itemid between", value1, value2, "itemid");
            return (Criteria) this;
        }

        public Criteria andItemidNotBetween(Integer value1, Integer value2) {
            addCriterion("itemid not between", value1, value2, "itemid");
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

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andNumberIsNull() {
            addCriterion("number is null");
            return (Criteria) this;
        }

        public Criteria andNumberIsNotNull() {
            addCriterion("number is not null");
            return (Criteria) this;
        }

        public Criteria andNumberEqualTo(String value) {
            addCriterion("number =", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberNotEqualTo(String value) {
            addCriterion("number <>", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberGreaterThan(String value) {
            addCriterion("number >", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberGreaterThanOrEqualTo(String value) {
            addCriterion("number >=", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberLessThan(String value) {
            addCriterion("number <", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberLessThanOrEqualTo(String value) {
            addCriterion("number <=", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberLike(String value) {
            addCriterion("number like", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberNotLike(String value) {
            addCriterion("number not like", value, "number");
            return (Criteria) this;
        }

        public Criteria andNumberIn(List<String> values) {
            addCriterion("number in", values, "number");
            return (Criteria) this;
        }

        public Criteria andNumberNotIn(List<String> values) {
            addCriterion("number not in", values, "number");
            return (Criteria) this;
        }

        public Criteria andNumberBetween(String value1, String value2) {
            addCriterion("number between", value1, value2, "number");
            return (Criteria) this;
        }

        public Criteria andNumberNotBetween(String value1, String value2) {
            addCriterion("number not between", value1, value2, "number");
            return (Criteria) this;
        }

        public Criteria andCoverIsNull() {
            addCriterion("cover is null");
            return (Criteria) this;
        }

        public Criteria andCoverIsNotNull() {
            addCriterion("cover is not null");
            return (Criteria) this;
        }

        public Criteria andCoverEqualTo(String value) {
            addCriterion("cover =", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverNotEqualTo(String value) {
            addCriterion("cover <>", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverGreaterThan(String value) {
            addCriterion("cover >", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverGreaterThanOrEqualTo(String value) {
            addCriterion("cover >=", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverLessThan(String value) {
            addCriterion("cover <", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverLessThanOrEqualTo(String value) {
            addCriterion("cover <=", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverLike(String value) {
            addCriterion("cover like", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverNotLike(String value) {
            addCriterion("cover not like", value, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverIn(List<String> values) {
            addCriterion("cover in", values, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverNotIn(List<String> values) {
            addCriterion("cover not in", values, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverBetween(String value1, String value2) {
            addCriterion("cover between", value1, value2, "cover");
            return (Criteria) this;
        }

        public Criteria andCoverNotBetween(String value1, String value2) {
            addCriterion("cover not between", value1, value2, "cover");
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

        public Criteria andSpecialIsNull() {
            addCriterion("special is null");
            return (Criteria) this;
        }

        public Criteria andSpecialIsNotNull() {
            addCriterion("special is not null");
            return (Criteria) this;
        }

        public Criteria andSpecialEqualTo(Byte value) {
            addCriterion("special =", value, "special");
            return (Criteria) this;
        }

        public Criteria andSpecialNotEqualTo(Byte value) {
            addCriterion("special <>", value, "special");
            return (Criteria) this;
        }

        public Criteria andSpecialGreaterThan(Byte value) {
            addCriterion("special >", value, "special");
            return (Criteria) this;
        }

        public Criteria andSpecialGreaterThanOrEqualTo(Byte value) {
            addCriterion("special >=", value, "special");
            return (Criteria) this;
        }

        public Criteria andSpecialLessThan(Byte value) {
            addCriterion("special <", value, "special");
            return (Criteria) this;
        }

        public Criteria andSpecialLessThanOrEqualTo(Byte value) {
            addCriterion("special <=", value, "special");
            return (Criteria) this;
        }

        public Criteria andSpecialIn(List<Byte> values) {
            addCriterion("special in", values, "special");
            return (Criteria) this;
        }

        public Criteria andSpecialNotIn(List<Byte> values) {
            addCriterion("special not in", values, "special");
            return (Criteria) this;
        }

        public Criteria andSpecialBetween(Byte value1, Byte value2) {
            addCriterion("special between", value1, value2, "special");
            return (Criteria) this;
        }

        public Criteria andSpecialNotBetween(Byte value1, Byte value2) {
            addCriterion("special not between", value1, value2, "special");
            return (Criteria) this;
        }

        public Criteria andSkuidIsNull() {
            addCriterion("skuid is null");
            return (Criteria) this;
        }

        public Criteria andSkuidIsNotNull() {
            addCriterion("skuid is not null");
            return (Criteria) this;
        }

        public Criteria andSkuidEqualTo(Integer value) {
            addCriterion("skuid =", value, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidNotEqualTo(Integer value) {
            addCriterion("skuid <>", value, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidGreaterThan(Integer value) {
            addCriterion("skuid >", value, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidGreaterThanOrEqualTo(Integer value) {
            addCriterion("skuid >=", value, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidLessThan(Integer value) {
            addCriterion("skuid <", value, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidLessThanOrEqualTo(Integer value) {
            addCriterion("skuid <=", value, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidIn(List<Integer> values) {
            addCriterion("skuid in", values, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidNotIn(List<Integer> values) {
            addCriterion("skuid not in", values, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidBetween(Integer value1, Integer value2) {
            addCriterion("skuid between", value1, value2, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidNotBetween(Integer value1, Integer value2) {
            addCriterion("skuid not between", value1, value2, "skuid");
            return (Criteria) this;
        }

        public Criteria andUnitIsNull() {
            addCriterion("unit is null");
            return (Criteria) this;
        }

        public Criteria andUnitIsNotNull() {
            addCriterion("unit is not null");
            return (Criteria) this;
        }

        public Criteria andUnitEqualTo(String value) {
            addCriterion("unit =", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotEqualTo(String value) {
            addCriterion("unit <>", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitGreaterThan(String value) {
            addCriterion("unit >", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitGreaterThanOrEqualTo(String value) {
            addCriterion("unit >=", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitLessThan(String value) {
            addCriterion("unit <", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitLessThanOrEqualTo(String value) {
            addCriterion("unit <=", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitLike(String value) {
            addCriterion("unit like", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotLike(String value) {
            addCriterion("unit not like", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitIn(List<String> values) {
            addCriterion("unit in", values, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotIn(List<String> values) {
            addCriterion("unit not in", values, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitBetween(String value1, String value2) {
            addCriterion("unit between", value1, value2, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotBetween(String value1, String value2) {
            addCriterion("unit not between", value1, value2, "unit");
            return (Criteria) this;
        }

        public Criteria andIscommentIsNull() {
            addCriterion("iscomment is null");
            return (Criteria) this;
        }

        public Criteria andIscommentIsNotNull() {
            addCriterion("iscomment is not null");
            return (Criteria) this;
        }

        public Criteria andIscommentEqualTo(Byte value) {
            addCriterion("iscomment =", value, "iscomment");
            return (Criteria) this;
        }

        public Criteria andIscommentNotEqualTo(Byte value) {
            addCriterion("iscomment <>", value, "iscomment");
            return (Criteria) this;
        }

        public Criteria andIscommentGreaterThan(Byte value) {
            addCriterion("iscomment >", value, "iscomment");
            return (Criteria) this;
        }

        public Criteria andIscommentGreaterThanOrEqualTo(Byte value) {
            addCriterion("iscomment >=", value, "iscomment");
            return (Criteria) this;
        }

        public Criteria andIscommentLessThan(Byte value) {
            addCriterion("iscomment <", value, "iscomment");
            return (Criteria) this;
        }

        public Criteria andIscommentLessThanOrEqualTo(Byte value) {
            addCriterion("iscomment <=", value, "iscomment");
            return (Criteria) this;
        }

        public Criteria andIscommentIn(List<Byte> values) {
            addCriterion("iscomment in", values, "iscomment");
            return (Criteria) this;
        }

        public Criteria andIscommentNotIn(List<Byte> values) {
            addCriterion("iscomment not in", values, "iscomment");
            return (Criteria) this;
        }

        public Criteria andIscommentBetween(Byte value1, Byte value2) {
            addCriterion("iscomment between", value1, value2, "iscomment");
            return (Criteria) this;
        }

        public Criteria andIscommentNotBetween(Byte value1, Byte value2) {
            addCriterion("iscomment not between", value1, value2, "iscomment");
            return (Criteria) this;
        }

        public Criteria andBarcodeIsNull() {
            addCriterion("barcode is null");
            return (Criteria) this;
        }

        public Criteria andBarcodeIsNotNull() {
            addCriterion("barcode is not null");
            return (Criteria) this;
        }

        public Criteria andBarcodeEqualTo(String value) {
            addCriterion("barcode =", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotEqualTo(String value) {
            addCriterion("barcode <>", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeGreaterThan(String value) {
            addCriterion("barcode >", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeGreaterThanOrEqualTo(String value) {
            addCriterion("barcode >=", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeLessThan(String value) {
            addCriterion("barcode <", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeLessThanOrEqualTo(String value) {
            addCriterion("barcode <=", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeLike(String value) {
            addCriterion("barcode like", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotLike(String value) {
            addCriterion("barcode not like", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeIn(List<String> values) {
            addCriterion("barcode in", values, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotIn(List<String> values) {
            addCriterion("barcode not in", values, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeBetween(String value1, String value2) {
            addCriterion("barcode between", value1, value2, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotBetween(String value1, String value2) {
            addCriterion("barcode not between", value1, value2, "barcode");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIsNull() {
            addCriterion("service_type is null");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIsNotNull() {
            addCriterion("service_type is not null");
            return (Criteria) this;
        }

        public Criteria andServiceTypeEqualTo(Byte value) {
            addCriterion("service_type =", value, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeNotEqualTo(Byte value) {
            addCriterion("service_type <>", value, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeGreaterThan(Byte value) {
            addCriterion("service_type >", value, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("service_type >=", value, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeLessThan(Byte value) {
            addCriterion("service_type <", value, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeLessThanOrEqualTo(Byte value) {
            addCriterion("service_type <=", value, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIn(List<Byte> values) {
            addCriterion("service_type in", values, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeNotIn(List<Byte> values) {
            addCriterion("service_type not in", values, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeBetween(Byte value1, Byte value2) {
            addCriterion("service_type between", value1, value2, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("service_type not between", value1, value2, "serviceType");
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

        public Criteria andTaxIsNull() {
            addCriterion("tax is null");
            return (Criteria) this;
        }

        public Criteria andTaxIsNotNull() {
            addCriterion("tax is not null");
            return (Criteria) this;
        }

        public Criteria andTaxEqualTo(String value) {
            addCriterion("tax =", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxNotEqualTo(String value) {
            addCriterion("tax <>", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxGreaterThan(String value) {
            addCriterion("tax >", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxGreaterThanOrEqualTo(String value) {
            addCriterion("tax >=", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxLessThan(String value) {
            addCriterion("tax <", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxLessThanOrEqualTo(String value) {
            addCriterion("tax <=", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxLike(String value) {
            addCriterion("tax like", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxNotLike(String value) {
            addCriterion("tax not like", value, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxIn(List<String> values) {
            addCriterion("tax in", values, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxNotIn(List<String> values) {
            addCriterion("tax not in", values, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxBetween(String value1, String value2) {
            addCriterion("tax between", value1, value2, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxNotBetween(String value1, String value2) {
            addCriterion("tax not between", value1, value2, "tax");
            return (Criteria) this;
        }

        public Criteria andTaxPriceIsNull() {
            addCriterion("tax_price is null");
            return (Criteria) this;
        }

        public Criteria andTaxPriceIsNotNull() {
            addCriterion("tax_price is not null");
            return (Criteria) this;
        }

        public Criteria andTaxPriceEqualTo(BigDecimal value) {
            addCriterion("tax_price =", value, "taxPrice");
            return (Criteria) this;
        }

        public Criteria andTaxPriceNotEqualTo(BigDecimal value) {
            addCriterion("tax_price <>", value, "taxPrice");
            return (Criteria) this;
        }

        public Criteria andTaxPriceGreaterThan(BigDecimal value) {
            addCriterion("tax_price >", value, "taxPrice");
            return (Criteria) this;
        }

        public Criteria andTaxPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("tax_price >=", value, "taxPrice");
            return (Criteria) this;
        }

        public Criteria andTaxPriceLessThan(BigDecimal value) {
            addCriterion("tax_price <", value, "taxPrice");
            return (Criteria) this;
        }

        public Criteria andTaxPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("tax_price <=", value, "taxPrice");
            return (Criteria) this;
        }

        public Criteria andTaxPriceIn(List<BigDecimal> values) {
            addCriterion("tax_price in", values, "taxPrice");
            return (Criteria) this;
        }

        public Criteria andTaxPriceNotIn(List<BigDecimal> values) {
            addCriterion("tax_price not in", values, "taxPrice");
            return (Criteria) this;
        }

        public Criteria andTaxPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tax_price between", value1, value2, "taxPrice");
            return (Criteria) this;
        }

        public Criteria andTaxPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("tax_price not between", value1, value2, "taxPrice");
            return (Criteria) this;
        }

        public Criteria andNakedPriceIsNull() {
            addCriterion("naked_price is null");
            return (Criteria) this;
        }

        public Criteria andNakedPriceIsNotNull() {
            addCriterion("naked_price is not null");
            return (Criteria) this;
        }

        public Criteria andNakedPriceEqualTo(BigDecimal value) {
            addCriterion("naked_price =", value, "nakedPrice");
            return (Criteria) this;
        }

        public Criteria andNakedPriceNotEqualTo(BigDecimal value) {
            addCriterion("naked_price <>", value, "nakedPrice");
            return (Criteria) this;
        }

        public Criteria andNakedPriceGreaterThan(BigDecimal value) {
            addCriterion("naked_price >", value, "nakedPrice");
            return (Criteria) this;
        }

        public Criteria andNakedPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("naked_price >=", value, "nakedPrice");
            return (Criteria) this;
        }

        public Criteria andNakedPriceLessThan(BigDecimal value) {
            addCriterion("naked_price <", value, "nakedPrice");
            return (Criteria) this;
        }

        public Criteria andNakedPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("naked_price <=", value, "nakedPrice");
            return (Criteria) this;
        }

        public Criteria andNakedPriceIn(List<BigDecimal> values) {
            addCriterion("naked_price in", values, "nakedPrice");
            return (Criteria) this;
        }

        public Criteria andNakedPriceNotIn(List<BigDecimal> values) {
            addCriterion("naked_price not in", values, "nakedPrice");
            return (Criteria) this;
        }

        public Criteria andNakedPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("naked_price between", value1, value2, "nakedPrice");
            return (Criteria) this;
        }

        public Criteria andNakedPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("naked_price not between", value1, value2, "nakedPrice");
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

        public Criteria andCJdOrderIdIsNull() {
            addCriterion("c_jd_order_id is null");
            return (Criteria) this;
        }

        public Criteria andCJdOrderIdIsNotNull() {
            addCriterion("c_jd_order_id is not null");
            return (Criteria) this;
        }

        public Criteria andCJdOrderIdEqualTo(String value) {
            addCriterion("c_jd_order_id =", value, "cJdOrderId");
            return (Criteria) this;
        }

        public Criteria andCJdOrderIdNotEqualTo(String value) {
            addCriterion("c_jd_order_id <>", value, "cJdOrderId");
            return (Criteria) this;
        }

        public Criteria andCJdOrderIdGreaterThan(String value) {
            addCriterion("c_jd_order_id >", value, "cJdOrderId");
            return (Criteria) this;
        }

        public Criteria andCJdOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("c_jd_order_id >=", value, "cJdOrderId");
            return (Criteria) this;
        }

        public Criteria andCJdOrderIdLessThan(String value) {
            addCriterion("c_jd_order_id <", value, "cJdOrderId");
            return (Criteria) this;
        }

        public Criteria andCJdOrderIdLessThanOrEqualTo(String value) {
            addCriterion("c_jd_order_id <=", value, "cJdOrderId");
            return (Criteria) this;
        }

        public Criteria andCJdOrderIdLike(String value) {
            addCriterion("c_jd_order_id like", value, "cJdOrderId");
            return (Criteria) this;
        }

        public Criteria andCJdOrderIdNotLike(String value) {
            addCriterion("c_jd_order_id not like", value, "cJdOrderId");
            return (Criteria) this;
        }

        public Criteria andCJdOrderIdIn(List<String> values) {
            addCriterion("c_jd_order_id in", values, "cJdOrderId");
            return (Criteria) this;
        }

        public Criteria andCJdOrderIdNotIn(List<String> values) {
            addCriterion("c_jd_order_id not in", values, "cJdOrderId");
            return (Criteria) this;
        }

        public Criteria andCJdOrderIdBetween(String value1, String value2) {
            addCriterion("c_jd_order_id between", value1, value2, "cJdOrderId");
            return (Criteria) this;
        }

        public Criteria andCJdOrderIdNotBetween(String value1, String value2) {
            addCriterion("c_jd_order_id not between", value1, value2, "cJdOrderId");
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

        public Criteria andIsConfimIsNull() {
            addCriterion("is_confim is null");
            return (Criteria) this;
        }

        public Criteria andIsConfimIsNotNull() {
            addCriterion("is_confim is not null");
            return (Criteria) this;
        }

        public Criteria andIsConfimEqualTo(Boolean value) {
            addCriterion("is_confim =", value, "isConfim");
            return (Criteria) this;
        }

        public Criteria andIsConfimNotEqualTo(Boolean value) {
            addCriterion("is_confim <>", value, "isConfim");
            return (Criteria) this;
        }

        public Criteria andIsConfimGreaterThan(Boolean value) {
            addCriterion("is_confim >", value, "isConfim");
            return (Criteria) this;
        }

        public Criteria andIsConfimGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_confim >=", value, "isConfim");
            return (Criteria) this;
        }

        public Criteria andIsConfimLessThan(Boolean value) {
            addCriterion("is_confim <", value, "isConfim");
            return (Criteria) this;
        }

        public Criteria andIsConfimLessThanOrEqualTo(Boolean value) {
            addCriterion("is_confim <=", value, "isConfim");
            return (Criteria) this;
        }

        public Criteria andIsConfimIn(List<Boolean> values) {
            addCriterion("is_confim in", values, "isConfim");
            return (Criteria) this;
        }

        public Criteria andIsConfimNotIn(List<Boolean> values) {
            addCriterion("is_confim not in", values, "isConfim");
            return (Criteria) this;
        }

        public Criteria andIsConfimBetween(Boolean value1, Boolean value2) {
            addCriterion("is_confim between", value1, value2, "isConfim");
            return (Criteria) this;
        }

        public Criteria andIsConfimNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_confim not between", value1, value2, "isConfim");
            return (Criteria) this;
        }

        public Criteria andFinishTimeIsNull() {
            addCriterion("finish_time is null");
            return (Criteria) this;
        }

        public Criteria andFinishTimeIsNotNull() {
            addCriterion("finish_time is not null");
            return (Criteria) this;
        }

        public Criteria andFinishTimeEqualTo(Integer value) {
            addCriterion("finish_time =", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeNotEqualTo(Integer value) {
            addCriterion("finish_time <>", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeGreaterThan(Integer value) {
            addCriterion("finish_time >", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("finish_time >=", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeLessThan(Integer value) {
            addCriterion("finish_time <", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeLessThanOrEqualTo(Integer value) {
            addCriterion("finish_time <=", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeIn(List<Integer> values) {
            addCriterion("finish_time in", values, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeNotIn(List<Integer> values) {
            addCriterion("finish_time not in", values, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeBetween(Integer value1, Integer value2) {
            addCriterion("finish_time between", value1, value2, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("finish_time not between", value1, value2, "finishTime");
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

        public Criteria andThirdSkuIdIsNull() {
            addCriterion("third_sku_id is null");
            return (Criteria) this;
        }

        public Criteria andThirdSkuIdIsNotNull() {
            addCriterion("third_sku_id is not null");
            return (Criteria) this;
        }

        public Criteria andThirdSkuIdEqualTo(Integer value) {
            addCriterion("third_sku_id =", value, "thirdSkuId");
            return (Criteria) this;
        }

        public Criteria andThirdSkuIdNotEqualTo(Integer value) {
            addCriterion("third_sku_id <>", value, "thirdSkuId");
            return (Criteria) this;
        }

        public Criteria andThirdSkuIdGreaterThan(Integer value) {
            addCriterion("third_sku_id >", value, "thirdSkuId");
            return (Criteria) this;
        }

        public Criteria andThirdSkuIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("third_sku_id >=", value, "thirdSkuId");
            return (Criteria) this;
        }

        public Criteria andThirdSkuIdLessThan(Integer value) {
            addCriterion("third_sku_id <", value, "thirdSkuId");
            return (Criteria) this;
        }

        public Criteria andThirdSkuIdLessThanOrEqualTo(Integer value) {
            addCriterion("third_sku_id <=", value, "thirdSkuId");
            return (Criteria) this;
        }

        public Criteria andThirdSkuIdIn(List<Integer> values) {
            addCriterion("third_sku_id in", values, "thirdSkuId");
            return (Criteria) this;
        }

        public Criteria andThirdSkuIdNotIn(List<Integer> values) {
            addCriterion("third_sku_id not in", values, "thirdSkuId");
            return (Criteria) this;
        }

        public Criteria andThirdSkuIdBetween(Integer value1, Integer value2) {
            addCriterion("third_sku_id between", value1, value2, "thirdSkuId");
            return (Criteria) this;
        }

        public Criteria andThirdSkuIdNotBetween(Integer value1, Integer value2) {
            addCriterion("third_sku_id not between", value1, value2, "thirdSkuId");
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