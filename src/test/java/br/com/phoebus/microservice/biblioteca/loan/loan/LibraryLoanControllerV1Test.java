package br.com.phoebus.microservice.biblioteca.loan.loan;


import br.com.phoebus.microservice.biblioteca.loan.exceptions.LibraryLoanNotFoundException;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryBookDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryUserDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.DeleteLibraryLoanService;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.EditLibraryLoanService;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.GetLibraryLoanService;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.ListLibraryLoanService;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.PageLibraryLoanService;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.SaveLibraryLoanService;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.v1.LibraryLoanControllerV1;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryBookDTOBuilder.createLibraryBookDTO;
import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryLoanDTOBuilder.createLibraryLoanDTO;
import static br.com.phoebus.microservice.biblioteca.loan.loan.builders.LibraryUserDTOBuilder.createLibraryUserDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("Controller")
@ExtendWith(SpringExtension.class)
@WebMvcTest(LibraryLoanControllerV1.class)
@DisplayName("Valida a funcionalidade do controlador de emprestimo")
public class LibraryLoanControllerV1Test {

    private final String URL_LOAN = "/v1/libraryLoan";
    private final String CONT_TYPE = "application/json";
    private final String UTF8 = "utf-8";
    private final Long ID_LOAN = 1L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeleteLibraryLoanService deleteLibraryLoanService;
    @MockBean
    private EditLibraryLoanService editLibraryLoanService;
    @MockBean
    private GetLibraryLoanService getLibraryLoanService;
    @MockBean
    private ListLibraryLoanService listLibraryLoanService;
    @MockBean
    private PageLibraryLoanService pageLibraryLoanService;
    @MockBean
    private SaveLibraryLoanService saveLibraryLoanService;

