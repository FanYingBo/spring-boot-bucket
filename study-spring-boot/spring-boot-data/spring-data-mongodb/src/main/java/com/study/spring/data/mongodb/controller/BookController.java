package com.study.spring.data.mongodb.controller;

import com.study.spring.data.mongodb.pojo.Book;
import com.study.spring.data.mongodb.pojo.PageInfo;
import com.study.spring.data.mongodb.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class BookController {


    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private BookService bookService;

    @PostMapping("/books/add/v1")
    public Book addBook(@RequestBody Book book){
        bookService.addOneBook(book);
        return book;
    }

    @PostMapping("/books/add/v2")
    public Book addBookV2(@RequestBody Book book){
        bookService.addOneBookByTemplate(book);
        return book;
    }

    @GetMapping("/books/fromId")
    public Book getBookById(Long id){
        Book bookById = bookService.getBookById(id);
        return bookById;
    }

    @GetMapping("/books/fromAuthor")
    public List<Book> getBookByAuthor(String author){
        List<Book> bookById = bookService.findByAuthor(author);
        return bookById;
    }
    @PostMapping("/books/updatePrice")
    public Book updatePrice(@RequestBody Book book){
        // 返回更新前
        Book bookById = bookService.updatePrice(book.getName(), book.getPublishedInformation().getPrice());
        return bookById;
    }
    @PostMapping("/books/updateIntro")
    public Book updateBookIntro(@RequestBody Book book){
        Book bookById = bookService.findAndUpdateIntro(book.getName(), book.getIntro());
        return bookById;
    }


    @GetMapping("/books/pagination")
    public PageInfo<Book> paginationQuery(@RequestBody PageInfo pageInfo){
        PageInfo<Book> bookPageInfo = bookService.paginationQuery(pageInfo);
        return bookPageInfo;
    }

}
