package com.mitrais.cdc.blogmicroservices.services.impl;

import com.mitrais.cdc.blogmicroservices.entity.Category;
import com.mitrais.cdc.blogmicroservices.mapper.CategoryMapper;
import com.mitrais.cdc.blogmicroservices.payload.CategoryPayload;
import com.mitrais.cdc.blogmicroservices.repository.CategoryRepository;
import com.mitrais.cdc.blogmicroservices.services.ReactiveCategoryServices;
import io.reactivex.Single;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReactiveCategoryServicesImpl implements ReactiveCategoryServices {

    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;

    public ReactiveCategoryServicesImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Single<CategoryPayload> save(CategoryPayload categoryDTO) {
        return Single.create(category -> {
            Category categorydata = categoryMapper.toEntity(categoryDTO);
            category.onSuccess(categoryMapper.toDto(categoryRepository.save(categorydata)));
        });
    }

    @Override
    public Single<List<CategoryPayload>> findAll() {
        return Single.create(findall -> {
            findall.onSuccess(categoryRepository.findAll().stream().map(categoryMapper::toDto).collect(Collectors.toCollection(LinkedList::new)));
        });
    }

    @Override
    public Single<Optional<CategoryPayload>> findOne(Long id) {
        return Single.create(findone -> {
            findone.onSuccess(categoryRepository.findById(id).map(categoryMapper::toDto));
        });
    }

    @Override
    public Single<Optional<CategoryPayload>> findByName(String name) {
        return Single.create(findByName -> {
            findByName.onSuccess(categoryRepository.findByName(name).map(categoryMapper::toDto));
        });
    }

    @Override
    public Single<String> delete(Long id) {
        return Single.create(delete -> {
            categoryRepository.deleteById(id);
            delete.onSuccess("Data has been delete successfully");
        });
    }
}
