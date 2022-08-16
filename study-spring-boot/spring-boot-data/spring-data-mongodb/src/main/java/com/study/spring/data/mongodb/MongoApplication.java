package com.study.spring.data.mongodb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.spring.data.mongodb.pojo.Book;
import com.study.spring.data.mongodb.pojo.PublishedInformation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@SpringBootApplication
public class MongoApplication {

    public static void main(String[] args) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Book book = new Book();
//        book.setAuthor("龚");
//        book.setName("Kubernetes权威指南");
//        book.setIntro("Kubernetes 是由谷歌开源的容器集群管理系统");
//        PublishedInformation publishedInformation = new PublishedInformation();
//        publishedInformation.setPrice(BigDecimal.valueOf(238.00d));
//        publishedInformation.setPublisher("电子工业出版社");
//        publishedInformation.setPrinting("三河市良远印务");
//        publishedInformation.setEditorInCharge("张");
//        publishedInformation.setPublishDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//        book.setPublishedInformation(publishedInformation);
//        String string = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(book);
//        System.out.println(string);
        SpringApplication.run(MongoApplication.class);
    }
}
