package com.unir.loans.model.pojo;

import jakarta.persistence.Column;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LoanDto {

    private Long idBook;
    private String idClient;
    private Date startDate;
    private Date dueDate;
    private String status;
}
