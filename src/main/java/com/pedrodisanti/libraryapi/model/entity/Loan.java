package com.pedrodisanti.libraryapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne
    private Book book;

    @Column(length = 100)
    private String customer;

    @Column
    private String customerEmail;

    @Column
    private Boolean returned;

    @Column
    private LocalDate loanDate;
}
