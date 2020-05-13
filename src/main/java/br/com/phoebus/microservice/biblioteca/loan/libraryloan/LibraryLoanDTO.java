package br.com.phoebus.microservice.biblioteca.loan.libraryloan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder")
public class LibraryLoanDTO {

    private Long id;

    @NotEmpty(message = "Loam time may is not be empty")
    private String loanTime;

    private LibraryUserDTO libraryUserDTO;

}
