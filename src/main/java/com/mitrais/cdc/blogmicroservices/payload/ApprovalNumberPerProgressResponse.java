package com.mitrais.cdc.blogmicroservices.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalNumberPerProgressResponse {

    private double y;
    private String label;

    public ApprovalNumberPerProgressResponse(double y, String label) {
        this.y = y;
        this.label = label;
    }

    public ApprovalNumberPerProgressResponse() {

    }
}
