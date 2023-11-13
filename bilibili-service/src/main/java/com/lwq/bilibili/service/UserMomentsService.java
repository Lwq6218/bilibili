package com.lwq.bilibili.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lwq.bilibili.dao.UserMomentsDao;
import com.lwq.bilibili.domain.UserMoment;
import com.lwq.bilibili.domain.constant.UserMomentsConstant;
import com.lwq.bilibili.service.util.RocketMQUtil;

@Service
public class UserMomentsService {
    @Autowired
    private UserMomentsDao userMomentsDao;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RedisTemplate redisTemplate;

    public void addUserMoments(UserMoment userMoment) throws Exception {
        userMoment.setCreateTime(new Date());
        userMomentsDao.addUserMonments(userMoment);
        DefaultMQProducer producer = (DefaultMQProducer) applicationContext.getBean("MomentsProducer");
        Message msg = new Message(UserMomentsConstant.TOPIC_MOMENTS,
                JSONObject.toJSONString(userMoment).getBytes(StandardCharsets.UTF_8));
        RocketMQUtil.syncSendMsg(producer, msg);
    }

    public List<UserMoment> getUserSubscribedMoments(Long userId) {
        String key = "subscribed-" + userId;
        String listStr = (String) redisTemplate.opsForValue().get(key);
        return JSONArray.parseArray(listStr, UserMoment.class);
    }

}
