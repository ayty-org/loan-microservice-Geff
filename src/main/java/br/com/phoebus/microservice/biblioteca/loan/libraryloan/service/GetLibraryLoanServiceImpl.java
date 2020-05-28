package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LoanWithoutBookException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LoanWithoutUserException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetLibraryLoanServiceImpl implements GetLibraryLoanService {
    private final UserAndBookService userAndBookService;
    private final LibraryLoanRepository libraryLoanRepository;

    @Override
    public LibraryLoanDTO getLibraryLoanForID(Long id) {
        LibraryLoanDTO libraryLoanDTO;
        libraryLoanDTO = LibraryLoanDTO.from(libraryLoanRepository.findById(id).orElseThrow(LibraryLoanNotFoundException::new));
        try {
            libraryLoanDTO.setLibraryUserDTO(userAndBookService.findUserOfLoan(libraryLoanDTO.getSpecificIDUser()));
        } catch (FeignException.NotFound e) {
            throw new LoanWithoutUserException();
        }
        libraryLoanDTO.setLibraryBookDTOList(userAndBookService.findAllBookOfLoan(id));

        if(libraryLoanDTO.getLibraryBookDTOList().size() == 0) {
            throw new LoanWithoutBookException();
        }
        return libraryLoanDTO;
    }
}
