package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoan;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SaveLibraryLoanServiceImpl implements SaveLibraryLoanService {
    private final LibraryLoanRepository libraryLoanRepository;
    @Override
    public void saveLibraryBook(LibraryLoanDTO libraryLoanDTO) {
        libraryLoanRepository.save(LibraryLoan.to(libraryLoanDTO));
    }
}
