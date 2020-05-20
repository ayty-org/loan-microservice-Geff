package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryBookDTO;
import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "userandbook")
public interface UserAndBookService {

    @GetMapping(value = "v1/libraryUser/{id}")
    LibraryUserDTO findUserOfLoan(@PathVariable("id") Long id);

    @PutMapping(value = "v1/libraryUser/editUserSpecific/{id}")
    void editUserSpecifId(@PathVariable("id") Long id, @RequestBody String specificIDLoan);

    @GetMapping(value = "v1/libraryBook/getAllLoanBook/{id}")
    List<LibraryBookDTO> findAllBookOfLoan(@PathVariable("id") Long id);

    @GetMapping(value = "v1/libraryBook/verifyBooks", params = "idsBooks")
    void verifyBooks(@RequestParam("idsBooks") List<Long> idsBooks);

    @PutMapping(value = "v1/libraryBook/changeStatus/{id}", params = "idsBooks")
    void changeStatus(@PathVariable("id") Long id, @RequestParam("idsBooks") List<Long> idsBooks);

}
