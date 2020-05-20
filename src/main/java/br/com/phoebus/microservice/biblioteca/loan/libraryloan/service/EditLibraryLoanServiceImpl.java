package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanUserNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoan;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EditLibraryLoanServiceImpl implements EditLibaryLoanService {

    private final LibraryLoanRepository libraryLoanRepository;
    private final UserAndBookService userAndBookService;

    @Override//Verificar se o livro e usuário passado existem no outro micro serviço
    public void editLibraryLoan(Long id, LibraryLoanDTO libraryLoanDTO) {

        LibraryLoan libraryLoan = libraryLoanRepository.findById(id).orElseThrow(LibraryLoanNotFoundException::new);

        libraryLoan.setLoanTime(libraryLoanDTO.getLoanTime());
        libraryLoan.setSpecificIDUser(libraryLoanDTO.getSpecificIDUser());
        try {
            userAndBookService.findUserOfLoan(libraryLoanDTO.getSpecificIDUser());
        } catch (FeignException.NotFound e) {
            throw new LibraryLoanUserNotFoundException();
        }
        String specificIDLoan = "000"+id;
        userAndBookService.editUserSpecifId(libraryLoanDTO.getSpecificIDUser(),specificIDLoan);
        libraryLoanRepository.save(libraryLoan);
    }
}
