package com.pedrodisanti.libraryapi.service;

import com.pedrodisanti.libraryapi.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {
    Book save(Book any);

    Optional<Book> getById(Long id);

    Page<Book> find(Book filter, Pageable pageRequest);

    Book update(Book book);

    void delete(Book book);

    Optional<Book> getBookByIsbn(String isbn);
}
