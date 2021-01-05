package com.pedrodisanti.libraryapi.api.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private long id;
    private String title;
    private String author;
    private String isbn;
}

