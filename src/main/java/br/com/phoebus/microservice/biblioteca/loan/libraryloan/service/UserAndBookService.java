package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryUserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "userandbook")
public interface UserAndBookService {

    @GetMapping(value = "v1/libraryUser/{id}")
    LibraryUserDTO findUserOfLoan(@PathVariable("id") Long id);

}
