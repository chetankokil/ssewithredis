package com.chetan.test.ssewithredis.producer;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class NotificationProducer {

    RedisTemplate<String, SseEmitter> redisTemplate;

    public NotificationProducer(RedisTemplate<String, SseEmitter> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publish(String message) {
        System.out.println("message="+message);
        redisTemplate.convertAndSend("emitters", message);
    }

}
