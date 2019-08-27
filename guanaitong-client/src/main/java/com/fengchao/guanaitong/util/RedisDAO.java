package com.fengchao.guanaitong.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisDAO {

    @Autowired
    private StringRedisTemplate template;

    public  void setValue(String key,String value, int expire){
        ValueOperations<String, String> ops = template.opsForValue();
        ops.set(key,value,expire, TimeUnit.SECONDS);
    }

    public String getValue(String key){
        ValueOperations<String, String> ops = this.template.opsForValue();
        return ops.get(key);
    }

    public Long ttl(String key) {
        return this.template.getExpire(key);
    }
}
