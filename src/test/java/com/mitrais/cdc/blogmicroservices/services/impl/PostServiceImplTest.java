package com.mitrais.cdc.blogmicroservices.services.impl;

import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import com.mitrais.cdc.blogmicroservices.services.PostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostServiceImplTest {

    @Autowired
    PostService postService;

    @Test
    @Transactional
    @Rollback(true)
    public void save() {
        PostPayload postPayload = postService.save(new PostPayload("Blog number one", "This is the content of blog", ZonedDateTime.now(), new Long(2), "Web Application"));

        assertThat("Blog number one", is(postPayload.getTitle()));
        assertThat("This is the content of blog", is(postPayload.getContent()));
        assertThat(String.valueOf(2), is(String.valueOf(postPayload.getCategoryId())));
    }

    @Test
    public void findAll() {
        Pageable pageable=new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getPageSize() {
                return 5;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return Sort.by("id");
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

        };

        Page<PostPayload> postPayload = postService.findAll(pageable);

        assertThat(String.valueOf(95), is(String.valueOf(postPayload.getContent().get(0).getId())));
        assertThat("Test image 7", is(postPayload.getContent().get(0).getTitle()));
        assertThat(String.valueOf(1), is(String.valueOf(postPayload.getContent().get(0).getCategoryId())));
        assertThat("Enterprise Application Integration", is(postPayload.getContent().get(0).getCategoryName()));
    }

    @Test
    public void findOne() {
        PostPayload postPayload = postService.findOne(new Long(95)).get();

        assertThat("Test image 7", is(postPayload.getTitle()));
        assertThat(String.valueOf(1), is(String.valueOf(postPayload.getCategoryId())));
        assertThat("Enterprise Application Integration", is(postPayload.getCategoryName()));
    }

    @Test
    public void findByTitle() {
        PostPayload postPayload = postService.findByTitle("Test image 7").get();

        assertThat(String.valueOf(95), is(String.valueOf(postPayload.getId())));
        assertThat(String.valueOf(1), is(String.valueOf(postPayload.getCategoryId())));
        assertThat("Enterprise Application Integration", is(postPayload.getCategoryName()));
    }

    @Test
    public void delete() {
        PostPayload postPayload = postService.save(new PostPayload("Blog number one", "This is the content of blog", ZonedDateTime.now(), new Long(2), "Web Application"));

        postService.delete(postPayload.getId());
        Optional<PostPayload> searchPayload = postService.findOne(postPayload.getId());
        assertTrue(!searchPayload.isPresent());
    }

    @Test
    public void findByCategory() {

        Pageable pageable=new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getPageSize() {
                return 5;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return Sort.by("id");
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

        };

        Page<PostPayload> postPayload = postService.findByCategory(pageable, "Enterprise Application Integration");

        assertThat(String.valueOf(95), is(String.valueOf(postPayload.getContent().get(0).getId())));
        assertThat("Test image 7", is(postPayload.getContent().get(0).getTitle()));
        assertThat(String.valueOf(1), is(String.valueOf(postPayload.getContent().get(0).getCategoryId())));
        assertThat("Enterprise Application Integration", is(postPayload.getContent().get(0).getCategoryName()));
    }

    @Test
    public void findByCreatedDate() {

        Pageable pageable=new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getPageSize() {
                return 5;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return Sort.by("id");
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

        };

        ZonedDateTime beforeToday = ZonedDateTime.now().minusDays(1);
        ZonedDateTime today = ZonedDateTime.now();

        Page<PostPayload> postPayload = postService.findByCreatedDate(pageable, today, beforeToday);

        assertThat(String.valueOf(149), is(String.valueOf(postPayload.getContent().get(0).getId())));
        assertThat("Camel In Action", is(postPayload.getContent().get(0).getTitle()));
        assertThat(String.valueOf(1), is(String.valueOf(postPayload.getContent().get(0).getCategoryId())));
        assertThat("Enterprise Application Integration", is(postPayload.getContent().get(0).getCategoryName()));
    }

    @Test
    public void findByKeywords() {

        Pageable pageable=new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getPageSize() {
                return 5;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return Sort.by("id");
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

        };

        Page<PostPayload> postPayload = postService.findByKeywords(pageable, "sql");

        assertThat(String.valueOf(98), is(String.valueOf(postPayload.getContent().get(0).getId())));
        assertThat("SQL Join", is(postPayload.getContent().get(0).getTitle()));
        assertThat(String.valueOf(7), is(String.valueOf(postPayload.getContent().get(0).getCategoryId())));
        assertThat("Backend Development", is(postPayload.getContent().get(0).getCategoryName()));
    }
}