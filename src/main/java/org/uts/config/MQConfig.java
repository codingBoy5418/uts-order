//package org.uts.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @Description 交换机 配置类
// * @Author codBoy
// * @Date 2024/8/24 19:55
// */
//@Configuration
//public class MQConfig {
//
//    //业务类交换机
//    private static final String BUSINESS_EXCHANGE = "BUSINESS_EXCHANGE";
//
//    //业务类队列
//    private static final String BUSINESS_QUEUE = "BUSINESS_QUEUE";
//
//    //业务类路由键
//    private static final String BUSINESS_ROUTING_KEY = "business";
//
//    //声明业务队列
//    @Bean(BUSINESS_QUEUE)
//    public Queue queue(){
//        return QueueBuilder.durable(BUSINESS_QUEUE).build();
//    }
//
//    //声明业务交换机
//    @Bean(BUSINESS_EXCHANGE)
//    public Exchange exchange(){
//        return new DirectExchange(BUSINESS_EXCHANGE);
//    }
//
//    //绑定业务队列和业务交换机
//    @Bean
//    public Binding queueBindExchange(@Qualifier(BUSINESS_QUEUE) Queue businessQueue, @Qualifier(BUSINESS_EXCHANGE) DirectExchange directExchange){
//        return BindingBuilder.bind(businessQueue).to(directExchange).with(BUSINESS_ROUTING_KEY);
//    }
//}
