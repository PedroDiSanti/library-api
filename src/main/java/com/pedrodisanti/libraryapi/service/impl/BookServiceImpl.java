package com.pedrodisanti.libraryapi.service.impl;

import com.pedrodisanti.libraryapi.exception.BusinessException;
import com.pedrodisanti.libraryapi.model.entity.Book;
import com.pedrodisanti.libraryapi.model.repository.BookRepository;
import com.pedrodisanti.libraryapi.service.BookService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
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
    public Page<Book> find(Book filter, org.springframework.data.domain.Pageable pageRequest) {
        Example<Book> example = Example.of(filter, ExampleMatcher.matching().withIgnoreCase().withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );

        return repository.findAll(example, pageRequest);
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
