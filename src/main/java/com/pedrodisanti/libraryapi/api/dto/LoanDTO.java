package com.pedrodisanti.libraryapi.api.dto;

import com.pedrodisanti.libraryapi.model.entity.Loan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {
    private Loan id;
    private String isbn;
    private String customer;
    private BookDTO book;
}
