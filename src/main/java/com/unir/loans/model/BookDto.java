package com.unir.loans.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private String author;
    private String title;
    private String isbn;
    private Short age;
    private String synapsis;
    private Short stock;

}
