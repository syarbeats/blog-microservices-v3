package com.mitrais.cdc.blogmicroservices.services;

import com.mitrais.cdc.blogmicroservices.payload.CategoryPayload;

import java.util.List;
import java.util.Optional;


public interface CategoryService {

    CategoryPayload save(CategoryPayload categoryDTO);
    List<CategoryPayload> findAll();
    Optional<CategoryPayload> findOne(Long id);
    Optional<CategoryPayload> findByName(String name);
    void delete(Long id);
}
