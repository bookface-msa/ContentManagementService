//package com.bookface.postsservice.consumertest;
//
//import com.bookface.postsservice.dto.ElasticPostsMessage;
//import com.bookface.postsservice.mqconfig.MessagingConfig;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class User {
//
//    @RabbitListener(queues = MessagingConfig.QUEUE_CREATE)
//    public void consumeMessageFromQueueCre(ElasticPostsMessage epm){
//        System.out.println("Recieved "+ epm);
//    }
//    @RabbitListener(queues = MessagingConfig.QUEUE_DELETE)
//    public void consumeMessageFromQueueDel(String id){
//        System.out.println("Recieved "+ id.toString());
//    }
//    @RabbitListener(queues = MessagingConfig.QUEUE_UPDATE)
//    public void consumeMessageFromQueueUpd(ElasticPostsMessage epm){
//        System.out.println("Recieved "+ epm);
//    }
//}
