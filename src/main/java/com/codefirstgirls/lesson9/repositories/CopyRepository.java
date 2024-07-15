package com.codefirstgirls.lesson9.repositories;

import com.codefirstgirls.lesson9.models.database.Book;
import com.codefirstgirls.lesson9.models.database.Copy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CopyRepository extends JpaRepository<Copy, Long> {
    List<Copy> findByBook(Book book);
}
