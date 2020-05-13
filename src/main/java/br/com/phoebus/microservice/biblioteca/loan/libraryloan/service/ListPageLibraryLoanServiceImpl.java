package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ListPageLibraryLoanServiceImpl implements ListPageLibraryLoanService {

    private final LibraryLoanRepository libraryLoanRepository;

    @Override
    public Page<LibraryLoanDTO> listPageLibraryLoan(Integer page, Integer size) {
        return LibraryLoanDTO.from(
                libraryLoanRepository.findAll(PageRequest.of(
                        page, size, Sort.Direction.ASC, "id")));
    }
}
