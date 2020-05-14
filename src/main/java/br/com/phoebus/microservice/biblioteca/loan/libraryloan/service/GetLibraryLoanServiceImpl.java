package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetLibraryLoanServiceImpl implements GetLibraryLoanService {
    private final UserAndBookServiceImpl userAndBookService;
    private final LibraryLoanRepository libraryLoanRepository;

    @Override
    public LibraryLoanDTO getLibraryLoanForID(Long id) {
        LibraryLoanDTO libraryLoanDTO;
        libraryLoanDTO = LibraryLoanDTO.from(libraryLoanRepository.findById(id).orElseThrow(LibraryLoanNotFoundException::new));

        libraryLoanDTO.setLibraryUserDTO(userAndBookService.findUserOfLoan(libraryLoanDTO.getSpecificIDUser()));

        return libraryLoanDTO;
    }
}
