package com.pedrodisanti.libraryapi.service.impl;

import com.pedrodisanti.libraryapi.api.dto.LoanFilterDTO;
import com.pedrodisanti.libraryapi.exception.BusinessException;
import com.pedrodisanti.libraryapi.model.entity.Loan;
import com.pedrodisanti.libraryapi.model.repository.LoanRepository;
import com.pedrodisanti.libraryapi.service.LoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class LoanServiceImpl implements LoanService {
    private final LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        if(repository.existsLoanByBook(loan.getBook())){
            throw new BusinessException("The books is already loaned.");
        }
        return repository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return repository.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterDTO filterDTO, Pageable pageable) {
        return repository.findByBookIsbnOrCustomer(filterDTO.getIsbn(), filterDTO.getCustomer(), pageable);
    }
}
