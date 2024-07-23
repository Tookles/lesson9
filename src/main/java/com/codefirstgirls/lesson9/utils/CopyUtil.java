package com.codefirstgirls.lesson9.utils;

import com.codefirstgirls.lesson9.enums.LoanStatus;
import com.codefirstgirls.lesson9.models.database.Book;
import com.codefirstgirls.lesson9.models.database.Copy;
import com.codefirstgirls.lesson9.repositories.CopyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class CopyUtil {
    @Autowired
    private CopyRepository copyRepository;

    /**
     * Finds an available copy of the given book in the best possible condition.
     * @param book the book to find a copy of
     * @return an optimal-condition copy of the book that is not currently on loan
     */
    public Optional<Copy> findBestAvailableCopy(Book book) {
        List<Copy> matchingCopies = copyRepository.findByBook(book);
        return matchingCopies
                .stream()
                .filter(copy -> copy.getLoans()
                        .stream()
                        .allMatch(loan -> loan.getStatus() == LoanStatus.COMPLETED))
                .max(Comparator.comparing(Copy::getCondition));
    }
}
