package br.com.phoebus.microservice.biblioteca.loan.loan;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LoanWithoutBookException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LoanWithoutUserException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryBookDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoan;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryUserDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.GetLibraryLoanServiceImpl;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.UserAndBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryBookDTOBuilder.createLibraryBookDTO;
import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryLoanBuilder.createLibraryLoan;
import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryUserDTOBuilder.createLibraryUserDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("Service")
@ExtendWith(MockitoExtension.class)
@DisplayName("Valida a funcionalidade do serviço em pegar um emprestimo")
public class GetLibraryLoanServiceTest {
    @Mock
    private UserAndBookService userAndBookService;
    @Mock
    private LibraryLoanRepository repository;

    private GetLibraryLoanServiceImpl getLibraryLoanService;

    private final Long ID_GET = 1L;

    @BeforeEach
    void setUp() {
        this.getLibraryLoanService = new GetLibraryLoanServiceImpl(userAndBookService, repository);
    }

    @Test
    @DisplayName("Traz um emprestimo")
    void shouldGetLoan() {
        LibraryLoan libraryLoan = createLibraryLoan().id(ID_GET).build();
        when(repository.findById(anyLong())).thenReturn(Optional.of(libraryLoan));

        LibraryUserDTO libraryUserDTO = createLibraryUserDTO().build();
        when(userAndBookService.findUserOfLoan(anyLong())).thenReturn(libraryUserDTO);

        LibraryBookDTO libraryBookDTO1 = createLibraryBookDTO().author("1 author").borrowed(true).specificIDLoan(ID_GET).build();
        LibraryBookDTO libraryBookDTO2 = createLibraryBookDTO().title("2 title").borrowed(true).specificIDLoan(ID_GET).id(2L).build();
        List<LibraryBookDTO> libraryBookDTOList = Arrays.asList(libraryBookDTO1, libraryBookDTO2);
        when(userAndBookService.findAllBookOfLoan(anyLong())).thenReturn(libraryBookDTOList);

        LibraryLoanDTO result = getLibraryLoanService.getLibraryLoanForID(ID_GET);

        assertAll("Loan",
                () -> assertThat(result.getId(), is(ID_GET)),
                () -> assertThat(result.getLoanTime(), is(libraryLoan.getLoanTime())),

                () -> assertThat(result.getSpecificIDUser(), is(libraryLoan.getSpecificIDUser())),
                () -> assertThat(result.getLibraryUserDTO().getId(), is(libraryUserDTO.getId())),
                () -> assertThat(result.getLibraryUserDTO().getName(), is(libraryUserDTO.getName())),
                () -> assertThat(result.getLibraryUserDTO().getAge(), is(libraryUserDTO.getAge())),
                () -> assertThat(result.getLibraryUserDTO().getTelephone(), is(libraryUserDTO.getTelephone())),
                () -> assertThat(result.getLibraryUserDTO().getSpecificIDLoan(), is(libraryUserDTO.getSpecificIDLoan())),

                () -> assertThat(result.getLibraryBookDTOList().size(), is(libraryBookDTOList.size())),

                () -> assertThat(result.getLibraryBookDTOList().get(0).getAuthor(), is(libraryBookDTOList.get(0).getAuthor())),
                () -> assertThat(result.getLibraryBookDTOList().get(0).getIsbn(), is(libraryBookDTOList.get(0).getIsbn())),
                () -> assertThat(result.getLibraryBookDTOList().get(0).getResume(), is(libraryBookDTOList.get(0).getResume())),
                () -> assertThat(result.getLibraryBookDTOList().get(0).getId(), is(libraryBookDTOList.get(0).getId())),
                () -> assertThat(result.getLibraryBookDTOList().get(0).getSpecificIDLoan(), is(libraryBookDTOList.get(0).getSpecificIDLoan())),
                () -> assertThat(result.getLibraryBookDTOList().get(0).getTitle(), is(libraryBookDTOList.get(0).getTitle())),
                () -> assertThat(result.getLibraryBookDTOList().get(0).getYear(), is(libraryBookDTOList.get(0).getYear())),
                () -> assertThat(result.getLibraryBookDTOList().get(0).isBorrowed(), is(libraryBookDTOList.get(0).isBorrowed())),

                () -> assertThat(result.getLibraryBookDTOList().get(1).getAuthor(), is(libraryBookDTOList.get(1).getAuthor())),
                () -> assertThat(result.getLibraryBookDTOList().get(1).getIsbn(), is(libraryBookDTOList.get(1).getIsbn())),
                () -> assertThat(result.getLibraryBookDTOList().get(1).getResume(), is(libraryBookDTOList.get(1).getResume())),
                () -> assertThat(result.getLibraryBookDTOList().get(1).getId(), is(libraryBookDTOList.get(1).getId())),
                () -> assertThat(result.getLibraryBookDTOList().get(1).getSpecificIDLoan(), is(libraryBookDTOList.get(1).getSpecificIDLoan())),
                () -> assertThat(result.getLibraryBookDTOList().get(1).getTitle(), is(libraryBookDTOList.get(1).getTitle())),
                () -> assertThat(result.getLibraryBookDTOList().get(1).getYear(), is(libraryBookDTOList.get(1).getYear())),
                () -> assertThat(result.getLibraryBookDTOList().get(1).isBorrowed(), is(libraryBookDTOList.get(1).isBorrowed()))
        );

        verify(repository, times(1)).findById(ID_GET);
        verify(userAndBookService, times(1)).findUserOfLoan(libraryLoan.getSpecificIDUser());
        verify(userAndBookService, times(1)).findAllBookOfLoan(ID_GET);
    }

