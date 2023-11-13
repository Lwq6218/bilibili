package com.lwq.bilibili.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;

import com.lwq.bilibili.domain.User;
import com.lwq.bilibili.domain.UserInfo;

@Mapper
public interface UserDao {

    User getUserByPhone(String phone);

    Integer addUser(User user);

    Integer addUserInfo(UserInfo userInfo);

    User getUserById(Long id);

	UserInfo getUserInfoByUserId(Long userId);

    Integer updateUsers(User user);

    Integer updateUserInfos(UserInfo userInfo);

    List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList);

}
