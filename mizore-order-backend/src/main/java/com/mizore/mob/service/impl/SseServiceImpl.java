package com.mizore.mob.service.impl;

import com.mizore.mob.exception.SseException;
import com.mizore.mob.service.ISseService;
import com.mizore.mob.util.SseSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
public class SseServiceImpl implements ISseService {
    @Override
    public SseEmitter connect(Integer id) {
        if(SseSession.exist(id)){
            SseSession.remove(id);
        }
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onError((err)-> {
            log.error("type: SseSession Error, msg: {} session Id : {}",err.getMessage(), id);
            SseSession.onError(id, err);
            SseSession.remove(id);
        });

        sseEmitter.onTimeout(() -> {
            log.info("type: SseSession Timeout, session Id : {}", id);
            SseSession.remove(id);
        });

        sseEmitter.onCompletion(() -> {
            log.info("type: SseSession Completion, session Id : {}", id);
            SseSession.remove(id);
        });
        SseSession.add(id, sseEmitter);
        return sseEmitter;
    }

    @Override
    public boolean send(Integer id, String content) {
        if(SseSession.exist(id)){
            try{
                SseSession.send(id, content);
                return true;
            }catch(IOException exception){
                log.error("type: SseSession send Erorr:IOException, msg: {} session Id : {}",exception.getMessage(), id);
            }
        }else{
            throw new SseException("User Id " + id + " not Found");
        }
        return false;
    }

    @Override
    public boolean close(Integer id) {
        log.info("type: SseSession Close, session Id : {}", id);
        return SseSession.remove(id);
    }

}
