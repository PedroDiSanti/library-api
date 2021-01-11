package com.pedrodisanti.libraryapi;

import com.pedrodisanti.libraryapi.model.entity.Book;
import com.pedrodisanti.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@SpringBootApplication
public class LibraryApiApplication {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Bean
	public BookService bookService(){
		return new BookService() {
			@Override
			public Book save(Book entity) {
				return null;
			}

			@Override
			public Optional<Book> getById(Long id) {
				return Optional.empty();
			}

			@Override
			public Page<Book> find(Book filter, Pageable pageRequest) {
				return null;
			}

			@Override
			public Book update(Book book) {
				return book;
			}

			@Override
			public void delete(Book book) { }

			@Override
			public Optional<Book> getBookByIsbn(String isbn) { return Optional.empty(); }
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
