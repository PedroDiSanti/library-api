package com.pedrodisanti.libraryapi.service;

import com.pedrodisanti.libraryapi.exception.BusinessException;
import com.pedrodisanti.libraryapi.model.entity.Book;
import com.pedrodisanti.libraryapi.model.repository.BookRepository;
import com.pedrodisanti.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class BookServiceTest {
    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save a new book.")
    public void saveBookTest(){
        Book book = createValidBook();

        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(book)).thenReturn(
                createValidBook()
        );

        Book savedBook = service.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("O Conde de Monte Cristo");
        assertThat(savedBook.getAuthor()).isEqualTo("Alexandre Dumas");
        assertThat(savedBook.getIsbn()).isEqualTo("27011994");
    }

    @Test
    @DisplayName("Should not save a book with duplicated ISBN.")
    public void shouldNotSaveBookWithDuplicatedISBN(){
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado.");

        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Should get a book by id.")
    public void getBookByIdTest(){
        long id = 1L;

        Book book = createValidBook();
        book.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = service.getById(id);

        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Should not get a book by id when the book does not exists.")
    public void shouldNotGetBookByIdTest(){
        long id = 1L;


        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> book = service.getById(id);

        assertThat(book.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should update book's information.")
    public void updateBookByIdTest(){
        long id = 1L;

        Book updatingBook = Book.builder().id(id).build();
        Book updatedBook = createValidBook();

        updatedBook.setId(id);

        Mockito.when(repository.save(updatingBook)).thenReturn(updatedBook);

        Book book = service.update(updatingBook);

        assertThat(book.getId()).isEqualTo(updatedBook.getId());
        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());

    }

    @Test
    @DisplayName("Should not update book's information when the book does not exists.")
    public void shouldNotUpdateBookTest(){
        Book book = null;

        assertThrows(IllegalArgumentException.class, () -> service.update(book));

        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Should delete a book.")
    public void deleteBookTest(){
        Book book = Book.builder().id(1L).build();

        assertDoesNotThrow(()->service.delete(book));

        Mockito.verify(repository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Should not delete a book that does not exists.")
    public void shouldNotDeleteBookTest(){
        Book book = null;

        assertThrows(IllegalArgumentException.class, () -> service.delete(book));

        Mockito.verify(repository, Mockito.never()).delete(book);
    }

    private Book createValidBook() {
        return Book.builder()
                .title("O Conde de Monte Cristo")
                .author("Alexandre Dumas")
                .isbn("27011994")
                .build();
    }
}

