package br.com.phoebus.microservice.biblioteca.loan.loan.builders;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryBookDTO;

public class LibraryBookDTOBuilder {

    public static LibraryBookDTO.Builder createLibraryBookDTO() {
        return LibraryBookDTO.builder()
                .id(1L)
                .author("Geff author")
                .borrowed(false)
                .isbn("ISBN")
                .resume("Resume")
                .specificIDLoan(null)
                .title("title")
                .year(2020);
    }
}