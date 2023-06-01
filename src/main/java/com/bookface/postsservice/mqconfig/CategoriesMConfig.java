package com.bookface.postsservice.mqconfig;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableRabbit
public class CategoriesMConfig {

    // Categories message queue configuration
    public static final String CATEGORIES_EXCHANGE = "categoryExchange";

    public static final String CATEGORIES_QUEUE_CREATE = "categoryQueueCreate";
    public static final String CATEGORIES_ROUTING_KEY_CREATE = "categoryCreate";

    public static final String CATEGORIES_QUEUE_DELETE = "categoryQueueDelete";
    public static final String CATEGORIES_ROUTING_KEY_DELETE = "categoryDelete";

    @Bean
    public Queue queueCreateCategory() {
        return new Queue(CATEGORIES_QUEUE_CREATE);
    }

    @Bean
    public Queue queueDeleteCategory() {
        return new Queue(CATEGORIES_QUEUE_DELETE);
    }

    @Bean
    public TopicExchange exchangeCategories() {
        return new TopicExchange(CATEGORIES_EXCHANGE);
    }

    @Bean
    public Binding bindingCreateCategories(
            Queue queueCreateCategory,
            TopicExchange exchangeCategories
    ) {
        return BindingBuilder.bind(queueCreateCategory)
                .to(exchangeCategories)
                .with(CATEGORIES_ROUTING_KEY_CREATE);
    }

    @Bean
    public Binding bindingDeleteCategories(
            Queue queueDeleteCategory,
            TopicExchange exchangeCategories
    ) {
        return BindingBuilder.bind(queueDeleteCategory)
                .to(exchangeCategories)
                .with(CATEGORIES_ROUTING_KEY_DELETE);
    }

    // Add other necessary binding methods for categories update if needed

    @Bean
    public Jackson2JsonMessageConverter converter() {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build();

        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public AmqpTemplate categoriesAmqpTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
