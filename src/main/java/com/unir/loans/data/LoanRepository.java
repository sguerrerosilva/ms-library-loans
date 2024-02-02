package com.unir.loans.data;

import com.unir.loans.data.utils.SearchCriteria;
import com.unir.loans.data.utils.SearchOperation;
import com.unir.loans.data.utils.SearchStatement;
import com.unir.loans.model.pojo.Loan;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LoanRepository {

    private final LoanJpaRepository repository;

    public List<Loan> getLoans(){ return repository.findAll(); }

    public void save(Loan loan) {repository.save(loan);    }

    public List<Loan> search(Integer idBook, String idClient, String status) {
        SearchCriteria<Loan> spec = new SearchCriteria<>();
        if (idBook != null) {
            spec.add(new SearchStatement("idBook", idBook, SearchOperation.EQUAL));
        }

        if (StringUtils.isNotBlank(idClient)) {
            spec.add(new SearchStatement("idClient", idClient, SearchOperation.EQUAL));
        }

        if (StringUtils.isNotBlank(status)) {
            spec.add(new SearchStatement("status", status, SearchOperation.EQUAL));
        }

        return repository.findAll(spec);
    }

}
