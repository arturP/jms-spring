package io.artur.spring.jmsspring.sender;

import io.artur.spring.jmsspring.config.JmsConfig;
import io.artur.spring.jmsspring.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 *
 */
@RequiredArgsConstructor
@Component
public class JmsMessageSender {

    private final JmsTemplate jmsTemplate;

    @Scheduled(fixedRate = 3000)
    public void sendMessage() {
        System.out.println("Sending a message to jms queue");

        Message message = Message.builder()
                .id(UUID.randomUUID())
                .content("This is sample message ")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.QUEUE_NAME, message);

    }
}
