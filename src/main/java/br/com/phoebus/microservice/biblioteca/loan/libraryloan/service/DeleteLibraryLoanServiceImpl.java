package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteLibraryLoanServiceImpl implements DeleteLibraryLoanService{

    private final LibraryLoanRepository libraryLoanRepository;


    @Override
    public void deleteLibraryLoan(Long id) {
        if (!libraryLoanRepository.existsById(id)) {
            throw new LibraryLoanNotFoundException();
        }
        libraryLoanRepository.deleteById(id);
    }
}
