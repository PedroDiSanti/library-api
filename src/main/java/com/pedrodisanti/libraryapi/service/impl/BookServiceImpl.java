package com.pedrodisanti.libraryapi.service.impl;

import com.pedrodisanti.libraryapi.exception.BusinessException;
import com.pedrodisanti.libraryapi.model.entity.Book;
import com.pedrodisanti.libraryapi.model.repository.BookRepository;
import com.pedrodisanti.libraryapi.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())){
            throw new BusinessException("Isbn já cadastrado.");
        }
        return repository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public Book update(Book book) {
        if(book == null){
            throw new IllegalArgumentException("Book should not be null.");
        }

        return this.repository.save(book);
    }

    @Override
    public void delete(Book book) {
        if(book == null){
            throw new IllegalArgumentException("Book should not be null.");
        }

        this.repository.delete(book);
    }
}
