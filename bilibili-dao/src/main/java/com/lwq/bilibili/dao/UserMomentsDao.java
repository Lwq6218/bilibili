package com.lwq.bilibili.dao;

import org.apache.ibatis.annotations.Mapper;

import com.lwq.bilibili.domain.UserMoment;

@Mapper 
public interface UserMomentsDao {

    Integer addUserMonments(UserMoment userMoment);
    
}
