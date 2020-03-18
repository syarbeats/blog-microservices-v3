package com.mitrais.cdc.blogmicroservices.services.impl;

import com.mitrais.cdc.blogmicroservices.entity.Post;
import com.mitrais.cdc.blogmicroservices.mapper.PostMapper;
import com.mitrais.cdc.blogmicroservices.mapper.PostMapperV1;
import com.mitrais.cdc.blogmicroservices.payload.BlogNumberPerCategory;
import com.mitrais.cdc.blogmicroservices.payload.BlogStatistic;
import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import com.mitrais.cdc.blogmicroservices.payload.RowNum;
import com.mitrais.cdc.blogmicroservices.repository.PostRepository;
import com.mitrais.cdc.blogmicroservices.services.KafkaService;
import com.mitrais.cdc.blogmicroservices.services.ReactivePostServices;
import io.reactivex.Single;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReactivePostServicesImpl implements ReactivePostServices {

    private PostRepository postRepository;
    private PostMapper postMapper;
    private PostMapperV1 postMapperV1;
    private KafkaService kafkaService;

    public ReactivePostServicesImpl(PostRepository postRepository, PostMapper postMapper, PostMapperV1 postMapperV1, KafkaService kafkaService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.postMapperV1 = postMapperV1;
        this.kafkaService = kafkaService;
    }

    @Override
    public Single<PostPayload> save(PostPayload postDTO) {
        return Single.create(save -> {
            Post post = postMapper.toEntity(postDTO);
            post = postRepository.save(post);
            postDTO.setId(post.getId());
            kafkaService.publishBlogCreationMessage(postDTO);
            save.onSuccess(postMapper.toDto(post));
        });
    }

    @Override
    public Single<Page<PostPayload>> findAll(Pageable pageable) {
        return Single.create(find -> {
            find.onSuccess(postRepository.getAllApprovedBlog(pageable, true).map(postMapper::toDto));
        });
    }

    @Override
    public Single<Optional<PostPayload>> findOne(Long id) {
        return Single.create(find -> {
            find.onSuccess(postRepository.findById(id).map(postMapper::toDto));
        });
    }

    @Override
    public Single<Optional<PostPayload>> findByTitle(String title) {
        return Single.create(find -> {
            find.onSuccess(postRepository.findByTitle(title).map(postMapperV1::toDto));
        });
    }

    @Override
    public Single<String> delete(Long id) {
        return Single.create(delete -> {
            postRepository.deleteById(id);
            delete.onSuccess("Data with id:"+id+" has been deleted successfully");
        });
    }

    @Override
    public Single<Page<PostPayload>> findByCategory(Pageable pageable, String category) {
        return Single.create(find -> {
            find.onSuccess(postRepository.findByCategory(pageable, category).map(postMapper::toDto));
        });
    }

    @Override
    public Single<Page<PostPayload>> findByCreatedDate(Pageable pageable, ZonedDateTime createdDate, ZonedDateTime oneDayBeforeCreatedDate, boolean status) {
        return Single.create(find -> {
            find.onSuccess(postRepository.findByCreatedDate(pageable, createdDate, oneDayBeforeCreatedDate, status).map(postMapper::toDto));
        });
    }

    @Override
    public Single<Page<PostPayload>> findByKeywords(Pageable pageable, String keyword) {
        return Single.create(find -> {
            find.onSuccess(postRepository.findByKeyword(pageable, keyword).map(postMapper::toDto));
        });
    }

    @Override
    public Single<List<BlogStatistic>> getBlogNumberPerCategory(Pageable pageable) {
        return Single.create(find -> {
            List<BlogNumberPerCategory> blogNumberPerCategoryList = postRepository.getBlogNumberPercategory(pageable).getContent();
            List<BlogStatistic> blogStatisticsList = new ArrayList<>();
            long rownum = postRepository.getBlogNumber().getRownum();

            for(BlogNumberPerCategory blogNumberPerCategory:blogNumberPerCategoryList){
                BlogStatistic blogStatistic = new BlogStatistic();
                blogStatistic.setY(((new Double(blogNumberPerCategory.getY()))/new Double(rownum))*100);
                blogStatistic.setLabel(blogNumberPerCategory.getLabel());
                blogStatisticsList.add(blogStatistic);
            }

            find.onSuccess(blogStatisticsList);
        });
    }

    @Override
    public Single<List<BlogNumberPerCategory>> getBlogNumberPerCategoryV2(Pageable pageable) {
        return Single.create(blog -> {
            blog.onSuccess(postRepository.getBlogNumberPercategory(pageable).getContent());
        });
    }

    @Override
    public Single<List<BlogStatistic>> getBlogNumberPerCategory() {
        return Single.create(blog -> {
            List<BlogNumberPerCategory> blogNumberPerCategoryList = postRepository.getBlogNumberPercategory();
            List<BlogStatistic> blogStatisticsList = new ArrayList<>();
            long rownum = postRepository.getBlogNumber().getRownum();

            for(BlogNumberPerCategory blogNumberPerCategory:blogNumberPerCategoryList){
                BlogStatistic blogStatistic = new BlogStatistic();
                blogStatistic.setY(((new Double(blogNumberPerCategory.getY()))/new Double(rownum))*100);
                blogStatistic.setLabel(blogNumberPerCategory.getLabel());
                blogStatisticsList.add(blogStatistic);
            }
            blog.onSuccess(blogStatisticsList);
        });
    }

    @Override
    public Single<List<BlogNumberPerCategory>> getBlogNumberPerCategoryV2() {
        return Single.create(blog -> {
            blog.onSuccess(postRepository.getBlogNumberPercategory());
        });
    }

    @Override
    public RowNum getBlogRowNum() {
        return postRepository.getBlogNumber();
    }
}
