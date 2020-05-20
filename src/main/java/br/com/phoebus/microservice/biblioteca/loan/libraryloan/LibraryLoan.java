package br.com.phoebus.microservice.biblioteca.loan.libraryloan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "library_loan")
@Builder(builderClassName = "Builder")
public class LibraryLoan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "loan_id")
    private Long id;

    private String loanTime;

    private Long SpecificIDUser;

    public static LibraryLoan to(LibraryLoanDTO libraryLoanDTO) {
        return LibraryLoan.builder()
                .id(libraryLoanDTO.getId())
                .loanTime(libraryLoanDTO.getLoanTime())
                .SpecificIDUser(libraryLoanDTO.getSpecificIDUser())
                .build();
    }

    public static LibraryLoan to(LibraryLoanEditDTO libraryLoanDTO) {
        return LibraryLoan.builder()
                .id(libraryLoanDTO.getId())
                .loanTime(libraryLoanDTO.getLoanTime())
                .SpecificIDUser(libraryLoanDTO.getSpecificIDUser())
                .build();
    }

    public String getSpecificIdLoan () { //metodo para usar após a conversa com kawe sobre specific id
        return "000"+getId();
    }
}
