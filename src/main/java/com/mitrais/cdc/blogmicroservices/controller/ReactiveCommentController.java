package com.mitrais.cdc.blogmicroservices.controller;

import com.mitrais.cdc.blogmicroservices.payload.CommentPayload;
import com.mitrais.cdc.blogmicroservices.services.ReactiveCommentServices;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReactiveCommentController {

    private ReactiveCommentServices reactiveCommentServices;

    public ReactiveCommentController(ReactiveCommentServices reactiveCommentServices) {
        this.reactiveCommentServices = reactiveCommentServices;
    }

    @PostMapping("/comments-reactive")
    public Single<ResponseEntity<CommentPayload>> createComment(@RequestBody CommentPayload commentDTO) throws URISyntaxException {
        return reactiveCommentServices.save(commentDTO)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok().body(payload));
    }

    @PutMapping("/comments-reactive")
    public Single<ResponseEntity<CommentPayload>> updateComment(@RequestBody CommentPayload commentDTO) throws URISyntaxException {
        return reactiveCommentServices.save(commentDTO)
                .subscribeOn(Schedulers.io())
                .map(commentPayload -> ResponseEntity.ok().body(commentPayload));
    }

    @GetMapping("/comments-reactive")
    public Single<ResponseEntity<List<CommentPayload>>> getAllComments() {
        return reactiveCommentServices.findAll()
                .subscribeOn(Schedulers.io())
                .map(commentPayloads -> ResponseEntity.ok().body(commentPayloads));
    }

    @GetMapping("/comments-reactive/{id}")
    public Single<ResponseEntity<CommentPayload>> getComment(@PathVariable Long id) {
        return reactiveCommentServices.findOne(id)
                .subscribeOn(Schedulers.io())
                .map(commentPayload -> ResponseEntity.ok(commentPayload.get()));
    }

    @DeleteMapping("/comments-reactive/{id}")
    public Single<ResponseEntity<String>> deleteComment(@PathVariable Long id) {
        return reactiveCommentServices.delete(id)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload));
    }

    @GetMapping("/comments-by-title-reactive")
    public Single<ResponseEntity<List<CommentPayload>>> getCommentByTitle(@RequestParam String title, Pageable pageable){
        return reactiveCommentServices.findAllCommentByTitleV2(pageable,title)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload.getContent()));
    }

    @GetMapping("/comment-by-comment-reactive")
    public Single<ResponseEntity<CommentPayload>> getCommenDatatByComment(@RequestParam String comment){
        return reactiveCommentServices.findByComment(comment)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload.get()));
    }
}
