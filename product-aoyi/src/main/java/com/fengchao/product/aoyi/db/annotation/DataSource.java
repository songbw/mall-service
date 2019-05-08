package com.fengchao.product.aoyi.db.annotation;

import com.fengchao.product.aoyi.db.config.DataSourceNames;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    String value() default DataSourceNames.ONE;
}
