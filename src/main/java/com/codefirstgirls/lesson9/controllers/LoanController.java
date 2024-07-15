package com.codefirstgirls.lesson9.controllers;

import com.codefirstgirls.lesson9.constants.LoanConstants;
import com.codefirstgirls.lesson9.enums.LoanStatus;
import com.codefirstgirls.lesson9.models.database.Book;
import com.codefirstgirls.lesson9.models.database.Copy;
import com.codefirstgirls.lesson9.models.database.Customer;
import com.codefirstgirls.lesson9.models.database.Loan;
import com.codefirstgirls.lesson9.models.request.CreateLoanRequest;
import com.codefirstgirls.lesson9.models.response.ErrorResponse;
import com.codefirstgirls.lesson9.repositories.BookRepository;
import com.codefirstgirls.lesson9.repositories.CustomerRepository;
import com.codefirstgirls.lesson9.repositories.LoanRepository;
import com.codefirstgirls.lesson9.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class LoanController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CopyUtil copyUtil;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanRepository loanRepository;

    /**
     * Retrieves all active loans (including those that are overdue).
     * @return a response containing a list of all active (including overdue) loans
     */
    @GetMapping(value = "/loans/active")
    public ResponseEntity<List<Loan>> getAllActiveLoans() {
        return ResponseEntity.ok(loanRepository.findByDateReturnedIsNull());
    }

    /**
     * Retrieves all overdue loans.
     * @return a response containing a list of all overdue loans
     */
    @GetMapping(value = "/loans/overdue")
    public ResponseEntity<List<Loan>> getAllOverdueLoans() {
        return ResponseEntity.ok(loanRepository.findByDateDueBeforeAndDateReturnedIsNull(LocalDate.now()));
    }

    /**
     * Retrieves a specified loan by its unique id.
     * @param id the id of the loan to retrieve
     * @return a response containing the loan with that id, or a Not Found response if no such loan exists
     */
    @GetMapping(value = "/loans/{id}")
    public ResponseEntity<Loan> getLoan(@PathVariable Long id) {
        Optional<Loan> matchingLoan = loanRepository.findById(id);
        if (matchingLoan.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(matchingLoan.get());
    }

    /**
     * Creates a new loan of a specified book to a specified customer.
     * @param createLoanRequest details of the book and customer the loan should be linked to
     * @return a response containing a URI for the created loan if successful, or details of any errors otherwise
     */
    @PostMapping(value = "/loans")
    public ResponseEntity createLoan(@RequestBody CreateLoanRequest createLoanRequest) {
        ErrorResponse errorResponse = new ErrorResponse();
        Optional<Book> matchingBook = bookRepository.findById(createLoanRequest.getBookId());
        if (matchingBook.isEmpty()) {
            errorResponse.getErrors().add("The specified book does not exist in the library");
        }
        Optional<Copy> availableCopy = copyUtil.findBestAvailableCopy(matchingBook.get());
        if (availableCopy.isEmpty()) {
            errorResponse.getErrors().add("No copies of the specified book are currently available for loan");
        }
        Optional<Customer> matchingCustomer = customerRepository.findById(createLoanRequest.getCustomerId());
        if (matchingCustomer.isEmpty()) {
            errorResponse.getErrors().add("The specified customer does not exist in the library");
        }
        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(errorResponse);
        }
        Loan loanToCreate = new Loan(availableCopy.get(), matchingCustomer.get());
        Loan createdLoan = loanRepository.save(loanToCreate);
        UriComponents uriComponents = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/loans/" + createdLoan.getId())
                .build();
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    /**
     * Renews a specified loan given its unique id.
     * @param id the id of the loan to renew
     * @return an empty response if successful, or details of any errors otherwise
     */
    @PatchMapping(value = "/loans/{id}/renew")
    public ResponseEntity renewLoan(@PathVariable Long id) {
        ErrorResponse errorResponse = new ErrorResponse();
        Optional<Loan> matchingLoan = loanRepository.findById(id);
        if (matchingLoan.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if (matchingLoan.get().getStatus() == LoanStatus.COMPLETED) {
            errorResponse.getErrors().add("This loan has already been completed");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        matchingLoan.get().setDateDue(LocalDate.now().plusWeeks(LoanConstants.LOAN_DURATION_IN_WEEKS));
        return ResponseEntity.noContent().build();
    }

    /**
     * Completes a specified loan given its unique id.
     * @param id the id of the loan to complete
     * @return an empty response if successful, or details of any errors otherwise
     */
    @PatchMapping(value = "/loans/{id}/complete")
    public ResponseEntity completeLoan(@PathVariable Long id) {
        ErrorResponse errorResponse = new ErrorResponse();
        Optional<Loan> matchingLoan = loanRepository.findById(id);
        if (matchingLoan.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if (matchingLoan.get().getStatus() == LoanStatus.COMPLETED) {
            errorResponse.getErrors().add("This loan has already been completed");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        matchingLoan.get().setDateReturned(LocalDate.now());
        return ResponseEntity.noContent().build();
    }
}
