package com.mitrais.cdc.blogmicroservices.services;

import com.mitrais.cdc.blogmicroservices.entity.Comment;
import com.mitrais.cdc.blogmicroservices.payload.CommentPayload;
import io.reactivex.Single;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReactiveCommentServices {
    Single<CommentPayload> save(CommentPayload commentDTO);
    Single<List<CommentPayload>> findAll();
    Single<Optional<CommentPayload>> findOne(Long id);
    Single<Optional<CommentPayload>> findByComment(String comment);
    Single<String> delete(Long id);
    Single<List<CommentPayload>> findAllCommentByPostTitle(String title);
    Single<Page<CommentPayload>> findAllCommentByTitleV2(Pageable pageable, String title);
}
