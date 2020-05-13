package br.com.phoebus.microservice.biblioteca.loan.libraryloan;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryLoanRepository extends JpaRepository<LibraryLoan, Long> {
}
