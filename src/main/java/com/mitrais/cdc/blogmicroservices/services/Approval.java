package com.mitrais.cdc.blogmicroservices.services;

import com.mitrais.cdc.blogmicroservices.payload.BlogApprovalInProgress;
import com.mitrais.cdc.blogmicroservices.payload.Key;

public interface Approval {
    void subscribeBlogUpdateStatusMessage(BlogApprovalInProgress blogApprovalInProgress);
    void subscribeBlogSendUpdateStatusNotification(BlogApprovalInProgress blogApprovalInProgress);
    void subsKey(Key key);
}

