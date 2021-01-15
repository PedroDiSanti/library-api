package com.pedrodisanti.libraryapi.model.repository;

import com.pedrodisanti.libraryapi.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Should return true when a book exists on database with the isbn.")
    public void returnTrueWhenIsbnExists(){
        String isbn = "27011994";

        Book book = Book.builder()
                .title("O Conde de Monte Cristo")
                .author("Alexandre Dumas")
                .isbn(isbn)
                .build();

        entityManager.persist(book);

        boolean exists = repository.existsByIsbn(isbn);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when a book does not exists on database with the isbn.")
    public void returnFalseWhenIsbnExists(){
        String isbn = "27011996";


        boolean exists = repository.existsByIsbn(isbn);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should return a book by its id.")
    public void findByIdTest(){
        Book book = createNewBook();

        entityManager.persist(book);

        Optional<Book> foundBook = repository.findById(book.getId());

        assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should save a book and returns its id.")
    public void saveNewBookTest(){
        Book book = createNewBook();

        Book savedBook = repository.save(book);

        assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Should delete a book.")
    public void deleteBookTest(){
        Book book = createNewBook();

        entityManager.persist(book);
        Book foundBook = entityManager.find(Book.class, book.getId());

        repository.delete(foundBook);

        Book deletedBook = entityManager.find(Book.class, book.getId());
        assertThat(deletedBook).isNull();
    }

    public static Book createNewBook() {
        return Book.builder()
                .title("O Conde de Monte Cristo")
                .author("Alexandre Dumas")
                .isbn("27011994")
                .build();
    }


}
