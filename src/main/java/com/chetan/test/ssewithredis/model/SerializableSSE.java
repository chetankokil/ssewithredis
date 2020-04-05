package com.chetan.test.ssewithredis.model;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.Serializable;

public class SerializableSSE extends SseEmitter implements Serializable {

    public SerializableSSE() {
        super();
    }

    public SerializableSSE(Long timeout) {
        super(timeout);
    }
}