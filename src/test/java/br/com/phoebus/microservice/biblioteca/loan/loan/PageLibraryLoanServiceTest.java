package br.com.phoebus.microservice.biblioteca.loan.loan;

import br.com.phoebus.microservice.biblioteca.loan.exceptions.LoanWithoutBookException;
import br.com.phoebus.microservice.biblioteca.loan.exceptions.LoanWithoutUserException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryBookDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoan;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanRepository;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryUserDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.ListLibraryLoanServiceImpl;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.PageLibraryLoanServiceImpl;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.UserAndBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryBookDTOBuilder.createLibraryBookDTO;
import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryLoanBuilder.createLibraryLoan;
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
@DisplayName("valida a funcionalidade do serviço em paginar os emprestimos")
public class PageLibraryLoanServiceTest {
    @Mock
    private LibraryLoanRepository repository;
    @Mock
    private UserAndBookService userAndBookService;

    private PageLibraryLoanServiceImpl pageLibraryLoanService;

    @BeforeEach
    void setUp() {
        this.pageLibraryLoanService = new PageLibraryLoanServiceImpl(repository, userAndBookService);
    }

    @Test
    @DisplayName("Lista os emprestimos")
    void shouldListLend() {
        Pageable pageRequest = PageRequest.of(0, 2, Sort.Direction.ASC, "id");

        LibraryLoan libraryLoan1 = createLibraryLoan().id(1L).specificIDUser(1L).loanTime("50 dias").build();
        LibraryLoan libraryLoan2 = createLibraryLoan().id(2L).specificIDUser(2L).build();
        List<LibraryLoan> libraryLoanList = Arrays.asList(libraryLoan1, libraryLoan2);
        PageImpl<LibraryLoan> libraryLoanPage = new PageImpl<>(libraryLoanList);
        when(repository.findAll(pageRequest)).thenReturn(libraryLoanPage);

        LibraryUserDTO libraryUserDTO1 = createLibraryUserDTO().build();
        LibraryUserDTO libraryUserDTO2 = createLibraryUserDTO().build();
        when(userAndBookService.findUserOfLoan(eq(libraryLoan1.getSpecificIDUser()))).thenReturn(libraryUserDTO1);
        when(userAndBookService.findUserOfLoan(eq(libraryLoan2.getSpecificIDUser()))).thenReturn(libraryUserDTO2);

        LibraryBookDTO libraryBookDTO1 = createLibraryBookDTO().title("title 1").borrowed(true).specificIDLoan(1L).build();
        LibraryBookDTO libraryBookDTO2 = createLibraryBookDTO().author("author 2").borrowed(true).specificIDLoan(1L).build();
        List<LibraryBookDTO> libraryBookDTOList1 = Arrays.asList(libraryBookDTO1, libraryBookDTO2);
        LibraryBookDTO libraryBookDTO3 = createLibraryBookDTO().resume("resume 3").borrowed(true).specificIDLoan(2L).build();
        LibraryBookDTO libraryBookDTO4 = createLibraryBookDTO().year(2000).borrowed(true).specificIDLoan(2L).build();
        List<LibraryBookDTO> libraryBookDTOList2 = Arrays.asList(libraryBookDTO3, libraryBookDTO4);

        when(userAndBookService.findAllBookOfLoan(eq(libraryLoan1.getId()))).thenReturn(libraryBookDTOList1);
        when(userAndBookService.findAllBookOfLoan(eq(libraryLoan2.getId()))).thenReturn(libraryBookDTOList2);

        Page<LibraryLoanDTO> result = pageLibraryLoanService.listPageLibraryLoan(0, 2);

        assertAll("Loan",
                () -> assertThat(result.getTotalPages(), is(libraryLoanPage.getTotalPages())),
                () -> assertThat(result.getTotalElements(), is(libraryLoanPage.getTotalElements())),
                () -> assertThat(result.getContent().size(), is(libraryLoanList.size())),

                () -> assertThat(result.getContent().get(0).getId(), is(libraryLoan1.getId())),
                () -> assertThat(result.getContent().get(0).getLoanTime(), is(libraryLoan1.getLoanTime())),
                () -> assertThat(result.getContent().get(0).getSpecificIDUser(), is(libraryLoan1.getSpecificIDUser())),

                () -> assertThat(result.getContent().get(0).getLibraryUserDTO().getId(), is(libraryUserDTO1.getId())),
                () -> assertThat(result.getContent().get(0).getLibraryUserDTO().getSpecificIDLoan(), is(libraryUserDTO1.getSpecificIDLoan())),
                () -> assertThat(result.getContent().get(0).getLibraryUserDTO().getName(), is(libraryUserDTO1.getName())),
                () -> assertThat(result.getContent().get(0).getLibraryUserDTO().getTelephone(), is(libraryUserDTO1.getTelephone())),
                () -> assertThat(result.getContent().get(0).getLibraryUserDTO().getAge(), is(libraryUserDTO1.getAge())),

                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(0).getId(), is(libraryBookDTO1.getId())),
                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(0).getYear(), is(libraryBookDTO1.getYear())),
                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(0).getTitle(), is(libraryBookDTO1.getTitle())),
                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(0).getSpecificIDLoan(), is(libraryBookDTO1.getSpecificIDLoan())),
                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(0).getResume(), is(libraryBookDTO1.getResume())),
                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(0).getAuthor(), is(libraryBookDTO1.getAuthor())),
                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(0).getIsbn(), is(libraryBookDTO1.getIsbn())),
                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(0).isBorrowed(), is(libraryBookDTO1.isBorrowed())),

                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(1).getId(), is(libraryBookDTO2.getId())),
                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(1).getYear(), is(libraryBookDTO2.getYear())),
                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(1).getTitle(), is(libraryBookDTO2.getTitle())),
                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(1).getSpecificIDLoan(), is(libraryBookDTO2.getSpecificIDLoan())),
                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(1).getResume(), is(libraryBookDTO2.getResume())),
                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(1).getAuthor(), is(libraryBookDTO2.getAuthor())),
                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(1).getIsbn(), is(libraryBookDTO2.getIsbn())),
                () -> assertThat(result.getContent().get(0).getLibraryBookDTOList().get(1).isBorrowed(), is(libraryBookDTO2.isBorrowed())),


                () -> assertThat(result.getContent().get(1).getId(), is(libraryLoan2.getId())),
                () -> assertThat(result.getContent().get(1).getLoanTime(), is(libraryLoan2.getLoanTime())),
                () -> assertThat(result.getContent().get(1).getSpecificIDUser(), is(libraryLoan2.getSpecificIDUser())),

                () -> assertThat(result.getContent().get(1).getLibraryUserDTO().getId(), is(libraryUserDTO2.getId())),
                () -> assertThat(result.getContent().get(1).getLibraryUserDTO().getSpecificIDLoan(), is(libraryUserDTO2.getSpecificIDLoan())),
                () -> assertThat(result.getContent().get(1).getLibraryUserDTO().getName(), is(libraryUserDTO2.getName())),
                () -> assertThat(result.getContent().get(1).getLibraryUserDTO().getTelephone(), is(libraryUserDTO2.getTelephone())),
                () -> assertThat(result.getContent().get(1).getLibraryUserDTO().getAge(), is(libraryUserDTO2.getAge())),

                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(0).getId(), is(libraryBookDTO3.getId())),
                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(0).getYear(), is(libraryBookDTO3.getYear())),
                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(0).getTitle(), is(libraryBookDTO3.getTitle())),
                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(0).getSpecificIDLoan(), is(libraryBookDTO3.getSpecificIDLoan())),
                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(0).getResume(), is(libraryBookDTO3.getResume())),
                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(0).getAuthor(), is(libraryBookDTO3.getAuthor())),
                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(0).getIsbn(), is(libraryBookDTO3.getIsbn())),
                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(0).isBorrowed(), is(libraryBookDTO3.isBorrowed())),

                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(1).getId(), is(libraryBookDTO4.getId())),
                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(1).getYear(), is(libraryBookDTO4.getYear())),
                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(1).getTitle(), is(libraryBookDTO4.getTitle())),
                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(1).getSpecificIDLoan(), is(libraryBookDTO4.getSpecificIDLoan())),
                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(1).getResume(), is(libraryBookDTO4.getResume())),
                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(1).getAuthor(), is(libraryBookDTO4.getAuthor())),
                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(1).getIsbn(), is(libraryBookDTO4.getIsbn())),
                () -> assertThat(result.getContent().get(1).getLibraryBookDTOList().get(1).isBorrowed(), is(libraryBookDTO4.isBorrowed()))
        );

        verify(repository, times(1)).findAll(any(PageRequest.class));
        verify(userAndBookService, times(1)).findUserOfLoan(libraryLoan1.getSpecificIDUser());
        verify(userAndBookService, times(1)).findUserOfLoan(libraryLoan2.getSpecificIDUser());
        verify(userAndBookService, times(2)).findUserOfLoan(anyLong());
        verify(userAndBookService, times(1)).findAllBookOfLoan(libraryLoan1.getId());
        verify(userAndBookService, times(1)).findAllBookOfLoan(libraryLoan2.getId());
        verify(userAndBookService, times(2)).findAllBookOfLoan(anyLong());
    }

    @Test
    @DisplayName("Retorna a lista vazia")
    void shouldListVoid() {
        Pageable pageRequest = PageRequest.of(0, 2, Sort.Direction.ASC, "id");
        PageImpl<LibraryLoan> libraryLoanPage = new PageImpl<>(Arrays.asList());
        when(repository.findAll(pageRequest)).thenReturn(libraryLoanPage);

        Page<LibraryLoanDTO> result = pageLibraryLoanService.listPageLibraryLoan(0, 2);

        assertThat(libraryLoanPage.getContent().size(), is(0));
        assertThat(libraryLoanPage.getTotalElements(), is(0L));
        assertThat(libraryLoanPage.getTotalPages(), is(1));

        verify(repository, times(1)).findAll(any(PageRequest.class));
        verify(userAndBookService, times(0)).findUserOfLoan(anyLong());
        verify(userAndBookService, times(0)).findAllBookOfLoan(anyLong());

    }

    @Test
    @DisplayName("Lança uma exceção de emprestimo sem usuário")
    void shouldLoanWithoutUser() {
        Pageable pageRequest = PageRequest.of(0, 2, Sort.Direction.ASC, "id");

        LibraryLoan libraryLoan1 = createLibraryLoan().id(1L).specificIDUser(1L).loanTime("50 dias").build();
        LibraryLoan libraryLoan2 = createLibraryLoan().id(2L).specificIDUser(2L).build();
        List<LibraryLoan> libraryLoanList = Arrays.asList(libraryLoan1, libraryLoan2);
        PageImpl<LibraryLoan> libraryLoanPage = new PageImpl<>(libraryLoanList);
        when(repository.findAll(pageRequest)).thenReturn(libraryLoanPage);

        when(userAndBookService.findUserOfLoan(eq(libraryLoan1.getSpecificIDUser()))).thenThrow(new LoanWithoutUserException());

        assertThrows(LoanWithoutUserException.class, () -> pageLibraryLoanService.listPageLibraryLoan(0, 2));

        verify(repository, times(1)).findAll(any(PageRequest.class));
        verify(userAndBookService, times(1)).findUserOfLoan(anyLong());
        verify(userAndBookService, times(0)).findAllBookOfLoan(anyLong());

    }

    @Test
    @DisplayName("Lança uma exceção de emprestimo sem livros")
    void shouldLoanWithoutBook() {
        Pageable pageRequest = PageRequest.of(0, 2, Sort.Direction.ASC, "id");

        LibraryLoan libraryLoan1 = createLibraryLoan().id(1L).specificIDUser(1L).loanTime("50 dias").build();
        LibraryLoan libraryLoan2 = createLibraryLoan().id(2L).specificIDUser(2L).build();
        List<LibraryLoan> libraryLoanList = Arrays.asList(libraryLoan1, libraryLoan2);
        PageImpl<LibraryLoan> libraryLoanPage = new PageImpl<>(libraryLoanList);
        when(repository.findAll(pageRequest)).thenReturn(libraryLoanPage);

        LibraryUserDTO libraryUserDTO1 = createLibraryUserDTO().build();
        when(userAndBookService.findUserOfLoan(eq(libraryLoan1.getSpecificIDUser()))).thenReturn(libraryUserDTO1);

        when(userAndBookService.findAllBookOfLoan(eq(libraryLoan1.getId()))).thenThrow(new LoanWithoutBookException());

        assertThrows(LoanWithoutBookException.class, () -> pageLibraryLoanService.listPageLibraryLoan(0, 2));

        verify(repository, times(1)).findAll(any(PageRequest.class));
        verify(userAndBookService, times(1)).findUserOfLoan(anyLong());
        verify(userAndBookService, times(1)).findAllBookOfLoan(anyLong());

    }
}
