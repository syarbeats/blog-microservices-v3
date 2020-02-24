package com.mitrais.cdc.blogmicroservices.payload;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;


public class PostPayload implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @Lob
    private String content;

    @Lob
    private String summary;

    private ZonedDateTime createdDate;


    private Long categoryId;

    private String categoryName;


    private boolean status;

    public PostPayload() {
    }

    public PostPayload(@NotNull String title, String content, @NotNull ZonedDateTime createdDate, Long categoryId, String categoryName) {
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public PostPayload(Long id, @NotNull String title, String content, @NotNull ZonedDateTime createdDate, Long categoryId, String categoryName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public PostPayload(Long id, boolean status, @NotNull String title, String content, @NotNull ZonedDateTime createdDate, Long categoryId, String categoryName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.status = status;
    }

    public PostPayload(@NotNull String title, String content, @NotNull ZonedDateTime createdDate, Long categoryId, String categoryName, String summary) {
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.summary = summary;
    }

    public PostPayload(Long id, @NotNull String title, String content, @NotNull ZonedDateTime createdDate, Long categoryId, String categoryName, String summary) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.summary = summary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
