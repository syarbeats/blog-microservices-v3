/**
 * <h1>Category Service</h1>
 * This class will be used to setup services for
 * Category API.
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */

package com.mitrais.cdc.blogmicroservices.services.impl;

import com.mitrais.cdc.blogmicroservices.entity.Category;
import com.mitrais.cdc.blogmicroservices.mapper.CategoryMapper;
import com.mitrais.cdc.blogmicroservices.payload.CategoryPayload;
import com.mitrais.cdc.blogmicroservices.repository.CategoryRepository;
import com.mitrais.cdc.blogmicroservices.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    /**
     * This constructor will be used to setup category repository
     * and category mapper.
     *
     * @param categoryRepository
     * @param categoryMapper
     */
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    /**
     * This method will be used to create category.
     *
     * @param categoryDTO
     * @return
     */
    @Override
    public CategoryPayload save(CategoryPayload categoryDTO) {
        log.debug("Request to save Category : {}", categoryDTO);
        Category category = categoryMapper.toEntity(categoryDTO);
        category = categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    /**
     * This method will be used to get the whole category data.
     *
     * @return will return the list of category data
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryPayload> findAll() {
        log.debug("Request to get all Categories");
        return categoryRepository.findAll().stream()
            .map(categoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * This method will be used to get category
     * for certain category id.
     *
     * @param id
     * @return will return category data for the given category id
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryPayload> findOne(Long id) {
        log.debug("Request to get Category : {}", id);
        return categoryRepository.findById(id)
            .map(categoryMapper::toDto);
    }

    /**
     * This method will be used to get category
     * data for certain category name.
     *
     * @param name
     * @return will return category data for the given category name
     */
    @Override
    public Optional<CategoryPayload> findByName(String name) {
        log.debug("Request to get Category : {}", name);
        return categoryRepository.findByName(name)
                .map(categoryMapper::toDto);
    }

    /**
     * This method will be used to delete category
     * data for certain category data.
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Category : {}", id);
        categoryRepository.deleteById(id);
    }
}
