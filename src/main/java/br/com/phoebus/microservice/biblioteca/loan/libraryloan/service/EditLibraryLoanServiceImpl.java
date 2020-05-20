package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanBookAlreadyBorrowedException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanBookNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanNotFoundException;
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
public class EditLibraryLoanServiceImpl implements EditLibaryLoanService {

    private final LibraryLoanRepository libraryLoanRepository;
    private final UserAndBookService userAndBookService;

    @Override//Verificar se o livro e usuário passado existem no outro micro serviço
    public void editLibraryLoan(Long id, LibraryLoanDTO libraryLoanDTO, List<Long> idsBooks) {

        LibraryLoan libraryLoan = libraryLoanRepository.findById(id).orElseThrow(LibraryLoanNotFoundException::new);

        libraryLoan.setLoanTime(libraryLoanDTO.getLoanTime());

        userAndBookService.editUserSpecifId(libraryLoan.getSpecificIDUser(), "null"); //caso troque o user aqui muda
        libraryLoan.setSpecificIDUser(libraryLoanDTO.getSpecificIDUser());

        try {
            userAndBookService.findUserOfLoan(libraryLoanDTO.getSpecificIDUser());
        } catch (FeignException.NotFound e) {
            throw new LibraryLoanUserNotFoundException();
        }

        try {
            userAndBookService.verifyJustExist(idsBooks);
        } catch (FeignException.NotFound e) {
            throw new LibraryLoanBookNotFoundException();
        }
        userAndBookService.changeStatus(id, idsBooks);

        String specificIDLoan = "000" + id;
        userAndBookService.editUserSpecifId(libraryLoanDTO.getSpecificIDUser(), specificIDLoan);
        libraryLoanRepository.save(libraryLoan);
    }
}
