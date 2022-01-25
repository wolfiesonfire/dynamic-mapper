package com.example.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.FooBar;
import com.example.mapper.FooBarMapper;
import org.springframework.stereotype.Service;

/**
 * 一个已经定义好的 service
 */
@Service
public class FooBarService extends ServiceImpl<FooBarMapper, FooBar> {
}