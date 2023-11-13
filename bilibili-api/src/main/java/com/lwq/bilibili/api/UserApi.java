package com.lwq.bilibili.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lwq.bilibili.api.support.UserSupport;
import com.lwq.bilibili.domain.JsonResponse;
import com.lwq.bilibili.domain.PageResult;
import com.lwq.bilibili.domain.User;
import com.lwq.bilibili.domain.UserInfo;
import com.lwq.bilibili.service.UserFollowingService;
import com.lwq.bilibili.service.UserService;
import com.lwq.bilibili.service.util.RSAUtil;
import java.util.List;

@RestController
public class UserApi {

    @Autowired
    private UserService userService;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private UserFollowingService userFollowingService;

    @GetMapping("/users")
    public JsonResponse<User> getUserInfo() {
        Long userId = userSupport.getCurrentUserId();
        User user = userService.getUserInfo(userId);
        return new JsonResponse<>(user);

    }

    @GetMapping("/rsa-pks")
    public JsonResponse<String> getPublicKey() {
        return new JsonResponse<>(RSAUtil.getPublicKeyStr());
    }

    @PostMapping("/users")
    public JsonResponse<String> addUser(@RequestBody User user) {
        userService.addUser(user);
        return JsonResponse.success();
    }

    @PostMapping("/user-tokens")
    public JsonResponse<String> login(@RequestBody User user) throws IllegalArgumentException, Exception {
        String token = userService.login(user);
        return new JsonResponse<String>(token);
    }

    @PutMapping("/users")
    public JsonResponse<String> updateUsers(@RequestBody User user) {
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);
        userService.updateUsers(user);
        return JsonResponse.success();
    }

    @PutMapping("/user-infos")
    public JsonResponse<String> updateUserInfos(@RequestBody UserInfo userInfo) {
        Long userId = userSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        userService.updateUserInfos(userInfo);
        return JsonResponse.success();
    }

    @GetMapping("/user-infos")
    public JsonResponse<PageResult<UserInfo>> getUserInfos(@RequestParam Integer no, @RequestParam Integer size,
            String nick) {
        Long userId = userSupport.getCurrentUserId();
        JSONObject params = new JSONObject();
        params.put("no", no);
        params.put("size", params);
        params.put("nick", nick);
        params.put("userId", userId);
        PageResult<UserInfo> result = userService.getUserInfos(params);
        if (result.getTotal() > 0) {
            List<UserInfo> checkedUserInfoList = userFollowingService.checkFollowingStatus(result.getList(), userId);
            result.setList(checkedUserInfoList);
        }
        return new JsonResponse<>(result);
    }

}
