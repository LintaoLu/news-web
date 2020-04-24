package com.llu25.paperweb;

import java.util.ArrayList;
import java.util.List;

public class News {

    private String source, author, title, description, url, urlToImage, publishedAt;
    private int id;
    private List<String> keyWords;

    public News(int id, String source, String author, String title, String description,
                String url, String urlToImage, String publishedAt, List<String> keyWords) {
        this.id = id;
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.keyWords = new ArrayList<>();
        this.keyWords = keyWords;
    }

    public int getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    @Override
    public String toString() {
        return "News{" +
                "source='" + source + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                '}';
    }
}
