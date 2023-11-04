package com.mizore.mob.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.User;
import com.mizore.mob.util.Constant;
import com.mizore.mob.util.JWT;
import com.mizore.mob.util.UserHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.mizore.mob.util.Constant.LOGIN_TOKEN_PREFIX;

public class RefreshTokenInterceptor implements HandlerInterceptor {

    StringRedisTemplate stringRedisTemplate;
    // 拦截器是以new的方式添加到MVC配置的，不是bean,所以stringRedisTemplate不能自动装配，需从创建方传过来
    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token == null) {
            // 拦截动作在login check 拦截器
            return true;
        }

        // 从redis获取密钥
        String tokenKey = Constant.LOGIN_TOKEN_PREFIX + token;
        String keyStr = stringRedisTemplate.opsForValue().get(tokenKey);
        if (StrUtil.isEmpty(keyStr)) {
            // token有误或彻底过期（超过ttl*2）
            response.getOutputStream().write(Result.error("NOT_LOGIN").toString().getBytes());
            return true;
        }

        // token正确并且未在redis缓存过期
        Claims claims;
        try {
            // 使用从redis擦查来的密钥解析
            claims = JWT.parseJWT(token, JWT.getSecretKey(keyStr));
        } catch (JwtException e) {
            // JWT鉴权失败 token错误或者过期
            response.getOutputStream().write(Result.error("NOT_LOGIN").toString().getBytes());
            return true;
        }
        if (claims == null) {
            response.getOutputStream().write(Result.error("INVALID_TOKEN").toString().getBytes());
            return true;
        }
        // 认证通过
        Map<String, Object> map = new HashMap<>();
        map.put("id", claims.get("id"));
        map.put("phone", claims.get("phone"));
        map.put("name", claims.get("name"));
        map.put("role", claims.get("role"));
        // 检查是否需要刷新token
        Long expire = stringRedisTemplate.getExpire(tokenKey);      // 得到剩余时间
        // token的redis缓存保质期是两天，在保质期还剩小于一天时重新颁发token。
        // expire会在键是过期键时得到null,但经过上面对keyStr的判断，可以断言这里的expire不会为hull
        if (expire <= JWT.TTL) {
            // 剩余时间不足一天，需要自动续期，实际上是重新颁发新jwt
            String jwt = JWT.generateJWT(map);
            // 把新jwt放入
            stringRedisTemplate.opsForValue().set(LOGIN_TOKEN_PREFIX + jwt, JWT.getSecretKeyStr(), JWT.TTL * 2, TimeUnit.MILLISECONDS);
            // 删除旧的
            stringRedisTemplate.delete(tokenKey);
            response.setHeader("token", jwt);
        }
        User currentUser = BeanUtil.fillBeanWithMap(map, new User(), false);
//        System.out.println(currentUser);
        UserHolder.save(currentUser);
        return true;
    }


}
