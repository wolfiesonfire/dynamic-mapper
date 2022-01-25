package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("bar")
public class Bar {

    private String firstName;

    private String lastName;

    @Override
    public String toString() {
        return "Bar{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
