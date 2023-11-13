package com.lwq.bilibili.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lwq.bilibili.dao.UserFollowingDao;
import com.lwq.bilibili.domain.FollowingGroup;
import com.lwq.bilibili.domain.User;
import com.lwq.bilibili.domain.UserFollowing;
import com.lwq.bilibili.domain.UserInfo;
import com.lwq.bilibili.domain.constant.UserConstant;
import com.lwq.bilibili.domain.exception.ConditionException;

@Service
public class UserFollowingService {
    @Autowired
    private UserFollowingDao userFollowingDao;

    @Autowired
    private FollowingGroupService followingGroupService;

    @Autowired
    private UserService userService;

    @Transactional
    public void addUserFollowing(UserFollowing userFollowing) {
        Long groupId = userFollowing.getGroupId();
        if (groupId == null) {
            FollowingGroup followingGroup = followingGroupService
                    .getByType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
            userFollowing.setGroupId(followingGroup.getId());
        } else {
            FollowingGroup followingGroup = followingGroupService.getById(groupId);
            if (followingGroup == null) {
                throw new ConditionException("分组不存在");
            }
        }
        Long followingId = userFollowing.getFollowingId();
        User user = userService.getUserById(followingId);
        if (user == null) {
            throw new ConditionException("关注的用户不存在");
        }
        userFollowingDao.deleteUserFollowing(userFollowing.getUserId(), userFollowing.getFollowingId());
        userFollowing.setCreateTime(new Date());
        userFollowingDao.addUserFollowing(userFollowing);

    }

    public List<FollowingGroup> getUserFollowings(Long userId) {
        List<UserFollowing> list = userFollowingDao.getUserFollowings(userId);
        Set<Long> followingIdSet = list.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if (!followingIdSet.isEmpty()) {
            userInfoList = userService.getUserInfoByUserIds(followingIdSet);
        }
        for (UserFollowing userFollowing : list) {
            for (UserInfo userInfo : userInfoList) {
                if (userFollowing.getFollowingId().equals(userInfo.getUserId())) {
                    userFollowing.setUserInfo(userInfo);
                }
            }
        }
        List<FollowingGroup> groupList = followingGroupService.getFollowingGroupByUserId(userId);
        FollowingGroup allGroup = new FollowingGroup();
        allGroup.setName(UserConstant.USER_FOLLOWING_GROUUP_ALL_NAME);
        allGroup.setFollowingUserInfoList(userInfoList);
        List<FollowingGroup> result = new ArrayList<>();
        result.add(allGroup);
        for (FollowingGroup followingGroup : groupList) {
            List<UserInfo> infoList = new ArrayList<>();
            for (UserFollowing userFollowing : list) {
                if (userFollowing.getGroupId().equals(followingGroup.getId())) {
                    infoList.add(userFollowing.getUserInfo());
                }
            }
            followingGroup.setFollowingUserInfoList(infoList);
            result.add(followingGroup);
        }
        return result;
    }

    public List<UserFollowing> getUserFans(Long userId) {
        List<UserFollowing> fansList = userFollowingDao.getUserFans(userId);
        Set<Long> fanIdSet = fansList.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if (!fanIdSet.isEmpty()) {
            userInfoList = userService.getUserInfoByUserIds(fanIdSet);
        }
        List<UserFollowing> userFollowingList = userFollowingDao.getUserFollowings(userId);
        for (UserFollowing fan : fansList) {
            for (UserInfo userInfo : userInfoList) {
                if (fan.getUserId().equals(userInfo.getUserId())) {
                    userInfo.setFollowed(true);
                    fan.setUserInfo(userInfo);
                }
            }
            for (UserFollowing following : userFollowingList) {
                if (fan.getUserId().equals(following.getFollowingId())) {
                    fan.getUserInfo().setFollowed(true);
                }
            }
        }
        return fansList;
    }

    public Long addUserFollowingGroups(FollowingGroup followingGroup) {
    followingGroup.setCreateTime(new Date());
    followingGroup.setType(UserConstant.USER_FOLLOWING_GROUP_TYPE_USER);
    followingGroupService.addFollowingGroups(followingGroup);
    return followingGroup.getId();
    }

    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
    return followingGroupService.getUserFollowingGroups(userId);
    }

    public List<UserInfo> checkFollowingStatus(List<UserInfo> userInfolist, Long userId) {
        List<UserFollowing> userFollowingList = userFollowingDao.getUserFollowings(userId);
        for(UserInfo userInfo:userInfolist){
            for(UserFollowing userFollowing:userFollowingList){
                if(userInfo.getUserId().equals(userFollowing.getFollowingId())){
                    userInfo.setFollowed(true);
                }
            }
        }
        return userInfolist;
    }
}
