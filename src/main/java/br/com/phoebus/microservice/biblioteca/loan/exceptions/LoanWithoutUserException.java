package br.com.phoebus.microservice.biblioteca.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LoanWithoutUserException extends RuntimeException {
    public LoanWithoutUserException () {
        super("Loan Without User");
    }
}