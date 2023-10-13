package tr.com.htr.intern.userservice.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageProducer {

    //    @Value("${sr.rabbit.routing.name}")
    private String routingName = "test.routing.key";

    private final ObjectMapper objectMapper;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    public MessageProducer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public void sendToQueue(Notification notification) {

        rabbitTemplate.convertAndSend("gul-exchanger", routingName, objectMapper.convertValue(notification, Notification.class));
        log.info("Message send");

//        new Thread(() -> {
//            while (true) {
//                try {
//
//                    rabbitTemplate.convertAndSend("gul-exchanger", routingName, objectMapper.convertValue(notification, Notification.class));
//                    log.info("Message send");
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }
}
