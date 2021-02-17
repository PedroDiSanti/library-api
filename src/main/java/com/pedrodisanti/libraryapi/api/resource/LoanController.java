package com.pedrodisanti.libraryapi.api.resource;

import com.pedrodisanti.libraryapi.api.dto.LoanDTO;
import com.pedrodisanti.libraryapi.api.dto.LoanFilterDTO;
import com.pedrodisanti.libraryapi.api.dto.ReturnedLoanDTO;
import com.pedrodisanti.libraryapi.model.entity.Book;
import com.pedrodisanti.libraryapi.model.entity.Loan;
import com.pedrodisanti.libraryapi.service.BookService;
import com.pedrodisanti.libraryapi.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static com.pedrodisanti.libraryapi.api.resource.BookController.getLoanDTOS;

@Slf4j
@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService service;
    private final BookService bookService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody LoanDTO dto){
        Book book = bookService.getBookByIsbn(dto.getIsbn())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found."));

        Loan entity = Loan.builder()
                .book(book)
                .customer(dto.getCustomer())
                .loanDate(LocalDate.now())
                .build();

        entity = service.save(entity);

        return entity.getId();
    }

    @PatchMapping("{id}")
    public void returnBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO dto){
        Loan loan = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        loan.setReturned(dto.getReturned());

        service.update(loan);
    }

    @GetMapping
    public Page<LoanDTO> find(LoanFilterDTO dto, Pageable pageRequest){
        Page<Loan> result = service.find(dto, pageRequest);
        return getLoanDTOS(pageRequest, result, modelMapper);
    }
}
