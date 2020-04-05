package com.chetan.test.ssewithredis.listener;

import com.chetan.test.ssewithredis.services.SubscriptionService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener implements MessageListener {

    SubscriptionService subscriptionService;

    public NotificationListener(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        System.out.println(message.toString());
        subscriptionService.notifySubscribers(message.toString());
    }
}
