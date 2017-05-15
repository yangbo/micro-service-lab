package com.huajin.commander.manager.mq;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConfiguration {

//    @Bean
//    public SimpleRabbitListenerContainerFactory myFactory(
//            SimpleRabbitListenerContainerFactoryConfigurer configurer,
//            ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory =
//                new SimpleRabbitListenerContainerFactory();
//        configurer.configure(factory, connectionFactory);
//        factory.setMessageConverter(jsonMessageConverter());
//        return factory;
//    }

    @Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
