package com.zhishu.samples.mapper;

import com.zhishu.common.dto.Page;
import com.zhishu.samples.entitys.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LogMapperTest {

    @Autowired
    private LogMapper logMapper;
    @Test
    public void findAll() {
        Page<Log> logPage = new Page<>();
        logMapper.findAll(logPage);
        System.out.println(logPage);
    }
}