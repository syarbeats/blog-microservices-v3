package com.mitrais.cdc.blogmicroservices.repository;


import com.mitrais.cdc.blogmicroservices.entity.Category;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;


/**
 * Spring Data  repository for the Category entity.
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

}
