package com.mitrais.cdc.blogmicroservices.payload;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Getter
@Setter
public class BlogApprovalInProgress {

    private int id;
    private String title;
    private String summary;
    private ZonedDateTime createdDate;
    private Long categoryId;
    private String categoryName;
    private boolean status;
    private String approvalProgress;

    public BlogApprovalInProgress(@NotNull String title, String summary, ZonedDateTime createdDate, Long categoryId, String categoryName, boolean status, String approvalProgress) {
        this.title = title;
        this.summary = summary;
        this.createdDate = createdDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.status = status;
        this.approvalProgress = approvalProgress;
    }

    public BlogApprovalInProgress() {

    }
}
