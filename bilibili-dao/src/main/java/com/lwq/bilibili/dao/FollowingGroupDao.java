package com.lwq.bilibili.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.lwq.bilibili.domain.FollowingGroup;

@Mapper
public interface FollowingGroupDao {

    FollowingGroup getByType(String type);

    FollowingGroup getById(Long id);

    List<FollowingGroup> getByUserId(Long userId);

    Integer addFollowingGroups(FollowingGroup followingGroup);

    List<FollowingGroup> getUserFollowingGroups(Long userId);
    
}
