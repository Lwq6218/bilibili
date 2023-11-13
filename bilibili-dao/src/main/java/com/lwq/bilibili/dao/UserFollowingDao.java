package com.lwq.bilibili.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lwq.bilibili.domain.UserFollowing;

@Mapper
public interface UserFollowingDao {

    Integer deleteUserFollowing(@Param("userId") Long userId, @Param("followingId") long followingId);

    Integer addUserFollowing(UserFollowing userFollowing);

    List<UserFollowing> getUserFollowings(Long userId);

    List<UserFollowing> getUserFans(Long userId);

}
