package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import org.springframework.data.domain.Page;

@FunctionalInterface
public interface PageLibraryLoanService {

    Page<LibraryLoanDTO> listPageLibraryLoan(Integer page, Integer size);
}
