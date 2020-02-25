package com.mitrais.cdc.blogmicroservices.repository;

import com.mitrais.cdc.blogmicroservices.entity.Category;
import com.mitrais.cdc.blogmicroservices.entity.Post;
import com.mitrais.cdc.blogmicroservices.payload.CategoryPayload;
import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
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

}
