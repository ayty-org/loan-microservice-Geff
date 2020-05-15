package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "userandbook")
public interface UserAndBookService {

    @GetMapping(value = "v1/libraryUser/{id}")
    LibraryUserDTO findUserOfLoan(@PathVariable("id") Long id);

    @PutMapping(value = "v1/libraryUser/editSpecific/{id}")
    void editSpecifId(@PathVariable("id") Long id, String specificId);

}
