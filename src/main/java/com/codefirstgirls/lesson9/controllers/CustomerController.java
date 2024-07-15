package com.codefirstgirls.lesson9.controllers;

import com.codefirstgirls.lesson9.models.database.Customer;
import com.codefirstgirls.lesson9.models.request.CreateCustomerRequest;
import com.codefirstgirls.lesson9.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Retrieves all customers.
     * @return a response containing a list of all customers
     */
    @GetMapping(value = "/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerRepository.findAll());
    }

    /**
     * Retrieves a specified customer by their unique id.
     * @param id the id of the customer to retrieve
     * @return a response containing the customer with that id, or a Not Found response if no such customer exists
     */
    @GetMapping(value = "/customers/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        Optional<Customer> matchingCustomer = customerRepository.findById(id);
        if (matchingCustomer.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(matchingCustomer.get());
        }
    }

    /**
     * Creates a new customer.
     * @param createCustomerRequest details of the customer (namely the customer's name)
     * @return a response containing a URI for the created customer
     */
    @PostMapping(value = "/customers")
    public ResponseEntity createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
        Customer customerToCreate = new Customer(createCustomerRequest.getName());
        Customer createdCustomer = customerRepository.save(customerToCreate);
        UriComponents uriComponents = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/customers/" + createdCustomer.getId())
                .build();
        return ResponseEntity.created(uriComponents.toUri()).build();
    }
}
