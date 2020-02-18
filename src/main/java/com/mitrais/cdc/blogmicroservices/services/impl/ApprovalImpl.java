package com.mitrais.cdc.blogmicroservices.services.impl;

import com.mitrais.cdc.blogmicroservices.payload.BlogApprovalInProgress;
import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import com.mitrais.cdc.blogmicroservices.services.Approval;
import com.mitrais.cdc.blogmicroservices.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApprovalImpl implements Approval {

    private PostService postService;

    public ApprovalImpl(PostService postService){
        this.postService = postService;
    }

    @Override
    @StreamListener("BlogUpdateStatusInput")
    public void subscribeBlogUpdateStatusMessage(BlogApprovalInProgress blogApprovalInProgress) {
        log.info("Receive Blog Update Statue data:"+blogApprovalInProgress.getTitle());
        PostPayload postPayload = postService.findByTitle(blogApprovalInProgress.getTitle()).get();
        postPayload.setStatus(blogApprovalInProgress.isStatus());
        postService.save(postPayload);
        log.info("Blog status updated to {}", postPayload.isStatus());
    }
}
