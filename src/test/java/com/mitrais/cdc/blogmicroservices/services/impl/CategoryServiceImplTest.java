package com.mitrais.cdc.blogmicroservices.services.impl;

import com.mitrais.cdc.blogmicroservices.payload.CategoryPayload;
import com.mitrais.cdc.blogmicroservices.services.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    CategoryService categoryService;

    @Test
    @Transactional
    @Rollback(true)
    public void save() {
        CategoryPayload payload = new CategoryPayload("Electronics", "All knowledge related to electronics field");
        CategoryPayload categoryPayload = categoryService.save(payload);

        assertThat("Electronics", is(categoryPayload.getName()));
        assertThat("All knowledge related to electronics field", is(categoryPayload.getDescription()));
    }

    @Test
    public void findAll() {
        List<CategoryPayload> categoryPayloadList = categoryService.findAll();

        assertThat("Enterprise Application Integration", is(categoryPayloadList.get(0).getName()));
        assertThat(String.valueOf(1), is(String.valueOf(categoryPayloadList.get(0).getId())));
        assertThat("All about integration in Enterprise Application", is(categoryPayloadList.get(0).getDescription()));
    }

    @Test
    public void findOne() {
        CategoryPayload categoryPayload = categoryService.findOne(new Long(1)).get();

        assertThat("Enterprise Application Integration", is(categoryPayload.getName()));
        assertThat("All about integration in Enterprise Application", is(categoryPayload.getDescription()));
    }

    @Test
    public void findByName() {
        CategoryPayload categoryPayload = categoryService.findByName("Enterprise Application Integration").get();

        assertThat(String.valueOf(1), is(String.valueOf(categoryPayload.getId())));
        assertThat("All about integration in Enterprise Application", is(categoryPayload.getDescription()));
    }

    @Test
    @Transactional
    public void delete() {

        CategoryPayload payload = new CategoryPayload("Electronics", "All knowledge related to electronics field");
        CategoryPayload categoryPayload = categoryService.save(payload);

        categoryService.delete(categoryPayload.getId());

        Optional<CategoryPayload> searchPayload = categoryService.findOne(categoryPayload.getId());
        assertTrue(!searchPayload.isPresent());

    }
}