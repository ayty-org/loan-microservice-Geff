package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LoanWithoutUserException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoan;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SaveLibraryLoanServiceImpl implements SaveLibraryLoanService {
    private final LibraryLoanRepository libraryLoanRepository;
    private final UserAndBookService userAndBookService;

    @Override
    public void saveLibraryBook(LibraryLoanDTO libraryLoanDTO) {

        try {
            userAndBookService.findUserOfLoan(libraryLoanDTO.getSpecificIDUser());
        } catch (FeignException.NotFound e) {
            throw new LoanWithoutUserException();
        }

        Long id = libraryLoanRepository.save(LibraryLoan.to(libraryLoanDTO)).getId();

        userAndBookService.editSpecifId(libraryLoanDTO.getSpecificIDUser(), "000"+id);
    }
}
