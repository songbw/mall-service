package com.fengchao.product.aoyi.utils;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author songbw
 * @date 2019/10/31 13:34
 */
@Repository
public class MyBatisUtil {

//    private static final String configFile = "mybatis-config.xml";

    @Autowired
    private SqlSessionTemplate template;

    /**
     * 创建连接
     */
    public SqlSession getSession() {
        SqlSession session = null;
        try {
//            InputStream is = Resources.getResourceAsStream(configFile);
//            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
            SqlSessionFactory factory = template.getSqlSessionFactory();
            session = factory.openSession();
            session.getConnection().setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }

    public void closeSession(SqlSession session) {
        session.close();
    }
}
