package br.com.phoebus.microservice.biblioteca.loan.loan;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanBookNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanUserNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoan;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryUserDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.EditLibraryLoanServiceImpl;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.UserAndBookService;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryLoanBuilder.createLibraryLoan;
import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryLoanDTOBuilder.createLibraryLoanDTO;
import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryUserDTOBuilder.createLibraryUserDTO;
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
@DisplayName("Valida a funcionalidade do serviço em editar um emprestimo")
public class EditLibraryLoanServiceTest {

    @Mock
    private LibraryLoanRepository repository;
    @Mock
    private UserAndBookService userAndBookService;

    private EditLibraryLoanServiceImpl editLibraryLoanService;

    private final Long ID_EDIT = 1L;

    @BeforeEach
    void setUp() {
        this.editLibraryLoanService = new EditLibraryLoanServiceImpl(repository, userAndBookService);
    }

    @Test
    @DisplayName("Edita um emprestimo")
    void shouldEditLoan() {
        List<Long> idsBooks = Arrays.asList(1L, 2L);
        LibraryLoanDTO libraryLoanDTO = createLibraryLoanDTO()
                .specificIDUser(1L)
                .loanTime("5 dias").build();
        LibraryLoan libraryLoan = createLibraryLoan()
                .specificIDUser(2L).build();
        Long idSpecificUserOfLong = libraryLoan.getSpecificIDUser();

        LibraryUserDTO libraryUserDTO = createLibraryUserDTO().build();
        when(repository.findById(anyLong())).thenReturn(Optional.of(libraryLoan));
        when(userAndBookService.findUserOfLoan(anyLong())).thenReturn(libraryUserDTO);

        editLibraryLoanService.editLibraryLoan(ID_EDIT,libraryLoanDTO, idsBooks);

        ArgumentCaptor<String> captorStringIdLoan = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<LibraryLoan> captorLoan = ArgumentCaptor.forClass(LibraryLoan.class);
        ArgumentCaptor<Long> captorSpecificIdUser = ArgumentCaptor.forClass(Long.class);

        verify(repository, times(1))
                .findById(ID_EDIT);
        verify(userAndBookService, times(1))
                .findUserOfLoan(libraryLoanDTO.getSpecificIDUser());
        verify(userAndBookService, times(1))
                .verifyJustExist(idsBooks);
        verify(userAndBookService, times(1))
                .editUserSpecifId(idSpecificUserOfLong, "null");
        verify(userAndBookService, times(1))
                .changeStatus(ID_EDIT,idsBooks);
        verify(userAndBookService, times(2))
                .editUserSpecifId(captorSpecificIdUser.capture(),captorStringIdLoan.capture());
        verify(repository, times(1))
                .save(captorLoan.capture());

        LibraryLoan result = captorLoan.getValue();

        assertAll("Loan",
                () -> assertThat(result.getId(), is(libraryLoanDTO.getId())),
                () -> assertThat(result.getSpecificIDUser(), is(libraryLoanDTO.getSpecificIDUser())),
                () -> assertThat(result.getLoanTime(), is(libraryLoanDTO.getLoanTime())),
                () -> assertThat(captorSpecificIdUser.getValue(), is(libraryLoanDTO.getSpecificIDUser())),
                () -> assertThat(captorStringIdLoan.getValue(), is("000"+ID_EDIT))
        );
    }

    @Test
    @DisplayName("Deve lançar uma exceção de user not found")
    void shouldExceptionLibraryLoanUserNotFound() {

        List<Long> idsBooks = Arrays.asList(1L, 2L);
        LibraryLoanDTO libraryLoanDTO = createLibraryLoanDTO().specificIDUser(2L).build();
        LibraryLoan libraryLoan = createLibraryLoan().specificIDUser(1L).build();
        LibraryUserDTO libraryUserDTO = createLibraryUserDTO().build();

        when(repository.findById(anyLong())).thenReturn(Optional.of(libraryLoan));
        when(userAndBookService.findUserOfLoan(anyLong())).thenThrow(new LibraryLoanUserNotFoundException()); // Não sei qual o tipo de exceção que vem

        assertThrows(LibraryLoanUserNotFoundException.class, () -> editLibraryLoanService.editLibraryLoan(ID_EDIT,libraryLoanDTO, idsBooks));

        verify(repository, times(1))
                .findById(ID_EDIT);
        verify(userAndBookService, times(1))
                .findUserOfLoan(libraryLoanDTO.getSpecificIDUser());
        verify(userAndBookService, times(0))
                .verifyJustExist(idsBooks);
        verify(userAndBookService, times(0))
                .editUserSpecifId(anyLong(), anyString());
        verify(userAndBookService, times(0))
                .changeStatus(ID_EDIT,idsBooks);
        verify(repository, times(0))
                .save(any(LibraryLoan.class));
    }

    @Test
    @DisplayName("Lança uma exceção de Book Not Found")
    void shouldExceptionBookNotFound() {

        List<Long> idsBooks = Arrays.asList(1L, 2L);
        LibraryLoanDTO libraryLoanDTO = createLibraryLoanDTO().specificIDUser(2L).build();
        LibraryLoan libraryLoan = createLibraryLoan().specificIDUser(1L).build();
        LibraryUserDTO libraryUserDTO = createLibraryUserDTO().build();

        when(repository.findById(anyLong())).thenReturn(Optional.of(libraryLoan));
        when(userAndBookService.findUserOfLoan(anyLong())).thenReturn(libraryUserDTO);
        doThrow(new LibraryLoanBookNotFoundException()).when(userAndBookService).verifyJustExist(idsBooks); // Não sei qual o tipo de exceção que vem e nem como capturar

        assertThrows(LibraryLoanBookNotFoundException.class, () -> editLibraryLoanService.editLibraryLoan(ID_EDIT,libraryLoanDTO, idsBooks));

        verify(repository, times(1))
                .findById(ID_EDIT);
        verify(userAndBookService, times(1))
                .findUserOfLoan(libraryLoanDTO.getSpecificIDUser());
        verify(userAndBookService, times(1))
                .verifyJustExist(idsBooks);
        verify(userAndBookService, times(0))
                .editUserSpecifId(anyLong(), anyString());
        verify(userAndBookService, times(0))
                .changeStatus(ID_EDIT,idsBooks);
        verify(repository, times(0))
                .save(any(LibraryLoan.class));
    }
}
