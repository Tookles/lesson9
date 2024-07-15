package com.codefirstgirls.lesson9.repositories;

import com.codefirstgirls.lesson9.models.database.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByDateReturnedIsNull();
    List<Loan> findByDateDueBeforeAndDateReturnedIsNull(LocalDate date);
}