    @Test
    @DisplayName("Deleta um emprestimo")
    void shouldDeleteLoan() throws Exception {

        mockMvc.perform(delete(URL_LOAN + "/{id}", ID_LOAN)
                .contentType(CONT_TYPE)
                .characterEncoding(UTF8))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deleteLibraryLoanService, times(1)).deleteLibraryLoan(ID_LOAN);
    }

    @Test
    @DisplayName("Lança uma exceção loan not found")
    void shouldExceptionLoanNotFound() throws Exception {

        doThrow(new LibraryLoanNotFoundException()).when(deleteLibraryLoanService).deleteLibraryLoan(ID_LOAN);

        mockMvc.perform(delete(URL_LOAN + "/{id}", ID_LOAN)
                .contentType(CONT_TYPE)
                .characterEncoding(UTF8))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(deleteLibraryLoanService, times(1)).deleteLibraryLoan(ID_LOAN);

    }

    @Test
    @DisplayName("Edita um Loan")
    void shouldEditLoan() throws Exception {

        LibraryLoanDTO libraryLoanDTO = createLibraryLoanDTO().build();

        mockMvc.perform(put(URL_LOAN + "/{id}", ID_LOAN)
                .characterEncoding(UTF8)
                .contentType(CONT_TYPE)
                .param("idsBooks" , "1, 2")
                .content(objectMapper.writeValueAsString(libraryLoanDTO)))
                .andDo(print())
                .andExpect(status().isNoContent());

        ArgumentCaptor<LibraryLoanDTO> captorLibraryLoanDTO = ArgumentCaptor.forClass(LibraryLoanDTO.class);
        ArgumentCaptor<Long> captorLong = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<List<Long>> captorList = ArgumentCaptor.forClass(List.class);

        verify(editLibraryLoanService, times(1)).editLibraryLoan(captorLong.capture(), captorLibraryLoanDTO.capture(), captorList.capture());

        LibraryLoanDTO resultLoanDTO = captorLibraryLoanDTO.getValue();
        Long resultIdLong = captorLong.getValue();
        List<Long> resultIdsBooks = captorList.getValue();

        assertAll("Loan controller",
                () -> assertThat(resultIdsBooks.size(), is(2)),
                () -> assertThat(resultIdsBooks.get(0), is(1L)),
                () -> assertThat(resultIdsBooks.get(1), is(2L)),
                () -> assertThat(resultIdLong, is(ID_LOAN)),
                () -> assertThat(resultLoanDTO.getLoanTime(), is(libraryLoanDTO.getLoanTime())),
                () -> assertThat(resultLoanDTO.getSpecificIDUser(), is(libraryLoanDTO.getSpecificIDUser())),
                () -> assertThat(resultLoanDTO.getId(), is(libraryLoanDTO.getId()))
        );
    }

    @Test
    @DisplayName("lança uma exceção de bad request pelo specific ID")
    void shouldBadRequestOnEdit() throws Exception {

        LibraryLoanDTO libraryLoanDTO = createLibraryLoanDTO().specificIDUser(null).build();

        mockMvc.perform(put(URL_LOAN + "/{id}", ID_LOAN)
                .characterEncoding(UTF8)
                .contentType(CONT_TYPE)
                .param("idsBooks" , "1, 2")
                .content(objectMapper.writeValueAsString(libraryLoanDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(editLibraryLoanService, times(0)).editLibraryLoan(anyLong(), any(LibraryLoanDTO.class), anyList());
    }

    @Test
    @DisplayName("lança uma exceção de bad request pelo LoanTime")
    void shouldExceptionBadRequestOnEditForLoanTime() throws Exception {

        LibraryLoanDTO libraryLoanDTO = createLibraryLoanDTO().loanTime("").build();

        mockMvc.perform(put(URL_LOAN + "/{id}", ID_LOAN)
                .characterEncoding(UTF8)
                .contentType(CONT_TYPE)
                .param("idsBooks" , "1, 2")
                .content(objectMapper.writeValueAsString(libraryLoanDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(editLibraryLoanService, times(0)).editLibraryLoan(anyLong(), any(LibraryLoanDTO.class), anyList());
    }

    @Test
    @DisplayName("Traz um emprestimo")
    void shouldGetALoan() throws Exception {
        LibraryBookDTO libraryBookDTO1 = createLibraryBookDTO().build();
        LibraryBookDTO libraryBookDTO2 = createLibraryBookDTO().id(2L).build();
        List<LibraryBookDTO> libraryBookDTOList = Arrays.asList(libraryBookDTO1, libraryBookDTO2);

        LibraryUserDTO libraryUserDTO = createLibraryUserDTO().build();

        LibraryLoanDTO libraryLoanDTO = createLibraryLoanDTO()
                .libraryBookDTOList(libraryBookDTOList)
                .libraryUserDTO(libraryUserDTO).build();

        when(getLibraryLoanService.getLibraryLoanForID(anyLong())).thenReturn(libraryLoanDTO);

        MvcResult mvcResult = mockMvc.perform(get(URL_LOAN + "/{id}", ID_LOAN)
                .contentType(CONT_TYPE)
                .characterEncoding(UTF8))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        String resultResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(objectMapper.writeValueAsString(libraryLoanDTO), is(resultResponseBody));

        verify(getLibraryLoanService, times(1)).getLibraryLoanForID(ID_LOAN);
    }

    @Test
    @DisplayName("Lança uma exceção de not found")
    void shouldLoanNotFoundOnGet() throws Exception {
        when(getLibraryLoanService.getLibraryLoanForID(anyLong())).thenThrow(new LibraryLoanNotFoundException());

        mockMvc.perform(get(URL_LOAN + "/{id}", ID_LOAN)
                .characterEncoding(UTF8)
                .contentType(CONT_TYPE))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(getLibraryLoanService, times(1)).getLibraryLoanForID(ID_LOAN);
    }

    @Test
    @DisplayName("Lista os emprestimos")
    void shouldListLoan() throws Exception {

        //create Loan 1
        LibraryBookDTO libraryBookDTO1 = createLibraryBookDTO().build();
        LibraryBookDTO libraryBookDTO2 = createLibraryBookDTO().id(2L).build();
        List<LibraryBookDTO> libraryBookDTOList1 = Arrays.asList(libraryBookDTO1, libraryBookDTO2);

        LibraryUserDTO libraryUserDTO1 = createLibraryUserDTO().build();

        LibraryLoanDTO libraryLoanDTO1 = createLibraryLoanDTO()
                .libraryBookDTOList(libraryBookDTOList1)
                .libraryUserDTO(libraryUserDTO1).build();


        //create Loan 2
        LibraryBookDTO libraryBookDTO3 = createLibraryBookDTO().id(3L).build();
        LibraryBookDTO libraryBookDTO4 = createLibraryBookDTO().id(4L).build();
        List<LibraryBookDTO> libraryBookDTOList2 = Arrays.asList(libraryBookDTO3, libraryBookDTO4);

        LibraryUserDTO libraryUserDTO2 = createLibraryUserDTO().id(2L).build();

        LibraryLoanDTO libraryLoanDTO2 = createLibraryLoanDTO().id(2L)
                .libraryBookDTOList(libraryBookDTOList2)
                .libraryUserDTO(libraryUserDTO2).build();

        //Create List Loan
        List<LibraryLoanDTO> libraryLoanDTOList = Arrays.asList(libraryLoanDTO1, libraryLoanDTO2);

        when(listLibraryLoanService.listLibraryLoan()).thenReturn(libraryLoanDTOList);

        MvcResult mvcResult = mockMvc.perform(get(URL_LOAN)
                .contentType(CONT_TYPE)
                .characterEncoding(UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String resultResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(objectMapper.writeValueAsString(libraryLoanDTOList), is(resultResponseBody));

        verify(listLibraryLoanService, times(1)).listLibraryLoan();
    }

    @Test
    @DisplayName("Pagina os emprestimos")
    void shouldPageLoan() throws Exception {
        //create Loan 1
        LibraryBookDTO libraryBookDTO1 = createLibraryBookDTO().build();
        LibraryBookDTO libraryBookDTO2 = createLibraryBookDTO().id(2L).build();
        List<LibraryBookDTO> libraryBookDTOList1 = Arrays.asList(libraryBookDTO1, libraryBookDTO2);

        LibraryUserDTO libraryUserDTO1 = createLibraryUserDTO().build();

        LibraryLoanDTO libraryLoanDTO1 = createLibraryLoanDTO()
                .libraryBookDTOList(libraryBookDTOList1)
                .libraryUserDTO(libraryUserDTO1).build();


        //create Loan 2
        LibraryBookDTO libraryBookDTO3 = createLibraryBookDTO().id(3L).build();
        LibraryBookDTO libraryBookDTO4 = createLibraryBookDTO().id(4L).build();
        List<LibraryBookDTO> libraryBookDTOList2 = Arrays.asList(libraryBookDTO3, libraryBookDTO4);

        LibraryUserDTO libraryUserDTO2 = createLibraryUserDTO().id(2L).build();

        LibraryLoanDTO libraryLoanDTO2 = createLibraryLoanDTO().id(2L)
                .libraryBookDTOList(libraryBookDTOList2)
                .libraryUserDTO(libraryUserDTO2).build();

        //Create Page Loan
        List<LibraryLoanDTO> libraryLoanDTOList = Arrays.asList(libraryLoanDTO1, libraryLoanDTO2);

        PageRequest pageRequest = PageRequest.of(1, 2);

        Page<LibraryLoanDTO> libraryLoanDTOPage = new PageImpl<>(libraryLoanDTOList, pageRequest, libraryLoanDTOList.size());

        when(pageLibraryLoanService.listPageLibraryLoan(anyInt(), anyInt())).thenReturn(libraryLoanDTOPage);

        MvcResult mvcResult = mockMvc.perform(get(URL_LOAN)
                .characterEncoding(UTF8)
                .contentType(CONT_TYPE)
                .param("page", "1")
                .param("size", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String resultResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(objectMapper.writeValueAsString(libraryLoanDTOPage), is(resultResponseBody));

        verify(pageLibraryLoanService, times(1)).listPageLibraryLoan(1, 2);
    }

    @Test
    @DisplayName("Salva um emprestimo")
    void shouldSaveLoan() throws Exception {
        LibraryLoanDTO libraryLoanDTO = createLibraryLoanDTO().id(null).build();

        mockMvc.perform(post(URL_LOAN)
                .contentType(CONT_TYPE)
                .characterEncoding(UTF8)
                .content(objectMapper.writeValueAsString(libraryLoanDTO))
                .param("idsBooks", "1, 2"))
                .andDo(print()).andExpect(status().isCreated());

        ArgumentCaptor<LibraryLoanDTO> captorLibraryLoanDTO = ArgumentCaptor.forClass(LibraryLoanDTO.class);
        ArgumentCaptor<List<Long>> captorIdsBooks = ArgumentCaptor.forClass(List.class);
        verify(saveLibraryLoanService, times(1)).saveLibraryLoan(captorLibraryLoanDTO.capture(), captorIdsBooks.capture());

        LibraryLoanDTO resultLoanDTO = captorLibraryLoanDTO.getValue();
        List<Long> resultIdsBooks = captorIdsBooks.getValue();

        assertAll("Loan controller",
                () -> assertThat(resultIdsBooks.size(), is(2)),
                () -> assertThat(resultIdsBooks.get(0), is(1L)),
                () -> assertThat(resultIdsBooks.get(1), is(2L)),

                () -> assertThat(resultLoanDTO.getSpecificIDUser(), is(libraryLoanDTO.getSpecificIDUser())),
                () -> assertThat(resultLoanDTO.getLoanTime(), is(libraryLoanDTO.getLoanTime()))
        );
    }

    @Test
    @DisplayName("Lança uma exceção pelo loan time")
    void shouldExceptionBadRequestOnLoanTime() throws Exception {
        LibraryLoanDTO libraryLoanDTO = createLibraryLoanDTO().id(null).loanTime("").build();

        mockMvc.perform(post(URL_LOAN)
                .contentType(CONT_TYPE)
                .characterEncoding(UTF8)
                .content(objectMapper.writeValueAsString(libraryLoanDTO))
                .param("idsBooks", "1, 2"))
                .andDo(print()).andExpect(status().isBadRequest());

        verify(saveLibraryLoanService, times(0)).saveLibraryLoan(any(LibraryLoanDTO.class), anyList());
    }
}
