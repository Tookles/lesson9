package com.codefirstgirls.lesson9.utils;

import com.codefirstgirls.lesson9.enums.CopyCondition;
import com.codefirstgirls.lesson9.models.database.Book;
import com.codefirstgirls.lesson9.models.database.Copy;
import com.codefirstgirls.lesson9.models.database.Customer;
import com.codefirstgirls.lesson9.models.database.Loan;
import com.codefirstgirls.lesson9.repositories.CopyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CopyUtilTests {
    @Mock
    private CopyRepository copyRepository;

    @InjectMocks
    private CopyUtil copyUtil;

    @Test
    public void findBestAvailableCopy_calledWithBookWithOneAvailableCopy_returnsThatCopy() {
        // Arrange
        Book bookWithOneAvailableCopy = new Book("Cool Book 1", "Fun Author 1");

        Copy unavailableCopy = new Copy(bookWithOneAvailableCopy, CopyCondition.EXCELLENT);
        Customer customerBorrowingUnavailableCopy = new Customer("Friendly Customer 1");
        Loan activeLoanForUnavailableCopy = new Loan(unavailableCopy, customerBorrowingUnavailableCopy);
        unavailableCopy.setLoans(List.of(activeLoanForUnavailableCopy));

        Copy availableCopy = new Copy(bookWithOneAvailableCopy, CopyCondition.GOOD);

        when(copyRepository.findByBook(bookWithOneAvailableCopy)).thenReturn(List.of(unavailableCopy, availableCopy));

        // Act
        Optional<Copy> bestAvailableCopy = copyUtil.findBestAvailableCopy(bookWithOneAvailableCopy);

        // Assert
        assertThat(bestAvailableCopy).isPresent();
        assertThat(bestAvailableCopy.get()).isEqualTo(availableCopy);
    }

    @Test
    public void findBestAvailableCopy_calledWithBookWithMultipleAvailableCopies_returnsCopyInBestCondition() {
        // Arrange
        Book bookWithMultipleAvailableCopies = new Book("Very Old Book 4", "Unknown");

        Copy unavailableCopy = new Copy(bookWithMultipleAvailableCopies, CopyCondition.GOOD);
        Customer customerBorrowingUnavailableCopy = new Customer("Studious Customer 3");
        Loan activeLoanForUnavailableCopy = new Loan(unavailableCopy, customerBorrowingUnavailableCopy);
        unavailableCopy.setLoans(List.of(activeLoanForUnavailableCopy));

        Copy okConditionAvailableCopy = new Copy(bookWithMultipleAvailableCopies, CopyCondition.OK);
        Copy poorConditionAvailableCopy = new Copy(bookWithMultipleAvailableCopies, CopyCondition.POOR);

        when(copyRepository.findByBook(bookWithMultipleAvailableCopies)).thenReturn(
                List.of(unavailableCopy, okConditionAvailableCopy, poorConditionAvailableCopy));

        // Act
        Optional<Copy> bestAvailableCopy = copyUtil.findBestAvailableCopy(bookWithMultipleAvailableCopies);

        // Assert
        assertThat(bestAvailableCopy).isPresent();
        assertThat(bestAvailableCopy.get()).isEqualTo(okConditionAvailableCopy);
    }

    @Test
    public void findBestAvailableCopy_calledWithBookWithNoAvailableCopies_returnsEmpty() {
        // Arrange
        Book bookWithNoAvailableCopies = new Book("Weird Book 2", "Eccentric Author 2");

        Copy unavailableCopy = new Copy(bookWithNoAvailableCopies, CopyCondition.OK);
        Customer customerBorrowingUnavailableCopy = new Customer("Notorious Customer 2");
        Loan activeLoanForUnavailableCopy = new Loan(unavailableCopy, customerBorrowingUnavailableCopy);
        unavailableCopy.setLoans(List.of(activeLoanForUnavailableCopy));

        when(copyRepository.findByBook(bookWithNoAvailableCopies)).thenReturn(List.of(unavailableCopy));

        // Act
        Optional<Copy> bestAvailableCopy = copyUtil.findBestAvailableCopy(bookWithNoAvailableCopies);

        // Assert
        assertThat(bestAvailableCopy).isEmpty();
    }
}
