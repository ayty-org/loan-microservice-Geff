package br.com.phoebus.microservice.biblioteca.loan.libraryloan;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userandbook")
public interface UserAndBook {

    @GetMapping(value = "v1/libraryUser/{id}")
    LibraryUserDTO findUserOfLoan(@PathVariable("id") Long id);

}
