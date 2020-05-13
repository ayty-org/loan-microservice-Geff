package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetLibraryLoanServiceImpl implements GetLibraryLoanService {
    private final LibraryLoanRepository libraryLoanRepository;

    @Override //provavel que eu precise utilizar a interface "UserAndBook" para ir pegar o usuário e setalo no DTO de Loan
    public LibraryLoanDTO getLibraryLoanForID(Long id) {
        return LibraryLoanDTO.from(libraryLoanRepository.findById(id).orElseThrow(LibraryLoanNotFoundException::new));
    }
}
