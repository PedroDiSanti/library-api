package com.pedrodisanti.libraryapi.model.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private long id;
    private String author;
    private String title;
    private String isbn;
}
