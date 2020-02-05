package com.mitrais.cdc.blogmicroservices.utility;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface KafkaCustomChannel {

    @Input("BlogCreationInput")
    SubscribableChannel blogCreationSubsChannel();

    @Output("BlogCreationOutput")
    MessageChannel blogCreationPubChannel();

}
