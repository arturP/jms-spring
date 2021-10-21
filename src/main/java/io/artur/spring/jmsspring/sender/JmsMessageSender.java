package io.artur.spring.jmsspring.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.artur.spring.jmsspring.config.JmsConfig;
import io.artur.spring.jmsspring.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Session;
import java.util.UUID;

/**
 *
 */
@RequiredArgsConstructor
@Component
public class JmsMessageSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper mapper;

    @Scheduled(fixedRate = 3000)
    public void sendMessage() {
        System.out.println("Sending a message to jms queue");

        Message message = Message.builder()
                .id(UUID.randomUUID())
                .content("This is sample message ")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.QUEUE_NAME, message);

    }

    @Scheduled(fixedRate = 3000)
    public void sendAndReceiveMessage() throws JMSException {
        System.out.println("Sending and receiving a message to jms queue");

        Message message = Message.builder()
                .id(UUID.randomUUID())
                .content("This is new type of message 123-445")
                .build();

        javax.jms.Message receivedMsg = jmsTemplate.sendAndReceive(JmsConfig.QUEUE_SEND_RCV_NAME, new MessageCreator() {
            @SneakyThrows
            @Override
            public javax.jms.Message createMessage(Session session) throws JMSException {
                javax.jms.Message jmsMsg = session.createTextMessage(mapper.writeValueAsString(message));
                jmsMsg.setStringProperty("_type", "io.artur.spring.jmsspring.model.Message");

                System.out.println("Sending new type of message");
                return jmsMsg;
            }
        });
        System.out.println(receivedMsg.getBody(String.class));
    }
}
