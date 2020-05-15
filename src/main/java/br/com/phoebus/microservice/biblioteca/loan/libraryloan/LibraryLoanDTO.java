package br.com.phoebus.microservice.biblioteca.loan.libraryloan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

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

    @NotNull
    private Long SpecificIDUser;

    public static LibraryLoanDTO from(LibraryLoan libraryLoan) {
        return LibraryLoanDTO.builder()
                .id(libraryLoan.getId())
                .loanTime(libraryLoan.getLoanTime())
                .SpecificIDUser(libraryLoan.getSpecificIDUser())
                .build();
    }

    public static List<LibraryLoanDTO> from(List<LibraryLoan> libraryLoanList) {
        return libraryLoanList.stream().map(LibraryLoanDTO::from).collect(Collectors.toList());
    }

    public static Page<LibraryLoanDTO> from(Page<LibraryLoan> libraryLoanPage) {
        return libraryLoanPage.map(LibraryLoanDTO::from);
    }
}
