package com.mitrais.cdc.blogmicroservices.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogStatistic {

    private double y;
    private String label;

    public BlogStatistic(double y, String label) {
        this.y = y;
        this.label = label;
    }

    public BlogStatistic() {
    }
}
