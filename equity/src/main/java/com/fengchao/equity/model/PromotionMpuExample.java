package com.fengchao.equity.model;

import java.util.ArrayList;
import java.util.List;

public class PromotionMpuExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PromotionMpuExample() {
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

        public Criteria andPromotionIdIsNull() {
            addCriterion("promotion_id is null");
            return (Criteria) this;
        }

        public Criteria andPromotionIdIsNotNull() {
            addCriterion("promotion_id is not null");
            return (Criteria) this;
        }

        public Criteria andPromotionIdEqualTo(Integer value) {
            addCriterion("promotion_id =", value, "promotionId");
            return (Criteria) this;
        }

        public Criteria andPromotionIdNotEqualTo(Integer value) {
            addCriterion("promotion_id <>", value, "promotionId");
            return (Criteria) this;
        }

        public Criteria andPromotionIdGreaterThan(Integer value) {
            addCriterion("promotion_id >", value, "promotionId");
            return (Criteria) this;
        }

        public Criteria andPromotionIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("promotion_id >=", value, "promotionId");
            return (Criteria) this;
        }

        public Criteria andPromotionIdLessThan(Integer value) {
            addCriterion("promotion_id <", value, "promotionId");
            return (Criteria) this;
        }

        public Criteria andPromotionIdLessThanOrEqualTo(Integer value) {
            addCriterion("promotion_id <=", value, "promotionId");
            return (Criteria) this;
        }

        public Criteria andPromotionIdIn(List<Integer> values) {
            addCriterion("promotion_id in", values, "promotionId");
            return (Criteria) this;
        }

        public Criteria andPromotionIdNotIn(List<Integer> values) {
            addCriterion("promotion_id not in", values, "promotionId");
            return (Criteria) this;
        }

        public Criteria andPromotionIdBetween(Integer value1, Integer value2) {
            addCriterion("promotion_id between", value1, value2, "promotionId");
            return (Criteria) this;
        }

        public Criteria andPromotionIdNotBetween(Integer value1, Integer value2) {
            addCriterion("promotion_id not between", value1, value2, "promotionId");
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

        public Criteria andDiscountIsNull() {
            addCriterion("discount is null");
            return (Criteria) this;
        }

        public Criteria andDiscountIsNotNull() {
            addCriterion("discount is not null");
            return (Criteria) this;
        }

        public Criteria andDiscountEqualTo(String value) {
            addCriterion("discount =", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountNotEqualTo(String value) {
            addCriterion("discount <>", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountGreaterThan(String value) {
            addCriterion("discount >", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountGreaterThanOrEqualTo(String value) {
            addCriterion("discount >=", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountLessThan(String value) {
            addCriterion("discount <", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountLessThanOrEqualTo(String value) {
            addCriterion("discount <=", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountLike(String value) {
            addCriterion("discount like", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountNotLike(String value) {
            addCriterion("discount not like", value, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountIn(List<String> values) {
            addCriterion("discount in", values, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountNotIn(List<String> values) {
            addCriterion("discount not in", values, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountBetween(String value1, String value2) {
            addCriterion("discount between", value1, value2, "discount");
            return (Criteria) this;
        }

        public Criteria andDiscountNotBetween(String value1, String value2) {
            addCriterion("discount not between", value1, value2, "discount");
            return (Criteria) this;
        }

        public Criteria andScheduleIdIsNull() {
            addCriterion("schedule_id is null");
            return (Criteria) this;
        }

        public Criteria andScheduleIdIsNotNull() {
            addCriterion("schedule_id is not null");
            return (Criteria) this;
        }

        public Criteria andScheduleIdEqualTo(Integer value) {
            addCriterion("schedule_id =", value, "scheduleId");
            return (Criteria) this;
        }

        public Criteria andScheduleIdNotEqualTo(Integer value) {
            addCriterion("schedule_id <>", value, "scheduleId");
            return (Criteria) this;
        }

        public Criteria andScheduleIdGreaterThan(Integer value) {
            addCriterion("schedule_id >", value, "scheduleId");
            return (Criteria) this;
        }

        public Criteria andScheduleIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("schedule_id >=", value, "scheduleId");
            return (Criteria) this;
        }

        public Criteria andScheduleIdLessThan(Integer value) {
            addCriterion("schedule_id <", value, "scheduleId");
            return (Criteria) this;
        }

        public Criteria andScheduleIdLessThanOrEqualTo(Integer value) {
            addCriterion("schedule_id <=", value, "scheduleId");
            return (Criteria) this;
        }

        public Criteria andScheduleIdIn(List<Integer> values) {
            addCriterion("schedule_id in", values, "scheduleId");
            return (Criteria) this;
        }

        public Criteria andScheduleIdNotIn(List<Integer> values) {
            addCriterion("schedule_id not in", values, "scheduleId");
            return (Criteria) this;
        }

        public Criteria andScheduleIdBetween(Integer value1, Integer value2) {
            addCriterion("schedule_id between", value1, value2, "scheduleId");
            return (Criteria) this;
        }

        public Criteria andScheduleIdNotBetween(Integer value1, Integer value2) {
            addCriterion("schedule_id not between", value1, value2, "scheduleId");
            return (Criteria) this;
        }

        public Criteria andPromotionImageIsNull() {
            addCriterion("promotion_image is null");
            return (Criteria) this;
        }

        public Criteria andPromotionImageIsNotNull() {
            addCriterion("promotion_image is not null");
            return (Criteria) this;
        }

        public Criteria andPromotionImageEqualTo(String value) {
            addCriterion("promotion_image =", value, "promotionImage");
            return (Criteria) this;
        }

        public Criteria andPromotionImageNotEqualTo(String value) {
            addCriterion("promotion_image <>", value, "promotionImage");
            return (Criteria) this;
        }

        public Criteria andPromotionImageGreaterThan(String value) {
            addCriterion("promotion_image >", value, "promotionImage");
            return (Criteria) this;
        }

        public Criteria andPromotionImageGreaterThanOrEqualTo(String value) {
            addCriterion("promotion_image >=", value, "promotionImage");
            return (Criteria) this;
        }

        public Criteria andPromotionImageLessThan(String value) {
            addCriterion("promotion_image <", value, "promotionImage");
            return (Criteria) this;
        }

        public Criteria andPromotionImageLessThanOrEqualTo(String value) {
            addCriterion("promotion_image <=", value, "promotionImage");
            return (Criteria) this;
        }

        public Criteria andPromotionImageLike(String value) {
            addCriterion("promotion_image like", value, "promotionImage");
            return (Criteria) this;
        }

        public Criteria andPromotionImageNotLike(String value) {
            addCriterion("promotion_image not like", value, "promotionImage");
            return (Criteria) this;
        }

        public Criteria andPromotionImageIn(List<String> values) {
            addCriterion("promotion_image in", values, "promotionImage");
            return (Criteria) this;
        }

        public Criteria andPromotionImageNotIn(List<String> values) {
            addCriterion("promotion_image not in", values, "promotionImage");
            return (Criteria) this;
        }

        public Criteria andPromotionImageBetween(String value1, String value2) {
            addCriterion("promotion_image between", value1, value2, "promotionImage");
            return (Criteria) this;
        }

        public Criteria andPromotionImageNotBetween(String value1, String value2) {
            addCriterion("promotion_image not between", value1, value2, "promotionImage");
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