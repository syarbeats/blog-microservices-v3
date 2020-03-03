package com.mitrais.cdc.blogmicroservices.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitrais.cdc.blogmicroservices.payload.*;
import com.mitrais.cdc.blogmicroservices.services.Approval;
import com.mitrais.cdc.blogmicroservices.services.KafkaService;
import com.mitrais.cdc.blogmicroservices.services.PostService;
import com.mitrais.cdc.blogmicroservices.utility.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ApprovalImpl implements Approval {

    private PostService postService;
    private String key;
    private KafkaService kafkaService;

    @Autowired
    RestTemplate restTemplate;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public ApprovalImpl(PostService postService, KafkaService kafkaService){
        this.kafkaService = kafkaService;
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

        sendMessage(message, "message", null, "", null, null);

       /* HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "Spring's RestTemplate" );  // value can be whatever
        *//*headers.add("Authorization", "Bearer "+  getKey());*//*
        ResponseEntity<String> testResponse = restTemplate.exchange("http://APPROVAL/send/message?message="+message,
                HttpMethod.GET, new HttpEntity<>("parameters", headers),  String.class);*/
    }

    @Override
    @StreamListener("BlogKey")
    public void subsKey(Key key) {
        log.info("KEY:"+key.getKey());
        setKey(key.getKey());
    }

    @Override
    @StreamListener("BlogNumberPerCategoryInput")
    public void sendUpdateChart(BlogApprovalInProgress blogApprovalInProgress) {
        log.info("Update Chart DATA......with data:"+blogApprovalInProgress.getTitle()+" Category: "+blogApprovalInProgress.getCategoryName());


        List<BlogStatistic> blogStatisticList = postService.getBlogNumberPerCategory();
        long rownum =  postService.getBlogRowNum().getRownum();

        for(int i=0; i<blogStatisticList.size(); i++){
            if(blogStatisticList.get(i).getLabel().trim().equals(blogApprovalInProgress.getCategoryName().trim())){
                BlogStatistic blogStatistic = new BlogStatistic();
                blogStatistic.setLabel(blogStatisticList.get(i).getLabel());
                blogStatistic.setY(blogStatisticList.get(i).getY()+((new Double(1.0)))/new Double(rownum));
                blogStatisticList.set(i, blogStatistic);

            }
        }

        sendMessage("","", blogStatisticList, "", null, null);
        log.info("Updated chart data has been sent to websocket....");
        /*this.kafkaService.sendBlogStatistic(blogStatisticList);
        log.info("Updated statistic data has been sent to kafka channel...");*/
    }

    @Override
    @StreamListener("BlogNumberPerCategoryInputV2")
    public void sendUpdateChartV2(BlogApprovalInProgress blogApprovalInProgress) {
        log.info("Update Chart DATA......with data:"+blogApprovalInProgress.getTitle()+" Category: "+blogApprovalInProgress.getCategoryName());
        List<BlogNumberPerCategory> blogNumberPerCategoryList = postService.getBlogNumberPerCategoryV2();
        sendMessage("","", null, "amount", blogNumberPerCategoryList, null);
        log.info("Updated chart data has been sent to websocket....");

    }

    @Override
    @StreamListener("BlogApprovaltatisticInput")
    public void sendUpdateApprovalStatisticChart(List<ApprovalNumberPerProgress> approvalNumberPerProgress) {
        sendMessage("","",null,"approval-statistic", null, approvalNumberPerProgress);
    }

    @Override
    @StreamListener("BlogApprovalResultStatisticInput")
    public void sendUpdateApprovalResultStatistic(List<ApprovalResultStatistic> approvalResultStatistic) {
        sendUpdateApprovalResult(approvalResultStatistic);
    }

    @Override
    @StreamListener("BlogApprovalResultStatisticV2Input")
    public void sendUpdateApprovalResultStatisticV2(List<ApprovalNumberPerProgressResponse> approvalNumberPerProgressResponses) {
        sendUpdateApprovalResultV2(approvalNumberPerProgressResponses);
    }

    private void sendUpdateApprovalResultV2(List<ApprovalNumberPerProgressResponse> approvalNumberPerProgressResponses) {
        String testResponse = restTemplate.postForObject( "http://APPROVAL/update-approval-result-chart-v2", approvalNumberPerProgressResponses, String.class);
    }

    @Override
    @StreamListener("BlogApprovalStatisticV2Input")
    public void sendUpdateApprovalStatisticChartV2(List<ApprovalNumberPerProgressResponse> approvalNumberPerProgressResponses) {
        sendUpdateApprovalStatisticV2(approvalNumberPerProgressResponses);
    }

    private void sendUpdateApprovalStatisticV2(List<ApprovalNumberPerProgressResponse> approvalNumberPerProgressResponses) {
        String testResponse = restTemplate.postForObject( "http://APPROVAL/update-approval-statistic-v2-chart", approvalNumberPerProgressResponses, String.class);
    }

    public void sendMessage(String message, String topic, List<BlogStatistic> blogStatisticList, String mode, List<BlogNumberPerCategory> blogNumberPerCategoryList, List<ApprovalNumberPerProgress> approvalNumberPerProgress){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "Spring's RestTemplate" );  // value can be whatever

        if(topic.equals("")){
            log.info("Send update chart to websocket");

            if(mode.equals("")){
                log.info("Sent percentage");
                String testResponse = restTemplate.postForObject( "http://APPROVAL/update-chart", blogStatisticList, String.class);
            }else if(mode.equals("amount")){
                log.info("Sent Amount");
                String testResponse = restTemplate.postForObject( "http://APPROVAL/update-chart-v2", blogNumberPerCategoryList, String.class);
            }else if(mode.equals("approval-statistic")){
                String testResponse = restTemplate.postForObject( "http://APPROVAL/update-approval-chart", approvalNumberPerProgress, String.class);
            }

        }else {
            ResponseEntity<String> testResponse = restTemplate.exchange("http://APPROVAL/send/"+topic+"?message="+message,
                    HttpMethod.GET, new HttpEntity<>("parameters", headers),  String.class);
        }
    }

    public void sendUpdateApprovalResult(List<ApprovalResultStatistic> approvalResultStatistics){
        String testResponse = restTemplate.postForObject( "http://APPROVAL/update-approval-result-chart", approvalResultStatistics, String.class);
    }


}
