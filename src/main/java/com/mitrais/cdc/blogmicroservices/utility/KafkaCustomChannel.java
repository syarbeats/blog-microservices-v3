package com.mitrais.cdc.blogmicroservices.utility;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface KafkaCustomChannel {

    @Input("BlogNotificationInput")
    SubscribableChannel blogNotificationSubsChannel();

    @Input("BlogKey")
    SubscribableChannel blogKeySubsChannel();

    @Input("BlogUpdateStatusInput")
    SubscribableChannel blogUpdateStatusSubsChannel();

    @Output("BlogCreationOutput")
    MessageChannel blogCreationPubChannel();

    @Input("BlogNumberPerCategoryInput")
    SubscribableChannel blogNumberPerCategoryInputPubChannel();

    @Input("BlogNumberPerCategoryInputV2")
    SubscribableChannel blogNumberPerCategoryInputPubChannelV2();

    @Output("BlogCategoryStatisticOutput")
    MessageChannel blogCategoryStatisticOutputPubChannel();
}
