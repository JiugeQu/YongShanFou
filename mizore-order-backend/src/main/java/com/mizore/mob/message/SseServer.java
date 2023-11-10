package com.mizore.mob.message;

import com.mizore.mob.entity.User;
import com.mizore.mob.util.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.mizore.mob.util.Constant.*;

@Slf4j
@Service
public class SseServer {
    /**
     * 当前连接数
     */
    private final AtomicInteger count = new AtomicInteger(0);

    /**
     * 使用map对象，便于根据userId来获取对应的SseEmitter，或者放redis里面
     * userId: {身份} + ":" + userId
     * STAFF:1
     * DELIVERY:2
     */
    private final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    /**
     * 创建用户连接并返回 SseEmitter
     *
     * @param userId 用户ID
     * @return SseEmitter
     */
    public SseEmitter connect(String userId) {
        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        SseEmitter sseEmitter = new SseEmitter(0L);
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(userId));
        sseEmitter.onError(errorCallBack(userId));
        sseEmitter.onTimeout(timeoutCallBack(userId));
        // 加入sseEmitter容器
        sseEmitterMap.put(userId, sseEmitter);
        // 数量+1
        count.getAndIncrement();
        log.info("创建新的sse连接，当前用户：{}", userId);
        return sseEmitter;
    }

    /**
     * 给指定用户发送信息
     */
    public void sendMessage(String userId, String message) {
        if (sseEmitterMap.containsKey(userId)) {
            try {
                // sseEmitterMap.get(userId).send(message, MediaType.APPLICATION_JSON);
                sseEmitterMap.get(userId).send(message);
            } catch (IOException e) {
                log.error("用户[{}]推送异常:{}", userId, e.getMessage());
                removeUser(userId);
            }
        }
    }

    /**
     * 群发消息-给指定角色群
     */
    public void batchSendMessage(String message, String rolePrefix) {
        sseEmitterMap.keySet().forEach(
                key -> {
                    if (key.startsWith(rolePrefix)) {
                        sendMessage(key, message);
                    }
                }
        );
    }


    /**
     * 移除用户连接
     */
    public boolean removeUser(String userId) {
        if (!sseEmitterMap.containsKey(userId)) {
            return false;
        }
        sseEmitterMap.remove(userId);
        // 数量-1
        count.getAndDecrement();
        log.info("移除用户：{}", userId);
        return true;
    }

    /**
     * 获取当前连接信息
     */
    public List<String> getIds() {
        return new ArrayList<>(sseEmitterMap.keySet());
    }

    /**
     * 获取当前连接数量
     */
    public int getUserCount() {
        return count.intValue();
    }

    // 下面是sse生命周期触发回调函数

    private Runnable completionCallBack(String userId) {
        return () -> {
            log.info("结束连接：{}", userId);
            removeUser(userId);
        };
    }

    private Runnable timeoutCallBack(String userId) {
        return () -> {
            log.info("连接超时：{}", userId);
            removeUser(userId);
        };
    }

    private Consumer<Throwable> errorCallBack(String userId) {
        return throwable -> {
            log.info("连接异常：{}", userId);
            removeUser(userId);
        };
    }

    public String getKey() {
        User user = UserHolder.get();
        String prefix;
        Byte role = user.getRole();
        if (role.equals(ROLE_CUSTOMER)) {
            prefix = CUSTOMER_PREFIX;
        } else if (role.equals(ROLE_DELIVERY)) {
            prefix = DELIVERY_PREFIX;
        } else {
            prefix = STAFF_PREFIX;
        }
        return prefix + user.getId();
    }
}
