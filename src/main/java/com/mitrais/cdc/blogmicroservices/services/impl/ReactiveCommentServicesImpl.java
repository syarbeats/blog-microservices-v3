package com.mitrais.cdc.blogmicroservices.services.impl;

import com.mitrais.cdc.blogmicroservices.entity.Comment;
import com.mitrais.cdc.blogmicroservices.mapper.CommentMapper;
import com.mitrais.cdc.blogmicroservices.payload.CommentPayload;
import com.mitrais.cdc.blogmicroservices.repository.CommentRepository;
import com.mitrais.cdc.blogmicroservices.services.ReactiveCommentServices;
import io.reactivex.Single;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReactiveCommentServicesImpl implements ReactiveCommentServices {

    private CommentRepository commentRepository;
    private CommentMapper commentMapper;

    public ReactiveCommentServicesImpl(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public Single<CommentPayload> save(CommentPayload commentDTO) {
        return Single.create(save -> save.onSuccess(commentMapper.toDto(commentRepository.save(commentMapper.toEntity(commentDTO)))));
    }

    @Override
    public Single<List<CommentPayload>> findAll() {
        return Single.create(find -> find.onSuccess(commentRepository.findAll().stream().map(commentMapper::toDto).collect(Collectors.toCollection(LinkedList::new))));
    }

    public List<CommentPayload> findAllComment() {
        return commentRepository.findAll().stream().map(commentMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Single<Optional<CommentPayload>> findOne(Long id) {
        return Single.create(find -> find.onSuccess(commentRepository.findById(id).map(commentMapper::toDto)));
    }

    @Override
    public Single<Optional<CommentPayload>> findByComment(String comment) {
        return Single.create(find -> find.onSuccess(commentRepository.findByComment(comment).map(commentMapper::toDto)));
    }

    @Override
    public Single<String> delete(Long id) {
        return Single.create(delete -> {
            commentRepository.deleteById(id);
            delete.onSuccess("Comment with id:"+id+" Has been deleted successfully.");
        });
    }

    @Override
    public Single<List<CommentPayload>> findAllCommentByPostTitle(String title) {
        return Single.create(find -> {
            List<CommentPayload> commentPayloads = findAllComment();
            List<CommentPayload> commentPayloadsforThisTitle = new ArrayList<CommentPayload>();

            for(CommentPayload commentPayload : commentPayloads ){
                if(commentPayload.getPostTitle().equals(title)){
                    commentPayloadsforThisTitle.add(commentPayload);
                }
            }

            find.onSuccess(commentPayloadsforThisTitle);
        });
    }

    @Override
    public Single<Page<Comment>> findAllCommentByTitleV2(Pageable pageable, String title) {
        return Single.create(find -> find.onSuccess(commentRepository.findAllCommentByTitle(pageable, title)));
    }


}
