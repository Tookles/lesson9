package com.codefirstgirls.lesson9.models.database;

import com.codefirstgirls.lesson9.enums.CopyCondition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "copies")
@Data
@NoArgsConstructor
public class Copy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bookId")
    @JsonIgnoreProperties("copies")
    private Book book;

    private CopyCondition condition;

    @OneToMany(mappedBy = "copy")
    @JsonIgnoreProperties("copy")
    private List<Loan> loans = List.of();

    public Copy(Book book, CopyCondition condition)
    {
        this.book = book;
        this.condition = condition;
    }
}
