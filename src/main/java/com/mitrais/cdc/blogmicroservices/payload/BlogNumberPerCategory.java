package com.mitrais.cdc.blogmicroservices.payload;

import com.mitrais.cdc.blogmicroservices.entity.Category;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BlogNumberPerCategory {

    private long y;
    private String label;

    public BlogNumberPerCategory(long y, String label){
        this.y = y;
        this.label= label;
    }

    public BlogNumberPerCategory() {
    }
}
