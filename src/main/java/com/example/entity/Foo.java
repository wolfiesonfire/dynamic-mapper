package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("foo")
public class Foo {

    private String orderNo;

    private String invoiceNo;

    @Override
    public String toString() {
        return "Foo{" +
                "orderNo='" + orderNo + '\'' +
                ", invoiceNo='" + invoiceNo + '\'' +
                '}';
    }
}
