package com.mitrais.cdc.blogmicroservices.services.impl;

import com.mitrais.cdc.blogmicroservices.payload.BlogApprovalInProgress;
import com.mitrais.cdc.blogmicroservices.payload.BlogStatistic;
import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import com.mitrais.cdc.blogmicroservices.services.KafkaService;
import com.mitrais.cdc.blogmicroservices.services.PostService;
import com.mitrais.cdc.blogmicroservices.utility.KafkaCustomChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class KafkaServiceImpl implements KafkaService {

    private KafkaCustomChannel kafkaCustomChannel;
    /*private PostService postService;*/

    @Autowired
    public KafkaServiceImpl(KafkaCustomChannel kafkaCustomChannel) {
        this.kafkaCustomChannel = kafkaCustomChannel;
        /*this.postService = postService;*/
    }

    @Override
    public void publishBlogCreationMessage(PostPayload postPayload) {
        this.kafkaCustomChannel.blogCreationPubChannel().send(MessageBuilder.withPayload(postPayload).build());
        log.info("Send Blog Creation:"+ postPayload);
    }

    @Override
    public void sendNotification(PostPayload postPayload) {

    }

    @Override
    public void sendBlogStatistic(List<BlogStatistic> blogStatisticList) {
        this.kafkaCustomChannel.blogCategoryStatisticOutputPubChannel().send(MessageBuilder.withPayload(blogStatisticList).build());
    }

}
