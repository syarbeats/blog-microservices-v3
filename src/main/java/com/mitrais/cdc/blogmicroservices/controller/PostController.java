/**
 * <h1>Post Controller</h1>
 * This class will be used to setup controller for
 * Post API.
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */

package com.mitrais.cdc.blogmicroservices.controller;

import com.mitrais.cdc.blogmicroservices.exception.BadRequestAlertException;
import com.mitrais.cdc.blogmicroservices.payload.BlogNumberPerCategory;
import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import com.mitrais.cdc.blogmicroservices.payload.RowNum;
import com.mitrais.cdc.blogmicroservices.services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class PostController extends CrossOriginController{

    private final Logger log = LoggerFactory.getLogger(PostController.class);

    private static final String ENTITY_NAME = "blogPost";

    @Value("${spring.application.name}")
    private String applicationName;

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * This method will be used to expose Create Blog API.
     *
     * @param postDTO
     * @return will return the created blog api
     * @throws URISyntaxException
     */
    @PostMapping("/posts")
    public ResponseEntity<PostPayload> createPost(@Valid @RequestBody PostPayload postDTO) throws URISyntaxException {
        log.debug("REST request to save Post : {}", postDTO);
        log.info("REST request to save Post : {}", postDTO);
        ZonedDateTime zone =ZonedDateTime.now();
        postDTO.setCreatedDate(zone);
        postDTO.setStatus(false);

        if (postDTO.getId() != null) {
            throw new BadRequestAlertException("A new post cannot already have an ID", ENTITY_NAME, "id exists");
        }

        return ResponseEntity.ok().body(postService.save(postDTO));

    }

    /**
     * This method will be used to expose update blog api
     * for certain Blog Id.
     *
     * @param postDTO
     * @return will return updated blog data
     */
    @PutMapping("/posts")
    public ResponseEntity<PostPayload> updatePost(@Valid @RequestBody PostPayload postDTO)  {
        log.debug("REST request to update Post : {}", postDTO);
        if (postDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        return ResponseEntity.ok().body(postService.save(postDTO));
    }

    /**
     * This method will be used to expose get the whole blog data api.
     *
     * @param pageable
     * @return will return the list of blog data
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostPayload>> getAllPosts(Pageable pageable) {
        log.debug("REST request to get a page of Posts");

        return ResponseEntity.ok().body(postService.findAll(pageable).getContent());
    }

    /**
     * This method will be used to expose get Blog data api
     * for certain blog id.
     *
     * @param id
     * @return will return blog data for the given blog id
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostPayload> getPost(@PathVariable Long id) {
        log.debug("REST request to get Post : {}", id);
        Optional<PostPayload> postDTO = postService.findOne(id);
        PostPayload result = null;

        if(postDTO.isPresent()){
           result = postDTO.get();
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(result);
    }

    /**
     * This method will be used to expose get Blog data api
     * for certain blog title.
     *
     * @param title
     * @return will return blog data for the given title
     */
    @GetMapping("/post")
    public ResponseEntity<PostPayload> getPostByTitle(@RequestParam String title) {
        log.debug("REST request to get Post : {}", title);
        Optional<PostPayload> postDTO = postService.findByTitle(title);
        PostPayload result = null;

        if(postDTO.isPresent()){
            result = postDTO.get();
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(result);
    }

    /**
     * This method will be used to expose delete blog data api
     * for certain blog id.
     *
     * @param id
     * @return will return deleted blog data
     */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<PostPayload> deletePost(@PathVariable Long id) {
        log.debug("REST request to delete Post : {}", id);
        Optional<PostPayload> postPayload = postService.findOne(id);
        PostPayload result = null;

        if(postPayload.isPresent()){
            result = postPayload.get();
            postService.delete(id);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(result);
    }

    /**
     * This method will be used to expose get Blog data api
     * for certain category.
     *
     * @param pageable
     * @param category
     * @return will return list of blog for the given category
     */
    @GetMapping("/posts/category")
    public ResponseEntity<List<PostPayload>> findPostsByCategory(Pageable pageable, @RequestParam("category") String category){
        log.debug("REST request to get posts by category {}", category);

        return ResponseEntity.ok(postService.findByCategory(pageable, category).getContent());
    }

    /**
     * This method will be used to expose get Blog data api
     * for today posting.
     *
     * @param pageable
     * @return will return the list of blog for today posting
     */
    @GetMapping("/posts/today")
    public ResponseEntity<List<PostPayload>> findPostByToday(Pageable pageable){
        ZonedDateTime beforeToday = ZonedDateTime.now().minusDays(1);
        ZonedDateTime today = ZonedDateTime.now();

        return ResponseEntity.ok(postService.findByCreatedDate(pageable, today, beforeToday, true).getContent());

    }

    /**
     * This method will be used to expose blog data api
     * for certain keyword.
     *
     * @param keyword
     * @param pageable
     * @return will return the list of blog data for the given keyword
     */
    @GetMapping("/posts/search")
    public ResponseEntity<List<PostPayload>> findPostByKeyword(@RequestParam("keyword") String keyword, Pageable pageable){
        log.debug("REST request to get posts by keyword");

        return ResponseEntity.ok(postService.findByKeywords(pageable, keyword).getContent());
    }

    /**
     * This method will be used to get Blog Number
     * per Category.
     *
     * @param pageable
     * @return will return the num for each category
     */
    @GetMapping("/posts/report")
    public ResponseEntity<List<BlogNumberPerCategory>> getBlogNumberPerCategory(Pageable pageable){
        log.debug("REST request to get blog number per category");

        return ResponseEntity.ok(postService.getBlogNumberPerCategory(pageable).getContent());
    }

    /**
     * This method will be used to get blog number.
     * @return will return the row num of the whole blog.
     */
    @GetMapping("/posts/blog-rownum")
    public ResponseEntity<RowNum> getBlogNumber(){
        log.debug("REST request to get blog number per category");

        return ResponseEntity.ok(postService.getBlogRowNum());
    }
}
