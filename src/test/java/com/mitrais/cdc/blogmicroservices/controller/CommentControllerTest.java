package com.mitrais.cdc.blogmicroservices.controller;

import com.mitrais.cdc.blogmicroservices.payload.CommentPayload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentControllerTest {

    @Autowired
    CommentController commentController;

    @Test
    @Transactional
    @Rollback(true)
    public void createComment() throws URISyntaxException {
        CommentPayload payload = new CommentPayload("admin", "Test Comment", new Long(96), "Mastering Apache Camel");
        ResponseEntity<CommentPayload> responseEntity =  commentController.createComment(payload);

        assertThat("admin", is(responseEntity.getBody().getUsername()));
        assertThat("Test Comment", is(responseEntity.getBody().getComment()));
        assertThat(String.valueOf(96), is(String.valueOf(responseEntity.getBody().getPostId())));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void updateComment() throws URISyntaxException {
        ResponseEntity<CommentPayload> searchEntity = commentController.getComment(new Long(81));
        CommentPayload commentPayload = searchEntity.getBody();
        commentPayload.setComment("Update Comment for PostId 95");

        ResponseEntity<CommentPayload> responseEntity = commentController.updateComment(commentPayload);
        assertThat("admin", is(responseEntity.getBody().getUsername()));
        assertThat("Update Comment for PostId 95", is(responseEntity.getBody().getComment()));
        assertThat(String.valueOf(95), is(String.valueOf(responseEntity.getBody().getPostId())));
    }

    @Test
    public void getAllComments() {
        List<CommentPayload> commentPayloadList = commentController.getAllComments();

        assertThat(String.valueOf(81), is(String.valueOf(commentPayloadList.get(0).getId())));
        assertThat("admin", is(commentPayloadList.get(0).getUsername()));
        assertThat("Test Comment", is(commentPayloadList.get(0).getComment()));
        assertThat(String.valueOf(95), is(String.valueOf(commentPayloadList.get(0).getPostId())));
    }

    @Test
    public void getComment() {
        ResponseEntity<CommentPayload> responseEntity = commentController.getComment(new Long(81));

        assertThat("admin", is(responseEntity.getBody().getUsername()));
        assertThat("Test Comment", is(responseEntity.getBody().getComment()));
        assertThat(String.valueOf(95), is(String.valueOf(responseEntity.getBody().getPostId())));
        assertThat("Test image 7", is(String.valueOf(responseEntity.getBody().getPostTitle())));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void deleteComment() {
        ResponseEntity<CommentPayload> responseEntity = commentController.deleteComment(new Long(81));

        assertThat("admin", is(responseEntity.getBody().getUsername()));
        assertThat("Test Comment", is(responseEntity.getBody().getComment()));
        assertThat(String.valueOf(95), is(String.valueOf(responseEntity.getBody().getPostId())));
    }

    @Test
    public void getCommentByTitle() {
        ResponseEntity<List<CommentPayload>> responseEntity = commentController.getCommentByTitle("Mastering Apache Camel");

        assertThat(String.valueOf(84), is(String.valueOf(responseEntity.getBody().get(0).getId())));
        assertThat("admin", is(responseEntity.getBody().get(0).getUsername()));
        assertThat("Test Comment", is(responseEntity.getBody().get(0).getComment()));
        assertThat(String.valueOf(96), is(String.valueOf(responseEntity.getBody().get(0).getPostId())));
    }

    @Test
    public void getCommenDatatByComment() {

        ResponseEntity<CommentPayload> responseEntity = commentController.getCommenDatatByComment("Ini update comment untuk post Test Blog");

        assertThat(String.valueOf(95), is(String.valueOf(responseEntity.getBody().getId())));
        assertThat("admin", is(responseEntity.getBody().getUsername()));
        assertThat(String.valueOf(98), is(String.valueOf(responseEntity.getBody().getPostId())));
        assertThat(("SQL Join").trim(), is(responseEntity.getBody().getPostTitle().trim()));
    }
}