package com.mitrais.cdc.blogmicroservices.repository;

import com.mitrais.cdc.blogmicroservices.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Comment entity.
 */

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByComment(String comment);

    @Query("SELECT c FROM Comment c WHERE c.post.title = :title")
    Page<Comment> findAllCommentByTitle(Pageable pageable, String title);


}
