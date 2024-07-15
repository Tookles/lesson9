package com.codefirstgirls.lesson9.repositories;

import com.codefirstgirls.lesson9.models.database.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
