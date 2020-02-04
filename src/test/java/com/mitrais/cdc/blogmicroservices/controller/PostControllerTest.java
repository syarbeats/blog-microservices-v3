package com.mitrais.cdc.blogmicroservices.controller;

import com.mitrais.cdc.blogmicroservices.exception.BadRequestAlertException;
import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostControllerTest {

    @Autowired
    PostController postController;

    @Test
    @Transactional
    @Rollback(true)
    public void createPost() throws URISyntaxException {
        ResponseEntity<PostPayload> responseEntity = postController.createPost(new PostPayload("Blog number one", "This is the content of blog", ZonedDateTime.now(), new Long(2), "Web Application"));

        assertThat("Blog number one", is(responseEntity.getBody().getTitle()));
        assertThat("This is the content of blog", is(responseEntity.getBody().getContent()));
        assertThat(String.valueOf(2), is(String.valueOf(responseEntity.getBody().getCategoryId())));
    }

    @Test(expected = BadRequestAlertException.class)
    @Transactional
    @Rollback(true)
    public void createPostNegative() throws URISyntaxException {
        ResponseEntity<PostPayload> responseEntity = postController.createPost(new PostPayload(new Long(1),"Blog number one", "This is the content of blog", ZonedDateTime.now(), new Long(2), "Web Application"));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void updatePost() throws URISyntaxException {
        ResponseEntity<PostPayload> searchEntity = postController.getPost(new Long(95));
        PostPayload postPayload = searchEntity.getBody();
        postPayload.setSummary("This is the new summary");
        ResponseEntity<PostPayload> responseEntity = postController.updatePost(postPayload);

        assertThat("Test image 7", is(responseEntity.getBody().getTitle()));
        assertThat("This is the new summary", is(responseEntity.getBody().getSummary()));
        assertThat(String.valueOf(1), is(String.valueOf(responseEntity.getBody().getCategoryId())));
        assertThat("Enterprise Application Integration", is(responseEntity.getBody().getCategoryName()));

    }

    @Test(expected = BadRequestAlertException.class)
    @Transactional
    @Rollback(true)
    public void updatePostNegative() throws URISyntaxException {
        ResponseEntity<PostPayload> searchEntity = postController.getPost(new Long(95));
        PostPayload postPayload = searchEntity.getBody();
        postPayload.setId(null);
        postPayload.setSummary("This is the new summary");
        ResponseEntity<PostPayload> responseEntity = postController.updatePost(postPayload);

    }

    @Test
    public void getAllPosts() {

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

        ResponseEntity<List<PostPayload>> responseEntity = postController.getAllPosts(pageable);

        assertThat("Test image 7", is(responseEntity.getBody().get(0).getTitle()));
        assertThat(String.valueOf(1), is(String.valueOf(String.valueOf(responseEntity.getBody().get(0).getCategoryId()))));
        assertThat("Enterprise Application Integration", is(responseEntity.getBody().get(0).getCategoryName()));

    }

    @Test
    public void getPost() {
        ResponseEntity<PostPayload> responseEntity = postController.getPost(new Long(95));

        assertThat("Test image 7", is(responseEntity.getBody().getTitle()));
        assertThat(String.valueOf(1), is(String.valueOf(responseEntity.getBody().getCategoryId())));
        assertThat("Enterprise Application Integration", is(responseEntity.getBody().getCategoryName()));

    }

    @Test
    public void getPostNegative() {
        ResponseEntity<PostPayload> responseEntity = postController.getPost(new Long(300));

    }

    @Test
    public void getPostByTitle() {
        ResponseEntity<PostPayload> responseEntity = postController.getPostByTitle("Test image 7");

        assertThat(String.valueOf(95), is(String.valueOf(responseEntity.getBody().getId())));
        assertThat(String.valueOf(1), is(String.valueOf(responseEntity.getBody().getCategoryId())));
        assertThat("Enterprise Application Integration", is(responseEntity.getBody().getCategoryName()));

    }

    @Test
    public void getPostByTitleNegative() {
        ResponseEntity<PostPayload> responseEntity = postController.getPostByTitle("Title Gak Ada");

    }


    @Test
    @Transactional
    @Rollback(true)
    public void deletePost() {
        ResponseEntity<PostPayload> searchEntity = postController.getPost(new Long(95));
        ResponseEntity<PostPayload> responseEntity = postController.deletePost(searchEntity.getBody().getId());

        assertThat("Test image 7", is(responseEntity.getBody().getTitle()));
        assertThat(String.valueOf(1), is(String.valueOf(responseEntity.getBody().getCategoryId())));
        assertThat("Enterprise Application Integration", is(responseEntity.getBody().getCategoryName()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void deletePostNegative() {
        ResponseEntity<PostPayload> responseEntity = postController.deletePost(new Long(300));

    }

    @Test
    public void findPostsByCategory() {

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

        ResponseEntity<List<PostPayload>> responseEntity = postController.findPostsByCategory(pageable, "Enterprise Application Integration");

        assertThat("Test image 7", is(responseEntity.getBody().get(0).getTitle()));
        assertThat(String.valueOf(1), is(String.valueOf(responseEntity.getBody().get(0).getCategoryId())));
        assertThat("Enterprise Application Integration", is(responseEntity.getBody().get(0).getCategoryName()));
    }


    @Test
    public void findPostByToday() {

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

        ResponseEntity<List<PostPayload>> responseEntity = postController.findPostByToday(pageable);

        assertThat(String.valueOf(149), is(String.valueOf(responseEntity.getBody().get(0).getId())));
        assertThat("Camel In Action", is(responseEntity.getBody().get(0).getTitle()));
        assertThat(String.valueOf(1), is(String.valueOf(responseEntity.getBody().get(0).getCategoryId())));
        assertThat("Enterprise Application Integration", is(responseEntity.getBody().get(0).getCategoryName()));
    }

    @Test
    public void findPostByKeyword() {

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

        ResponseEntity<List<PostPayload>> responseEntity =  postController.findPostByKeyword("sql", pageable);

        assertThat(String.valueOf(98), is(String.valueOf(responseEntity.getBody().get(0).getId())));
        assertThat("SQL Join", is(responseEntity.getBody().get(0).getTitle()));
        assertThat(String.valueOf(7), is(String.valueOf(responseEntity.getBody().get(0).getCategoryId())));
        assertThat("Backend Development", is(responseEntity.getBody().get(0).getCategoryName()));
    }


}