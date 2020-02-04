package com.mitrais.cdc.blogmicroservices;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitrais.cdc.blogmicroservices.entity.Category;
import com.mitrais.cdc.blogmicroservices.payload.CategoryPayload;
import com.mitrais.cdc.blogmicroservices.payload.PostPayload;
import com.mitrais.cdc.blogmicroservices.repository.CategoryRepository;
import com.mitrais.cdc.blogmicroservices.repository.PostRepository;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
@Slf4j
@FixMethodOrder(MethodSorters.DEFAULT)
public class CategoryMicroservicesTests {

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
    CategoryRepository categoryRepository;

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
    public void CreateCategory() throws Exception {

        CategoryPayload categoryPayload = new CategoryPayload("Microservices","All that related to microservices");
        String categoryJson = mapper.writeValueAsString(categoryPayload);

        mockMvc.perform(post("http://localhost:8081/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$['name']", containsString("Microservices")))
                .andExpect(jsonPath("$['description']", containsString("All that related to microservices")));

    }

    @Test
    public void getCatById() throws Exception{

        Category category = categoryRepository.findByName("Microservices").get();

        mockMvc.perform(get("http://localhost:8081/api/categories/"+category.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['name']", containsString("Microservices")))
                .andExpect(jsonPath("$['description']", containsString("All that related to microservices")));

    }

    @Test
    public void getByTitle() throws Exception{

        mockMvc.perform(get("http://localhost:8081/api/category?name=Microservices")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['name']", containsString("Microservices")))
                .andExpect(jsonPath("$['description']", containsString("All that related to microservices")));

    }

    @Test
    public void updateCategory() throws Exception{
        Category category = categoryRepository.findByName("Microservices").get();
        CategoryPayload categoryPayload = new CategoryPayload(category.getId(),"Microservices","All that related to microservices architecture");
        String categoryJson = mapper.writeValueAsString(categoryPayload);

        mockMvc.perform(put("http://localhost:8081/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['name']", containsString("Microservices")))
                .andExpect(jsonPath("$['description']", containsString("All that related to microservices architecture")));

    }

    @Test
    public void getAllCategories() throws Exception {
        mockMvc.perform(get("http://localhost:8081/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[2]['name']", containsString("Category 1")))
                .andExpect(jsonPath("$[2]['description']", containsString("Test Category Description")));

    }

    @Test
    public void deleteById() throws Exception{
        Category category = categoryRepository.findByName("Microservices").get();

        mockMvc.perform(delete("http://localhost:8081/api/categories/"+category.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['name']", containsString("Microservices")))
                .andExpect(jsonPath("$['description']", containsString("All that related to microservices architecture")));

    }
}
