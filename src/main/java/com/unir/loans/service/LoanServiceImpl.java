package com.unir.loans.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
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

    @Autowired
    private ObjectMapper objectMapper;

    public List<Loan> getLoans(Integer idBook, String idClient, String status){

        if( idBook != null || StringUtils.hasLength(idClient) ||  StringUtils.hasLength(status)){
            return repository.search(idBook,idClient,status);
        }
        return repository.getLoans();
    }

    public Loan getLoan(Long idLoan){
        return repository.getLoan(idLoan);
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

    public Loan updateLoan(Long idLoan, String request){
        Loan loan = repository.getLoan(idLoan);
        if (loan != null){
            try{
                JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(request));
                JsonNode target = jsonMergePatch.apply(objectMapper.readTree(objectMapper.writeValueAsString(loan)));
                Loan bookPatched = objectMapper.treeToValue(target, Loan.class);
                return repository.save(bookPatched);
            }catch (JsonProcessingException | JsonPatchException e){
                log.error("Error updating book: {}",idLoan);
                return null;
            }
        }else{
            return null;
        }
    }


}
