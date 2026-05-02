package com.mall.LongTou.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.mall.LongTou.util.SeckillKey.*;

@Configuration
public class RabbitMQConfig {



    //配置rabbitmq 使得传入的对象不被序列化  而是以json对象的形式发送到交换机
    @Bean
    public RabbitTemplate  rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }


    //使消费者能正确接收json信息
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public Exchange seckillExchange() {
        //直接交换机  durable表示持久化交换机  使得服务器重启交换机也不丢失
        return ExchangeBuilder.directExchange(SECKILL_EXCHANGE).durable(true).build();
    }

    @Bean//声明队列
    public Queue seckillQueue() {
        return QueueBuilder.durable(SECKILL_QUEUE).build();
    }


    //绑定交换机
    @Bean
    public Binding seckillBinding() {
        return BindingBuilder.bind(seckillQueue()).to(seckillExchange()).with(SECKILL_ROUTING_KEY).noargs();
    }

    //订单超时取消
    // 1. 延迟交换机
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");  // 底层路由类型
        return new CustomExchange(
                "order.delayed.exchange",
                "x-delayed-message",  // 固定字符串，标识为延迟交换机
                true,
                false,
                args
        );
    }

    //延迟队列
    @Bean
    public Queue cancelOrderQueue() {
        return new Queue(CANCEL_QUEUE, true);
    }


    //绑定队列

    @Bean
    public Binding delayedBinding() {
        return BindingBuilder.bind(cancelOrderQueue())
                .to(delayedExchange())
                .with(CANCEL_ROUTING_KEY)
                .noargs(); // 对于CustomExchange，使用noargs()方法
    }

}