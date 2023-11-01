package com.mizore.mob.util;

import com.mizore.mob.exception.SseException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SseSession {

    private static Map<Integer, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    public static void add(Integer id, SseEmitter sseEmitter) {
        if (sseEmitterMap.containsKey(id)) {
            throw new SseException("重复连接！！");
        }
        sseEmitterMap.put(id, sseEmitter);
    }

    public static boolean exist(Integer id){
        return sseEmitterMap.containsKey(id);
    }

    public static boolean remove(Integer id) {
        if (!sseEmitterMap.containsKey(id)) {
            return false;
        }
        sseEmitterMap.get(id).complete();
        return true;
    }

    public static void onError(Integer id , Throwable throwable) {
        if (!sseEmitterMap.containsKey(id)) {
            return;
        }
        sseEmitterMap.get(id).completeWithError(throwable);
    }

    public static void send(Integer id, String message) throws IOException {
        if (!sseEmitterMap.containsKey(id)) {
            return;
        }
        sseEmitterMap.get(id).send(message);
    }
}
