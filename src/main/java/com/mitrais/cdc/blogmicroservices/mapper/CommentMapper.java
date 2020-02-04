package com.mitrais.cdc.blogmicroservices.mapper;

import com.mitrais.cdc.blogmicroservices.entity.Comment;
import com.mitrais.cdc.blogmicroservices.payload.CommentPayload;


import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {PostMapper.class})
public interface CommentMapper extends EntityMapper<CommentPayload, Comment> {

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "post.title", target = "postTitle")
    CommentPayload toDto(Comment comment);

    @Mapping(source = "postId", target = "post")
    Comment toEntity(CommentPayload commentDTO);

    default Comment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(id);
        return comment;
    }
}
