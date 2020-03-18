package com.mitrais.cdc.blogmicroservices.services;

import com.mitrais.cdc.blogmicroservices.payload.CategoryPayload;
import io.reactivex.Single;

import java.util.List;
import java.util.Optional;

public interface ReactiveCategoryServices {

    Single<CategoryPayload> save(CategoryPayload categoryDTO);
    Single<List<CategoryPayload>> findAll();
    Single<Optional<CategoryPayload>> findOne(Long id);
    Single<Optional<CategoryPayload>> findByName(String name);
    Single<String> delete(Long id);
}
