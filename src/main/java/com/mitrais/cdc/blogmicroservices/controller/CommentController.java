/**
 * <h1>Comment Controller</h1>
 * This class will be used to setup controller for
 * Comment API.
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */

package com.mitrais.cdc.blogmicroservices.controller;

import com.mitrais.cdc.blogmicroservices.exception.BadRequestAlertException;
import com.mitrais.cdc.blogmicroservices.payload.CommentPayload;
import com.mitrais.cdc.blogmicroservices.services.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;


@RestController
@RequestMapping("/api")
public class CommentController extends CrossOriginController{

    private final Logger log = LoggerFactory.getLogger(CommentController.class);

    private static final String ENTITY_NAME = "blogComment";

    @Value("${spring.application.name}")
    private String applicationName;

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * This method will be used to expose Create Comment API
     * for certain Blog.
     *
     * @param commentDTO
     * @return will return created comment data
     * @throws URISyntaxException
     */
    @PostMapping("/comments")
    public ResponseEntity<CommentPayload> createComment(@RequestBody CommentPayload commentDTO) throws URISyntaxException {
        log.debug("REST request to save Comment : {}", commentDTO);
        if (commentDTO.getId() != null) {
            throw new BadRequestAlertException("A new comment cannot already have an ID", ENTITY_NAME, "idexists");
        }

        return ResponseEntity.ok().body(commentService.save(commentDTO));
    }

    /**
     * This method will be used to expose update comment API
     * for certain Blog.
     *
     * @param commentDTO
     * @return will return updated comment data
     * @throws URISyntaxException
     */
    @PutMapping("/comments")
    public ResponseEntity<CommentPayload> updateComment(@RequestBody CommentPayload commentDTO) throws URISyntaxException {
        log.debug("REST request to update Comment : {}", commentDTO);
        if (commentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        return ResponseEntity.ok().body(commentService.save(commentDTO));
    }

    /**
     * This method will be used to expose get the whole comment api.
     *
     * @return will return all comment data
     */
    @GetMapping("/comments")
    public List<CommentPayload> getAllComments() {
        log.debug("REST request to get all Comments");
        return commentService.findAll();
    }

    /**
     * This method will be used to expose get comment api
     * for certain comment id.
     *
     * @param id
     * @return will return the comment data for the given comment id
     */
    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentPayload> getComment(@PathVariable Long id) {
        log.debug("REST request to get Comment : {}", id);

        return ResponseEntity.ok(commentService.findOne(id).get());
    }

    /**
     * This method will be used to expose delete comment api
     * for certain comment id.
     *
     * @param id
     * @return will return deleted comment data
     */
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<CommentPayload> deleteComment(@PathVariable Long id) {
        log.debug("REST request to delete Comment : {}", id);
        CommentPayload commentPayload = commentService.findOne(id).get();
        commentService.delete(id);
        return ResponseEntity.ok(commentPayload);
    }

    /**
     * This method will be used to expose get comment data api
     * for certain title.
     *
     * @param title
     * @return will return comment data for the given title
     */
    @GetMapping("/comments-by-title")
    public ResponseEntity<List<CommentPayload>> getCommentByTitle(@RequestParam String title){
        log.debug("Get All Comment for certain post title");

        return ResponseEntity.ok(commentService.findAllCommentByPostTitle(title));
    }

    /**
     * This method will be used to expose get comment data api
     * for certain comment.
     *
     * @param comment
     * @return will return the comment data for the given blog comment.
     */
    @GetMapping("/comment-by-comment")
    public ResponseEntity<CommentPayload> getCommenDatatByComment(@RequestParam String comment){
        log.debug("Get Comment Data for certain comment");

        return ResponseEntity.ok(commentService.findByComment(comment).get());
    }
}
