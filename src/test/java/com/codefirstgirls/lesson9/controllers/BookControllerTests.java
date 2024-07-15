package com.codefirstgirls.lesson9.controllers;

import com.codefirstgirls.lesson9.models.database.Book;
import com.codefirstgirls.lesson9.repositories.BookRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BookControllerTests {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setUpMockBookController() {
        RestAssuredMockMvc.standaloneSetup(bookController);
    }

    @Test
    public void getAllBooks_called_returnsAllBooks() {
        // Arrange
        Book coolBook1 = new Book("Cool Book 1", "Fun Author 1");
        Book veryOldBook4 = new Book("Very Old Book 4", "Unknown");
        List<Book> books = List.of(coolBook1, veryOldBook4);
        when(bookRepository.findAll()).thenReturn(books);

        RestAssuredMockMvc
                // Act
                .when()
                        .get("/books")
                // Assert
                .then()
                        .statusCode(HttpStatus.OK.value())
                        .body("$.size()", equalTo(books.size()))
                        .body("[0].title", equalTo(coolBook1.getTitle()))
                        .body("[0].author", equalTo(coolBook1.getAuthor()))
                        .body("[1].title", equalTo(veryOldBook4.getTitle()))
                        .body("[1].author", equalTo(veryOldBook4.getAuthor()));
    }

    @Test
    public void getBook_calledWithPresentId_returnsCorrespondingBook() {
        // Arrange
        Book coolBook1 = new Book("Cool Book 1", "Fun Author 1");
        Long coolBook1Id = 1L;
        coolBook1.setId(coolBook1Id);
        when(bookRepository.findById(coolBook1Id)).thenReturn(Optional.of(coolBook1));

        RestAssuredMockMvc
                // Act
                .when()
                        .get("/books/" + coolBook1Id)
                // Assert
                .then()
                        .statusCode(HttpStatus.OK.value())
                        .body("title", equalTo(coolBook1.getTitle()))
                        .body("author", equalTo(coolBook1.getAuthor()));
    }

    @Test
    public void getBook_calledWithAbsentId_returnsNotFound() {
        // Arrange
        Long absentId = 5L;
        when(bookRepository.findById(absentId)).thenReturn(Optional.empty());

        RestAssuredMockMvc
                // Act
                .when()
                        .get("/books/" + absentId)
                // Assert
                .then()
                        .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void createBook_calledWithTitleAndAuthor_returnsUriForCreatedBook() {
        // Arrange
        Book bookToCreate = new Book("Funny Book 5", "Witty Author 3");
        Book createdBook = new Book(bookToCreate.getTitle(), bookToCreate.getAuthor());
        createdBook.setId(5L);
        when(bookRepository.save(bookToCreate)).thenReturn(createdBook);

        RestAssuredMockMvc
                // Arrange (continued)
                .given()
                        .contentType("application/json")
                        .body(bookToCreate)
                // Act
                .when()
                        .post("/books")
                // Assert
                .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .header("Location", endsWith("/books/" + createdBook.getId()));
    }

    @Test
    public void createBook_calledWithoutTitleAndAuthor_returnsBadRequest() {
        RestAssuredMockMvc
                // Act
                .when()
                        .post("/books")
                // Assert
                .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
