package com.mitrais.cdc.blogmicroservices.services;

import com.mitrais.cdc.blogmicroservices.payload.BlogApprovalInProgress;
import com.mitrais.cdc.blogmicroservices.payload.BlogStatistic;
import com.mitrais.cdc.blogmicroservices.payload.Key;
import com.mitrais.cdc.blogmicroservices.payload.PostPayload;

public interface Approval {
    void subscribeBlogUpdateStatusMessage(BlogApprovalInProgress blogApprovalInProgress);
    void subscribeBlogSendUpdateStatusNotification(BlogApprovalInProgress blogApprovalInProgress);
    void subsKey(Key key);
    void sendUpdateChart(BlogApprovalInProgress blogApprovalInProgress);
    void sendUpdateChartV2(BlogApprovalInProgress blogApprovalInProgress);
}

