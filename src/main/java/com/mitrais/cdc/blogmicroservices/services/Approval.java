package com.mitrais.cdc.blogmicroservices.services;

import com.mitrais.cdc.blogmicroservices.payload.*;

import java.util.List;

public interface Approval {
    void subscribeBlogUpdateStatusMessage(BlogApprovalInProgress blogApprovalInProgress);

    void subscribeBlogSendUpdateStatusNotification(BlogApprovalInProgress blogApprovalInProgress);

    void subsKey(Key key);

    void sendUpdateChart(BlogApprovalInProgress blogApprovalInProgress);

    void sendUpdateChartV2(BlogApprovalInProgress blogApprovalInProgress);

    void sendUpdateApprovalStatisticChart(List<ApprovalNumberPerProgress> approvalNumberPerProgress);

    void sendUpdateApprovalResultStatistic(List<ApprovalResultStatistic> approvalResultStatistic);

    void sendUpdateApprovalResultStatisticV2(List<ApprovalNumberPerProgressResponse> approvalNumberPerProgressResponses);

    void sendUpdateApprovalStatisticChartV2(List<ApprovalNumberPerProgressResponse> approvalNumberPerProgressResponses);
}
