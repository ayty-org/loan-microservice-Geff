package br.com.phoebus.microservice.biblioteca.loan.loan;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoan;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryUserDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.SaveLibraryLoanServiceImpl;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.UserAndBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryLoanDTOBuilder.createLibraryLoanDTO;
import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryUserDTOBuilder.createLibraryUserDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@Tag("Service")
@ExtendWith(MockitoExtension.class)
@DisplayName("Valida a funcionalidade do serviço em salvar um emprestimo")
public class SaveLibraryLoanServiceTest {
    @Mock
    private LibraryLoanRepository repository;
    @Mock
    private UserAndBookService userAndBookService;

    private SaveLibraryLoanServiceImpl saveLibraryLoanService;

    @BeforeEach
    void setUp() {
        this.saveLibraryLoanService = new SaveLibraryLoanServiceImpl(repository, userAndBookService);
    }

    @Test
    @DisplayName("Salva um emprestimo")
    void shouldSaveEmprestimo() {

        List<Long> idsBooks = Arrays.asList(1L, 2L);
        LibraryLoanDTO libraryLoanDTO = createLibraryLoanDTO().specificIDUser(1L).build();
        LibraryUserDTO libraryUserDTO = createLibraryUserDTO().build();

        when(userAndBookService.findUserOfLoan(anyLong())).thenReturn(libraryUserDTO);
        when(repository.save(any(LibraryLoan.class)).getId()).thenReturn(1L);
        saveLibraryLoanService.saveLibraryBook(libraryLoanDTO, idsBooks);

        ArgumentCaptor<LibraryLoan> captorLoan = ArgumentCaptor.forClass(LibraryLoan.class);
        ArgumentCaptor<String> captorString = ArgumentCaptor.forClass(String.class);
        verify(repository, times(1)).save(captorLoan.capture());
        verify(userAndBookService, times(1)).findUserOfLoan(eq(libraryLoanDTO.getSpecificIDUser()));
        verify(userAndBookService, times(1)).verifyBooks(idsBooks);
        verify(userAndBookService, times(1)).changeStatus(1L, idsBooks);
        verify(userAndBookService, times(1)).editUserSpecifId(libraryLoanDTO.getSpecificIDUser(), captorString.capture());

        LibraryLoan result = captorLoan.getValue();
        String resultSpecificId = captorString.getValue();
    }

    @Test
    @DisplayName("Lança uma exceção")
    void shouldLoanUserNotFound() {
        
    }

}
