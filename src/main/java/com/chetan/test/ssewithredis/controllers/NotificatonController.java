package com.chetan.test.ssewithredis.controllers;

import com.chetan.test.ssewithredis.model.SerializableSSE;
import com.chetan.test.ssewithredis.producer.NotificationProducer;
import com.chetan.test.ssewithredis.services.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class NotificatonController {

    SubscriptionService service;
    NotificationProducer producer;

    public NotificatonController(SubscriptionService service, NotificationProducer producer) {
        this.service = service;
        this.producer = producer;
    }

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe() {

        final SerializableSSE emitter = new SerializableSSE();
        service.subscribe(emitter);
        emitter.onCompletion(() -> service.removeEmitter(emitter));
        emitter.onTimeout(() -> service.removeEmitter(emitter));
        return new ResponseEntity(emitter, HttpStatus.OK);
    }


    @GetMapping(value = "/publish")
    public ResponseEntity publish() {
        Random random = new Random();
        List<String> stringList = random.longs(10).boxed().map(p ->"String "+p).collect(Collectors.toList());
        stringList.forEach(sp -> {
            System.out.println(sp);
            producer.publish(sp);

        });
        return ResponseEntity.ok().build();
    }
}
