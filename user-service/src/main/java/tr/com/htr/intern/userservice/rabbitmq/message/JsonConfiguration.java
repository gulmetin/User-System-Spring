package tr.com.htr.intern.userservice.rabbitmq.message;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfiguration {
    @Bean
    public MessageConverter messageConverter() {
        return new JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory jsaFactory(
            ConnectionFactory connectionFactory, SimpleRabbitListenerContainerFactoryConfigurer configurer
    ) {
        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(simpleRabbitListenerContainerFactory, connectionFactory);
        simpleRabbitListenerContainerFactory.setMessageConverter(messageConverter());
        return simpleRabbitListenerContainerFactory;
    }

}
