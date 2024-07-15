package com.codefirstgirls.lesson9.controllers;

import com.codefirstgirls.lesson9.models.request.CreateBookRequest;
import com.codefirstgirls.lesson9.models.database.Book;
import com.codefirstgirls.lesson9.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    /**
     * Retrieves all books.
     * @return a response containing a list of all books
     */
    @GetMapping(value = "/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookRepository.findAll());
    }

    /**
     * Retrieves a specified book by its unique id.
     * @param id the id of the book to retrieve
     * @return a response containing the book with that id, or a Not Found response if no such book exists
     */
    @GetMapping(value = "/books/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        Optional<Book> matchingBook = bookRepository.findById(id);
        if (matchingBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(matchingBook.get());
        }
    }

    /**
     * Creates a new book.
     * @param createBookRequest details of the book (namely the book's title and author)
     * @return a response containing a URI for the created book
     */
    @PostMapping(value = "/books")
    public ResponseEntity createBook(@RequestBody CreateBookRequest createBookRequest) {
        Book bookToCreate = new Book(createBookRequest.getTitle(), createBookRequest.getAuthor());
        Book createdBook = bookRepository.save(bookToCreate);
        UriComponents uriComponents = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/books/" + createdBook.getId())
                .build();
        return ResponseEntity.created(uriComponents.toUri()).build();
    }
}
