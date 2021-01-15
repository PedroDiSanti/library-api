package com.pedrodisanti.libraryapi.model.repository;

import com.pedrodisanti.libraryapi.model.entity.Book;
import com.pedrodisanti.libraryapi.model.entity.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static com.pedrodisanti.libraryapi.model.repository.BookRepositoryTest.createNewBook;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.PageRequest.of;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class LoanRepositoryTest {
    @Autowired
    private LoanRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should verified if there is a loan for the book passed.")
    public void existsLoanByBookTest() {
        Loan loan = createAndPersistLoan();

        Book book = loan.getBook();

        boolean exists = repository.existsLoanByBook(book);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should find loan by a book's ISBN or by customer.")
    public void findByBookIsbnOrCustomerTest(){
        createAndPersistLoan();

        Page<Loan> result = repository.findByBookIsbnOrCustomer("27011994", "Pedro Di Santi",
                of(0,10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    public Loan createAndPersistLoan(){
        Book book = createNewBook();
        entityManager.persist(book);

        Loan loan = Loan.builder().book(book).customer("Pedro Di Santi").loanDate(LocalDate.now()).build();
        entityManager.persist(loan);

        return loan;
    }

}
