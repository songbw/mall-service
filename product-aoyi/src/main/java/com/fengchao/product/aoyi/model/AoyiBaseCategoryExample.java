package com.fengchao.product.aoyi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AoyiBaseCategoryExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AoyiBaseCategoryExample() {
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

        public Criteria andCategoryNameIsNull() {
            addCriterion("category_name is null");
            return (Criteria) this;
        }

        public Criteria andCategoryNameIsNotNull() {
            addCriterion("category_name is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryNameEqualTo(String value) {
            addCriterion("category_name =", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameNotEqualTo(String value) {
            addCriterion("category_name <>", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameGreaterThan(String value) {
            addCriterion("category_name >", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameGreaterThanOrEqualTo(String value) {
            addCriterion("category_name >=", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameLessThan(String value) {
            addCriterion("category_name <", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameLessThanOrEqualTo(String value) {
            addCriterion("category_name <=", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameLike(String value) {
            addCriterion("category_name like", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameNotLike(String value) {
            addCriterion("category_name not like", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameIn(List<String> values) {
            addCriterion("category_name in", values, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameNotIn(List<String> values) {
            addCriterion("category_name not in", values, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameBetween(String value1, String value2) {
            addCriterion("category_name between", value1, value2, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameNotBetween(String value1, String value2) {
            addCriterion("category_name not between", value1, value2, "categoryName");
            return (Criteria) this;
        }

        public Criteria andParentIdIsNull() {
            addCriterion("parent_id is null");
            return (Criteria) this;
        }

        public Criteria andParentIdIsNotNull() {
            addCriterion("parent_id is not null");
            return (Criteria) this;
        }

        public Criteria andParentIdEqualTo(Integer value) {
            addCriterion("parent_id =", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotEqualTo(Integer value) {
            addCriterion("parent_id <>", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThan(Integer value) {
            addCriterion("parent_id >", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("parent_id >=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThan(Integer value) {
            addCriterion("parent_id <", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThanOrEqualTo(Integer value) {
            addCriterion("parent_id <=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdIn(List<Integer> values) {
            addCriterion("parent_id in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotIn(List<Integer> values) {
            addCriterion("parent_id not in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdBetween(Integer value1, Integer value2) {
            addCriterion("parent_id between", value1, value2, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotBetween(Integer value1, Integer value2) {
            addCriterion("parent_id not between", value1, value2, "parentId");
            return (Criteria) this;
        }

        public Criteria andCategoryClassIsNull() {
            addCriterion("category_class is null");
            return (Criteria) this;
        }

        public Criteria andCategoryClassIsNotNull() {
            addCriterion("category_class is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryClassEqualTo(String value) {
            addCriterion("category_class =", value, "categoryClass");
            return (Criteria) this;
        }

        public Criteria andCategoryClassNotEqualTo(String value) {
            addCriterion("category_class <>", value, "categoryClass");
            return (Criteria) this;
        }

        public Criteria andCategoryClassGreaterThan(String value) {
            addCriterion("category_class >", value, "categoryClass");
            return (Criteria) this;
        }

        public Criteria andCategoryClassGreaterThanOrEqualTo(String value) {
            addCriterion("category_class >=", value, "categoryClass");
            return (Criteria) this;
        }

        public Criteria andCategoryClassLessThan(String value) {
            addCriterion("category_class <", value, "categoryClass");
            return (Criteria) this;
        }

        public Criteria andCategoryClassLessThanOrEqualTo(String value) {
            addCriterion("category_class <=", value, "categoryClass");
            return (Criteria) this;
        }

        public Criteria andCategoryClassLike(String value) {
            addCriterion("category_class like", value, "categoryClass");
            return (Criteria) this;
        }

        public Criteria andCategoryClassNotLike(String value) {
            addCriterion("category_class not like", value, "categoryClass");
            return (Criteria) this;
        }

        public Criteria andCategoryClassIn(List<String> values) {
            addCriterion("category_class in", values, "categoryClass");
            return (Criteria) this;
        }

        public Criteria andCategoryClassNotIn(List<String> values) {
            addCriterion("category_class not in", values, "categoryClass");
            return (Criteria) this;
        }

        public Criteria andCategoryClassBetween(String value1, String value2) {
            addCriterion("category_class between", value1, value2, "categoryClass");
            return (Criteria) this;
        }

        public Criteria andCategoryClassNotBetween(String value1, String value2) {
            addCriterion("category_class not between", value1, value2, "categoryClass");
            return (Criteria) this;
        }

        public Criteria andCategoryIconIsNull() {
            addCriterion("category_icon is null");
            return (Criteria) this;
        }

        public Criteria andCategoryIconIsNotNull() {
            addCriterion("category_icon is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryIconEqualTo(String value) {
            addCriterion("category_icon =", value, "categoryIcon");
            return (Criteria) this;
        }

        public Criteria andCategoryIconNotEqualTo(String value) {
            addCriterion("category_icon <>", value, "categoryIcon");
            return (Criteria) this;
        }

        public Criteria andCategoryIconGreaterThan(String value) {
            addCriterion("category_icon >", value, "categoryIcon");
            return (Criteria) this;
        }

        public Criteria andCategoryIconGreaterThanOrEqualTo(String value) {
            addCriterion("category_icon >=", value, "categoryIcon");
            return (Criteria) this;
        }

        public Criteria andCategoryIconLessThan(String value) {
            addCriterion("category_icon <", value, "categoryIcon");
            return (Criteria) this;
        }

        public Criteria andCategoryIconLessThanOrEqualTo(String value) {
            addCriterion("category_icon <=", value, "categoryIcon");
            return (Criteria) this;
        }

        public Criteria andCategoryIconLike(String value) {
            addCriterion("category_icon like", value, "categoryIcon");
            return (Criteria) this;
        }

        public Criteria andCategoryIconNotLike(String value) {
            addCriterion("category_icon not like", value, "categoryIcon");
            return (Criteria) this;
        }

        public Criteria andCategoryIconIn(List<String> values) {
            addCriterion("category_icon in", values, "categoryIcon");
            return (Criteria) this;
        }

        public Criteria andCategoryIconNotIn(List<String> values) {
            addCriterion("category_icon not in", values, "categoryIcon");
            return (Criteria) this;
        }

        public Criteria andCategoryIconBetween(String value1, String value2) {
            addCriterion("category_icon between", value1, value2, "categoryIcon");
            return (Criteria) this;
        }

        public Criteria andCategoryIconNotBetween(String value1, String value2) {
            addCriterion("category_icon not between", value1, value2, "categoryIcon");
            return (Criteria) this;
        }

        public Criteria andCategoryDescIsNull() {
            addCriterion("category_desc is null");
            return (Criteria) this;
        }

        public Criteria andCategoryDescIsNotNull() {
            addCriterion("category_desc is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryDescEqualTo(String value) {
            addCriterion("category_desc =", value, "categoryDesc");
            return (Criteria) this;
        }

        public Criteria andCategoryDescNotEqualTo(String value) {
            addCriterion("category_desc <>", value, "categoryDesc");
            return (Criteria) this;
        }

        public Criteria andCategoryDescGreaterThan(String value) {
            addCriterion("category_desc >", value, "categoryDesc");
            return (Criteria) this;
        }

        public Criteria andCategoryDescGreaterThanOrEqualTo(String value) {
            addCriterion("category_desc >=", value, "categoryDesc");
            return (Criteria) this;
        }

        public Criteria andCategoryDescLessThan(String value) {
            addCriterion("category_desc <", value, "categoryDesc");
            return (Criteria) this;
        }

        public Criteria andCategoryDescLessThanOrEqualTo(String value) {
            addCriterion("category_desc <=", value, "categoryDesc");
            return (Criteria) this;
        }

        public Criteria andCategoryDescLike(String value) {
            addCriterion("category_desc like", value, "categoryDesc");
            return (Criteria) this;
        }

        public Criteria andCategoryDescNotLike(String value) {
            addCriterion("category_desc not like", value, "categoryDesc");
            return (Criteria) this;
        }

        public Criteria andCategoryDescIn(List<String> values) {
            addCriterion("category_desc in", values, "categoryDesc");
            return (Criteria) this;
        }

        public Criteria andCategoryDescNotIn(List<String> values) {
            addCriterion("category_desc not in", values, "categoryDesc");
            return (Criteria) this;
        }

        public Criteria andCategoryDescBetween(String value1, String value2) {
            addCriterion("category_desc between", value1, value2, "categoryDesc");
            return (Criteria) this;
        }

        public Criteria andCategoryDescNotBetween(String value1, String value2) {
            addCriterion("category_desc not between", value1, value2, "categoryDesc");
            return (Criteria) this;
        }

        public Criteria andSortOrderIsNull() {
            addCriterion("sort_order is null");
            return (Criteria) this;
        }

        public Criteria andSortOrderIsNotNull() {
            addCriterion("sort_order is not null");
            return (Criteria) this;
        }

        public Criteria andSortOrderEqualTo(Integer value) {
            addCriterion("sort_order =", value, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderNotEqualTo(Integer value) {
            addCriterion("sort_order <>", value, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderGreaterThan(Integer value) {
            addCriterion("sort_order >", value, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderGreaterThanOrEqualTo(Integer value) {
            addCriterion("sort_order >=", value, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderLessThan(Integer value) {
            addCriterion("sort_order <", value, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderLessThanOrEqualTo(Integer value) {
            addCriterion("sort_order <=", value, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderIn(List<Integer> values) {
            addCriterion("sort_order in", values, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderNotIn(List<Integer> values) {
            addCriterion("sort_order not in", values, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderBetween(Integer value1, Integer value2) {
            addCriterion("sort_order between", value1, value2, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andSortOrderNotBetween(Integer value1, Integer value2) {
            addCriterion("sort_order not between", value1, value2, "sortOrder");
            return (Criteria) this;
        }

        public Criteria andIsShowIsNull() {
            addCriterion("is_show is null");
            return (Criteria) this;
        }

        public Criteria andIsShowIsNotNull() {
            addCriterion("is_show is not null");
            return (Criteria) this;
        }

        public Criteria andIsShowEqualTo(Boolean value) {
            addCriterion("is_show =", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowNotEqualTo(Boolean value) {
            addCriterion("is_show <>", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowGreaterThan(Boolean value) {
            addCriterion("is_show >", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_show >=", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowLessThan(Boolean value) {
            addCriterion("is_show <", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowLessThanOrEqualTo(Boolean value) {
            addCriterion("is_show <=", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowIn(List<Boolean> values) {
            addCriterion("is_show in", values, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowNotIn(List<Boolean> values) {
            addCriterion("is_show not in", values, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowBetween(Boolean value1, Boolean value2) {
            addCriterion("is_show between", value1, value2, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_show not between", value1, value2, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsNavIsNull() {
            addCriterion("is_nav is null");
            return (Criteria) this;
        }

        public Criteria andIsNavIsNotNull() {
            addCriterion("is_nav is not null");
            return (Criteria) this;
        }

        public Criteria andIsNavEqualTo(Boolean value) {
            addCriterion("is_nav =", value, "isNav");
            return (Criteria) this;
        }

        public Criteria andIsNavNotEqualTo(Boolean value) {
            addCriterion("is_nav <>", value, "isNav");
            return (Criteria) this;
        }

        public Criteria andIsNavGreaterThan(Boolean value) {
            addCriterion("is_nav >", value, "isNav");
            return (Criteria) this;
        }

        public Criteria andIsNavGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_nav >=", value, "isNav");
            return (Criteria) this;
        }

        public Criteria andIsNavLessThan(Boolean value) {
            addCriterion("is_nav <", value, "isNav");
            return (Criteria) this;
        }

        public Criteria andIsNavLessThanOrEqualTo(Boolean value) {
            addCriterion("is_nav <=", value, "isNav");
            return (Criteria) this;
        }

        public Criteria andIsNavIn(List<Boolean> values) {
            addCriterion("is_nav in", values, "isNav");
            return (Criteria) this;
        }

        public Criteria andIsNavNotIn(List<Boolean> values) {
            addCriterion("is_nav not in", values, "isNav");
            return (Criteria) this;
        }

        public Criteria andIsNavBetween(Boolean value1, Boolean value2) {
            addCriterion("is_nav between", value1, value2, "isNav");
            return (Criteria) this;
        }

        public Criteria andIsNavNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_nav not between", value1, value2, "isNav");
            return (Criteria) this;
        }

        public Criteria andIsTopStyleIsNull() {
            addCriterion("is_top_style is null");
            return (Criteria) this;
        }

        public Criteria andIsTopStyleIsNotNull() {
            addCriterion("is_top_style is not null");
            return (Criteria) this;
        }

        public Criteria andIsTopStyleEqualTo(Byte value) {
            addCriterion("is_top_style =", value, "isTopStyle");
            return (Criteria) this;
        }

        public Criteria andIsTopStyleNotEqualTo(Byte value) {
            addCriterion("is_top_style <>", value, "isTopStyle");
            return (Criteria) this;
        }

        public Criteria andIsTopStyleGreaterThan(Byte value) {
            addCriterion("is_top_style >", value, "isTopStyle");
            return (Criteria) this;
        }

        public Criteria andIsTopStyleGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_top_style >=", value, "isTopStyle");
            return (Criteria) this;
        }

        public Criteria andIsTopStyleLessThan(Byte value) {
            addCriterion("is_top_style <", value, "isTopStyle");
            return (Criteria) this;
        }

        public Criteria andIsTopStyleLessThanOrEqualTo(Byte value) {
            addCriterion("is_top_style <=", value, "isTopStyle");
            return (Criteria) this;
        }

        public Criteria andIsTopStyleIn(List<Byte> values) {
            addCriterion("is_top_style in", values, "isTopStyle");
            return (Criteria) this;
        }

        public Criteria andIsTopStyleNotIn(List<Byte> values) {
            addCriterion("is_top_style not in", values, "isTopStyle");
            return (Criteria) this;
        }

        public Criteria andIsTopStyleBetween(Byte value1, Byte value2) {
            addCriterion("is_top_style between", value1, value2, "isTopStyle");
            return (Criteria) this;
        }

        public Criteria andIsTopStyleNotBetween(Byte value1, Byte value2) {
            addCriterion("is_top_style not between", value1, value2, "isTopStyle");
            return (Criteria) this;
        }

        public Criteria andIdateIsNull() {
            addCriterion("idate is null");
            return (Criteria) this;
        }

        public Criteria andIdateIsNotNull() {
            addCriterion("idate is not null");
            return (Criteria) this;
        }

        public Criteria andIdateEqualTo(Date value) {
            addCriterion("idate =", value, "idate");
            return (Criteria) this;
        }

        public Criteria andIdateNotEqualTo(Date value) {
            addCriterion("idate <>", value, "idate");
            return (Criteria) this;
        }

        public Criteria andIdateGreaterThan(Date value) {
            addCriterion("idate >", value, "idate");
            return (Criteria) this;
        }

        public Criteria andIdateGreaterThanOrEqualTo(Date value) {
            addCriterion("idate >=", value, "idate");
            return (Criteria) this;
        }

        public Criteria andIdateLessThan(Date value) {
            addCriterion("idate <", value, "idate");
            return (Criteria) this;
        }

        public Criteria andIdateLessThanOrEqualTo(Date value) {
            addCriterion("idate <=", value, "idate");
            return (Criteria) this;
        }

        public Criteria andIdateIn(List<Date> values) {
            addCriterion("idate in", values, "idate");
            return (Criteria) this;
        }

        public Criteria andIdateNotIn(List<Date> values) {
            addCriterion("idate not in", values, "idate");
            return (Criteria) this;
        }

        public Criteria andIdateBetween(Date value1, Date value2) {
            addCriterion("idate between", value1, value2, "idate");
            return (Criteria) this;
        }

        public Criteria andIdateNotBetween(Date value1, Date value2) {
            addCriterion("idate not between", value1, value2, "idate");
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