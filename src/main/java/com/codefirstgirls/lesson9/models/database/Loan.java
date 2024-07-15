package com.codefirstgirls.lesson9.models.database;

import com.codefirstgirls.lesson9.constants.LoanConstants;
import com.codefirstgirls.lesson9.enums.LoanStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "copyId")
    @JsonIgnoreProperties("loans")
    private Copy copy;

    @ManyToOne
    @JoinColumn(name = "customerId")
    @JsonIgnoreProperties("loans")
    private Customer customer;

    private LocalDate dateLoaned;
    private LocalDate dateDue;
    private LocalDate dateReturned;

    public Loan(Copy copy, Customer customer)
    {
        this.copy = copy;
        this.customer = customer;
        this.dateLoaned = LocalDate.now();
        this.dateDue = LocalDate.now().plusWeeks(LoanConstants.LOAN_DURATION_IN_WEEKS);
    }

    @JsonProperty("status")
    public LoanStatus getStatus() {
        if (dateReturned != null) {
            return LoanStatus.COMPLETED;
        } else if (dateDue.isBefore(LocalDate.now())) {
            return LoanStatus.OVERDUE;
        } else {
            return LoanStatus.ACTIVE;
        }
    }
}
