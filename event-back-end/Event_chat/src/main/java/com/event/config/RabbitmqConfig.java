package com.event.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitmqConfig{
    @Bean("queue")
    public Queue queueMsg(){
        return new Queue("queue");
    }
    @Bean("exchange")
    public TopicExchange exchange(){
        return new TopicExchange("exchange");
    }
//    @Bean
//    Binding bindingExchangeAndQueuemsg(@Qualifier("queue") Queue queue, @Qualifier("exchange") TopicExchange exchange){
//        return BindingBuilder.bind(queue).to(exchange).with("*");
//   }
}