package br.com.phoebus.microservice.biblioteca.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LibraryLoanUserNotFoundException extends RuntimeException {
    public LibraryLoanUserNotFoundException () {
        super("Library User Not Found");
    }
}
