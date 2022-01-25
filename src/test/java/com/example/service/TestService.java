package com.example.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Bar;
import com.example.entity.Foo;
import com.example.entity.FooBar;
import com.example.mapper.FooBarMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestService {

    private final static Logger log = LoggerFactory.getLogger(TestService.class);

    @Resource
    private FooBarMapper fooBarMapper;

    @Resource
    private BaseMapper<?> fooMapper;

    @Resource
    private BaseMapper<?> barMapper;


    @Test
    public void test() {
        List<FooBar> fooBarList = fooBarMapper.selectList(null);
        List<Foo> fooList = (List<Foo>) fooMapper.selectList(null);
        List<Bar> barList = (List<Bar>) barMapper.selectList(null);
        log.info("foo_bar: {}", fooBarList);
        log.info("foo: {}", fooList);
        log.info("bar: {}", barList);
    }


}