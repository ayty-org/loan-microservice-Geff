package br.com.phoebus.microservice.biblioteca.loan.libraryloan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder")
public class LibraryUserDTO {

    private Long id;

    @NotEmpty(message = "Name may is not be empty")
    @Size(min = 2)
    private String name;

    @Min(3)
    private int age;

    @NotEmpty(message = "Telephone may not be empty")
    @Size(min = 8)
    private String telephone;

}