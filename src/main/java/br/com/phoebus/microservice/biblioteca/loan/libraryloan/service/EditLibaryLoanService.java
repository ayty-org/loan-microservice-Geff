package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;

@FunctionalInterface
public interface EditLibaryLoanService {

    void editLibraryLoan(Long id, LibraryLoanDTO libraryLoanDTO);
}
