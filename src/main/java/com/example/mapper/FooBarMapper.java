package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.FooBar;
import org.apache.ibatis.annotations.Mapper;

/**
 * 一个已经定义好的 mapper
 */
@Mapper
public interface FooBarMapper extends BaseMapper<FooBar> {
}
