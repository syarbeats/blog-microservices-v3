package com.mitrais.cdc.blogmicroservices.services;

import com.mitrais.cdc.blogmicroservices.payload.PostPayload;

public interface KafkaService {

    public void publishBlogCreationMessage(PostPayload postPayload);
    public void subscribeBlogCreationMessage(PostPayload postPayload);
}
