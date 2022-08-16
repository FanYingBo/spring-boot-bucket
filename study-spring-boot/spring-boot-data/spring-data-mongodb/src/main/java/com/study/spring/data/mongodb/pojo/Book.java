package com.study.spring.data.mongodb.pojo;

import org.springframework.data.annotation.Id;

public class Book {
    /**
     * mongodb 中的ObjectId
     */
    @Id
    private String _id;
    private Long id;
    private String name;
    private String author;
    private PublishedInformation publishedInformation;
    private String intro;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public PublishedInformation getPublishedInformation() {
        return publishedInformation;
    }

    public void setPublishedInformation(PublishedInformation publishedInformation) {
        this.publishedInformation = publishedInformation;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
