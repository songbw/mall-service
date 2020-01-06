package com.fengchao.order.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDetailExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OrderDetailExample() {
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

        public Criteria andOrderIdIsNull() {
            addCriterion("order_id is null");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNotNull() {
            addCriterion("order_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrderIdEqualTo(Integer value) {
            addCriterion("order_id =", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotEqualTo(Integer value) {
            addCriterion("order_id <>", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThan(Integer value) {
            addCriterion("order_id >", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("order_id >=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThan(Integer value) {
            addCriterion("order_id <", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThanOrEqualTo(Integer value) {
            addCriterion("order_id <=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdIn(List<Integer> values) {
            addCriterion("order_id in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotIn(List<Integer> values) {
            addCriterion("order_id not in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdBetween(Integer value1, Integer value2) {
            addCriterion("order_id between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotBetween(Integer value1, Integer value2) {
            addCriterion("order_id not between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andSubOrderIdIsNull() {
            addCriterion("sub_order_id is null");
            return (Criteria) this;
        }

        public Criteria andSubOrderIdIsNotNull() {
            addCriterion("sub_order_id is not null");
            return (Criteria) this;
        }

        public Criteria andSubOrderIdEqualTo(String value) {
            addCriterion("sub_order_id =", value, "subOrderId");
            return (Criteria) this;
        }

        public Criteria andSubOrderIdNotEqualTo(String value) {
            addCriterion("sub_order_id <>", value, "subOrderId");
            return (Criteria) this;
        }

        public Criteria andSubOrderIdGreaterThan(String value) {
            addCriterion("sub_order_id >", value, "subOrderId");
            return (Criteria) this;
        }

        public Criteria andSubOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("sub_order_id >=", value, "subOrderId");
            return (Criteria) this;
        }

        public Criteria andSubOrderIdLessThan(String value) {
            addCriterion("sub_order_id <", value, "subOrderId");
            return (Criteria) this;
        }

        public Criteria andSubOrderIdLessThanOrEqualTo(String value) {
            addCriterion("sub_order_id <=", value, "subOrderId");
            return (Criteria) this;
        }

        public Criteria andSubOrderIdLike(String value) {
            addCriterion("sub_order_id like", value, "subOrderId");
            return (Criteria) this;
        }

        public Criteria andSubOrderIdNotLike(String value) {
            addCriterion("sub_order_id not like", value, "subOrderId");
            return (Criteria) this;
        }

        public Criteria andSubOrderIdIn(List<String> values) {
            addCriterion("sub_order_id in", values, "subOrderId");
            return (Criteria) this;
        }

        public Criteria andSubOrderIdNotIn(List<String> values) {
            addCriterion("sub_order_id not in", values, "subOrderId");
            return (Criteria) this;
        }

        public Criteria andSubOrderIdBetween(String value1, String value2) {
            addCriterion("sub_order_id between", value1, value2, "subOrderId");
            return (Criteria) this;
        }

        public Criteria andSubOrderIdNotBetween(String value1, String value2) {
            addCriterion("sub_order_id not between", value1, value2, "subOrderId");
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

        public Criteria andSkuIdIsNull() {
            addCriterion("sku_id is null");
            return (Criteria) this;
        }

        public Criteria andSkuIdIsNotNull() {
            addCriterion("sku_id is not null");
            return (Criteria) this;
        }

        public Criteria andSkuIdEqualTo(String value) {
            addCriterion("sku_id =", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdNotEqualTo(String value) {
            addCriterion("sku_id <>", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdGreaterThan(String value) {
            addCriterion("sku_id >", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdGreaterThanOrEqualTo(String value) {
            addCriterion("sku_id >=", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdLessThan(String value) {
            addCriterion("sku_id <", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdLessThanOrEqualTo(String value) {
            addCriterion("sku_id <=", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdLike(String value) {
            addCriterion("sku_id like", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdNotLike(String value) {
            addCriterion("sku_id not like", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdIn(List<String> values) {
            addCriterion("sku_id in", values, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdNotIn(List<String> values) {
            addCriterion("sku_id not in", values, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdBetween(String value1, String value2) {
            addCriterion("sku_id between", value1, value2, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdNotBetween(String value1, String value2) {
            addCriterion("sku_id not between", value1, value2, "skuId");
            return (Criteria) this;
        }

        public Criteria andNumIsNull() {
            addCriterion("num is null");
            return (Criteria) this;
        }

        public Criteria andNumIsNotNull() {
            addCriterion("num is not null");
            return (Criteria) this;
        }

        public Criteria andNumEqualTo(Integer value) {
            addCriterion("num =", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumNotEqualTo(Integer value) {
            addCriterion("num <>", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumGreaterThan(Integer value) {
            addCriterion("num >", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("num >=", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumLessThan(Integer value) {
            addCriterion("num <", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumLessThanOrEqualTo(Integer value) {
            addCriterion("num <=", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumIn(List<Integer> values) {
            addCriterion("num in", values, "num");
            return (Criteria) this;
        }

        public Criteria andNumNotIn(List<Integer> values) {
            addCriterion("num not in", values, "num");
            return (Criteria) this;
        }

        public Criteria andNumBetween(Integer value1, Integer value2) {
            addCriterion("num between", value1, value2, "num");
            return (Criteria) this;
        }

        public Criteria andNumNotBetween(Integer value1, Integer value2) {
            addCriterion("num not between", value1, value2, "num");
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

        public Criteria andPromotionDiscountIsNull() {
            addCriterion("promotion_discount is null");
            return (Criteria) this;
        }

        public Criteria andPromotionDiscountIsNotNull() {
            addCriterion("promotion_discount is not null");
            return (Criteria) this;
        }

        public Criteria andPromotionDiscountEqualTo(Float value) {
            addCriterion("promotion_discount =", value, "promotionDiscount");
            return (Criteria) this;
        }

        public Criteria andPromotionDiscountNotEqualTo(Float value) {
            addCriterion("promotion_discount <>", value, "promotionDiscount");
            return (Criteria) this;
        }

        public Criteria andPromotionDiscountGreaterThan(Float value) {
            addCriterion("promotion_discount >", value, "promotionDiscount");
            return (Criteria) this;
        }

        public Criteria andPromotionDiscountGreaterThanOrEqualTo(Float value) {
            addCriterion("promotion_discount >=", value, "promotionDiscount");
            return (Criteria) this;
        }

        public Criteria andPromotionDiscountLessThan(Float value) {
            addCriterion("promotion_discount <", value, "promotionDiscount");
            return (Criteria) this;
        }

        public Criteria andPromotionDiscountLessThanOrEqualTo(Float value) {
            addCriterion("promotion_discount <=", value, "promotionDiscount");
            return (Criteria) this;
        }

        public Criteria andPromotionDiscountIn(List<Float> values) {
            addCriterion("promotion_discount in", values, "promotionDiscount");
            return (Criteria) this;
        }

        public Criteria andPromotionDiscountNotIn(List<Float> values) {
            addCriterion("promotion_discount not in", values, "promotionDiscount");
            return (Criteria) this;
        }

        public Criteria andPromotionDiscountBetween(Float value1, Float value2) {
            addCriterion("promotion_discount between", value1, value2, "promotionDiscount");
            return (Criteria) this;
        }

        public Criteria andPromotionDiscountNotBetween(Float value1, Float value2) {
            addCriterion("promotion_discount not between", value1, value2, "promotionDiscount");
            return (Criteria) this;
        }

        public Criteria andSalePriceIsNull() {
            addCriterion("sale_price is null");
            return (Criteria) this;
        }

        public Criteria andSalePriceIsNotNull() {
            addCriterion("sale_price is not null");
            return (Criteria) this;
        }

        public Criteria andSalePriceEqualTo(BigDecimal value) {
            addCriterion("sale_price =", value, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceNotEqualTo(BigDecimal value) {
            addCriterion("sale_price <>", value, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceGreaterThan(BigDecimal value) {
            addCriterion("sale_price >", value, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("sale_price >=", value, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceLessThan(BigDecimal value) {
            addCriterion("sale_price <", value, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("sale_price <=", value, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceIn(List<BigDecimal> values) {
            addCriterion("sale_price in", values, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceNotIn(List<BigDecimal> values) {
            addCriterion("sale_price not in", values, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sale_price between", value1, value2, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sale_price not between", value1, value2, "salePrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceIsNull() {
            addCriterion("unit_price is null");
            return (Criteria) this;
        }

        public Criteria andUnitPriceIsNotNull() {
            addCriterion("unit_price is not null");
            return (Criteria) this;
        }

        public Criteria andUnitPriceEqualTo(BigDecimal value) {
            addCriterion("unit_price =", value, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceNotEqualTo(BigDecimal value) {
            addCriterion("unit_price <>", value, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceGreaterThan(BigDecimal value) {
            addCriterion("unit_price >", value, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("unit_price >=", value, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceLessThan(BigDecimal value) {
            addCriterion("unit_price <", value, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("unit_price <=", value, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceIn(List<BigDecimal> values) {
            addCriterion("unit_price in", values, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceNotIn(List<BigDecimal> values) {
            addCriterion("unit_price not in", values, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("unit_price between", value1, value2, "unitPrice");
            return (Criteria) this;
        }

        public Criteria andUnitPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("unit_price not between", value1, value2, "unitPrice");
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

        public Criteria andLogisticsIdIsNull() {
            addCriterion("logistics_id is null");
            return (Criteria) this;
        }

        public Criteria andLogisticsIdIsNotNull() {
            addCriterion("logistics_id is not null");
            return (Criteria) this;
        }

        public Criteria andLogisticsIdEqualTo(String value) {
            addCriterion("logistics_id =", value, "logisticsId");
            return (Criteria) this;
        }

        public Criteria andLogisticsIdNotEqualTo(String value) {
            addCriterion("logistics_id <>", value, "logisticsId");
            return (Criteria) this;
        }

        public Criteria andLogisticsIdGreaterThan(String value) {
            addCriterion("logistics_id >", value, "logisticsId");
            return (Criteria) this;
        }

        public Criteria andLogisticsIdGreaterThanOrEqualTo(String value) {
            addCriterion("logistics_id >=", value, "logisticsId");
            return (Criteria) this;
        }

        public Criteria andLogisticsIdLessThan(String value) {
            addCriterion("logistics_id <", value, "logisticsId");
            return (Criteria) this;
        }

        public Criteria andLogisticsIdLessThanOrEqualTo(String value) {
            addCriterion("logistics_id <=", value, "logisticsId");
            return (Criteria) this;
        }

        public Criteria andLogisticsIdLike(String value) {
            addCriterion("logistics_id like", value, "logisticsId");
            return (Criteria) this;
        }

        public Criteria andLogisticsIdNotLike(String value) {
            addCriterion("logistics_id not like", value, "logisticsId");
            return (Criteria) this;
        }

        public Criteria andLogisticsIdIn(List<String> values) {
            addCriterion("logistics_id in", values, "logisticsId");
            return (Criteria) this;
        }

        public Criteria andLogisticsIdNotIn(List<String> values) {
            addCriterion("logistics_id not in", values, "logisticsId");
            return (Criteria) this;
        }

        public Criteria andLogisticsIdBetween(String value1, String value2) {
            addCriterion("logistics_id between", value1, value2, "logisticsId");
            return (Criteria) this;
        }

        public Criteria andLogisticsIdNotBetween(String value1, String value2) {
            addCriterion("logistics_id not between", value1, value2, "logisticsId");
            return (Criteria) this;
        }

        public Criteria andLogisticsContentIsNull() {
            addCriterion("logistics_content is null");
            return (Criteria) this;
        }

        public Criteria andLogisticsContentIsNotNull() {
            addCriterion("logistics_content is not null");
            return (Criteria) this;
        }

        public Criteria andLogisticsContentEqualTo(String value) {
            addCriterion("logistics_content =", value, "logisticsContent");
            return (Criteria) this;
        }

        public Criteria andLogisticsContentNotEqualTo(String value) {
            addCriterion("logistics_content <>", value, "logisticsContent");
            return (Criteria) this;
        }

        public Criteria andLogisticsContentGreaterThan(String value) {
            addCriterion("logistics_content >", value, "logisticsContent");
            return (Criteria) this;
        }

        public Criteria andLogisticsContentGreaterThanOrEqualTo(String value) {
            addCriterion("logistics_content >=", value, "logisticsContent");
            return (Criteria) this;
        }

        public Criteria andLogisticsContentLessThan(String value) {
            addCriterion("logistics_content <", value, "logisticsContent");
            return (Criteria) this;
        }

        public Criteria andLogisticsContentLessThanOrEqualTo(String value) {
            addCriterion("logistics_content <=", value, "logisticsContent");
            return (Criteria) this;
        }

        public Criteria andLogisticsContentLike(String value) {
            addCriterion("logistics_content like", value, "logisticsContent");
            return (Criteria) this;
        }

        public Criteria andLogisticsContentNotLike(String value) {
            addCriterion("logistics_content not like", value, "logisticsContent");
            return (Criteria) this;
        }

        public Criteria andLogisticsContentIn(List<String> values) {
            addCriterion("logistics_content in", values, "logisticsContent");
            return (Criteria) this;
        }

        public Criteria andLogisticsContentNotIn(List<String> values) {
            addCriterion("logistics_content not in", values, "logisticsContent");
            return (Criteria) this;
        }

        public Criteria andLogisticsContentBetween(String value1, String value2) {
            addCriterion("logistics_content between", value1, value2, "logisticsContent");
            return (Criteria) this;
        }

        public Criteria andLogisticsContentNotBetween(String value1, String value2) {
            addCriterion("logistics_content not between", value1, value2, "logisticsContent");
            return (Criteria) this;
        }

        public Criteria andComcodeIsNull() {
            addCriterion("comcode is null");
            return (Criteria) this;
        }

        public Criteria andComcodeIsNotNull() {
            addCriterion("comcode is not null");
            return (Criteria) this;
        }

        public Criteria andComcodeEqualTo(String value) {
            addCriterion("comcode =", value, "comcode");
            return (Criteria) this;
        }

        public Criteria andComcodeNotEqualTo(String value) {
            addCriterion("comcode <>", value, "comcode");
            return (Criteria) this;
        }

        public Criteria andComcodeGreaterThan(String value) {
            addCriterion("comcode >", value, "comcode");
            return (Criteria) this;
        }

        public Criteria andComcodeGreaterThanOrEqualTo(String value) {
            addCriterion("comcode >=", value, "comcode");
            return (Criteria) this;
        }

        public Criteria andComcodeLessThan(String value) {
            addCriterion("comcode <", value, "comcode");
            return (Criteria) this;
        }

        public Criteria andComcodeLessThanOrEqualTo(String value) {
            addCriterion("comcode <=", value, "comcode");
            return (Criteria) this;
        }

        public Criteria andComcodeLike(String value) {
            addCriterion("comcode like", value, "comcode");
            return (Criteria) this;
        }

        public Criteria andComcodeNotLike(String value) {
            addCriterion("comcode not like", value, "comcode");
            return (Criteria) this;
        }

        public Criteria andComcodeIn(List<String> values) {
            addCriterion("comcode in", values, "comcode");
            return (Criteria) this;
        }

        public Criteria andComcodeNotIn(List<String> values) {
            addCriterion("comcode not in", values, "comcode");
            return (Criteria) this;
        }

        public Criteria andComcodeBetween(String value1, String value2) {
            addCriterion("comcode between", value1, value2, "comcode");
            return (Criteria) this;
        }

        public Criteria andComcodeNotBetween(String value1, String value2) {
            addCriterion("comcode not between", value1, value2, "comcode");
            return (Criteria) this;
        }

        public Criteria andSkuCouponDiscountIsNull() {
            addCriterion("sku_coupon_discount is null");
            return (Criteria) this;
        }

        public Criteria andSkuCouponDiscountIsNotNull() {
            addCriterion("sku_coupon_discount is not null");
            return (Criteria) this;
        }

        public Criteria andSkuCouponDiscountEqualTo(Integer value) {
            addCriterion("sku_coupon_discount =", value, "skuCouponDiscount");
            return (Criteria) this;
        }

        public Criteria andSkuCouponDiscountNotEqualTo(Integer value) {
            addCriterion("sku_coupon_discount <>", value, "skuCouponDiscount");
            return (Criteria) this;
        }

        public Criteria andSkuCouponDiscountGreaterThan(Integer value) {
            addCriterion("sku_coupon_discount >", value, "skuCouponDiscount");
            return (Criteria) this;
        }

        public Criteria andSkuCouponDiscountGreaterThanOrEqualTo(Integer value) {
            addCriterion("sku_coupon_discount >=", value, "skuCouponDiscount");
            return (Criteria) this;
        }

        public Criteria andSkuCouponDiscountLessThan(Integer value) {
            addCriterion("sku_coupon_discount <", value, "skuCouponDiscount");
            return (Criteria) this;
        }

        public Criteria andSkuCouponDiscountLessThanOrEqualTo(Integer value) {
            addCriterion("sku_coupon_discount <=", value, "skuCouponDiscount");
            return (Criteria) this;
        }

        public Criteria andSkuCouponDiscountIn(List<Integer> values) {
            addCriterion("sku_coupon_discount in", values, "skuCouponDiscount");
            return (Criteria) this;
        }

        public Criteria andSkuCouponDiscountNotIn(List<Integer> values) {
            addCriterion("sku_coupon_discount not in", values, "skuCouponDiscount");
            return (Criteria) this;
        }

        public Criteria andSkuCouponDiscountBetween(Integer value1, Integer value2) {
            addCriterion("sku_coupon_discount between", value1, value2, "skuCouponDiscount");
            return (Criteria) this;
        }

        public Criteria andSkuCouponDiscountNotBetween(Integer value1, Integer value2) {
            addCriterion("sku_coupon_discount not between", value1, value2, "skuCouponDiscount");
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

        public Criteria andCompleteTimeIsNull() {
            addCriterion("complete_time is null");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeIsNotNull() {
            addCriterion("complete_time is not null");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeEqualTo(Date value) {
            addCriterion("complete_time =", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeNotEqualTo(Date value) {
            addCriterion("complete_time <>", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeGreaterThan(Date value) {
            addCriterion("complete_time >", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("complete_time >=", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeLessThan(Date value) {
            addCriterion("complete_time <", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeLessThanOrEqualTo(Date value) {
            addCriterion("complete_time <=", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeIn(List<Date> values) {
            addCriterion("complete_time in", values, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeNotIn(List<Date> values) {
            addCriterion("complete_time not in", values, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeBetween(Date value1, Date value2) {
            addCriterion("complete_time between", value1, value2, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeNotBetween(Date value1, Date value2) {
            addCriterion("complete_time not between", value1, value2, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCheckedPriceIsNull() {
            addCriterion("checked_price is null");
            return (Criteria) this;
        }

        public Criteria andCheckedPriceIsNotNull() {
            addCriterion("checked_price is not null");
            return (Criteria) this;
        }

        public Criteria andCheckedPriceEqualTo(BigDecimal value) {
            addCriterion("checked_price =", value, "checkedPrice");
            return (Criteria) this;
        }

        public Criteria andCheckedPriceNotEqualTo(BigDecimal value) {
            addCriterion("checked_price <>", value, "checkedPrice");
            return (Criteria) this;
        }

        public Criteria andCheckedPriceGreaterThan(BigDecimal value) {
            addCriterion("checked_price >", value, "checkedPrice");
            return (Criteria) this;
        }

        public Criteria andCheckedPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("checked_price >=", value, "checkedPrice");
            return (Criteria) this;
        }

        public Criteria andCheckedPriceLessThan(BigDecimal value) {
            addCriterion("checked_price <", value, "checkedPrice");
            return (Criteria) this;
        }

        public Criteria andCheckedPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("checked_price <=", value, "checkedPrice");
            return (Criteria) this;
        }

        public Criteria andCheckedPriceIn(List<BigDecimal> values) {
            addCriterion("checked_price in", values, "checkedPrice");
            return (Criteria) this;
        }

        public Criteria andCheckedPriceNotIn(List<BigDecimal> values) {
            addCriterion("checked_price not in", values, "checkedPrice");
            return (Criteria) this;
        }

        public Criteria andCheckedPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("checked_price between", value1, value2, "checkedPrice");
            return (Criteria) this;
        }

        public Criteria andCheckedPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("checked_price not between", value1, value2, "checkedPrice");
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

        public Criteria andSpriceEqualTo(BigDecimal value) {
            addCriterion("sprice =", value, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceNotEqualTo(BigDecimal value) {
            addCriterion("sprice <>", value, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceGreaterThan(BigDecimal value) {
            addCriterion("sprice >", value, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("sprice >=", value, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceLessThan(BigDecimal value) {
            addCriterion("sprice <", value, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("sprice <=", value, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceIn(List<BigDecimal> values) {
            addCriterion("sprice in", values, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceNotIn(List<BigDecimal> values) {
            addCriterion("sprice not in", values, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sprice between", value1, value2, "sprice");
            return (Criteria) this;
        }

        public Criteria andSpriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sprice not between", value1, value2, "sprice");
            return (Criteria) this;
        }

        public Criteria andProductTypeIsNull() {
            addCriterion("product_type is null");
            return (Criteria) this;
        }

        public Criteria andProductTypeIsNotNull() {
            addCriterion("product_type is not null");
            return (Criteria) this;
        }

        public Criteria andProductTypeEqualTo(Integer value) {
            addCriterion("product_type =", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotEqualTo(Integer value) {
            addCriterion("product_type <>", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeGreaterThan(Integer value) {
            addCriterion("product_type >", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("product_type >=", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeLessThan(Integer value) {
            addCriterion("product_type <", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeLessThanOrEqualTo(Integer value) {
            addCriterion("product_type <=", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeIn(List<Integer> values) {
            addCriterion("product_type in", values, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotIn(List<Integer> values) {
            addCriterion("product_type not in", values, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeBetween(Integer value1, Integer value2) {
            addCriterion("product_type between", value1, value2, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("product_type not between", value1, value2, "productType");
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