package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LoanWithoutBookException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LoanWithoutUserException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ListPageLibraryLoanServiceImpl implements ListPageLibraryLoanService {

    private final LibraryLoanRepository libraryLoanRepository;
    private final UserAndBookService userAndBookService;

    @Override
    public Page<LibraryLoanDTO> listPageLibraryLoan(Integer page, Integer size) {
        Page<LibraryLoanDTO> libraryLoanDTOPage = LibraryLoanDTO.from(libraryLoanRepository.findAll(PageRequest.of(
                page, size, Sort.Direction.ASC, "id")));

        for (LibraryLoanDTO libraryLoanDTO : libraryLoanDTOPage) {
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

        return libraryLoanDTOPage;
    }
}
