package com.mitrais.cdc.blogmicroservices.services.impl;

import com.mitrais.cdc.blogmicroservices.entity.Comment;
import com.mitrais.cdc.blogmicroservices.payload.CommentPayload;
import com.mitrais.cdc.blogmicroservices.services.CommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceImplTest {

    @Autowired
    CommentService commentService;

    @Test
    @Transactional
    @Rollback(true)
    public void save() {
        CommentPayload payload = new CommentPayload("admin", "Test Comment", new Long(96), "Mastering Apache Camel");
        CommentPayload commentPayload =  commentService.save(payload);

        assertThat("admin", is(commentPayload.getUsername()));
        assertThat("Test Comment", is(commentPayload.getComment()));
        assertThat(String.valueOf(96), is(String.valueOf(commentPayload.getPostId())));
    }

    @Test
    public void findAll() {
        List<CommentPayload> commentPayload = commentService.findAll();

        assertThat(String.valueOf(81), is(String.valueOf(commentPayload.get(0).getId())));
        assertThat("admin", is(commentPayload.get(0).getUsername()));
        assertThat("Test Comment", is(commentPayload.get(0).getComment()));
        assertThat(String.valueOf(95), is(String.valueOf(commentPayload.get(0).getPostId())));
        assertThat(("Test image 7").trim(), is(commentPayload.get(0).getPostTitle().trim()));
    }

    @Test
    public void findOne() {
        CommentPayload commentPayload = commentService.findOne(new Long(81)).get();

        assertThat("admin", is(commentPayload.getUsername()));
        assertThat("Test Comment", is(commentPayload.getComment()));
        assertThat(String.valueOf(95), is(String.valueOf(commentPayload.getPostId())));
        assertThat(("Test image 7").trim(), is(commentPayload.getPostTitle().trim()));
    }

    @Test
    public void findByComment() {
        CommentPayload commentPayload = commentService.findByComment("Ini update comment untuk post Test Blog").get();

        assertThat(String.valueOf(95), is(String.valueOf(commentPayload.getId())));
        assertThat("admin", is(commentPayload.getUsername()));
        assertThat(String.valueOf(98), is(String.valueOf(commentPayload.getPostId())));
        assertThat(("SQL Join").trim(), is(commentPayload.getPostTitle().trim()));
    }

    @Test
    @Transactional
    public void delete() {
        CommentPayload payload = new CommentPayload("admin", "Test Comment 212", new Long(96), "Mastering Apache Camel");
        CommentPayload commentPayload =  commentService.save(payload);

        commentService.delete(commentPayload.getId());
        Optional<CommentPayload> searchPayload = commentService.findOne(commentPayload.getId());
        assertTrue(!searchPayload.isPresent());
    }

    @Test
    public void findAllCommentByPostTitle() {

        List<CommentPayload> commentPayloadList = commentService.findAllCommentByPostTitle("Mastering Apache Camel");

        assertThat(String.valueOf(84), is(String.valueOf(commentPayloadList.get(0).getId())));
        assertThat("admin", is(commentPayloadList.get(0).getUsername()));
        assertThat("Test Comment", is(commentPayloadList.get(0).getComment()));
        assertThat(String.valueOf(96), is(String.valueOf(commentPayloadList.get(0).getPostId())));

    }
}