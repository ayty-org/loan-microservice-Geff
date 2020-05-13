package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;


import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ListLibraryLoanServiceImpl implements ListLibraryLoanService {

    private final LibraryLoanRepository libraryLoanRepository;


    @Override
    public List<LibraryLoanDTO> listLibraryLoan() {
        return LibraryLoanDTO.from(libraryLoanRepository.findAll());
    }
}
