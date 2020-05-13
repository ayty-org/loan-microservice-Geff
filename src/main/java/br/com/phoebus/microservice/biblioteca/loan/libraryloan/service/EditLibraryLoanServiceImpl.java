package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoan;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EditLibraryLoanServiceImpl implements EditLibaryLoanService {

    private final LibraryLoanRepository libraryLoanRepository;

    @Override
    public void editLibraryLoan(Long id, LibraryLoanDTO libraryLoanDTO) {
        LibraryLoan libraryLoan = libraryLoanRepository.findById(id).orElseThrow(LibraryLoanNotFoundException::new);

        libraryLoan.setLoanTime(libraryLoanDTO.getLoanTime());

        libraryLoanRepository.save(libraryLoan);
    }
}
