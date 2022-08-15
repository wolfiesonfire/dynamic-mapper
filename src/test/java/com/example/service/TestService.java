package com.example.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    private BaseMapper<Foo> fooMapper;

    @Resource
    private BaseMapper<Bar> barMapper;

    @Resource
    private FooBarService fooBarService;

    @Resource
    private ServiceImpl<BaseMapper<Foo>, Foo> fooService;

    @Resource
    private ServiceImpl<BaseMapper<Bar>, Bar> barService;

    @Test
    public void testService() {

        List<FooBar> fooBarList = fooBarService.lambdaQuery().list();
        log.info("foo_bar: {}", fooBarList);

        List<Foo> fooList = fooService.lambdaQuery().list();
        log.info("foo: {}", fooList);

        List<Bar> barList = barService.lambdaQuery().list();
        log.info("bar: {}", barList);

    }


    @Test
    public void testMapper() {

        List<FooBar> fooBarList = fooBarMapper.selectList(null);
        log.info("foo_bar: {}", fooBarList);

        List<Foo> fooList = fooMapper.selectList(null);
        log.info("foo: {}", fooList);

        List<Bar> barList = barMapper.selectList(null);
        log.info("bar: {}", barList);

    }


}