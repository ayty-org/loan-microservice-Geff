package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;

import java.util.List;

@FunctionalInterface
public interface ListLibraryLoanService {

    List<LibraryLoanDTO> listLibraryLoan();
}
