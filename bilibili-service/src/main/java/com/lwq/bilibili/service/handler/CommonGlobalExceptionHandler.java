package com.lwq.bilibili.service.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lwq.bilibili.domain.JsonResponse;
import com.lwq.bilibili.domain.exception.ConditionException;

import jakarta.servlet.http.HttpServletRequest;

//TODO: 有待优化
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonGlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResponse<String> commonexceptionHandler(HttpServletRequest request, Exception e) {
        String errorMsg = e.getMessage();
        if (e instanceof ConditionException) {
            String errorCode = ((ConditionException) e).getCode();
            return JsonResponse.fail(errorCode, errorMsg);
        } else {
            return new JsonResponse<String>("500", errorMsg);
        }
    }
}
