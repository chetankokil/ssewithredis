package com.chetan.test.ssewithredis.services;

import com.chetan.test.ssewithredis.model.SerializableSSE;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SubscriptionService {

    RedisTemplate<String, String> redisTemplate;
    RedisSerializer redisSerializer;
    final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SubscriptionService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        redisSerializer = new StringRedisSerializer();
    }


    public void subscribe(SerializableSSE serializableSSE) {
       emitters.add(serializableSSE);
    }

    public void removeEmitter(final SseEmitter emitter) {
        emitters.remove(emitter);
    }

    public void notifySubscribers(String message) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        System.out.println(redisSerializer.deserialize(message.getBytes(StandardCharsets.UTF_8)));
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .data(redisSerializer.deserialize(message.getBytes(StandardCharsets.UTF_8))));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }

}
