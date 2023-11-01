package com.mizore.mob.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ISseService {

    SseEmitter connect(Integer userId);

    boolean send(Integer userId, String message);

    boolean close(Integer userId);
}
