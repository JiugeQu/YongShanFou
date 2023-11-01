package com.mizore.mob.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.User;
import com.mizore.mob.util.JWT;
import com.mizore.mob.util.UserHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class RefreshTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token == null) {
            response.getOutputStream().write(Result.error("NOT_LOGIN").toString().getBytes());
//            response.getWriter().write(JSONUtil.toJsonStr(Result.error("NOT_PROVIDED")));
            return true;
        }
        Claims claims;
        try {
            claims = JWT.parseJWT(token);
        } catch (JwtException e) {
            // JWT鉴权失败 token错误或者过期
            response.getOutputStream().write(Result.error("NOT_LOGIN").toString().getBytes());
//            response.getWriter().write(JSONUtil.toJsonStr(Result.error()));
            return true;
        }
        if (claims == null) {
            response.getOutputStream().write(Result.error("INVALID_TOKEN").toString().getBytes());
//            response.getWriter().write(JSONUtil.toJsonStr(Result.error("INVALID_TOKEN")));
            return true;
        }
        // 认证通过
        Map<String, Object> map = new HashMap<>();
        map.put("id", claims.get("id"));
        map.put("phone", claims.get("phone"));
        map.put("name", claims.get("name"));
        map.put("role", claims.get("role"));
        // 检查是否需要刷新token
        if (claims.getExpiration().getTime() - System.currentTimeMillis() < JWT.TTL) {
//            System.out.println("claims.getExpiration().getTime(): " + claims.getExpiration().getTime()
//            + "\n" + "System.currentTimeMillis(): " + System.currentTimeMillis());
            // token保质期是两天，在保质期还剩小于一天时重新颁发token
            String jwt = JWT.generateJWT(map);
            response.setHeader("token", jwt);
        }
        User currentUser = BeanUtil.fillBeanWithMap(map, new User(), false);
//        System.out.println(currentUser);
        UserHolder.save(currentUser);
        return true;
    }


}
