package com.study.spring.data.mongodb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.study.spring.data.mongodb.pojo.Book;
import com.study.spring.data.mongodb.pojo.PageInfo;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.ExecutableUpdateOperation;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.core.schema.JsonSchemaObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookService {

    private static  final Logger logger = LoggerFactory.getLogger(BookService.class);

    private String collectionName = null;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 方法一：
     * 这种通过Collection添加的方式在存储时没有以下两个字段
     *  _id: ObjectId("62f8a7e79f0a22121d617b95")
     *  _class: 'com.study.spring.data.mongodb.pojo.Book' 字段
     * @param book
     * @return
     */
    public Book addOneBook(Book book) {
        book.setId(System.currentTimeMillis());
        MongoCollection<Document> collection;
        if(!mongoTemplate.collectionExists(Book.class)){
            collection = mongoTemplate.createCollection(Book.class);
            collectionName = collection.getNamespace().getCollectionName();
        }else{
            if(Objects.isNull(collectionName)){
                collectionName = mongoTemplate.getCollectionName(Book.class);
            }
            collection = mongoTemplate.getCollection(collectionName);
        }
        logger.info("Insert ont to collection [{}]", collectionName);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String valueAsString = objectMapper.writeValueAsString(book);
            collection.insertOne(Document.parse(valueAsString));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  book;
    }

    /**
     * 方法二 spring boot mongo template
     * 模板方法 如果没有配置 {@link com.study.spring.data.mongodb.config.MongoConfiguration#mappingMongoConverter}
     * 插入时会有 _class: 'com.study.spring.data.mongodb.pojo.Book'
     * @param book
     * @return
     */
    public Book addOneBookByTemplate(Book book) {
        book.setId(System.currentTimeMillis());
        Book book1 = mongoTemplate.insert(book);
        return book1;
    }

    public Book getBookById(Long id){
        Query query = Query.query(Criteria.where("id").is(id));
        return  mongoTemplate.findOne(query, Book.class);
    }

    public List<Book> findByAuthor(String author){
        Query query = Query.query(Criteria.where("author").is(author));
        return  mongoTemplate.find(query, Book.class);
    }

    /**
     * 更新数据
     * in a fluent API style
     * @param name
     * @param price
     * @return
     */
    public Book updatePrice(String name, BigDecimal price){
        Query query = Query.query(Criteria.where("name").is(name));
        Update update = BasicUpdate.update("publishedInformation.price", price);
        Optional<Book> bookOptional = mongoTemplate
                .update(Book.class).matching(query).apply(update).findAndModify();
        return bookOptional.orElse(null);
    }
    /**
     * 原子更新
     * @param name  书名
     * @param intro 简介
     * @return
     */
    public Book findAndUpdateIntro(String name, String intro){
        Query query = Query.query(Criteria.where("name").is(name));
        Update update = BasicUpdate.update("intro", intro);
        Book book = mongoTemplate.findAndModify(query, update, Book.class);
        return book;
    }

    /**
     * 分页查询
     * @param pageInfo
     * @return
     */
    public PageInfo<Book> paginationQuery(PageInfo pageInfo){
        int limit = pageInfo.getLimit();
        int skip = pageInfo.getSkip();
        Query query = new Query();
        long count = mongoTemplate.count(query, Book.class);
        long totalPage = count % pageInfo.getPageSize() == 0  ? count / pageInfo.getPageSize() : count / pageInfo.getPageSize() + 1;
        List<Book> bookList = mongoTemplate.find(query.limit(limit).skip(skip), Book.class);
        PageInfo<Book> pageInfo1 = new PageInfo<>();
        pageInfo1.setPageIndex(pageInfo.getPageIndex());
        pageInfo1.setTotalCount(count);
        pageInfo1.setPageSize(pageInfo.getPageSize());
        pageInfo1.setTotalPage((int) totalPage);
        pageInfo1.setData(bookList);
        return pageInfo1;
    }
}
