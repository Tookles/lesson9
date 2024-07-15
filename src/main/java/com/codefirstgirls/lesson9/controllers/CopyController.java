package com.codefirstgirls.lesson9.controllers;

import com.codefirstgirls.lesson9.models.request.CreateCopyRequest;
import com.codefirstgirls.lesson9.models.database.Book;
import com.codefirstgirls.lesson9.models.database.Copy;
import com.codefirstgirls.lesson9.models.response.ErrorResponse;
import com.codefirstgirls.lesson9.repositories.BookRepository;
import com.codefirstgirls.lesson9.repositories.CopyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.util.Optional;

@RestController
public class CopyController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CopyRepository copyRepository;

    /**
     * Retrieves a specified copy by its unique id.
     * @param id the id of the copy to retrieve
     * @return a response containing the copy with that id, or a Not Found response if no such copy exists
     */
    @GetMapping(value = "/copies/{id}")
    public ResponseEntity<Copy> getCopy(@PathVariable Long id) {
        Optional<Copy> matchingCopy = copyRepository.findById(id);
        if (matchingCopy.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(matchingCopy.get());
    }

    /**
     * Creates a new copy of a specified book.
     * @param createCopyRequest details of the copy (namely the book it is a copy of and the condition of the copy)
     * @return a response containing a URI for the created copy if successful, or details of any errors otherwise
     */
    @PostMapping(value = "/copies")
    public ResponseEntity createCopy(@RequestBody CreateCopyRequest createCopyRequest) {
        ErrorResponse errorResponse = new ErrorResponse();
        Optional<Book> matchingBook = bookRepository.findById(createCopyRequest.getBookId());
        if (matchingBook.isEmpty()) {
            errorResponse.getErrors().add("The specified book does not exist in the library");
            return ResponseEntity.unprocessableEntity().body(errorResponse);
        }
        Copy copyToCreate = new Copy(matchingBook.get(), createCopyRequest.getCondition());
        Copy createdCopy = copyRepository.save(copyToCreate);
        UriComponents uriComponents = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/copies/" + createdCopy.getId())
                .build();
        return ResponseEntity.created(uriComponents.toUri()).build();
    }
}
