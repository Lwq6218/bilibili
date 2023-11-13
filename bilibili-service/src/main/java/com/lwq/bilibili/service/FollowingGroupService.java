package com.lwq.bilibili.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lwq.bilibili.dao.FollowingGroupDao;
import com.lwq.bilibili.domain.FollowingGroup;

@Service
public class FollowingGroupService {
    @Autowired
    private FollowingGroupDao followingGroupDao;

    public FollowingGroup getByType(String type) {
        return followingGroupDao.getByType(type);
    }

    public FollowingGroup getById(Long id) {
        return followingGroupDao.getById(id);
    }

    public List<FollowingGroup> getFollowingGroupByUserId(Long userId) {
        return followingGroupDao.getByUserId(userId);
    }

    public void addFollowingGroups(FollowingGroup followingGroup) {
        followingGroupDao.addFollowingGroups(followingGroup);
    }

    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
        return followingGroupDao.getUserFollowingGroups(userId);
    }
}
