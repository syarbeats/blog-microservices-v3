package com.mitrais.cdc.blogmicroservices;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitrais.cdc.blogmicroservices.entity.Comment;
import com.mitrais.cdc.blogmicroservices.payload.CategoryPayload;
import com.mitrais.cdc.blogmicroservices.payload.CommentPayload;
import com.mitrais.cdc.blogmicroservices.repository.CategoryRepository;
import com.mitrais.cdc.blogmicroservices.repository.CommentRepository;
import com.mitrais.cdc.blogmicroservices.security.jwt.UserDetails;
import com.mitrais.cdc.blogmicroservices.services.UserDetailsServices;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
@Slf4j
@FixMethodOrder(MethodSorters.DEFAULT)
public class CommentMicroserviceTests {

    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    private MockMvc mockMvc;
    private UserDetails userDetails;
    private Authentication authToken;
    private String token;
    private int id;

    @Autowired
    UserDetailsServices userDetailsServices;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void contextLoads() {
        userDetails = userDetailsServices.loadUserByUsername("admin");
        authToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Transactional
    @Rollback(true)
    public void createComment() throws Exception {

        CommentPayload payload = new CommentPayload("admin", "Test Comment", new Long(96), "Mastering Apache Camel");
        String commentJson = mapper.writeValueAsString(payload);

        mockMvc.perform(post("http://localhost:8081/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(commentJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$['username']", containsString("admin")))
                .andExpect(jsonPath("$['comment']", containsString("Test Comment")));

    }

    @Test
    public void getCommentById() throws Exception{
        Comment comment = commentRepository.findByComment("Ini update comment untuk post Test Blog").get();
        mockMvc.perform(get("http://localhost:8081/api/comments/"+comment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['username']", containsString("admin")))
                .andExpect(jsonPath("$['comment']", containsString("Ini update comment untuk post Test Blog")))
                .andExpect(jsonPath("$['postTitle']", containsString("SQL Join")));

    }

    @Test
    @Transactional
    @Rollback(true)
    public void updateComment() throws Exception{
        Comment comment = commentRepository.findByComment("Ini update comment untuk post Test Blog").get();
        CommentPayload commentPayload = new CommentPayload(comment.getId(), comment.getUsername(),"Ini update comment", Long.parseLong("98"), "SQL Join");
        String commentJson = mapper.writeValueAsString(commentPayload);
        mockMvc.perform(put("http://localhost:8081/api/comments/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(commentJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['username']", containsString("admin")))
                .andExpect(jsonPath("$['comment']", containsString("Ini update comment")))
                .andExpect(jsonPath("$['postTitle']", containsString("SQL Join")));
    }

    @Test
    public void getAllComment() throws Exception{
        mockMvc.perform(get("http://localhost:8081/api/comments/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]['username']", containsString("admin")))
                .andExpect(jsonPath("$[0]['comment']", containsString("Test Comment")))
                .andExpect(jsonPath("$[0]['postTitle']", containsString("Test image 7")));

    }

    @Test
    public void getCommentDataByComment() throws Exception{
        mockMvc.perform(get("http://localhost:8081/api/comment-by-comment?comment=Ini update comment untuk post Test Blog")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['username']", containsString("admin")))
                .andExpect(jsonPath("$['comment']", containsString("Ini update comment untuk post Test Blog")))
                .andExpect(jsonPath("$['postTitle']", containsString("SQL Join")));
    }

    @Test
    public void getCommentByTitle() throws Exception{
        mockMvc.perform(get("http://localhost:8081/api/comments-by-title?title=SQL Join")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]['username']", containsString("admin")))
                .andExpect(jsonPath("$[0]['comment']", containsString("Ini update comment untuk post Test Blog")))
                .andExpect(jsonPath("$[0]['postTitle']", containsString("SQL Join")));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void deleteComment() throws Exception{

        Comment comment = commentRepository.findByComment("Ini update comment untuk post Test Blog").get();

        mockMvc.perform(delete("http://localhost:8081/api/comments/"+comment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['username']", containsString("admin")))
                .andExpect(jsonPath("$['comment']", containsString("Ini update comment untuk post Test Blog")))
                .andExpect(jsonPath("$['postTitle']", containsString("SQL Join")));
    }
}
