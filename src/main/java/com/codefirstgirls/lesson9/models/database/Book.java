package com.codefirstgirls.lesson9.models.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;

    @OneToMany(mappedBy = "book")
    @JsonIgnoreProperties("book")
    List<Copy> copies = List.of();

    public Book(String title, String author)
    {
        this.title = title;
        this.author = author;
    }
}
