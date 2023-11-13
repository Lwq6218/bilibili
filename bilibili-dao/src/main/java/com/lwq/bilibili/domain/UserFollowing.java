package com.lwq.bilibili.domain;

import java.util.Date;

public class UserFollowing {
    private Long userId;
    private Long id;
    private long followingId;
    private Long groupId;
    private Date createTime;
    private UserInfo userInfo;
    public UserInfo getUserInfo() {
        return userInfo;
    }
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getFollowingId() {
        return followingId;
    }
    public void setFollowingId(long followingId) {
        this.followingId = followingId;
    }
    public Long getGroupId() {
        return groupId;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
