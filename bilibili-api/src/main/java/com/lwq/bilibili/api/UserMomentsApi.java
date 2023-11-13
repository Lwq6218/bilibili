package com.lwq.bilibili.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lwq.bilibili.api.support.UserSupport;
import com.lwq.bilibili.domain.JsonResponse;
import com.lwq.bilibili.domain.User;
import com.lwq.bilibili.domain.UserMoment;
import com.lwq.bilibili.service.UserMomentsService;

@RestController
public class UserMomentsApi {
    @Autowired
    private UserMomentsService userMomentsService;
    @Autowired
    private UserSupport userSupport;

    @PostMapping("/user-moments")
    public JsonResponse<String> addUserMoments(@RequestBody UserMoment userMoment) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        userMoment.setUserId(userId);
        userMomentsService.addUserMoments(userMoment);
        return JsonResponse.success();
    }
    @GetMapping("user-subscribed-moments")
    public JsonResponse<List<UserMoment>> getUserSubscribedMoments(){
        Long userId = userSupport.getCurrentUserId();
        List <UserMoment> list = userMomentsService.getUserSubscribedMoments(userId);
        return new JsonResponse<>(list);
    }
}
