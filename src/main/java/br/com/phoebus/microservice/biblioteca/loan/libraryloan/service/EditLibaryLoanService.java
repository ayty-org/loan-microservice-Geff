package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanEditDTO;

import java.util.List;

@FunctionalInterface
public interface EditLibaryLoanService {

    void editLibraryLoan(Long id, LibraryLoanDTO libraryLoanDTO, List<Long> idsBooks);
}
