package com.mitrais.cdc.blogmicroservices.repository;

import com.mitrais.cdc.blogmicroservices.entity.Post;
import com.mitrais.cdc.blogmicroservices.payload.BlogNumberPerCategory;
import com.mitrais.cdc.blogmicroservices.payload.RowNum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;


/**
 * Spring Data  repository for the Post entity.
 */

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByTitle(String title);

    @Query("SELECT p FROM Post p, Category c WHERE p.category = c AND c.name=:category")
    Page<Post> findByCategory(Pageable pageable, String category);

    @Query("SELECT p FROM Post p WHERE  p.createdDate > :oneDayBeforeCreatedDate AND p.createdDate < :createdDate AND p.status= :status")
    Page<Post> findByCreatedDate(Pageable pageable, ZonedDateTime createdDate, ZonedDateTime oneDayBeforeCreatedDate, boolean status);

    @Query("SELECT p FROM Post p WHERE  upper(p.title) LIKE CONCAT('%',upper(:keyword),'%')")
    Page<Post> findByKeyword(Pageable pageable, String keyword);

    @Query("SELECT p FROM Post p WHERE  p.status = :status")
    Page<Post> getAllApprovedBlog(Pageable pageable, boolean status);

    @Query("SELECT new com.mitrais.cdc.blogmicroservices.payload.BlogNumberPerCategory(count(p.category.name), p.category.name)  FROM Post p GROUP BY p.category.name")
    Page<BlogNumberPerCategory> getBlogNumberPercategory(Pageable pageable);

    @Query("SELECT new com.mitrais.cdc.blogmicroservices.payload.RowNum(count(p)) FROM Post p")
    RowNum getBlogNumber();
}
