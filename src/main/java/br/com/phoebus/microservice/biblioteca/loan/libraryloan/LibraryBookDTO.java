package br.com.phoebus.microservice.biblioteca.loan.libraryloan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@Builder(builderClassName = "Builder")
public class LibraryBookDTO {

    private Long id;

    @NotNull
    private boolean borrowed;

    @NotEmpty(message = "Title may not be empty")
    private String title;

    @NotEmpty(message = "Resume may not be empty")
    @Size(max = 500)
    private String resume;

    @NotEmpty(message = "ISBN may not be empty")
    private String isbn;

    @NotEmpty(message = "Author may not be empty")
    private String author;

    @Min(1300)
    private int year;

    private Long specificIDLoan;

}