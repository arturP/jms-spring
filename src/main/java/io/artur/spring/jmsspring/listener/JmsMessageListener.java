package io.artur.spring.jmsspring.listener;

import io.artur.spring.jmsspring.config.JmsConfig;
import io.artur.spring.jmsspring.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.UUID;

/**
 *
 */
@RequiredArgsConstructor
@Component
public class JmsMessageListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.QUEUE_NAME)
    public void listen(@Payload Message message,
                       @Headers MessageHeaders headers,
                       javax.jms.Message jmsMessage) {

        System.out.println("Listener: I have got a message.");

        System.out.println(message);
    }

    @JmsListener(destination = JmsConfig.QUEUE_SEND_RCV_NAME)
    public void listenNewMsg(@Payload Message message,
                       @Headers MessageHeaders headers,
                       javax.jms.Message jmsMessage) throws JMSException {

        System.out.println("Listener: I have got a message.");

        Message reply = Message.builder()
                .id(UUID.randomUUID())
                .content("This is reply message")
                .build();

        jmsTemplate.convertAndSend(jmsMessage.getJMSReplyTo(), reply);

        System.out.println(message);
    }
}
