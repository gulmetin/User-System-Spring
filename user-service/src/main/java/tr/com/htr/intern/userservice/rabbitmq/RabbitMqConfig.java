package tr.com.htr.intern.userservice.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${sr.rabbit.queue.name}")
    private String queueName;

    @Value("${sr.rabbit.exchange.name}")
    private String exchangeName;

    @Value("${sr.rabbit.routing.name}")
    private String routingName;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchangeName);
    }

//    @Bean
//    public Binding binding(final Queue queue, final TopicExchange topicExchange) {
//        return BindingBuilder.bind(queue).to(topicExchange).with(routingName);
//    }
}
