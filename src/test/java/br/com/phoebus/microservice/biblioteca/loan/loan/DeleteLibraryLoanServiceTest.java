package br.com.phoebus.microservice.biblioteca.loan.loan;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LoanWithPendingBooksException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryBookDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoan;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.DeleteLibraryLoanServiceImpl;
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
import java.util.Optional;

import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryBookDTOBuilder.createLibraryBookDTO;
import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryLoanBuilder.createLibraryLoan;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("Service")
@ExtendWith(MockitoExtension.class)
@DisplayName("Valida a funcionalidade do serviço em deletar um emprestimo")
public class DeleteLibraryLoanServiceTest {
    @Mock
    private LibraryLoanRepository repository;
    @Mock
    private UserAndBookService userAndBookService;

    private DeleteLibraryLoanServiceImpl deleteLibraryLoanService;

    private final Long ID_DELETE = 1L;

    @BeforeEach
    void setUp() {
        this.deleteLibraryLoanService = new DeleteLibraryLoanServiceImpl(repository, userAndBookService);
    }

    @Test
    @DisplayName("Deleta um emprestimo")
    void shouldDeleteLoan() {
        LibraryLoan libraryLoan = createLibraryLoan().specificIDUser(1L).build();
        List<LibraryBookDTO> libraryBookDTOList = Arrays.asList();
        when(userAndBookService.findAllBookOfLoan(anyLong())).thenReturn(libraryBookDTOList);
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.getOne(anyLong())).thenReturn(libraryLoan);

        deleteLibraryLoanService.deleteLibraryLoan(ID_DELETE);

        ArgumentCaptor<Long> captorIdUser = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> captorIdLoan = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> captorString = ArgumentCaptor.forClass(String.class);

        verify(repository, times(1)).existsById(ID_DELETE);
        verify(repository, times(1)).getOne(ID_DELETE);
        verify(repository, times(1)).deleteById(ID_DELETE);
        verify(userAndBookService, times(1)).findAllBookOfLoan(captorIdLoan.capture());
        verify(userAndBookService, times(1)).editUserSpecifId(captorIdUser.capture(),captorString.capture());

        Long resultSpecificIdUser = captorIdUser.getValue();
        Long resultIdLoan = captorIdLoan.getValue();
        String resultSpecificIdLoan = captorString.getValue();

        assertAll("Loan",
                () -> assertThat(resultSpecificIdLoan, is("null")),
                () -> assertThat(resultSpecificIdUser, is(libraryLoan.getSpecificIDUser())),
                () -> assertThat(resultIdLoan, is(libraryLoan.getId()))
        );
    }

    @Test
    @DisplayName("Lança uma exceção de Loan With Pending Book")
    void shouldExceptionPendingBook() {

        LibraryBookDTO libraryBookDTO = createLibraryBookDTO().borrowed(true).specificIDLoan(1L).build();
        List<LibraryBookDTO> libraryBookDTOList = Arrays.asList(libraryBookDTO);
        when(repository.existsById(anyLong())).thenReturn(true);
        when(userAndBookService.findAllBookOfLoan(anyLong())).thenReturn(libraryBookDTOList);

        ArgumentCaptor<Long> captorIdLoan = ArgumentCaptor.forClass(Long.class);

        assertThrows(LoanWithPendingBooksException.class, () -> deleteLibraryLoanService.deleteLibraryLoan(ID_DELETE));

        verify(repository, times(1)).existsById(ID_DELETE);
        verify(repository, times(0)).getOne(ID_DELETE);
        verify(repository, times(0)).deleteById(ID_DELETE);
        verify(userAndBookService, times(1)).findAllBookOfLoan(captorIdLoan.capture());
        verify(userAndBookService, times(0)).editUserSpecifId(anyLong(),anyString());

        Long resultIdLoan = captorIdLoan.getValue();

        assertThat(resultIdLoan, is(ID_DELETE));
    }

    @Test
    @DisplayName("Lança uma exceção de Loan Not Found")
    void shouldExceptionLoanNotFound() {
        when(repository.existsById(anyLong())).thenReturn(false);

        assertThrows(LibraryLoanNotFoundException.class, () -> deleteLibraryLoanService.deleteLibraryLoan(ID_DELETE));

        verify(repository, times(1)).existsById(ID_DELETE);
        verify(repository, times(0)).getOne(ID_DELETE);
        verify(repository, times(0)).deleteById(ID_DELETE);
        verify(userAndBookService, times(0)).findAllBookOfLoan(anyLong());
        verify(userAndBookService, times(0)).editUserSpecifId(anyLong(),anyString());
    }
}
