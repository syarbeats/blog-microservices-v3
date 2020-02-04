package com.mitrais.cdc.blogmicroservices.controller;

import com.mitrais.cdc.blogmicroservices.payload.CategoryPayload;
import com.mitrais.cdc.blogmicroservices.services.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryControllerTest {

    @Autowired
    CategoryController categoryController;

    @Test
    @Transactional
    @Rollback(true)
    public void createCategory() throws URISyntaxException {
        CategoryPayload payload = new CategoryPayload("Electronics", "All knowledge related to electronics field");
        ResponseEntity<CategoryPayload> categoryPayload = categoryController.createCategory(payload);

        assertThat("Electronics", is(categoryPayload.getBody().getName()));
        assertThat("All knowledge related to electronics field", is(categoryPayload.getBody().getDescription()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void updateCategory() throws URISyntaxException {
        ResponseEntity<CategoryPayload> responseEntity = categoryController.getCategory(new Long(1));
        CategoryPayload categoryPayload = responseEntity.getBody();

        categoryPayload.setDescription("All about Enterprise Application Integration");
        ResponseEntity<CategoryPayload> updateResponse = categoryController.updateCategory(categoryPayload);

        assertThat("All about Enterprise Application Integration", is(updateResponse.getBody().getDescription()));
    }

    @Test
    public void getAllCategories() {
        List<CategoryPayload> categoryPayload =  categoryController.getAllCategories();

        assertThat(String.valueOf(1), is(String.valueOf(categoryPayload.get(0).getId())));
        assertThat("Enterprise Application Integration", is(categoryPayload.get(0).getName()));
        assertThat("All about integration in Enterprise Application", is(categoryPayload.get(0).getDescription()));
    }

    @Test
    public void getCategory() {
        ResponseEntity<CategoryPayload> responseEntity = categoryController.getCategory(new Long(1));

        assertThat("Enterprise Application Integration", is(responseEntity.getBody().getName()));
        assertThat("All about integration in Enterprise Application", is(responseEntity.getBody().getDescription()));
    }

    @Test
    public void getCategoryByName() {
        ResponseEntity<CategoryPayload> responseEntity = categoryController.getCategoryByName("Enterprise Application Integration");

        assertThat(String.valueOf(1), is(String.valueOf(responseEntity.getBody().getId())));
        assertThat("All about integration in Enterprise Application", is(responseEntity.getBody().getDescription()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void deleteCategory() {
        ResponseEntity<CategoryPayload> responseEntity = categoryController.deleteCategory(new Long(23));

        assertThat(String.valueOf(23), is(String.valueOf(responseEntity.getBody().getId())));
        assertThat("Microcontroller", is(responseEntity.getBody().getName()));
        assertThat("Description1", is(responseEntity.getBody().getDescription()));
    }
}