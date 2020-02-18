package com.mitrais.cdc.blogmicroservices.services;

import com.mitrais.cdc.blogmicroservices.payload.BlogApprovalInProgress;

public interface Approval {
    void subscribeBlogUpdateStatusMessage(BlogApprovalInProgress blogApprovalInProgress);
}
