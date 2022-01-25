package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("foo_bar")
public class FooBar {

    private String name;

    private String nickName;

    @Override
    public String toString() {
        return "FooBar{" +
                "name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
