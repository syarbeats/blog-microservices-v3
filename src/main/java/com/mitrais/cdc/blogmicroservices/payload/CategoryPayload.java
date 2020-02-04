package com.mitrais.cdc.blogmicroservices.payload;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


public class CategoryPayload implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    public CategoryPayload(@NotNull String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CategoryPayload(Long id, @NotNull String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public CategoryPayload() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
