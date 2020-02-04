package com.mitrais.cdc.blogmicroservices.services.impl;

import com.mitrais.cdc.blogmicroservices.entity.Comment;
import com.mitrais.cdc.blogmicroservices.mapper.CommentMapper;
import com.mitrais.cdc.blogmicroservices.payload.CommentPayload;
import com.mitrais.cdc.blogmicroservices.repository.CommentRepository;
import com.mitrais.cdc.blogmicroservices.services.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }


    @Override
    public CommentPayload save(CommentPayload commentDTO) {
        log.debug("Request to save Comment : {}", commentDTO);
        Comment comment = commentMapper.toEntity(commentDTO);
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentPayload> findAll() {
        log.debug("Request to get all Comments");
        return commentRepository.findAll().stream()
            .map(commentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentPayload> findOne(Long id) {
        log.debug("Request to get Comment : {}", id);
        return commentRepository.findById(id)
            .map(commentMapper::toDto);
    }

    @Override
    public Optional<CommentPayload> findByComment(String comment) {
        log.debug("Request to get Comment : {}", comment);
        return commentRepository.findByComment(comment)
                .map(commentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Comment : {}", id);
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentPayload> findAllCommentByPostTitle(String title) {
        List<CommentPayload> commentPayloads = findAll();
        List<CommentPayload> commentPayloadsforThisTitle = new ArrayList<CommentPayload>();

        for(CommentPayload commentPayload : commentPayloads ){
            if(commentPayload.getPostTitle().equals(title)){
                commentPayloadsforThisTitle.add(commentPayload);
            }
        }

        return commentPayloadsforThisTitle;
    }
}
