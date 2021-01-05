package com.pedrodisanti.libraryapi.api.resource;

import org.springframework.http.HttpStatus;

import com.pedrodisanti.libraryapi.model.entity.Book;
import com.pedrodisanti.libraryapi.service.BookService;
import com.pedrodisanti.libraryapi.api.dto.BookDTO;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody BookDTO dto){
        Book entity = Book.builder()
                .author(dto.getAuthor())
                .title(dto.getTitle())
                .isbn(dto.getIsbn())
                .build();

        entity = service.save(entity);

        return BookDTO.builder()
                .id(entity.getId())
                .author(entity.getAuthor())
                .title(entity.getTitle())
                .isbn(entity.getIsbn())
                .build();
    }
}
