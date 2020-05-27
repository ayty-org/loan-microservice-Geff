package br.com.phoebus.microservice.biblioteca.loan.loan.builders;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoan;

public class LibraryLoanBuilder {

    public static LibraryLoan.Builder createLibraryLoan() {

        return LibraryLoan.builder()
                .id(2L)
                .loanTime("2 dias").specificIDUser(2L);
    }
}
