package com.mitrais.cdc.blogmicroservices.services;

import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.Optional;


public interface PostService {

    PostPayload save(PostPayload postDTO);
    Page<PostPayload> findAll(Pageable pageable);
    Optional<PostPayload> findOne(Long id);
    Optional<PostPayload> findByTitle(String title);
    void delete(Long id);
    Page<PostPayload> findByCategory(Pageable pageable, String category);
    Page<PostPayload> findByCreatedDate(Pageable pageable, ZonedDateTime createdDate, ZonedDateTime oneDayBeforeCreatedDate);
    Page<PostPayload> findByKeywords(Pageable pageable, String keyword);
}
