package com.mitrais.cdc.blogmicroservices.services;

import com.mitrais.cdc.blogmicroservices.payload.CommentPayload;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    CommentPayload save(CommentPayload commentDTO);
    List<CommentPayload> findAll();
    Optional<CommentPayload> findOne(Long id);
    Optional<CommentPayload> findByComment(String comment);
    void delete(Long id);
    List<CommentPayload> findAllCommentByPostTitle(String title);
}
