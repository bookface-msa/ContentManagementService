package com.bookface.postsservice.mqconfig;

import ch.qos.logback.classic.pattern.MessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit

public class MessagingConfig {

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
    public DirectExchange exchangeCategories() {
        return new DirectExchange(CATEGORIES_EXCHANGE);
    }

    @Bean
    public Binding bindingCreateCategories(
            Queue queueCreateCategory,
            DirectExchange exchangeCategories
    ) {
        return BindingBuilder.bind(queueCreateCategory)
                .to(exchangeCategories)
                .with(CATEGORIES_ROUTING_KEY_CREATE);
    }
    @Bean
    public Binding bindingDeleteCategories(
            Queue queueDeleteCategory,
            DirectExchange exchangeCategories
    ) {
        return BindingBuilder.bind(queueDeleteCategory)
                .to(exchangeCategories)
                .with(CATEGORIES_ROUTING_KEY_DELETE);
    }
    public static final String EXCHANGE = "elastic.blogs";

    public static final String QUEUE_CREATE = "elastic.blogs.create";
    public static final String ROUTING_KEY_CREATE = "create";

    public static final String QUEUE_DELETE = "elastic.blogs.delete";
    public static final String ROUTING_KEY_DELETE = "delete";

    public static final String QUEUE_UPDATE = "elastic.blogs.update";
    public static final String ROUTING_KEY_UPDATE= "update";

    @Bean
    //For created Posts to send to search service.
    public Queue queueCreateBlog(){
        return new Queue(QUEUE_CREATE);
    }
    @Bean
    public Queue queueDeleteBlog(){
        return new Queue(QUEUE_DELETE);
    }
    @Bean
    public Queue queueUpdateBlog(){
        return new Queue(QUEUE_UPDATE);
    }
    @Bean
    public TopicExchange exchangeBlogs(){
        return new TopicExchange(EXCHANGE);
    }
    @Bean
    public Binding bindingCreateBlogs(Queue queueCreateBlog, TopicExchange exchange){
        return BindingBuilder.bind(queueCreateBlog).to(exchange).with(ROUTING_KEY_CREATE);
    }
    @Bean
    public Binding bindingDeleteBlogs(Queue queueDeleteBlog, TopicExchange exchange){
        return BindingBuilder.bind(queueDeleteBlog).to(exchange).with(ROUTING_KEY_DELETE);
    }
    @Bean
    public Binding bindingUpdateBlogs(Queue queueUpdateBlog, TopicExchange exchange){
        return BindingBuilder.bind(queueUpdateBlog).to(exchange).with(ROUTING_KEY_UPDATE);
    }
    @Bean
    public Jackson2JsonMessageConverter converter(){
        ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build();

        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
