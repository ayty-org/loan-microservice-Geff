package br.com.phoebus.microservice.biblioteca.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LoanWithoutBookException extends RuntimeException {
    public LoanWithoutBookException(){
        super("Loan Without Books");
    }
}
