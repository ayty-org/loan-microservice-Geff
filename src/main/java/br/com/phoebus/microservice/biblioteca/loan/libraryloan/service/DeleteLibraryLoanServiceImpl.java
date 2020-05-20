package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LoanWithPendingBooksException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryBookDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoan;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DeleteLibraryLoanServiceImpl implements DeleteLibraryLoanService {

    private final LibraryLoanRepository libraryLoanRepository;
    private final UserAndBookService userAndBookService;


    @Override//Ver se existe livros ainda em emprestimo nesse emprestimo, caso tenha, não excluir.
    public void deleteLibraryLoan(Long id) {
        List<LibraryBookDTO> libraryBookDTOList = userAndBookService.findAllBookOfLoan(id);
        if (libraryBookDTOList.size() > 0) {
            throw new LoanWithPendingBooksException();
        }
        LibraryLoan libraryLoan = libraryLoanRepository.findById(id).orElseThrow(LibraryLoanNotFoundException::new);
        userAndBookService.editUserSpecifId(libraryLoan.getSpecificIDUser(), "null");
        libraryLoanRepository.deleteById(id);
    }
}
