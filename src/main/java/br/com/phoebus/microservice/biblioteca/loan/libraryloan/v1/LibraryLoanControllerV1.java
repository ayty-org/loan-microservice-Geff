package br.com.phoebus.microservice.biblioteca.loan.libraryloan.v1;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryLoanDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.DeleteLibraryLoanService;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.EditLibraryLoanService;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.GetLibraryLoanService;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.ListLibraryLoanService;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.ListPageLibraryLoanService;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.service.SaveLibraryLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/libraryLoan")
public class LibraryLoanControllerV1 {
    private final DeleteLibraryLoanService deleteLibraryLoanService;
    private final EditLibraryLoanService editLibraryLoanService;
    private final GetLibraryLoanService getLibraryLoanService;
    private final ListLibraryLoanService listLibraryLoanService;
    private final ListPageLibraryLoanService listPageLibraryLoanService;
    private final SaveLibraryLoanService saveLibraryLoanService;

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteLibraryLoanForId(@PathVariable(value = "id") Long id) {
        deleteLibraryLoanService.deleteLibraryLoan(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void editLibraryLoan(@PathVariable(value = "id") Long id,
                         @RequestBody LibraryLoanDTO libraryLoanDTO,
                         @RequestParam List<Long> idsBooks) {
        System.out.println(id);
        editLibraryLoanService.editLibraryLoan(id, libraryLoanDTO, idsBooks);
    }

    @GetMapping(value = "/{id}")
    LibraryLoanDTO getLibraryLoan(@PathVariable(value = "id") Long id) {
        return getLibraryLoanService.getLibraryLoanForID(id);
    }

    @GetMapping
    List<LibraryLoanDTO> listLibraryLoan() {
        return listLibraryLoanService.listLibraryLoan();
    }

    @GetMapping(params = {"page", "size"})
    Page<LibraryLoanDTO> listOnPageLibraryLoan(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        return listPageLibraryLoanService.listPageLibraryLoan(page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void saveLibraryLoan(@Valid @RequestBody LibraryLoanDTO libraryLoanDTO, @RequestParam("idsBooks") List<Long> idsBooks) {
        saveLibraryLoanService.saveLibraryBook(libraryLoanDTO, idsBooks);
    }
}
