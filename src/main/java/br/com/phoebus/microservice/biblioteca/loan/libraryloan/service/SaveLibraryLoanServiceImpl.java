package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanBookAlreadyBorrowedException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanBookNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanUserNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoan;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SaveLibraryLoanServiceImpl implements SaveLibraryLoanService {
    private final LibraryLoanRepository libraryLoanRepository;
    private final UserAndBookService userAndBookService;

    @Override
    public void saveLibraryBook(LibraryLoanDTO libraryLoanDTO, List<Long> idsBooks) {

        try {
            userAndBookService.findUserOfLoan(libraryLoanDTO.getSpecificIDUser());
        } catch (FeignException.NotFound e) {
            throw new LibraryLoanUserNotFoundException();
        }
        try {
            userAndBookService.verifyBooks(idsBooks);
        } catch (FeignException.NotFound e) {
            throw new LibraryLoanBookNotFoundException();
        } catch (FeignException.NotAcceptable e) {
            throw new LibraryLoanBookAlreadyBorrowedException();
        }
        Long id = libraryLoanRepository.save(LibraryLoan.to(libraryLoanDTO)).getId();
        userAndBookService.changeStatus(id, idsBooks);
        userAndBookService.editUserSpecifId(libraryLoanDTO.getSpecificIDUser(), "000" + id);
    }
}
