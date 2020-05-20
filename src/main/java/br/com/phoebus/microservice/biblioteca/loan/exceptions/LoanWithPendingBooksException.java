package br.com.phoebus.microservice.biblioteca.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class LoanWithPendingBooksException extends RuntimeException {
    public LoanWithPendingBooksException () {
        super("Loan with pending books");
    }
}
