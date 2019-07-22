package com.fengchao.guanaitong.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

@Component
public class RedisUtil {

    public static  int webexpire = Config.getInt("redis.webexpire");
    public static  int appexpire = Config.getInt("redis.appexpire");
    private static String redisIp = Config.getString("redis.ip");
    private static int redisPort = Config.getInt("redis.port");
    private static int active = Config.getInt("redis.maxactive");
    private static int idle = Config.getInt("redis.maxidle");
    private static int wait = Config.getInt("redis.maxwait");


    public static JedisPool pool;
    /**
     * 初始化Redis连接池
     */
    private static JedisPool getJedisPool(){
        if(pool == null ){
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxActive(active);
            config.setMaxIdle(idle);
            config.setMaxWait(wait);
            config.setTestOnBorrow(true);
            pool = new JedisPool(config, redisIp,redisPort);

        }
        return pool;
    }

    public static void returnResource(JedisPool pool, Jedis redis) {
        if (redis != null) {
            pool.returnResource(redis);
        }
    }

    public static void putRedis(String key, String token, int expire){
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            //logger.info("redisIp:"+redisIp+" key="+key+", value="+token);
            pool =getJedisPool();
            jedis = pool.getResource();
            jedis.setex(key, expire, token);
            Long ttl = jedis.ttl(key);
            System.out.println("token缓存成功！剩余时间="+ttl+"秒");
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            throw e;
        }finally{
            returnResource(pool, jedis);
        }
    }

    public static void expire(String openId, int expire){
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis=pool.getResource();
            jedis.expire(openId, expire);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            throw e;
        }finally{
            returnResource(pool, jedis);
        }
    }

    public static long ttl(String key){
        JedisPool pool = null;
        Jedis jedis = null;
        if("".equals(key) || key==null) return -1;
        try {
            pool = getJedisPool();
            jedis=pool.getResource();
            Long ttl = jedis.ttl(key);
            return ttl;
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            throw e;
        }finally{
            returnResource(pool, jedis);
        }
    }

    public static boolean exist(String key) {
        JedisPool pool = null;
        Jedis jedis = null;
        boolean flag = false;
        if ("".equals(key) || key == null) {
            return flag;
        }
        try {
            pool = getJedisPool();
            jedis = pool.getResource();
            flag = jedis.exists(key.getBytes());
            return flag;
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            throw e;
        }finally{
            returnResource(pool, jedis);
        }
    }


    public static String getValue(String key) {
        JedisPool pool = null;
        Jedis jedis = null;
        String value = "";
        if ("".equals(key) || key == null) {
            return value;
        }
        try {
            pool = getJedisPool();
            jedis = pool.getResource();
            value = jedis.get(key);
            return value;
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            throw e;
        }finally{
            returnResource(pool, jedis);
        }
    }

    public static long removeValue(String key) {
        JedisPool pool = null;
        Jedis jedis = null;
        long value = 0l;
        if ("".equals(key) || key == null) {
            return value;
        }
        try {
            pool = getJedisPool();
            jedis = pool.getResource();
            value = jedis.del(key) ;
            return value;
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            throw e;
        }finally{
            returnResource(pool, jedis);
        }
    }

    public static String getKey(String value) {
        JedisPool pool = null;
        Jedis jedis = null;
        String key = null;
        if ("".equals(value) || value == null) {
            return key;
        }
        try {
            pool = getJedisPool();
            jedis = pool.getResource();
            Set<String> keys = jedis.keys("*");
            for (String k:keys){
                if(value.equals(jedis.get(k))){
                    key = k;
                }
            }
            return key;
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            throw e;
        }finally{
            returnResource(pool, jedis);
        }
    }

}

