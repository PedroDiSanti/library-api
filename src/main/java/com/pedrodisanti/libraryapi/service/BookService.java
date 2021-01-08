package com.pedrodisanti.libraryapi.service;

import com.pedrodisanti.libraryapi.model.entity.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book any);

    Optional<Book> getById(Long id);

    Book update(Book book);

    void delete(Book book);
}
