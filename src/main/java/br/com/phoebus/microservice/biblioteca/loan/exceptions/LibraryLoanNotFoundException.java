package br.com.phoebus.microservice.biblioteca.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LibraryLoanNotFoundException extends RuntimeException {
    public LibraryLoanNotFoundException () {
        super("Library Loan Not Found :o");
    }
}
