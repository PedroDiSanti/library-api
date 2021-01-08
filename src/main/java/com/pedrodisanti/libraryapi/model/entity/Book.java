package com.pedrodisanti.libraryapi.model.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String author;

    @Column
    private String title;

    @Column
    private String isbn;
}
