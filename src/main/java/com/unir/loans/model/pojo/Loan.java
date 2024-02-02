package com.unir.loans.model.pojo;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "loans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_book")
    private Long idBook;

    @Column(name = "id_client")
    private String idClient;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "status")
    private String status;

    public void update(LoanDto loanDto){
        this.idBook = loanDto.getIdBook();
        this.idClient = loanDto.getIdClient();
        this.startDate = loanDto.getStartDate();
        this.dueDate = loanDto.getDueDate();
        this.status = loanDto.getStatus();
    }

}
