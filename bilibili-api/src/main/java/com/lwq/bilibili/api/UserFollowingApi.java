package com.lwq.bilibili.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lwq.bilibili.api.support.UserSupport;
import com.lwq.bilibili.domain.FollowingGroup;
import com.lwq.bilibili.domain.JsonResponse;
import com.lwq.bilibili.domain.UserFollowing;
import com.lwq.bilibili.service.UserFollowingService;

@RestController
public class UserFollowingApi {
    @Autowired
    private UserFollowingService userFollowingService;
    @Autowired
    private UserSupport userSupport;

    @PostMapping("/user-followings")
    public JsonResponse<String> addUserFollowing(@RequestBody UserFollowing userFollowing) {
        Long userId = userSupport.getCurrentUserId();
        userFollowing.setUserId(userId);
        userFollowingService.addUserFollowing(userFollowing);
        return JsonResponse.success();
    }

    @GetMapping("/user-followings")
    public JsonResponse<List<FollowingGroup>> getUserFollowings() {
        Long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> list = userFollowingService.getUserFollowings(userId);
        return new JsonResponse<>(list);
    }

    @GetMapping("/user-fans")
    public JsonResponse<List<UserFollowing>> getUserFans() {
        Long userId = userSupport.getCurrentUserId();
        List<UserFollowing> list = userFollowingService.getUserFans(userId);
        return new JsonResponse<>(list);
    }

    @PostMapping("/user-following-groups")
    public JsonResponse<Long> addUserFollowingGroups(@RequestBody FollowingGroup followingGroup) {
        Long userId = userSupport.getCurrentUserId();
        followingGroup.setUserId(userId);
        Long groupId = userFollowingService.addUserFollowingGroups(followingGroup);
        return new JsonResponse<>(groupId);
    }

    @GetMapping("/user-following-groups")
    public JsonResponse<List<FollowingGroup>> getUserFollowingGroups() {
        Long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> userFollowingGroupList = userFollowingService.getUserFollowingGroups(userId);
        return new JsonResponse<>(userFollowingGroupList);
    }

}
