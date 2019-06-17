package com.fengchao.equity.db.annotation;

import com.fengchao.equity.db.config.DataSourceNames;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    String value() default DataSourceNames.ONE;
}