    @Test
    @DisplayName("Lança uma exceção de Loan Not Found")
    void shouldExceptionLoanNotFound() {

        when(repository.findById(anyLong())).thenThrow(new LibraryLoanNotFoundException());

        assertThrows(LibraryLoanNotFoundException.class, () -> getLibraryLoanService.getLibraryLoanForID(ID_GET));

        verify(repository, times(1)).findById(ID_GET);
        verify(userAndBookService, times(0)).findUserOfLoan(anyLong());
        verify(userAndBookService, times(0)).findAllBookOfLoan(ID_GET);

    }
    @Test
    @DisplayName("Lança uma exceção de Loan Whithout User")
    void shouldExceptionLoanWithoutUser() {
        LibraryLoan libraryLoan = createLibraryLoan().id(ID_GET).build();
        when(repository.findById(anyLong())).thenReturn(Optional.of(libraryLoan));

        when(userAndBookService.findUserOfLoan(anyLong())).thenThrow(new LoanWithoutUserException());

        assertThrows(LoanWithoutUserException.class, () -> getLibraryLoanService.getLibraryLoanForID(ID_GET));

        verify(repository, times(1)).findById(ID_GET);
        verify(userAndBookService, times(1)).findUserOfLoan(libraryLoan.getSpecificIDUser());
        verify(userAndBookService, times(0)).findAllBookOfLoan(ID_GET);

    }
    @Test
    @DisplayName("Lança uma exceção de Loan Without Book")
    void shouldExceptionLoanWithoutBook() {
        LibraryLoan libraryLoan = createLibraryLoan().id(ID_GET).build();
        when(repository.findById(anyLong())).thenReturn(Optional.of(libraryLoan));

        LibraryUserDTO libraryUserDTO = createLibraryUserDTO().build();
        when(userAndBookService.findUserOfLoan(anyLong())).thenReturn(libraryUserDTO);

        when(userAndBookService.findAllBookOfLoan(anyLong())).thenReturn(Arrays.asList());

        assertThrows(LoanWithoutBookException.class, () -> getLibraryLoanService.getLibraryLoanForID(ID_GET));

        verify(repository, times(1)).findById(ID_GET);
        verify(userAndBookService, times(1)).findUserOfLoan(libraryLoan.getSpecificIDUser());
        verify(userAndBookService, times(1)).findAllBookOfLoan(ID_GET);
    }
}
