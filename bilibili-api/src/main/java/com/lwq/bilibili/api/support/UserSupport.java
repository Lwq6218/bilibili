package com.lwq.bilibili.api.support;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.lwq.bilibili.domain.exception.ConditionException;
import com.lwq.bilibili.service.util.TokenUtil;

@Component
public class UserSupport {
    public Long getCurrentUserId() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (requestAttributes == null) {
            throw new ConditionException("无法获取当前请求的属性");
        }
        String token = requestAttributes.getRequest().getHeader("token");
        Long userId = TokenUtil.vertifyToken(token);
        if (userId < 0) {
            throw new ConditionException("非法用户token");
        }
        return userId;

    }
}
