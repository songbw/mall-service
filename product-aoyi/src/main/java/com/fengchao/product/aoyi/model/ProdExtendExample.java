package com.fengchao.product.aoyi.model;

import java.util.ArrayList;
import java.util.List;

public class ProdExtendExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ProdExtendExample() {
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