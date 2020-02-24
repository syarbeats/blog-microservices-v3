package com.mitrais.cdc.blogmicroservices.services.impl;

import com.mitrais.cdc.blogmicroservices.payload.BlogApprovalInProgress;
import com.mitrais.cdc.blogmicroservices.payload.Key;
import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import com.mitrais.cdc.blogmicroservices.services.Approval;
import com.mitrais.cdc.blogmicroservices.services.PostService;
import com.mitrais.cdc.blogmicroservices.utility.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@Slf4j
public class ApprovalImpl implements Approval {

    private PostService postService;
    private String key;

    @Autowired
    RestTemplate restTemplate;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


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

        log.info("Token update status:"+UserContextHolder.getContext().getAuthToken());

    }

    @Override
    @StreamListener("BlogNotificationInput")
    public void subscribeBlogSendUpdateStatusNotification(BlogApprovalInProgress blogApprovalInProgress) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("TOKEN:"+ getKey());

        String message = null;

        if(blogApprovalInProgress.getApprovalProgress().equals("Done")){
            message = "Approval process for "+blogApprovalInProgress.getTitle()+"have already done with status "+ (blogApprovalInProgress.isStatus() ? "Approved" : "Rejected");
        }else{
            message = "Approval process for "+blogApprovalInProgress.getTitle()+"is still "+blogApprovalInProgress.getApprovalProgress();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "Spring's RestTemplate" );  // value can be whatever
        /*headers.add("Authorization", "Bearer "+  getKey());*/
        ResponseEntity<String> testResponse = restTemplate.exchange("http://APPROVAL/send/message?message="+message,
                HttpMethod.GET, new HttpEntity<>("parameters", headers),  String.class);
    }

    @Override
    @StreamListener("BlogKey")
    public void subsKey(Key key) {
        log.info("KEY:"+key.getKey());
        setKey(key.getKey());
    }
}
