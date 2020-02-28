package com.mitrais.cdc.blogmicroservices.services.impl;

import com.mitrais.cdc.blogmicroservices.entity.Category;
import com.mitrais.cdc.blogmicroservices.entity.Post;
import com.mitrais.cdc.blogmicroservices.mapper.PostMapper;
import com.mitrais.cdc.blogmicroservices.mapper.PostMapperV1;
import com.mitrais.cdc.blogmicroservices.payload.*;
import com.mitrais.cdc.blogmicroservices.repository.PostRepository;
import com.mitrais.cdc.blogmicroservices.services.KafkaService;
import com.mitrais.cdc.blogmicroservices.services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;

    private final PostMapper postMapper;
    private final PostMapperV1 postMapperV1;
    private KafkaService kafkaService;


    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper, PostMapperV1 postMapperV1, KafkaService kafkaService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.postMapperV1 = postMapperV1;
        this.kafkaService = kafkaService;

    }

    @Override
    public PostPayload save(PostPayload postDTO) {
        log.debug("Request to save Post : {}", postDTO);
        Post post = postMapper.toEntity(postDTO);
        post = postRepository.save(post);
        postDTO.setId(post.getId());
        kafkaService.publishBlogCreationMessage(postDTO);
        log.info("Publish Blog Data Creation...");
        return postMapper.toDto(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostPayload> findAll(Pageable pageable) {
        log.debug("Request to get all Posts");
        /*return postRepository.findAll(pageable)
            .map(postMapper::toDto);*/

        return postRepository.getAllApprovedBlog(pageable, true)
                .map(postMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostPayload> findOne(Long id) {
        log.debug("Request to get Post : {}", id);
        return postRepository.findById(id)
            .map(postMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostPayload> findByTitle(String title) {
        log.debug("Request to get Post : {}", title);
        return postRepository.findByTitle(title).map(postMapperV1::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Post : {}", id);
        postRepository.deleteById(id);
    }

    @Override
    public Page<PostPayload> findByCategory(Pageable pageable, String category) {
        log.debug("Request to get all Posts baseon on certain category");
        return postRepository.findByCategory(pageable, category).map(postMapper::toDto);
    }

    @Override
    public Page<PostPayload> findByCreatedDate(Pageable pageable, ZonedDateTime createdDate, ZonedDateTime oneDayBeforeCreatedDate, boolean status) {
        log.debug("Request to get all Posts based on on certain date");
        return postRepository.findByCreatedDate(pageable, createdDate, oneDayBeforeCreatedDate, status).map(postMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<PostPayload> findByKeywords(Pageable pageable, String keyword) {
        log.debug("Request to get all Posts based on keyword");
        return postRepository.findByKeyword(pageable, keyword).map(postMapper::toDto);
    }

    @Override
    /*public Page<BlogNumberPerCategory> getBlogNumberPerCategory(Pageable pageable) {*/
    public List<BlogStatistic> getBlogNumberPerCategory(Pageable pageable) {
        List<BlogNumberPerCategory> blogNumberPerCategoryList = postRepository.getBlogNumberPercategory(pageable).getContent();
        List<BlogStatistic> blogStatisticsList = new ArrayList<>();
        long rownum = postRepository.getBlogNumber().getRownum();

        for(BlogNumberPerCategory blogNumberPerCategory:blogNumberPerCategoryList){
            BlogStatistic blogStatistic = new BlogStatistic();
            blogStatistic.setY(((new Double(blogNumberPerCategory.getY()))/new Double(rownum))*100);
            blogStatistic.setLabel(blogNumberPerCategory.getLabel());
            blogStatisticsList.add(blogStatistic);
        }

        return blogStatisticsList;
        /*return postRepository.getBlogNumberPercategory(pageable);*/
    }

    @Override
    public List<BlogStatistic> getBlogNumberPerCategory() {
        List<BlogNumberPerCategory> blogNumberPerCategoryList = postRepository.getBlogNumberPercategory();
        List<BlogStatistic> blogStatisticsList = new ArrayList<>();
        long rownum = postRepository.getBlogNumber().getRownum();

        for(BlogNumberPerCategory blogNumberPerCategory:blogNumberPerCategoryList){
            BlogStatistic blogStatistic = new BlogStatistic();
            blogStatistic.setY(((new Double(blogNumberPerCategory.getY()))/new Double(rownum))*100);
            blogStatistic.setLabel(blogNumberPerCategory.getLabel());
            blogStatisticsList.add(blogStatistic);
        }

        return blogStatisticsList;
    }

    @Override
    public RowNum getBlogRowNum() {
        return postRepository.getBlogNumber();
    }

}
