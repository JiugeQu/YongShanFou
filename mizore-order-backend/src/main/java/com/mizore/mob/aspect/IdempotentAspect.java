package com.mizore.mob.aspect;

import com.mizore.mob.annotation.Idempotent;
import com.mizore.mob.entity.User;
import com.mizore.mob.exception.IdemPotentException;
import com.mizore.mob.util.Constant;
import com.mizore.mob.util.UserHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class IdempotentAspect {


    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Pointcut("@annotation(com.mizore.mob.annotation.Idempotent)")
    public void idempotentMethod() {}

    @Around("idempotentMethod()")
    public Object handleIdempotentMethod(ProceedingJoinPoint joinPoint) {

        // 根据userId + 方法名 + 参数 生成redis setnx的键， 方法执行结果为redis值
        User user = UserHolder.get();

        if (user == null) {
            throw new IdemPotentException("空用户异常！！");
        }

        // user id
        Integer userId = user.getId();

        /**
         * 幂等性方法签名
         * toLongString() 方法返回一个较长的字符串表示目标方法的签名信息，包括方法的修饰符、返回类型、方法名、参数类型等详细信息。
         * toShortString() 方法返回一个相对较短的字符串表示目标方法的签名信息，包括方法名和参数类型。与toString()一致
         * 众所周知，方法名+参数列表是一个方法的唯一标识。
         */
        String methodSignature = joinPoint.getSignature().toShortString();
        log.info("long string method signature: {}", joinPoint.getSignature().toLongString());
        // 参数值
        String args = Arrays.toString(joinPoint.getArgs());
        log.info("执行幂等性处理: userId: {}, methodName: {}, args: {}", userId, methodSignature, args);

        String hash = getHash(userId + methodSignature + args);
        String key = Constant.IDEMPOTENT_KEY_PREFIX +  hash;

        // 通过注解获取过期时间
        Idempotent annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Idempotent.class);
        long expireTime = annotation.expireTime();
        Boolean res = stringRedisTemplate.opsForValue().setIfAbsent(key, Constant.IDEMPOTENT_VALUE, Duration.ofSeconds(expireTime));
        if (Boolean.FALSE.equals(res)) {
            throw new IdemPotentException("发生误重复请求！！");
        }
        // 通过幂等性检验 执行控制器方法
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private String getHash(String s) {
        return Integer.toString(s.hashCode());
    }
}
