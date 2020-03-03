package com.mitrais.cdc.blogmicroservices.services;

import com.mitrais.cdc.blogmicroservices.payload.BlogApprovalInProgress;
import com.mitrais.cdc.blogmicroservices.payload.BlogStatistic;
import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import org.springframework.stereotype.Service;

import java.util.List;

public interface KafkaService {

     void publishBlogCreationMessage(PostPayload postPayload);
     void sendNotification(PostPayload postPayload);
     void sendBlogStatistic(List<BlogStatistic> blogStatisticList);
}
