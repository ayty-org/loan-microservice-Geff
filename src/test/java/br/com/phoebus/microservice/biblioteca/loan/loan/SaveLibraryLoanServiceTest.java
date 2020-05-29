package br.com.phoebus.microservice.biblioteca.loan.loan;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanBookAlreadyBorrowedException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanBookNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanUserNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoan;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
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
        LibraryLoan libraryLoan = LibraryLoan.to(libraryLoanDTO);
        libraryLoan.setId(1L);

        when(repository.save(any(LibraryLoan.class))).thenReturn(libraryLoan);
        saveLibraryLoanService.saveLibraryLoan(libraryLoanDTO, idsBooks);

        ArgumentCaptor<LibraryLoan> captorLoan = ArgumentCaptor.forClass(LibraryLoan.class);
        ArgumentCaptor<String> captorString = ArgumentCaptor.forClass(String.class);

        verify(userAndBookService, times(1)).findUserOfLoan(eq(libraryLoanDTO.getSpecificIDUser()));
        verify(userAndBookService, times(1)).verifyBooks(idsBooks);
        verify(repository, times(1)).save(captorLoan.capture());
        verify(userAndBookService, times(1)).changeStatus(1L, idsBooks);
        verify(userAndBookService, times(1)).editUserSpecifId(eq(libraryLoanDTO.getSpecificIDUser()), captorString.capture());

        LibraryLoan result = captorLoan.getValue();
        String resultSpecificId = captorString.getValue();

        assertAll("Loan",
                () -> assertThat(result.getLoanTime(), is(libraryLoanDTO.getLoanTime())),
                () -> assertThat(result.getSpecificIDUser(), is(libraryLoanDTO.getSpecificIDUser())),
                () -> assertThat(resultSpecificId, is("000" + libraryLoan.getId()))
        );
    }

    @Test
    @DisplayName("Lança uma exceção usuário não encontrado")
    void shouldLoanUserNotFound() {

        List<Long> idsBooks = Arrays.asList(1L, 2L);
        LibraryLoanDTO libraryLoanDTO = createLibraryLoanDTO().specificIDUser(1L).build();

        when(userAndBookService.findUserOfLoan(anyLong())).thenThrow(new LibraryLoanUserNotFoundException());

        assertThrows(LibraryLoanUserNotFoundException.class, () -> saveLibraryLoanService.saveLibraryLoan(libraryLoanDTO, idsBooks));


        verify(userAndBookService, times(1)).findUserOfLoan(eq(libraryLoanDTO.getSpecificIDUser()));
        verify(userAndBookService, times(0)).verifyBooks(idsBooks);
        verify(repository, times(0)).save(any(LibraryLoan.class));
        verify(userAndBookService, times(0)).changeStatus(1L, idsBooks);
        verify(userAndBookService, times(0)).editUserSpecifId(anyLong(), anyString());

    }

    @Test
    @DisplayName("Lança uma exceção livro não encontrado")
    void shouldLoanBookNotFound() {

        List<Long> idsBooks = Arrays.asList(1L, 2L);
        LibraryLoanDTO libraryLoanDTO = createLibraryLoanDTO().specificIDUser(1L).build();

        doThrow(new LibraryLoanBookNotFoundException()).when(userAndBookService).verifyBooks(idsBooks);

        assertThrows(LibraryLoanBookNotFoundException.class, () -> saveLibraryLoanService.saveLibraryLoan(libraryLoanDTO, idsBooks));

        verify(userAndBookService, times(1)).findUserOfLoan(eq(libraryLoanDTO.getSpecificIDUser()));
        verify(userAndBookService, times(1)).verifyBooks(idsBooks);
        verify(repository, times(0)).save(any(LibraryLoan.class));
        verify(userAndBookService, times(0)).changeStatus(1L, idsBooks);
        verify(userAndBookService, times(0)).editUserSpecifId(anyLong(), anyString());

    }

    @Test
    @DisplayName("Lança uma exceção livro já emprestado")
    void shouldLoanBookAlreadyBorrowed() {

        List<Long> idsBooks = Arrays.asList(1L, 2L);
        LibraryLoanDTO libraryLoanDTO = createLibraryLoanDTO().specificIDUser(1L).build();

        doThrow(new LibraryLoanBookAlreadyBorrowedException()).when(userAndBookService).verifyBooks(idsBooks);

        assertThrows(LibraryLoanBookAlreadyBorrowedException.class, () -> saveLibraryLoanService.saveLibraryLoan(libraryLoanDTO, idsBooks));

        verify(userAndBookService, times(1)).findUserOfLoan(eq(libraryLoanDTO.getSpecificIDUser()));
        verify(userAndBookService, times(1)).verifyBooks(idsBooks);
        verify(repository, times(0)).save(any(LibraryLoan.class));
        verify(userAndBookService, times(0)).changeStatus(1L, idsBooks);
        verify(userAndBookService, times(0)).editUserSpecifId(anyLong(), anyString());

    }

}
