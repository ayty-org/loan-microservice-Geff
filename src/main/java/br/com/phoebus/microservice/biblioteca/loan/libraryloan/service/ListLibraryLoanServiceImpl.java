package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;


import br.com.phoebus.microservice.biblioteca.loan.exceptions.LoanWithoutBookException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LoanWithoutUserException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ListLibraryLoanServiceImpl implements ListLibraryLoanService {

    private final LibraryLoanRepository libraryLoanRepository;
    private final UserAndBookService userAndBookService;


    @Override
    public List<LibraryLoanDTO> listLibraryLoan() {
        List<LibraryLoanDTO> libraryLoanDTOList = LibraryLoanDTO.from(libraryLoanRepository.findAll());
        for (LibraryLoanDTO libraryLoanDTO : libraryLoanDTOList) {
            try {
                libraryLoanDTO.setLibraryUserDTO(userAndBookService.findUserOfLoan(libraryLoanDTO.getSpecificIDUser()));
            } catch (FeignException.NotFound e) {
                throw new LoanWithoutUserException();
            }
            try {
                libraryLoanDTO.setLibraryBookDTOList(userAndBookService.findAllBookOfLoan(libraryLoanDTO.getId()));
            } catch (FeignException.NotFound e) {
                throw new LoanWithoutBookException();
            }
        }
        return libraryLoanDTOList;
    }
}
