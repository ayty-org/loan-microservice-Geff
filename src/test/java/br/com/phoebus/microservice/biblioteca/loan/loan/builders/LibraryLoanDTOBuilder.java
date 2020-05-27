package br.com.phoebus.microservice.biblioteca.loan.loan.builders;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;

public class LibraryLoanDTOBuilder {

    public static LibraryLoanDTO.Builder createLibraryLoanDTO() {

        return LibraryLoanDTO.builder()
                .id(1L)
                .loanTime("2 dias")
                .specificIDUser(1L);
    }
}
