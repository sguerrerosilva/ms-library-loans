package com.unir.loans.service;


import com.google.gson.Gson;
import com.unir.loans.data.LoanRepository;
import com.unir.loans.fecade.LoansFecade;
import com.unir.loans.model.BookDto;
import com.unir.loans.model.pojo.Loan;
import com.unir.loans.model.request.CreateLoanRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class LoanServiceImpl {

    @Autowired
    private LoanRepository repository;

    @Autowired
    private LoansFecade loansFecade;

    public List<Loan> getLoans(Integer idBook, String idClient, String status){

        if( idBook != null || StringUtils.hasLength(idClient) ||  StringUtils.hasLength(status)){
            return repository.search(idBook,idClient,status);
        }
        return repository.getLoans();
    }

    public Loan addLoan(CreateLoanRequest request){
        // Se consulta el libro a realizar prestamo
        BookDto book = loansFecade.getBook(request.getIdBook());

        //Se validan sus existencias
        if (book.getStock() > 0) {
            Loan loan = Loan.builder().idBook(request.getIdBook()).idClient(request.getIdClient()).
                    startDate(Date.valueOf(LocalDate.now())).dueDate(Date.valueOf(LocalDate.now().plusDays(5))).status("borrowed").build();

            //Se guarda el registro del prestamos
            repository.save(loan);
            book.setStock((short) (book.getStock() - 1));
            String body = new Gson().toJson(book);

            //Se envia la peticion para actualizar el libro prestado en el inventario
            loansFecade.patchBook(request.getIdBook(), body);
            return loan;
        }
        return null;
    }


}
