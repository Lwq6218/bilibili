package com.lwq.bilibili.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DemoDao {
    String query(String name);
}

