package com.codefirstgirls.lesson9.repositories;

import com.codefirstgirls.lesson9.models.database.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface BookRepository extends JpaRepository<Book, Long> {
}
