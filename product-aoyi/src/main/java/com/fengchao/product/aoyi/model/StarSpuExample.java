package com.fengchao.product.aoyi.model;

import java.util.ArrayList;
import java.util.List;

public class StarSpuExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StarSpuExample() {
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

        public Criteria andCategoryIdIsNull() {
            addCriterion("category_id is null");
            return (Criteria) this;
        }

        public Criteria andCategoryIdIsNotNull() {
            addCriterion("category_id is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryIdEqualTo(Integer value) {
            addCriterion("category_id =", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdNotEqualTo(Integer value) {
            addCriterion("category_id <>", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdGreaterThan(Integer value) {
            addCriterion("category_id >", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("category_id >=", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdLessThan(Integer value) {
            addCriterion("category_id <", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdLessThanOrEqualTo(Integer value) {
            addCriterion("category_id <=", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdIn(List<Integer> values) {
            addCriterion("category_id in", values, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdNotIn(List<Integer> values) {
            addCriterion("category_id not in", values, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdBetween(Integer value1, Integer value2) {
            addCriterion("category_id between", value1, value2, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdNotBetween(Integer value1, Integer value2) {
            addCriterion("category_id not between", value1, value2, "categoryId");
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

        public Criteria andGoodsAreaInfoIsNull() {
            addCriterion("goods_area_info is null");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaInfoIsNotNull() {
            addCriterion("goods_area_info is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaInfoEqualTo(String value) {
            addCriterion("goods_area_info =", value, "goodsAreaInfo");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaInfoNotEqualTo(String value) {
            addCriterion("goods_area_info <>", value, "goodsAreaInfo");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaInfoGreaterThan(String value) {
            addCriterion("goods_area_info >", value, "goodsAreaInfo");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaInfoGreaterThanOrEqualTo(String value) {
            addCriterion("goods_area_info >=", value, "goodsAreaInfo");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaInfoLessThan(String value) {
            addCriterion("goods_area_info <", value, "goodsAreaInfo");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaInfoLessThanOrEqualTo(String value) {
            addCriterion("goods_area_info <=", value, "goodsAreaInfo");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaInfoLike(String value) {
            addCriterion("goods_area_info like", value, "goodsAreaInfo");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaInfoNotLike(String value) {
            addCriterion("goods_area_info not like", value, "goodsAreaInfo");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaInfoIn(List<String> values) {
            addCriterion("goods_area_info in", values, "goodsAreaInfo");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaInfoNotIn(List<String> values) {
            addCriterion("goods_area_info not in", values, "goodsAreaInfo");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaInfoBetween(String value1, String value2) {
            addCriterion("goods_area_info between", value1, value2, "goodsAreaInfo");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaInfoNotBetween(String value1, String value2) {
            addCriterion("goods_area_info not between", value1, value2, "goodsAreaInfo");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaKbnIsNull() {
            addCriterion("goods_area_kbn is null");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaKbnIsNotNull() {
            addCriterion("goods_area_kbn is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaKbnEqualTo(Integer value) {
            addCriterion("goods_area_kbn =", value, "goodsAreaKbn");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaKbnNotEqualTo(Integer value) {
            addCriterion("goods_area_kbn <>", value, "goodsAreaKbn");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaKbnGreaterThan(Integer value) {
            addCriterion("goods_area_kbn >", value, "goodsAreaKbn");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaKbnGreaterThanOrEqualTo(Integer value) {
            addCriterion("goods_area_kbn >=", value, "goodsAreaKbn");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaKbnLessThan(Integer value) {
            addCriterion("goods_area_kbn <", value, "goodsAreaKbn");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaKbnLessThanOrEqualTo(Integer value) {
            addCriterion("goods_area_kbn <=", value, "goodsAreaKbn");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaKbnIn(List<Integer> values) {
            addCriterion("goods_area_kbn in", values, "goodsAreaKbn");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaKbnNotIn(List<Integer> values) {
            addCriterion("goods_area_kbn not in", values, "goodsAreaKbn");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaKbnBetween(Integer value1, Integer value2) {
            addCriterion("goods_area_kbn between", value1, value2, "goodsAreaKbn");
            return (Criteria) this;
        }

        public Criteria andGoodsAreaKbnNotBetween(Integer value1, Integer value2) {
            addCriterion("goods_area_kbn not between", value1, value2, "goodsAreaKbn");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeIsNull() {
            addCriterion("goods_code is null");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeIsNotNull() {
            addCriterion("goods_code is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeEqualTo(String value) {
            addCriterion("goods_code =", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeNotEqualTo(String value) {
            addCriterion("goods_code <>", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeGreaterThan(String value) {
            addCriterion("goods_code >", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeGreaterThanOrEqualTo(String value) {
            addCriterion("goods_code >=", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeLessThan(String value) {
            addCriterion("goods_code <", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeLessThanOrEqualTo(String value) {
            addCriterion("goods_code <=", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeLike(String value) {
            addCriterion("goods_code like", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeNotLike(String value) {
            addCriterion("goods_code not like", value, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeIn(List<String> values) {
            addCriterion("goods_code in", values, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeNotIn(List<String> values) {
            addCriterion("goods_code not in", values, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeBetween(String value1, String value2) {
            addCriterion("goods_code between", value1, value2, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsCodeNotBetween(String value1, String value2) {
            addCriterion("goods_code not between", value1, value2, "goodsCode");
            return (Criteria) this;
        }

        public Criteria andGoodsIslimitIsNull() {
            addCriterion("goods_islimit is null");
            return (Criteria) this;
        }

        public Criteria andGoodsIslimitIsNotNull() {
            addCriterion("goods_islimit is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsIslimitEqualTo(Integer value) {
            addCriterion("goods_islimit =", value, "goodsIslimit");
            return (Criteria) this;
        }

        public Criteria andGoodsIslimitNotEqualTo(Integer value) {
            addCriterion("goods_islimit <>", value, "goodsIslimit");
            return (Criteria) this;
        }

        public Criteria andGoodsIslimitGreaterThan(Integer value) {
            addCriterion("goods_islimit >", value, "goodsIslimit");
            return (Criteria) this;
        }

        public Criteria andGoodsIslimitGreaterThanOrEqualTo(Integer value) {
            addCriterion("goods_islimit >=", value, "goodsIslimit");
            return (Criteria) this;
        }

        public Criteria andGoodsIslimitLessThan(Integer value) {
            addCriterion("goods_islimit <", value, "goodsIslimit");
            return (Criteria) this;
        }

        public Criteria andGoodsIslimitLessThanOrEqualTo(Integer value) {
            addCriterion("goods_islimit <=", value, "goodsIslimit");
            return (Criteria) this;
        }

        public Criteria andGoodsIslimitIn(List<Integer> values) {
            addCriterion("goods_islimit in", values, "goodsIslimit");
            return (Criteria) this;
        }

        public Criteria andGoodsIslimitNotIn(List<Integer> values) {
            addCriterion("goods_islimit not in", values, "goodsIslimit");
            return (Criteria) this;
        }

        public Criteria andGoodsIslimitBetween(Integer value1, Integer value2) {
            addCriterion("goods_islimit between", value1, value2, "goodsIslimit");
            return (Criteria) this;
        }

        public Criteria andGoodsIslimitNotBetween(Integer value1, Integer value2) {
            addCriterion("goods_islimit not between", value1, value2, "goodsIslimit");
            return (Criteria) this;
        }

        public Criteria andGoosLimitNumIsNull() {
            addCriterion("goos_limit_num is null");
            return (Criteria) this;
        }

        public Criteria andGoosLimitNumIsNotNull() {
            addCriterion("goos_limit_num is not null");
            return (Criteria) this;
        }

        public Criteria andGoosLimitNumEqualTo(Integer value) {
            addCriterion("goos_limit_num =", value, "goosLimitNum");
            return (Criteria) this;
        }

        public Criteria andGoosLimitNumNotEqualTo(Integer value) {
            addCriterion("goos_limit_num <>", value, "goosLimitNum");
            return (Criteria) this;
        }

        public Criteria andGoosLimitNumGreaterThan(Integer value) {
            addCriterion("goos_limit_num >", value, "goosLimitNum");
            return (Criteria) this;
        }

        public Criteria andGoosLimitNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("goos_limit_num >=", value, "goosLimitNum");
            return (Criteria) this;
        }

        public Criteria andGoosLimitNumLessThan(Integer value) {
            addCriterion("goos_limit_num <", value, "goosLimitNum");
            return (Criteria) this;
        }

        public Criteria andGoosLimitNumLessThanOrEqualTo(Integer value) {
            addCriterion("goos_limit_num <=", value, "goosLimitNum");
            return (Criteria) this;
        }

        public Criteria andGoosLimitNumIn(List<Integer> values) {
            addCriterion("goos_limit_num in", values, "goosLimitNum");
            return (Criteria) this;
        }

        public Criteria andGoosLimitNumNotIn(List<Integer> values) {
            addCriterion("goos_limit_num not in", values, "goosLimitNum");
            return (Criteria) this;
        }

        public Criteria andGoosLimitNumBetween(Integer value1, Integer value2) {
            addCriterion("goos_limit_num between", value1, value2, "goosLimitNum");
            return (Criteria) this;
        }

        public Criteria andGoosLimitNumNotBetween(Integer value1, Integer value2) {
            addCriterion("goos_limit_num not between", value1, value2, "goosLimitNum");
            return (Criteria) this;
        }

        public Criteria andMainImgUrlIsNull() {
            addCriterion("main_img_url is null");
            return (Criteria) this;
        }

        public Criteria andMainImgUrlIsNotNull() {
            addCriterion("main_img_url is not null");
            return (Criteria) this;
        }

        public Criteria andMainImgUrlEqualTo(String value) {
            addCriterion("main_img_url =", value, "mainImgUrl");
            return (Criteria) this;
        }

        public Criteria andMainImgUrlNotEqualTo(String value) {
            addCriterion("main_img_url <>", value, "mainImgUrl");
            return (Criteria) this;
        }

        public Criteria andMainImgUrlGreaterThan(String value) {
            addCriterion("main_img_url >", value, "mainImgUrl");
            return (Criteria) this;
        }

        public Criteria andMainImgUrlGreaterThanOrEqualTo(String value) {
            addCriterion("main_img_url >=", value, "mainImgUrl");
            return (Criteria) this;
        }

        public Criteria andMainImgUrlLessThan(String value) {
            addCriterion("main_img_url <", value, "mainImgUrl");
            return (Criteria) this;
        }

        public Criteria andMainImgUrlLessThanOrEqualTo(String value) {
            addCriterion("main_img_url <=", value, "mainImgUrl");
            return (Criteria) this;
        }

        public Criteria andMainImgUrlLike(String value) {
            addCriterion("main_img_url like", value, "mainImgUrl");
            return (Criteria) this;
        }

        public Criteria andMainImgUrlNotLike(String value) {
            addCriterion("main_img_url not like", value, "mainImgUrl");
            return (Criteria) this;
        }

        public Criteria andMainImgUrlIn(List<String> values) {
            addCriterion("main_img_url in", values, "mainImgUrl");
            return (Criteria) this;
        }

        public Criteria andMainImgUrlNotIn(List<String> values) {
            addCriterion("main_img_url not in", values, "mainImgUrl");
            return (Criteria) this;
        }

        public Criteria andMainImgUrlBetween(String value1, String value2) {
            addCriterion("main_img_url between", value1, value2, "mainImgUrl");
            return (Criteria) this;
        }

        public Criteria andMainImgUrlNotBetween(String value1, String value2) {
            addCriterion("main_img_url not between", value1, value2, "mainImgUrl");
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

        public Criteria andSimpleDescIsNull() {
            addCriterion("simple_desc is null");
            return (Criteria) this;
        }

        public Criteria andSimpleDescIsNotNull() {
            addCriterion("simple_desc is not null");
            return (Criteria) this;
        }

        public Criteria andSimpleDescEqualTo(String value) {
            addCriterion("simple_desc =", value, "simpleDesc");
            return (Criteria) this;
        }

        public Criteria andSimpleDescNotEqualTo(String value) {
            addCriterion("simple_desc <>", value, "simpleDesc");
            return (Criteria) this;
        }

        public Criteria andSimpleDescGreaterThan(String value) {
            addCriterion("simple_desc >", value, "simpleDesc");
            return (Criteria) this;
        }

        public Criteria andSimpleDescGreaterThanOrEqualTo(String value) {
            addCriterion("simple_desc >=", value, "simpleDesc");
            return (Criteria) this;
        }

        public Criteria andSimpleDescLessThan(String value) {
            addCriterion("simple_desc <", value, "simpleDesc");
            return (Criteria) this;
        }

        public Criteria andSimpleDescLessThanOrEqualTo(String value) {
            addCriterion("simple_desc <=", value, "simpleDesc");
            return (Criteria) this;
        }

        public Criteria andSimpleDescLike(String value) {
            addCriterion("simple_desc like", value, "simpleDesc");
            return (Criteria) this;
        }

        public Criteria andSimpleDescNotLike(String value) {
            addCriterion("simple_desc not like", value, "simpleDesc");
            return (Criteria) this;
        }

        public Criteria andSimpleDescIn(List<String> values) {
            addCriterion("simple_desc in", values, "simpleDesc");
            return (Criteria) this;
        }

        public Criteria andSimpleDescNotIn(List<String> values) {
            addCriterion("simple_desc not in", values, "simpleDesc");
            return (Criteria) this;
        }

        public Criteria andSimpleDescBetween(String value1, String value2) {
            addCriterion("simple_desc between", value1, value2, "simpleDesc");
            return (Criteria) this;
        }

        public Criteria andSimpleDescNotBetween(String value1, String value2) {
            addCriterion("simple_desc not between", value1, value2, "simpleDesc");
            return (Criteria) this;
        }

        public Criteria andSpuIdIsNull() {
            addCriterion("spu_id is null");
            return (Criteria) this;
        }

        public Criteria andSpuIdIsNotNull() {
            addCriterion("spu_id is not null");
            return (Criteria) this;
        }

        public Criteria andSpuIdEqualTo(String value) {
            addCriterion("spu_id =", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdNotEqualTo(String value) {
            addCriterion("spu_id <>", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdGreaterThan(String value) {
            addCriterion("spu_id >", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdGreaterThanOrEqualTo(String value) {
            addCriterion("spu_id >=", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdLessThan(String value) {
            addCriterion("spu_id <", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdLessThanOrEqualTo(String value) {
            addCriterion("spu_id <=", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdLike(String value) {
            addCriterion("spu_id like", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdNotLike(String value) {
            addCriterion("spu_id not like", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdIn(List<String> values) {
            addCriterion("spu_id in", values, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdNotIn(List<String> values) {
            addCriterion("spu_id not in", values, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdBetween(String value1, String value2) {
            addCriterion("spu_id between", value1, value2, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdNotBetween(String value1, String value2) {
            addCriterion("spu_id not between", value1, value2, "spuId");
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

        public Criteria andTransFeeIsNull() {
            addCriterion("trans_fee is null");
            return (Criteria) this;
        }

        public Criteria andTransFeeIsNotNull() {
            addCriterion("trans_fee is not null");
            return (Criteria) this;
        }

        public Criteria andTransFeeEqualTo(Integer value) {
            addCriterion("trans_fee =", value, "transFee");
            return (Criteria) this;
        }

        public Criteria andTransFeeNotEqualTo(Integer value) {
            addCriterion("trans_fee <>", value, "transFee");
            return (Criteria) this;
        }

        public Criteria andTransFeeGreaterThan(Integer value) {
            addCriterion("trans_fee >", value, "transFee");
            return (Criteria) this;
        }

        public Criteria andTransFeeGreaterThanOrEqualTo(Integer value) {
            addCriterion("trans_fee >=", value, "transFee");
            return (Criteria) this;
        }

        public Criteria andTransFeeLessThan(Integer value) {
            addCriterion("trans_fee <", value, "transFee");
            return (Criteria) this;
        }

        public Criteria andTransFeeLessThanOrEqualTo(Integer value) {
            addCriterion("trans_fee <=", value, "transFee");
            return (Criteria) this;
        }

        public Criteria andTransFeeIn(List<Integer> values) {
            addCriterion("trans_fee in", values, "transFee");
            return (Criteria) this;
        }

        public Criteria andTransFeeNotIn(List<Integer> values) {
            addCriterion("trans_fee not in", values, "transFee");
            return (Criteria) this;
        }

        public Criteria andTransFeeBetween(Integer value1, Integer value2) {
            addCriterion("trans_fee between", value1, value2, "transFee");
            return (Criteria) this;
        }

        public Criteria andTransFeeNotBetween(Integer value1, Integer value2) {
            addCriterion("trans_fee not between", value1, value2, "transFee");
            return (Criteria) this;
        }

        public Criteria andVideoUrlIsNull() {
            addCriterion("video_url is null");
            return (Criteria) this;
        }

        public Criteria andVideoUrlIsNotNull() {
            addCriterion("video_url is not null");
            return (Criteria) this;
        }

        public Criteria andVideoUrlEqualTo(String value) {
            addCriterion("video_url =", value, "videoUrl");
            return (Criteria) this;
        }

        public Criteria andVideoUrlNotEqualTo(String value) {
            addCriterion("video_url <>", value, "videoUrl");
            return (Criteria) this;
        }

        public Criteria andVideoUrlGreaterThan(String value) {
            addCriterion("video_url >", value, "videoUrl");
            return (Criteria) this;
        }

        public Criteria andVideoUrlGreaterThanOrEqualTo(String value) {
            addCriterion("video_url >=", value, "videoUrl");
            return (Criteria) this;
        }

        public Criteria andVideoUrlLessThan(String value) {
            addCriterion("video_url <", value, "videoUrl");
            return (Criteria) this;
        }

        public Criteria andVideoUrlLessThanOrEqualTo(String value) {
            addCriterion("video_url <=", value, "videoUrl");
            return (Criteria) this;
        }

        public Criteria andVideoUrlLike(String value) {
            addCriterion("video_url like", value, "videoUrl");
            return (Criteria) this;
        }

        public Criteria andVideoUrlNotLike(String value) {
            addCriterion("video_url not like", value, "videoUrl");
            return (Criteria) this;
        }

        public Criteria andVideoUrlIn(List<String> values) {
            addCriterion("video_url in", values, "videoUrl");
            return (Criteria) this;
        }

        public Criteria andVideoUrlNotIn(List<String> values) {
            addCriterion("video_url not in", values, "videoUrl");
            return (Criteria) this;
        }

        public Criteria andVideoUrlBetween(String value1, String value2) {
            addCriterion("video_url between", value1, value2, "videoUrl");
            return (Criteria) this;
        }

        public Criteria andVideoUrlNotBetween(String value1, String value2) {
            addCriterion("video_url not between", value1, value2, "videoUrl");
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