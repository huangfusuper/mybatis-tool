package com.zhishu.samples.mappers;


import com.zhishu.common.dto.Page;
import com.zhishu.samples.entitys.TestEntity;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author huangfu
 * @date 2020年8月5日17:53:11
 * 测试分页插件是否能用
 */
public class TestMapperTest {
    SqlSessionFactory sqlSessionFactory = null;


    @Before
    public void init() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //构建会话工厂
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void findAll() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        TestMapper mapper = sqlSession.getMapper(TestMapper.class);
        Page<TestEntity> testEntityPage = new Page<>();
        testEntityPage.setPageNo(2);
        mapper.findAll(testEntityPage);
        System.out.println(testEntityPage);
        Page<TestEntity> testEntityPage2 = new Page<>();
        testEntityPage2.setPageNo(2);
        System.out.println(mapper.findAll(testEntityPage2));
        System.out.println(testEntityPage2);
    }

    @Test
    public void findAllById() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        TestMapper mapper = sqlSession.getMapper(TestMapper.class);
        Page<TestEntity> testEntityPage = new Page<>();
        mapper.findAllById(testEntityPage,5);
        System.out.println(testEntityPage);
    }
}