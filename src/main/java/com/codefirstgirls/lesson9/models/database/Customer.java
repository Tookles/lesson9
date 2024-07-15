package com.codefirstgirls.lesson9.models.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "customer")
    @JsonIgnoreProperties("customer")
    private List<Loan> loans = List.of();

    public Customer(String name) {
        this.name = name;
    }
}
