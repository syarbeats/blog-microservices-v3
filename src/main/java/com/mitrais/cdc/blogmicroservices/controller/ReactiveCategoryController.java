package com.mitrais.cdc.blogmicroservices.controller;

import com.mitrais.cdc.blogmicroservices.exception.BadRequestAlertException;
import com.mitrais.cdc.blogmicroservices.payload.CategoryPayload;
import com.mitrais.cdc.blogmicroservices.services.ReactiveCategoryServices;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReactiveCategoryController {

    private ReactiveCategoryServices reactiveCategoryServices;

    public ReactiveCategoryController(ReactiveCategoryServices reactiveCategoryServices) {
        this.reactiveCategoryServices = reactiveCategoryServices;
    }

    @PostMapping("/categories-reactive")
    public Single<ResponseEntity<CategoryPayload>> createCategory(@Valid @RequestBody CategoryPayload categoryDTO) throws URISyntaxException {
        return reactiveCategoryServices.save(categoryDTO)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload));
    }

    @PutMapping("/categories-reactive")
    public Single<ResponseEntity<CategoryPayload>> updateCategory(@Valid @RequestBody CategoryPayload categoryDTO) throws URISyntaxException {
       return reactiveCategoryServices.save(categoryDTO)
               .subscribeOn(Schedulers.io())
               .map(category -> ResponseEntity.ok().body(category));
    }

    @GetMapping("/categories-reactive")
    public Single<ResponseEntity<List<CategoryPayload>>> getAllCategories() {
        return reactiveCategoryServices.findAll()
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload));
    }

    @GetMapping("/categories-reactive/{id}")
    public Single<ResponseEntity<CategoryPayload>> getCategory(@PathVariable Long id) {
        return reactiveCategoryServices.findOne(id)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload.get()));
    }

    @GetMapping("/category-reactive")
    public Single<ResponseEntity<CategoryPayload>> getCategoryByName(@RequestParam String name) {
        return reactiveCategoryServices.findByName(name)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok(payload.get()));
    }

    @DeleteMapping("/categories-reactive/{id}")
    public Single<ResponseEntity<String>> deleteCategory(@PathVariable Long id) {
        return reactiveCategoryServices.delete(id)
                .subscribeOn(Schedulers.io())
                .map(payload -> ResponseEntity.ok("Category with id:"+id+" has been deleted successfully"));
    }
}
