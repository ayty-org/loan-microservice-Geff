package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;

@FunctionalInterface
public interface GetLibraryLoanService {

    LibraryLoanDTO getLibraryLoanForID(Long id);
}
