package com.lwq.bilibili.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lwq.bilibili.dao.UserDao;
import com.lwq.bilibili.domain.PageResult;
import com.lwq.bilibili.domain.User;
import com.lwq.bilibili.domain.UserInfo;
import com.lwq.bilibili.domain.constant.UserConstant;
import com.lwq.bilibili.domain.exception.ConditionException;
import com.lwq.bilibili.service.util.MD5Util;
import com.lwq.bilibili.service.util.RSAUtil;
import com.lwq.bilibili.service.util.TokenUtil;
import com.mysql.cj.util.StringUtils;

@Service
public class UserService {
    @Autowired
    public UserDao userDao;

    public void addUser(User user) {
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空");
        }
        if (getUserByPhone(phone) != null) {
            throw new ConditionException("手机号已被注册");
        }
        String password = user.getPassword();
        String rawPassword = null;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解析失败");
        }
        Date now = new Date();
        String salt = String.valueOf(now.getTime());
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreateTime(now);
        userDao.addUser(user);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setNick(UserConstant.NICK_DEFUALT);
        userInfo.setBirth(UserConstant.BIRTH_DEAULT);
        userDao.addUserInfo(userInfo);
    }

    public String login(User user) throws IllegalArgumentException, Exception {
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空");
        }
        User userDB = getUserByPhone(phone);
        if (userDB == null) {
            throw new ConditionException("手机号未注册");
        }
        String password = user.getPassword();
        String rawPassword = null;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解析失败");
        }
        String salt = user.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if (!md5Password.equals(userDB.getPassword())) {
            throw new ConditionException("密码错误");
        }

        return TokenUtil.generateToken(userDB.getId());
    }

    public User getUserInfo(Long userId) {
        User user = userDao.getUserById(userId);
        UserInfo userInfo = userDao.getUserInfoByUserId(userId);
        ;
        user.setUserInfo(userInfo);
        return user;
    }

    public void updateUsers(User user) {
        Long id = user.getId();
        User userDB = userDao.getUserById(id);
        if (userDB == null) {
            throw new ConditionException("用户不存在");
        }
        if (!StringUtils.isNullOrEmpty(user.getPassword())) {
            String password = user.getPassword();
            String rawPassword = null;
            try {
                rawPassword = RSAUtil.decrypt(password);
            } catch (Exception e) {
                throw new ConditionException("密码解析失败");
            }
            String salt = user.getSalt();
            String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
            user.setPassword(md5Password);
            userDao.updateUsers(user);
        }

    }

    public void updateUserInfos(UserInfo userInfo) {
        userInfo.setUpdateTime(new Date());
        userDao.updateUserInfos(userInfo);
    }

    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    public List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList) {
        return userDao.getUserInfoByUserIds(userIdList);
    }

    private User getUserByPhone(String phone) {
        return userDao.getUserByPhone(phone);
    }

    public PageResult<UserInfo> getUserInfos(JSONObject params) {
        Integer no = params.getInteger("no");
        Integer size = params.getInteger("size");
        params.put("star", (no - 1) * size);
        params.put("limit", size);
        Integer total = userDao.pageCountUserInfos(params);
        List<UserInfo> list = new ArrayList<>();
        if (total > 0) {
            list = userDao.pageListUserInfos(params);
        }
        return new PageResult<>(total, list);
    }

}
