package com.mitrais.cdc.blogmicroservices.services.impl;

import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import com.mitrais.cdc.blogmicroservices.services.KafkaService;
import com.mitrais.cdc.blogmicroservices.utility.KafkaCustomChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaServiceImpl implements KafkaService {

    private KafkaCustomChannel kafkaCustomChannel;

    @Autowired
    public KafkaServiceImpl(KafkaCustomChannel kafkaCustomChannel) {
        this.kafkaCustomChannel = kafkaCustomChannel;
    }

    @Override
    public void publishBlogCreationMessage(PostPayload postPayload) {
        this.kafkaCustomChannel.blogCreationPubChannel().send(MessageBuilder.withPayload(postPayload).build());
        log.info("Send Blog Creation:"+ postPayload);
    }

    @Override
    @StreamListener("BlogCreationInput")
    public void subscribeBlogCreationMessage(@Payload PostPayload postPayload) {
        log.info("Receive Blog Creation data:"+postPayload.getTitle());
    }
}
