package io.incondensable.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author abbas
 */
@Configuration
@PropertySource("classpath:rabbitmq-info.properties")
public class RabbitMqConfig {

    @Value("${exchange.name}")
    private String exchange;
    @Value("${exchange.name.fanout}")
    private String fanoutExchange;

    @Value("${notification.service.queue}")
    private String notificationQueue;
    @Value("${owner.service.queue}")
    private String ownerQueue;
    @Value("${owner.service.fanout.queue}")
    private String ownerFanoutQueue;
    @Value("${notification.service.fanout.queue}")
    private String notificationFanoutQueue;

    @Value("${notifications.routing.key}")
    private String notificationRoutingKey;
    @Value("${owner.routing.key}")
    private String ownerRoutingKey;

    // Configuring the Exchange

    /**
     * Since in RabbitMQ we need an Exchange we have to define at least One.
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    // Configuring the Queues

    /**
     * in RabbitMQ, the Exchange sends messages to the Queues based on Routing-Keys, Bindings, Header Attributes.
     * So that I defined a Queue for Notification-Service.
     */
    @Bean
    public Queue notificationQueue() {
        return new Queue(notificationQueue, false);
    }

    /**
     * in RabbitMQ, the Exchange sends messages to the Queues based on Routing-Keys, Bindings, Header Attributes.
     * So that I defined a Queue for Owner-Service.
     */
    @Bean
    public Queue ownerQueue() {
        return new Queue(ownerQueue, false);
    }

    // Binding the Exchange to Queues.
    @Bean
    public Binding notificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(exchange())
                .with(notificationRoutingKey);
    }

    @Bean
    public Binding ownerBinding() {
        return BindingBuilder
                .bind(ownerQueue())
                .to(exchange())
                .with(ownerRoutingKey);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(fanoutExchange);
    }

    @Bean
    public Queue fanoutNotificationQueue() {
        return new Queue(notificationFanoutQueue, false);
    }

    @Bean
    public Queue fanoutOwnerQueue() {
        return new Queue(ownerFanoutQueue, false);
    }

    @Bean
    public Binding notificationBindingFanout(Queue fanoutNotificationQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder
                .bind(fanoutNotificationQueue)
                .to(fanoutExchange);
    }

    @Bean
    public Binding ownerBindingFanout(Queue fanoutOwnerQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder
                .bind(fanoutOwnerQueue)
                .to(fanoutExchange);
    }

}
