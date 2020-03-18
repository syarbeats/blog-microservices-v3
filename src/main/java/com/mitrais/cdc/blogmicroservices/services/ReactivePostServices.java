package com.mitrais.cdc.blogmicroservices.services;

import com.mitrais.cdc.blogmicroservices.payload.BlogNumberPerCategory;
import com.mitrais.cdc.blogmicroservices.payload.BlogStatistic;
import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import com.mitrais.cdc.blogmicroservices.payload.RowNum;
import io.reactivex.Single;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface ReactivePostServices {

    Single<PostPayload> save(PostPayload postDTO);
    Single<Page<PostPayload>> findAll(Pageable pageable);
    Single<Optional<PostPayload>> findOne(Long id);
    Single<Optional<PostPayload>> findByTitle(String title);
    Single<String> delete(Long id);
    Single<Page<PostPayload>> findByCategory(Pageable pageable, String category);
    Single<Page<PostPayload>> findByCreatedDate(Pageable pageable, ZonedDateTime createdDate, ZonedDateTime oneDayBeforeCreatedDate, boolean status);
    Single<Page<PostPayload>> findByKeywords(Pageable pageable, String keyword);
    Single<List<BlogStatistic>> getBlogNumberPerCategory(Pageable pageable);
    Single<List<BlogNumberPerCategory>> getBlogNumberPerCategoryV2(Pageable pageable);
    Single<List<BlogStatistic>> getBlogNumberPerCategory();
    Single<List<BlogNumberPerCategory>> getBlogNumberPerCategoryV2();
    RowNum getBlogRowNum();
}
