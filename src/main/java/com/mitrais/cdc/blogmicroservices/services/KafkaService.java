package com.mitrais.cdc.blogmicroservices.services;

import com.mitrais.cdc.blogmicroservices.payload.BlogApprovalInProgress;
import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import org.springframework.stereotype.Service;

public interface KafkaService {

     void publishBlogCreationMessage(PostPayload postPayload);
     void sendNotification(PostPayload postPayload);
}
