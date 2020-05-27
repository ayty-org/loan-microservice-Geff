package br.com.phoebus.microservice.biblioteca.loan.loan;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoan;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryUserDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.EditLibraryLoanServiceImpl;
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

import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryLoanBuilder.createLibraryLoan;
import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryLoanDTOBuilder.createLibraryLoanDTO;
import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryUserDTOBuilder.createLibraryUserDTO;
import static org.mockito.ArgumentMatchers.anyLong;
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
        LibraryLoanDTO libraryLoanDTO = createLibraryLoanDTO().specificIDUser(1L).loanTime("5 dias").build();
        LibraryLoan libraryLoan = createLibraryLoan().specificIDUser(2L).build();
        System.out.println(libraryLoan.getSpecificIDUser());

        LibraryUserDTO libraryUserDTO = createLibraryUserDTO().build();
        when(repository.findById(anyLong())).thenReturn(Optional.of(libraryLoan));
        when(userAndBookService.findUserOfLoan(anyLong())).thenReturn(libraryUserDTO);

        editLibraryLoanService.editLibraryLoan(ID_EDIT,libraryLoanDTO, idsBooks);

        ArgumentCaptor<String> captorStringIdLoan = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<LibraryLoan> captorLoan = ArgumentCaptor.forClass(LibraryLoan.class);

        verify(repository, times(1)).findById(ID_EDIT);
        System.out.println(libraryLoan.getSpecificIDUser());
        verify(userAndBookService, times(1)).editUserSpecifId(libraryLoan.getSpecificIDUser(), "null");
        verify(userAndBookService, times(1)).findUserOfLoan(libraryLoanDTO.getSpecificIDUser());
        verify(userAndBookService, times(1)).verifyJustExist(idsBooks);
        verify(userAndBookService, times(1)).changeStatus(ID_EDIT,idsBooks);
        verify(userAndBookService, times(1)).editUserSpecifId(libraryLoanDTO.getSpecificIDUser(),captorStringIdLoan.capture());
        verify(repository, times(1)).save(captorLoan.capture());
    }
}
