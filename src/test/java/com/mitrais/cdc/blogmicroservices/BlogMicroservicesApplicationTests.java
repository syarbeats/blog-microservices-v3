package com.mitrais.cdc.blogmicroservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mitrais.cdc.blogmicroservices.entity.Post;
import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import com.mitrais.cdc.blogmicroservices.repository.PostRepository;
import com.mitrais.cdc.blogmicroservices.repository.UserRepository;
import com.mitrais.cdc.blogmicroservices.security.jwt.UserDetails;
import com.mitrais.cdc.blogmicroservices.services.UserDetailsServices;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import java.nio.charset.Charset;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
@Slf4j
@FixMethodOrder(MethodSorters.DEFAULT)
public class BlogMicroservicesApplicationTests {

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
    PostRepository postRepository;

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
    public void postBlog() throws Exception {

        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));

        PostPayload postPayload = new PostPayload("Test Blog 11","Ini Content Blog 11 Chuy",  zonedDateTime, Long.parseLong("1"), "Enterprise Application Integration");
        String postJson = mapper.writeValueAsString(postPayload);

        mockMvc.perform(post("http://localhost:8081/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$['content']", containsString("Ini Content Blog 11 Chuy")))
                .andExpect(jsonPath("$['title']", containsString("Test Blog 11")));

    }

    @Test
    public void getAllBlog() throws Exception{

        mockMvc.perform(get("http://localhost:8081/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]['title']", containsString("Test image 7")));
    }

    @Test
    public void getByTitle() throws Exception{
        MvcResult result = mockMvc.perform(get("http://localhost:8081/api/post?title=Mastering Apache Camel")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("['categoryName']", containsString("Enterprise Application Integration")))
                .andExpect(jsonPath("['title']", containsString("Mastering Apache Camel"))).andReturn();

    }

    @Test
    public void getBlogById() throws Exception{

        Post post = postRepository.findByTitle("Mastering Apache Camel").get();
        mockMvc.perform(get("http://localhost:8081/api/posts/"+post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("['categoryName']", containsString("Enterprise Application Integration")))
                .andExpect(jsonPath("['title']", containsString("Mastering Apache Camel")));

    }

    @Test
    @Transactional
    @Rollback(true)
    public void deleteById() throws Exception{

        Post post = postRepository.findByTitle("Mastering Apache Camel").get();
        mockMvc.perform(delete("http://localhost:8081/api/posts/"+post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['categoryName']", containsString("Enterprise Application Integration")))
                .andExpect(jsonPath("$['title']", containsString("Mastering Apache Camel")));

    }

    @Test
    @Transactional
    @Rollback(true)
    public void updateBlog() throws Exception {

        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));

        PostPayload postPayload = new PostPayload(Long.parseLong("96"),"Mastering Apache Camel","Ini Content Baru Update",  zonedDateTime, Long.parseLong("1"), "Enterprise Application Integration");
        String postJson = mapper.writeValueAsString(postPayload);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("http://localhost:8081/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson)
                        .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['content']", containsString("Ini Content Baru Update")))
                .andExpect(jsonPath("$['title']", containsString("Mastering Apache Camel")));

    }

}
