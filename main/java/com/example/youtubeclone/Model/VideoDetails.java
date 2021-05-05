package com.example.youtubeclone.Model;

public class VideoDetails {
    public String videoId, title, description, url;
    public String categoryId = "";

    public VideoDetails(String videoId, String title, String description, String url, String categoryId) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.url = url;
        this.categoryId = categoryId;
    }
    public VideoDetails(){}

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
