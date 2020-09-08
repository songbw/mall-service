package com.fengchao.product.aoyi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AoyiProdIndexExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AoyiProdIndexExample() {
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

        public Criteria andSkuidIsNull() {
            addCriterion("skuId is null");
            return (Criteria) this;
        }

        public Criteria andSkuidIsNotNull() {
            addCriterion("skuId is not null");
            return (Criteria) this;
        }

        public Criteria andSkuidEqualTo(String value) {
            addCriterion("skuId =", value, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidNotEqualTo(String value) {
            addCriterion("skuId <>", value, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidGreaterThan(String value) {
            addCriterion("skuId >", value, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidGreaterThanOrEqualTo(String value) {
            addCriterion("skuId >=", value, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidLessThan(String value) {
            addCriterion("skuId <", value, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidLessThanOrEqualTo(String value) {
            addCriterion("skuId <=", value, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidLike(String value) {
            addCriterion("skuId like", value, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidNotLike(String value) {
            addCriterion("skuId not like", value, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidIn(List<String> values) {
            addCriterion("skuId in", values, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidNotIn(List<String> values) {
            addCriterion("skuId not in", values, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidBetween(String value1, String value2) {
            addCriterion("skuId between", value1, value2, "skuid");
            return (Criteria) this;
        }

        public Criteria andSkuidNotBetween(String value1, String value2) {
            addCriterion("skuId not between", value1, value2, "skuid");
            return (Criteria) this;
        }

        public Criteria andBrandIsNull() {
            addCriterion("brand is null");
            return (Criteria) this;
        }

        public Criteria andBrandIsNotNull() {
            addCriterion("brand is not null");
            return (Criteria) this;
        }

        public Criteria andBrandEqualTo(String value) {
            addCriterion("brand =", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandNotEqualTo(String value) {
            addCriterion("brand <>", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandGreaterThan(String value) {
            addCriterion("brand >", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandGreaterThanOrEqualTo(String value) {
            addCriterion("brand >=", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandLessThan(String value) {
            addCriterion("brand <", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandLessThanOrEqualTo(String value) {
            addCriterion("brand <=", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandLike(String value) {
            addCriterion("brand like", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandNotLike(String value) {
            addCriterion("brand not like", value, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandIn(List<String> values) {
            addCriterion("brand in", values, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandNotIn(List<String> values) {
            addCriterion("brand not in", values, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandBetween(String value1, String value2) {
            addCriterion("brand between", value1, value2, "brand");
            return (Criteria) this;
        }

        public Criteria andBrandNotBetween(String value1, String value2) {
            addCriterion("brand not between", value1, value2, "brand");
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

        public Criteria andImageIsNull() {
            addCriterion("image is null");
            return (Criteria) this;
        }

        public Criteria andImageIsNotNull() {
            addCriterion("image is not null");
            return (Criteria) this;
        }

        public Criteria andImageEqualTo(String value) {
            addCriterion("image =", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotEqualTo(String value) {
            addCriterion("image <>", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageGreaterThan(String value) {
            addCriterion("image >", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageGreaterThanOrEqualTo(String value) {
            addCriterion("image >=", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageLessThan(String value) {
            addCriterion("image <", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageLessThanOrEqualTo(String value) {
            addCriterion("image <=", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageLike(String value) {
            addCriterion("image like", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotLike(String value) {
            addCriterion("image not like", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageIn(List<String> values) {
            addCriterion("image in", values, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotIn(List<String> values) {
            addCriterion("image not in", values, "image");
            return (Criteria) this;
        }

        public Criteria andImageBetween(String value1, String value2) {
            addCriterion("image between", value1, value2, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotBetween(String value1, String value2) {
            addCriterion("image not between", value1, value2, "image");
            return (Criteria) this;
        }

        public Criteria andModelIsNull() {
            addCriterion("model is null");
            return (Criteria) this;
        }

        public Criteria andModelIsNotNull() {
            addCriterion("model is not null");
            return (Criteria) this;
        }

        public Criteria andModelEqualTo(String value) {
            addCriterion("model =", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotEqualTo(String value) {
            addCriterion("model <>", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelGreaterThan(String value) {
            addCriterion("model >", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelGreaterThanOrEqualTo(String value) {
            addCriterion("model >=", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLessThan(String value) {
            addCriterion("model <", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLessThanOrEqualTo(String value) {
            addCriterion("model <=", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLike(String value) {
            addCriterion("model like", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotLike(String value) {
            addCriterion("model not like", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelIn(List<String> values) {
            addCriterion("model in", values, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotIn(List<String> values) {
            addCriterion("model not in", values, "model");
            return (Criteria) this;
        }

        public Criteria andModelBetween(String value1, String value2) {
            addCriterion("model between", value1, value2, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotBetween(String value1, String value2) {
            addCriterion("model not between", value1, value2, "model");
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

        public Criteria andWeightIsNull() {
            addCriterion("weight is null");
            return (Criteria) this;
        }

        public Criteria andWeightIsNotNull() {
            addCriterion("weight is not null");
            return (Criteria) this;
        }

        public Criteria andWeightEqualTo(String value) {
            addCriterion("weight =", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotEqualTo(String value) {
            addCriterion("weight <>", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThan(String value) {
            addCriterion("weight >", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThanOrEqualTo(String value) {
            addCriterion("weight >=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThan(String value) {
            addCriterion("weight <", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThanOrEqualTo(String value) {
            addCriterion("weight <=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLike(String value) {
            addCriterion("weight like", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotLike(String value) {
            addCriterion("weight not like", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightIn(List<String> values) {
            addCriterion("weight in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotIn(List<String> values) {
            addCriterion("weight not in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightBetween(String value1, String value2) {
            addCriterion("weight between", value1, value2, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotBetween(String value1, String value2) {
            addCriterion("weight not between", value1, value2, "weight");
            return (Criteria) this;
        }

        public Criteria andUpcIsNull() {
            addCriterion("upc is null");
            return (Criteria) this;
        }

        public Criteria andUpcIsNotNull() {
            addCriterion("upc is not null");
            return (Criteria) this;
        }

        public Criteria andUpcEqualTo(String value) {
            addCriterion("upc =", value, "upc");
            return (Criteria) this;
        }

        public Criteria andUpcNotEqualTo(String value) {
            addCriterion("upc <>", value, "upc");
            return (Criteria) this;
        }

        public Criteria andUpcGreaterThan(String value) {
            addCriterion("upc >", value, "upc");
            return (Criteria) this;
        }

        public Criteria andUpcGreaterThanOrEqualTo(String value) {
            addCriterion("upc >=", value, "upc");
            return (Criteria) this;
        }

        public Criteria andUpcLessThan(String value) {
            addCriterion("upc <", value, "upc");
            return (Criteria) this;
        }

        public Criteria andUpcLessThanOrEqualTo(String value) {
            addCriterion("upc <=", value, "upc");
            return (Criteria) this;
        }

        public Criteria andUpcLike(String value) {
            addCriterion("upc like", value, "upc");
            return (Criteria) this;
        }

        public Criteria andUpcNotLike(String value) {
            addCriterion("upc not like", value, "upc");
            return (Criteria) this;
        }

        public Criteria andUpcIn(List<String> values) {
            addCriterion("upc in", values, "upc");
            return (Criteria) this;
        }

        public Criteria andUpcNotIn(List<String> values) {
            addCriterion("upc not in", values, "upc");
            return (Criteria) this;
        }

        public Criteria andUpcBetween(String value1, String value2) {
            addCriterion("upc between", value1, value2, "upc");
            return (Criteria) this;
        }

        public Criteria andUpcNotBetween(String value1, String value2) {
            addCriterion("upc not between", value1, value2, "upc");
            return (Criteria) this;
        }

        public Criteria andSaleunitIsNull() {
            addCriterion("saleUnit is null");
            return (Criteria) this;
        }

        public Criteria andSaleunitIsNotNull() {
            addCriterion("saleUnit is not null");
            return (Criteria) this;
        }

        public Criteria andSaleunitEqualTo(String value) {
            addCriterion("saleUnit =", value, "saleunit");
            return (Criteria) this;
        }

        public Criteria andSaleunitNotEqualTo(String value) {
            addCriterion("saleUnit <>", value, "saleunit");
            return (Criteria) this;
        }

        public Criteria andSaleunitGreaterThan(String value) {
            addCriterion("saleUnit >", value, "saleunit");
            return (Criteria) this;
        }

        public Criteria andSaleunitGreaterThanOrEqualTo(String value) {
            addCriterion("saleUnit >=", value, "saleunit");
            return (Criteria) this;
        }

        public Criteria andSaleunitLessThan(String value) {
            addCriterion("saleUnit <", value, "saleunit");
            return (Criteria) this;
        }

        public Criteria andSaleunitLessThanOrEqualTo(String value) {
            addCriterion("saleUnit <=", value, "saleunit");
            return (Criteria) this;
        }

        public Criteria andSaleunitLike(String value) {
            addCriterion("saleUnit like", value, "saleunit");
            return (Criteria) this;
        }

        public Criteria andSaleunitNotLike(String value) {
            addCriterion("saleUnit not like", value, "saleunit");
            return (Criteria) this;
        }

        public Criteria andSaleunitIn(List<String> values) {
            addCriterion("saleUnit in", values, "saleunit");
            return (Criteria) this;
        }

        public Criteria andSaleunitNotIn(List<String> values) {
            addCriterion("saleUnit not in", values, "saleunit");
            return (Criteria) this;
        }

        public Criteria andSaleunitBetween(String value1, String value2) {
            addCriterion("saleUnit between", value1, value2, "saleunit");
            return (Criteria) this;
        }

        public Criteria andSaleunitNotBetween(String value1, String value2) {
            addCriterion("saleUnit not between", value1, value2, "saleunit");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(String value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(String value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(String value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(String value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(String value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(String value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLike(String value) {
            addCriterion("state like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotLike(String value) {
            addCriterion("state not like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<String> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<String> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(String value1, String value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(String value1, String value2) {
            addCriterion("state not between", value1, value2, "state");
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

        public Criteria andPriceEqualTo(String value) {
            addCriterion("price =", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotEqualTo(String value) {
            addCriterion("price <>", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThan(String value) {
            addCriterion("price >", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThanOrEqualTo(String value) {
            addCriterion("price >=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThan(String value) {
            addCriterion("price <", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThanOrEqualTo(String value) {
            addCriterion("price <=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLike(String value) {
            addCriterion("price like", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotLike(String value) {
            addCriterion("price not like", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceIn(List<String> values) {
            addCriterion("price in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotIn(List<String> values) {
            addCriterion("price not in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceBetween(String value1, String value2) {
            addCriterion("price between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotBetween(String value1, String value2) {
            addCriterion("price not between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andIpriceIsNull() {
            addCriterion("iprice is null");
            return (Criteria) this;
        }

        public Criteria andIpriceIsNotNull() {
            addCriterion("iprice is not null");
            return (Criteria) this;
        }

        public Criteria andIpriceEqualTo(Integer value) {
            addCriterion("iprice =", value, "iprice");
            return (Criteria) this;
        }

        public Criteria andIpriceNotEqualTo(Integer value) {
            addCriterion("iprice <>", value, "iprice");
            return (Criteria) this;
        }

        public Criteria andIpriceGreaterThan(Integer value) {
            addCriterion("iprice >", value, "iprice");
            return (Criteria) this;
        }

        public Criteria andIpriceGreaterThanOrEqualTo(Integer value) {
            addCriterion("iprice >=", value, "iprice");
            return (Criteria) this;
        }

        public Criteria andIpriceLessThan(Integer value) {
            addCriterion("iprice <", value, "iprice");
            return (Criteria) this;
        }

        public Criteria andIpriceLessThanOrEqualTo(Integer value) {
            addCriterion("iprice <=", value, "iprice");
            return (Criteria) this;
        }

        public Criteria andIpriceIn(List<Integer> values) {
            addCriterion("iprice in", values, "iprice");
            return (Criteria) this;
        }

        public Criteria andIpriceNotIn(List<Integer> values) {
            addCriterion("iprice not in", values, "iprice");
            return (Criteria) this;
        }

        public Criteria andIpriceBetween(Integer value1, Integer value2) {
            addCriterion("iprice between", value1, value2, "iprice");
            return (Criteria) this;
        }

        public Criteria andIpriceNotBetween(Integer value1, Integer value2) {
            addCriterion("iprice not between", value1, value2, "iprice");
            return (Criteria) this;
        }

        public Criteria andSpriceIsNull() {
            addCriterion("sprice is null");
            return (Criteria) this;
        }

        public Criteria andSpriceIsNotNull() {
            addCriterion("sprice is not null");
            return (Criteria) this;
        }

        public Criteria andSpriceEqualTo(String value) {
            addCriterion("sprice =", value, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceNotEqualTo(String value) {
            addCriterion("sprice <>", value, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceGreaterThan(String value) {
            addCriterion("sprice >", value, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceGreaterThanOrEqualTo(String value) {
            addCriterion("sprice >=", value, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceLessThan(String value) {
            addCriterion("sprice <", value, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceLessThanOrEqualTo(String value) {
            addCriterion("sprice <=", value, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceLike(String value) {
            addCriterion("sprice like", value, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceNotLike(String value) {
            addCriterion("sprice not like", value, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceIn(List<String> values) {
            addCriterion("sprice in", values, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceNotIn(List<String> values) {
            addCriterion("sprice not in", values, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceBetween(String value1, String value2) {
            addCriterion("sprice between", value1, value2, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceNotBetween(String value1, String value2) {
            addCriterion("sprice not between", value1, value2, "sprice");
            return (Criteria) this;
        }

        public Criteria andImagesUrlIsNull() {
            addCriterion("images_url is null");
            return (Criteria) this;
        }

        public Criteria andImagesUrlIsNotNull() {
            addCriterion("images_url is not null");
            return (Criteria) this;
        }

        public Criteria andImagesUrlEqualTo(String value) {
            addCriterion("images_url =", value, "imagesUrl");
            return (Criteria) this;
        }

        public Criteria andImagesUrlNotEqualTo(String value) {
            addCriterion("images_url <>", value, "imagesUrl");
            return (Criteria) this;
        }

        public Criteria andImagesUrlGreaterThan(String value) {
            addCriterion("images_url >", value, "imagesUrl");
            return (Criteria) this;
        }

        public Criteria andImagesUrlGreaterThanOrEqualTo(String value) {
            addCriterion("images_url >=", value, "imagesUrl");
            return (Criteria) this;
        }

        public Criteria andImagesUrlLessThan(String value) {
            addCriterion("images_url <", value, "imagesUrl");
            return (Criteria) this;
        }

        public Criteria andImagesUrlLessThanOrEqualTo(String value) {
            addCriterion("images_url <=", value, "imagesUrl");
            return (Criteria) this;
        }

        public Criteria andImagesUrlLike(String value) {
            addCriterion("images_url like", value, "imagesUrl");
            return (Criteria) this;
        }

        public Criteria andImagesUrlNotLike(String value) {
            addCriterion("images_url not like", value, "imagesUrl");
            return (Criteria) this;
        }

        public Criteria andImagesUrlIn(List<String> values) {
            addCriterion("images_url in", values, "imagesUrl");
            return (Criteria) this;
        }

        public Criteria andImagesUrlNotIn(List<String> values) {
            addCriterion("images_url not in", values, "imagesUrl");
            return (Criteria) this;
        }

        public Criteria andImagesUrlBetween(String value1, String value2) {
            addCriterion("images_url between", value1, value2, "imagesUrl");
            return (Criteria) this;
        }

        public Criteria andImagesUrlNotBetween(String value1, String value2) {
            addCriterion("images_url not between", value1, value2, "imagesUrl");
            return (Criteria) this;
        }

        public Criteria andIntroductionUrlIsNull() {
            addCriterion("introduction_url is null");
            return (Criteria) this;
        }

        public Criteria andIntroductionUrlIsNotNull() {
            addCriterion("introduction_url is not null");
            return (Criteria) this;
        }

        public Criteria andIntroductionUrlEqualTo(String value) {
            addCriterion("introduction_url =", value, "introductionUrl");
            return (Criteria) this;
        }

        public Criteria andIntroductionUrlNotEqualTo(String value) {
            addCriterion("introduction_url <>", value, "introductionUrl");
            return (Criteria) this;
        }

        public Criteria andIntroductionUrlGreaterThan(String value) {
            addCriterion("introduction_url >", value, "introductionUrl");
            return (Criteria) this;
        }

        public Criteria andIntroductionUrlGreaterThanOrEqualTo(String value) {
            addCriterion("introduction_url >=", value, "introductionUrl");
            return (Criteria) this;
        }

        public Criteria andIntroductionUrlLessThan(String value) {
            addCriterion("introduction_url <", value, "introductionUrl");
            return (Criteria) this;
        }

        public Criteria andIntroductionUrlLessThanOrEqualTo(String value) {
            addCriterion("introduction_url <=", value, "introductionUrl");
            return (Criteria) this;
        }

        public Criteria andIntroductionUrlLike(String value) {
            addCriterion("introduction_url like", value, "introductionUrl");
            return (Criteria) this;
        }

        public Criteria andIntroductionUrlNotLike(String value) {
            addCriterion("introduction_url not like", value, "introductionUrl");
            return (Criteria) this;
        }

        public Criteria andIntroductionUrlIn(List<String> values) {
            addCriterion("introduction_url in", values, "introductionUrl");
            return (Criteria) this;
        }

        public Criteria andIntroductionUrlNotIn(List<String> values) {
            addCriterion("introduction_url not in", values, "introductionUrl");
            return (Criteria) this;
        }

        public Criteria andIntroductionUrlBetween(String value1, String value2) {
            addCriterion("introduction_url between", value1, value2, "introductionUrl");
            return (Criteria) this;
        }

        public Criteria andIntroductionUrlNotBetween(String value1, String value2) {
            addCriterion("introduction_url not between", value1, value2, "introductionUrl");
            return (Criteria) this;
        }

        public Criteria andMerchantIdIsNull() {
            addCriterion("merchant_id is null");
            return (Criteria) this;
        }

        public Criteria andMerchantIdIsNotNull() {
            addCriterion("merchant_id is not null");
            return (Criteria) this;
        }

        public Criteria andMerchantIdEqualTo(Integer value) {
            addCriterion("merchant_id =", value, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdNotEqualTo(Integer value) {
            addCriterion("merchant_id <>", value, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdGreaterThan(Integer value) {
            addCriterion("merchant_id >", value, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("merchant_id >=", value, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdLessThan(Integer value) {
            addCriterion("merchant_id <", value, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdLessThanOrEqualTo(Integer value) {
            addCriterion("merchant_id <=", value, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdIn(List<Integer> values) {
            addCriterion("merchant_id in", values, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdNotIn(List<Integer> values) {
            addCriterion("merchant_id not in", values, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdBetween(Integer value1, Integer value2) {
            addCriterion("merchant_id between", value1, value2, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdNotBetween(Integer value1, Integer value2) {
            addCriterion("merchant_id not between", value1, value2, "merchantId");
            return (Criteria) this;
        }

        public Criteria andInventoryIsNull() {
            addCriterion("inventory is null");
            return (Criteria) this;
        }

        public Criteria andInventoryIsNotNull() {
            addCriterion("inventory is not null");
            return (Criteria) this;
        }

        public Criteria andInventoryEqualTo(Integer value) {
            addCriterion("inventory =", value, "inventory");
            return (Criteria) this;
        }

        public Criteria andInventoryNotEqualTo(Integer value) {
            addCriterion("inventory <>", value, "inventory");
            return (Criteria) this;
        }

        public Criteria andInventoryGreaterThan(Integer value) {
            addCriterion("inventory >", value, "inventory");
            return (Criteria) this;
        }

        public Criteria andInventoryGreaterThanOrEqualTo(Integer value) {
            addCriterion("inventory >=", value, "inventory");
            return (Criteria) this;
        }

        public Criteria andInventoryLessThan(Integer value) {
            addCriterion("inventory <", value, "inventory");
            return (Criteria) this;
        }

        public Criteria andInventoryLessThanOrEqualTo(Integer value) {
            addCriterion("inventory <=", value, "inventory");
            return (Criteria) this;
        }

        public Criteria andInventoryIn(List<Integer> values) {
            addCriterion("inventory in", values, "inventory");
            return (Criteria) this;
        }

        public Criteria andInventoryNotIn(List<Integer> values) {
            addCriterion("inventory not in", values, "inventory");
            return (Criteria) this;
        }

        public Criteria andInventoryBetween(Integer value1, Integer value2) {
            addCriterion("inventory between", value1, value2, "inventory");
            return (Criteria) this;
        }

        public Criteria andInventoryNotBetween(Integer value1, Integer value2) {
            addCriterion("inventory not between", value1, value2, "inventory");
            return (Criteria) this;
        }

        public Criteria andBrandIdIsNull() {
            addCriterion("brand_id is null");
            return (Criteria) this;
        }

        public Criteria andBrandIdIsNotNull() {
            addCriterion("brand_id is not null");
            return (Criteria) this;
        }

        public Criteria andBrandIdEqualTo(Integer value) {
            addCriterion("brand_id =", value, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdNotEqualTo(Integer value) {
            addCriterion("brand_id <>", value, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdGreaterThan(Integer value) {
            addCriterion("brand_id >", value, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("brand_id >=", value, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdLessThan(Integer value) {
            addCriterion("brand_id <", value, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdLessThanOrEqualTo(Integer value) {
            addCriterion("brand_id <=", value, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdIn(List<Integer> values) {
            addCriterion("brand_id in", values, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdNotIn(List<Integer> values) {
            addCriterion("brand_id not in", values, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdBetween(Integer value1, Integer value2) {
            addCriterion("brand_id between", value1, value2, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdNotBetween(Integer value1, Integer value2) {
            addCriterion("brand_id not between", value1, value2, "brandId");
            return (Criteria) this;
        }

        public Criteria andMpuIsNull() {
            addCriterion("mpu is null");
            return (Criteria) this;
        }

        public Criteria andMpuIsNotNull() {
            addCriterion("mpu is not null");
            return (Criteria) this;
        }

        public Criteria andMpuEqualTo(String value) {
            addCriterion("mpu =", value, "mpu");
            return (Criteria) this;
        }

        public Criteria andMpuNotEqualTo(String value) {
            addCriterion("mpu <>", value, "mpu");
            return (Criteria) this;
        }

        public Criteria andMpuGreaterThan(String value) {
            addCriterion("mpu >", value, "mpu");
            return (Criteria) this;
        }

        public Criteria andMpuGreaterThanOrEqualTo(String value) {
            addCriterion("mpu >=", value, "mpu");
            return (Criteria) this;
        }

        public Criteria andMpuLessThan(String value) {
            addCriterion("mpu <", value, "mpu");
            return (Criteria) this;
        }

        public Criteria andMpuLessThanOrEqualTo(String value) {
            addCriterion("mpu <=", value, "mpu");
            return (Criteria) this;
        }

        public Criteria andMpuLike(String value) {
            addCriterion("mpu like", value, "mpu");
            return (Criteria) this;
        }

        public Criteria andMpuNotLike(String value) {
            addCriterion("mpu not like", value, "mpu");
            return (Criteria) this;
        }

        public Criteria andMpuIn(List<String> values) {
            addCriterion("mpu in", values, "mpu");
            return (Criteria) this;
        }

        public Criteria andMpuNotIn(List<String> values) {
            addCriterion("mpu not in", values, "mpu");
            return (Criteria) this;
        }

        public Criteria andMpuBetween(String value1, String value2) {
            addCriterion("mpu between", value1, value2, "mpu");
            return (Criteria) this;
        }

        public Criteria andMpuNotBetween(String value1, String value2) {
            addCriterion("mpu not between", value1, value2, "mpu");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNull() {
            addCriterion("created_at is null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNotNull() {
            addCriterion("created_at is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtEqualTo(Date value) {
            addCriterion("created_at =", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotEqualTo(Date value) {
            addCriterion("created_at <>", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThan(Date value) {
            addCriterion("created_at >", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("created_at >=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThan(Date value) {
            addCriterion("created_at <", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThanOrEqualTo(Date value) {
            addCriterion("created_at <=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIn(List<Date> values) {
            addCriterion("created_at in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotIn(List<Date> values) {
            addCriterion("created_at not in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtBetween(Date value1, Date value2) {
            addCriterion("created_at between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotBetween(Date value1, Date value2) {
            addCriterion("created_at not between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIsNull() {
            addCriterion("updated_at is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIsNotNull() {
            addCriterion("updated_at is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtEqualTo(Date value) {
            addCriterion("updated_at =", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotEqualTo(Date value) {
            addCriterion("updated_at <>", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtGreaterThan(Date value) {
            addCriterion("updated_at >", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("updated_at >=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtLessThan(Date value) {
            addCriterion("updated_at <", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtLessThanOrEqualTo(Date value) {
            addCriterion("updated_at <=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIn(List<Date> values) {
            addCriterion("updated_at in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotIn(List<Date> values) {
            addCriterion("updated_at not in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtBetween(Date value1, Date value2) {
            addCriterion("updated_at between", value1, value2, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotBetween(Date value1, Date value2) {
            addCriterion("updated_at not between", value1, value2, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andSyncAtIsNull() {
            addCriterion("sync_at is null");
            return (Criteria) this;
        }

        public Criteria andSyncAtIsNotNull() {
            addCriterion("sync_at is not null");
            return (Criteria) this;
        }

        public Criteria andSyncAtEqualTo(Date value) {
            addCriterion("sync_at =", value, "syncAt");
            return (Criteria) this;
        }

        public Criteria andSyncAtNotEqualTo(Date value) {
            addCriterion("sync_at <>", value, "syncAt");
            return (Criteria) this;
        }

        public Criteria andSyncAtGreaterThan(Date value) {
            addCriterion("sync_at >", value, "syncAt");
            return (Criteria) this;
        }

        public Criteria andSyncAtGreaterThanOrEqualTo(Date value) {
            addCriterion("sync_at >=", value, "syncAt");
            return (Criteria) this;
        }

        public Criteria andSyncAtLessThan(Date value) {
            addCriterion("sync_at <", value, "syncAt");
            return (Criteria) this;
        }

        public Criteria andSyncAtLessThanOrEqualTo(Date value) {
            addCriterion("sync_at <=", value, "syncAt");
            return (Criteria) this;
        }

        public Criteria andSyncAtIn(List<Date> values) {
            addCriterion("sync_at in", values, "syncAt");
            return (Criteria) this;
        }

        public Criteria andSyncAtNotIn(List<Date> values) {
            addCriterion("sync_at not in", values, "syncAt");
            return (Criteria) this;
        }

        public Criteria andSyncAtBetween(Date value1, Date value2) {
            addCriterion("sync_at between", value1, value2, "syncAt");
            return (Criteria) this;
        }

        public Criteria andSyncAtNotBetween(Date value1, Date value2) {
            addCriterion("sync_at not between", value1, value2, "syncAt");
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

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andCompareUrlIsNull() {
            addCriterion("compare_url is null");
            return (Criteria) this;
        }

        public Criteria andCompareUrlIsNotNull() {
            addCriterion("compare_url is not null");
            return (Criteria) this;
        }

        public Criteria andCompareUrlEqualTo(String value) {
            addCriterion("compare_url =", value, "compareUrl");
            return (Criteria) this;
        }

        public Criteria andCompareUrlNotEqualTo(String value) {
            addCriterion("compare_url <>", value, "compareUrl");
            return (Criteria) this;
        }

        public Criteria andCompareUrlGreaterThan(String value) {
            addCriterion("compare_url >", value, "compareUrl");
            return (Criteria) this;
        }

        public Criteria andCompareUrlGreaterThanOrEqualTo(String value) {
            addCriterion("compare_url >=", value, "compareUrl");
            return (Criteria) this;
        }

        public Criteria andCompareUrlLessThan(String value) {
            addCriterion("compare_url <", value, "compareUrl");
            return (Criteria) this;
        }

        public Criteria andCompareUrlLessThanOrEqualTo(String value) {
            addCriterion("compare_url <=", value, "compareUrl");
            return (Criteria) this;
        }

        public Criteria andCompareUrlLike(String value) {
            addCriterion("compare_url like", value, "compareUrl");
            return (Criteria) this;
        }

        public Criteria andCompareUrlNotLike(String value) {
            addCriterion("compare_url not like", value, "compareUrl");
            return (Criteria) this;
        }

        public Criteria andCompareUrlIn(List<String> values) {
            addCriterion("compare_url in", values, "compareUrl");
            return (Criteria) this;
        }

        public Criteria andCompareUrlNotIn(List<String> values) {
            addCriterion("compare_url not in", values, "compareUrl");
            return (Criteria) this;
        }

        public Criteria andCompareUrlBetween(String value1, String value2) {
            addCriterion("compare_url between", value1, value2, "compareUrl");
            return (Criteria) this;
        }

        public Criteria andCompareUrlNotBetween(String value1, String value2) {
            addCriterion("compare_url not between", value1, value2, "compareUrl");
            return (Criteria) this;
        }

        public Criteria andSubTitleIsNull() {
            addCriterion("sub_title is null");
            return (Criteria) this;
        }

        public Criteria andSubTitleIsNotNull() {
            addCriterion("sub_title is not null");
            return (Criteria) this;
        }

        public Criteria andSubTitleEqualTo(String value) {
            addCriterion("sub_title =", value, "subTitle");
            return (Criteria) this;
        }

        public Criteria andSubTitleNotEqualTo(String value) {
            addCriterion("sub_title <>", value, "subTitle");
            return (Criteria) this;
        }

        public Criteria andSubTitleGreaterThan(String value) {
            addCriterion("sub_title >", value, "subTitle");
            return (Criteria) this;
        }

        public Criteria andSubTitleGreaterThanOrEqualTo(String value) {
            addCriterion("sub_title >=", value, "subTitle");
            return (Criteria) this;
        }

        public Criteria andSubTitleLessThan(String value) {
            addCriterion("sub_title <", value, "subTitle");
            return (Criteria) this;
        }

        public Criteria andSubTitleLessThanOrEqualTo(String value) {
            addCriterion("sub_title <=", value, "subTitle");
            return (Criteria) this;
        }

        public Criteria andSubTitleLike(String value) {
            addCriterion("sub_title like", value, "subTitle");
            return (Criteria) this;
        }

        public Criteria andSubTitleNotLike(String value) {
            addCriterion("sub_title not like", value, "subTitle");
            return (Criteria) this;
        }

        public Criteria andSubTitleIn(List<String> values) {
            addCriterion("sub_title in", values, "subTitle");
            return (Criteria) this;
        }

        public Criteria andSubTitleNotIn(List<String> values) {
            addCriterion("sub_title not in", values, "subTitle");
            return (Criteria) this;
        }

        public Criteria andSubTitleBetween(String value1, String value2) {
            addCriterion("sub_title between", value1, value2, "subTitle");
            return (Criteria) this;
        }

        public Criteria andSubTitleNotBetween(String value1, String value2) {
            addCriterion("sub_title not between", value1, value2, "subTitle");
            return (Criteria) this;
        }

        public Criteria andComparePriceIsNull() {
            addCriterion("compare_price is null");
            return (Criteria) this;
        }

        public Criteria andComparePriceIsNotNull() {
            addCriterion("compare_price is not null");
            return (Criteria) this;
        }

        public Criteria andComparePriceEqualTo(String value) {
            addCriterion("compare_price =", value, "comparePrice");
            return (Criteria) this;
        }

        public Criteria andComparePriceNotEqualTo(String value) {
            addCriterion("compare_price <>", value, "comparePrice");
            return (Criteria) this;
        }

        public Criteria andComparePriceGreaterThan(String value) {
            addCriterion("compare_price >", value, "comparePrice");
            return (Criteria) this;
        }

        public Criteria andComparePriceGreaterThanOrEqualTo(String value) {
            addCriterion("compare_price >=", value, "comparePrice");
            return (Criteria) this;
        }

        public Criteria andComparePriceLessThan(String value) {
            addCriterion("compare_price <", value, "comparePrice");
            return (Criteria) this;
        }

        public Criteria andComparePriceLessThanOrEqualTo(String value) {
            addCriterion("compare_price <=", value, "comparePrice");
            return (Criteria) this;
        }

        public Criteria andComparePriceLike(String value) {
            addCriterion("compare_price like", value, "comparePrice");
            return (Criteria) this;
        }

        public Criteria andComparePriceNotLike(String value) {
            addCriterion("compare_price not like", value, "comparePrice");
            return (Criteria) this;
        }

        public Criteria andComparePriceIn(List<String> values) {
            addCriterion("compare_price in", values, "comparePrice");
            return (Criteria) this;
        }

        public Criteria andComparePriceNotIn(List<String> values) {
            addCriterion("compare_price not in", values, "comparePrice");
            return (Criteria) this;
        }

        public Criteria andComparePriceBetween(String value1, String value2) {
            addCriterion("compare_price between", value1, value2, "comparePrice");
            return (Criteria) this;
        }

        public Criteria andComparePriceNotBetween(String value1, String value2) {
            addCriterion("compare_price not between", value1, value2, "comparePrice");
            return (Criteria) this;
        }

        public Criteria andTaxRateIsNull() {
            addCriterion("tax_rate is null");
            return (Criteria) this;
        }

        public Criteria andTaxRateIsNotNull() {
            addCriterion("tax_rate is not null");
            return (Criteria) this;
        }

        public Criteria andTaxRateEqualTo(String value) {
            addCriterion("tax_rate =", value, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateNotEqualTo(String value) {
            addCriterion("tax_rate <>", value, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateGreaterThan(String value) {
            addCriterion("tax_rate >", value, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateGreaterThanOrEqualTo(String value) {
            addCriterion("tax_rate >=", value, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateLessThan(String value) {
            addCriterion("tax_rate <", value, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateLessThanOrEqualTo(String value) {
            addCriterion("tax_rate <=", value, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateLike(String value) {
            addCriterion("tax_rate like", value, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateNotLike(String value) {
            addCriterion("tax_rate not like", value, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateIn(List<String> values) {
            addCriterion("tax_rate in", values, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateNotIn(List<String> values) {
            addCriterion("tax_rate not in", values, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateBetween(String value1, String value2) {
            addCriterion("tax_rate between", value1, value2, "taxRate");
            return (Criteria) this;
        }

        public Criteria andTaxRateNotBetween(String value1, String value2) {
            addCriterion("tax_rate not between", value1, value2, "taxRate");
            return (Criteria) this;
        }

        public Criteria andFloorPriceIsNull() {
            addCriterion("floor_price is null");
            return (Criteria) this;
        }

        public Criteria andFloorPriceIsNotNull() {
            addCriterion("floor_price is not null");
            return (Criteria) this;
        }

        public Criteria andFloorPriceEqualTo(String value) {
            addCriterion("floor_price =", value, "floorPrice");
            return (Criteria) this;
        }

        public Criteria andFloorPriceNotEqualTo(String value) {
            addCriterion("floor_price <>", value, "floorPrice");
            return (Criteria) this;
        }

        public Criteria andFloorPriceGreaterThan(String value) {
            addCriterion("floor_price >", value, "floorPrice");
            return (Criteria) this;
        }

        public Criteria andFloorPriceGreaterThanOrEqualTo(String value) {
            addCriterion("floor_price >=", value, "floorPrice");
            return (Criteria) this;
        }

        public Criteria andFloorPriceLessThan(String value) {
            addCriterion("floor_price <", value, "floorPrice");
            return (Criteria) this;
        }

        public Criteria andFloorPriceLessThanOrEqualTo(String value) {
            addCriterion("floor_price <=", value, "floorPrice");
            return (Criteria) this;
        }

        public Criteria andFloorPriceLike(String value) {
            addCriterion("floor_price like", value, "floorPrice");
            return (Criteria) this;
        }

        public Criteria andFloorPriceNotLike(String value) {
            addCriterion("floor_price not like", value, "floorPrice");
            return (Criteria) this;
        }

        public Criteria andFloorPriceIn(List<String> values) {
            addCriterion("floor_price in", values, "floorPrice");
            return (Criteria) this;
        }

        public Criteria andFloorPriceNotIn(List<String> values) {
            addCriterion("floor_price not in", values, "floorPrice");
            return (Criteria) this;
        }

        public Criteria andFloorPriceBetween(String value1, String value2) {
            addCriterion("floor_price between", value1, value2, "floorPrice");
            return (Criteria) this;
        }

        public Criteria andFloorPriceNotBetween(String value1, String value2) {
            addCriterion("floor_price not between", value1, value2, "floorPrice");
            return (Criteria) this;
        }

        public Criteria andCrossBorderIsNull() {
            addCriterion("cross_border is null");
            return (Criteria) this;
        }

        public Criteria andCrossBorderIsNotNull() {
            addCriterion("cross_border is not null");
            return (Criteria) this;
        }

        public Criteria andCrossBorderEqualTo(Integer value) {
            addCriterion("cross_border =", value, "crossBorder");
            return (Criteria) this;
        }

        public Criteria andCrossBorderNotEqualTo(Integer value) {
            addCriterion("cross_border <>", value, "crossBorder");
            return (Criteria) this;
        }

        public Criteria andCrossBorderGreaterThan(Integer value) {
            addCriterion("cross_border >", value, "crossBorder");
            return (Criteria) this;
        }

        public Criteria andCrossBorderGreaterThanOrEqualTo(Integer value) {
            addCriterion("cross_border >=", value, "crossBorder");
            return (Criteria) this;
        }

        public Criteria andCrossBorderLessThan(Integer value) {
            addCriterion("cross_border <", value, "crossBorder");
            return (Criteria) this;
        }

        public Criteria andCrossBorderLessThanOrEqualTo(Integer value) {
            addCriterion("cross_border <=", value, "crossBorder");
            return (Criteria) this;
        }

        public Criteria andCrossBorderIn(List<Integer> values) {
            addCriterion("cross_border in", values, "crossBorder");
            return (Criteria) this;
        }

        public Criteria andCrossBorderNotIn(List<Integer> values) {
            addCriterion("cross_border not in", values, "crossBorder");
            return (Criteria) this;
        }

        public Criteria andCrossBorderBetween(Integer value1, Integer value2) {
            addCriterion("cross_border between", value1, value2, "crossBorder");
            return (Criteria) this;
        }

        public Criteria andCrossBorderNotBetween(Integer value1, Integer value2) {
            addCriterion("cross_border not between", value1, value2, "crossBorder");
            return (Criteria) this;
        }

        public Criteria andMerchantCodeIsNull() {
            addCriterion("merchant_code is null");
            return (Criteria) this;
        }

        public Criteria andMerchantCodeIsNotNull() {
            addCriterion("merchant_code is not null");
            return (Criteria) this;
        }

        public Criteria andMerchantCodeEqualTo(String value) {
            addCriterion("merchant_code =", value, "merchantCode");
            return (Criteria) this;
        }

        public Criteria andMerchantCodeNotEqualTo(String value) {
            addCriterion("merchant_code <>", value, "merchantCode");
            return (Criteria) this;
        }

        public Criteria andMerchantCodeGreaterThan(String value) {
            addCriterion("merchant_code >", value, "merchantCode");
            return (Criteria) this;
        }

        public Criteria andMerchantCodeGreaterThanOrEqualTo(String value) {
            addCriterion("merchant_code >=", value, "merchantCode");
            return (Criteria) this;
        }

        public Criteria andMerchantCodeLessThan(String value) {
            addCriterion("merchant_code <", value, "merchantCode");
            return (Criteria) this;
        }

        public Criteria andMerchantCodeLessThanOrEqualTo(String value) {
            addCriterion("merchant_code <=", value, "merchantCode");
            return (Criteria) this;
        }

        public Criteria andMerchantCodeLike(String value) {
            addCriterion("merchant_code like", value, "merchantCode");
            return (Criteria) this;
        }

        public Criteria andMerchantCodeNotLike(String value) {
            addCriterion("merchant_code not like", value, "merchantCode");
            return (Criteria) this;
        }

        public Criteria andMerchantCodeIn(List<String> values) {
            addCriterion("merchant_code in", values, "merchantCode");
            return (Criteria) this;
        }

        public Criteria andMerchantCodeNotIn(List<String> values) {
            addCriterion("merchant_code not in", values, "merchantCode");
            return (Criteria) this;
        }

        public Criteria andMerchantCodeBetween(String value1, String value2) {
            addCriterion("merchant_code between", value1, value2, "merchantCode");
            return (Criteria) this;
        }

        public Criteria andMerchantCodeNotBetween(String value1, String value2) {
            addCriterion("merchant_code not between", value1, value2, "merchantCode");
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