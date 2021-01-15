package com.pedrodisanti.libraryapi.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedrodisanti.libraryapi.api.dto.LoanDTO;
import com.pedrodisanti.libraryapi.api.dto.LoanFilterDTO;
import com.pedrodisanti.libraryapi.api.dto.ReturnedLoanDTO;
import com.pedrodisanti.libraryapi.exception.BusinessException;
import com.pedrodisanti.libraryapi.model.entity.Book;
import com.pedrodisanti.libraryapi.model.entity.Loan;
import com.pedrodisanti.libraryapi.service.BookService;
import com.pedrodisanti.libraryapi.service.BookServiceTest;
import com.pedrodisanti.libraryapi.service.LoanService;
import com.pedrodisanti.libraryapi.service.LoanServiceTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.pedrodisanti.libraryapi.model.repository.BookRepositoryTest.createNewBook;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static java.util.Optional.of;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LoanController.class)
public class LoanControllerTest {
    static final String LOAN_API = "/api/loans";

    @Autowired
    MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private LoanService loanService;

    @Test
    @DisplayName("Should make a loan.")
    public void createLoanTest() throws Exception{
        LoanDTO dto = LoanDTO.builder().isbn("27011994").customer("Pedro Di Santi").build();

        String json = new ObjectMapper().writeValueAsString(dto);

        Book book = Book.builder().id(1L).isbn("27011994").build();
        BDDMockito.given(bookService.getBookByIsbn("27011994"))
                .willReturn(of(Book.builder().id(1L).isbn("27011994").build()));

        Loan loan = Loan.builder().id(1L).customer("Pedro Di Santi").book(book).loanDate(LocalDate.now()).build();
        BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("Should not make a loan.")
    public void shouldNotCreateLoanTest() throws Exception{
        LoanDTO dto = LoanDTO.builder().isbn("27011994").customer("Pedro Di Santi").build();

        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(bookService.getBookByIsbn("27011994")).willReturn(Optional.empty());


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Book not found."));
    }

    @Test
    @DisplayName("Should not make a loan when the book is already loaned.")
    public void loanedBookTest() throws Exception{
        LoanDTO dto = LoanDTO.builder().isbn("27011994").customer("Pedro Di Santi").build();

        String json = new ObjectMapper().writeValueAsString(dto);

        Book book = Book.builder().id(1L).isbn("27011994").build();
        BDDMockito.given(bookService.getBookByIsbn("27011994"))
                .willReturn(Optional.of(book));

        BDDMockito.given(loanService.save(Mockito.any(Loan.class)))
                .willThrow(new BusinessException("Book already loaned!"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Book already loaned!"));
    }

    @Test
    @DisplayName("Should return a book.")
    public void returnBookTest() throws Exception{
        Loan loan = Loan.builder().id(1L).build();

        ReturnedLoanDTO dto = ReturnedLoanDTO.builder().returned(true).build();

        BDDMockito.given(loanService.getById(Mockito.anyLong())).willReturn(Optional.of(loan));

        String json = new ObjectMapper().writeValueAsString(dto);

        mvc.perform(patch(LOAN_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isOk()
        );

        Mockito.verify(loanService, Mockito.times(1)).update(loan);
    }

    @Test
    @DisplayName("Should not return a book when its is not loaned.")
    public void returnBookNotLoanedTest() throws Exception{
        ReturnedLoanDTO dto = ReturnedLoanDTO.builder().returned(true).build();

        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(loanService.getById(Mockito.anyLong())).willReturn(Optional.empty());

        mvc.perform(patch(LOAN_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isNotFound()
        );
    }

    @Test
    @DisplayName("Should filter loans.")
    public void findLoanTest() throws Exception {
        long id = 1L;

        Loan loan = LoanServiceTest.createLoan();
        loan.setId(id);

        Book book = Book.builder().id(id).isbn("27011994").build();
        loan.setBook(book);

        BDDMockito.given(loanService.find(Mockito.any(LoanFilterDTO.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<Loan>(singletonList(loan), PageRequest.of(0,10), 1));

        String queryString = String.format("?isbn=%s&customer=%s&page=0&size=10", loan.getBook().getIsbn(), loan.getCustomer());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LOAN_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(10))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }
}
