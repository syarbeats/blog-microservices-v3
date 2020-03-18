package com.mitrais.cdc.blogmicroservices.controller;

import com.mitrais.cdc.blogmicroservices.payload.BlogNumberPerCategory;
import com.mitrais.cdc.blogmicroservices.payload.BlogStatistic;
import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import com.mitrais.cdc.blogmicroservices.payload.RowNum;
import com.mitrais.cdc.blogmicroservices.services.ReactivePostServices;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ReactivePostController {

    private ReactivePostServices reactivePostServices;

    public ReactivePostController(ReactivePostServices reactivePostServices) {
        this.reactivePostServices = reactivePostServices;
    }

    @PostMapping("/posts-reactive")
    public Single<ResponseEntity<PostPayload>> createPost(@Valid @RequestBody PostPayload postDTO) throws URISyntaxException {
        return reactivePostServices.save(postDTO)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok().body(payload));
    }

    @PutMapping("/posts-reactive")
    public Single<ResponseEntity<PostPayload>> updatePost(@Valid @RequestBody PostPayload postDTO)  {
        return reactivePostServices.save(postDTO)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok().body(payload));
    }

    @GetMapping("/posts-reactive")
    public Single<ResponseEntity<List<PostPayload>>> getAllPosts(Pageable pageable) {
        return reactivePostServices.findAll(pageable)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok().body(payload.getContent()));
    }

    @GetMapping("/posts-reactive/{id}")
    public Single<ResponseEntity<PostPayload>> getPost(@PathVariable Long id) {
       return reactivePostServices.findOne(id)
               .subscribeOn(Schedulers.io())
               .map(postPayload -> ResponseEntity.ok(postPayload.get()));
    }

    @GetMapping("/post-reactive")
    public Single<ResponseEntity<PostPayload>> getPostByTitle(@RequestParam String title) {
        return reactivePostServices.findByTitle(title)
                .subscribeOn(Schedulers.io())
                .map(postPayload -> ResponseEntity.ok(postPayload.get()));
    }

    @DeleteMapping("/posts-reactive/{id}")
    public Single<ResponseEntity<String>> deletePost(@PathVariable Long id) {
        return reactivePostServices.delete(id)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload));
    }

    @GetMapping("/posts/category-reactive")
    public Single<ResponseEntity<List<PostPayload>>> findPostsByCategory(Pageable pageable, @RequestParam("category") String category){
        return reactivePostServices.findByCategory(pageable, category)
                .subscribeOn(Schedulers.io())
                .map(postPayloads -> ResponseEntity.ok(postPayloads.getContent()));
    }

    @GetMapping("/posts/today-reactive")
    public Single<ResponseEntity<List<PostPayload>>> findPostByToday(Pageable pageable){
        ZonedDateTime beforeToday = ZonedDateTime.now().minusDays(1);
        ZonedDateTime today = ZonedDateTime.now();

        return reactivePostServices.findByCreatedDate(pageable, today, beforeToday, true)
                .subscribeOn(Schedulers.io())
                .map(postPayloads -> ResponseEntity.ok(postPayloads.getContent()));
    }

    @GetMapping("/posts/search-reactive")
    public Single<ResponseEntity<List<PostPayload>>> findPostByKeyword(@RequestParam("keyword") String keyword, Pageable pageable){
        return reactivePostServices.findByKeywords(pageable,keyword)
                .subscribeOn(Schedulers.io())
                .map(postPayloads -> ResponseEntity.ok(postPayloads.getContent()));
    }

    @GetMapping("/posts/report-reactive")
    public Single<ResponseEntity<List<BlogStatistic>>> getBlogNumberPerCategory(Pageable pageable){
        return reactivePostServices.getBlogNumberPerCategory(pageable)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload));
    }

    @GetMapping("/posts/report-v2-reactive")
    public Single<ResponseEntity<List<BlogNumberPerCategory>>> getBlogNumberPerCategoryV2(Pageable pageable){
        return reactivePostServices.getBlogNumberPerCategoryV2(pageable)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload));

    }

    @GetMapping("/posts/blog-rownum-reactive")
    public ResponseEntity<RowNum> getBlogNumber(){
        return ResponseEntity.ok(reactivePostServices.getBlogRowNum());
    }
}
